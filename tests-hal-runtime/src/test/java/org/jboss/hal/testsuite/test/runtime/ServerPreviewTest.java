package org.jboss.hal.testsuite.test.runtime;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.ItemFragment;
import org.jboss.hal.testsuite.fragment.finder.ServerPreviewFragment;
import org.jboss.hal.testsuite.util.AvailablePortFinder;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

@RunWith(Arquillian.class)
public class ServerPreviewTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private static final String EDIT_URL_LABEL = "Edit URL";
    private static final String CUSTOM_URL_ICON_CLASS = "fa-external-link";
    private static final String DEFAULT_URL_ICON_CLASS = "pficon-server";
    private static final String RESET_BTN_ID = "server-url-form-server-url-form-reset";

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }


    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Test
    public void editUrl() throws IOException {
        String host = Random.name();
        FinderFragment fragment = getMainAttributesFinder();
        resetUrlInFinder(fragment);
        editHostInFinder(host, fragment);
        ServerPreviewFragment serverPreviewFragment = fragment.preview(ServerPreviewFragment.class);
        Assert.assertTrue("\"URL\" field contains value set by test",
            serverPreviewFragment.getUrlAttributeItem().getValueElement().getText().contains(host));
    }

    @Test
    public void editUrlAndVerifyIsClickable() throws IOException {
        String host = "localhost";
        int port = AvailablePortFinder.getNextAvailableTCPPort();
        String url = String.format("https://%s:%d", host, port);
        FinderFragment fragment = getMainAttributesFinder();
        resetUrlInFinder(fragment);
        editUrlInFinder("https", host, port, fragment);
        fragment.preview(ServerPreviewFragment.class).getUrlAttributeItem().getValueElement()
            .findElement(By.partialLinkText(url)).click();
        verifyBrowserRedirected(url, "Browser should be redirected after clicking on the \"URL\" attribute");
    }

    @Test
    public void editUrlAndVerifyIconChanged() throws IOException {
        FinderFragment fragment = getMainAttributesFinder();
        selectServer(fragment);
        ServerPreviewFragment serverPreviewFragment = fragment.preview(ServerPreviewFragment.class);

        resetUrlInFinder(fragment);
        editHostInFinder(Random.name(), fragment);
        verifyIconClass(serverPreviewFragment, CUSTOM_URL_ICON_CLASS,
            "Icon for the URL should be custom");
        resetUrlInFinder(fragment);
        verifyIconClass(serverPreviewFragment, DEFAULT_URL_ICON_CLASS,
            "Icon for the URL should be the one provided by model");
    }

    private void verifyIconClass(ServerPreviewFragment serverPreviewFragment, String iconClass, String assertMessage) {
        Assert.assertTrue(assertMessage,
            serverPreviewFragment.getUrlAttributeItem().getValueElement().findElement(ByJQuery.selector("span > span"))
                .getAttribute("class").contains(iconClass));
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

    private void editUrlInFinder(String scheme, String host, int port, FinderFragment fragment) throws IOException {
        selectServer(fragment).dropdown().click(EDIT_URL_LABEL);
        AddResourceDialogFragment editURLDialog = console.addResourceDialog();
        editURLDialog.getForm().select("scheme", scheme);
        editURLDialog.getForm().text("host", host);
        editURLDialog.getForm().number("port", port);
        editURLDialog.add();
    }

    private void editHostInFinder(String host, FinderFragment fragment) throws IOException {
        selectServer(fragment).dropdown().click(EDIT_URL_LABEL);
        AddResourceDialogFragment editURLDialog = console.addResourceDialog();
        editURLDialog.getForm().text("host", host);
        editURLDialog.add();
    }

    private void resetUrlInFinder(FinderFragment fragment) throws IOException {
        selectServer(fragment).dropdown().click(EDIT_URL_LABEL);
        AddResourceDialogFragment editURLDialog = console.addResourceDialog();
        editURLDialog.getRoot().findElement(By.id(RESET_BTN_ID)).click();
    }
}
