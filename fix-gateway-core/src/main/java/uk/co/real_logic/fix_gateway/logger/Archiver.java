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
package uk.co.real_logic.fix_gateway.logger;

import uk.co.real_logic.aeron.Subscription;
import uk.co.real_logic.aeron.common.concurrent.logbuffer.Header;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.IoUtil;
import uk.co.real_logic.agrona.MutableDirectBuffer;
import uk.co.real_logic.agrona.concurrent.Agent;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;
import uk.co.real_logic.fix_gateway.replication.ReplicationStreams;

import java.nio.MappedByteBuffer;

// TODO: rework this to be a simple buffer copier
public class Archiver implements Agent
{
    private static final int FRAGMENT_LIMIT = 10;

    private final Subscription subscription;

    private MappedByteBuffer currentMappedBuffer;
    private MutableDirectBuffer currentBuffer;
    private int currentIndex;
    private int currentId;

    public Archiver(final int initialId, final ReplicationStreams streams)
    {
        this.subscription = streams.dataSubscription(this::onData);
        nextBuffer(initialId);
    }

    private void onData(final DirectBuffer buffer, final int offset, final int length, final Header header)
    {
        /*if (currentIndex + length > LOG_FILE_SIZE)
        {
            nextBuffer(currentId + 1);
        }*/

        copyData(buffer, offset, length);
    }

    private void nextBuffer(final int newId)
    {
        currentId = newId;
        if (currentMappedBuffer != null)
        {
            IoUtil.unmap(currentMappedBuffer);
        }
        currentMappedBuffer = null; //IoUtil.mapNewFile(LogDirectoryDescriptor.logFile(newId), LOG_FILE_SIZE);
        currentBuffer = new UnsafeBuffer(currentMappedBuffer);
        currentIndex = 0;
    }

    private void copyData(final DirectBuffer srcBuffer, final int srcOffset, final int srcLength)
    {
        currentBuffer.putBytes(currentIndex, srcBuffer, srcOffset, srcLength);
        currentIndex += srcLength;
    }

    public int doWork() throws Exception
    {
        return subscription.poll(FRAGMENT_LIMIT);
    }

    public String roleName()
    {
        return "Archiver";
    }

    public void onClose()
    {
        subscription.close();
    }
}