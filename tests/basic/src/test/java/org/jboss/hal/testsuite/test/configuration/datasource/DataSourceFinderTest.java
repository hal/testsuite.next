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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DATASOURCES;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.DATA_SOURCE_DELETE;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.DATA_SOURCE_READ;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.dataSourceAddress;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.h2ConnectionUrl;

@RunWith(Arquillian.class)
public class DataSourceFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_READ)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_READ))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_READ))
                .build());
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_DELETE)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_DELETE))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_DELETE))
                .build());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_READ));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_DELETE));
    }

    @Drone private WebDriver browser;
    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION)
                .select(new FinderPath().append(Ids.CONFIGURATION, Ids.asId(Names.SUBSYSTEMS))
                        .append(Ids.CONFIGURATION_SUBSYSTEM, DATASOURCES)
                        .append(Ids.DATA_SOURCE_DRIVER, Ids.asId(Names.DATASOURCES)))
                .column("ds-configuration");
        column.dropdownAction(Ids.DATA_SOURCE_ADD_ACTIONS, Ids.DATA_SOURCE_ADD);
    }
}
