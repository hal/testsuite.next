package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.pooled.connection.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
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
public class PooledConnectionFactoryTest extends AbstractPooledConnectionFactoryTest {

    private static final String POOLED_CONNECTION_FACTORY_CREATE = "pooled-connection-factory-to-create-" + Random.name();
    private static final String POOLED_CONNECTION_FACTORY_DELETE = "pooled-connection-factory-to-delete-" + Random.name();

    private static final String DISCOVERY_GROUP_CREATE = "discovery-group-create-" + Random.name();

    private static final String JGROUPS_CHANNEL = "jgroups-channel-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, TimeoutException, InterruptedException {
        operations.add(JGroupsFixtures.channelAddress(JGROUPS_CHANNEL), Values.of(ModelDescriptionConstants.STACK, "tcp"))
            .assertSuccess();
        createDiscoveryGroup(DISCOVERY_GROUP_CREATE, JGROUPS_CHANNEL);
        administration.reloadIfRequired();
        createPooledConnectionFactory(POOLED_CONNECTION_FACTORY_DELETE, DISCOVERY_GROUP_CREATE);
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(
                RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_CREATE));
            operations.removeIfExists(
                RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_DELETE));
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
    public void create() throws Exception {
        crudOperations.create(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_CREATE),
            page.getPooledConnectionFactoryTable(), formFragment -> {
                formFragment.text(ModelDescriptionConstants.NAME, POOLED_CONNECTION_FACTORY_CREATE);
                formFragment.list("entries").add(Arrays.asList(Random.name(), Random.name(), Random.name()));
                formFragment.text(ModelDescriptionConstants.DISCOVERY_GROUP, DISCOVERY_GROUP_CREATE);
            }, ResourceVerifier::verifyExists);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(RemoteActiveMQServer.pooledConnectionFactoryAddress(POOLED_CONNECTION_FACTORY_DELETE),
            page.getPooledConnectionFactoryTable(), POOLED_CONNECTION_FACTORY_DELETE);
    }
}
