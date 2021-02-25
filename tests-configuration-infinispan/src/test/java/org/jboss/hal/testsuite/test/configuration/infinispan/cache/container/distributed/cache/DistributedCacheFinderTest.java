package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container.distributed.cache;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.distributedCacheAddress;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;

@RunWith(Arquillian.class)
public class DistributedCacheFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration administration = new Administration(client);

    private static final String CACHE_CONTAINER = "cache-container-" + Random.name();
    private static final String DISTRIBUTED_CACHE_CREATE = "distributed-cache-create-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException, TimeoutException, InterruptedException {
        operations.add(cacheContainerAddress(CACHE_CONTAINER));
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
        cacheColumn.dropdownAction("cache-add-actions", "distributed-cache-add");
        AddResourceDialogFragment addResourceDialogFragment = console.addResourceDialog();
        addResourceDialogFragment.getForm().text("name", DISTRIBUTED_CACHE_CREATE);
        addResourceDialogFragment.add();
        console.verifySuccess();
        Assert.assertTrue("Newly created distributed cache should be present in the cache column",
                cacheColumn.containsItem(distributedCacheId(DISTRIBUTED_CACHE_CREATE)));
        new ResourceVerifier(distributedCacheAddress(CACHE_CONTAINER, DISTRIBUTED_CACHE_CREATE), client).verifyExists();
    }

    private static String distributedCacheId(String distributedCacheName) {
        return Ids.build("distributed-cache", distributedCacheName);
    }

}
