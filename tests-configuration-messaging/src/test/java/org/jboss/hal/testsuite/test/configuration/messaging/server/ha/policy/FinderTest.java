package org.jboss.hal.testsuite.test.configuration.messaging.server.ha.policy;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MESSAGING_ACTIVEMQ;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SHARED_STORE_COLOCATED;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SHARED_STORE_PRIMARY;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_REPLICATION;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_REPLICATION_LIVE_ONLY;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_REPLICATION_PRIMARY;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_SHARED_STORE;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_SHARED_STORE_COLOCATED;
import static org.jboss.hal.resources.Ids.MESSAGING_HA_SHARED_STORE_PRIMARY;
import static org.jboss.hal.resources.Ids.MESSAGING_SERVER_CONFIGURATION;
import static org.jboss.hal.resources.Ids.MESSAGING_SERVER_HA_POLICY;
import static org.jboss.hal.resources.Ids.MESSAGING_SERVER_SETTINGS;
import static org.jboss.hal.resources.Ids.messagingServer;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.haPolicyAddress;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;

@RunWith(Arquillian.class)
public class FinderTest extends AbstractHaPolicyTest {

    private final HAPolicyConsumer createPolicyInFinder = (haPolicy -> {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        WizardFragment wizard = console.wizard();
        wizard.getRoot().findElement(By.id(haPolicy.basicStrategy)).click();
        wizard.next(haPolicy.serverRole);
        wizard.getRoot().findElement(By.id(haPolicy.serverRole)).click();
        wizard.finish();
        console.verifySuccess();
        new ResourceVerifier(haPolicy.haPolicyAddress, client)
            .verifyExists();
        operations.removeIfExists(haPolicy.haPolicyAddress);
    });

