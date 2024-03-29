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
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFiltersFixtures;
import org.jboss.hal.testsuite.page.configuration.UndertowFiltersPage;
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
public class ExpressionFilterTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowFiltersPage page;

    private static final String EXPRESSION_VALUE_BASE = "path(/a) -> redirect(/%s)";
    private static final String EXPRESSION_FILTER_CREATE =
        "expression-filter-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String EXPRESSION_FILTER_DELETE =
        "expression-filter-to-be-deleted-" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFiltersFixtures.expressionFilterAddress(EXPRESSION_FILTER_DELETE),
            Values.of("expression", String.format(EXPRESSION_VALUE_BASE, Random.name()))).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFiltersFixtures.expressionFilterAddress(EXPRESSION_FILTER_CREATE));
        operations.removeIfExists(UndertowFiltersFixtures.expressionFilterAddress(EXPRESSION_FILTER_DELETE));
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-expression-filter", "item"));
    }

    @Test
    public void create() throws Exception {
        String expression = String.format(EXPRESSION_VALUE_BASE, Random.name());
        crudOperations.create(UndertowFiltersFixtures.expressionFilterAddress(EXPRESSION_FILTER_CREATE),
            page.getExpressionFilterTable(), formFragment -> {
                formFragment.text("name", EXPRESSION_FILTER_CREATE);
                formFragment.text("expression", expression);
            }, resourceVerifier -> {
                resourceVerifier.verifyExists();
                resourceVerifier.verifyAttribute("expression", expression);
            });
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(UndertowFiltersFixtures.expressionFilterAddress(EXPRESSION_FILTER_DELETE),
            page.getExpressionFilterTable(), EXPRESSION_FILTER_DELETE);
    }
}
