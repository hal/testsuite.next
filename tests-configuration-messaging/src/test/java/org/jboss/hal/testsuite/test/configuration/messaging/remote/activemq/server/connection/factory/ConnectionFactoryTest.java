package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.connection.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.JGroupsFixtures;
import org.jboss.hal.testsuite.page.configuration.MessagingRemoteActiveMQPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.RemoteActiveMQServer;

@RunWith(Arquillian.class)
public class ConnectionFactoryTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration administration = new Administration(client);

    private static final String CONNECTION_FACTORY_CREATE = "connection-factory-to-create-" + Random.name();
    private static final String CONNECTION_FACTORY_UPDATE = "connection-factory-to-update-" + Random.name();
    private static final String CONNECTION_FACTORY_DELETE = "connection-factory-to-delete-" + Random.name();

    private static final String DISCOVERY_GROUP_CREATE = "discovery-group-create-" + Random.name();
    private static final String DISCOVERY_GROUP_UPDATE = "discovery-group-update-" + Random.name();
    private static final String GENERIC_CONNECTOR_UPDATE = "generic-connection-update-" + Random.name();

    private static final String JGROUPS_CHANNEL = "jgroups-channel-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, TimeoutException, InterruptedException {
        operations.add(JGroupsFixtures.channelAddress(JGROUPS_CHANNEL), Values.of(ModelDescriptionConstants.STACK, "tcp"))
            .assertSuccess();
        createDiscoveryGroup(DISCOVERY_GROUP_CREATE);
        createDiscoveryGroup(DISCOVERY_GROUP_UPDATE);
        administration.reloadIfRequired();
        createConnectionFactory(CONNECTION_FACTORY_UPDATE, DISCOVERY_GROUP_CREATE);
        createConnectionFactory(CONNECTION_FACTORY_DELETE, DISCOVERY_GROUP_CREATE);
        createGenericConnector(GENERIC_CONNECTOR_UPDATE);
    }

    private static void createDiscoveryGroup(String name) throws IOException {
        Batch batch = new Batch();
        batch.add(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(name));
        batch.writeAttribute(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(name), "jgroups-channel", JGROUPS_CHANNEL);
        batch.writeAttribute(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(name), "jgroups-cluster", Random.name());
        operations.batch(batch).assertSuccess();
    }

    private static void createConnectionFactory(String name, String discoveryGroup) throws IOException {
        operations.add(RemoteActiveMQServer.connectionFactoryAddress(name),
            Values.of(ModelDescriptionConstants.DISCOVERY_GROUP, discoveryGroup)
                .and("entries",
                    new ModelNodeGenerator.ModelNodeListBuilder().addAll(Random.name(), Random.name(), Random.name())
                        .build())).assertSuccess();
    }

    private static void createGenericConnector(String name) throws IOException {
        operations.add(RemoteActiveMQServer.genericConnectorAddress(name), Values.of("factory-class", Random.name()))
            .assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_CREATE));
            operations.removeIfExists(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_DELETE));
            operations.removeIfExists(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_CREATE));
            operations.removeIfExists(RemoteActiveMQServer.jgroupsDiscoveryGroupAddress(DISCOVERY_GROUP_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_UPDATE));
            operations.removeIfExists(JGroupsFixtures.channelAddress(JGROUPS_CHANNEL));
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private MessagingRemoteActiveMQPage page;

    @Before
    public void navigate() {
        page.navigate();
        console.verticalNavigation().selectPrimary("msg-remote-connection-factory-item");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_CREATE),
            page.getConnectionFactoryTable(), formFragment -> {
                formFragment.text(ModelDescriptionConstants.NAME, CONNECTION_FACTORY_CREATE);
                formFragment.list("entries").add(Arrays.asList(Random.name(), Random.name(), Random.name()));
                formFragment.text(ModelDescriptionConstants.DISCOVERY_GROUP, DISCOVERY_GROUP_CREATE);
            }, ResourceVerifier::verifyExists);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_DELETE),
            page.getConnectionFactoryTable(), CONNECTION_FACTORY_DELETE);
    }

    @Test
    public void editConnectors() throws Exception {
        page.getConnectionFactoryTable().select(CONNECTION_FACTORY_UPDATE);
        crudOperations.update(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_UPDATE),
            page.getConnectionFactoryForm(), formFragment -> {
                formFragment.list("connectors").add(Collections.singletonList(GENERIC_CONNECTOR_UPDATE));
                formFragment.clear(ModelDescriptionConstants.DISCOVERY_GROUP);
            },
            resourceVerifier -> resourceVerifier.verifyAttribute("connectors",
                new ModelNodeGenerator.ModelNodeListBuilder().addAll(GENERIC_CONNECTOR_UPDATE).build()));
    }

    @Test
    public void editDiscoveryGroup() throws Exception {
        page.getConnectionFactoryTable().select(CONNECTION_FACTORY_UPDATE);
        crudOperations.update(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_UPDATE),
            page.getConnectionFactoryForm(), formFragment -> {
                formFragment.text(ModelDescriptionConstants.DISCOVERY_GROUP, DISCOVERY_GROUP_UPDATE);
                formFragment.clear("connectors");
            }, resourceVerifier -> resourceVerifier.verifyAttribute(ModelDescriptionConstants.DISCOVERY_GROUP,
                DISCOVERY_GROUP_UPDATE));
    }

    @Test
    public void editEntries() throws Exception {
        page.getConnectionFactoryTable().select(CONNECTION_FACTORY_UPDATE);
        crudOperations.update(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_UPDATE),
            page.getConnectionFactoryForm(), "entries", Arrays.asList(Random.name(), Random.name()));
    }

    @Test
    public void editFactoryType() throws Exception {
        String[] factoryTypes = {"GENERIC", "TOPIC", "QUEUE", "XA_GENERIC", "XA_QUEUE", "XA_TOPIC"};
        String factoryType = factoryTypes[Random.number(1, factoryTypes.length)];
        page.getConnectionFactoryTable().select(CONNECTION_FACTORY_UPDATE);
        crudOperations.update(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_UPDATE),
            page.getConnectionFactoryForm(), formFragment -> formFragment.select("factory-type", factoryType),
            resourceVerifier -> resourceVerifier.verifyAttribute("factory-type", factoryType));
    }

    @Test
    public void toggleHa() throws Exception {
        boolean ha =
            operations.readAttribute(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_UPDATE), "ha")
                .booleanValue(false);
        page.getConnectionFactoryTable().select(CONNECTION_FACTORY_UPDATE);
        crudOperations.update(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_UPDATE),
            page.getConnectionFactoryForm(), "ha", !ha);
    }

    @Test
    public void toggleAmq1Prefix() throws Exception {
        boolean amq1Prefix =
            operations.readAttribute(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_UPDATE), "enable-amq1-prefix")
                .booleanValue(false);
        page.getConnectionFactoryTable().select(CONNECTION_FACTORY_UPDATE);
        crudOperations.update(RemoteActiveMQServer.connectionFactoryAddress(CONNECTION_FACTORY_UPDATE),
            page.getConnectionFactoryForm(), "enable-amq1-prefix", !amq1Prefix);
    }
}
