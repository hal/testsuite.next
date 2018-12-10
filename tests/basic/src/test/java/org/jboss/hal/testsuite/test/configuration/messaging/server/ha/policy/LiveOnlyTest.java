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
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;

@RunWith(Arquillian.class)
public class LiveOnlyTest extends AbstractHaPolicyTest {

    @BeforeClass
    public static void createHaPolicy() throws IOException {
        operations.add(HAPolicy.LIVE_ONLY.haPolicyAddress).assertSuccess();
    }

    @AfterClass
    public static void removeHaPolicy() throws IOException, OperationException {
        operations.removeIfExists(HAPolicy.LIVE_ONLY.haPolicyAddress);
    }

    @Before
    public void before() {
        page.navigate(SERVER, SRV_UPDATE);
    }

    @Test
    public void editScaleDownClusterName() throws Exception {
        crudOperations.update(HAPolicy.LIVE_ONLY.haPolicyAddress, page.getReplicationLiveOnlyForm(),
            "scale-down-cluster-name");
    }
}
