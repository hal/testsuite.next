package org.jboss.hal.testsuite.test.configuration.undertow.filters;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.UndertowFiltersPage;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFiltersFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class RequestLimitTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowFiltersPage page;

    private static final String REQUEST_LIMIT_CREATE =
        "request-limit-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String REQUEST_LIMIT_DELETE =
        "request-limit-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFiltersFixtures.requestLimitAddress(REQUEST_LIMIT_DELETE),
            Values.of("max-concurrent-requests", Random.number()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFiltersFixtures.requestLimitAddress(REQUEST_LIMIT_CREATE));
        operations.removeIfExists(UndertowFiltersFixtures.requestLimitAddress(REQUEST_LIMIT_DELETE));
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-request-limit", "item"));
    }

    @Test
    public void create() throws Exception {
        int maxConcurrentRequests = Random.number();
        crudOperations.create(UndertowFiltersFixtures.requestLimitAddress(REQUEST_LIMIT_CREATE), page.getRequestLimitTable(),
            formFragment -> {
                formFragment.text("name", REQUEST_LIMIT_CREATE);
                formFragment.number("max-concurrent-requests", maxConcurrentRequests);
            }, resourceVerifier -> {
                resourceVerifier.verifyExists();
                resourceVerifier.verifyAttribute("max-concurrent-requests", maxConcurrentRequests);
            });
    }

    @Test
    public void delete() throws Exception {
        crudOperations.delete(UndertowFiltersFixtures.requestLimitAddress(REQUEST_LIMIT_DELETE), page.getRequestLimitTable(),
            REQUEST_LIMIT_DELETE);
    }

}
