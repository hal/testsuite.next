package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store.binary.jdbc;

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
import org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures;
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
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.binaryJDBCStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class BinaryTableTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE = "scattered-cache-" + Random.name();
    private static final String DATA_SOURCE = "data-source-to-be-created-" + Random.name();

    private static final Address BINARY_TABLE_ADDRESS =
        binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE).and("table", "binary");

    @BeforeClass
    public static void init() throws IOException, CommandFailedException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE));
        client.apply(
            new AddDataSource.Builder<>(DATA_SOURCE).driverName("h2").jndiName(Random.jndiName()).connectionUrl(
                DataSourceFixtures.h2ConnectionUrl(Random.name())).build());
        operations.headers(Values.of("allow-resource-service-restart", true))
            .add(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE), Values.of("data-source", DATA_SOURCE));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        try {
            operations.removeIfExists(cacheContainerAddress(CACHE_CONTAINER));
            client.apply(new RemoveDataSource(DATA_SOURCE));
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
    public void editPrefix() throws Exception {
        crudOperations.update(BINARY_TABLE_ADDRESS, page.getBinaryJDBCStoreBinaryTableForm(), "prefix");
    }

    @Test
    public void editIDColumnName() throws Exception {
        String idColumnName = Random.name();
        crudOperations.update(BINARY_TABLE_ADDRESS, page.getBinaryJDBCStoreBinaryTableForm(),
            formFragment -> formFragment.textByLabel("ID Column Name", idColumnName),
            resourceVerifier -> resourceVerifier.verifyAttribute("id-column.name", idColumnName));
    }

    @Test
    public void editIDColumnType() throws Exception {
        String idColumnType = Random.name();
        crudOperations.update(BINARY_TABLE_ADDRESS, page.getBinaryJDBCStoreBinaryTableForm(),
            formFragment -> formFragment.textByLabel("ID Column Type", idColumnType),
            resourceVerifier -> resourceVerifier.verifyAttribute("id-column.type", idColumnType));
    }

    @Test
    public void editDataColumnName() throws Exception {
        String dataColumnName = Random.name();
        crudOperations.update(BINARY_TABLE_ADDRESS, page.getBinaryJDBCStoreBinaryTableForm(),
            formFragment -> formFragment.textByLabel("Data Column Name", dataColumnName),
            resourceVerifier -> resourceVerifier.verifyAttribute("data-column.name", dataColumnName));
    }

    @Test
    public void editDataColumnType() throws Exception {
        String dataColumnType = Random.name();
        crudOperations.update(BINARY_TABLE_ADDRESS, page.getBinaryJDBCStoreBinaryTableForm(),
            formFragment -> formFragment.textByLabel("Data Column Type", dataColumnType),
            resourceVerifier -> resourceVerifier.verifyAttribute("data-column.type", dataColumnType));
    }

    @Test
    public void editTimestampColumnName() throws Exception {
        String timestampColumnName = Random.name();
        crudOperations.update(BINARY_TABLE_ADDRESS, page.getBinaryJDBCStoreBinaryTableForm(),
            formFragment -> formFragment.textByLabel("Timestamp Column Name", timestampColumnName),
            resourceVerifier -> resourceVerifier.verifyAttribute("timestamp-column.name", timestampColumnName));
    }

    @Test
    public void editTimestampColumnType() throws Exception {
        String timeStampColumnName = Random.name();
        crudOperations.update(BINARY_TABLE_ADDRESS, page.getBinaryJDBCStoreBinaryTableForm(),
            formFragment -> formFragment.textByLabel("Timestamp Column Type", timeStampColumnName),
            resourceVerifier -> resourceVerifier.verifyAttribute("timestamp-column.type", timeStampColumnName));
    }

    @Test
    public void editFetchSize() throws Exception {
        crudOperations.update(BINARY_TABLE_ADDRESS, page.getBinaryJDBCStoreBinaryTableForm(), "fetch-size", Random.number());
    }

}
