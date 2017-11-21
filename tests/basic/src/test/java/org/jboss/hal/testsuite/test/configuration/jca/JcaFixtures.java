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
package org.jboss.hal.testsuite.test.configuration.jca;

import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.BEAN_VALIDATION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.JCA;

public interface JcaFixtures {

    String ARCHIVE_VALIDATION = "archive-validation";
    String CACHED_CONNECTION_MANAGER = "cached-connection-manager";
    String DEBUG = "debug";
    String TRACER = "tracer";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(JCA);

    // ------------------------------------------------------ configuration

    Address CACHED_CONNECTION_MANAGER_ADDRESS = SUBSYSTEM_ADDRESS.and(CACHED_CONNECTION_MANAGER,
            CACHED_CONNECTION_MANAGER);
    Address ARCHIVE_VALIDATION_ADDRESS = SUBSYSTEM_ADDRESS.and(ARCHIVE_VALIDATION, ARCHIVE_VALIDATION);
    Address BEAN_VALIDATION_ADDRESS = SUBSYSTEM_ADDRESS.and(BEAN_VALIDATION, BEAN_VALIDATION);

    // ------------------------------------------------------ tracer

    Address TRACER_ADDRESS = SUBSYSTEM_ADDRESS.and(TRACER, TRACER);
}
