package org.jboss.hal.testsuite.test.runtime.io;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.ItemFragment;
import org.jboss.hal.testsuite.fragment.finder.ServerPreviewFragment;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.SUBSYSTEM_ADDRESS;


@RunWith(Arquillian.class)
@Category(Domain.class)
public class UrlConnectionTestCase {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private static final Operations operations = new Operations(client);
    private static final BackupAndRestoreAttributes backup = new BackupAndRestoreAttributes.Builder(SUBSYSTEM_ADDRESS).build();
    private static final Administration administration = new Administration(client);

    @BeforeClass
    public static void setUp() throws IOException, OperationException, CommandFailedException {
        client.apply(backup.backup());
        if (operations.exists(UndertowFixtures.httpListenerAddress("default-server", "default"))) {
            operations.removeIfExists(UndertowFixtures.httpListenerAddress("default-server", "default"));
        }

    }
    @AfterClass
    public static void tearDown()
            throws IOException, CommandFailedException, OperationException, TimeoutException, InterruptedException, TimeoutException {
        try {
            client.apply(backup.restore());
            administration.reloadIfRequired();
        } finally {
            client.close();
        }
    }


    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Test
    public void editUrl() throws IOException {
        FinderFragment fragment = getMainAttributesFinder();
        selectServer(fragment);
        ServerPreviewFragment serverPreviewFragment = fragment.preview(ServerPreviewFragment.class);
        if (!serverPreviewFragment.getUrlAttributeItem().getValueElement().getText().contains("https")) {
            Assert.fail("URL should contain https!");
        }
    }




    public void verifyBrowserRedirected(String url, String assertMessage) {
        String redirectUrlExpression = "^" + url + "[/]?$";
        for (String windowHandle : browser.getWindowHandles()) {
            browser.switchTo().window(windowHandle);
            if (browser.getCurrentUrl().matches(redirectUrlExpression)) {
                return;
            }
        }
        Assert.fail(assertMessage);
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
