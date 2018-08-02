package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.configuration;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.lockingAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class LockingTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_LOCKING_CREATE =
        "scattered-cache-with-locking-to-be-created-" + Random.name();
    private static final String SCATTERED_CACHE_LOCKING_DELETE =
        "scattered-cache-with-locking-to-be-deleted-" + Random.name();
    private static final String SCATTERED_CACHE_LOCKING_EDIT =
        "scattered-cache-with-locking-to-be-edited-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, OperationException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_CREATE));
        operations.removeIfExists(
            lockingAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_CREATE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_DELETE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_EDIT));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Inject
    private CrudOperations crudOperations;

    @Inject
    private Console console;

    @Page
    private ScatteredCachePage page;

    @Test
    public void create() throws Exception {
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_CREATE);
        crudOperations.createSingleton(
            lockingAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_CREATE),
            page.getLockingForm());
    }

    private void navigateToExpirationForm(String cacheContainer, String scatteredCache) {
        page.navigate(cacheContainer, scatteredCache);
        console.verticalNavigation().selectPrimary("scattered-cache-item");
    }

    @Test
    public void delete() throws Exception {
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_DELETE);
        crudOperations.deleteSingleton(
            lockingAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_DELETE),
            page.getLockingForm());
    }

    @Test
    public void editAcquireTimeout() throws Exception {
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_EDIT);
        crudOperations.update(
            lockingAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_EDIT),
            page.getLockingForm(), "acquire-timeout", (long) Random.number());
    }

    @Test
    public void editConcurrencyLevel() throws Exception {
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_EDIT);
        crudOperations.update(
            lockingAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_EDIT),
            page.getLockingForm(), "concurrency-level", Random.number());
    }

    @Test
    public void toggleStriping() throws Exception {
        boolean striping =
            operations.readAttribute(lockingAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_EDIT), "stripping")
                .booleanValue(false);
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_EDIT);
        crudOperations.update(
            lockingAddress(CACHE_CONTAINER, SCATTERED_CACHE_LOCKING_EDIT),
            page.getLockingForm(), "striping", !striping);
    }
}
