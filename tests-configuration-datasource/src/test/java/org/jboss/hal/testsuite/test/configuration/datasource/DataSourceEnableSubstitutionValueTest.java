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
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DATASOURCES;
import static org.jboss.hal.resources.CSS.alertDismissable;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.DATA_SOURCE_TEST;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.dataSourceAddress;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.h2ConnectionUrl;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;

@RunWith(Arquillian.class)
public class DataSourceEnableSubstitutionValueTest {
    private static final String H2_DRIVER_NAME = "h2";
    private static final String H2_ENABLED_VARIABLE = "h2.enabled";
    private static final String H2_ENABLED_VARIABLE_COMPLETE = "${" + H2_ENABLED_VARIABLE + "}";

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration administration = new Administration(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_TEST)
                .driverName(H2_DRIVER_NAME)
                .jndiName(Random.jndiName(DATA_SOURCE_TEST))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_TEST))
                .enableAfterCreate()
                .build());
        administration.reloadIfRequired();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_TEST));
        operations.removeIfExists(Address.root().and("system-property", H2_ENABLED_VARIABLE));
        administration.reloadIfRequired();
        client.close();
    }

    @Inject
    private Console console;
    private ColumnFragment column;
    @Drone
    private WebDriver browser;

    @Before
    public void setUp() throws Exception {
        operations.add(Address.root().and("system-property", H2_ENABLED_VARIABLE), Values.of("value", "true")).assertSuccess();
        operations.writeAttribute(dataSourceAddress(DATA_SOURCE_TEST), "enabled", H2_ENABLED_VARIABLE_COMPLETE).assertSuccess();
        administration.reloadIfRequired();
    }

    /**
     * Test display DataSource list if datasource has a property substitution
     * @see <a href="https://issues.jboss.org/browse/HAL-1572">HAL-1572</a>
     */
    @Test
    public void displayDatasourceListTest() {
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(DATASOURCES)
                .append(Ids.DATA_SOURCE_DRIVER, Ids.asId(Names.DATASOURCES)))
                .column(Ids.DATA_SOURCE_CONFIGURATION);
        Assert.assertTrue("Page shouldn't contain any alert message. See https://issues.jboss.org/browse/HAL-1572", By.cssSelector("." + alertDismissable).findElements(browser).isEmpty());
    }
}
