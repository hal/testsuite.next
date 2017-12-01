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
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_SERVER;
import static org.jboss.hal.resources.Ids.TAB;
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
        console.verticalNavigation().selectPrimary(MESSAGING_SERVER + "-" + ITEM);
    }

    // --------------- attributes tab

    @Test
    public void attributesConnectionTTLOverride() throws Exception {
        page.getTab().select(Ids.build(MESSAGING_SERVER, ATTRIBUTES, TAB));
        FormFragment form = page.getAttributesForm();

        crudOperations.update(serverAddress(SRV_UPDATE), form, CONNECTION_TTL_OVERRIDE, 123456L);
    }

    @Test
    public void attributesPersistenceEnabled() throws Exception {
        page.getTab().select(Ids.build(MESSAGING_SERVER, ATTRIBUTES, TAB));
        FormFragment form = page.getAttributesForm();

        crudOperations.update(serverAddress(SRV_UPDATE), form, PERSISTENCE_ENABLED, false);
    }

    // --------------- management tab
    @Test
    public void managementMaskJmxDomain() throws Exception {
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, MANAGEMENT, TAB));
        FormFragment form = page.getManagementForm();
        String message = valueMustBeMasked(JMX_DOMAIN, form.value(JMX_DOMAIN));
        assertTrue(message, form.isMasked(JMX_DOMAIN));
    }

    @Test
    public void managementUnmaskJmxDomain() throws Exception {
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, MANAGEMENT, TAB));
        FormFragment form = page.getManagementForm();
        form.showSensitive(JMX_DOMAIN);
        String message = valueMustBeUnmasked(JMX_DOMAIN, form.value(JMX_DOMAIN));
        assertFalse(message, form.isMasked(JMX_DOMAIN));
    }

    // --------------- security tab
    @Test
    public void securityUpdateElytronDomain() throws Exception {
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, SECURITY, TAB));
        FormFragment form = page.getSecurityForm();

        crudOperations.update(serverAddress(SRV_UPDATE), form, ELYTRON_DOMAIN, APPLICATION_DOMAIN);
    }

    // --------------- journal tab
    @Test
    public void updateJournal() throws Exception {
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, "journal", TAB));
        FormFragment form = page.getJournalForm();
        String table = Random.name();

        crudOperations.update(serverAddress(SRV_UPDATE), form, JOURNAL_BINDING_TABLE, table);
    }

    // --------------- cluster tab
    @Test
    public void clusterPassword() throws Exception {
        page.getTab().select(Ids.build(MESSAGING_SERVER, GROUP, "cluster", TAB));
        FormFragment form = page.getClusterForm();
        String passwd = Random.name();
        crudOperations.update(serverAddress(SRV_UPDATE), form, "cluster-password", passwd);
    }


    // --------------- cluster credential reference tab
    @Test
    public void clusterCredentialReferenceAddInvalid() throws Exception {
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
    public void clusterCredentialReferenceRemove() throws Exception {
        page.getTab().select(Ids.build(MESSAGING_SERVER, CLUSTER_CREDENTIAL_REFERENCE, TAB));
        FormFragment form = page.getClusterCredentialReferenceForm();
        form.remove();

        console.verifySuccess();
        new ResourceVerifier(serverAddress(SRV_UPDATE), client)
                .verifyAttributeIsUndefined(CLUSTER_CREDENTIAL_REFERENCE);
    }


}
