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
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.partitionHandlingAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class PartitionHandlingTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_PARTITION_HANDLING_CREATE =
        "scattered-cache-with-partition-handling-to-be-created-" + Random.name();
    private static final String SCATTERED_CACHE_PARTITION_HANDLING_DELETE =
        "scattered-cache-with-partition-handling-to-be-deleted-" + Random.name();
    private static final String SCATTERED_CACHE_PARTITION_HANDLING_EDIT =
        "scattered-cache-with-partition-handling-to-be-edited-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, OperationException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_CREATE));
        operations.removeIfExists(
            partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_CREATE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_DELETE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_EDIT));
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
        navigateToPartitionHandlingForm(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_CREATE);
        crudOperations.createSingleton(
            partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_CREATE),
            page.getPartitionHandlingForm());
    }

    private void navigateToPartitionHandlingForm(String cacheContainer, String scatteredCache) {
        Map<String, String> params = new HashMap<>();
        params.put("cache-container", cacheContainer);
        params.put("name", scatteredCache);
        page.navigate(params);
        console.verticalNavigation().selectPrimary("scattered-cache-item");
    }

    @Test
    public void delete() throws Exception {
        navigateToPartitionHandlingForm(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_DELETE);
        crudOperations.deleteSingleton(
            partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_DELETE),
            page.getPartitionHandlingForm());
    }

    @Test
    public void toggleEnabled() throws Exception {
        boolean enabled =
            operations.readAttribute(partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_EDIT), "enabled")
                .booleanValue(false);
        navigateToPartitionHandlingForm(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_EDIT);
        crudOperations.update(
            partitionHandlingAddress(CACHE_CONTAINER, SCATTERED_CACHE_PARTITION_HANDLING_EDIT),
            page.getPartitionHandlingForm(), "enabled", !enabled);
    }

}
