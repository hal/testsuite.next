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
package org.jboss.hal.testsuite.fixtures;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DISTRIBUTABLE_WEB;
import static org.jboss.hal.testsuite.CrudConstants.CREATE;
import static org.jboss.hal.testsuite.CrudConstants.DELETE;
import static org.jboss.hal.testsuite.CrudConstants.UPDATE;

public final class DistributableWebFixtures {

    private static final String HOTROD_PREFIX = "hr";
    private static final String INFINISPAN_PREFIX = "inf";
    private static final String SESSION_PREFIX = "session";
    private static final String SSO_PREFIX = "sso";

    public static final String ATTRIBUTE = "ATTRIBUTE";
    public static final String CACHE = "cache";
    public static final String DEFAULT_SESSION_MANAGEMENT = "default-session-management";
    public static final String DEFAULT_SSO_MANAGEMENT = "default-single-sign-on-management";
    public static final String GRANULARITY = "granularity";
    public static final String REMOTE_SOCKET_BINDING = "remote-socket-binding-" + Random.name();
    public static final String SESSION = "SESSION";

    public static final Address SUBSYSTEM_ADDRESS = Address.subsystem(DISTRIBUTABLE_WEB);

    // ------------------------------------------------------ hotrod session

    public static final String HOTROD_SESSION_CREATE = Ids.build(HOTROD_PREFIX, SESSION_PREFIX, CREATE, Random.name());
    public static final String HOTROD_SESSION_UPDATE = Ids.build(HOTROD_PREFIX, SESSION_PREFIX, UPDATE, Random.name());
    public static final String HOTROD_SESSION_DELETE = Ids.build(HOTROD_PREFIX, SESSION_PREFIX, DELETE, Random.name());

    public static Address hotrodSessionAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("hotrod-session-management", name);
    }

    // ------------------------------------------------------ hotrod sso

    public static final String HOTROD_SSO_CREATE = Ids.build(HOTROD_PREFIX, SSO_PREFIX, CREATE, Random.name());
    public static final String HOTROD_SSO_UPDATE = Ids.build(HOTROD_PREFIX, SSO_PREFIX, UPDATE, Random.name());
    public static final String HOTROD_SSO_DELETE = Ids.build(HOTROD_PREFIX, SSO_PREFIX, DELETE, Random.name());

    public static Address hotrodSSOAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("hotrod-single-sign-on-management", name);
    }

    // ------------------------------------------------------ infinispan session

    public static final String INFINISPAN_SESSION_CREATE = Ids.build(INFINISPAN_PREFIX, SESSION_PREFIX, CREATE, Random.name());
    public static final String INFINISPAN_SESSION_REF = Ids.build(INFINISPAN_PREFIX, SESSION_PREFIX, "ref", Random.name());
    public static final String INFINISPAN_SESSION_UPDATE = Ids.build(INFINISPAN_PREFIX, SESSION_PREFIX, UPDATE, Random.name());
    public static final String INFINISPAN_SESSION_DELETE = Ids.build(INFINISPAN_PREFIX, SESSION_PREFIX, DELETE, Random.name());

    public static Address infinispanSessionAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("infinispan-session-management", name);
    }

    // ------------------------------------------------------ infinispan sso

    public static final String INFINISPAN_SSO_CREATE = Ids.build(INFINISPAN_PREFIX, SSO_PREFIX, CREATE, Random.name());
    public static final String INFINISPAN_SSO_REF = Ids.build(INFINISPAN_PREFIX, SSO_PREFIX, "ref", Random.name());
    public static final String INFINISPAN_SSO_UPDATE = Ids.build(INFINISPAN_PREFIX, SSO_PREFIX, UPDATE, Random.name());
    public static final String INFINISPAN_SSO_DELETE = Ids.build(INFINISPAN_PREFIX, SSO_PREFIX, DELETE, Random.name());

    public static Address infinispanSSOAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("infinispan-single-sign-on-management", name);
    }

    private DistributableWebFixtures() {
    }
}
