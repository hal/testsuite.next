package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.scattered.cache;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.INFINISPAN;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.scatteredCacheAddress;

@RunWith(Arquillian.class)
public class ScatteredCacheFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration administration = new Administration(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String SCATTERED_CACHE_CREATE = "scattered-cache-create-" + Random.name();
    private static final String SCATTERED_CACHE_DELETE = "scattered-cache-delete-" + Random.name();
    private static final String SCATTERED_CACHE_VIEW = "scattered-cache-view-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, TimeoutException, InterruptedException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
        operations.add(cacheContainerAddress(CACHE_CONTAINER).and("transport", "jgroups"));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_VIEW));
        operations.add(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_DELETE));
        administration.reloadIfRequired();
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

    private ColumnFragment cacheColumn;

    @Before
    public void initPage() {
        cacheColumn = console.finder(NameTokens.CONFIGURATION,
            configurationSubsystemPath(INFINISPAN).append(Ids.CACHE_CONTAINER, Ids.cacheContainer(CACHE_CONTAINER)))
            .column("cache");
    }

    @Test
    public void create() throws Exception {
        cacheColumn.dropdownAction("cache-add-actions", "scattered-cache-add");
        AddResourceDialogFragment addResourceDialogFragment = console.addResourceDialog();
        addResourceDialogFragment.getForm().text("name", SCATTERED_CACHE_CREATE);
        addResourceDialogFragment.add();
        console.verifySuccess();
        Assert.assertTrue("Newly created scattered cache should be present in the cache column",
            cacheColumn.containsItem(scatteredCacheId(SCATTERED_CACHE_CREATE)));
        new ResourceVerifier(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_CREATE), client).verifyExists();
    }

    private static String scatteredCacheId(String scatteredCacheName) {
        return Ids.build("scattered-cache", scatteredCacheName);
    }

    @Test
    public void view() {
        cacheColumn.selectItem(scatteredCacheId(SCATTERED_CACHE_VIEW)).view();
        console.verify(new PlaceRequest.Builder().nameToken("scattered-cache")
            .with("cache-container", CACHE_CONTAINER)
            .with("name", SCATTERED_CACHE_VIEW)
            .build());
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue("Scattered cache to be removed should be present in the column before removal",
            cacheColumn.containsItem(scatteredCacheId(SCATTERED_CACHE_DELETE)));
        cacheColumn.selectItem(scatteredCacheId(SCATTERED_CACHE_DELETE)).dropdown().click("Remove");
        console.confirmationDialog().confirm();
        console.verifySuccess();
        Assert.assertFalse("Recently removed scattered cache should not be present in the column anymore",
            cacheColumn.containsItem(scatteredCacheId(SCATTERED_CACHE_DELETE)));
        new ResourceVerifier(scatteredCacheAddress(CACHE_CONTAINER, SCATTERED_CACHE_DELETE), client).verifyDoesNotExist();
    }
}
