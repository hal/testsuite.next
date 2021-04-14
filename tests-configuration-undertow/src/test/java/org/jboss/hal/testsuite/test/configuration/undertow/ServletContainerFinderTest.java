package org.jboss.hal.testsuite.test.configuration.undertow;

import java.io.IOException;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
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

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;

@RunWith(Arquillian.class)
public class ServletContainerFinderTest {

    @Inject
    private Console console;

    @Drone
    private WebDriver browser;

    private ColumnFragment servletContainerColumn;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String SERVLET_CONTAINER_CREATE =
        "servlet-container-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_VIEW =
        "servlet-container-to-be-viewed-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_REFRESH =
        "servlet-container-to-be-refreshed-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_REMOVE =
        "servlet-container-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_REMOVE));
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_VIEW));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(
            UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_CREATE));
        operations.removeIfExists(
            UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_VIEW));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(
            SERVLET_CONTAINER_REFRESH));
        operations.removeIfExists(
            UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_REMOVE));
    }

    @Before
    public void initColumn() {
        servletContainerColumn =
            console.finder(NameTokens.CONFIGURATION, FinderFragment.configurationSubsystemPath(NameTokens.UNDERTOW)
                .append(Ids.UNDERTOW_SETTINGS, "servlet-container"))
                .column(Ids.UNDERTOW_SERVLET_CONTAINER);
    }

    @Test
    public void create() {
        AddResourceDialogFragment addResourceDialogFragment = servletContainerColumn.add();
        FormFragment formFragment = addResourceDialogFragment.getForm();
        formFragment.text(NAME, SERVLET_CONTAINER_CREATE);
        addResourceDialogFragment.add();
        console.verifySuccess();
        Assert.assertTrue("Newly added servlet container should be present in the column",
            servletContainerColumn.containsItem(
                Ids.undertowServletContainer(SERVLET_CONTAINER_CREATE)));
    }

    @Test
    public void refresh() throws IOException {
        Assert.assertFalse("Servlet container should not be visible before refresh",
            servletContainerColumn.containsItem(
                Ids.undertowServletContainer(SERVLET_CONTAINER_REFRESH)));
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_REFRESH));
        servletContainerColumn.refresh();
        Assert.assertTrue("Servlet container should be visible after refresh",
            servletContainerColumn.containsItem(Ids.undertowServletContainer(
                SERVLET_CONTAINER_REFRESH)));
    }

    @Test
    public void remove() {
        Assert.assertTrue("Servlet container should be visible before removal",
            servletContainerColumn.containsItem(
                Ids.undertowServletContainer(SERVLET_CONTAINER_REMOVE)));
        servletContainerColumn.selectItem(
            Ids.undertowServletContainer(SERVLET_CONTAINER_REMOVE))
            .dropdown().click("Remove");
        console.confirmationDialog().confirm();
        console.verifySuccess();
        Assert.assertFalse("Application security domain should not be present in the column anymore",
            servletContainerColumn.containsItem(
                Ids.undertowServletContainer(SERVLET_CONTAINER_REMOVE)));
    }

    @Test
    public void view() {
        servletContainerColumn.selectItem(Ids.undertowServletContainer(SERVLET_CONTAINER_VIEW))
            .view();
        console.verify(new PlaceRequest.Builder().nameToken(NameTokens.UNDERTOW_SERVLET_CONTAINER)
            .with("name", SERVLET_CONTAINER_VIEW).build());
    }
}
