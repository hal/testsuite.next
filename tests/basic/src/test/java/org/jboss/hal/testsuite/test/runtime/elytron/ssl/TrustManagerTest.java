package org.jboss.hal.testsuite.test.runtime.elytron.ssl;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.runtime.elytron.ElytronRuntimeSSLPage;
import org.jboss.hal.testsuite.util.audit.log.AuditLog;
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

import static org.jboss.as.domain.management.ModelDescriptionConstants.JKS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.keyStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.trustManagerAddress;

@RunWith(Arquillian.class)
public class TrustManagerTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String KEY_STORE = "key-store-" + Random.name();
    private static final String TRUST_MANAGER = "trust-manager-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException {
        createKeyStore(KEY_STORE);
        operations.add(trustManagerAddress(TRUST_MANAGER), Values.of("key-store", KEY_STORE)).assertSuccess();
    }

    private static void createKeyStore(String name) throws IOException {
        ModelNode credentialReference = new ModelNode();
        credentialReference.get(CLEAR_TEXT).set(Random.name());
        operations.add(keyStoreAddress(name),
            Values.of(ModelDescriptionConstants.TYPE, JKS).and(CREDENTIAL_REFERENCE, credentialReference)
        .and(ModelDescriptionConstants.PATH, Random.name()).and(ModelDescriptionConstants.RELATIVE_TO, "jboss.home.dir"))
            .assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(trustManagerAddress(TRUST_MANAGER));
            operations.removeIfExists(keyStoreAddress(KEY_STORE));
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Page
    private ElytronRuntimeSSLPage page;

    private AuditLog auditLog;

    @Before
    public void initPage() throws IOException {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.ELYTRON_TRUST_MANAGER);
    }

    @Test
    public void init() {
        page.getTrustManagerTable().select(TRUST_MANAGER);
        page.getTrustManagerTable().button("Initialize").click();
        console.verifySuccess();
    }
}
