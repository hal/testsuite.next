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

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.test.configuration.datasource.properties.AbstractDatasourcePropertiesTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.junit.Assert.assertEquals;


@RunWith(Arquillian.class)
public class NonXADatasourceCreateTest extends AbstractDatasourcePropertiesTest {
    private static final String POSTGRESQL_CSS_SELECTOR = "input[type=radio][name=template][value=postgresql]";
    private static final String SQLSERVER_CSS_SELECTOR = "input[type=radio][name=template][value=sqlserver]";
    private static final String PG_DATASOURCE_NAME = "postgresql-data-source-create-" + Random.name();
    private static final String SQLSERVER_DATASOURCE_NAME = "sqlserver-data-source-create-" + Random.name();

    @Drone
    private WebDriver browser;
    @Inject private Console console;
    private ColumnFragment column;
    private WizardFragment wizard;

    @Before
    public void navigateToCreateXADatasourceWizard() {
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(DATASOURCES)
                .append(Ids.DATA_SOURCE_DRIVER, Ids.asId(Names.DATASOURCES)))
                .column(Ids.DATA_SOURCE_CONFIGURATION);
        column.dropdownAction(Ids.DATA_SOURCE_ADD_ACTIONS, Ids.DATA_SOURCE_ADD);
        wizard = console.wizard();
    }

    @AfterClass
    public static void cleanUpDataSources() throws InterruptedException, TimeoutException, IOException, OperationException {
        operations.removeIfExists(DataSourceFixtures.dataSourceAddress(PG_DATASOURCE_NAME));
        operations.removeIfExists(DataSourceFixtures.dataSourceAddress(SQLSERVER_DATASOURCE_NAME));
        administration.reloadIfRequired();
    }

    /** Create a data source for postgresql */
    @Test
    public void createPostgreSQLDataSources() throws Exception {
        wizard.getRoot().findElement(By.cssSelector(POSTGRESQL_CSS_SELECTOR)).click();
        wizard.next(Ids.DATA_SOURCE_NAMES_FORM);
        FormFragment namesForms = wizard.getForm(Ids.DATA_SOURCE_NAMES_FORM);
        namesForms.text(NAME, PG_DATASOURCE_NAME);
        wizard.next(Ids.DATA_SOURCE_DRIVER_FORM);
        FormFragment driverForm = wizard.getForm(Ids.DATA_SOURCE_DRIVER_FORM);
        driverForm.text(DRIVER_NAME, PG_DRIVER_NAME);
        driverForm.text(DRIVER_CLASS_NAME, "");
        driverForm.clear(DRIVER_CLASS_NAME);
        wizard.next(Ids.DATA_SOURCE_CONNECTION_FORM);
        wizard.next(Ids.DATA_SOURCE_TEST_CONNECTION);
        wizard.next(Ids.DATA_SOURCE_REVIEW_FORM);

        assertEquals("", driverForm.value(DRIVER_CLASS_NAME));
        wizard.finishStayOpen();
        wizard.verifySuccess();
        wizard.close();
        assertEquals("Bad value of field " + DATASOURCE_CLASS + " in configuration file!","",operations.readAttribute(DataSourceFixtures.dataSourceAddress(PG_DATASOURCE_NAME), DATASOURCE_CLASS).stringValue());
    }

    /** Create a data source for sqlserver */
    @Test
    public void createSqlserverDataSources() throws Exception {
        wizard.getRoot().findElement(By.cssSelector(SQLSERVER_CSS_SELECTOR)).click();
        wizard.next(Ids.DATA_SOURCE_NAMES_FORM);
        FormFragment namesForms = wizard.getForm(Ids.DATA_SOURCE_NAMES_FORM);
        namesForms.text(NAME, SQLSERVER_DATASOURCE_NAME);
        wizard.next(Ids.DATA_SOURCE_DRIVER_FORM);
        FormFragment driverForm = wizard.getForm(Ids.DATA_SOURCE_DRIVER_FORM);
        driverForm.text(DRIVER_NAME, MSSQL_DRIVER_NAME);
        driverForm.text(DRIVER_CLASS_NAME, "");
        driverForm.clear(DRIVER_CLASS_NAME);
        wizard.next(Ids.DATA_SOURCE_CONNECTION_FORM);
        wizard.next(Ids.DATA_SOURCE_TEST_CONNECTION);
        wizard.next(Ids.DATA_SOURCE_REVIEW_FORM);

        assertEquals("", driverForm.value(DRIVER_CLASS_NAME));
        wizard.finishStayOpen();
        wizard.verifySuccess();
        wizard.close();
        assertEquals("Bad value of field " + DATASOURCE_CLASS + " in configuration file!","",operations.readAttribute(DataSourceFixtures.dataSourceAddress(SQLSERVER_DATASOURCE_NAME), DATASOURCE_CLASS).stringValue());
    }
}
