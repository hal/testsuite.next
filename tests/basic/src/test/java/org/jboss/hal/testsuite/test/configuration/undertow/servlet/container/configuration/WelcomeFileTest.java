package org.jboss.hal.testsuite.test.configuration.undertow.servlet.container.configuration;

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
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.UndertowServletContainerPage;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFixtures;
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

@RunWith(Arquillian.class)
public class WelcomeFileTest {

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

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
    }

    @Before
    public void initPage() {
        page.navigate("name", SERVLET_CONTAINER_EDIT);
        console.verticalNavigation()
            .selectPrimary(Ids.UNDERTOW_SERVLET_CONTAINER_CONFIGURATION_ITEM);
    }

    @Test
    public void editWelcomeFile() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT).and("welcome-file","aaa"),
            page.getWelcomeFileForm(),
            formFragment -> formFragment.list("welcome-file").add("aaa"), ResourceVerifier::verifyExists);
    }

    @Test
    public void cancelEditWelcomeFile() {
        FormFragment form = page.getWelcomeFileForm();
        form.edit();
        form.cancel();
        Assert.assertTrue(console.verifyNoError());
    }
}
