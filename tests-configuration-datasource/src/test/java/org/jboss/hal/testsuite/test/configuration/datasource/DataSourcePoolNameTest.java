/*
 * Copyright 2015-2019 Red Hat, Inc, and individual contributors.
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


import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DATASOURCES;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.*;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.runtimeSubsystemPath;

@RunWith(Arquillian.class)
public class DataSourcePoolNameTest {

    private static final String H2_DRIVER_NAME = "h2";
    private static final String DATA_SOURCE_SLASH_FORMAT = DATA_SOURCE_TEST + "/test";

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration administration = new Administration(client);
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_SLASH_FORMAT)
                .driverName(H2_DRIVER_NAME)
                .jndiName(Random.jndiName(DATA_SOURCE_SLASH_FORMAT))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_SLASH_FORMAT))
                .enableAfterCreate()
                .statisticsEnabled(true)
                .build());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_SLASH_FORMAT));
        administration.reloadIfRequired();
        client.close();
    }

    @Inject
    private Console console;

    /**
     * Test display DataSource with a pool name of prefix/poolName format at runtime
     * @see <a href="https://issues.redhat.com/browse/HAL-1906">HAL-1906</a>
     */
    @Test
    public void testPoolNameWithSlashSeparator() throws Exception {
        try {
            console.finder(NameTokens.RUNTIME, runtimeSubsystemPath(serverEnvironmentUtils.getServerHostName(), DATASOURCES))
                    .column(Ids.DATA_SOURCE_RUNTIME)
                    .selectItem(Ids.dataSourceRuntime(DATA_SOURCE_SLASH_FORMAT, false))
                    .view();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception was caught. Page contains alert message");
        }
    }
}
