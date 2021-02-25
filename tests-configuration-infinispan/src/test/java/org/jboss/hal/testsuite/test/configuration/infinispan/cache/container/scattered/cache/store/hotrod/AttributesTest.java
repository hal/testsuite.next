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
import org.jboss.hal.testsuite.creaper.command.AddRemoteSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.jboss.hal.testsuite.util.AvailablePortFinder;
import org.junit.AfterClass;
import org.junit.Before;
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

import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.SOCKET_BINDINGS;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.hotrodStoreAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.remoteCacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class AttributesTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();

    private static final String REMOTE_SOCKET_BINDING = "remote-socket-binding-" + Random.name();
    private static final String REMOTE_CLUSTER = "remote-cluster-" + Random.name();
    private static final String REMOTE_CACHE_CONTAINER = "remote-cache-container-" + Random.name();

    private static final String REMOTE_SOCKET_BINDING_EDIT = "remote-socket-binding-edit-" + Random.name();
    private static final String REMOTE_CLUSTER_EDIT = "remote-cluster-edit-" + Random.name();
    private static final String REMOTE_CACHE_CONTAINER_EDIT = "remote-cache-container-edit-" + Random.name();

    @BeforeClass
    public static void init() throws IOException, CommandFailedException {
        client.apply(new AddRemoteSocketBinding(REMOTE_SOCKET_BINDING, "localhost",
            AvailablePortFinder.getNextAvailableTCPPort()));
        operations.batch(new Batch().add(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER),
            Values.of("default-remote-cluster", REMOTE_CLUSTER))
            .add(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER).and("remote-cluster", REMOTE_CLUSTER),
                Values.of(SOCKET_BINDINGS,
                    new ModelNodeGenerator.ModelNodeListBuilder().addAll(REMOTE_SOCKET_BINDING).build())));
        client.apply(new AddRemoteSocketBinding(REMOTE_SOCKET_BINDING_EDIT, "localhost",
            AvailablePortFinder.getNextAvailableTCPPort()));
        operations.batch(new Batch().add(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_EDIT),
            Values.of("default-remote-cluster", REMOTE_CLUSTER_EDIT))
            .add(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_EDIT).and("remote-cluster", REMOTE_CLUSTER_EDIT),
                Values.of(SOCKET_BINDINGS,
                    new ModelNodeGenerator.ModelNodeListBuilder().addAll(REMOTE_SOCKET_BINDING).build())));
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE),
                Values.of("remote-cache-container", REMOTE_CACHE_CONTAINER));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
            operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER));
            operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_EDIT));
            client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING));
            client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_EDIT));
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private ScatteredCachePage page;

    @Before
    public void navigate() {
        page.navigate(CACHE_CONTAINER, SCATTERED_CACHE);
        console.verticalNavigation().selectPrimary("scattered-cache-store-item");
    }

    @Test
    public void editCacheConfiguration() throws Exception {
        console.waitNoNotification();
        crudOperations.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getHotrodStoreAttributesForm(),
            "cache-configuration");
    }

    @Test
    public void toggleFetchState() throws Exception {
        console.waitNoNotification();
        boolean fetchState =
            operations.readAttribute(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "fetch-state")
                .booleanValue(true);
        crudOperations.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getHotrodStoreAttributesForm(),
            "fetch-state", !fetchState);
    }

    @Test
    public void editMaxBatchSize() throws Exception {
        console.waitNoNotification();
        crudOperations.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getHotrodStoreAttributesForm(),
            "max-batch-size", Random.number());
    }

    @Test
    public void togglePassivation() throws Exception {
        console.waitNoNotification();
        boolean passivation =
            operations.readAttribute(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "passivation")
                .booleanValue(true);
        crudOperations.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getHotrodStoreAttributesForm(),
            "passivation", !passivation);
    }

    @Test
    public void togglePreload() throws Exception {
        console.waitNoNotification();
        boolean preload =
            operations.readAttribute(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "preload")
                .booleanValue(false);
        crudOperations.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getHotrodStoreAttributesForm(),
            "preload", !preload);
    }

    @Test
    public void editProperties() throws Exception {
        console.waitNoNotification();
        crudOperations.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getHotrodStoreAttributesForm(),
            "properties", Random.properties());
    }

    @Test
    public void togglePurge() throws Exception {
        console.waitNoNotification();
        boolean purge =
            operations.readAttribute(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "purge")
                .booleanValue(true);
        crudOperations.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getHotrodStoreAttributesForm(),
            "purge", !purge);
    }

    @Test
    public void editRemoteCacheContainer() throws Exception {
        console.waitNoNotification();
        crudOperations.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getHotrodStoreAttributesForm(),
            "remote-cache-container", REMOTE_CACHE_CONTAINER_EDIT);
    }

    @Test
    public void toggleShared() throws Exception {
        console.waitNoNotification();
        boolean shared =
            operations.readAttribute(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "shared")
                .booleanValue(false);
        crudOperations.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getHotrodStoreAttributesForm(),
            "shared", !shared);
    }
}
