package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store.custom;

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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.customStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class AttributesTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();

    @BeforeClass
    public static void init() throws IOException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), Values.of("class", Random.name()));
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
    public void editClass() throws Exception {
        console.waitNoNotification();
        crudOperations.update(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getCustomStoreAttributesForm(),
            "class");
    }

    @Test
    public void toggleFetchState() throws Exception {
        console.waitNoNotification();
        boolean fetchState =
            operations.readAttribute(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "fetch-state")
                .booleanValue(true);
        crudOperations.update(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getCustomStoreAttributesForm(),
            "fetch-state", !fetchState);
    }

    @Test
    public void editMaxBatchSize() throws Exception {
        console.waitNoNotification();
        crudOperations.update(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getCustomStoreAttributesForm(),
            "max-batch-size", Random.number());
    }

    @Test
    public void togglePassivation() throws Exception {
        console.waitNoNotification();
        crudOperations.update(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getCustomStoreAttributesForm(),
            "max-batch-size", Random.number());
    }

    @Test
    public void togglePreload() throws Exception {
        console.waitNoNotification();
        boolean preload =
            operations.readAttribute(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "preload")
                .booleanValue(false);
        crudOperations.update(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getCustomStoreAttributesForm(),
            "preload", !preload);
    }

    @Test
    public void editProperties() throws Exception {
        console.waitNoNotification();
        crudOperations.update(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getCustomStoreAttributesForm(),
            "properties", Random.properties());
    }

    @Test
    public void togglePurge() throws Exception {
        console.waitNoNotification();
        boolean purge =
            operations.readAttribute(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "purge")
                .booleanValue(true);
        crudOperations.update(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getCustomStoreAttributesForm(),
            "purge", !purge);
    }

    @Test
    public void toggleShared() throws Exception {
        console.waitNoNotification();
        boolean shared =
            operations.readAttribute(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "shared")
                .booleanValue(false);
        crudOperations.update(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getCustomStoreAttributesForm(),
            "shared", !shared);
    }
}
