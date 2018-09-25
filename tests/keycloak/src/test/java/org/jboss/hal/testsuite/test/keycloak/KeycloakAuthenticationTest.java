package org.jboss.hal.testsuite.test.keycloak;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.HomePage;
import org.jboss.hal.testsuite.page.Places;
import org.jboss.hal.testsuite.page.keycloak.KeycloakAccessControlPage;
import org.jboss.hal.testsuite.page.keycloak.KeycloakLoginPage;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.SslOptions;

import static org.jboss.hal.testsuite.test.keycloak.KeycloakOperations.*;
import static org.junit.Assert.*;

/**
 * Requires {@code ssl.truststore.file} system property to be passed.
 */
@RunWith(Arquillian.class)
public class KeycloakAuthenticationTest {

    private static final OnlineManagementClient
        wildflyClient = ManagementClientProvider.createOnlineManagementClient(),
        wildflySslClient,
        keycloakClient = ManagementClientProvider.standaloneWithPort(Integer.valueOf(ConfigUtils.get("keycloak.port", "10090")));
    private static final KeycloakOperations keycloakOps = new KeycloakOperations(keycloakClient);
    private static final KeycloakWildflyOperations
        keycloakWildflyOps = new KeycloakWildflyOperations(wildflyClient),
        keycloakWildflySslOps;

    static {
        try {
            wildflySslClient = KeycloakWildflyOperations.createSslClient(new SslOptions.Builder()
                    .trustStore(new File(ConfigUtils.get("ssl.truststore.file")))
                    .trustStorePassword("secure")
                    .build());
            keycloakWildflySslOps = new KeycloakWildflyOperations(wildflySslClient);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String realmPubKey;

    @Page private HomePage halHomePage;
    @Page private KeycloakLoginPage keycloakLoginPage;
    @Page private KeycloakAccessControlPage accessControlPage;

    @Inject
    protected Console console;

    @BeforeClass
    public static void setupServers() throws IOException, InterruptedException, java.util.concurrent.TimeoutException {
        realmPubKey = keycloakOps
            .enableSsl()
            .createRealm()
            .setupConsoleClient()
            .setupManagementClient()
            .setupConsoleUser()
            .getPubKey();
        keycloakWildflyOps.secureManagementWithKeycloak(realmPubKey);
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        wildflyClient.close();
        wildflySslClient.close();
        keycloakClient.close();
    }

    @Test
    public void loginAndLogoutViaHttp() throws Exception {
        login(homePage -> homePage.navigate());
        logout();
    }

    @Test
    public void loginAndLogoutViaHttps() throws Exception {
        try {
            keycloakWildflyOps.enableSslForManagement();
            keycloakWildflySslOps.waitUntilServerIsRunning();
            login(homePage -> homePage.navigateViaHttps());
            logout();
        } finally {
            keycloakWildflySslOps.disableSslForManagement();
            keycloakWildflyOps.waitUntilServerIsRunning();
        }
    }

    private void login(Consumer<HomePage> navigateAction) {
        try {
            navigateAction.accept(halHomePage);
            fail("User should not be authenticated yet.");
        } catch (TimeoutException e) {
            // expected
        }

        // browser should be redirected to Keycloak login page
        try {
            keycloakLoginPage
                .waitForLoginForm()
                .login(ADMIN_NAME, ADMIN_PASSWORD);
        } catch (NoSuchElementException e) {
            fail("Unpossible to login to Keycloak. Browser probably not redirected to Keycloak. " + e.getMessage());
        }

        // browser should be redirected back to HAL
        try {
            halHomePage.waitUntilHomePageIsLoaded();
        } catch (TimeoutException e) {
            fail("Browser should be redirected back to HAL after login to Keycloak. " + e.getMessage());
        }
    }

    private void logout() {
        halHomePage.logout();
        // logout should redirect browser back to Keycloak
        keycloakLoginPage.waitForLoginForm();
    }

    @Test
    public void accessControlPage() throws Exception {
        login(homePage -> homePage.navigate());
        accessControlPage.navigateToAccessControlSso();
        console.verify(Places.finderPlace(NameTokens.ACCESS_CONTROL_SSO, null));

        assertEquals(ADMIN_NAME, accessControlPage.getFormText("username"));
        assertEquals("http://localhost:8180/auth", accessControlPage.getFormLink("keycloak-server-url"));
        assertTrue(accessControlPage.getFormLink("account-url").startsWith("http://localhost:8180/auth/realms/" + WILDFLY_INFRA
                + "/account?referrer=" + WILDFLY_CONSOLE));
        assertEquals(WILDFLY_INFRA, accessControlPage.getFormText("realm"));
        assertEquals(realmPubKey, accessControlPage.getFormText("realm-public-key"));
    }
}
