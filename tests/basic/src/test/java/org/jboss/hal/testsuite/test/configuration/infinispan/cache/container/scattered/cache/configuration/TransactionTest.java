package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.transactionAddress;

@RunWith(Arquillian.class)
public class TransactionTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_TRANSACTION_CREATE =
        "scattered-cache-with-transaction-to-be-created-" + Random.name();
    private static final String SCATTERED_CACHE_TRANSACTION_DELETE =
        "scattered-cache-with-transaction-to-be-deleted-" + Random.name();
    private static final String SCATTERED_CACHE_TRANSACTION_EDIT =
        "scattered-cache-with-transaction-to-be-edited-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, OperationException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_CREATE));
        operations.removeIfExists(
            transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_CREATE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_DELETE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_EDIT));
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
        navigateToTransactionForm(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_CREATE);
        crudOperations.createSingleton(
            transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_CREATE),
            page.getTransactionForm());
    }

    private void navigateToTransactionForm(String cacheContainer, String scatteredCache) {
        Map<String, String> params = new HashMap<>();
        params.put("cache-container", cacheContainer);
        params.put("name", scatteredCache);
        page.navigate(params);
        console.verticalNavigation().selectPrimary("scattered-cache-item");
    }

    @Test
    public void delete() throws Exception {
        navigateToTransactionForm(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_DELETE);
        crudOperations.deleteSingleton(
            transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_DELETE),
            page.getTransactionForm());
    }

    @Test
    public void editLocking() throws Exception {
        String currentLocking =
            operations.readAttribute(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_EDIT), "locking")
                .stringValue();
        List<String> lockings = new ArrayList<>(Arrays.asList("PESSIMISTIC", "OPTIMISTIC"));
        lockings.remove(currentLocking);
        navigateToTransactionForm(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_EDIT);
        crudOperations.update(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_EDIT), page.getTransactionForm(),
            formFragment -> formFragment.select("locking", lockings.get(0)),
            resourceVerifier -> resourceVerifier.verifyAttribute("locking", lockings.get(0)));
    }

    @Test
    public void editMode() throws Exception {
        String currentMode =
            operations.readAttribute(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_EDIT), "mode")
                .stringValue();
        List<String> modes = new ArrayList<>(Arrays.asList("NONE", "BATCH", "NON_XA", "NON_DURABLE_XA", "FULL_XA"));
        modes.remove(currentMode);
        navigateToTransactionForm(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_EDIT);
        crudOperations.update(transactionAddress(CACHE_CONTAINER, SCATTERED_CACHE_TRANSACTION_EDIT), page.getTransactionForm(),
            formFragment -> formFragment.select("mode", modes.get(0)),
            resourceVerifier -> resourceVerifier.verifyAttribute("mode", modes.get(0)));
    }

}
