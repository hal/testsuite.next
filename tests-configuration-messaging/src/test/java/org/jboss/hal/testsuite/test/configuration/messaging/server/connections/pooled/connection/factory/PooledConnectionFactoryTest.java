package org.jboss.hal.testsuite.test.configuration.messaging.server.connections.pooled.connection.factory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.test.configuration.messaging.server.connections.AbstractServerConnectionsTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CONNECTORS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DISCOVERY_GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.EE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ENTRIES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PASSWORD;
import static org.jboss.hal.dmr.ModelDescriptionConstants.POOLED_CONNECTION_FACTORY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STORE;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_SERVER;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CALL_TIMEOUT;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.DG_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JGROUPS_CHANNEL;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.POOL_CONN_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.POOL_CONN_CREATE_ENTRY;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.POOL_CONN_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.POOL_CONN_TRY_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.POOL_CONN_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.connectionFactoryAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.discoveryGroupAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.pooledConnectionFactoryAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class PooledConnectionFactoryTest extends AbstractServerConnectionsTest {

    @BeforeClass
    public static void createResources() throws IOException, TimeoutException, InterruptedException {
        createServer(SRV_UPDATE);
        operations.add(discoveryGroupAddress(SRV_UPDATE, DG_UPDATE), Values.of(JGROUPS_CHANNEL, EE)).assertSuccess();
        administration.reloadIfRequired();
        operations.add(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE),
            Values.ofList(ENTRIES, Random.name()).and(DISCOVERY_GROUP, DG_UPDATE)).assertSuccess();
        operations.add(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_TRY_UPDATE),
            Values.ofList(ENTRIES, Random.name()).and(DISCOVERY_GROUP, DG_UPDATE)).assertSuccess();
        operations.add(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_DELETE),
            Values.ofList(ENTRIES, Random.name()).and(DISCOVERY_GROUP, DG_UPDATE)).assertSuccess();
    }

    @AfterClass
    public static void removeResources() throws IOException, OperationException {
        operations.removeIfExists(serverAddress(SRV_UPDATE));
    }

    @Before
    public void navigate() {
        page.navigate(SERVER, SRV_UPDATE);
    }

    @Test
    public void pooledConnectionFactoryCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);
        crudOperations.create(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, POOL_CONN_CREATE);
                formFragment.text(DISCOVERY_GROUP, DG_UPDATE);
                formFragment.list(ENTRIES).add(POOL_CONN_CREATE_ENTRY);
            }
        );
    }

    @Test
    public void pooledConnectionFactoryTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, POOL_CONN_CREATE, DISCOVERY_GROUP);
    }

    @Test
    public void pooledConnectionFactoryUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        page.getPooledFormsTab().select(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ATTRIBUTES, TAB));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        crudOperations.update(connectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), form,
                formFragment -> {
                    formFragment.number(CALL_TIMEOUT, 123L);
                    formFragment.flip("use-topology-for-load-balancing", false);
                },
                verifier -> {
                    verifier.verifyAttribute(CALL_TIMEOUT, 123L);
                    verifier.verifyAttribute("use-topology-for-load-balancing", false);
                });

    }

    @Test
    public void pooledConnectionFactoryTryUpdate() {
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);
        table.select(POOL_CONN_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.list(CONNECTORS).add(Random.name()), DISCOVERY_GROUP);
    }

    @Test
    public void pooledConnectionFactoryRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);

        crudOperations.delete(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_DELETE), table, POOL_CONN_DELETE);
    }

    // tests the credential-reference form

    @Test
    public void pooledConnectionFactoryTryAddCredentialReferenceRequires() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE);
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        // the order of UI navigation is important
        // first select the table item, then navigate to the tab
        page.getPooledFormsTab().select(CREDENTIAL_REFERENCE_TAB);
        form.emptyState().mainAction();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(STORE, Random.name());
        addResource.getPrimaryButton().click();
        try {
            addResource.getForm().expectError(ALIAS);
        } finally {
            addResource.getSecondaryButton().click(); // close dialog to cleanup
        }
    }

    @Test
    public void pooledConnectionFactoryTryAddCredentialReferenceEmpty() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE);
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        EmptyState emptyState = form.emptyState();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        page.getPooledFormsTab().select(CREDENTIAL_REFERENCE_TAB);
        emptyState.mainAction();

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
    public void pooledConnectionFactoryTryAddCredentialReferenceAlternatives() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE);
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        EmptyState emptyState = form.emptyState();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        page.getPooledFormsTab().select(CREDENTIAL_REFERENCE_TAB);
        emptyState.mainAction();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(STORE, Random.name());
        addResource.getForm().text(CLEAR_TEXT, Random.name());
        addResource.getPrimaryButton().click();
        try {
            addResource.getForm().expectError(STORE);
            addResource.getForm().expectError(CLEAR_TEXT);
        } finally {
            addResource.getSecondaryButton().click(); // close dialog to cleanup
        }
    }

    @Test
    public void pooledConnectionFactoryAddCredentialReference() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        EmptyState emptyState = form.emptyState();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        page.getPooledFormsTab().select(CREDENTIAL_REFERENCE_TAB);
        emptyState.mainAction();
        String clearText = Random.name();
        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(CLEAR_TEXT, clearText);
        addResource.add();

        console.verifySuccess();
        new ResourceVerifier(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), client)
            .verifyAttribute(CREDENTIAL_REFERENCE + "." + CLEAR_TEXT, clearText);
    }

    @Test
    public void pooledConnectionFactoryTryUpdateCredentialReferenceAlternatives() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(Random.name());
        operations.writeAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE, cr);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigateAgain(SERVER, SRV_UPDATE);

        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        page.getPooledFormsTab().select(CREDENTIAL_REFERENCE_TAB);

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);

        crudOperations.updateWithError(form, f -> f.text(STORE, Random.name()), STORE);
    }

    @Test
    public void pooledConnectionFactoryTryUpdateCredentialReferenceEmpty() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(Random.name());
        operations.writeAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE, cr);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigateAgain(SERVER, SRV_UPDATE);

        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        page.getPooledFormsTab().select(CREDENTIAL_REFERENCE_TAB);

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);

        crudOperations.updateWithError(form, f -> f.clear(CLEAR_TEXT), STORE, CLEAR_TEXT);
    }

    @Test
    public void pooledConnectionFactoryRemoveCredentialReference() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(Random.name());
        operations.writeAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE, cr);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigateAgain(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        page.getPooledFormsTab().select(CREDENTIAL_REFERENCE_TAB);

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);

        crudOperations.deleteSingleton(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), form,
            resourceVerifier -> resourceVerifier.verifyAttributeIsUndefined(CREDENTIAL_REFERENCE));
    }
}
