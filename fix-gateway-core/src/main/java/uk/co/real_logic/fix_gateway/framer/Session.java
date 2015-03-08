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
package uk.co.real_logic.fix_gateway.framer;

import uk.co.real_logic.fix_gateway.util.MilliClock;

/**
 * Stores information about the current state of a session - no matter whether outbound or inbound
 */
public class Session
{
    public static final long UNKNOWN = -1;

    protected final MilliClock clock;
    protected final SessionProxy proxy;

    private long heartbeatInterval;
    private long nextRequiredMessageTime;
    private long connectionId;
    private long sequenceNumber;
    private SessionState state;
    private long id = UNKNOWN;
    private int lastMsgSeqNum = 0;

    public Session(
            final long heartbeatInterval,
            final MilliClock clock,
            final long connectionId,
            final long sequenceNumber,
            final SessionState state,
            final SessionProxy proxy)
    {
        this.heartbeatInterval = heartbeatInterval;
        this.clock = clock;
        this.proxy = proxy;
        this.nextRequiredMessageTime = clock.time() + heartbeatInterval;
        this.connectionId = connectionId;
        this.sequenceNumber = sequenceNumber;
        this.state = state;
    }

    public long heartbeatInterval()
    {
        return this.heartbeatInterval;
    }

    public long nextRequiredMessageTime()
    {
        return this.nextRequiredMessageTime;
    }

    public long connectionId()
    {
        return this.connectionId;
    }

    public long sequenceNumber()
    {
        return this.sequenceNumber;
    }

    public SessionState state()
    {
        return this.state;
    }

    public Session heartbeatInterval(final long heartbeatInterval)
    {
        this.heartbeatInterval = heartbeatInterval;
        return this;
    }

    public Session nextRequiredMessageTime(final long nextRequiredMessageTime)
    {
        this.nextRequiredMessageTime = nextRequiredMessageTime;
        return this;
    }

    public Session connectionId(final long connectionId)
    {
        this.connectionId = connectionId;
        return this;
    }

    public Session sequenceNumber(final long sequenceNumber)
    {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public Session state(final SessionState state)
    {
        this.state = state;
        return this;
    }

    public long id()
    {
        return id;
    }

    public Session id(final long id)
    {
        this.id = id;
        return this;
    }

    public int lastMsgSeqNum()
    {
        return lastMsgSeqNum;
    }

    public Session lastMsgSeqNum(final int lastMsgSeqNum)
    {
        this.lastMsgSeqNum = lastMsgSeqNum;
        return this;
    }
}
