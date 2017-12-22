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

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.page.Places;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DATASOURCES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ENABLED;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class DataSourceFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_DELETE)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_DELETE))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_DELETE))
                .build());
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_DISABLE)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_DISABLE))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_DISABLE))
                .enableAfterCreate()
                .build());
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_ENABLE)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_ENABLE))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_ENABLE))
                .build());
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_READ)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_READ))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_READ))
                .build());
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_TEST)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_TEST))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_TEST))
                .enableAfterCreate()
                .build());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_DELETE));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_DISABLE));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_ENABLE));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_READ));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_TEST));
    }

    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(DATASOURCES)
                .append(Ids.DATA_SOURCE_DRIVER, Ids.asId(Names.DATASOURCES)))
                .column(Ids.DATA_SOURCE_CONFIGURATION);
    }

    @Test
    public void read() {
        assertTrue(column.containsItem(Ids.dataSourceConfiguration(DATA_SOURCE_READ, false)));
    }

    @Test
    public void select() {
        column.selectItem(Ids.dataSourceConfiguration(DATA_SOURCE_READ, false));
        PlaceRequest placeRequest = Places.finderPlace(NameTokens.CONFIGURATION,
                configurationSubsystemPath(DATASOURCES)
                        .append(Ids.DATA_SOURCE_DRIVER, Ids.asId(Names.DATASOURCES))
                        .append(Ids.DATA_SOURCE_CONFIGURATION, Ids.dataSourceConfiguration(DATA_SOURCE_READ, false)));
        console.verify(placeRequest);
    }

    @Test
    public void view() {
        column.selectItem(Ids.dataSourceConfiguration(DATA_SOURCE_READ, false))
                .view();

        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.DATA_SOURCE_CONFIGURATION)
                .with(NAME, DATA_SOURCE_READ)
                .build();
        console.verify(placeRequest);
    }

    @Test
    public void disable() throws Exception {
        column.selectItem(Ids.dataSourceConfiguration(DATA_SOURCE_DISABLE, false))
                .dropdown()
                .click("Disable");

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_DISABLE), client)
                .verifyAttribute(ENABLED, false);
    }

    @Test
    public void enable() throws Exception {
        column.selectItem(Ids.dataSourceConfiguration(DATA_SOURCE_ENABLE, false))
                .dropdown()
                .click("Enable");

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_ENABLE), client)
                .verifyAttribute(ENABLED, true);
    }

    @Test
    public void testConnection() {
        column.selectItem(Ids.dataSourceConfiguration(DATA_SOURCE_TEST, false))
                .dropdown()
                .click("Test Connection");
        console.verifySuccess();
    }

    @Test
    public void delete() throws Exception {
        column.selectItem(Ids.dataSourceConfiguration(DATA_SOURCE_DELETE, false))
                .dropdown()
                .click("Remove");
        console.confirmationDialog().confirm();

        console.verifySuccess();
        assertFalse(column.containsItem(Ids.dataSourceConfiguration(DATA_SOURCE_DELETE, false)));
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_DELETE), client)
                .verifyDoesNotExist();
    }
}
