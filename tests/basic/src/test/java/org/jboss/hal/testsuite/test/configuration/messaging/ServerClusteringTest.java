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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.MessagingServerClusteringPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.EE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

@RunWith(Arquillian.class)
public class ServerClusteringTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String anyString = Random.name();
    private static String crTab = Ids.build(MESSAGING_SERVER, BRIDGE, CREDENTIAL_REFERENCE, TAB);
    private static Values CC_PARAMS = Values.of(CLUSTER_CONNECTION_ADDRESS, anyString)
            .and(CONNECTOR_NAME, HTTP_CONNECTOR)
            .and(DISCOVERY_GROUP, anyString);
    private static Values GH_PARAMS = Values.of(GROUPING_HANDLER_ADDRESS, anyString)
            .and(TYPE, REMOTE);
    private static Values BRIDGE_PARAMS = Values.of(QUEUE_NAME, anyString)
            .andList(STATIC_CONNECTORS, HTTP_CONNECTOR);

    @BeforeClass
    public static void beforeTests() throws Exception {
        Batch batchSrvUpd = new Batch();
        batchSrvUpd.add(serverAddress(SRV_UPDATE));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvUpd);
        operations.add(broadcastGroupAddress(SRV_UPDATE, BG_UPDATE));
        operations.add(broadcastGroupAddress(SRV_UPDATE, BG_DELETE));
        operations.add(discoveryGroupAddress(SRV_UPDATE, DG_UPDATE));
        operations.add(discoveryGroupAddress(SRV_UPDATE, DG_UPDATE_ALTERNATIVES));
        operations.add(discoveryGroupAddress(SRV_UPDATE, DG_DELETE));
        operations.add(clusterConnectionAddress(SRV_UPDATE, CC_UPDATE), CC_PARAMS);
        operations.add(clusterConnectionAddress(SRV_UPDATE, CC_UPDATE_ALTERNATIVES), CC_PARAMS);
        operations.add(clusterConnectionAddress(SRV_UPDATE, CC_DELETE), CC_PARAMS);
        operations.add(groupingHandlerAddress(SRV_UPDATE, GH_UPDATE), GH_PARAMS);
        operations.add(groupingHandlerAddress(SRV_UPDATE, GH_DELETE), GH_PARAMS);
        operations.add(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), BRIDGE_PARAMS);
        operations.add(bridgeAddress(SRV_UPDATE, BRIDGE_DELETE), BRIDGE_PARAMS);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(serverAddress(SRV_UPDATE));
    }

    @Page private MessagingServerClusteringPage page;
    @Inject private Console console;
    @Inject private CrudOperations crudOperations;

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }

    // --------------- broadcast group

    @Test
    public void broadcastGroupCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getBroadcastGroupTable();
        FormFragment form = page.getBroadcastGroupForm();
        table.bind(form);

        crudOperations.create(broadcastGroupAddress(SRV_UPDATE, BG_CREATE), table, BG_CREATE);
    }

    @Test
    public void broadcastGroupUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getBroadcastGroupTable();
        FormFragment form = page.getBroadcastGroupForm();
        table.bind(form);
        table.select(BG_UPDATE);
        crudOperations.update(broadcastGroupAddress(SRV_UPDATE, BG_UPDATE), form, BROADCAST_PERIOD, 123L);
    }

    @Test
    public void broadcastGroupTryUpdateAlternatives() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getBroadcastGroupTable();
        FormFragment form = page.getBroadcastGroupForm();
        table.bind(form);
        table.select(BG_UPDATE);
        crudOperations.updateWithError(form, f -> {
            f.text(JGROUPS_CLUSTER, EE);
            f.text(JGROUPS_CHANNEL, EE);
            f.text(SOCKET_BINDING, HTTP);
        }, JGROUPS_CLUSTER, SOCKET_BINDING);
    }

    @Test
    public void broadcastGroupRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_BROADCAST_GROUP, ITEM));
        TableFragment table = page.getBroadcastGroupTable();
        FormFragment form = page.getBroadcastGroupForm();
        table.bind(form);

        crudOperations.delete(broadcastGroupAddress(SRV_UPDATE, BG_DELETE), table, BG_DELETE);
    }

    // --------------- discovery group

    @Test
    public void discoveryGroupCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getDiscoveryGroupTable();
        FormFragment form = page.getDiscoveryGroupForm();
        table.bind(form);

        crudOperations.create(discoveryGroupAddress(SRV_UPDATE, DG_CREATE), table, DG_CREATE);
    }

    @Test
    public void discoveryGroupUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getDiscoveryGroupTable();
        FormFragment form = page.getDiscoveryGroupForm();
        table.bind(form);
        table.select(DG_UPDATE);
        crudOperations.update(discoveryGroupAddress(SRV_UPDATE, DG_UPDATE), form, REFRESH_TIMEOUT, 123L);
    }

    @Test
    public void discoveryGroupTryUpdateAlternatives() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getDiscoveryGroupTable();
        FormFragment form = page.getDiscoveryGroupForm();
        table.bind(form);
        table.select(DG_UPDATE_ALTERNATIVES);
        crudOperations.updateWithError(form, f -> {
            f.text(JGROUPS_CLUSTER, EE);
            f.text(JGROUPS_CHANNEL, EE);
            f.text(SOCKET_BINDING, HTTP);
        }, JGROUPS_CLUSTER, SOCKET_BINDING);
    }

    @Test
    public void discoveryGroupRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getDiscoveryGroupTable();
        FormFragment form = page.getDiscoveryGroupForm();
        table.bind(form);

        crudOperations.delete(discoveryGroupAddress(SRV_UPDATE, DG_DELETE), table, DG_DELETE);
    }

    // --------------- cluster connection

    @Test
    public void clusterConnectionCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);

        crudOperations.create(clusterConnectionAddress(SRV_UPDATE, CC_CREATE), table, f -> {
            f.text(NAME, CC_CREATE);
            f.text(CLUSTER_CONNECTION_ADDRESS, anyString);
            f.text(CONNECTOR_NAME, HTTP_CONNECTOR);
            f.text(DISCOVERY_GROUP, anyString);
        });
    }

    @Test
    public void clusterConnectionTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, f -> f.text(NAME, CC_CREATE), CLUSTER_CONNECTION_ADDRESS);
    }

    @Test
    public void clusterConnectionUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);
        table.select(CC_UPDATE);
        crudOperations.update(clusterConnectionAddress(SRV_UPDATE, CC_UPDATE), form, CALL_TIMEOUT, 123L);
    }

    @Test
    public void clusterConnectionTryUpdateAlternatives() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);
        table.select(CC_UPDATE_ALTERNATIVES);
        crudOperations.updateWithError(form, f -> {
            f.text(DISCOVERY_GROUP, anyString);
            f.list(STATIC_CONNECTORS).add(HTTP);
        }, STATIC_CONNECTORS);
    }

    @Test
    public void clusterConnectionRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CLUSTER_CONNECTION, ITEM));
        TableFragment table = page.getClusterConnectionTable();
        FormFragment form = page.getClusterConnectionForm();
        table.bind(form);

        crudOperations.delete(clusterConnectionAddress(SRV_UPDATE, CC_DELETE), table, CC_DELETE);
    }


    // --------------- grouping handler

    @Test
    public void groupingHandlerCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_GROUPING_HANDLER, ITEM));
        TableFragment table = page.getGroupingHandlerTable();
        FormFragment form = page.getGroupingHandlerForm();
        table.bind(form);

        crudOperations.create(groupingHandlerAddress(SRV_UPDATE, GH_CREATE), table, f -> {
            f.text(NAME, GH_CREATE);
            f.text(GROUPING_HANDLER_ADDRESS, anyString);
            f.select(TYPE, REMOTE.toUpperCase());
        });
    }

    @Test
    public void groupingHandlerTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_GROUPING_HANDLER, ITEM));
        TableFragment table = page.getGroupingHandlerTable();
        FormFragment form = page.getGroupingHandlerForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, f -> f.text(NAME, GH_CREATE), GROUPING_HANDLER_ADDRESS);
    }

    @Test
    public void groupingHandlerUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_GROUPING_HANDLER, ITEM));
        TableFragment table = page.getGroupingHandlerTable();
        FormFragment form = page.getGroupingHandlerForm();
        table.bind(form);
        table.select(GH_UPDATE);
        crudOperations.update(groupingHandlerAddress(SRV_UPDATE, GH_UPDATE), form, GROUPING_HANDLER_ADDRESS,
                Random.name());
    }

    @Test
    public void groupingHandlerRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_GROUPING_HANDLER, ITEM));
        TableFragment table = page.getGroupingHandlerTable();
        FormFragment form = page.getGroupingHandlerForm();
        table.bind(form);

        crudOperations.delete(groupingHandlerAddress(SRV_UPDATE, GH_DELETE), table, GH_DELETE);
    }


    // --------------- bridge

    @Test
    public void bridgeCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));
        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeForm();
        table.bind(form);

        crudOperations.create(bridgeAddress(SRV_UPDATE, BRIDGE_CREATE), table, f -> {
            f.text(NAME, BRIDGE_CREATE);
            f.text(QUEUE_NAME, anyString);
            f.text(DISCOVERY_GROUP, anyString);
        });
    }

    @Test
    public void bridgeTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));
        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, f -> f.text(NAME, BRIDGE_CREATE), QUEUE_NAME);
    }

    @Test
    public void bridgeUpdate() throws Exception {
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), CREDENTIAL_REFERENCE);
        page.navigate(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));
        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeForm();
        table.bind(form);
        table.select(BRIDGE_UPDATE);
        page.getBridgeFormsTab().select(Ids.build(MESSAGING_SERVER, BRIDGE, ATTRIBUTES, TAB));
        crudOperations.update(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), form, CHECK_PERIOD, 123L);
    }

    @Test
    public void bridgeRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));
        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeForm();
        table.bind(form);

        crudOperations.delete(bridgeAddress(SRV_UPDATE, BRIDGE_DELETE), table, BRIDGE_DELETE);
    }

    // tests the credential-reference form of bridge

    @Test
    public void bridgeTryAddCredentialReferenceRequires() throws Exception {
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), PASSWORD);
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), CREDENTIAL_REFERENCE);
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));

        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeCRForm();
        table.bind(form);
        table.select(BRIDGE_UPDATE);
        // the order of UI navigation is important
        // first select the table item, then navigate to the tab
        page.getBridgeFormsTab().select(crTab);
        form.emptyState().mainAction();
        console.confirmationDialog().getPrimaryButton().click();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(STORE, anyString);
        addResource.getPrimaryButton().click();
        try {
            addResource.getForm().expectError(ALIAS);
        } finally {
            addResource.getSecondaryButton().click(); // close dialog to cleanup
        }
    }

    @Test
    public void bridgeTryAddCredentialReferenceEmpty() throws Exception {
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), PASSWORD);
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), CREDENTIAL_REFERENCE);
        page.navigate(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));

        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeCRForm();
        EmptyState emptyState = form.emptyState();
        table.bind(form);
        table.select(BRIDGE_UPDATE);
        page.getBridgeFormsTab().select(crTab);
        emptyState.mainAction();
        console.confirmationDialog().getPrimaryButton().click();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getPrimaryButton().click();
        try {
            addResource.getForm().expectError(STORE);
            addResource.getForm().expectError(CLEAR_TEXT);
        } finally {
            addResource.getSecondaryButton().click(); // close dialog to cleanup
        }
    }

    @Test
    public void bridgeTryAddCredentialReferenceAlternatives() throws Exception {
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), PASSWORD);
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), CREDENTIAL_REFERENCE);
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));

        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeCRForm();
        EmptyState emptyState = form.emptyState();
        table.bind(form);
        table.select(BRIDGE_UPDATE);
        page.getBridgeFormsTab().select(crTab);
        emptyState.mainAction();
        console.confirmationDialog().getPrimaryButton().click();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(STORE, anyString);
        addResource.getForm().text(CLEAR_TEXT, anyString);
        addResource.getPrimaryButton().click();
        try {
            addResource.getForm().expectError(STORE);
            addResource.getForm().expectError(CLEAR_TEXT);
        } finally {
            addResource.getSecondaryButton().click(); // close dialog to cleanup
        }
    }

    @Test
    public void bridgeAddCredentialReference() throws Exception {
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), PASSWORD);
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), CREDENTIAL_REFERENCE);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));

        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeCRForm();
        EmptyState emptyState = form.emptyState();
        table.bind(form);
        table.select(BRIDGE_UPDATE);
        page.getBridgeFormsTab().select(crTab);
        emptyState.mainAction();
        console.confirmationDialog().getPrimaryButton().click();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(CLEAR_TEXT, anyString);
        addResource.add();

        console.verifySuccess();
        new ResourceVerifier(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), client)
                .verifyAttribute(CREDENTIAL_REFERENCE + "." + CLEAR_TEXT, anyString);

    }

    @Test
    public void bridgeTryUpdateCredentialReferenceAlternatives() throws Exception {
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.writeAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), CREDENTIAL_REFERENCE, cr);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));

        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeCRForm();
        table.bind(form);
        table.select(BRIDGE_UPDATE);
        page.getBridgeFormsTab().select(crTab);

        crudOperations.updateWithError(form, f -> f.text(STORE, anyString), STORE);
    }

    @Test
    public void bridgeTryUpdateCredentialReferenceEmpty() throws Exception {
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.writeAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), CREDENTIAL_REFERENCE, cr);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));

        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeCRForm();
        table.bind(form);
        table.select(BRIDGE_UPDATE);
        page.getBridgeFormsTab().select(crTab);

        crudOperations.updateWithError(form, f -> f.clear(CLEAR_TEXT), STORE, CLEAR_TEXT);
    }

    @Test
    public void bridgeRemoveCredentialReference() throws Exception {
        operations.undefineAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.writeAttribute(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), CREDENTIAL_REFERENCE, cr);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigateAgain(SERVER, SRV_UPDATE);

        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, BRIDGE, ITEM));

        TableFragment table = page.getBridgeTable();
        FormFragment form = page.getBridgeCRForm();
        table.bind(form);
        table.select(BRIDGE_UPDATE);
        page.getBridgeFormsTab().select(crTab);

        crudOperations.deleteSingleton(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), form,
                resourceVerifier -> resourceVerifier.verifyAttributeIsUndefined(CREDENTIAL_REFERENCE));
    }
}
