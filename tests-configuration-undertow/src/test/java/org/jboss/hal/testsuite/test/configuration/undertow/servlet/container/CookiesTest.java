package org.jboss.hal.testsuite.test.configuration.undertow.servlet.container;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.UndertowServletContainerPage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;

@RunWith(Arquillian.class)
public class CookiesTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private UndertowServletContainerPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String SERVLET_CONTAINER_EDIT =
        "servlet-container-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_COOKIES_CREATE =
        "servlet-container-without-cookies-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_COOKIES_REMOVE =
        "servlet-container-with-cookies-" + RandomStringUtils.randomAlphanumeric(7);

    private static final Address SERVLET_CONTAINER_EDIT_COOKIES_ADDRESS = cookiesAddress(SERVLET_CONTAINER_EDIT);

    private static Address cookiesAddress(String servletContainer) {
        return UndertowFixtures.servletContainerAddress(servletContainer).and("setting", "session-cookie");
    }

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
        operations.add(SERVLET_CONTAINER_EDIT_COOKIES_ADDRESS);
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_COOKIES_CREATE));
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_COOKIES_REMOVE));
        operations.add(cookiesAddress(SERVLET_CONTAINER_COOKIES_REMOVE));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_COOKIES_CREATE));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_COOKIES_REMOVE));
    }

    @Test
    public void create() throws Exception {
        navigateToCookiesForm(SERVLET_CONTAINER_COOKIES_CREATE);
        crudOperations.createSingleton(cookiesAddress(SERVLET_CONTAINER_COOKIES_CREATE), page.getCookiesForm());
    }

    private void navigateToCookiesForm(String servletContainerName) {
        page.navigate(NAME, servletContainerName);
        // necessary to call 2 times as the first navigation doesn't open the view with the correct name parameter
        // page.navigate(NAME, servletContainerName);
        console.verticalNavigation()
            .selectPrimary(Ids.build(Ids.UNDERTOW_SERVLET_CONTAINER_COOKIE, "item"));
    }

    @Test
    public void remove() throws Exception {
        navigateToCookiesForm(SERVLET_CONTAINER_COOKIES_REMOVE);
        crudOperations.deleteSingleton(cookiesAddress(SERVLET_CONTAINER_COOKIES_REMOVE), page.getCookiesForm());
    }

    @Test
    public void editDomain() throws Exception {
        navigateToCookiesForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_COOKIES_ADDRESS, page.getCookiesForm(), "domain");
    }

    @Test
    public void toggleHttpOnly() throws Exception {
        navigateToCookiesForm(SERVLET_CONTAINER_EDIT);
        boolean httpOnly = operations.readAttribute(SERVLET_CONTAINER_EDIT_COOKIES_ADDRESS, "http-only").booleanValue(false);
        crudOperations.update(SERVLET_CONTAINER_EDIT_COOKIES_ADDRESS, page.getCookiesForm(), "http-only", !httpOnly);
    }

    @Test
    public void editMaxAge() throws Exception {
        navigateToCookiesForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_COOKIES_ADDRESS, page.getCookiesForm(), "max-age", Random.number());
    }

    @Test
    public void editName() throws Exception {
        navigateToCookiesForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_COOKIES_ADDRESS, page.getCookiesForm(), "name");
    }

    @Test
    public void toggleSecure() throws Exception {
        navigateToCookiesForm(SERVLET_CONTAINER_EDIT);
        boolean secure = operations.readAttribute(SERVLET_CONTAINER_EDIT_COOKIES_ADDRESS, "secure").booleanValue(false);
        crudOperations.update(SERVLET_CONTAINER_EDIT_COOKIES_ADDRESS, page.getCookiesForm(), "secure", !secure);
    }
    @Test
    public void cancelEditCookieForm() {
        navigateToCookiesForm(SERVLET_CONTAINER_EDIT);
        FormFragment form = page.getCookiesForm();
        form.edit();
        form.cancel();
        Assert.assertTrue(console.verifyNoError());
    }
}
