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
package org.jboss.hal.testsuite.test.configuration.socketbinding;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING_GROUP;

public interface SocketBindingFixtures {

    int OUTBOUND_REMOTE_PORT = 60000;
    String LOCALHOST = "localhost";
    String PRIVATE = "private";
    String PUBLIC = "public";
    String STANDARD_SOCKETS = "standard-sockets";
    String SOURCE_PORT = "source-port";

    // ------------------------------------------------------ socket binding group

    String SBG_CREATE = Ids.build("sbg", "create", Random.name());
    String SBG_READ = Ids.build("sbg", "read", Random.name());
    String SBG_UPDATE = Ids.build("sbg", "update", Random.name());
    String SBG_DELETE = Ids.build("sbg", "delete", Random.name());

    static Address socketBindingGroupAddress(String name) {
        return Address.of(SOCKET_BINDING_GROUP, name);
    }

    // ------------------------------------------------------ inbound

    // prefix with "a", so the resources are on the first table page
    String INBOUND_CREATE = Ids.build("a", "inbound", "create", Random.name());
    String INBOUND_READ = Ids.build("a", "inbound", "read", Random.name());
    String INBOUND_UPDATE = Ids.build("a", "inbound", "update", Random.name());
    String INBOUND_DELETE = Ids.build("a", "inbound", "delete", Random.name());

    static Address inboundAddress(String sbg, String name) {
        return socketBindingGroupAddress(sbg).and(SOCKET_BINDING, name);
    }

    // ------------------------------------------------------ outbound local

    String OUTBOUND_LOCAL_CREATE = Ids.build("outbound", "local", "create", Random.name());
    String OUTBOUND_LOCAL_READ = Ids.build("outbound", "local", "read", Random.name());
    String OUTBOUND_LOCAL_UPDATE = Ids.build("outbound", "local", "update", Random.name());
    String OUTBOUND_LOCAL_DELETE = Ids.build("outbound", "local", "delete", Random.name());

    static Address outboundLocalAddress(String sbg, String name) {
        return socketBindingGroupAddress(sbg).and(LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING, name);
    }

    // ------------------------------------------------------ outbound local

    String OUTBOUND_REMOTE_CREATE = Ids.build("outbound", "remote", "create", Random.name());
    String OUTBOUND_REMOTE_READ = Ids.build("outbound", "remote", "read", Random.name());
    String OUTBOUND_REMOTE_UPDATE = Ids.build("outbound", "remote", "update", Random.name());
    String OUTBOUND_REMOTE_DELETE = Ids.build("outbound", "remote", "delete", Random.name());

    static Address outboundRemoteAddress(String sbg, String name) {
        return socketBindingGroupAddress(sbg).and(REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING, name);
    }
}
