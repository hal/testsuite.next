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
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.JGROUPS;

public interface JGroupsFixtures {

    String DEFAULT_CHANNEL = "default-channel";
    String TCP = "tcp";
    String JGROUPS_TCP = "jgroups-tcp";
    String CLUSTER = "cluster";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(JGROUPS);

    // ------------------------------------------------------ stack

    String STACK_CREATE = Ids.build("stack", "create", Random.name());
    String STACK_UPDATE = Ids.build("stack", "update", Random.name());
    String STACK_DELETE = Ids.build("stack", "delete", Random.name());

    static Address stackAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("stack", name);
    }

    // ------------------------------------------------------ stack / transport

    String TRANSPORT_CREATE = Ids.build("transport", "create", Random.name());
    String TRANSPORT_UPDATE = Ids.build("transport", "update", Random.name());
    String TRANSPORT_DELETE = Ids.build("transport", "delete", Random.name());

    static Address transportAddress(String stack, String name) {
        return stackAddress(stack).and("transport", name);
    }

    // ------------------------------------------------------ channel

    String CHANNEL_CREATE = Ids.build("channel", "create", Random.name());
    String CHANNEL_UPDATE = Ids.build("channel", "update", Random.name());
    String CHANNEL_DELETE = Ids.build("channel", "delete", Random.name());

    static Address channelAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("channel", name);
    }

}
