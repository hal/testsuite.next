package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.pooled.connection.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.test.configuration.jgroups.JGroupsFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.RemoteActiveMQServer;

@RunWith(Arquillian.class)
public class AttributesTest extends AbstractPooledConnectionFactoryTest {

    private static final String POOLED_CONNECTION_FACTORY_UPDATE = "pooled-connection-factory-to-update-" + Random.name();
    private static final String DISCOVERY_GROUP_CREATE = "discovery-group-to-be-updated-" + Random.name();
    private static final String DISCOVERY_GROUP_UPDATE = "discovery-group-to-be-updated-" + Random.name();
    private static final String GENERIC_CONNECTOR_UPDATE = "generic-connector-" + Random.name();
    private static final String JGROUPS_CHANNEL = "jgroups-channel-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, TimeoutException, InterruptedException {
        operations.add(JGroupsFixtures.channelAddress(JGROUPS_CHANNEL), Values.of(ModelDescriptionConstants.STACK, "tcp"))
            .assertSuccess();
        createDiscoveryGroup(DISCOVERY_GROUP_CREATE, JGROUPS_CHANNEL);
        createDiscoveryGroup(DISCOVERY_GROUP_UPDATE, JGROUPS_CHANNEL);
        administration.reloadIfRequired();
        createPooledConnectionFactory(POOLED_CONNECTION_FACTORY_UPDATE, DISCOVERY_GROUP_CREATE);
        createGenericConnector(GENERIC_CONNECTOR_UPDATE);
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_CREATE));
            operations.removeIfExists(RemoteActiveMQServer.discoveryGroupAddress(DISCOVERY_GROUP_UPDATE));
            operations.removeIfExists(RemoteActiveMQServer.genericConnectorAddress(GENERIC_CONNECTOR_UPDATE));
            operations.removeIfExists(JGroupsFixtures.channelAddress(JGROUPS_CHANNEL));
        } finally {
            client.close();
        }
    }

    @Before
    public void navigate() {
        page.navigate();
        console.verticalNavigation().selectPrimary("msg-remote-activemq-pooled-connection-factory-item");
        page.getPooledConnectionFactoryTable().select(POOLED_CONNECTION_FACTORY_UPDATE);
    }

    @Test
    public void toggleAllowLocalTransactions() throws Exception {
        boolean allowLocalTransactions = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            "allow-local-transactions").booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "allow-local-transactions", !allowLocalTransactions);
    }

    @Test
    public void toggleAutoGroup() throws Exception {
        boolean autoGroup = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "auto-group")
            .booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "auto-group", !autoGroup);
    }

    @Test
    public void toggleBlockOnAcknowledge() throws Exception {
        boolean blockOnAcknowledge = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "block-on-acknowledge")
            .booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "block-on-acknowledge", !blockOnAcknowledge);
    }

    @Test
    public void toggleBlockOnDurableSend() throws Exception {
        boolean blockOnDurableSend = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            "block-on-durable-send").booleanValue(true);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "block-on-durable-send", !blockOnDurableSend);
    }

    @Test
    public void toggleBlockOnNonDurableSend() throws Exception {
        boolean blockOnNonDurableSend = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            "block-on-non-durable-send").booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "block-on-non-durable-send", !blockOnNonDurableSend);
    }

    @Test
    public void toggleCacheLargeMessageClient() throws Exception {
        boolean cacheLargeMessageClient = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            "cache-large-message-client").booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "cache-large-message-client", !cacheLargeMessageClient);
    }

    @Test
    public void editCallFailoverTimeout() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "call-failover-timeout", Long.valueOf(Random.number()));
    }

    @Test
    public void editCallTimeout() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "call-timeout", Long.valueOf(Random.number()));
    }

    @Test
    public void editClientFailureCheckPeriod() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "client-failure-check-period", Long.valueOf(Random.number()));
    }

    @Test
    public void editClientId() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "client-id");
    }

    @Test
    public void toggleCompressLargeMessages() throws Exception {
        boolean compressLargeMessages = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            "compress-large-messages").booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "compress-large-messages", !compressLargeMessages);
    }

    @Test
    public void editConfirmationWindowSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "confirmation-window-size", Random.number());
    }

    @Test
    public void editConnectionLoadBalancingPolicyClassName() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "connection-load-balancing-policy-class-name");
    }

    @Test
    public void editConnectionTtl() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "connection-ttl", Long.valueOf(Random.number()));
    }

    @Test
    public void editConnectors() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), formFragment -> {
                formFragment.list("connectors").add(GENERIC_CONNECTOR_UPDATE);
                formFragment.clear("discovery-group");
            },
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue("connectors", GENERIC_CONNECTOR_UPDATE));
    }

    @Test
    public void editConsumerMaxRate() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "consumer-max-rate", Random.number());
    }

    @Test
    public void editConsumerWindowSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "consumer-window-size", Random.number());
    }

    @Test
    public void editDeserializationBlackList() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "deserialization-black-list",
            Arrays.asList(Random.name(), Random.name(), Random.name()));
    }

    @Test
    public void editDeserializationWhiteList() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "deserialization-white-list",
            Arrays.asList(Random.name(), Random.name(), Random.name()));
    }

    @Test
    public void editDiscoveryGroup() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), formFragment -> {
                formFragment.clear("connectors");
                formFragment.text("discovery-group", DISCOVERY_GROUP_UPDATE);
            },
            resourceVerifier -> resourceVerifier.verifyAttribute("discovery-group", DISCOVERY_GROUP_UPDATE));
    }

    @Test
    public void editDupsOkBatchSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "dups-ok-batch-size", Random.number());
    }

    @Test
    public void toggleEnlistmentTrace() throws Exception {
        boolean enlistmentTrace = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "enlistment-trace")
            .booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "enlistment-trace", !enlistmentTrace);
    }

    @Test
    public void editEntries() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "entries", Arrays.asList(Random.name(), Random.name(), Random.name()));
    }

    @Test
    public void toggleFailoverOnInitialConnection() throws Exception {
        boolean failoverOnInitialConnection = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            "failover-on-initial-connection").booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "failover-on-initial-connection", !failoverOnInitialConnection);
    }

    @Test
    public void editGroupId() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "group-id");
    }

    @Test
    public void toggleHa() throws Exception {
        boolean ha = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "ha")
            .booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "ha", !ha);
    }

    @Test
    public void editInitialConnectAttempts() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "initial-connect-attempts", Random.number());
    }

    @Test
    public void editInitialMessagePacketSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "initial-message-packet-size", Random.number());
    }

    @Test
    public void editJndiParams() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "jndi-params");
    }

    @Test
    public void editManagedConnectionPool() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "managed-connection-pool");
    }

    @Test
    public void editMaxPoolSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "max-pool-size", Random.number());
    }

    @Test
    public void editMaxRetryInterval() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "max-retry-interval", Long.valueOf(Random.number()));
    }

    @Test
    public void editMinLargeMessageSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "min-large-message-size", Random.number());
    }

    @Test
    public void editMinPoolSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "min-pool-size", Random.number());
    }

    @Test
    public void editPassword() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "password");
    }

    @Test
    public void togglePreAcknowledge() throws Exception {
        boolean preAcknowledge = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "pre-acknowledge")
            .booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "pre-acknowledge", !preAcknowledge);
    }

    @Test
    public void editProducerMaxRate() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "producer-max-rate", Random.number());
    }

    @Test
    public void editProducerWindowSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "producer-window-size", Random.number());
    }

    @Test
    public void editProtocolManagerFactory() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "protocol-manager-factory");
    }

    @Test
    public void toggleRebalanceConnections() throws Exception {
        boolean rebalanceConnections = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            "rebalance-connections").booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "rebalance-connections", !rebalanceConnections);
    }

    @Test
    public void editReconnectAttempts() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "reconnect-attempts", Random.number());
    }

    @Test
    public void editRetryInterval() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "retry-interval", Long.valueOf(Random.number()));
    }

    @Test
    public void editRetryIntervalMultiplier() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "retry-interval-multiplier", Random.numberDouble());
    }

    @Test
    public void editScheduledThreadPoolMaxSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "scheduled-thread-pool-max-size", Random.number());
    }

    @Test
    public void editSetupAttempts() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "setup-attempts", Random.number());
    }

    @Test
    public void editSetupInterval() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "setup-interval", Long.valueOf(Random.number()));
    }

    @Test
    public void toggleStatisticsEnabled() throws Exception {
        boolean statisticsEnabled = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "statistics-enabled")
            .booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "statistics-enabled", !statisticsEnabled);
    }

    @Test
    public void editThreadPoolMaxSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "thread-pool-max-size", Random.number());
    }

    @Test
    public void editTransaction() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "transaction");
    }

    @Test
    public void editTransactionBatchSize() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "transaction-batch-size", Random.number());
    }

    @Test
    public void toggleUseAutoRecovery() throws Exception {
        boolean useAutoRecovery = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "use-auto-recovery")
            .booleanValue(true);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "use-auto-recovery", !useAutoRecovery);
    }

    @Test
    public void toggleUseGlobalPools() throws Exception {
        boolean useGlobalPools = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "use-global-pools")
            .booleanValue(true);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "use-global-pools", !useGlobalPools);
    }

    @Test
    public void toggleUseJndi() throws Exception {
        boolean useJndi = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "use-jndi")
            .booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "use-jndi", !useJndi);
    }

    @Test
    public void toggleUseLocalTx() throws Exception {
        boolean useLocalTx = operations.readAttribute(
            RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE), "use-local-tx")
            .booleanValue(false);
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "use-local-tx", !useLocalTx);
    }

    @Test
    public void editUser() throws Exception {
        crudOperations.update(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_UPDATE),
            page.getPooledConnectionFactoryForm(), "user");
    }

}
