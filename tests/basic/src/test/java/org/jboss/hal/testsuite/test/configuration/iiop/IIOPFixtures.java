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
package org.jboss.hal.testsuite.test.configuration.iiop;

import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.IIOP_OPENJDK;

public final class IIOPFixtures {

    static final String PERSISTENT_SERVER_ID = "persistent-server-id";
    static final String EXPORT_CORBALOC = "export-corbaloc";
    static final String ROOT_CONTEXT = "root-context";
    static final String SPEC = "spec";
    static final String AUTH_METHOD = "auth-method";
    static final String USERNAME_PASSWORD = "username_password";
    static final String CALLER_PROPAGATION = "caller-propagation";
    static final String SUPPORTED = "supported";
    static final String HIGH_WATER_MARK = "high-water-mark";
    static final String NUMBER_TO_RECLAIM = "number-to-reclaim";
    static final String DEFAULT_ROOT_CONTEXT = "JBoss/Naming/root";
    static final String SUPPORT_SSL = "support-ssl";
    static final String IIOP = "iiop";

    static final Address SUBSYSTEM_ADDRESS = Address.subsystem(IIOP_OPENJDK);

    private IIOPFixtures() {
    }

}
