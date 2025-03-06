package org.jboss.hal.testsuite.test.configuration.undertow.server.listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.category.RequiresLetsEncrypt;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.AddKeyManager;
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
import org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.ssl.EnableSslWizard;
import org.jboss.hal.testsuite.page.configuration.UndertowServerPage;
import org.jboss.hal.testsuite.tooling.ssl.SslOperations;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.foundation.online.SnapshotBackup;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CERTIFICATE_AUTHORITY_ACCOUNT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_MANAGER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_STORE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.READ_ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER_SSL_CONTEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SSL_CONTEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TRUST_MANAGER;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KEY_MAN_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SRV_SSL_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.keyManagerAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.keyStoreAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.serverSslContextAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.trustManagerAddress;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.CERT;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.CLIENT_CERTIFICATE_ALIAS;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.CLIENT_CERTIFICATE_PATH;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.CLIENT_CERTIFICATE_VALIDATE;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.ENABLED_CIPHER_SUITES;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.ENABLED_PROTOCOLS;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.HAL;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.ISSUER;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.KEY_DN_ORGANIZATION;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.KEY_STORE_NAME;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.KEY_STORE_PASSWORD;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.KEY_STORE_PATH;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.KEY_STORE_RELATIVE_TO;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.NOT_REQUESTED;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.PASS;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.SSL_SESSION_CACHE_SIZE;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.SSL_SESSION_TIMEOUT;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.SUBJECT;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.TRUST_STORE;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.TRUST_STORE_NAME;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.TRUST_STORE_PASSWORD;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.TRUST_STORE_PATH;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.VERIFY_CLIENT;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY;
import static org.jboss.hal.testsuite.tooling.ssl.KeyEntryType.PRIVATE_KEY_ENTRY;
import static org.jboss.hal.testsuite.tooling.ssl.KeyEntryType.TRUSTED_CERTIFICATE_ENTRY;

@RunWith(Arquillian.class)
public class HTTPSListenerSSLTest {

    private static final List<String> FILE_NAMES_TO_BE_DELETED = new ArrayList<>();
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations ops = new Operations(client);
    private static final SslOperations sslOps = new SslOperations(client).filesToBeCleanedUp(FILE_NAMES_TO_BE_DELETED);
    private static final String
        UNDERTOW_SERVER_TO_BE_TESTED = Ids.build("undertow-server-to-be-tested", Random.name()),
        HTTPS_LISTENER_TO_BE_TESTED = Ids.build("https-listener-to-be-tested", Random.name()),
        INIT_SOCKET_BINDING_NAME = Ids.build("socket-binding", Random.name()),
        DOMAIN_NAME = "www.foobar.com";

    private static final Address
        UNDERTOW_SERVER_ADRESS = UndertowFixtures.serverAddress(UNDERTOW_SERVER_TO_BE_TESTED),
        HTTPS_LISTENER_ADDRESS = UndertowFixtures.httpsListenerAddress(UNDERTOW_SERVER_TO_BE_TESTED, HTTPS_LISTENER_TO_BE_TESTED);

    private static final SnapshotBackup snapshot = new SnapshotBackup();

    @Inject
    private Console console;

    @Page
    private UndertowServerPage page;

