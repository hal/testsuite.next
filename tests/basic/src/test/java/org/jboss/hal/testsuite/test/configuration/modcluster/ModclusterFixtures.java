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
package org.jboss.hal.testsuite.test.configuration.modcluster;

import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONFIGURATION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODCLUSTER;

public interface ModclusterFixtures {

    String EXCLUDED_CONTEXTS = "excluded-contexts";
    String NODE_TIMEOUT = "node-timeout";
    String PROXY_URL = "proxy-url";
    String STICKY_SESSION = "sticky-session";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(MODCLUSTER);
    Address CONFIG_ADDRESS = SUBSYSTEM_ADDRESS.and("mod-cluster-config", CONFIGURATION);
    Address SSL_ADDRESS = CONFIG_ADDRESS.and("ssl", CONFIGURATION);
}
