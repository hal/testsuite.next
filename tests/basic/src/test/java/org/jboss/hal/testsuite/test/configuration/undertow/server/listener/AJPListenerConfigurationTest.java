package org.jboss.hal.testsuite.test.configuration.undertow.server.listener;

import java.io.IOException;
import java.util.Collections;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.page.configuration.UndertowServerPage;
import org.jboss.hal.testsuite.test.configuration.io.IOFixtures;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.undertow.UndertowFixtures.serverAddress;

@RunWith(Arquillian.class)
public class AJPListenerConfigurationTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private UndertowServerPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String UNDERTOW_SERVER_TO_BE_TESTED =
        "undertow-server-to-be-tested-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String AJP_LISTENER_TO_BE_EDITED =
        "ajp-listener-to-be-added-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SOCKET_BINDING_TO_BE_EDITED =
        "socket-binding-to-be-added-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SOCKET_REDIRECT_TO_BE_EDITED =
        "socket-redirect-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SOCKET_BINDING =
        "socket-binding-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String BUFFER_POOL_TO_BE_EDITED = "buffer-pool-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String WORKER_TO_BE_EDITED = "worker-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        operations.add(IOFixtures.bufferPoolAddress(BUFFER_POOL_TO_BE_EDITED));
        operations.add(IOFixtures.workerAddress(WORKER_TO_BE_EDITED));
        operations.add(serverAddress(UNDERTOW_SERVER_TO_BE_TESTED));
        client.apply(new AddLocalSocketBinding(SOCKET_BINDING));
        client.apply(new AddLocalSocketBinding(SOCKET_BINDING_TO_BE_EDITED));
        client.apply(new AddLocalSocketBinding(SOCKET_REDIRECT_TO_BE_EDITED));
        operations.add(serverAddress(UNDERTOW_SERVER_TO_BE_TESTED).and("ajp-listener", AJP_LISTENER_TO_BE_EDITED),
            Values.of("socket-binding", SOCKET_BINDING.toLowerCase() + "ref"));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        operations.removeIfExists(serverAddress(UNDERTOW_SERVER_TO_BE_TESTED));
        operations.removeIfExists(IOFixtures.bufferPoolAddress(BUFFER_POOL_TO_BE_EDITED));
        operations.removeIfExists(IOFixtures.workerAddress(WORKER_TO_BE_EDITED));
        client.apply(new RemoveLocalSocketBinding(SOCKET_BINDING));
        client.apply(new RemoveLocalSocketBinding(SOCKET_BINDING_TO_BE_EDITED));
        client.apply(new RemoveLocalSocketBinding(SOCKET_REDIRECT_TO_BE_EDITED));
    }

    @Before
    public void initPage() {
        page.navigateAgain(NAME, UNDERTOW_SERVER_TO_BE_TESTED);
        console.verticalNavigation()
            .selectSecondary(Ids.UNDERTOW_SERVER_LISTENER_ITEM, Ids.build(Ids.UNDERTOW_SERVER_AJP_LISTENER, "item"));
        page.getAjpListenerTable().select(AJP_LISTENER_TO_BE_EDITED);
    }

    @Test
    public void toggleAllowEncodedSlash() throws Exception {
        boolean allowEncodedSlash = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "allow-encoded-slash").booleanValue();
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "allow-encoded-slash", !allowEncodedSlash);
    }

    @Test
    public void toggleAllowEqualsInCookieValue() throws Exception {
        boolean allowEqualsInCookieValue = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "allow-equals-in-cookie-value").booleanValue();
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "allow-equals-in-cookie-value", !allowEqualsInCookieValue);
    }

    @Test
    public void toggleAllowUnescapedCharactersInURL() throws Exception {
        boolean allowUnescapedCharactersInURL = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "allow-unescaped-characters-in-url").booleanValue();
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "allow-unescaped-characters-in-url", !allowUnescapedCharactersInURL);
    }

    @Test
    public void toggleAwaysSetKeepAlive() throws Exception {
        boolean alwaysSetKeepAlive = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "always-set-keep-alive").booleanValue();
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "always-set-keep-alive", !alwaysSetKeepAlive);
    }

    @Test
    public void toggleBufferPipelinedData() throws Exception {
        boolean bufferPipelinedData = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "buffer-pipelined-data").booleanValue();
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "buffer-pipelined-data", !bufferPipelinedData);
    }

    @Test
    public void editBufferPool() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "buffer-pool", BUFFER_POOL_TO_BE_EDITED);
    }

    @Test
    public void toggleDecodeURL() throws Exception {
        boolean decodeURL = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "decode-url").booleanValue();
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "decode-url", !decodeURL);
    }

    @Test
    public void editDisallowedMethods() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "disallowed-methods", Collections.singletonList("DISALLOWED-METHOD"));
    }

    @Test
    public void editMaxAJPPacketSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "max-ajp-packet-size", Random.number());
    }

    @Test
    public void editMaxBufferedRequestSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "max-buffered-request-size", Random.number());
    }

    @Test
    public void editMaxConnections() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "max-connections", Random.number());
    }

    @Test
    public void editMaxCookies() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "max-cookies", Random.number());
    }

    @Test
    public void editMaxHeaderSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "max-header-size", Random.number());
    }

    @Test
    public void editMaxHeaders() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "max-headers", Random.number());
    }

    @Test
    public void editMaxParameters() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "max-parameters", Random.number());
    }

    @Test
    public void editMaxPostSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "max-post-size", Long.valueOf(Random.number()));
    }

    @Test
    public void editNoRequestTimeout() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "no-request-timeout", Random.number());
    }

    @Test
    public void editReadTimeout() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "read-timeout", Random.number());
    }

    @Test
    public void editReceiveBuffer() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "receive-buffer", Random.number());
    }

    @Test
    public void toggleRecordRequestStartTime() throws Exception {
        boolean recordRequestStartTime = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "record-request-start-time").booleanValue();
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "record-request-start-time", !recordRequestStartTime);
    }

    @Test
    public void editRedirectSocket() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "redirect-socket", SOCKET_REDIRECT_TO_BE_EDITED.toLowerCase() + "ref");
    }

    @Test
    public void editRequestParseTimeout() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "request-parse-timeout", Random.number());
    }

    @Test
    public void toggleResolvePeerAddress() throws Exception {
        boolean resolvePeerAddress = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "resolve-peer-address").booleanValue();
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "resolve-peer-address", !resolvePeerAddress);
    }

    @Test
    public void toggleRfc6265CookieValidation() throws Exception {
        boolean rfc6265CookieValidation = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "rfc6265-cookie-validation").booleanValue();
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "rfc6265-cookie-validation", !rfc6265CookieValidation);
    }

    @Test
    public void editScheme() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "scheme");
    }

    @Test
    public void editSendBuffer() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "send-buffer", Random.number());
    }

    @Test
    public void editSocketBinding() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "socket-binding", SOCKET_BINDING_TO_BE_EDITED.toLowerCase() + "ref");
    }

    @Test
    public void editTCPBacklog() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "tcp-backlog", Random.number());
    }

    @Test
    public void toggleTCPKeepAlive() throws Exception {
        ModelNodeResult modelNodeResult = operations.readAttribute(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            "tcp-keep-alive");
        boolean tcpKeepAlive = modelNodeResult.booleanValue(false);
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "tcp-keep-alive", !tcpKeepAlive);
    }

    @Test
    public void editURLCharset() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "url-charset");
    }

    @Test
    public void editWorker() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "worker", WORKER_TO_BE_EDITED);
    }

    @Test
    public void editWriteTimeout() throws Exception {
        crudOperations.update(
            UndertowFixtures.ajpListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, AJP_LISTENER_TO_BE_EDITED),
            page.getAjpListenerForm(), "write-timeout", Random.number());
    }
}
