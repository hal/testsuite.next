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
package org.jboss.hal.testsuite.test.configuration.messaging.jms.bridge;

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
import org.jboss.hal.testsuite.page.configuration.MessagingJmsBridgePage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.JMS_BRIDGE;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

@RunWith(Arquillian.class)
public class ViewTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String NESTED_ATTRIBUTE_DELIMITER = ".";
    private static String anyString = Random.name();
    private static Values PARAMS;

    static {
        ModelNode targetContextModel = new ModelNode();
        targetContextModel.get("java.naming.factory.initial")
                .set("org.jboss.naming.remote.client.InitialContextFactory");
        targetContextModel.get("java.naming.provider.url").set("http-remoting://localhost:8180");

        PARAMS = Values.of(QUALITY_OF_SERVICE, AT_MOST_ONCE)
                .and(MODULE, "org.wildfly.extension.messaging-activemq")
                .and(TARGET_CONTEXT, targetContextModel)
                .and(SOURCE_CONNECTION_FACTORY, CONNECTION_FACTORY_VALUE)
                .and(SOURCE_DESTINATION, DESTINATION_QUEUE)
                .and(TARGET_CONNECTION_FACTORY, REMOTE_CONNECTION_FACTORY)
                .and(TARGET_DESTINATION, DESTINATION_QUEUE);
    }

    @BeforeClass
    public static void beforeTests() throws Exception {
        operations.add(jmsBridgeAddress(JMSBRIDGE_UPDATE), PARAMS);
        operations.add(jmsBridgeAddress(JMSBRIDGE_DELETE), PARAMS);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(jmsBridgeAddress(JMSBRIDGE_CREATE));
        operations.remove(jmsBridgeAddress(JMSBRIDGE_CREATE2));
        operations.remove(jmsBridgeAddress(JMSBRIDGE_UPDATE));
        operations.remove(jmsBridgeAddress(JMSBRIDGE_DELETE));
    }

    @Page private MessagingJmsBridgePage page;
    @Inject private Console console;
    @Inject private CrudOperations crudOperations;

    @Test
    public void editAttribute() throws Exception {
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        FormFragment form = page.getAttributesForm();
        crudOperations.update(jmsBridgeAddress(JMSBRIDGE_UPDATE), form, SELECTOR, anyString);
    }

    @Test
    public void tryEditAttribute() {
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        FormFragment form = page.getAttributesForm();
        crudOperations.updateWithError(form, f -> f.clear(MODULE), MODULE);
    }

    @Test
    public void editSourceAttribute() throws Exception {
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, SOURCE, TAB));
        FormFragment form = page.getSourceForm();
        crudOperations.update(jmsBridgeAddress(JMSBRIDGE_UPDATE), form, "source-user", anyString);
    }

    @Test
    public void addSourceCredentialReference() throws Exception {
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_PASSWORD);
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_CREDENTIAL_REFERENCE);
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, SOURCE_CREDENTIAL_REFERENCE, TAB));
        EmptyState emptyState = page.getSourceCredentialReferenceForm().emptyState();
        emptyState.mainAction();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(CLEAR_TEXT, anyString);
        dialog.add();
        console.verifySuccess();
        new ResourceVerifier(jmsBridgeAddress(JMSBRIDGE_UPDATE), client).verifyAttribute(
                SOURCE_CREDENTIAL_REFERENCE + NESTED_ATTRIBUTE_DELIMITER + CLEAR_TEXT, anyString);
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_CREDENTIAL_REFERENCE);
    }

    @Test
    public void tryAddSourceCredentialReference() throws Exception {
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_PASSWORD);
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_CREDENTIAL_REFERENCE);
        page.navigate(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, SOURCE_CREDENTIAL_REFERENCE, TAB));
        EmptyState emptyState = page.getSourceCredentialReferenceForm().emptyState();
        emptyState.mainAction();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError(CLEAR_TEXT);
        dialog.getSecondaryButton().click();
    }

    @Test
    public void editSourceCredentialReference() throws Exception {
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.writeAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_CREDENTIAL_REFERENCE, cr);
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, SOURCE_CREDENTIAL_REFERENCE, TAB));
        FormFragment form = page.getSourceCredentialReferenceForm();
        String randomText = Random.name();
        crudOperations.update(jmsBridgeAddress(JMSBRIDGE_UPDATE), form, f -> f.text(CLEAR_TEXT, randomText),
                verifier -> verifier.verifyAttribute(SOURCE_CREDENTIAL_REFERENCE + NESTED_ATTRIBUTE_DELIMITER + CLEAR_TEXT, randomText));
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_CREDENTIAL_REFERENCE);
    }

    @Test
    public void editSourcePasswordWhenCRExists() throws Exception {
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.writeAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_CREDENTIAL_REFERENCE, cr);
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, SOURCE, TAB));
        FormFragment form = page.getSourceForm();
        form.edit();
        form.text(SOURCE_PASSWORD, anyString);
        form.trySave();
        form.expectError(SOURCE_PASSWORD);
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_CREDENTIAL_REFERENCE);
    }

    @Test
    public void editCRWhenSourcePasswordExists() throws Exception {
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_CREDENTIAL_REFERENCE);
        operations.writeAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), SOURCE_PASSWORD, anyString);
        page.navigate(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, SOURCE_CREDENTIAL_REFERENCE, TAB));
        EmptyState emptyState = page.getSourceCredentialReferenceForm().emptyState();
        emptyState.mainAction();
        // there should be a confirmation dialog asking to undefine the source-password attribute
        console.confirmationDialog().getSecondaryButton().click();
    }

    @Test
    public void editTargetAttribute() throws Exception {
        page.navigate(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, "target", TAB));
        FormFragment form = page.getTargetForm();
        crudOperations.update(jmsBridgeAddress(JMSBRIDGE_UPDATE), form, "target-user", anyString);
    }

    @Test
    public void addTargetCredentialReference() throws Exception {
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_CREDENTIAL_REFERENCE);
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, TARGET_CREDENTIAL_REFERENCE, TAB));
        EmptyState emptyState = page.getTargetCredentialReferenceForm().emptyState();
        emptyState.mainAction();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(CLEAR_TEXT, anyString);
        dialog.add();
        console.verifySuccess();
        new ResourceVerifier(jmsBridgeAddress(JMSBRIDGE_UPDATE), client).verifyAttribute(
                TARGET_CREDENTIAL_REFERENCE + NESTED_ATTRIBUTE_DELIMITER + CLEAR_TEXT, anyString);
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_CREDENTIAL_REFERENCE);
    }

    @Test
    public void tryAddTargetCredentialReference() throws Exception {
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_CREDENTIAL_REFERENCE);
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, TARGET_CREDENTIAL_REFERENCE, TAB));
        EmptyState emptyState = page.getTargetCredentialReferenceForm().emptyState();
        emptyState.mainAction();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError(CLEAR_TEXT);
        dialog.getSecondaryButton().click();
    }

    @Test
    public void editTargetCredentialReference() throws Exception {
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_PASSWORD);
        operations.writeAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_CREDENTIAL_REFERENCE, cr);
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, TARGET_CREDENTIAL_REFERENCE, TAB));
        FormFragment form = page.getTargetCredentialReferenceForm();
        String randomText = Random.name();
        crudOperations.update(jmsBridgeAddress(JMSBRIDGE_UPDATE), form, f -> f.text(CLEAR_TEXT, randomText),
                v -> v.verifyAttribute(TARGET_CREDENTIAL_REFERENCE + NESTED_ATTRIBUTE_DELIMITER + CLEAR_TEXT, randomText));
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_CREDENTIAL_REFERENCE);
    }

    @Test
    public void editTargetPasswordWhenCRExists() throws Exception {
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.writeAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_CREDENTIAL_REFERENCE, cr);
        page.navigateAgain(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, TARGET, TAB));
        FormFragment form = page.getTargetForm();
        form.edit();
        form.text(TARGET_PASSWORD, anyString);
        form.trySave();
        form.expectError(TARGET_PASSWORD);
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_CREDENTIAL_REFERENCE);
    }

    @Test
    public void editCRWhenTargetPasswordExists() throws Exception {
        operations.undefineAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_CREDENTIAL_REFERENCE);
        operations.writeAttribute(jmsBridgeAddress(JMSBRIDGE_UPDATE), TARGET_PASSWORD, anyString);
        page.navigate(NAME, JMSBRIDGE_UPDATE);
        page.getTabs().select(Ids.build(JMS_BRIDGE, TARGET_CREDENTIAL_REFERENCE, TAB));
        EmptyState emptyState = page.getTargetCredentialReferenceForm().emptyState();
        emptyState.mainAction();
        // there should be a confirmation dialog asking to undefine the target-password attribute
        console.confirmationDialog().getSecondaryButton().click();
    }
}
