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
package org.jboss.hal.testsuite.test.configuration.deploymentscanner;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEPLOYMENT_SCANNER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;

public interface DeploymentScannerFixtures {

    Address SUBSYSTEM_ADDRESS = Address.subsystem(DEPLOYMENT_SCANNER);

    String DS_CREATE = Ids.build("ds", "create", Random.name());
    String DS_READ = Ids.build("ds", "read", Random.name());
    String DS_UPDATE = Ids.build("ds", "update", Random.name());
    String DS_DELETE = Ids.build("ds", "delete", Random.name());

    static Address deploymentScannerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("scanner", name);
    }

    static String path(String name) {
        return name + "/" + PATH;
    }
}
