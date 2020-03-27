package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store.hotrod;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.AddRemoteSocketBinding;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.jboss.hal.testsuite.util.AvailablePortFinder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALLOW_RESOURCE_SERVICE_RESTART;
import static org.jboss.hal.dmr.ModelDescriptionConstants.JGROUPS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TRANSPORT;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.BEHIND;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.SOCKET_BINDINGS;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.THROUGH;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.WRITE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.hotrodStoreAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.remoteCacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WriteBehaviourTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();
    private static final String REMOTE_CC = "rcc-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING = "rem-socket-binding-" + Random.name();

    @BeforeClass
    public static void init() throws IOException, CommandFailedException {
        createRemoteCacheContainer(REMOTE_CC, REMOTE_SOCKET_BINDING);
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and(TRANSPORT, JGROUPS));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));
        operations.headers(Values.of(ALLOW_RESOURCE_SERVICE_RESTART, true))
            .add(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE),
                Values.of("remote-cache-container", REMOTE_CC));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
            operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CC));
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

    @Inject private CrudOperations crud;
    @Inject private Console console;
    @Page private ScatteredCachePage page;
    private FormFragment form;

    @Before
    public void initPage() {
        page.navigate(CACHE_CONTAINER, SCATTERED_CACHE);
        console.waitNoNotification();
        console.verticalNavigation().selectPrimary("scattered-cache-store-item");
        form = page.getHotrodStoreWriteBehindForm();
    }

    @Test
    public void change1ToWriteBehind() throws Exception {
        console.waitNoNotification();
        page.switchBehaviour();
        new ResourceVerifier(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, BEHIND),
                client).verifyExists();
    }

    @Test
    public void change2ModificationQueueSize() throws Exception {
        console.waitNoNotification();
        crud.update(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, BEHIND), form,
                "modification-queue-size", Random.number());
    }

    @Test
    public void change4ToWriteThrough() throws Exception {
        page.switchBehaviour();
        new ResourceVerifier(hotrodStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and(WRITE, THROUGH),
                client).verifyExists();
    }
}
