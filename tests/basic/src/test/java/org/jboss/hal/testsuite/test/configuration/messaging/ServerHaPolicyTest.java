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
package org.jboss.hal.testsuite.test.configuration.messaging;

import java.util.function.Supplier;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.page.configuration.MessagingServerHaPolicyPage;
import org.jboss.hal.testsuite.util.Library;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

@RunWith(Arquillian.class)
public class ServerHaPolicyTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String anyString = Random.name();

    @BeforeClass
    public static void beforeTests() throws Exception {
        Batch batchSrvUpd = new Batch();
        batchSrvUpd.add(serverAddress(SRV_UPDATE));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvUpd);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(serverAddress(SRV_UPDATE));
    }

    @Page private MessagingServerHaPolicyPage page;
    @Inject private Console console;
    @Inject private CrudOperations crudOperations;
    private ColumnFragment column;
    private WizardFragment wizard;

    private void refreshConfigurationColumn() throws Exception {
        // after the previous operations, it is necessary to refresh the "server" column
        console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(MESSAGING_ACTIVEMQ)
                .append(Ids.MESSAGING_CATEGORY, SERVER))
                .column(MESSAGING_SERVER_CONFIGURATION)
                .refresh();

        // navigate again to the messaging-server-settings column
        setUp();
    }

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(MESSAGING_ACTIVEMQ)
                .append(Ids.MESSAGING_CATEGORY, SERVER)
                .append(MESSAGING_SERVER_CONFIGURATION, messagingServer(SRV_UPDATE)))
                .column(MESSAGING_SERVER_SETTINGS);
    }

    @Test
    public void createSharedStoreLiveMaster() throws Exception {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        wizard = console.wizard();
        // clicks on the shared-store radio box
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE)).click();
        wizard.next(MESSAGING_HA_SHARED_STORE_MASTER);
        // clicks on the "Live server (master)" radio box
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE_MASTER)).click();
        // finish the wizard
        wizard.next();

        console.verifySuccess();
        new ResourceVerifier(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER), client)
                .verifyExists();
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER));
    }

    // use default values, do not select any radio box.
    @Test
    public void createReplicationLiveOnly() throws Exception {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        wizard = console.wizard();
        // clicks the "next" button, do not select any radio box, the default is "replication"
        wizard.next(MESSAGING_HA_REPLICATION_LIVE_ONLY);
        // "finish" the wizard, do not select any radio box
        wizard.next();

        console.verifySuccess();
        new ResourceVerifier(haPolicyAddress(SRV_UPDATE, LIVE_ONLY), client)
                .verifyExists();
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, LIVE_ONLY));
    }

    @Test
    public void createReplicationSlave() throws Exception {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        wizard = console.wizard();
        // clicks the "next" button, do not select any radio box, the default is "replication"
        wizard.next(MESSAGING_HA_REPLICATION_LIVE_ONLY);
        wizard.getRoot().findElement(By.id(MESSAGING_HA_REPLICATION_SLAVE)).click();
        wizard.next();

        console.verifySuccess();
        new ResourceVerifier(haPolicyAddress(SRV_UPDATE, REPLICATION_SLAVE), client)
                .verifyExists();
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, REPLICATION_SLAVE));
    }

    // test the back/next wizard workflow
    // first select replication/live-master, then goes back and select shared-store/colocated
    @Test
    public void createSharedStoreColocatedBackForth() throws Exception {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        wizard = console.wizard();
        // selects the "replication" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_REPLICATION)).click();
        // clicks the "next" button and waits for the dom id
        wizard.next(MESSAGING_HA_REPLICATION_LIVE_ONLY);
        // selects the "Live server (master)" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_REPLICATION_MASTER)).click();
        // clicks the "back" button and waits for the "shared-store" dom id
        wizard.back(By.id(MESSAGING_HA_SHARED_STORE));
        // clicks the "shared-store" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE)).click();
        // clicks "next"
        wizard.next(MESSAGING_HA_SHARED_STORE_MASTER);
        // clicks on the "Colocate live and backup server" radio box
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE_COLOCATED)).click();
        // finish the wizard
        wizard.next();

        console.verifySuccess();
        new ResourceVerifier(haPolicyAddress(SRV_UPDATE, SHARED_STORE_COLOCATED), client)
                .verifyExists();
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, SHARED_STORE_COLOCATED));
    }

    // test the back/next wizard workflow with default values
    // first select replication/live-master, then goes back and select shared-store, do not select any item and finish
    @Test
    public void createSharedStoreLiveMasterBackForth() throws Exception {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        wizard = console.wizard();
        // selects the "replication" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_REPLICATION)).click();
        // clicks the "next" button and waits for the dom id
        wizard.next(MESSAGING_HA_REPLICATION_LIVE_ONLY);
        // selects the "Live server (master)" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_REPLICATION_MASTER)).click();
        // clicks the "back" button and waits for the "shared-store" dom id
        wizard.back(By.id(MESSAGING_HA_SHARED_STORE));
        // clicks the "shared-store" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE)).click();
        // clicks "next"
        wizard.next(MESSAGING_HA_SHARED_STORE_MASTER);
        // finish the wizard with default "Live server (master)" radio selected
        wizard.next();

        console.verifySuccess();
        new ResourceVerifier(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER), client)
                .verifyExists();
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER));
    }

    @Test
    public void removeSharedStoreLiveMaster() throws Exception {
        operations.add(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER));
        refreshConfigurationColumn();
        column.selectItem(MESSAGING_SERVER_HA_POLICY).dropdown().click("Remove");
        console.confirmationDialog().confirm();

        console.verifySuccess();
        new ResourceVerifier(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER), client)
                .verifyDoesNotExist();
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER));
    }

    @Test
    public void viewEmptyHaPolicy() {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).dropdown().click("View");

        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.MESSAGING_SERVER_HA_POLICY)
                .with(SERVER, SRV_UPDATE)
                .build();
        console.verify(placeRequest);
    }

    @Test
    public void editReplicationLiveOnly() throws Exception {
        editTemplate(LIVE_ONLY, SCALE_DOWN_CLUSTER_NAME, () -> page.getReplicationLiveOnlyForm());
    }

    @Test
    public void editReplicationMaster() throws Exception {
        editTemplate(REPLICATION_MASTER, CLUSTER_NAME, () -> page.getReplicationMasterForm());
    }

    @Test
    public void editReplicationSlave() throws Exception {
        editTemplate(REPLICATION_SLAVE, CLUSTER_NAME, () -> page.getReplicationSlaveForm());
    }

    @Test
    public void editReplicationColocated() throws Exception {
        operations.add(haPolicyAddress(SRV_UPDATE, REPLICATION_COLOCATED));
        console.waitNoNotification();
        refreshConfigurationColumn();
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        Library.letsSleep(1000);
        FormFragment form = page.getReplicationColocatedForm();
        crudOperations.update(haPolicyAddress(SRV_UPDATE, REPLICATION_COLOCATED), form, MAX_BACKUPS, 123);
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, REPLICATION_COLOCATED));

    }

    @Test
    public void editSharedStoreMaster() throws Exception {
        operations.add(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER));
        console.waitNoNotification();
        refreshConfigurationColumn();
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        Library.letsSleep(1000);
        FormFragment form = page.getSharedStoreMasterForm();
        crudOperations.update(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER), form, FAILOVER_ON_SERVER_SHUTDOWN,
                true);
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER));
    }

    @Test
    public void editSharedStoreSlave() throws Exception {
        editTemplate(SHARED_STORE_SLAVE, SCALE_DOWN_CLUSTER_NAME, () -> page.getSharedStoreSlaveForm());
    }

    @Test
    public void editSharedStoreColocated() throws Exception {
        operations.add(haPolicyAddress(SRV_UPDATE, SHARED_STORE_COLOCATED));
        console.waitNoNotification();
        refreshConfigurationColumn();
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        Library.letsSleep(1000);
        FormFragment form = page.getSharedStoreColocatedForm();
        crudOperations.update(haPolicyAddress(SRV_UPDATE, SHARED_STORE_COLOCATED), form, MAX_BACKUPS, 123);
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, SHARED_STORE_COLOCATED));
    }

    @Test
    public void createPolicyFromView() throws Exception {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).dropdown().click("View");
        EmptyState emptyState = page.getEmptyState();
        console.waitNoNotification();
        emptyState.mainAction();

        wizard = console.wizard();
        // clicks on the shared-store radio box
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE)).click();
        wizard.next(MESSAGING_HA_SHARED_STORE_MASTER);
        // clicks on the "Live server (master)" radio box
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE_MASTER)).click();
        // finish the wizard
        wizard.next();

        console.verifySuccess();
        new ResourceVerifier(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER), client)
                .verifyExists();
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, SHARED_STORE_MASTER));
    }

    private void editTemplate(String haPolicy, String attribute, Supplier<FormFragment> lazyForm) throws Exception {
        operations.add(haPolicyAddress(SRV_UPDATE, haPolicy));
        console.waitNoNotification();
        refreshConfigurationColumn();
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        Library.letsSleep(1000);
        FormFragment form = lazyForm.get();
        crudOperations.update(haPolicyAddress(SRV_UPDATE, haPolicy), form, attribute, anyString);
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, haPolicy));
    }
}
