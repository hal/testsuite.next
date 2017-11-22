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

public interface IIOPFixtures {

    String PERSISTENT_SERVER_ID = "persistent-server-id";
    String EXPORT_CORBALOC = "export-corbaloc";
    String ROOT_CONTEXT = "root-context";
    String SPEC = "spec";
    String IDENTITY = "identity";
    String AUTH_METHOD = "auth-method";
    String USERNAME_PASSWORD = "username_password";
    String CALLER_PROPAGATION = "caller-propagation";
    String SUPPORTED = "supported";
    String HIGH_WATER_MARK = "high-water-mark";
    String NUMBER_TO_RECLAIM = "number-to-reclaim";
    String DEFAULT_ROOT_CONTEXT = "JBoss/Naming/root";
    String SUPPORT_SSL = "support-ssl";
    String IIOP = "iiop";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(IIOP_OPENJDK);

}
