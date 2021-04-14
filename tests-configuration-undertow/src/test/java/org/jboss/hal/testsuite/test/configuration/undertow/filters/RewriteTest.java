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
public class RewriteTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowFiltersPage page;

    private static final String REWRITE_CREATE = "rewrite-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String REWRITE_DELETE = "rewrite-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7);

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFiltersFixtures.rewriteAddress(REWRITE_DELETE), Values.of("target", Random.number()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFiltersFixtures.rewriteAddress(REWRITE_CREATE));
        operations.removeIfExists(UndertowFiltersFixtures.rewriteAddress(REWRITE_DELETE));
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-rewrite","item"));
    }

    @Test
    public void create() throws Exception {
        String target = Random.name();
        crudOperations.create(UndertowFiltersFixtures.rewriteAddress(REWRITE_CREATE), page.getRewriteTable(), formFragment -> {
            formFragment.text("name", REWRITE_CREATE);
            formFragment.text("target", target);
        }, resourceVerifier -> {
            resourceVerifier.verifyExists();
            resourceVerifier.verifyAttribute("target", target);
        });
    }

    @Test
    public void delete() throws Exception {
        crudOperations.delete(UndertowFiltersFixtures.rewriteAddress(REWRITE_DELETE), page.getRewriteTable(), REWRITE_DELETE);
    }

}
