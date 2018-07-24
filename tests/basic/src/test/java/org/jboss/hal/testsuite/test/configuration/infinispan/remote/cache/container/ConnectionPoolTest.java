package org.jboss.hal.testsuite.test.configuration.infinispan.remote.cache.container;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

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

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.SOCKET_BINDINGS;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.connectionPoolAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.remoteCacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.remoteClusterAddress;

@RunWith(Arquillian.class)
public class ConnectionPoolTest extends AbstractRemoteCacheContainerTest {

    private static final String REMOTE_CACHE_CONTAINER_TO_BE_TESTED =
        "remote-cache-container-to-be-tested-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING = "remote-socket-binding-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_CLUSTER = "remote-socket-binding-cluster-" + Random.name();

    private static final String REMOTE_CLUSTER = "remote-cluster-" + Random.name();

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException, TimeoutException, InterruptedException {
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING);
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER);
        createRemoteCacheContainer(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_SOCKET_BINDING);
        operations.add(remoteClusterAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED, REMOTE_CLUSTER),
            Values.of(SOCKET_BINDINGS,
                new ModelNodeGenerator.ModelNodeListBuilder().addAll(REMOTE_SOCKET_BINDING_CLUSTER).build()));
        administration.reloadIfRequired();
    }

    @AfterClass
    public static void tearDown() throws IOException, CommandFailedException, OperationException {
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_CLUSTER));
    }

    @Before
    public void initPage() {
        page.navigate("name", REMOTE_CACHE_CONTAINER_TO_BE_TESTED);
        console.verticalNavigation().selectPrimary("connection-pool-item");
    }

    @Test
    public void editExhaustedAction() throws Exception {
        String previousExhaustedAction =
            operations.readAttribute(connectionPoolAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED), "exhausted-action")
                .stringValue();
        String[] allExhaustedActions = {"EXCEPTION", "WAIT", "CREATE_NEW"};
        List<String> availableExhaustedActions =
            Arrays.stream(allExhaustedActions).filter(action -> !action.equals(previousExhaustedAction))
                .collect(Collectors.toList());
        String exhaustedAction = availableExhaustedActions.get(Random.number(0, availableExhaustedActions.size()));
        crudOperations.update(connectionPoolAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED), page.getConnectionPoolForm(),
            formFragment -> formFragment.select("exhausted-action", exhaustedAction),
            resourceVerifier -> resourceVerifier.verifyAttribute("exhausted-action", exhaustedAction));
    }

    @Test
    public void editMaxActive() throws Exception {
        crudOperations.update(connectionPoolAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED), page.getConnectionPoolForm(),
            "max-active", Random.number());
    }

    @Test
    public void editMaxWait() throws Exception {
        crudOperations.update(connectionPoolAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED), page.getConnectionPoolForm(),
            "max-wait", (long) Random.number());
    }

    @Test
    public void editMinEvictableIdleTime() throws Exception {
        crudOperations.update(connectionPoolAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED), page.getConnectionPoolForm(),
            "min-evictable-idle-time", (long) Random.number());
    }

    @Test
    public void editMinIdle() throws Exception {
        crudOperations.update(connectionPoolAddress(REMOTE_CACHE_CONTAINER_TO_BE_TESTED), page.getConnectionPoolForm(),
            "min-idle", Random.number());
    }
}
