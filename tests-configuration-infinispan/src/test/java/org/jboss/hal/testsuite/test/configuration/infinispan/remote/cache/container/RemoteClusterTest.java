package org.jboss.hal.testsuite.test.configuration.infinispan.remote.cache.container;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.SOCKET_BINDINGS;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.remoteCacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.remoteClusterAddress;

@RunWith(Arquillian.class)
public class RemoteClusterTest extends AbstractRemoteCacheContainerTest {

    private static final String REMOTE_CACHE_CONTAINER_TO_BE_TESTED =
        "remote-cache-container-to-be-tested-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING = "remote-socket-binding-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_CLUSTER_CREATE =
        "remote-socket-binding-cluster-create" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_CLUSTER_EDIT =
        "remote-socket-binding-cluster-edit-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_CLUSTER_DELETE =
        "remote-socket-binding-cluster-delete-" + Random.name();

    private static final String REMOTE_CLUSTER = "remote-cluster-" + Random.name();
    private static final String REMOTE_CLUSTER_CREATE = "remote-cluster-to-be-created-" + Random.name();
    private static final String REMOTE_CLUSTER_DELETE = "remote-cluster-to-be-deleted-" + Random.name();

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException, TimeoutException, InterruptedException {
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING);
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER_CREATE);
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER_EDIT);
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER_DELETE);
        createRemoteCacheContainer(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_SOCKET_BINDING_CLUSTER_CREATE);
        createRemoteCluster(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_CLUSTER, REMOTE_SOCKET_BINDING);
        createRemoteCluster(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_CLUSTER_DELETE,
            REMOTE_SOCKET_BINDING_CLUSTER_DELETE);
        administration.reloadIfRequired();
    }

    private static void createRemoteCluster(String remoteCacheContainerName, String remoteClusterName,
        String socketBinding) throws IOException {
        operations.add(remoteClusterAddress(remoteCacheContainerName, remoteClusterName),
            Values.of("socket-bindings",
                new ModelNodeGenerator.ModelNodeListBuilder().addAll(socketBinding).build()));
    }

    @AfterClass
    public static void tearDown() throws IOException, CommandFailedException, OperationException {
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER_CREATE));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER_EDIT));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER_DELETE));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING));
    }

    @Before
    public void initPage() {
        page.navigate("name", REMOTE_CACHE_CONTAINER_TO_BE_TESTED);
        console.verticalNavigation().selectPrimary("rc-item");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(remoteClusterAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_CLUSTER_CREATE),
            page.getRemoteClusterTable(), formFragment -> {
                formFragment.text("name", REMOTE_CLUSTER_CREATE);
                formFragment.list(SOCKET_BINDINGS).add(REMOTE_SOCKET_BINDING_CLUSTER_CREATE);
            }, resourceVerifier -> {
                resourceVerifier.verifyExists();
                resourceVerifier.verifyListAttributeContainsValue(SOCKET_BINDINGS,
                    REMOTE_SOCKET_BINDING_CLUSTER_CREATE);
            });
    }

    @Test
    public void delete() throws Exception {
        crudOperations.delete(remoteClusterAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_CLUSTER_DELETE),
            page.getRemoteClusterTable(), REMOTE_CLUSTER_DELETE);
    }

    @Test
    public void edit() throws Exception {
        page.getRemoteClusterTable().select(REMOTE_CLUSTER);
        crudOperations.update(remoteClusterAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_CLUSTER),
            page.getRemoteClusterForm(), SOCKET_BINDINGS,
            Collections.singletonList(REMOTE_SOCKET_BINDING_CLUSTER_EDIT));
    }
}
