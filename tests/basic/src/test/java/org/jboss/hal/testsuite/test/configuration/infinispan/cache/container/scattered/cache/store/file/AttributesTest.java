package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store.file;

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
import org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.fileStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class AttributesTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();
    private static final String PATH = "path-" + Random.name();

    @BeforeClass
    public static void init() throws IOException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE));
        operations.add(Address.of(InfinispanFixtures.PATH, PATH), Values.of(InfinispanFixtures.PATH, Random.name()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
            operations.removeIfExists(Address.of(InfinispanFixtures.PATH, PATH));
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
        Map<String, String> params = new HashMap<>();
        params.put("cache-container", CACHE_CONTAINER);
        params.put("name", SCATTERED_CACHE);
        page.navigate(params);
        console.verticalNavigation().selectPrimary("scattered-cache-store-item");
    }

    @Test
    public void toggleFetchState() throws Exception {
        boolean fetchState =
            operations.readAttribute(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "fetch-state")
                .booleanValue(true);
        crudOperations.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getFileStoreAttributesForm(),
            "fetch-state", !fetchState);
    }

    @Test
    public void editMaxBatchSize() throws Exception {
        crudOperations.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getFileStoreAttributesForm(),
            "max-batch-size", Random.number());
    }

    @Test
    public void togglePassivation() throws Exception {
        boolean passivation =
            operations.readAttribute(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "passivation")
                .booleanValue(true);
        crudOperations.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getFileStoreAttributesForm(),
            "passivation", !passivation);
    }

    @Test
    public void editPath() throws Exception {
        crudOperations.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getFileStoreAttributesForm(),
            InfinispanFixtures.PATH);
    }

    @Test
    public void togglePreload() throws Exception {
        boolean preload =
            operations.readAttribute(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "preload")
                .booleanValue(false);
        crudOperations.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getFileStoreAttributesForm(),
            "preload", !preload);
    }

    @Test
    public void editProperties() throws Exception {
        crudOperations.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getFileStoreAttributesForm(),
            "properties", Random.properties());
    }

    @Test
    public void togglePurge() throws Exception {
        boolean purge =
            operations.readAttribute(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "purge")
                .booleanValue(true);
        crudOperations.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getFileStoreAttributesForm(),
            "purge", !purge);
    }

    @Test
    public void editRelativeTo() throws Exception {
        crudOperations.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getFileStoreAttributesForm(),
            "relative-to", PATH);
    }

    @Test
    public void toggleShared() throws Exception {
        boolean shared =
            operations.readAttribute(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "shared")
                .booleanValue(false);
        crudOperations.update(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getFileStoreAttributesForm(),
            "shared", !shared);
    }
}
