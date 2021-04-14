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
import org.jboss.hal.testsuite.page.configuration.UndertowServletContainerPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;

@RunWith(Arquillian.class)
public class CrawlerTest {

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

    private static final String SERVLET_CONTAINER_CRAWLER_CREATE =
        "servlet-container-without-crawler-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_CRAWLER_REMOVE =
        "servlet-container-with-crawler-" + RandomStringUtils.randomAlphanumeric(7);

    private static final Address SERVLET_CONTAINER_EDIT_CRAWLER_ADDRESS = crawlerAddress(SERVLET_CONTAINER_EDIT);

    private static Address crawlerAddress(String servletContainer) {
        return UndertowFixtures.servletContainerAddress(servletContainer).and("setting", "crawler-session-management");
    }

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
        operations.add(SERVLET_CONTAINER_EDIT_CRAWLER_ADDRESS);
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_CRAWLER_CREATE));
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_CRAWLER_REMOVE));
        operations.add(crawlerAddress(SERVLET_CONTAINER_CRAWLER_REMOVE));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_CRAWLER_CREATE));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_CRAWLER_REMOVE));
    }

    @Test
    public void create() throws Exception {
        navigateToCrawlerForm(SERVLET_CONTAINER_CRAWLER_CREATE);
        crudOperations.createSingleton(crawlerAddress(SERVLET_CONTAINER_CRAWLER_CREATE), page.getCrawlerForm());
    }

    private void navigateToCrawlerForm(String servletContainerName) {
        page.navigateAgain(NAME, servletContainerName);
        // necessary to call 2 times as the first navigation doesn't open the view with the correct name parameter
        page.navigateAgain(NAME, servletContainerName);
        console.verticalNavigation()
            .selectPrimary(Ids.build(Ids.UNDERTOW_SERVLET_CONTAINER_CRAWLER, "item"));
    }

    @Test
    public void remove() throws Exception {
        navigateToCrawlerForm(SERVLET_CONTAINER_CRAWLER_REMOVE);
        crudOperations.deleteSingleton(crawlerAddress(SERVLET_CONTAINER_CRAWLER_REMOVE), page.getCrawlerForm());
    }

    @Test
    public void editSessionTimeout() throws Exception {
        navigateToCrawlerForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_CRAWLER_ADDRESS, page.getCrawlerForm(), "session-timeout", Random.number());
    }

    @Test
    public void editUserAgents() throws Exception {
        navigateToCrawlerForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_CRAWLER_ADDRESS, page.getCrawlerForm(), "user-agents");
    }

}
