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
package org.jboss.hal.testsuite.test.configuration.datasource;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DATASOURCES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DATA_SOURCE;

public interface DataSourceFixtures {

    String H2_NAME = "H2DS";
    String H2_JNDI_NAME = "java:/H2DS";
    String H2_DRIVER = "h2";
    String H2_CONNECTION_URL = h2ConnectionUrl("test");
    String H2_USER_NAME = "sa";
    String H2_PASSWORD = "sa";

    String DATA_SOURCE_CREATE_CUSTOM = Ids.build("ds", "create-custom", Random.name());
    String DATA_SOURCE_CREATE_EXISTING = Ids.build("ds", "create-existing", Random.name());
    String DATA_SOURCE_CREATE_H2 = H2_NAME;
    String DATA_SOURCE_CREATE_TEST_CANCEL = Ids.build("ds", "create-test-cancel", Random.name());
    String DATA_SOURCE_CREATE_TEST_CHANGE = Ids.build("ds", "create-test-change", Random.name());
    String DATA_SOURCE_CREATE_TEST_FINISH = Ids.build("ds", "create-test-finish", Random.name());
    String DATA_SOURCE_DELETE = Ids.build("ds", "delete", Random.name());
    String DATA_SOURCE_DISABLE = Ids.build("ds", "disable", Random.name());
    String DATA_SOURCE_ENABLE = Ids.build("ds", "enable", Random.name());
    String DATA_SOURCE_READ = Ids.build("ds", "read", Random.name());
    String DATA_SOURCE_TEST = Ids.build("ds", "test", Random.name());
    String DATA_SOURCE_UPDATE = Ids.build("ds", "update", Random.name());

    String URL_DELIMITER = "url-delimiter";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(DATASOURCES);
    String VALID_CONNECTION_CHECKER_CLASS_NAME = "valid-connection-checker-class-name";
    String VALID_CONNECTION_CHECKER_PROPERTIES = "valid-connection-checker-properties";
    String BACKGROUND_VALIDATION = "background-validation";
    String BACKGROUND_VALIDATION_MILLIS = "background-validation-millis";
    String USE_TRY_LOCK = "use-try-lock";
    String BLOCKING_TIMEOUT_WAIT_MILLIS = "blocking-timeout-wait-millis";
    String SPY = "spy";
    String TRACKING = "tracking";

    static Address dataSourceAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(DATA_SOURCE, name);
    }

    static String h2ConnectionUrl(String name) {
        return "jdbc:h2:mem:" + name + ";DB_CLOSE_DELAY=-1";
    }
}
