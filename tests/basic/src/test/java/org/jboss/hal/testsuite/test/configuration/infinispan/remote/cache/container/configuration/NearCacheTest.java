package org.jboss.hal.testsuite.test.configuration.infinispan.remote.cache.container.configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.test.configuration.infinispan.remote.cache.container.AbstractRemoteCacheContainerTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.CSS.btnDefault;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.nearCacheAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.remoteCacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.remoteClusterAddress;

@RunWith(Arquillian.class)
public class NearCacheTest extends AbstractRemoteCacheContainerTest {

    private static final String REMOTE_CACHE_CONTAINER_NEAR_CACHE_CREATE =
        "remote-cache-container-with-near-cache-to-be-created-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_NEAR_CACHE_CREATE =
        "remote-socket-binding-near-cache-create-" + Random.name();

    private static final String REMOTE_CACHE_CONTAINER_NEAR_CACHE_EDIT =
        "remote-cache-container-with-near-cache-to-be-edited-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_NEAR_CACHE_EDIT =
        "remote-socket-binding-near-cache-edit-" + Random.name();

    private static final String REMOTE_CACHE_CONTAINER_NEAR_CACHE_DELETE =
        "remote-cache-container-with-near-cache-to-be-deleted-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_NEAR_CACHE_DELETE =
        "remote-socket-binding-near-cache-delete-" + Random.name();

    @BeforeClass
    public static void setUp() throws CommandFailedException, IOException, TimeoutException, InterruptedException {
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_NEAR_CACHE_CREATE);
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_NEAR_CACHE_EDIT);
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_NEAR_CACHE_DELETE);
        createRemoteCacheContainer(REMOTE_CACHE_CONTAINER_NEAR_CACHE_CREATE, REMOTE_SOCKET_BINDING_NEAR_CACHE_CREATE,
            false);
        createRemoteCacheContainer(REMOTE_CACHE_CONTAINER_NEAR_CACHE_EDIT, REMOTE_SOCKET_BINDING_NEAR_CACHE_EDIT, true);
        createRemoteCacheContainer(REMOTE_CACHE_CONTAINER_NEAR_CACHE_DELETE, REMOTE_SOCKET_BINDING_NEAR_CACHE_DELETE,
            true);
        administration.reloadIfRequired();
    }

    private static void createRemoteCacheContainer(String name, String socketBinding, boolean shouldCreateNearCache)
        throws IOException {
        String remoteCluster = Random.name();
        Batch batch =
            new Batch().add(remoteCacheContainerAddress(name), Values.of("default-remote-cluster", remoteCluster))
                .add(remoteClusterAddress(name, remoteCluster),
                    Values.of("socket-bindings",
                        new ModelNodeGenerator.ModelNodeListBuilder().addAll(socketBinding).build()));
        if (shouldCreateNearCache) {
            batch.add(nearCacheAddress(name));
        }
        operations.batch(batch)
            .assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, CommandFailedException, OperationException {
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_NEAR_CACHE_CREATE));
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_NEAR_CACHE_EDIT));
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_NEAR_CACHE_DELETE));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_NEAR_CACHE_CREATE));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_NEAR_CACHE_EDIT));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_NEAR_CACHE_DELETE));
    }

    @Test
    public void enable() throws Exception {
        navigateToNearCache(REMOTE_CACHE_CONTAINER_NEAR_CACHE_CREATE);
        TabsFragment nearCacheTab = page.getConfigurationTab();
        toggleNearCache(nearCacheTab);
        new ResourceVerifier(nearCacheAddress(REMOTE_CACHE_CONTAINER_NEAR_CACHE_CREATE), client).verifyExists();
    }

    private void navigateToNearCache(String remoteCacheContainerName) {
        page.navigate(NAME, remoteCacheContainerName);
        // a second navigation is necessary to reload the page
        page.navigate(NAME, remoteCacheContainerName);
        console.verticalNavigation().selectPrimary("rcc-item");
    }

    private void toggleNearCache(TabsFragment tabsFragment) {
        tabsFragment.select("rcc-near-cache-tab");
        WebElement switchNearCacheElement =
            tabsFragment.getRoot().findElement(ByJQuery.selector("." + btnDefault + ":visible"));
        console.scrollIntoView(switchNearCacheElement);
        switchNearCacheElement.click();
    }

    @Test
    public void editMaxEntries() throws Exception {
        navigateToNearCache(REMOTE_CACHE_CONTAINER_NEAR_CACHE_EDIT);
        crudOperations.update(nearCacheAddress(REMOTE_CACHE_CONTAINER_NEAR_CACHE_EDIT), page.getNearCacheForm(),
            "max-entries", Random.number());
    }

    @Test
    public void disable() throws Exception {
        navigateToNearCache(REMOTE_CACHE_CONTAINER_NEAR_CACHE_DELETE);
        TabsFragment nearCacheTab = page.getConfigurationTab();
        toggleNearCache(nearCacheTab);
        new ResourceVerifier(nearCacheAddress(REMOTE_CACHE_CONTAINER_NEAR_CACHE_DELETE), client).verifyDoesNotExist();
        new ResourceVerifier(
            remoteCacheContainerAddress(REMOTE_CACHE_CONTAINER_NEAR_CACHE_DELETE).and("near-cache", "none"), client)
            .verifyExists();
    }
}
