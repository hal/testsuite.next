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
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.MessagingServerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MANAGEMENT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.Message.valueMustBeMasked;
import static org.jboss.hal.testsuite.Message.valueMustBeUnmasked;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class ServerConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeTests() throws Exception {
        operations.add(serverAddress(SRV_UPDATE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(serverAddress(SRV_UPDATE));
    }

    @Page private MessagingServerPage page;
    @Inject private Console console;
    @Inject private CrudOperations crudOperations;

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }

    // --------------- attributes tab

    @Test
    public void attributesConnectionTTLOverride() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, ATTRIBUTES, TAB));
        FormFragment form = page.getAttributesForm();

        crudOperations.update(serverAddress(SRV_UPDATE), form, CONNECTION_TTL_OVERRIDE, 123456L);
    }

    @Test
    public void attributesPersistenceEnabled() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, ATTRIBUTES, TAB));
        FormFragment form = page.getAttributesForm();

        crudOperations.update(serverAddress(SRV_UPDATE), form, PERSISTENCE_ENABLED, false);
    }

    // --------------- management tab

    @Test
    public void managementMaskJmxDomain() {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, MANAGEMENT, TAB));
        FormFragment form = page.getManagementForm();
        String message = valueMustBeMasked(JMX_DOMAIN, form.value(JMX_DOMAIN));
        assertTrue(message, form.isMasked(JMX_DOMAIN));
    }

    @Test
    public void managementUnmaskJmxDomain() {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, MANAGEMENT, TAB));
        FormFragment form = page.getManagementForm();
        form.showSensitive(JMX_DOMAIN);
        String message = valueMustBeUnmasked(JMX_DOMAIN, form.value(JMX_DOMAIN));
        assertFalse(message, form.isMasked(JMX_DOMAIN));
    }

    // --------------- security tab

    @Test
    public void securityUpdateElytronDomain() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, SECURITY, TAB));
        FormFragment form = page.getSecurityForm();

        crudOperations.update(serverAddress(SRV_UPDATE), form, ELYTRON_DOMAIN, APPLICATION_DOMAIN);
    }

    // --------------- journal tab

    @Test
    public void updateJournal() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, "journal", TAB));
        FormFragment form = page.getJournalForm();
        String table = Random.name();

        crudOperations.update(serverAddress(SRV_UPDATE), form, JOURNAL_BINDING_TABLE, table);
    }

    // --------------- cluster tab
    @Test
    public void clusterPassword() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, "cluster", TAB));
        FormFragment form = page.getClusterForm();
        String passwd = Random.name();
        crudOperations.update(serverAddress(SRV_UPDATE), form, "cluster-password", passwd);
    }


    // --------------- cluster credential reference tab
    @Test
    public void clusterCredentialReferenceAddInvalid() {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, CLUSTER_CREDENTIAL_REFERENCE, TAB));
        EmptyState emptyState = page.getClusterCredentialReferenceEmptyState();
        waitGui().until().element(emptyState.getRoot()).is().visible();
        emptyState.mainAction();
        console.confirmationDialog().confirm();
        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(STORE, Random.name());
        console.waitNoNotification();
        addResource.getPrimaryButton().click();
        addResource.getForm().expectError(ALIAS);
    }

    @Test
    public void clusterCredentialReferenceAddSuccess() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, CLUSTER_CREDENTIAL_REFERENCE, TAB));
        EmptyState emptyState = page.getClusterCredentialReferenceEmptyState();
        waitGui().until().element(emptyState.getRoot()).is().visible();
        emptyState.mainAction();
        console.confirmationDialog().confirm();

        String passwd = Random.name();
        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(CLEAR_TEXT, passwd);
        addResource.add();

        console.verifySuccess();
        new ResourceVerifier(serverAddress(SRV_UPDATE), client)
                .verifyAttribute(CLUSTER_CREDENTIAL_REFERENCE + "." + CLEAR_TEXT, passwd);
    }

    @Test
    public void clusterCredentialReferenceEditInvalid() {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, CLUSTER_CREDENTIAL_REFERENCE, TAB));
        FormFragment form = page.getClusterCredentialReferenceForm();
        crudOperations.updateWithError(form, f ->
                        f.text(ALIAS, Random.name())
                , STORE);
    }

    @Test
    public void clusterCredentialReferenceRemove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
        page.getTab().select(Ids.build(MESSAGING_SERVER, CLUSTER_CREDENTIAL_REFERENCE, TAB));
        FormFragment form = page.getClusterCredentialReferenceForm();
        crudOperations.deleteSingleton(serverAddress(SRV_UPDATE), form,
                resourceVerifier -> resourceVerifier.verifyAttributeIsUndefined(CLUSTER_CREDENTIAL_REFERENCE));
    }

    // --------------- directory / Binding

    @Test
    public void bindingDirectory1Update() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_BINDING_DIRECTORY, ITEM));

        FormFragment form = page.getBindingDirectoryForm();
        crudOperations.update(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), form, PATH);
    }

    @Test
    public void bindingDirectory2Reset() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_BINDING_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getBindingDirectoryForm();
        crudOperations.reset(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), form);
    }

    @Test
    public void bindingDirectory3Remove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_BINDING_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getBindingDirectoryForm();
        crudOperations.deleteSingleton(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), form);
    }

    @Test
    public void bindingDirectory4Add() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_BINDING_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getBindingDirectoryForm();
        crudOperations.createSingleton(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), form);
    }

    // --------------- directory / Journal

    @Test
    public void journalDirectory1Update() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_JOURNAL_DIRECTORY, ITEM));

        FormFragment form = page.getJournalDirectoryForm();
        crudOperations.update(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), form, PATH);
    }

    @Test
    public void journalDirectory2Reset() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_JOURNAL_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getJournalDirectoryForm();
        crudOperations.reset(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), form);
    }

    @Test
    public void journalDirectory3Remove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_JOURNAL_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getJournalDirectoryForm();
        crudOperations.deleteSingleton(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), form);
    }

    @Test
    public void journalDirectory4Add() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_JOURNAL_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getJournalDirectoryForm();
        crudOperations.createSingleton(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), form);
    }


    // --------------- directory / Large Messages

    @Test
    public void largeMessagesDirectory1Update() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_LARGE_MESSAGES_DIRECTORY, ITEM));

        FormFragment form = page.getLargeMessagesDirectoryForm();
        crudOperations.update(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), form, PATH);
    }

    @Test
    public void largeMessagesDirectory2Reset() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_LARGE_MESSAGES_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getLargeMessagesDirectoryForm();
        crudOperations.reset(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), form);
    }

    @Test
    public void largeMessagesDirectory3Remove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_LARGE_MESSAGES_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getLargeMessagesDirectoryForm();
        crudOperations.deleteSingleton(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), form);
    }

    @Test
    public void largeMessagesDirectory4Add() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_LARGE_MESSAGES_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getLargeMessagesDirectoryForm();
        crudOperations.createSingleton(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), form);
    }

    // --------------- directory / Paging

    @Test
    public void pagingDirectory1Update() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_PAGING_DIRECTORY, ITEM));

        FormFragment form = page.getPagingDirectoryForm();
        crudOperations.update(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), form, PATH);
    }

    @Test
    public void pagingDirectory2Reset() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_PAGING_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getPagingDirectoryForm();
        crudOperations.reset(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), form);
    }

    @Test
    public void pagingDirectory3Remove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_PAGING_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getPagingDirectoryForm();
        crudOperations.deleteSingleton(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), form);
    }

    @Test
    public void pagingDirectory4Add() throws Exception {
        console.verticalNavigation()
                .selectSecondary(MESSAGING_SERVER_DIRECTORY_ITEM,
                        Ids.build(MESSAGING_SERVER_PAGING_DIRECTORY, Ids.ITEM));

        FormFragment form = page.getPagingDirectoryForm();
        crudOperations.createSingleton(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), form);
    }
}
