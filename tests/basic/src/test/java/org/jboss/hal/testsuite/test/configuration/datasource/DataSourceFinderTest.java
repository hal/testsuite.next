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
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.*;
import static org.junit.Assert.assertEquals;

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
    public static void tearDown() throws Exception {
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_CREATE_CUSTOM));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_CREATE_H2));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_CREATE_TEST_CANCEL));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_CREATE_TEST_FINISH));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_READ));
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_DELETE));
    }

    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION)
                .select(new FinderPath().append(Ids.CONFIGURATION, Ids.asId(Names.SUBSYSTEMS))
                        .append(Ids.CONFIGURATION_SUBSYSTEM, DATASOURCES)
                        .append(Ids.DATA_SOURCE_DRIVER, Ids.asId(Names.DATASOURCES)))
                .column("ds-configuration");
    }

    @Test
    public void createCustom() throws Exception {
        column.dropdownAction(Ids.DATA_SOURCE_ADD_ACTIONS, Ids.DATA_SOURCE_ADD);
        WizardFragment wizard = console.wizard();

        // choose template
        wizard.getRoot().findElement(By.cssSelector("input[type=radio][name=template][value=custom]")).click();
        wizard.next(Ids.DATA_SOURCE_NAMES_FORM);

        // attributes
        FormFragment namesForms = wizard.getForm(Ids.DATA_SOURCE_NAMES_FORM);
        namesForms.text(NAME, DATA_SOURCE_CREATE_CUSTOM);
        String jndiName = Random.jndiName(DATA_SOURCE_CREATE_CUSTOM);
        namesForms.text(JNDI_NAME, jndiName);
        wizard.next(Ids.DATA_SOURCE_DRIVER_FORM);

        // JDBC driver
        FormFragment driverForm = wizard.getForm(Ids.DATA_SOURCE_DRIVER_FORM);
        driverForm.text(DRIVER_NAME, H2_DRIVER);
        wizard.next(Ids.DATA_SOURCE_CONNECTION_FORM);

        // connection
        FormFragment connectionForm = wizard.getForm(Ids.DATA_SOURCE_CONNECTION_FORM);
        String connectionUrl = h2ConnectionUrl(DATA_SOURCE_READ);
        connectionForm.text(CONNECTION_URL, connectionUrl);
        wizard.next(Ids.DATA_SOURCE_TEST_CONNECTION);

        // test connection
        wizard.next(Ids.DATA_SOURCE_REVIEW_FORM); // do nothing here

        // review
        FormFragment reviewForm = wizard.getForm(Ids.DATA_SOURCE_REVIEW_FORM);
        assertEquals(DATA_SOURCE_CREATE_CUSTOM, reviewForm.value(NAME));
        assertEquals(jndiName, reviewForm.value(JNDI_NAME));
        assertEquals(connectionUrl, reviewForm.value(CONNECTION_URL));
        assertEquals(H2_DRIVER, reviewForm.value(DRIVER_NAME));

        wizard.finishStayOpen();
        wizard.verifySuccess();
        wizard.close();

        String itemId = Ids.dataSourceConfiguration(DATA_SOURCE_CREATE_CUSTOM, false);
        column.containsItem(itemId);
        column.isSelected(itemId);
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_CREATE_CUSTOM), client).verifyExists();
    }

    @Test
    public void createH2() throws Exception {
        column.dropdownAction(Ids.DATA_SOURCE_ADD_ACTIONS, Ids.DATA_SOURCE_ADD);
        WizardFragment wizard = console.wizard();

        // choose template
        wizard.getRoot().findElement(By.cssSelector("input[type=radio][name=template][value=h2]")).click();
        wizard.next(Ids.DATA_SOURCE_NAMES_FORM);
        wizard.next(Ids.DATA_SOURCE_DRIVER_FORM);
        wizard.next(Ids.DATA_SOURCE_CONNECTION_FORM);
        wizard.next(Ids.DATA_SOURCE_TEST_CONNECTION);
        wizard.next(Ids.DATA_SOURCE_REVIEW_FORM); // do nothing here

        // review
        FormFragment reviewForm = wizard.getForm(Ids.DATA_SOURCE_REVIEW_FORM);
        reviewForm.showSensitive(USER_NAME);
        reviewForm.showSensitive(PASSWORD);
        assertEquals(H2_NAME, reviewForm.value(NAME));
        assertEquals(H2_JNDI_NAME, reviewForm.value(JNDI_NAME));
        assertEquals(H2_CONNECTION_URL, reviewForm.value(CONNECTION_URL));
        assertEquals(H2_DRIVER, reviewForm.value(DRIVER_NAME));
        assertEquals(H2_USER_NAME, reviewForm.value(USER_NAME));
        assertEquals(H2_PASSWORD, reviewForm.value(PASSWORD));

        wizard.finishStayOpen();
        wizard.verifySuccess();
        wizard.close();

        String itemId = Ids.dataSourceConfiguration(DATA_SOURCE_CREATE_H2, false);
        column.containsItem(itemId);
        column.isSelected(itemId);
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_CREATE_H2), client).verifyExists();
    }
}
