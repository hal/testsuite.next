package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.expirationAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class ExpirationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_EXPIRATION_CREATE =
        "scattered-cache-with-expiration-to-be-created-" + Random.name();
    private static final String SCATTERED_CACHE_EXPIRATION_DELETE =
        "scattered-cache-with-expiration-to-be-deleted-" + Random.name();
    private static final String SCATTERED_CACHE_EXPIRATION_EDIT =
        "scattered-cache-with-expiration-to-be-edited-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, OperationException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_CREATE));
        operations.removeIfExists(
            expirationAddress(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_CREATE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_DELETE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_EDIT));
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
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_CREATE);
        crudOperations.createSingleton(
            expirationAddress(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_CREATE),
            page.getExpirationForm());
    }

    private void navigateToExpirationForm(String cacheContainer, String scatteredCache) {
        Map<String, String> params = new HashMap<>();
        params.put("cache-container", cacheContainer);
        params.put("name", scatteredCache);
        page.navigate(params);
        console.verticalNavigation().selectPrimary("scattered-cache-item");
    }

    @Test
    public void delete() throws Exception {
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_DELETE);
        crudOperations.deleteSingleton(
            expirationAddress(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_DELETE),
            page.getExpirationForm());
    }

    @Test
    public void editInterval() throws Exception {
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_EDIT);
        crudOperations.update(
            expirationAddress(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_EDIT),
            page.getExpirationForm(), "interval", (long) Random.number());
    }

    @Test
    public void editLifespan() throws Exception {
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_EDIT);
        crudOperations.update(
            expirationAddress(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_EDIT),
            page.getExpirationForm(), "lifespan", (long) Random.number());
    }

    @Test
    public void editMaxIdle() throws Exception {
        navigateToExpirationForm(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_EDIT);
        crudOperations.update(
            expirationAddress(CACHE_CONTAINER, SCATTERED_CACHE_EXPIRATION_EDIT),
            page.getExpirationForm(), "max-idle", (long) Random.number());
    }
}
