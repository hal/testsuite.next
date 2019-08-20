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
package org.jboss.hal.testsuite.test.configuration.distributableweb;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DISTRIBUTABLE_WEB;
import static org.jboss.hal.testsuite.CrudConstants.CREATE;
import static org.jboss.hal.testsuite.CrudConstants.DELETE;
import static org.jboss.hal.testsuite.CrudConstants.UPDATE;

final class DistributableWebFixtures {

    private static final String HOTROD_PREFIX = "hr";
    private static final String INFINISPAN_PREFIX = "inf";
    private static final String SESSION_PREFIX = "session";
    private static final String SSO_PREFIX = "sso";

    static final String ATTRIBUTE = "ATTRIBUTE";
    static final String DEFAULT_SESSION_MANAGEMENT = "default-session-management";
    static final String DEFAULT_SSO_MANAGEMENT = "default-single-sign-on-management";
    static final String GRANULARITY = "granularity";
    static final String REMOTE_SOCKET_BINDING = "remote-socket-binding-" + Random.name();
    static final String SESSION = "SESSION";

    static final Address SUBSYSTEM_ADDRESS = Address.subsystem(DISTRIBUTABLE_WEB);

    // ------------------------------------------------------ hotrod session

    static final String HOTROD_SESSION_CREATE = Ids.build(HOTROD_PREFIX, SESSION_PREFIX, CREATE, Random.name());
    static final String HOTROD_SESSION_UPDATE = Ids.build(HOTROD_PREFIX, SESSION_PREFIX, UPDATE, Random.name());
    static final String HOTROD_SESSION_DELETE = Ids.build(HOTROD_PREFIX, SESSION_PREFIX, DELETE, Random.name());

    static Address hotrodSessionAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("hotrod-session-management", name);
    }

    // ------------------------------------------------------ hotrod sso

    static final String HOTROD_SSO_CREATE = Ids.build(HOTROD_PREFIX, SSO_PREFIX, CREATE, Random.name());
    static final String HOTROD_SSO_UPDATE = Ids.build(HOTROD_PREFIX, SSO_PREFIX, UPDATE, Random.name());
    static final String HOTROD_SSO_DELETE = Ids.build(HOTROD_PREFIX, SSO_PREFIX, DELETE, Random.name());

    static Address hotrodSSOAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("hotrod-single-sign-on-management", name);
    }

    // ------------------------------------------------------ infinispan session

    static final String INFINISPAN_SESSION_CREATE = Ids.build(INFINISPAN_PREFIX, SESSION_PREFIX, CREATE, Random.name());
    static final String INFINISPAN_SESSION_REF = Ids.build(INFINISPAN_PREFIX, SESSION_PREFIX, "ref", Random.name());
    static final String INFINISPAN_SESSION_UPDATE = Ids.build(INFINISPAN_PREFIX, SESSION_PREFIX, UPDATE, Random.name());
    static final String INFINISPAN_SESSION_DELETE = Ids.build(INFINISPAN_PREFIX, SESSION_PREFIX, DELETE, Random.name());

    static Address infinispanSessionAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("infinispan-session-management", name);
    }

    // ------------------------------------------------------ infinispan sso

    static final String INFINISPAN_SSO_CREATE = Ids.build(INFINISPAN_PREFIX, SSO_PREFIX, CREATE, Random.name());
    static final String INFINISPAN_SSO_REF = Ids.build(INFINISPAN_PREFIX, SSO_PREFIX, "ref", Random.name());
    static final String INFINISPAN_SSO_UPDATE = Ids.build(INFINISPAN_PREFIX, SSO_PREFIX, UPDATE, Random.name());
    static final String INFINISPAN_SSO_DELETE = Ids.build(INFINISPAN_PREFIX, SSO_PREFIX, DELETE, Random.name());

    static Address infinispanSSOAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("infinispan-single-sign-on-management", name);
    }

    private DistributableWebFixtures() {
    }
}
