package org.jboss.hal.testsuite.test.configuration.messaging.server.ha.policy;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

@RunWith(Arquillian.class)
public class ViewTest extends AbstractHaPolicyTest {

    private final HAPolicyConsumer createPolicyInView = haPolicy -> {
        EmptyState emptyState = page.getEmptyState();
        console.waitNoNotification();
        emptyState.mainAction();
        wizard = console.wizard();
        wizard.getRoot().findElement(By.id(haPolicy.basicStrategy)).click();
        wizard.next(haPolicy.serverRole);
        wizard.getRoot().findElement(By.id(haPolicy.serverRole)).click();
        wizard.finish();
        console.verifySuccess();
        new ResourceVerifier(haPolicy.haPolicyAddress, client)
            .verifyExists();
        operations.removeIfExists(haPolicy.haPolicyAddress);
    };

    private final HAPolicyConsumer removePolicyInView = haPolicy -> {
        console.waitNoNotification();
        page.getRootContainer().findElement(ByJQuery.selector(".clickable:contains('Remove')")).click();
        console.confirmationDialog().confirm();
        new ResourceVerifier(haPolicy.haPolicyAddress, client).verifyDoesNotExist();
    };

    @Test
    public void createReplicationLiveOnly() throws Exception {
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.LIVE_ONLY.create(createPolicyInView);
    }

    @Test
    public void removeReplicationLiveOnly() throws Exception {
        operations.add(HAPolicy.LIVE_ONLY.haPolicyAddress);
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.LIVE_ONLY.remove(removePolicyInView);
    }

    @Test
    public void createReplicationMaster() throws Exception {
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.REPLICATION_MASTER.create(createPolicyInView);
    }

    @Test
    public void removeReplicationMaster() throws Exception {
        operations.add(HAPolicy.REPLICATION_MASTER.haPolicyAddress);
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.REPLICATION_MASTER.remove(removePolicyInView);
    }

    @Test
    public void createReplicationSlave() throws Exception {
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.REPLICATION_SLAVE.create(createPolicyInView);
    }

    @Test
    public void removeReplicationSlave() throws Exception {
        operations.add(HAPolicy.REPLICATION_SLAVE.haPolicyAddress);
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.REPLICATION_SLAVE.remove(removePolicyInView);
    }

    @Test
    public void createReplicationColocated() throws Exception {
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.REPLICATION_COLOCATED.create(createPolicyInView);
    }

    @Test
    public void removeReplicationColocated() throws Exception {
        operations.add(HAPolicy.REPLICATION_COLOCATED.haPolicyAddress);
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.REPLICATION_COLOCATED.remove(removePolicyInView);
    }

    @Test
    public void createSharedStoreMaster() throws Exception {
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.SHARED_STORE_MASTER.create(createPolicyInView);
    }

    @Test
    public void removeSharedStoreMaster() throws Exception {
        operations.add(HAPolicy.SHARED_STORE_MASTER.haPolicyAddress);
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.SHARED_STORE_MASTER.remove(removePolicyInView);
    }

    @Test
    public void createSharedStoreSlave() throws Exception {
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.SHARED_STORE_SLAVE.create(createPolicyInView);
    }

    @Test
    public void removeSharedStoreSlave() throws Exception {
        operations.add(HAPolicy.SHARED_STORE_SLAVE.haPolicyAddress);
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.SHARED_STORE_SLAVE.remove(removePolicyInView);
    }

    @Test
    public void createSharedStoreColocated() throws Exception {
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.SHARED_STORE_COLOCATED.create(createPolicyInView);
    }

    @Test
    public void removeSharedStoreColocated() throws Exception {
        operations.add(HAPolicy.SHARED_STORE_COLOCATED.haPolicyAddress);
        page.navigateAgain(ModelDescriptionConstants.SERVER, MessagingFixtures.SRV_UPDATE);
        HAPolicy.SHARED_STORE_COLOCATED.remove(removePolicyInView);
    }

}
