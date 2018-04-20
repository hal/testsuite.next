package org.jboss.hal.testsuite.test.configuration.undertow.servlet.container.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

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
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowServletContainerFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class AttributesTest {

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
    public void toggleAllowNonStandardWrappers() throws Exception {
        boolean allowNonStandardWrappers =
            operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
                "allow-non-standard-wrappers").booleanValue();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "allow-non-standard-wrappers", !allowNonStandardWrappers);
    }

    @Test
    public void editDefaultBufferCache() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "default-buffer-cache");
    }

    @Test
    public void editDefaultCookieVersion() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "default-cookie-version", Random.number(0, 2));
    }

    @Test
    public void editDefaultEncoding() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "default-encoding");
    }

    @Test
    public void editDefaultSessionTimeout() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "default-session-timeout", Random.number());
    }

    @Test
    public void toggleDirectoryListing() throws Exception {
        boolean directoryListing =
            operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
                "directory-listing").booleanValue();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "directory-listing", !directoryListing);
    }

    @Test
    public void toggleDisableCachingForSecuredPages() throws Exception {
        boolean disableCachingForSecuredPages =
            operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
                "disable-caching-for-secured-pages").booleanValue();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "disable-caching-for-secured-pages", !disableCachingForSecuredPages);
    }

    @Test
    public void toggleDisableFileWatchService() throws Exception {
        boolean disableFileWatchService =
            operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
                "disable-file-watch-service").booleanValue();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "disable-file-watch-service", !disableFileWatchService);
    }

    @Test
    public void toggleDisableSessionIdReuse() throws Exception {
        boolean disableSessionIdReuse =
            operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
                "disable-session-id-reuse").booleanValue();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "disable-session-id-reuse", !disableSessionIdReuse);
    }

    @Test
    public void toggleEagerFilterInitialization() throws Exception {
        boolean eagerFilterInitialization =
            operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
                "eager-filter-initialization").booleanValue();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "eager-filter-initialization", !eagerFilterInitialization);
    }

    @Test
    public void editFileCacheMaxFileSize() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "file-cache-max-file-size", Random.number());
    }

    @Test
    public void editFileCacheMetadataSize() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "file-cache-metadata-size", Random.number());
    }

    @Test
    public void editFileCacheTimeToAlive() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "file-cache-time-to-live", Random.number());
    }

    @Test
    public void toggleIgnoreFlush() throws Exception {
        boolean ignoreFlush =
            operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
                "ignore-flush").booleanValue();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "ignore-flush", !ignoreFlush);
    }

    @Test
    public void editMaxSessions() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "max-sessions", Random.number());
    }

    @Test
    public void toggleProactiveAuthentication() throws Exception {
        boolean proactiveAuthentication =
            operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
                "proactive-authentication").booleanValue();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "proactive-authentication", !proactiveAuthentication);
    }

    @Test
    public void editSessionIdLength() throws Exception {
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "session-id-length", Random.number(16, 201));
    }

    @Test
    public void editStackTraceOnError() throws Exception {
        String[] validValues = {"local-only", "none", "all"};
        String originalValue = operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
            UndertowServletContainerFixtures.STACK_TRACE_ON_ERROR).stringValue();
        String newValue = Arrays.stream(validValues)
            .filter(Predicate.isEqual(originalValue).negate())
            .findAny().get();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            form -> form.select(UndertowServletContainerFixtures.STACK_TRACE_ON_ERROR, newValue),
            resourceVerifier -> resourceVerifier.verifyAttribute(UndertowServletContainerFixtures.STACK_TRACE_ON_ERROR,
            newValue));
    }

    @Test
    public void toggleUseListenerEncoding() throws Exception {
        boolean useListenerEncoding =
            operations.readAttribute(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT),
                "use-listener-encoding").booleanValue();
        crudOperations.update(UndertowFixtures.servletContainerAddress(SERVLET_CONTAINER_EDIT), page.getAttributesForm(),
            "use-listener-encoding", !useListenerEncoding);
    }
}
