package org.jboss.hal.testsuite.test.configuration.undertow.filters;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.UndertowFiltersPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.HEADER_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.undertow.UndertowFiltersFixtures.HEADER_VALUE;
import static org.jboss.hal.testsuite.test.configuration.undertow.UndertowFiltersFixtures.responseHeaderAddress;

@RunWith(Arquillian.class)
public class ResponseHeaderTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private UndertowFiltersPage page;

    private static final String RESPONSE_HEADER_CREATE = "Accept-Patch";
    private static final String RESPONSE_HEADER_DELETE = "Allow";

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(responseHeaderAddress(RESPONSE_HEADER_DELETE),
                Values.of(HEADER_NAME, RESPONSE_HEADER_DELETE).and(HEADER_VALUE, Random.name()));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(responseHeaderAddress(RESPONSE_HEADER_CREATE));
        operations.removeIfExists(responseHeaderAddress(RESPONSE_HEADER_DELETE));
    }

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation()
            .selectPrimary(Ids.build("undertow-response-header", "item"));
    }

    @Test
    public void create() throws Exception {
        String headerValue = Random.name();
        crudOperations.create(responseHeaderAddress(RESPONSE_HEADER_CREATE),
                page.getResponseHeaderTable(),
                formFragment -> {
                    formFragment.text(NAME, RESPONSE_HEADER_CREATE);
                    formFragment.text(HEADER_NAME, RESPONSE_HEADER_CREATE);
                    formFragment.text(HEADER_VALUE, headerValue);
                }, resourceVerifier -> {
                    resourceVerifier.verifyExists();
                    resourceVerifier.verifyAttribute(HEADER_NAME, RESPONSE_HEADER_CREATE);
                    resourceVerifier.verifyAttribute(HEADER_VALUE, headerValue);
                });
    }

    @Test
    public void delete() throws Exception {
        crudOperations.delete(responseHeaderAddress(RESPONSE_HEADER_DELETE), page.getResponseHeaderTable(),
                RESPONSE_HEADER_DELETE);
    }
}
