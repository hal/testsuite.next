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
package org.jboss.hal.testsuite.test.runtime.messaging;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.runtime.MessagingServerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.testsuite.test.runtime.messaging.MessagingFixtures.*;
import static org.junit.Assert.assertTrue;

/** This class just tests that the tables and forms are there. */
@RunWith(Arquillian.class)
public class ServerApplicationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        Batch batchSrvRead = new Batch();
        batchSrvRead.add(serverAddress(SRV_READ));
        batchSrvRead.add(serverPathAddress(SRV_READ, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvRead.add(serverPathAddress(SRV_READ, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvRead.add(serverPathAddress(SRV_READ, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvRead.add(serverPathAddress(SRV_READ, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvRead);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(serverAddress(SRV_READ));
    }

    @Inject private Console console;
    @Page private MessagingServerPage page;

    @Before
    public void setUp() throws Exception {
        page.navigate(Ids.MESSAGING_SERVER, SRV_READ);
    }

    @Test
    public void connection() {
        console.verticalNavigation().selectPrimary(Ids.MESSAGING_SERVER_CONNECTION_ITEM);
        assertTrue(page.getConnectionTable().getRoot().isDisplayed());
        assertTrue(page.getConnectionForm().getRoot().isDisplayed());
    }

    @Test
    public void consumer() {
        console.verticalNavigation().selectPrimary(Ids.MESSAGING_SERVER_CONSUMER_ITEM);
        assertTrue(page.getConsumerTable().getRoot().isDisplayed());
        assertTrue(page.getConsumerForm().getRoot().isDisplayed());
    }

    @Test
    public void producer() {
        console.verticalNavigation().selectPrimary(Ids.MESSAGING_SERVER_PRODUCER_ITEM);
        assertTrue(page.getProducerTable().getRoot().isDisplayed());
        assertTrue(page.getProducerForm().getRoot().isDisplayed());
    }

    @Test
    public void connector() {
        console.verticalNavigation().selectPrimary(Ids.MESSAGING_SERVER_CONNECTOR_ITEM);
        assertTrue(page.getConnectorTable().getRoot().isDisplayed());
        assertTrue(page.getConnectorForm().getRoot().isDisplayed());
    }

    @Test
    public void role() {
        console.verticalNavigation().selectPrimary(Ids.MESSAGING_SERVER_ROLE_ITEM);
        assertTrue(page.getRoleTable().getRoot().isDisplayed());
        assertTrue(page.getRoleForm().getRoot().isDisplayed());
    }

    @Test
    public void transaction() {
        console.verticalNavigation().selectPrimary(Ids.MESSAGING_SERVER_TRANSACTION_ITEM);
        assertTrue(page.getTransactionTable().getRoot().isDisplayed());
        assertTrue(page.getTransactionForm().getRoot().isDisplayed());
    }
}
