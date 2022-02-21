package org.jboss.hal.testsuite.test.runtime;

import java.io.IOException;
import java.util.List;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Standalone;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.ItemFragment;
import org.jboss.hal.testsuite.page.runtime.HostsPage;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

/**
 * @author <a href="mailto:padamec@redhat.com">Petr Adamec</a>
 */
@RunWith(Arquillian.class)
@Category(Standalone.class)
public class ActiveThreadsCountTest {
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    @Page
    private HostsPage page;

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    /**
     * Test if the preview in Runtime / Monitor / Batch shows values for Current (attribute current-thread-count)
     * For more information look at HAL-1767
     * @throws IOException
     */
    @Test
    public void ActiveThreadsCountTest() throws IOException {
        FinderFragment fragment = getMainAttributesFinder();
        selectServer(fragment);
        page.getRssElement().findElement(By.id("batch-jberet")).click();
        List<WebElement> elements = page.getHalFinderPreview().findElements(By.className("progress-description"));
        boolean isDisplayedCurrent = false;
        for (WebElement element : elements) {
            if (element.getText().equalsIgnoreCase("Current")) {
                isDisplayedCurrent = true;
                break;
            }
        }
        Assert.assertTrue("Add active threads count should be displayed. See HAL-1767", isDisplayedCurrent);
    }

    private FinderFragment getMainAttributesFinder() {
        FinderFragment fragment;
        if (ConfigUtils.isDomain()) {
            fragment = console.finder(NameTokens.RUNTIME, new FinderPath()
                .append(Ids.DOMAIN_BROWSE_BY, "hosts")
                .append(Ids.HOST, Ids.build("host", ConfigUtils.getDefaultHost())));
        } else {
            fragment = console.finder(NameTokens.RUNTIME);
        }
        return fragment;
    }

    private ItemFragment selectServer(FinderFragment fragment) throws IOException {
        if (ConfigUtils.isDomain()) {
            return fragment.column(Ids.SERVER)
                .selectItem(Ids.build(ConfigUtils.getDefaultHost(), ConfigUtils.getDefaultServer()));
        } else {
            return fragment.column(Ids.STANDALONE_SERVER_COLUMN)
                .selectItem("standalone-host-" + serverEnvironmentUtils.getServerHostName());
        }
    }

}
