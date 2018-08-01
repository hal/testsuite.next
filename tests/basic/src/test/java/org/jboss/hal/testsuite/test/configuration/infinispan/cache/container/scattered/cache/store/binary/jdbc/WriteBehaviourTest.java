package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store.binary.jdbc;

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
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.commands.datasources.RemoveDataSource;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.BEHIND;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.THROUGH;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.WRITE;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.binaryJDBCStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class WriteBehaviourTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_WRITE_BEHIND = "scattered-cache-with-write-behind-" + Random.name();
    private static final String SCATTERED_CACHE_WRITE_THROUGH = "scattered-cache-with-write-through-" + Random.name();
    private static final String SCATTERED_CACHE_WRITE_BEHIND_EDIT =
        "scattered-cache-with-write-behind-to-edit-" + Random.name();
    private static final String DATA_SOURCE_BEHIND = "data-source-for-scattered-cache-with-write-behind-" + Random.name();
    private static final String DATA_SOURCE_BEHIND_EDIT =
        "data-source-for-scattered-cache-with-write-behind-to-be-edited-" + Random.name();
    private static final String DATA_SOURCE_THROUGH =
        "data-source-for-scattered-cache-with-write-through-" + Random.name();

    @BeforeClass
    public static void init() throws IOException, CommandFailedException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_THROUGH));
        client.apply(
            new AddDataSource.Builder<>(DATA_SOURCE_BEHIND).driverName("h2").jndiName(Random.jndiName()).connectionUrl(
                DataSourceFixtures.h2ConnectionUrl(Random.name())).build());
        client.apply(
            new AddDataSource.Builder<>(DATA_SOURCE_BEHIND_EDIT).driverName("h2")
                .jndiName(Random.jndiName())
                .connectionUrl(
                    DataSourceFixtures.h2ConnectionUrl(Random.name()))
                .build());
        client.apply(
            new AddDataSource.Builder<>(DATA_SOURCE_THROUGH).driverName("h2").jndiName(Random.jndiName()).connectionUrl(
                DataSourceFixtures.h2ConnectionUrl(Random.name())).build());
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND), Values.of("data-source",
                DATA_SOURCE_BEHIND));
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT),
                Values.of("data-source", DATA_SOURCE_BEHIND_EDIT));
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_THROUGH), Values.of("data-source",
                DATA_SOURCE_THROUGH));
        operations.batch(
            new Batch().remove(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND).and(WRITE, THROUGH))
                .add(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND).and(WRITE, BEHIND)));
        operations.batch(
            new Batch().remove(
                binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT).and(WRITE, THROUGH))
                .add(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT).and(WRITE, BEHIND)));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
            client.apply(new RemoveDataSource(DATA_SOURCE_BEHIND));
            client.apply(new RemoveDataSource(DATA_SOURCE_BEHIND_EDIT));
            client.apply(new RemoveDataSource(DATA_SOURCE_THROUGH));
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

    private void navigateToWriteBehaviour(String cacheContainer, String scatteredCache) {
        Map<String, String> params = new HashMap<>();
        params.put("cache-container", cacheContainer);
        params.put("name", scatteredCache);
        page.navigate(params);
        console.verticalNavigation().selectPrimary("scattered-cache-store-item");
    }

    @Test
    public void switchToWriteBehind() throws Exception {
        navigateToWriteBehaviour(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_THROUGH);
        page.getBinaryJDBCStoreTab().select(ScatteredCachePage.BINARY_JDBC_STORE_WRITE_BEHAVIOUR_TAB);
        page.switchBehaviour();
        new ResourceVerifier(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_THROUGH).and(WRITE, BEHIND),
            client).verifyExists();
    }

    @Test
    public void switchToWriteThrough() throws Exception {
        navigateToWriteBehaviour(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND);
        page.getBinaryJDBCStoreTab().select(ScatteredCachePage.BINARY_JDBC_STORE_WRITE_BEHAVIOUR_TAB);
        page.switchBehaviour();
        new ResourceVerifier(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND).and(WRITE, THROUGH),
            client).verifyExists();
    }

    @Test
    public void editModificationQueueSize() throws Exception {
        navigateToWriteBehaviour(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT);
        crudOperations.update(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT).and(WRITE, BEHIND),
            page.getBinaryJDBCStoreWriteBehindForm(),
            "modification-queue-size", Random.number());
    }

    @Test
    public void editThreadPoolSize() throws Exception {
        navigateToWriteBehaviour(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT);
        crudOperations.update(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_WRITE_BEHIND_EDIT).and(WRITE, BEHIND),
            page.getBinaryJDBCStoreWriteBehindForm(),
            "thread-pool-size", Random.number());
    }

}
