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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.BatchPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.batch.BatchFixtures.*;
import static org.junit.Assert.assertEquals;

/** Requires the driver "h2" to be present */
@RunWith(Arquillian.class)
public class JdbcJobRepositoryTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddDataSource.Builder(DATA_SOURCE)
                .connectionUrl("jdbc:h2:mem:" + DATA_SOURCE + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE))
                .build());
        operations.add(jdbcAddress(JDBC_READ), Values.empty().and(ModelDescriptionConstants.DATA_SOURCE, DATA_SOURCE));
        operations.add(jdbcAddress(JDBC_DELETE),
                Values.empty().and(ModelDescriptionConstants.DATA_SOURCE, DATA_SOURCE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(jdbcAddress(JDBC_CREATE));
        operations.removeIfExists(jdbcAddress(JDBC_READ));
        operations.removeIfExists(jdbcAddress(JDBC_DELETE));
        operations.removeIfExists(
                Address.subsystem("datasources").and(ModelDescriptionConstants.DATA_SOURCE, DATA_SOURCE));
    }

    @Drone private WebDriver browser;
    @Page private BatchPage page;
    @Inject private Console console;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        page.getJdbcItem().click();

        form = page.getJdbcForm();
        table = page.getJdbcTable();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, JDBC_CREATE);
        dialog.getForm().text(ModelDescriptionConstants.DATA_SOURCE, DATA_SOURCE);
        dialog.add();

        console.success();
        new ResourceVerifier(jdbcAddress(JDBC_CREATE), client).verifyExists();
    }

    @Test
    public void read() throws Exception {
        table.select(JDBC_READ);
        assertEquals(DATA_SOURCE, form.value(ModelDescriptionConstants.DATA_SOURCE));
    }

    @Test
    public void reset() throws Exception {
        table.select(JDBC_READ);
        form.reset();
        console.success();
    }

    @Test
    public void delete() throws Exception {
        table.remove(JDBC_DELETE);

        console.success();
        new ResourceVerifier(jdbcAddress(JDBC_DELETE), client).verifyDoesNotExist();
    }
}