    private final HAPolicyConsumer removePolicyInFinder = (haPolicy -> {
        operations.add(haPolicy.haPolicyAddress);
        refreshConfigurationColumn();
        column.selectItem(MESSAGING_SERVER_HA_POLICY).dropdown().click("Remove");
        console.confirmationDialog().confirm();
        console.verifySuccess();
        new ResourceVerifier(haPolicy.haPolicyAddress, client)
            .verifyDoesNotExist();
        operations.removeIfExists(haPolicy.haPolicyAddress);
    });

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(MESSAGING_ACTIVEMQ)
            .append(Ids.MESSAGING_CATEGORY, SERVER)
            .append(MESSAGING_SERVER_CONFIGURATION, messagingServer(SRV_UPDATE)))
            .column(MESSAGING_SERVER_SETTINGS);
    }

    protected void refreshConfigurationColumn() throws Exception {
        // after the previous operations, it is necessary to refresh the "server" column
        console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(MESSAGING_ACTIVEMQ)
            .append(Ids.MESSAGING_CATEGORY, SERVER))
            .column(MESSAGING_SERVER_CONFIGURATION)
            .refresh();
        setUp();
    }

    @Test
    public void createReplicationLiveOnly() throws Exception {
        HAPolicy.LIVE_ONLY.create(createPolicyInFinder);
    }

    @Test
    public void removeReplicationLiveOnly() throws Exception {
        HAPolicy.LIVE_ONLY.remove(removePolicyInFinder);
    }

    @Test
    public void createReplicationMaster() throws Exception {
        HAPolicy.REPLICATION_PRIMARY.create(createPolicyInFinder);
    }

    @Test
    public void removeReplicationMaster() throws Exception {
        HAPolicy.REPLICATION_PRIMARY.remove(removePolicyInFinder);
    }

    @Test
    public void createReplicationSlave() throws Exception {
        HAPolicy.REPLICATION_SLAVE.create(createPolicyInFinder);
    }

    @Test
    public void removeReplicationSlave() throws Exception {
        HAPolicy.REPLICATION_SLAVE.remove(removePolicyInFinder);
    }

    @Test
    public void createReplicationColocated() throws Exception {
        HAPolicy.REPLICATION_COLOCATED.create(createPolicyInFinder);
    }

    @Test
    public void removeReplicationColocated() throws Exception {
        HAPolicy.REPLICATION_COLOCATED.remove(removePolicyInFinder);
    }

    @Test
    public void createSharedStoreMaster() throws Exception {
        HAPolicy.REPLICATION_PRIMARY.create(createPolicyInFinder);
    }

    @Test
    public void removeSharedStoreMaster() throws Exception {
        HAPolicy.REPLICATION_PRIMARY.remove(removePolicyInFinder);
    }

    @Test
    public void createSharedStoreSlave() throws Exception {
        HAPolicy.SHARED_STORE_SLAVE.create(createPolicyInFinder);
    }

    @Test
    public void removeSharedStoreSlave() throws Exception {
        HAPolicy.SHARED_STORE_SLAVE.remove(removePolicyInFinder);
    }

    @Test
    public void createSharedStoreColocated() throws Exception {
        HAPolicy.SHARED_STORE_COLOCATED.create(createPolicyInFinder);
    }

    @Test
    public void removeSharedStoreColocated() throws Exception {
        HAPolicy.SHARED_STORE_COLOCATED.remove(removePolicyInFinder);
    }

    // use default values, do not select any radio box.

    // test the back/next wizard workflow
    // first select replication/live-master, then goes back and select shared-store/colocated
    @Test
    public void createSharedStoreColocatedBackForth() throws Exception {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        wizard = console.wizard();
        // selects the "replication" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_REPLICATION)).click();
        // clicks the "next" button and waits for the dom id
        wizard.next(MESSAGING_HA_REPLICATION_LIVE_ONLY);
        // selects the "Live server (master)" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_REPLICATION_PRIMARY)).click();
        // clicks the "back" button and waits for the "shared-store" dom id
        wizard.back(By.id(MESSAGING_HA_SHARED_STORE));
        // clicks the "shared-store" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE)).click();
        // clicks "next"
        wizard.next(MESSAGING_HA_SHARED_STORE_PRIMARY);
        // clicks on the "Colocate live and backup server" radio box
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE_COLOCATED)).click();
        // finish the wizard
        wizard.next();

        console.verifySuccess();
        new ResourceVerifier(haPolicyAddress(SRV_UPDATE, SHARED_STORE_COLOCATED), client)
            .verifyExists();
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, SHARED_STORE_COLOCATED));
    }

    // test the back/next wizard workflow with default values
    // first select replication/live-master, then goes back and select shared-store, do not select any item and finish
    @Test
    public void createSharedStoreLiveMasterBackForth() throws Exception {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).defaultAction();
        wizard = console.wizard();
        // selects the "replication" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_REPLICATION)).click();
        // clicks the "next" button and waits for the dom id
        wizard.next(MESSAGING_HA_REPLICATION_LIVE_ONLY);
        // selects the "Live server (master)" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_REPLICATION_PRIMARY)).click();
        // clicks the "back" button and waits for the "shared-store" dom id
        wizard.back(By.id(MESSAGING_HA_SHARED_STORE));
        // clicks the "shared-store" radio item
        wizard.getRoot().findElement(By.id(MESSAGING_HA_SHARED_STORE)).click();
        // clicks "next"
        wizard.next(MESSAGING_HA_SHARED_STORE_PRIMARY);
        // finish the wizard with default "Live server (master)" radio selected
        wizard.next();

        console.verifySuccess();
        new ResourceVerifier(haPolicyAddress(SRV_UPDATE, SHARED_STORE_PRIMARY), client)
            .verifyExists();
        operations.removeIfExists(haPolicyAddress(SRV_UPDATE, SHARED_STORE_PRIMARY));
    }

    @Test
    public void viewEmptyHaPolicy() {
        column.selectItem(MESSAGING_SERVER_HA_POLICY).dropdown().click("View");
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.MESSAGING_SERVER_HA_POLICY)
            .with(SERVER, SRV_UPDATE)
            .build();
        waitModel().until().element(By.id(Ids.FINDER)).is().not().present();
        console.verify(placeRequest);
    }
}
