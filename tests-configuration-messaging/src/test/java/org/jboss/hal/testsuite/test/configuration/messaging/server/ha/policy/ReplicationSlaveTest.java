package org.jboss.hal.testsuite.test.configuration.messaging.server.ha.policy;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;

@RunWith(Arquillian.class)
public class ReplicationSlaveTest extends AbstractHaPolicyTest {
    @BeforeClass
    public static void createReplicationMasterHaPolicy() throws IOException {
        operations.add(HAPolicy.REPLICATION_SLAVE.haPolicyAddress).assertSuccess();
    }

    @AfterClass
    public static void removeReplicationMasterHaPolicy() throws IOException, OperationException {
        operations.removeIfExists(HAPolicy.REPLICATION_SLAVE.haPolicyAddress);
    }

    @Before
    public void before() {
        page.navigate(SERVER, SRV_UPDATE);
    }

    @Test
    public void editClusterName() throws Exception {
        crudOperations.update(HAPolicy.REPLICATION_SLAVE.haPolicyAddress, page.getReplicationSlaveForm(), "cluster-name");
    }
}