    @BeforeClass
    public static void setUp() throws IOException, CommandFailedException {
        client.apply(snapshot.backup());

        ops.add(UNDERTOW_SERVER_ADRESS).assertSuccess();

        client.apply(new AddKeyManager(KEY_MAN_CREATE));
        ops.add(serverSslContextAddress(SRV_SSL_CREATE), Values.of(KEY_MANAGER, KEY_MAN_CREATE)).assertSuccess();

        client.apply(new AddLocalSocketBinding(INIT_SOCKET_BINDING_NAME));
        ops.add(HTTPS_LISTENER_ADDRESS, Values.of(SOCKET_BINDING, INIT_SOCKET_BINDING_NAME.toLowerCase() + "ref")
                .and(SSL_CONTEXT, SRV_SSL_CREATE)
        ).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            client.apply(snapshot.restore());
            File keyStoresDir = sslOps.getKeyStoresDirectory();
            FILE_NAMES_TO_BE_DELETED.forEach(fileName -> FileUtils.deleteQuietly(new File(keyStoresDir, fileName)));
        } finally {
            client.close();
        }
    }

    @Before
    public void initPage() {
        page.navigate("name", UNDERTOW_SERVER_TO_BE_TESTED);
        console.verticalNavigation()
            .selectSecondary(Ids.UNDERTOW_SERVER_LISTENER_ITEM, Ids.build(Ids.UNDERTOW_SERVER_HTTPS_LISTENER, "item"));
        page.getHttpsListenerTable().select(HTTPS_LISTENER_TO_BE_TESTED);
    }

    /**
     * Testing configuration of https listener with
     * <ul>
     *  <li>just one-way server authentication</li>
     *  <li>server certificate key store to be created</li>
     *  <li>server self-signed certificate to be generated</li>
     * </ul>
     */
    @Test
    public void enableOneWayGenerated() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyStorePathValue = Ids.build(KEY_STORE, PATH, Random.name()),
            keyAliasValue = Ids.build(KEY_ALIAS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name());
        FILE_NAMES_TO_BE_DELETED.add(keyStorePathValue);
        EnableSslWizard wizard = page.enableSslWizard()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY)
            .disableMutualAuthentication()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY)
            .createAllResources()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        configForm.editTextFiringExtraChangeEvent(KEY_DN_ORGANIZATION, HAL);
        wizard.next();

        configForm
            .expectError(KEY_STORE_NAME)
            .expectError(KEY_STORE_PASSWORD)
            .expectError(KEY_STORE_PATH)
            .expectError(KEY_ALIAS)
            .expectError(KEY_MANAGER)
            .expectError(SERVER_SSL_CONTEXT);

        configForm
            .editTextFiringExtraChangeEvent(KEY_STORE_NAME, keyStoreNameValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PASSWORD, keyStorePasswordValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PATH, keyStorePathValue)
            .editTextFiringExtraChangeEvent(KEY_ALIAS, keyAliasValue)
            .editTextFiringExtraChangeEvent(KEY_MANAGER, keyManagerValue)
            .editTextFiringExtraChangeEvent(SERVER_SSL_CONTEXT, serverSslContextValue);

        wizard
            .nextReview()
            .finishStayOpen()
            .verifySuccess()
            .close();

        Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
        new ResourceVerifier(keyStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue)
            .verifyAttribute(PATH, keyStorePathValue);
        new ResourceVerifier(keyManagerAddress(keyManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, keyStoreNameValue)
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
        new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client)
            .verifyExists()
            .verifyAttribute(KEY_MANAGER, keyManagerValue);
        new ResourceVerifier(HTTPS_LISTENER_ADDRESS, client)
            .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
            .verifyAttribute(VERIFY_CLIENT, NOT_REQUESTED) // default value
            .verifyAttributeIsUndefined(ENABLED_CIPHER_SUITES)
            .verifyAttributeIsUndefined(ENABLED_PROTOCOLS)
            .verifyAttributeIsUndefined(SSL_SESSION_CACHE_SIZE)
            .verifyAttributeIsUndefined(SSL_SESSION_TIMEOUT);
        ModelNodeResult aliasResult =  ops.invoke(READ_ALIAS, keyStoreAddress, Values.of(ALIAS, keyAliasValue));
        aliasResult.assertSuccess();
        sslOps
            .assertHALisDNOrganizationUnitOf(ISSUER, aliasResult, PRIVATE_KEY_ENTRY)
            .assertHALisDNOrganizationUnitOf(SUBJECT, aliasResult, PRIVATE_KEY_ENTRY);

    }

    /**
     * Testing configuration of https listener with
     * <ul>
     *  <li>just one-way server authentication</li>
     *  <li>server certificate obtained from LetsEncrypt</li>
     * </ul>
     */
    @Test
    @Category(RequiresLetsEncrypt.class)
    public void enableObtainFromLetsEncrypt() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyStorePathValue = Ids.build(KEY_STORE, PATH, Random.name()),
            keyStoreRelativeToValue = "jboss.server.config.dir",
            keyAliasValue = Ids.build(KEY_ALIAS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            caaName = Ids.build(CERTIFICATE_AUTHORITY_ACCOUNT, Random.name()),
            caaAlias = Ids.build(CERTIFICATE_AUTHORITY_ACCOUNT, ALIAS, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name());

        FILE_NAMES_TO_BE_DELETED.add(keyStorePathValue);
        EnableSslWizard wizard = page.enableSslWizard()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY)
            .disableMutualAuthentication()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY)
            .obtainFromLetsEncrypt()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        wizard.next();

        configForm
            .expectError(KEY_STORE_NAME)
            .expectError(KEY_STORE_PASSWORD)
            .expectError(KEY_STORE_PATH)
            .expectError(KEY_ALIAS)
            .expectError(KEY_MANAGER)
            .expectError(SERVER_SSL_CONTEXT)
            .expectError("certificate-authority-account-name")
            .expectError("certificate-authority-alias")
            .expectError("certificate-domain-names");

        configForm
                .editTextFiringExtraChangeEvent(KEY_STORE_NAME, keyStoreNameValue)
                .editTextFiringExtraChangeEvent(KEY_STORE_PASSWORD, keyStorePasswordValue)
                .editTextFiringExtraChangeEvent(KEY_STORE_PATH, keyStorePathValue)
                .editTextFiringExtraChangeEvent(KEY_STORE_RELATIVE_TO, keyStoreRelativeToValue)
                .editTextFiringExtraChangeEvent(KEY_ALIAS, keyAliasValue)
                .editTextFiringExtraChangeEvent(KEY_MANAGER, keyManagerValue)
                .editTextFiringExtraChangeEvent(SERVER_SSL_CONTEXT, serverSslContextValue)
                .editTextFiringExtraChangeEvent("certificate-authority-account-name", caaName)
                .editTextFiringExtraChangeEvent("certificate-authority-alias", caaAlias)
                .list("certificate-domain-names").add(DOMAIN_NAME);

        wizard
            .nextReview()
            .finishStayOpen()
            .verifySuccess()
            .close();

        Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
        new ResourceVerifier(keyStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue)
            .verifyAttribute(PATH, keyStorePathValue);
        new ResourceVerifier(keyManagerAddress(keyManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, keyStoreNameValue)
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
        new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client)
            .verifyExists()
            .verifyAttribute(KEY_MANAGER, keyManagerValue);
        new ResourceVerifier(HTTPS_LISTENER_ADDRESS, client)
            .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
            .verifyAttribute(VERIFY_CLIENT, NOT_REQUESTED) // default value
            .verifyAttributeIsUndefined(ENABLED_CIPHER_SUITES)
            .verifyAttributeIsUndefined(ENABLED_PROTOCOLS)
            .verifyAttributeIsUndefined(SSL_SESSION_CACHE_SIZE)
            .verifyAttributeIsUndefined(SSL_SESSION_TIMEOUT);
        ModelNodeResult aliasResult =  ops.invoke(READ_ALIAS, keyStoreAddress, Values.of(ALIAS, keyAliasValue));
        aliasResult.assertSuccess();
    }

    /**
     * Testing configuration of https listener with
     * <ul>
     * <li>just one-way server authentication</li>
     * <li>server key store to be created</li>
     * <li>existing server certificate to be provided by user</li>
     * </ul>
     */
    @Test
    public void enableOneWayForExistingCertificate() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyStorePathValue = Ids.build(KEY_STORE, PATH, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name());
        sslOps.createKeyStoreWithCertificate(Random.name(), keyStorePathValue, keyStorePasswordValue, Random.name()); // we need just cert store
        EnableSslWizard wizard = page.enableSslWizard()
            .disableMutualAuthentication()
            .createKeyStore()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        wizard.next();

        configForm
            .expectError(KEY_STORE_NAME)
            .expectError(KEY_STORE_PASSWORD)
            .expectError(KEY_STORE_PATH)
            .expectError(KEY_MANAGER)
            .expectError(SERVER_SSL_CONTEXT);

        configForm
            .editTextFiringExtraChangeEvent(KEY_STORE_NAME, keyStoreNameValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PASSWORD, keyStorePasswordValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PATH, keyStorePathValue)
            .editTextFiringExtraChangeEvent(KEY_MANAGER, keyManagerValue)
            .editTextFiringExtraChangeEvent(SERVER_SSL_CONTEXT, serverSslContextValue);

        wizard
            .nextReview()
            .finishStayOpen()
            .verifySuccess()
            .close();

        Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
        new ResourceVerifier(keyStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue)
            .verifyAttribute(PATH, keyStorePathValue);
        new ResourceVerifier(keyManagerAddress(keyManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, keyStoreNameValue)
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
        new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client)
            .verifyExists()
            .verifyAttribute(KEY_MANAGER, keyManagerValue);
        new ResourceVerifier(HTTPS_LISTENER_ADDRESS, client)
            .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
            .verifyAttribute(VERIFY_CLIENT, NOT_REQUESTED) // default value
            .verifyAttributeIsUndefined(ENABLED_CIPHER_SUITES)
            .verifyAttributeIsUndefined(ENABLED_PROTOCOLS)
            .verifyAttributeIsUndefined(SSL_SESSION_CACHE_SIZE)
            .verifyAttributeIsUndefined(SSL_SESSION_TIMEOUT);

    }

    /**
     * Testing configuration of https listener with
     * <ul>
     * <li>just one-way server authentication</li>
     * <li>existing server key store with certificate to be provided by user</li>
     * </ul>
     */
    @Test
    public void enableOneWayForExistingKeyStoreAndCerificate() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name());
        sslOps.createKeyStoreWithCertificate(keyStoreNameValue, Random.name(), keyStorePasswordValue, Random.name());
        EnableSslWizard wizard = page.enableSslWizard()
            .disableMutualAuthentication()
            .reuseKeyStore()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        wizard.next();

        configForm
            .expectError(KEY_STORE)
            .expectError(KEY_STORE_PASSWORD)
            .expectError(KEY_MANAGER)
            .expectError(SERVER_SSL_CONTEXT);

        configForm
            .editTextFiringExtraChangeEvent(KEY_STORE, keyStoreNameValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PASSWORD, keyStorePasswordValue)
            .editTextFiringExtraChangeEvent(KEY_MANAGER, keyManagerValue)
            .editTextFiringExtraChangeEvent(SERVER_SSL_CONTEXT, serverSslContextValue);

        wizard
            .nextReview()
            .finishStayOpen()
            .verifySuccess()
            .close();

        Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
        new ResourceVerifier(keyStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
        new ResourceVerifier(keyManagerAddress(keyManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, keyStoreNameValue)
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
        new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client)
            .verifyExists()
            .verifyAttribute(KEY_MANAGER, keyManagerValue);
        new ResourceVerifier(HTTPS_LISTENER_ADDRESS, client)
            .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
            .verifyAttribute(VERIFY_CLIENT, NOT_REQUESTED) // default value
            .verifyAttributeIsUndefined(ENABLED_CIPHER_SUITES)
            .verifyAttributeIsUndefined(ENABLED_PROTOCOLS)
            .verifyAttributeIsUndefined(SSL_SESSION_CACHE_SIZE)
            .verifyAttributeIsUndefined(SSL_SESSION_TIMEOUT);

    }

    /**
     * Testing configuration of https listener with
     * <ul>
     *  <li>mutual authentication with client trust-store to be created and existing client certificate to be provided by user</li>
     *  <li>server key store to be created</li>
     *  <li>server self-signed certificate to be generated</li>
     * </ul>
     */
    @Test
    public void enableMutualGenerated() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyStorePathValue = Ids.build(KEY_STORE, PATH, Random.name()),
            keyAliasValue = Ids.build(KEY_ALIAS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            trustManagerValue = Ids.build(TRUST_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name()),
            clientCertificateAliasValue = Ids.build(CLIENT_CERTIFICATE_ALIAS, Random.name()),
            clientCertificatePathValue = Ids.build(CLIENT_CERTIFICATE_PATH, '.', Random.name(), CERT),
            trustStoreNameValue = Ids.build(TRUST_STORE, Random.name()),
            trustStorePasswordValue = Ids.build(TRUST_STORE_PASSWORD, Random.name()),
            trustStorePathValue = Ids.build(TRUST_STORE_PATH, Random.name());
        FILE_NAMES_TO_BE_DELETED.add(keyStorePathValue);
        FILE_NAMES_TO_BE_DELETED.add(trustStorePathValue);
        sslOps.createCertificate(clientCertificatePathValue, clientCertificateAliasValue);
        EnableSslWizard wizard = page.enableSslWizard()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY)
            .enableMutualAuthentication()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY)
            .createAllResources()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        configForm.editTextFiringExtraChangeEvent(KEY_DN_ORGANIZATION, HAL);
        wizard.next();

        configForm
            .expectError(KEY_STORE_NAME)
            .expectError(KEY_STORE_PASSWORD)
            .expectError(KEY_STORE_PATH)
            .expectError(KEY_ALIAS)
            .expectError(KEY_MANAGER)
            .expectError(TRUST_MANAGER)
            .expectError(SERVER_SSL_CONTEXT)
            .expectError(CLIENT_CERTIFICATE_ALIAS)
            .expectError(CLIENT_CERTIFICATE_PATH)
            .expectError(TRUST_STORE_NAME)
            .expectError(TRUST_STORE_PASSWORD)
            .expectError(TRUST_STORE_PATH);

        configForm
            .editTextFiringExtraChangeEvent(KEY_STORE_NAME, keyStoreNameValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PASSWORD, keyStorePasswordValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PATH, keyStorePathValue)
            .editTextFiringExtraChangeEvent(KEY_ALIAS, keyAliasValue)
            .editTextFiringExtraChangeEvent(KEY_MANAGER, keyManagerValue)
            .editTextFiringExtraChangeEvent(TRUST_MANAGER, trustManagerValue)
            .editTextFiringExtraChangeEvent(SERVER_SSL_CONTEXT, serverSslContextValue)
            .editTextFiringExtraChangeEvent(CLIENT_CERTIFICATE_ALIAS, clientCertificateAliasValue)
            .editTextFiringExtraChangeEvent(CLIENT_CERTIFICATE_PATH, clientCertificatePathValue)
            .flip(CLIENT_CERTIFICATE_VALIDATE, false)
            .editTextFiringExtraChangeEvent(TRUST_STORE_NAME, trustStoreNameValue)
            .editTextFiringExtraChangeEvent(TRUST_STORE_PASSWORD, trustStorePasswordValue)
            .editTextFiringExtraChangeEvent(TRUST_STORE_PATH, trustStorePathValue);

        wizard
            .nextReview()
            .finishStayOpen()
            .verifySuccess()
            .close();

        Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
        new ResourceVerifier(keyStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue)
            .verifyAttribute(PATH, keyStorePathValue);
        Address trustStoreAddress = keyStoreAddress(trustStoreNameValue);
        new ResourceVerifier(trustStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, trustStorePasswordValue)
            .verifyAttribute(PATH, trustStorePathValue);
        new ResourceVerifier(trustManagerAddress(trustManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, trustStoreNameValue);
        new ResourceVerifier(keyManagerAddress(keyManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, keyStoreNameValue)
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
        new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client)
            .verifyExists()
            .verifyAttribute(KEY_MANAGER, keyManagerValue)
            .verifyAttribute(TRUST_MANAGER, trustManagerValue);
        new ResourceVerifier(HTTPS_LISTENER_ADDRESS, client)
            .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
            .verifyAttribute(VERIFY_CLIENT, NOT_REQUESTED) // default value
            .verifyAttributeIsUndefined(ENABLED_CIPHER_SUITES)
            .verifyAttributeIsUndefined(ENABLED_PROTOCOLS)
            .verifyAttributeIsUndefined(SSL_SESSION_CACHE_SIZE)
            .verifyAttributeIsUndefined(SSL_SESSION_TIMEOUT);
        ModelNodeResult serverAliasResult =  ops.invoke(READ_ALIAS, keyStoreAddress, Values.of(ALIAS, keyAliasValue));
        serverAliasResult.assertSuccess();
        sslOps
            .assertHALisDNOrganizationUnitOf(ISSUER, serverAliasResult, PRIVATE_KEY_ENTRY)
            .assertHALisDNOrganizationUnitOf(SUBJECT, serverAliasResult, PRIVATE_KEY_ENTRY);
        ModelNodeResult clientAliasResult =  ops.invoke(READ_ALIAS, trustStoreAddress, Values.of(ALIAS, clientCertificateAliasValue));
        clientAliasResult.assertSuccess();
        sslOps
            .assertHALisDNOrganizationUnitOf(ISSUER, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY)
            .assertHALisDNOrganizationUnitOf(SUBJECT, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY);

    }

    /**
     * Testing configuration of https listener with
     * <ul>
     *  <li>mutual authentication with client trust-store to be created and existing client certificate to be provided by user</li>
     *  <li>server key store to be created</li>
     *  <li>existing server certificate to be provided by user</li>
     * </ul>
     */
    @Test
    public void enableMutualForExistingServerCertificate() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyStorePathValue = Ids.build(KEY_STORE, PATH, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            trustManagerValue = Ids.build(TRUST_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name()),
            clientCertificateAliasValue = Ids.build(CLIENT_CERTIFICATE_ALIAS, Random.name()),
            clientCertificatePathValue = Ids.build(CLIENT_CERTIFICATE_PATH, '.', Random.name(), CERT),
            trustStoreNameValue = Ids.build(TRUST_STORE, Random.name()),
            trustStorePasswordValue = Ids.build(TRUST_STORE_PASSWORD, Random.name()),
            trustStorePathValue = Ids.build(TRUST_STORE_PATH, Random.name());
        FILE_NAMES_TO_BE_DELETED.add(trustStorePathValue);
        sslOps.createKeyStoreWithCertificate(Random.name(), keyStorePathValue, keyStorePasswordValue, Random.name()); // we need just cert store
        sslOps.createCertificate(clientCertificatePathValue, clientCertificateAliasValue);
        EnableSslWizard wizard = page.enableSslWizard()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY)
            .enableMutualAuthentication()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY)
            .createKeyStore()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        wizard.next();

        configForm
            .expectError(KEY_STORE_NAME)
            .expectError(KEY_STORE_PASSWORD)
            .expectError(KEY_STORE_PATH)
            .expectError(KEY_MANAGER)
            .expectError(TRUST_MANAGER)
            .expectError(SERVER_SSL_CONTEXT)
            .expectError(CLIENT_CERTIFICATE_ALIAS)
            .expectError(CLIENT_CERTIFICATE_PATH)
            .expectError(TRUST_STORE_NAME)
            .expectError(TRUST_STORE_PASSWORD)
            .expectError(TRUST_STORE_PATH);

        configForm
            .editTextFiringExtraChangeEvent(KEY_STORE_NAME, keyStoreNameValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PASSWORD, keyStorePasswordValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PATH, keyStorePathValue)
            .editTextFiringExtraChangeEvent(KEY_MANAGER, keyManagerValue)
            .editTextFiringExtraChangeEvent(TRUST_MANAGER, trustManagerValue)
            .editTextFiringExtraChangeEvent(SERVER_SSL_CONTEXT, serverSslContextValue)
            .editTextFiringExtraChangeEvent(CLIENT_CERTIFICATE_ALIAS, clientCertificateAliasValue)
            .editTextFiringExtraChangeEvent(CLIENT_CERTIFICATE_PATH, clientCertificatePathValue)
            .flip(CLIENT_CERTIFICATE_VALIDATE, false)
            .editTextFiringExtraChangeEvent(TRUST_STORE_NAME, trustStoreNameValue)
            .editTextFiringExtraChangeEvent(TRUST_STORE_PASSWORD, trustStorePasswordValue)
            .editTextFiringExtraChangeEvent(TRUST_STORE_PATH, trustStorePathValue);

        wizard
            .nextReview()
            .finishStayOpen()
            .verifySuccess()
            .close();

        Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
        new ResourceVerifier(keyStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue)
            .verifyAttribute(PATH, keyStorePathValue);
        Address trustStoreAddress = keyStoreAddress(trustStoreNameValue);
        new ResourceVerifier(trustStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, trustStorePasswordValue)
            .verifyAttribute(PATH, trustStorePathValue);
        new ResourceVerifier(trustManagerAddress(trustManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, trustStoreNameValue);
        new ResourceVerifier(keyManagerAddress(keyManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, keyStoreNameValue)
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
        new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client)
            .verifyExists()
            .verifyAttribute(KEY_MANAGER, keyManagerValue)
            .verifyAttribute(TRUST_MANAGER, trustManagerValue);
        new ResourceVerifier(HTTPS_LISTENER_ADDRESS, client)
            .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
            .verifyAttribute(VERIFY_CLIENT, NOT_REQUESTED) // default value
            .verifyAttributeIsUndefined(ENABLED_CIPHER_SUITES)
            .verifyAttributeIsUndefined(ENABLED_PROTOCOLS)
            .verifyAttributeIsUndefined(SSL_SESSION_CACHE_SIZE)
            .verifyAttributeIsUndefined(SSL_SESSION_TIMEOUT);
        ModelNodeResult clientAliasResult =  ops.invoke(READ_ALIAS, trustStoreAddress, Values.of(ALIAS, clientCertificateAliasValue));
        clientAliasResult.assertSuccess();
        sslOps
            .assertHALisDNOrganizationUnitOf(ISSUER, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY)
            .assertHALisDNOrganizationUnitOf(SUBJECT, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY);

    }

    /**
     * Testing configuration of https listener with
     * <ul>
     *  <li>mutual authentication with client trust-store to be created and client certificate to be provided by user</li>
     *  <li>existing server key store with existing certificate to be provided by user</li>
     * </ul>
     */
    @Test
    public void enableMutualForExistingServerKeyStore() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            trustManagerValue = Ids.build(TRUST_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name()),
            clientCertificateAliasValue = Ids.build(CLIENT_CERTIFICATE_ALIAS, Random.name()),
            clientCertificatePathValue = Ids.build(CLIENT_CERTIFICATE_PATH, '.', Random.name(), CERT),
            trustStoreNameValue = Ids.build(TRUST_STORE, Random.name()),
            trustStorePasswordValue = Ids.build(TRUST_STORE_PASSWORD, Random.name()),
            trustStorePathValue = Ids.build(TRUST_STORE_PATH, Random.name());
        FILE_NAMES_TO_BE_DELETED.add(trustStorePathValue);
        sslOps.createKeyStoreWithCertificate(keyStoreNameValue, Random.name(), keyStorePasswordValue, Random.name());
        sslOps.createCertificate(clientCertificatePathValue, clientCertificateAliasValue);
        EnableSslWizard wizard = page.enableSslWizard()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY)
            .enableMutualAuthentication()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY)
            .reuseKeyStore()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        wizard.next();

        configForm
            .expectError(KEY_STORE)
            .expectError(KEY_STORE_PASSWORD)
            .expectError(KEY_MANAGER)
            .expectError(TRUST_MANAGER)
            .expectError(SERVER_SSL_CONTEXT)
            .expectError(CLIENT_CERTIFICATE_ALIAS)
            .expectError(CLIENT_CERTIFICATE_PATH)
            .expectError(TRUST_STORE_NAME)
            .expectError(TRUST_STORE_PASSWORD)
            .expectError(TRUST_STORE_PATH);

        configForm
            .editTextFiringExtraChangeEvent(KEY_STORE, keyStoreNameValue)
            .editTextFiringExtraChangeEvent(KEY_STORE_PASSWORD, keyStorePasswordValue)
            .editTextFiringExtraChangeEvent(KEY_MANAGER, keyManagerValue)
            .editTextFiringExtraChangeEvent(TRUST_MANAGER, trustManagerValue)
            .editTextFiringExtraChangeEvent(SERVER_SSL_CONTEXT, serverSslContextValue)
            .editTextFiringExtraChangeEvent(CLIENT_CERTIFICATE_ALIAS, clientCertificateAliasValue)
            .editTextFiringExtraChangeEvent(CLIENT_CERTIFICATE_PATH, clientCertificatePathValue)
            .flip(CLIENT_CERTIFICATE_VALIDATE, false)
            .editTextFiringExtraChangeEvent(TRUST_STORE_NAME, trustStoreNameValue)
            .editTextFiringExtraChangeEvent(TRUST_STORE_PASSWORD, trustStorePasswordValue)
            .editTextFiringExtraChangeEvent(TRUST_STORE_PATH, trustStorePathValue);

        wizard
            .nextReview()
            .finishStayOpen()
            .verifySuccess()
            .close();

        Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
        new ResourceVerifier(keyStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
        Address trustStoreAddress = keyStoreAddress(trustStoreNameValue);
        new ResourceVerifier(trustStoreAddress, client)
            .verifyExists()
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, trustStorePasswordValue)
            .verifyAttribute(PATH, trustStorePathValue);
        new ResourceVerifier(trustManagerAddress(trustManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, trustStoreNameValue);
        new ResourceVerifier(keyManagerAddress(keyManagerValue), client)
            .verifyExists()
            .verifyAttribute(KEY_STORE, keyStoreNameValue)
            .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
        new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client)
            .verifyExists()
            .verifyAttribute(KEY_MANAGER, keyManagerValue)
            .verifyAttribute(TRUST_MANAGER, trustManagerValue);
        new ResourceVerifier(HTTPS_LISTENER_ADDRESS, client)
            .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
            .verifyAttribute(VERIFY_CLIENT, NOT_REQUESTED) // default value
            .verifyAttributeIsUndefined(ENABLED_CIPHER_SUITES)
            .verifyAttributeIsUndefined(ENABLED_PROTOCOLS)
            .verifyAttributeIsUndefined(SSL_SESSION_CACHE_SIZE)
            .verifyAttributeIsUndefined(SSL_SESSION_TIMEOUT);
        ModelNodeResult clientAliasResult =  ops.invoke(READ_ALIAS, trustStoreAddress, Values.of(ALIAS, clientCertificateAliasValue));
        clientAliasResult.assertSuccess();
        sslOps.assertHALisDNOrganizationUnitOf(ISSUER, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY);
        sslOps.assertHALisDNOrganizationUnitOf(SUBJECT, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY);

    }
}
