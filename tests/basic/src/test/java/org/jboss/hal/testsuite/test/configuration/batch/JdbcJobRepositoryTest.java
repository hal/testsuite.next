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
package org.jboss.hal.testsuite.test.configuration.batch;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.page.configuration.BatchPage;
import org.jboss.hal.testsuite.util.Console;
import org.jboss.hal.testsuite.util.Notification;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.DATA_SOURCE;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.JDBC_TO_BE_ADDED;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.JDBC_TO_BE_REMOVED;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.JDBC_TO_BE_VIEWD;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.jdbcAddress;
import static org.junit.Assert.assertEquals;

/** Requires the driver "h2" to be present */
@RunWith(Arquillian.class)
public class JdbcJobRepositoryTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @Drone private WebDriver browser;
    @Page private BatchPage page;

    @BeforeClass
    public static void beforeClass() throws Exception {
        String connectionUrl = "jdbc:h2:mem:" + DATA_SOURCE + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
        operations.add(Address.subsystem("datasources").and("data-source", DATA_SOURCE),
                Values.empty()
                        .and("jndi-name", "java:/jboss/" + DATA_SOURCE)
                        .and("driver-name", "h2")
                        .and("connection-url", connectionUrl));
        operations.add(jdbcAddress(JDBC_TO_BE_VIEWD), Values.empty().and("data-source", DATA_SOURCE));
        operations.add(jdbcAddress(JDBC_TO_BE_REMOVED), Values.empty().and("data-source", DATA_SOURCE));
    }

    @Before
    public void setUp() throws Exception {
        page.navigate();
        page.getJdbcItem().click();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(jdbcAddress(JDBC_TO_BE_ADDED));
        operations.removeIfExists(jdbcAddress(JDBC_TO_BE_VIEWD));
        operations.removeIfExists(jdbcAddress(JDBC_TO_BE_REMOVED));
        operations.removeIfExists(Address.subsystem("datasources").and("data-source", DATA_SOURCE));
    }

    @Test
    public void create() throws Exception {
        page.getJdbcTable().add();
        AddResourceDialogFragment dialog = Console.withBrowser(browser).addResourceDialog();
        dialog.getForm().text("name", JDBC_TO_BE_ADDED);
        dialog.getForm().text("data-source", DATA_SOURCE);
        dialog.add();

        Notification.withBrowser(browser).success();
        new ResourceVerifier(jdbcAddress(JDBC_TO_BE_ADDED), client).verifyExists();
    }

    @Test
    public void read() throws Exception {
        page.getJdbcTable().select(JDBC_TO_BE_VIEWD);
        page.getJdbcForm().view();
        assertEquals(DATA_SOURCE, page.getJdbcForm().value("data-source"));
    }

    @Test
    public void delete() throws Exception {
        page.getJdbcTable().remove(JDBC_TO_BE_REMOVED);

        Notification.withBrowser(browser).success();
        new ResourceVerifier(jdbcAddress(JDBC_TO_BE_REMOVED), client).verifyDoesNotExist();
    }
}
