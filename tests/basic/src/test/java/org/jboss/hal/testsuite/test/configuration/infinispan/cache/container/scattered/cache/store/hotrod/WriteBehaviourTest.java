package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store.hotrod;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.AddRemoteSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures;
import org.jboss.hal.testsuite.util.AvailablePortFinder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.SOCKET_BINDINGS;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.hotrodStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.remoteCacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class WriteBehaviourTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_WRITE_BEHIND_EDIT =
        "scattered-cache-with-write-behind-to-be-edited-" + Random.name();
    private static final String SCATTERED_CACHE_WRITE_THROUGH = "scattered-cache-write-through-" + Random.name();
    private static final String SCATTERED_CACHE_WRITE_BEHIND = "scattered-cache-write-behind-" + Random.name();

    private static final String REMOTE_CACHE_CONTAINER_BEHIND =
        "remote-cache-container-for-scattered-cache-with-write-behind-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_BEHIND =
        "remote-socket-binding-for-scattered-cache-with-write-behind-" + Random.name();

    private static final String REMOTE_CACHE_CONTAINER_THROUGH =
        "remote-cache-container-for-scattered-cache-with-write-through-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_THROUGH =
        "remote-socket-binding-for-scattered-cache-with-write-through-" + Random.name();

    private static final String REMOTE_CACHE_CONTAINER_BEHIND_EDIT =
        "remote-cache-container-for-scattered-cache-with-write-behind-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_BEHIND_EDIT =
        "remote-socket-binding-for-scattered-cache-with-write-behind-" + Random.name();

    @BeforeClass
    public static void init() throws IOException, CommandFailedException {
        createRemoteCacheContainer(REMOTE_CACHE_CONTAINER_BEHIND, REMOTE_SOCKET_BINDING_BEHIND);
        createRemoteCacheContainer(REMOTE_CACHE_CONTAINER_THROUGH, REMOTE_SOCKET_BINDING_THROUGH);
        createRemoteCacheContainer(REMOTE_CACHE_CONTAINER_BEHIND_EDIT, REMOTE_SOCKET_BINDING_BEHIND_EDIT);
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_THROUGH));
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND),
                Values.of("remote-cache-container", REMOTE_CACHE_CONTAINER_BEHIND));
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_THROUGH),
                Values.of("remote-cache-container", REMOTE_CACHE_CONTAINER_THROUGH));
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT),
                Values.of("remote-cache-container", REMOTE_CACHE_CONTAINER_BEHIND_EDIT));
        operations.add(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND).and(InfinispanFixtures.WRITE,
            InfinispanFixtures.BEHIND));
        operations.add(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT).and(InfinispanFixtures.WRITE,
            InfinispanFixtures.BEHIND));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
            operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_BEHIND));
            operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_BEHIND_EDIT));
            operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_THROUGH));
            client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_BEHIND));
            client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_BEHIND_EDIT));
            client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_THROUGH));
        } finally {
            client.close();
        }
    }

    private static void createRemoteCacheContainer(String remoteCacheContainer, String socketBinding)
        throws IOException, CommandFailedException {
        String remoteCluster = "remote-cluster-" + Random.name();
        client.apply(new AddRemoteSocketBinding(socketBinding, "localhost",
            AvailablePortFinder.getNextAvailableTCPPort()));
        operations.batch(new Batch().add(remoteCacheContainerAddress(remoteCacheContainer),
            Values.of("default-remote-cluster", remoteCluster))
            .add(remoteCacheContainerAddress(remoteCacheContainer).and("remote-cluster", remoteCluster),
                Values.of(SOCKET_BINDINGS,
                    new ModelNodeGenerator.ModelNodeListBuilder().addAll(socketBinding).build())));
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private ScatteredCachePage page;

    private void navigateToWriteBehaviour(String cacheContainer, String scatteredCache) {
        page.navigate(cacheContainer, scatteredCache);
        console.verticalNavigation().selectPrimary("scattered-cache-store-item");
    }

    @Test
    public void switchToWriteBehind() throws Exception {
        navigateToWriteBehaviour(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_THROUGH);
        page.getHotrodStoreTab().select(ScatteredCachePage.HOTROD_STORE_WRITE_BEHAVIOUR_TAB);
        page.switchBehaviour();
        new ResourceVerifier(
            hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_THROUGH).and(InfinispanFixtures.WRITE,
                InfinispanFixtures.BEHIND),
            client).verifyExists();
    }

    @Test
    public void switchToWriteThrough() throws Exception {
        navigateToWriteBehaviour(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND);
        page.getHotrodStoreTab().select(ScatteredCachePage.HOTROD_STORE_WRITE_BEHAVIOUR_TAB);
        page.switchBehaviour();
        new ResourceVerifier(
            hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND).and(InfinispanFixtures.WRITE,
                InfinispanFixtures.THROUGH),
            client).verifyExists();
    }

    @Test
    public void editModificationQueueSize() throws Exception {
        navigateToWriteBehaviour(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT);
        crudOperations.update(
            hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT).and(InfinispanFixtures.WRITE,
                InfinispanFixtures.BEHIND),
            page.getHotrodStoreWriteBehindForm(), "modification-queue-size", Random.number());
    }

    @Test
    public void editThreadPoolSize() throws Exception {
        navigateToWriteBehaviour(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT);
        crudOperations.update(
            hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT).and(InfinispanFixtures.WRITE,
                InfinispanFixtures.BEHIND),
            page.getHotrodStoreWriteBehindForm(), "thread-pool-size", Random.number());
    }
}
