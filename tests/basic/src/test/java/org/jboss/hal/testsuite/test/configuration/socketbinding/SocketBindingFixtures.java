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
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING_GROUP;

public final class SocketBindingFixtures {

    private static final String SOCKET_BINDING_GROUP_PREFIX = "sbg";
    private static final String A_PREFIX = "a";
    private static final String INBOUND = "inbound";
    private static final String OUTBOUND_PREFIX = "outbound";
    private static final String LOCAL = "local";
    private static final String REMOTE = "remote";

    public static int OUTBOUND_REMOTE_PORT = 60000;
    public static final String LOCALHOST = "localhost";
    static final String PRIVATE = "private";
    static final String PUBLIC = "public";
    public static final String STANDARD_SOCKETS = "standard-sockets";
    static final String SOURCE_PORT = "source-port";

    // ------------------------------------------------------ socket binding group

    static final String SBG_CREATE = Ids.build(SOCKET_BINDING_GROUP_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SBG_READ = Ids.build(SOCKET_BINDING_GROUP_PREFIX, CrudConstants.READ, Random.name());
    static final String SBG_UPDATE = Ids.build(SOCKET_BINDING_GROUP_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SBG_DELETE = Ids.build(SOCKET_BINDING_GROUP_PREFIX, CrudConstants.DELETE, Random.name());

    static Address socketBindingGroupAddress(String name) {
        return Address.of(SOCKET_BINDING_GROUP, name);
    }

    // ------------------------------------------------------ inbound

    // prefix with "a", so the resources are on the first table page
    static final String INBOUND_CREATE = Ids.build(A_PREFIX, INBOUND, CrudConstants.CREATE, Random.name());
    public static final String INBOUND_READ = Ids.build(A_PREFIX, INBOUND, CrudConstants.READ, Random.name());
    static final String INBOUND_UPDATE = Ids.build(A_PREFIX, INBOUND, CrudConstants.UPDATE, Random.name());
    static final String INBOUND_DELETE = Ids.build(A_PREFIX, INBOUND, CrudConstants.DELETE, Random.name());

    public static Address inboundAddress(String sbg, String name) {
        return socketBindingGroupAddress(sbg).and(SOCKET_BINDING, name);
    }

    // ------------------------------------------------------ outbound local

    static final String OUTBOUND_LOCAL_CREATE = Ids.build(OUTBOUND_PREFIX, LOCAL, CrudConstants.CREATE, Random.name());
    public static final String OUTBOUND_LOCAL_READ = Ids.build(OUTBOUND_PREFIX, LOCAL, CrudConstants.READ, Random.name());
    static final String OUTBOUND_LOCAL_UPDATE = Ids.build(OUTBOUND_PREFIX, LOCAL, CrudConstants.UPDATE, Random.name());
    static final String OUTBOUND_LOCAL_DELETE = Ids.build(OUTBOUND_PREFIX, LOCAL, CrudConstants.DELETE, Random.name());

    static Address outboundLocalAddress(String sbg, String name) {
        return socketBindingGroupAddress(sbg).and(LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING, name);
    }

    // ------------------------------------------------------ outbound local

    static final String OUTBOUND_REMOTE_CREATE = Ids.build(OUTBOUND_PREFIX, REMOTE, CrudConstants.CREATE, Random.name());
    public static final String OUTBOUND_REMOTE_READ = Ids.build(OUTBOUND_PREFIX, REMOTE, CrudConstants.READ, Random.name());
    static final String OUTBOUND_REMOTE_UPDATE = Ids.build(OUTBOUND_PREFIX, REMOTE, CrudConstants.UPDATE, Random.name());
    static final String OUTBOUND_REMOTE_DELETE = Ids.build(OUTBOUND_PREFIX, REMOTE, CrudConstants.DELETE, Random.name());

    static Address outboundRemoteAddress(String sbg, String name) {
        return socketBindingGroupAddress(sbg).and(REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING, name);
    }

    private SocketBindingFixtures() {
    }
}
