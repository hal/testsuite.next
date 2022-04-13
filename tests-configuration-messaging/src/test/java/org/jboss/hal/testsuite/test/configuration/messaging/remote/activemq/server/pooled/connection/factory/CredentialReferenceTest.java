package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.pooled.connection.factory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fixtures.JGroupsFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.RemoteActiveMQServer;

@RunWith(Arquillian.class)
public class CredentialReferenceTest extends AbstractPooledConnectionFactoryTest {

    private static final String POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE =
        "pooled-connection-factory-with-credential-reference-to-update-" + Random.name();
    private static final String POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_CREATE =
        "pooled-connection-factory-with-credential-reference-to-create-" + Random.name();
    private static final String POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_DELETE =
        "pooled-connection-factory-with-credential-reference-to-delete-" + Random.name();
    private static final String DISCOVERY_GROUP = "discovery-group-" + Random.name();
    private static final String JGROUPS_CHANNEL = "jgroups-channel-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, TimeoutException, InterruptedException {
        operations.add(JGroupsFixtures.channelAddress(JGROUPS_CHANNEL), Values.of(ModelDescriptionConstants.STACK, "tcp"))
            .assertSuccess();
        createDiscoveryGroup(DISCOVERY_GROUP, JGROUPS_CHANNEL);
        administration.reloadIfRequired();
        createPooledConnectionFactory(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_CREATE, DISCOVERY_GROUP);
        createPooledConnectionFactory(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_DELETE, DISCOVERY_GROUP);
        createPooledConnectionFactory(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE, DISCOVERY_GROUP);
        addCredentialReferenceToPooledConnectionFactory(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_DELETE);
        addCredentialReferenceToPooledConnectionFactory(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE);
    }

    private static void addCredentialReferenceToPooledConnectionFactory(String connectionFactory) throws IOException {
        operations.writeAttribute(RemoteActiveMQServer.pooledConnectionFactoryAddress(connectionFactory),
            "credential-reference.clear-text", Random.name()).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(RemoteActiveMQServer.pooledConnectionFactoryAddress(
                POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.pooledConnectionFactoryAddress(
                POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_DELETE));
            operations.removeIfExists(RemoteActiveMQServer.pooledConnectionFactoryAddress(
                POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_CREATE));
            operations.removeIfExists(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP));
            operations.removeIfExists(JGroupsFixtures.channelAddress(JGROUPS_CHANNEL));
        } finally {
            client.close();
        }
    }

    @Before
    public void navigate() {
        page.navigate();
        console.verticalNavigation().selectPrimary("msg-remote-activemq-pooled-connection-factory-item");
    }

    @Test
    public void createCredentialReference() throws Exception {
        String clearTextValue = Random.name();
        page.getPooledConnectionFactoryTable().select(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_CREATE);
        crudOperations.createSingleton(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_CREATE),
            page.getPooledConnectionFactoryCredentialReferenceForm(), formFragment -> {
                formFragment.text(ModelDescriptionConstants.CLEAR_TEXT, clearTextValue);
            }, resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.clear-text", clearTextValue));
    }

    @Test
    public void removeCredentialReference() throws Exception {
        page.getPooledConnectionFactoryTable().select(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_DELETE);
        crudOperations.deleteSingleton(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_DELETE),
            page.getPooledConnectionFactoryCredentialReferenceForm(),
            resourceVerifier -> resourceVerifier.verifyAttributeIsUndefined("credential-reference"));
    }

    @Test
    public void editStore() throws Exception {
        String store = Random.name();
        page.getPooledConnectionFactoryTable().select(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE);
        crudOperations.update(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE),
            page.getPooledConnectionFactoryCredentialReferenceForm(), formFragment -> {
                formFragment.text(ModelDescriptionConstants.STORE, store);
                formFragment.text(ModelDescriptionConstants.ALIAS, Random.name());
                formFragment.clear(ModelDescriptionConstants.CLEAR_TEXT);
            }, resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.store", store));
    }

    @Test
    public void editAlias() throws Exception {
        String alias = Random.name();
        page.getPooledConnectionFactoryTable().select(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE);
        crudOperations.update(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE),
            page.getPooledConnectionFactoryCredentialReferenceForm(), formFragment -> {
                formFragment.text(ModelDescriptionConstants.STORE, Random.name());
                formFragment.text(ModelDescriptionConstants.ALIAS, alias);
                formFragment.clear(ModelDescriptionConstants.CLEAR_TEXT);
            }, resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.alias", alias));
    }

    @Test
    public void editClearText() throws Exception {
        String clearTextValue = Random.name();
        page.getPooledConnectionFactoryTable().select(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE);
        crudOperations.update(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE),
            page.getPooledConnectionFactoryCredentialReferenceForm(), formFragment -> {
                formFragment.clear(ModelDescriptionConstants.STORE);
                formFragment.clear(ModelDescriptionConstants.ALIAS);
                formFragment.text(ModelDescriptionConstants.CLEAR_TEXT, clearTextValue);
            }, resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.clear-text", clearTextValue));
    }

    @Test
    public void editType() throws Exception {
        String type = Random.name();
        page.getPooledConnectionFactoryTable().select(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE);
        crudOperations.update(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_CREDENTIAL_REFERENCE_UPDATE),
            page.getPooledConnectionFactoryCredentialReferenceForm(), formFragment -> {
                formFragment.clear(ModelDescriptionConstants.STORE);
                formFragment.clear(ModelDescriptionConstants.ALIAS);
                formFragment.text(ModelDescriptionConstants.CLEAR_TEXT, Random.name());
                formFragment.text("type", type);
            }, resourceVerifier -> resourceVerifier.verifyAttribute("credential-reference.type", type));
    }
}
