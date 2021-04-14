package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store.jdbc;

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
import org.jboss.hal.testsuite.fixtures.DataSourceFixtures;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.commands.datasources.RemoveDataSource;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.jdbcStoreAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class AttributesTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();
    private static final String DATA_SOURCE_CREATE = "data-source-to-be-created-" + Random.name();
    private static final String DATA_SOURCE_EDIT = "data-source-to-be-edited-" + Random.name();

    @BeforeClass
    public static void init() throws IOException, CommandFailedException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));
        client.apply(
            new AddDataSource.Builder<>(DATA_SOURCE_CREATE).driverName("h2").jndiName(Random.jndiName()).connectionUrl(
                DataSourceFixtures.h2ConnectionUrl(Random.name())).build());
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), Values.of("data-source", DATA_SOURCE_CREATE));
        client.apply(
            new AddDataSource.Builder<>(DATA_SOURCE_EDIT).driverName("h2").jndiName(Random.jndiName()).connectionUrl(
                DataSourceFixtures.h2ConnectionUrl(Random.name())).build());
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
            client.apply(new RemoveDataSource(DATA_SOURCE_CREATE));
            client.apply(new RemoveDataSource(DATA_SOURCE_EDIT));
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
    public void editDataSource() throws Exception {
        console.waitNoNotification();
        crudOperations.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getJdbcStoreAttributesForm(),
            "data-source", DATA_SOURCE_EDIT);
    }

    @Test
    public void editDialect() throws Exception {
        console.waitNoNotification();
        List<String> dialects = new ArrayList<>(
            Arrays.asList("MARIA_DB", "MY_SQL", "POSTGRES", "DERBY", "HSQL", "H2", "SQLITE", "DB2", "DB2_390", "INFORMIX",
                "INTERBASE", "FIREBIRD", "SQL_SERVER", "ACCESS", "ORACLE", "SYBASE"));
        String currentDialect =
            operations.readAttribute(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "dialect").stringValue("");
        dialects.remove(currentDialect);
        String dialect = dialects.get(Random.number(0, dialects.size()));
        crudOperations.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getJdbcStoreAttributesForm(),
            formFragment -> formFragment.select("dialect", dialect),
            resourceVerifier -> resourceVerifier.verifyAttribute("dialect", dialect));
    }

    @Test
    public void toggleFetchState() throws Exception {
        console.waitNoNotification();
        boolean fetchState = operations.readAttribute(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "fetch-state")
            .booleanValue(true);
        crudOperations.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getJdbcStoreAttributesForm(),
            "fetch-state", !fetchState);
    }

    @Test
    public void editMaxBatchSize() throws Exception {
        console.waitNoNotification();
        crudOperations.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getJdbcStoreAttributesForm(),
            "max-batch-size", Random.number());
    }

    @Test
    public void togglePassivation() throws Exception {
        console.waitNoNotification();
        crudOperations.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getJdbcStoreAttributesForm(),
            "max-batch-size", Random.number());
    }

    @Test
    public void togglePreload() throws Exception {
        console.waitNoNotification();
        boolean preload =
            operations.readAttribute(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "preload")
                .booleanValue(false);
        crudOperations.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getJdbcStoreAttributesForm(),
            "preload", !preload);
    }

    @Test
    public void editProperties() throws Exception {
        console.waitNoNotification();
        crudOperations.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getJdbcStoreAttributesForm(),
            "properties", Random.properties());
    }

    @Test
    public void togglePurge() throws Exception {
        console.waitNoNotification();
        boolean purge =
            operations.readAttribute(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "purge")
                .booleanValue(true);
        crudOperations.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getJdbcStoreAttributesForm(),
            "purge", !purge);
    }

    @Test
    public void toggleShared() throws Exception {
        console.waitNoNotification();
        boolean shared =
            operations.readAttribute(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), "shared")
                .booleanValue(false);
        crudOperations.update(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), page.getJdbcStoreAttributesForm(),
            "shared", !shared);
    }
}
