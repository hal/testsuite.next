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

import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.IIOP_OPENJDK;

public final class IIOPFixtures {

    public static final String PERSISTENT_SERVER_ID = "persistent-server-id";
    public static final String EXPORT_CORBALOC = "export-corbaloc";
    public static final String ROOT_CONTEXT = "root-context";
    public static final String SPEC = "spec";
    public static final String AUTH_METHOD = "auth-method";
    public static final String USERNAME_PASSWORD = "username_password";
    public static final String CALLER_PROPAGATION = "caller-propagation";
    public static final String SUPPORTED = "supported";
    public static final String HIGH_WATER_MARK = "high-water-mark";
    public static final String NUMBER_TO_RECLAIM = "number-to-reclaim";
    public static final String DEFAULT_ROOT_CONTEXT = "JBoss/Naming/root";
    public static final String SUPPORT_SSL = "support-ssl";
    public static final String IIOP = "iiop";

    public static final Address SUBSYSTEM_ADDRESS = Address.subsystem(IIOP_OPENJDK);

    private IIOPFixtures() {
    }

}
