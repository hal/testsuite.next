package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.memory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.offHeapMemoryAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class OffHeapMemoryTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();

    @BeforeClass
    public static void init() throws IOException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));
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
    public void initPage() {
        page.navigate(CACHE_CONTAINER, SCATTERED_CACHE);
        console.verticalNavigation().selectPrimary("scattered-cache-memory-item");
    }

    @Test
    public void editCapacity() throws Exception {
        crudOperations.update(offHeapMemoryAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getOffHeapMemoryForm(), "capacity",
            Random.number());
    }

    @Test
    public void editEvictionType() throws Exception {
        console.waitNoNotification();
        String currentEvictionType =
            operations.readAttribute(offHeapMemoryAddress(CACHE_CONTAINER, SCATTERED_CACHE), "eviction-type")
                .stringValue("COUNT");
        List<String> evictionTypes = new ArrayList<>(Arrays.asList("COUNT", "MEMORY"));
        evictionTypes.remove(currentEvictionType);
        String evictionType = evictionTypes.get(0);
        crudOperations.update(offHeapMemoryAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getOffHeapMemoryForm(),
            formFragment -> formFragment.select("eviction-type", evictionType),
            resourceVerifier -> resourceVerifier.verifyAttribute("eviction-type", evictionType));
    }

    @Test
    public void editSize() throws Exception {
        console.waitNoNotification();
        crudOperations.update(offHeapMemoryAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getOffHeapMemoryForm(), "size",
            (long) Random.number());
    }

}
