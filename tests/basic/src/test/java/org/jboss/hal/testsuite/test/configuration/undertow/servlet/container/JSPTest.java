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
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.UndertowServletContainerPage;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFixtures;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;

@RunWith(Arquillian.class)
public class JSPTest {

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

    private static final String SERVLET_CONTAINER_JSP_CREATE =
        "servlet-container-without-jsp-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SERVLET_CONTAINER_JSP_REMOVE =
        "servlet-container-with-jsp-" + RandomStringUtils.randomAlphanumeric(7);

    private static final Address SERVLET_CONTAINER_EDIT_JSP_ADDRESS = jspAddress(SERVLET_CONTAINER_EDIT);

    private static Address jspAddress(String servletContainer) {
        return UndertowFixtures.servletContainerAddress(servletContainer).and("setting","jsp");
    }

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
        operations.add(SERVLET_CONTAINER_EDIT_JSP_ADDRESS);
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_JSP_CREATE));
        operations.add(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_JSP_REMOVE));
        operations.add(jspAddress(SERVLET_CONTAINER_JSP_REMOVE));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_JSP_CREATE));
        operations.removeIfExists(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_JSP_REMOVE));
    }

    @Test
    public void create() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_JSP_CREATE);
        crudOperations.createSingleton(jspAddress(SERVLET_CONTAINER_JSP_CREATE), page.getJspForm());
    }

    private void navigateToJSPForm(String servletContainerName) {
        page.navigateAgain(NAME, servletContainerName);
        // necessary to call 2 times as the first navigation doesn't open the view with the correct name parameter
        page.navigateAgain(NAME, servletContainerName);
        console.verticalNavigation()
            .selectPrimary(Ids.build(Ids.UNDERTOW_SERVLET_CONTAINER_JSP, "item"));
    }

    @Test
    public void remove() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_JSP_REMOVE);
        crudOperations.deleteSingleton(jspAddress(SERVLET_CONTAINER_JSP_REMOVE), page.getJspForm());
    }

    @Test
    public void editCheckInterval() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS,
            page.getJspForm(),
            "check-interval", Random.number());
    }

    @Test
    public void toggleDevelopment() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean development = operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "development").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "development", !development);
    }

    @Test
    public void toggleDisabled() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean disabled = operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "disabled").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "disabled", !disabled);
    }

    @Test
    public void toggleDisplaySourceFragment() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean displaySourceFragment =
            operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "display-source-fragment").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "display-source-fragment",
            !displaySourceFragment);
    }

    @Test
    public void toggleDumpSmap() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean dumpSmap = operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "dump-smap").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "dump-smap", !dumpSmap);
    }

    @Test
    public void toggleErrorOnUseBeanInvalidClassAttribute() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean errorOnUseBeanInvalidClassAttribute =
            operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "error-on-use-bean-invalid-class-attribute")
                .booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(),
            "error-on-use-bean-invalid-class-attribute",
            !errorOnUseBeanInvalidClassAttribute);
    }

    @Test
    public void toggleGenerateStringsAsCharArrays() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean generateStringsAsCharArrays =
            operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "generate-strings-as-char-arrays")
                .booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "generate-strings-as-char-arrays",
            !generateStringsAsCharArrays);
    }

    @Test
    public void editJavaEncoding() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "java-encoding");
    }

    @Test
    public void toggleKeepGenerated() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean keepGenerated =
            operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "keep-generated").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "keep-generated", !keepGenerated);
    }

    @Test
    public void toggleMappedFile() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean mappedFile = operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "mapped-file").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "mapped-file", !mappedFile);
    }

    @Test
    public void editModificationTestInterval() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "modification-test-interval",
            Random.number());
    }

    @Test
    public void toggleOptimizeScriptlets() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean optimizeScriptlets =
            operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "optimize-scriptlets").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "optimize-scriptlets",
            !optimizeScriptlets);
    }

    @Test
    public void toggleRecompileOnFail() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean recompileOnFail =
            operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "recompile-on-fail").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "recompile-on-fail",
            !recompileOnFail);
    }

    @Test
    public void editScratchDir() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "scratch-dir");
    }

    @Test
    public void toggleSmap() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean smap = operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "smap").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "smap", !smap);
    }

    @Test
    public void editSourceVM() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "source-vm");
    }

    @Test
    public void toggleTagPooling() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean tagPooling = operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "tag-pooling").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "tag-pooling", !tagPooling);
    }

    @Test
    public void editTargetVM() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "target-vm");
    }

    @Test
    public void toggleTrimSpaces() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean trimSpaces = operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "trim-spaces").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "trim-spaces", !trimSpaces);
    }

    @Test
    public void toggleXPoweredBy() throws Exception {
        navigateToJSPForm(SERVLET_CONTAINER_EDIT);
        boolean xPoweredBy = operations.readAttribute(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, "x-powered-by").booleanValue();
        crudOperations.update(SERVLET_CONTAINER_EDIT_JSP_ADDRESS, page.getJspForm(), "x-powered-by", !xPoweredBy);
    }
}
