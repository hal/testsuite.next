package org.jboss.hal.testsuite.test.configuration.undertow.servlet.container;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.page.configuration.UndertowServletContainerPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;

@RunWith(Arquillian.class)
public class SessionsTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowServletContainerPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String SERVLET_CONTAINER_EDIT =
        "servlet-container-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_SESSIONS_CREATE =
        "servlet-container-without-sessions-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_SESSIONS_REMOVE =
        "servlet-container-with-sessions-" + RandomStringUtils.randomAlphanumeric(7);

    private static final Address SERVLET_CONTAINER_EDIT_SESSIONS_ADDRESS = sessionsAddress(SERVLET_CONTAINER_EDIT);

    private static Address sessionsAddress(String servletContainer) {
        return UndertowFixtures.servletContainerAddress(servletContainer).and("setting", "persistent-sessions");
    }

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT)).assertSuccess();
        operations.add(SERVLET_CONTAINER_EDIT_SESSIONS_ADDRESS).assertSuccess();
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_SESSIONS_CREATE)).assertSuccess();
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_SESSIONS_REMOVE)).assertSuccess();
        operations.add(sessionsAddress(SERVLET_CONTAINER_SESSIONS_REMOVE)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_SESSIONS_CREATE));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_SESSIONS_REMOVE));
    }

    @Test
    public void create() throws Exception {
        navigateToSessionsForm(SERVLET_CONTAINER_SESSIONS_CREATE);
        try {
            waitGui().until().element(By.id(Ids.build(Ids.UNDERTOW_SERVLET_CONTAINER_SESSION, "form-empty"))).is().visible();
        } catch (TimeoutException ex) {
            // ignore the intermittent exception and try again
            navigateToSessionsForm(SERVLET_CONTAINER_SESSIONS_CREATE);
        }
        crudOperations.createSingleton(sessionsAddress(SERVLET_CONTAINER_SESSIONS_CREATE), page.getSessionsForm());
    }

    private void navigateToSessionsForm(String servletContainerName) {
        page.navigateAgain(NAME, servletContainerName);
        // necessary to call 2 times as the first navigation doesn't open the view with the correct name parameter
        page.navigateAgain(NAME, servletContainerName);
        console.verticalNavigation()
            .selectPrimary(Ids.build(Ids.UNDERTOW_SERVLET_CONTAINER_SESSION, "item"));
    }

    @Test
    public void remove() throws Exception {
        navigateToSessionsForm(SERVLET_CONTAINER_SESSIONS_REMOVE);
        crudOperations.deleteSingleton(sessionsAddress(SERVLET_CONTAINER_SESSIONS_REMOVE), page.getSessionsForm());
    }

    @Test
    public void editPath() throws Exception {
        navigateToSessionsForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_SESSIONS_ADDRESS, page.getSessionsForm(), "path");
    }

    @Test
    public void editRelativeTo() throws Exception {
        navigateToSessionsForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_SESSIONS_ADDRESS, page.getSessionsForm(), "relative-to");
    }

}
