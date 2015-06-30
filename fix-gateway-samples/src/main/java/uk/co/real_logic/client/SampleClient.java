/*
 * Copyright 2015 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.client;

import uk.co.real_logic.aeron.Subscription;
import uk.co.real_logic.fix_gateway.FixGateway;
import uk.co.real_logic.fix_gateway.SessionConfiguration;
import uk.co.real_logic.fix_gateway.StaticConfiguration;
import uk.co.real_logic.fix_gateway.builder.TestRequestEncoder;
import uk.co.real_logic.fix_gateway.replication.DataSubscriber;
import uk.co.real_logic.fix_gateway.session.InitiatorSession;
import uk.co.real_logic.fix_gateway.session.Session;

import static uk.co.real_logic.server.SampleServer.ACCEPTOR_COMP_ID;
import static uk.co.real_logic.server.SampleServer.INITIATOR_COMP_ID;

public final class SampleClient
{
    private static Subscription subscription;

    public static void main(final String[] args) throws Exception
    {
        // Static configuration lasts the duration of a FIX-Gateway instance
        final StaticConfiguration configuration = new StaticConfiguration()
            .aeronChannel("udp://localhost:10002")
            .bind("localhost", 10001)
            .newSessionHandler(SampleClient::onConnect);

        try (final FixGateway gateway = FixGateway.launch(configuration))
        {
            // Each outbound session with an Exchange or broker is represented by
            // a Session object. Each session object can be configured with connection
            // details and credentials.
            final SessionConfiguration sessionConfig = SessionConfiguration.builder()
                .address("localhost", 9999)
                .targetCompId(ACCEPTOR_COMP_ID)
                .senderCompId(INITIATOR_COMP_ID)
                .build();

            final InitiatorSession session = gateway.initiate(sessionConfig);

            final TestReqIdFinder testReqIdFinder = new TestReqIdFinder();
            final DataSubscriber subscriber = new DataSubscriber(testReqIdFinder);

            final TestRequestEncoder testRequest = new TestRequestEncoder();
            testRequest.testReqID("Hello World");

            session.send(testRequest);

            while (!"Hello World".equals(testReqIdFinder.testReqId()))
            {
                subscription.poll(subscriber, 1);
                Thread.sleep(1000);
            }

            System.out.println("Success, received reply!");
            System.out.println(testReqIdFinder.testReqId());

            session.startLogout();
            session.disconnect();
        }
    }

    private static void onConnect(final Session session, final Subscription subscription)
    {
        SampleClient.subscription = subscription;
    }
}