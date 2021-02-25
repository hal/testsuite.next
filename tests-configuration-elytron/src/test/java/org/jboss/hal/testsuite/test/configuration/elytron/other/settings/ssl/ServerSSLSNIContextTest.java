package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.ssl;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.fragment.TagsInputFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronOtherSettingsPage;
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
public class ServerSSLSNIContextTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String KEY_STORE = "key-store-" + Random.name();
    private static final String KEY_MANAGER = "key-manager-" + Random.name();

    private static final String SERVER_SSL_CONTEXT_CREATE = "server-ssl-context-to-create-" + Random.name();
    private static final String SERVER_SSL_CONTEXT_UPDATE = "server-ssl-context-to-update-" + Random.name();
    private static final String SERVER_SSL_SNI_CONTEXT_CREATE = "server-ssl-sni-context-to-create-" + Random.name();
    private static final String SERVER_SSL_SNI_CONTEXT_UPDATE = "server-ssl-sni-context-to-update-" + Random.name();
    private static final String SERVER_SSL_SNI_CONTEXT_DELETE = "server-ssl-sni-context-to-delete-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException {
        operations.add(ElytronFixtures.keyStoreAddress(KEY_STORE),
            Values.of("credential-reference", new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty(
                "clear-text", Random.name()).build()).and("type", "JKS")).assertSuccess();
        operations.add(ElytronFixtures.keyManagerAddress(KEY_MANAGER),
            Values.of("credential-reference", new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty(
                "clear-text", Random.name()).build()).and("key-store", KEY_STORE)).assertSuccess();
        createServerSSLContext(SERVER_SSL_CONTEXT_CREATE);
        createServerSSLContext(SERVER_SSL_CONTEXT_UPDATE);
        createServerSSLSNIContext(SERVER_SSL_SNI_CONTEXT_UPDATE, SERVER_SSL_CONTEXT_CREATE);
        createServerSSLSNIContext(SERVER_SSL_SNI_CONTEXT_DELETE, SERVER_SSL_CONTEXT_CREATE);
    }

    private static void createServerSSLContext(String name) throws IOException {
        operations.add(ElytronFixtures.serverSslContextAddress(name), Values.of("key-manager", KEY_MANAGER))
            .assertSuccess();
    }

    private static void createServerSSLSNIContext(String name, String serverSSLContext) throws IOException {
        operations.add(ElytronFixtures.serverSSLSNIContextAddress(name),
            Values.of("default-ssl-context", serverSSLContext)
                .andObject(ElytronFixtures.HOST_CONTEXT_MAP, Values.of(Random.name(), serverSSLContext))).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(ElytronFixtures.serverSSLSNIContextAddress(SERVER_SSL_SNI_CONTEXT_CREATE));
            operations.removeIfExists(ElytronFixtures.serverSSLSNIContextAddress(SERVER_SSL_SNI_CONTEXT_DELETE));
            operations.removeIfExists(ElytronFixtures.serverSSLSNIContextAddress(SERVER_SSL_SNI_CONTEXT_UPDATE));
            operations.removeIfExists(ElytronFixtures.serverSslContextAddress(SERVER_SSL_CONTEXT_CREATE));
            operations.removeIfExists(ElytronFixtures.serverSslContextAddress(SERVER_SSL_CONTEXT_UPDATE));
            operations.removeIfExists(ElytronFixtures.keyManagerAddress(KEY_MANAGER));
            operations.removeIfExists(ElytronFixtures.keyStoreAddress(KEY_STORE));
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Page
    private ElytronOtherSettingsPage page;

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Before
    public void before() {
        page.navigate();
        console.verticalNavigation().selectSecondary(ElytronFixtures.SSL_ITEM, "elytron-server-ssl-sni-context-item");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(ElytronFixtures.serverSSLSNIContextAddress(SERVER_SSL_SNI_CONTEXT_CREATE),
            page.getServerSSLSNIContextTable(), formFragment -> {
                formFragment.text("name", SERVER_SSL_SNI_CONTEXT_CREATE);
                formFragment.text("default-ssl-context", SERVER_SSL_CONTEXT_CREATE);
                formFragment.properties(ElytronFixtures.HOST_CONTEXT_MAP).add(Random.name(), SERVER_SSL_CONTEXT_CREATE);
            });
    }

    @Test
    public void delete() throws Exception {
        crudOperations.delete(ElytronFixtures.serverSSLSNIContextAddress(SERVER_SSL_SNI_CONTEXT_DELETE),
            page.getServerSSLSNIContextTable(), SERVER_SSL_SNI_CONTEXT_DELETE);
    }

    @Test
    public void editDefaultSSLContext() throws Exception {
        page.getServerSSLSNIContextTable().select(SERVER_SSL_SNI_CONTEXT_UPDATE);
        crudOperations.update(ElytronFixtures.serverSSLSNIContextAddress(SERVER_SSL_SNI_CONTEXT_UPDATE),
            page.getServerSSLSNIContextForm(), "default-ssl-context", SERVER_SSL_CONTEXT_UPDATE);
    }

    @Test
    public void editHostContextMap() throws Exception {
        String key = Random.name();
        page.getServerSSLSNIContextTable().select(SERVER_SSL_SNI_CONTEXT_UPDATE);
        crudOperations.update(ElytronFixtures.serverSSLSNIContextAddress(SERVER_SSL_SNI_CONTEXT_UPDATE),
            page.getServerSSLSNIContextForm(), formFragment -> {
                TagsInputFragment hostContextMap = formFragment.properties(ElytronFixtures.HOST_CONTEXT_MAP);
                hostContextMap.removeTags();
                hostContextMap.add(key, SERVER_SSL_CONTEXT_UPDATE);
            }, resourceVerifier -> resourceVerifier.verifyAttribute(ElytronFixtures.HOST_CONTEXT_MAP,
                new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty(key, SERVER_SSL_CONTEXT_UPDATE).build()));
    }
}
