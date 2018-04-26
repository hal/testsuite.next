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
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class ResponseHeaderTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowFiltersPage page;

    private static final String RESPONSE_HEADER_CREATE =
        "response-header-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String RESPONSE_HEADER_DELETE =
        "response-header-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFiltersFixtures.responseHeaderAddress(RESPONSE_HEADER_DELETE),
            Values.of(UndertowFiltersFixtures.HEADER_NAME, Random.name()).and(UndertowFiltersFixtures.HEADER_VALUE, Random.name()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFiltersFixtures.responseHeaderAddress(RESPONSE_HEADER_CREATE));
        operations.removeIfExists(UndertowFiltersFixtures.responseHeaderAddress(RESPONSE_HEADER_DELETE));
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-response-header", "item"));
    }

    @Test
    public void create() throws Exception {
        String headerName = Random.name();
        String headerValue = Random.name();
        try {
            crudOperations.create(UndertowFiltersFixtures.responseHeaderAddress(RESPONSE_HEADER_CREATE),
                page.getResponseHeaderTable(),
                formFragment -> {
                    formFragment.text("name", RESPONSE_HEADER_CREATE);
                    formFragment.text(UndertowFiltersFixtures.HEADER_NAME, headerName);
                    formFragment.text(UndertowFiltersFixtures.HEADER_VALUE, headerValue);
                }, resourceVerifier -> {
                    resourceVerifier.verifyExists();
                    resourceVerifier.verifyAttribute(UndertowFiltersFixtures.HEADER_NAME, headerName);
                    resourceVerifier.verifyAttribute(UndertowFiltersFixtures.HEADER_VALUE, headerValue);
                });
        } catch (NoSuchElementException e) {
            Assert.fail("HAL-1448");
        }
    }

    @Test
    public void delete() throws Exception {
        try {
            crudOperations.delete(UndertowFiltersFixtures.responseHeaderAddress(RESPONSE_HEADER_DELETE),
                page.getResponseHeaderTable(),
                RESPONSE_HEADER_DELETE);
        } catch (NoSuchElementException e) {
            Assert.fail("HAL-1448");
        }
    }
}
