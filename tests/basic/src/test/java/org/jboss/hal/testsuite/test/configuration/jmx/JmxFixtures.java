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
package org.jboss.hal.testsuite.test.configuration.jmx;

import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONFIGURATION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.JMX;

public interface JmxFixtures {

    String NON_CORE_MBEAN_SENSITIVITY = "non-core-mbean-sensitivity";
    String USE_MANAGEMENT_ENDPOINT = "use-management-endpoint";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(JMX);
    Address AUDIT_LOG_ADDRESS = SUBSYSTEM_ADDRESS.and(CONFIGURATION, "audit-log");
    Address REMOTING_CONNECTOR_ADDRESS = SUBSYSTEM_ADDRESS.and("remoting-connector", JMX);
}
