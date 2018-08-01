package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.page.configuration.ScatteredCachePage;
import org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.binaryJDBCStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.customStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.fileStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.jdbcStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.mixedJDBCStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class StoreTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_CUSTOM_STORE =
        "scattered-cache-with-custom-store-to-be-created" + Random.name();
    private static final String SCATTERED_CACHE_FILE_STORE =
        "scattered-cache-with-file-store-to-be-created" + Random.name();
    private static final String SCATTERED_CACHE_JDBC_STORE =
        "scattered-cache-with-jdbc-store-to-be-created" + Random.name();
    private static final String SCATTERED_CACHE_BINARY_JDBC_STORE =
        "scattered-cache-with-binary-jdbc-store-to-be-created" + Random.name();
    private static final String SCATTERED_CACHE_MIXED_JDBC_STORE =
        "scattered-cache-with-mixed-jdbc-store-to-be-created" + Random.name();
    private static final String DATA_SOURCE_JDBC = "data-source-for-jdbc-store-" + Random.name();
    private static final String DATA_SOURCE_BINARY_JDBC = "data-source-for-jdbc-store-" + Random.name();
    private static final String DATA_SOURCE_MIXED_JDBC = "data-source-for-jdbc-store-" + Random.name();

    @BeforeClass
    public static void init() throws IOException, CommandFailedException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_FILE_STORE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_CUSTOM_STORE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_JDBC_STORE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_BINARY_JDBC_STORE));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_MIXED_JDBC_STORE));
        client.apply(
            new AddDataSource.Builder<>(DATA_SOURCE_JDBC).driverName("h2").jndiName(Random.jndiName()).connectionUrl(
                DataSourceFixtures.h2ConnectionUrl(Random.name())).build());
        client.apply(
            new AddDataSource.Builder<>(DATA_SOURCE_BINARY_JDBC).driverName("h2")
                .jndiName(Random.jndiName())
                .connectionUrl(
                    DataSourceFixtures.h2ConnectionUrl(Random.name()))
                .build());
        client.apply(
            new AddDataSource.Builder<>(DATA_SOURCE_MIXED_JDBC).driverName("h2")
                .jndiName(Random.jndiName())
                .connectionUrl(
                    DataSourceFixtures.h2ConnectionUrl(Random.name()))
                .build());
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

    @Page
    private ScatteredCachePage page;

    public void navigate(String cacheContainer, String scatteredCache) {
        Map<String, String> params = new HashMap<>();
        params.put("cache-container", cacheContainer);
        params.put("name", scatteredCache);
        page.navigate(params);
        console.verticalNavigation().selectPrimary("scattered-cache-store-item");
    }

    @Test
    public void addCustomStore() throws Exception {
        navigate(CACHE_CONTAINER, SCATTERED_CACHE_CUSTOM_STORE);
        String customClass = Random.name();
        page.getSelectStoreDropdown().selectExact("Custom", "custom");
        page.getEmptyStoreForm().mainAction();
        AddResourceDialogFragment addResourceDialog = console.addResourceDialog();
        addResourceDialog.getForm().text("class", customClass);
        addResourceDialog.add();
        console.verifySuccess();
        new ResourceVerifier(customStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_CUSTOM_STORE), client).verifyExists();
    }

    @Test
    public void addFileStore() throws Exception {
        navigate(CACHE_CONTAINER, SCATTERED_CACHE_FILE_STORE);
        page.getSelectStoreDropdown().selectExact("File", "file");
        page.getEmptyStoreForm().mainAction();
        console.verifySuccess();
        new ResourceVerifier(fileStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_FILE_STORE), client).verifyExists();
    }

    @Test
    public void addJDBCStore() throws Exception {
        navigate(CACHE_CONTAINER, SCATTERED_CACHE_JDBC_STORE);
        page.getSelectStoreDropdown().selectExact("JDBC", "jdbc");
        page.getEmptyStoreForm().mainAction();
        AddResourceDialogFragment addResourceDialogFragment = console.addResourceDialog();
        addResourceDialogFragment.getForm().text("data-source", DATA_SOURCE_JDBC);
        addResourceDialogFragment.add();
        console.verifySuccess();
        new ResourceVerifier(jdbcStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_JDBC_STORE),
            client).verifyExists();
    }

    @Test
    public void addBinaryJDBCStore() throws Exception {
        navigate(CACHE_CONTAINER, SCATTERED_CACHE_BINARY_JDBC_STORE);
        page.getSelectStoreDropdown().selectExact("Binary JDBC", "binary-jdbc");
        page.getEmptyStoreForm().mainAction();
        AddResourceDialogFragment addResourceDialogFragment = console.addResourceDialog();
        addResourceDialogFragment.getForm().text("data-source", DATA_SOURCE_BINARY_JDBC);
        addResourceDialogFragment.add();
        console.verifySuccess();
        new ResourceVerifier(binaryJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_BINARY_JDBC_STORE),
            client).verifyExists();
    }

    @Test
    public void addMixedJDBCStore() throws Exception {
        navigate(CACHE_CONTAINER, SCATTERED_CACHE_MIXED_JDBC_STORE);
        page.getSelectStoreDropdown().selectExact("Mixed JDBC", "mixed-jdbc");
        page.getEmptyStoreForm().mainAction();
        AddResourceDialogFragment addResourceDialogFragment = console.addResourceDialog();
        addResourceDialogFragment.getForm().text("data-source", DATA_SOURCE_MIXED_JDBC);
        addResourceDialogFragment.add();
        console.verifySuccess();
        new ResourceVerifier(mixedJDBCStoreAddress(CACHE_CONTAINER, SCATTERED_CACHE_MIXED_JDBC_STORE),
            client).verifyExists();
    }
}
