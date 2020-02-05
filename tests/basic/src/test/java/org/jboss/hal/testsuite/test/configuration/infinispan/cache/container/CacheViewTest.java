package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container;

import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;


import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.INFINISPAN;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;

@RunWith(Arquillian.class)
public class CacheViewTest {
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration administration = new Administration(client);

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    private ColumnFragment cacheColumn;

    @Before
    public void initPage() {
        cacheColumn = console.finder(NameTokens.CONFIGURATION,
                configurationSubsystemPath(INFINISPAN).append(Ids.CACHE_CONTAINER, Ids.cacheContainer("web")))
                .column("cache");
    }

    /**
     *Check if internal error is displayed when click on view on cache.
     * See https://issues.redhat.com/browse/HAL-1627
     */
    @Test
    public void checkNoErrorTest() {
        List<WebElement> elements =  cacheColumn.getItems();
        elements.get(0).click();
        elements.get(0).findElement(By.className("clickable")).click();
        waitGui().until().element(By.className("back")).is().visible();
        Assert.assertTrue("Internal error is displayed! See https://issues.redhat.com/browse/HAL-1627",
                console.verifyNoError());
    }
}
