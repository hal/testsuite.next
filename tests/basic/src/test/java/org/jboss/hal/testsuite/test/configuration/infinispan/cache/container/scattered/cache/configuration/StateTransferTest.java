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
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.stateTransferAddress;

@RunWith(Arquillian.class)
public class StateTransferTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_STATE_TRANSFER_CREATE =
        "scattered-cache-with-state-transfer-to-be-created-" + Random.name();
    private static final String SCATTERED_CACHE_STATE_TRANSFER_DELETE =
        "scattered-cache-with-state-transfer-to-be-deleted-" + Random.name();
    private static final String SCATTERED_CACHE_STATE_TRANSFER_EDIT =
        "scattered-cache-with-state-transfer-to-be-edited-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, OperationException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_CREATE));
        operations.removeIfExists(
            stateTransferAddress(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_CREATE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_DELETE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_EDIT));
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
        navigateToStateTransferForm(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_CREATE);
        crudOperations.createSingleton(
            stateTransferAddress(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_CREATE),
            page.getStateTransferForm());
    }

    private void navigateToStateTransferForm(String cacheContainer, String scatteredCache) {
        Map<String, String> params = new HashMap<>();
        params.put("cache-container", cacheContainer);
        params.put("name", scatteredCache);
        page.navigate(params);
        console.verticalNavigation().selectPrimary("scattered-cache-item");
    }

    @Test
    public void delete() throws Exception {
        navigateToStateTransferForm(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_DELETE);
        crudOperations.deleteSingleton(
            stateTransferAddress(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_DELETE),
            page.getStateTransferForm());
    }

    @Test
    public void editChunkSize() throws Exception {
        navigateToStateTransferForm(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_EDIT);
        crudOperations.update(stateTransferAddress(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_EDIT),
            page.getStateTransferForm(), "chunk-size", Random.number(1, Integer.MAX_VALUE));
    }

    @Test
    public void editTimeout() throws Exception {
        navigateToStateTransferForm(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_EDIT);
        crudOperations.update(stateTransferAddress(CACHE_CONTAINER, SCATTERED_CACHE_STATE_TRANSFER_EDIT),
            page.getStateTransferForm(), "timeout", (long) Random.number());
    }
}
