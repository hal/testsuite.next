package org.jboss.hal.testsuite.test.configuration.undertow.server.listener;

import java.io.IOException;
import java.util.Collections;

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
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.page.configuration.UndertowServerPage;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.jboss.hal.testsuite.test.configuration.io.IOFixtures;
import org.jboss.hal.testsuite.test.configuration.undertow.UndertowFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.ReadAttributeOption;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class HTTPSListenerConfigurationTest {

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Drone
    private WebDriver browser;

    @Page
    private UndertowServerPage page;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String UNDERTOW_SERVER_TO_BE_TESTED =
        "undertow-server-to-be-tested-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String HTTPS_LISTENER_TO_BE_EDITED =
        "https-listener-to-be-added-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String CLIENT_SSL_CONTEXT = "client-ssl-context-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String CLIENT_SSL_CONTEXT_TO_BE_EDITED = "client-ssl-context-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);


    private static final String SOCKET_BINDING_TO_BE_EDITED =
        "socket-binding-to-be-added-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SOCKET_REDIRECT_TO_BE_EDITED =
        "socket-redirect-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String SOCKET_BINDING =
        "socket-binding-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String BUFFER_POOL_TO_BE_EDITED =
        "buffer-pool-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    private static final String WORKER_TO_BE_EDITED = "worker-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        operations.add(IOFixtures.bufferPoolAddress(BUFFER_POOL_TO_BE_EDITED));
        operations.add(IOFixtures.workerAddress(WORKER_TO_BE_EDITED));
        operations.add(ElytronFixtures.clientSslContextAddress(CLIENT_SSL_CONTEXT));
        operations.add(ElytronFixtures.clientSslContextAddress(CLIENT_SSL_CONTEXT_TO_BE_EDITED));
        operations.add(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED));
        client.apply(new AddLocalSocketBinding(SOCKET_BINDING));
        client.apply(new AddLocalSocketBinding(SOCKET_BINDING_TO_BE_EDITED));
        client.apply(new AddLocalSocketBinding(SOCKET_REDIRECT_TO_BE_EDITED));
        operations.add(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            Values
                .of("socket-binding", SOCKET_BINDING.toLowerCase() + "ref")
                .and("ssl-context", CLIENT_SSL_CONTEXT));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        operations.removeIfExists(UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED));
        operations.removeIfExists(IOFixtures.bufferPoolAddress(BUFFER_POOL_TO_BE_EDITED));
        operations.removeIfExists(IOFixtures.workerAddress(WORKER_TO_BE_EDITED));
        client.apply(new RemoveLocalSocketBinding(SOCKET_BINDING));
        client.apply(new RemoveLocalSocketBinding(SOCKET_BINDING_TO_BE_EDITED));
        client.apply(new RemoveLocalSocketBinding(SOCKET_REDIRECT_TO_BE_EDITED));
        operations.removeIfExists(ElytronFixtures.clientSslContextAddress(CLIENT_SSL_CONTEXT));
        operations.removeIfExists(ElytronFixtures.clientSslContextAddress(CLIENT_SSL_CONTEXT_TO_BE_EDITED));
    }

    @Before
    public void initPage() {
        page.navigate("name", UNDERTOW_SERVER_TO_BE_TESTED);
        console.verticalNavigation()
            .selectSecondary(Ids.UNDERTOW_SERVER_LISTENER_ITEM, Ids.build(Ids.UNDERTOW_SERVER_HTTPS_LISTENER, "item"));
        page.getHttpsListenerTable().select(HTTPS_LISTENER_TO_BE_EDITED);
    }

    @Test
    public void toggleAllowEncodedSlash() throws Exception {
        boolean allowEncodedSlash = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "allow-encoded-slash").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "allow-encoded-slash", !allowEncodedSlash);
    }

    @Test
    public void toggleAllowEqualsInCookieValue() throws Exception {
        boolean allowEqualsInCookieValue = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "allow-equals-in-cookie-value").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "allow-equals-in-cookie-value", !allowEqualsInCookieValue);
    }

    @Test
    public void toggleAllowUnescapedCharactersInURL() throws Exception {
        boolean allowUnescapedCharactersInURL = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "allow-unescaped-characters-in-url").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "allow-unescaped-characters-in-url", !allowUnescapedCharactersInURL);
    }

    @Test
    public void toggleAwaysSetKeepAlive() throws Exception {
        boolean alwaysSetKeepAlive = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "always-set-keep-alive").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "always-set-keep-alive", !alwaysSetKeepAlive);
    }

    @Test
    public void toggleBufferPipelinedData() throws Exception {
        boolean bufferPipelinedData = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "buffer-pipelined-data").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "buffer-pipelined-data", !bufferPipelinedData);
    }

    @Test
    public void editBufferPool() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "buffer-pool", BUFFER_POOL_TO_BE_EDITED);
    }

    @Test
    public void toggleCertificateForwarding() throws Exception {
        boolean certificateForwarding = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "certificate-forwarding").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "certificate-forwarding", !certificateForwarding);
    }

    @Test
    public void toggleDecodeURL() throws Exception {
        boolean decodeURL = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "decode-url").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "decode-url", !decodeURL);
    }

    @Test
    public void editDisallowedMethods() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "disallowed-methods", Collections.singletonList("DISALLOWED-METHOD"));
    }

    @Test
    public void toggleCEnableHTTP2() throws Exception {
        boolean enableHTTP2 = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "enable-http2").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "enable-http2", !enableHTTP2);
    }

    @Test
    public void toggleHTTP2EnablePush() throws Exception {
        boolean enableHTTP2 = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "http2-enable-push").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "http2-enable-push", !enableHTTP2);
    }

    @Test
    public void editHTTP2HeaderTableSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "http2-header-table-size", Random.number());
    }

    @Test
    public void editHTTP2InitialWindowSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "http2-initial-window-size", Random.number());
    }

    @Test
    public void editHTTP2MaxConcurrentStreams() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "http2-max-concurrent-streams", Random.number());
    }

    @Test
    public void editHTTP2MaxFrameSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "http2-max-frame-size", Random.number());
    }

    @Test
    public void editHTTP2MaxHeaderListSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "http2-max-header-list-size", Random.number());
    }

    @Test
    public void editMaxBufferedRequestSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "max-buffered-request-size", Random.number());
    }

    @Test
    public void editMaxConnections() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "max-connections", Random.number());
    }

    @Test
    public void editMaxCookies() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "max-cookies", Random.number());
    }

    @Test
    public void editMaxHeaderSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "max-header-size", Random.number());
    }

    @Test
    public void editMaxHeaders() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "max-headers", Random.number());
    }

    @Test
    public void editMaxParameters() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "max-parameters", Random.number());
    }

    @Test
    public void editMaxPostSize() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "max-post-size", Long.valueOf(Random.number()));
    }

    @Test
    public void editNoRequestTimeout() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "no-request-timeout", Random.number());
    }

    @Test
    public void toggleProxyAddressForwarding() throws Exception {
        boolean proxyAddressForwarding = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "proxy-address-forwarding").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "proxy-address-forwarding", !proxyAddressForwarding);
    }

    @Test
    public void editReadTimeout() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "read-timeout", Random.number());
    }

    @Test
    public void editReceiveBuffer() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "receive-buffer");
    }

    @Test
    public void toggleRecordRequestStartTime() throws Exception {
        boolean recordRequestStartTime = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "record-request-start-time").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "record-request-start-time", !recordRequestStartTime);
    }


    @Test
    public void editRequestParseTimeout() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "request-parse-timeout", Random.number());
    }

    @Test
    public void toggleRequireHostHttp11() throws Exception {
        boolean requireHostHttp11 = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "require-host-http11").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "require-host-http11", !requireHostHttp11);
    }

    @Test
    public void toggleResolvePeerAddress() throws Exception {
        boolean resolvePeerAddress = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "resolve-peer-address").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "resolve-peer-address", !resolvePeerAddress);
    }

    @Test
    public void toggleRfc6265CookieValidation() throws Exception {
        boolean rfc6265CookieValidation = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "rfc6265-cookie-validation").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "rfc6265-cookie-validation", !rfc6265CookieValidation);
    }

    @Test
    public void toggleSecure() throws Exception {
        boolean secure = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "secure").booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "secure", !secure);
    }

    @Test
    public void editSendBuffer() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "send-buffer");
    }

    @Test
    public void editSocketBinding() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "socket-binding", SOCKET_BINDING_TO_BE_EDITED.toLowerCase() + "ref");
    }

    @Test
    public void editSSLContext() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "ssl-context", CLIENT_SSL_CONTEXT_TO_BE_EDITED);
    }

    @Test
    public void editTCPBacklog() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "tcp-backlog", Random.number());
    }

    @Test
    public void toggleTCPKeepAlive() throws Exception {
        ModelNodeResult modelNodeResult = operations.readAttribute(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            "tcp-keep-alive", ReadAttributeOption.NOT_INCLUDE_DEFAULTS);
        boolean tcpKeepAlive = modelNodeResult.booleanValue();
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "tcp-keep-alive", !tcpKeepAlive);
    }

    @Test
    public void editURLCharset() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "url-charset");
    }

    @Test
    public void editWorker() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "worker", WORKER_TO_BE_EDITED);
    }

    @Test
    public void editWriteTimeout() throws Exception {
        crudOperations.update(
            UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_EDITED),
            page.getHttpsListenerForm(), "write-timeout", Random.number());
    }
}
