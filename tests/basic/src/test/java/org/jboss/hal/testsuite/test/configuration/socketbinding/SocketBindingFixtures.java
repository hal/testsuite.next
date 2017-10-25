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

import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING_GROUP;

public interface SocketBindingFixtures {

    String STANDARD_SOCKETS = "standard-sockets";

    // ------------------------------------------------------ socket binding group

    String SBG_CREATE = Ids.build("sbg", "create", Random.name());
    String SBG_READ = Ids.build("sbg", "read", Random.name());
    String SBG_UPDATE = Ids.build("sbg", "update", Random.name());
    String SBG_DELETE = Ids.build("sbg", "delete", Random.name());

    static Address socketBindingGroupAddress(String name) {
        return Address.of(SOCKET_BINDING_GROUP, name);
    }


    // ------------------------------------------------------ inbound / socket binding

    String INBOUND_CREATE = Ids.build("inbound", "create", Random.name());
    String INBOUND_UPDATE = Ids.build("inbound", "update", Random.name());
    String INBOUND_DELETE = Ids.build("inbound", "delete", Random.name());

    static Address socketBindingAddress(String sbg, String name) {
        return socketBindingGroupAddress(sbg).and(SOCKET_BINDING, name);
    }
}
