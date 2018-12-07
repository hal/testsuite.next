package org.jboss.hal.testsuite.test.configuration.messaging.server.clustering;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.BRIDGE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DISCOVERY_GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PASSWORD;
import static org.jboss.hal.dmr.ModelDescriptionConstants.QUEUE_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STORE;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_SERVER;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.BRIDGE_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.BRIDGE_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.BRIDGE_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CHECK_PERIOD;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.bridgeAddress;

@RunWith(Arquillian.class)
public class BridgeTest extends AbstractClusteringTest {

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
        page.navigateAgain(SERVER, SRV_UPDATE);
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
