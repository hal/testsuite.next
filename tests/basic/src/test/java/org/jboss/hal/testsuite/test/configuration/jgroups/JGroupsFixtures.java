/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
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
package org.jboss.hal.testsuite.test.configuration.jgroups;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public final class JGroupsFixtures {

    public static final String DEFAULT_CHANNEL = "default-channel";
    public static final String TCP = "tcp";
    public static final String JGROUPS_TCP = "jgroups-tcp";
    public static final String CLUSTER = "cluster";
    public static final String SITE = "site";
    public static final String KEEPALIVE_TIME = "keepalive-time";
    public static final String REMOTE_SITE = "remote-site";

    public static final Address SUBSYSTEM_ADDRESS = Address.subsystem(JGROUPS);

    // ------------------------------------------------------ stack

    public static final String STACK_CREATE = Ids.build(STACK, CrudConstants.CREATE, Random.name());
    public static final String STACK_UPDATE = Ids.build(STACK, CrudConstants.UPDATE, Random.name());
    public static final String STACK_DELETE = Ids.build(STACK, CrudConstants.DELETE, Random.name());

    public static Address stackAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(STACK, name);
    }

    // ------------------------------------------------------ stack / relay

    public static Address relayAddress(String stack) {
        return stackAddress(stack).and(RELAY, RELAY.toUpperCase());
    }

    // ------------------------------------------------------ stack / remote site

    public static final String REMOTESITE_CREATE = Ids.build(REMOTE_SITE, CrudConstants.CREATE, Random.name());
    public static final String REMOTESITE_UPDATE = Ids.build(REMOTE_SITE, CrudConstants.UPDATE, Random.name());
    public static final String REMOTESITE_DELETE = Ids.build(REMOTE_SITE, CrudConstants.DELETE, Random.name());

    public static Address relayRemoteSiteAddress(String stack, String remoteSite) {
        return relayAddress(stack).and(REMOTE_SITE, remoteSite);
    }

    // ------------------------------------------------------ stack / protocol

    public static final String PROTOCOL_CREATE = Ids.build(PROTOCOL, CrudConstants.CREATE, Random.name());
    public static final String PROTOCOL_UPDATE = Ids.build(PROTOCOL, CrudConstants.UPDATE, Random.name());
    public static final String PROTOCOL_DELETE = Ids.build(PROTOCOL, CrudConstants.DELETE, Random.name());

    public static Address protocolAddress(String stack, String protocol) {
        return stackAddress(stack).and(PROTOCOL, protocol);
    }

    // ------------------------------------------------------ stack / transport

    public static final String TRANSPORT_CREATE = Ids.build(TRANSPORT, CrudConstants.CREATE, Random.name());

    public static Address transportAddress(String stack, String name) {
        return stackAddress(stack).and(TRANSPORT, name);
    }

    public static Address transportThreadPoolAddress(String stack, String transport, String threadPool) {
        return transportAddress(stack, transport).and(THREAD_POOL, threadPool);
    }

    // ------------------------------------------------------ channel

    public static final String CHANNEL_CREATE = Ids.build(CHANNEL, CrudConstants.CREATE, Random.name());
    public static final String CHANNEL_UPDATE = Ids.build(CHANNEL, CrudConstants.UPDATE, Random.name());
    public static final String CHANNEL_DELETE = Ids.build(CHANNEL, CrudConstants.DELETE, Random.name());

    public static Address channelAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CHANNEL, name);
    }

    // ------------------------------------------------------ channel / fork

    public static final String FORK_CREATE = Ids.build(FORK, CrudConstants.CREATE, Random.name());
    public static final String FORK_DELETE = Ids.build(FORK, CrudConstants.DELETE, Random.name());

    public static Address forkAddress(String channel, String name) {
        return channelAddress(channel).and(FORK, name);
    }

    // ------------------------------------------------------ channel / fork / protocol

    // protocol names are specific to jgroups domain
    public static final String FORK_PROTOCOL_CREATE = "COUNTER";
    public static final String FORK_PROTOCOL_UPDATE = "CENTRAL_LOCK";
    public static final String FORK_PROTOCOL_DELETE = "S3_PING";

    public static Address forkProtocolAddress(String channel, String fork, String protocol) {
        return forkAddress(channel, fork).and(PROTOCOL, protocol);
    }

    private JGroupsFixtures() {
    }
}
