package org.jboss.hal.testsuite.test.configuration.messaging.server.clustering;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.DG_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.DG_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.DG_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.REFRESH_TIMEOUT;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.serverAddress;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.socketDiscoveryGroupAddress;

@RunWith(Arquillian.class)
public class SocketDiscoveryGroupTest extends AbstractClusteringTest {

    private static final String MESSAGING_DISCOVERY_GROUP = "messaging-discovery-group";
    private static final String MESSAGING_SOCKET_DISCOVERY_GROUP = "msg-socket-discovery-group";

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        operations.add(socketDiscoveryGroupAddress(SRV_UPDATE, DG_UPDATE),
                Values.of(SOCKET_BINDING, HTTP)).assertSuccess();
        // operations.add(socketDiscoveryGroupAddress(SRV_UPDATE, DG_UPDATE_ALTERNATIVES),
        //         Values.of(SOCKET_BINDING, HTTP)).assertSuccess();
        operations.add(socketDiscoveryGroupAddress(SRV_UPDATE, DG_DELETE),
                Values.of(SOCKET_BINDING, HTTP)).assertSuccess();
    }

    @AfterClass
    public static void removeResources() throws IOException, OperationException {
        operations.removeIfExists(serverAddress(SRV_UPDATE));
    }

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }

    @Test
    public void discoveryGroupCreate() throws Exception {
        console.verticalNavigation().selectSecondary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM),
                Ids.build(MESSAGING_SOCKET_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getSocketDiscoveryGroupTable();
        FormFragment form = page.getSocketDiscoveryGroupForm();
        table.bind(form);

        crudOperations.create(socketDiscoveryGroupAddress(SRV_UPDATE, DG_CREATE), table, formFragment -> {
            formFragment.text(NAME, DG_CREATE);
            formFragment.text(SOCKET_BINDING, HTTP);
        });
    }

    @Test
    public void discoveryGroupUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM),
                Ids.build(MESSAGING_SOCKET_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getSocketDiscoveryGroupTable();
        FormFragment form = page.getSocketDiscoveryGroupForm();
        table.bind(form);
        table.select(DG_UPDATE);
        crudOperations.update(socketDiscoveryGroupAddress(SRV_UPDATE, DG_UPDATE), form, REFRESH_TIMEOUT, 123L);
    }

    @Test
    public void discoveryGroupRemove() throws Exception {
        console.verticalNavigation().selectSecondary(Ids.build(MESSAGING_DISCOVERY_GROUP, ITEM),
                Ids.build(MESSAGING_SOCKET_DISCOVERY_GROUP, ITEM));
        TableFragment table = page.getSocketDiscoveryGroupTable();
        FormFragment form = page.getSocketDiscoveryGroupForm();
        table.bind(form);

        crudOperations.delete(socketDiscoveryGroupAddress(SRV_UPDATE, DG_DELETE), table, DG_DELETE);
    }

}
