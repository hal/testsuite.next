package org.jboss.hal.testsuite.test.configuration.management.ssl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.ssl.EnableSslWizard;
import org.jboss.hal.testsuite.page.runtime.StandaloneServerPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.foundation.online.SnapshotBackup;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.SslOptions;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.keyManagerAddress;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.keyStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.serverSslContextAddress;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.trustManagerAddress;
import static org.jboss.hal.testsuite.test.configuration.management.ssl.KeyEntryType.PRIVATE_KEY_ENTRY;
import static org.jboss.hal.testsuite.test.configuration.management.ssl.KeyEntryType.TRUSTED_CERTIFICATE_ENTRY;
import static org.jboss.hal.testsuite.test.configuration.management.ssl.SslFixtures.*;

@RunWith(Arquillian.class)
public class HttpManagementInterfaceTest {

    private static final List<String> FILE_NAMES_TO_BE_DELETED = new ArrayList<>();
    static final Address HTTP_INTERFACE_ADDRESS = Address.coreService(MANAGEMENT).and(MANAGEMENT_INTERFACE, HTTP_INTERFACE);

    private static OnlineManagementClient client;
    private static Operations ops;
    private static SslOperations sslOps;
    private static File keyStoresDirectory;

    private SnapshotBackup snapshot = new SnapshotBackup();

    @Inject
    private Console console;

    @Page
    private StandaloneServerPage page;

    @BeforeClass
    public static void setUp() throws IOException {
        changeClientToNonSSL();
        keyStoresDirectory = sslOps.getKeyStoresDirectory();
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        FILE_NAMES_TO_BE_DELETED.stream().forEach(fileName -> FileUtils.deleteQuietly(new File(keyStoresDirectory, fileName)));
        client.close();
    }

    @Before
    public void backup() throws CommandFailedException, IOException {
        client.apply(snapshot.backup());
    }

    @After
    public void restoreBackup() throws IOException, InterruptedException, TimeoutException, CommandFailedException {
        client.apply(snapshot.restore());
    }

    /**
     * Testing configuration of http management interface with
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

        // Required fields validation test
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

        try {
            wizard
                .nextReview()
                .finishStayOpen()
                .verifySuccess()
                .close();

            reloadOneWaySecuredManagementWithClientUpdate(keyStoreNameValue, keyAliasValue);

            Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
            new ResourceVerifier(keyStoreAddress, client).verifyExists()
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue)
                    .verifyAttribute(PATH, keyStorePathValue);
            new ResourceVerifier(keyManagerAddress(keyManagerValue), client).verifyExists()
                    .verifyAttribute(KEY_STORE, keyStoreNameValue)
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
            new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client).verifyExists()
                    .verifyAttribute(KEY_MANAGER, keyManagerValue);
            new ResourceVerifier(HTTP_INTERFACE_ADDRESS, client)
                    .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
                    .verifyAttribute(SECURE_SOCKET_BINDING, MANAGEMENT_HTTPS);

            // Alias validation
            ModelNodeResult aliasResult = ops.invoke(READ_ALIAS, keyStoreAddress, Values.of(ALIAS, keyAliasValue));
            aliasResult.assertSuccess();
            sslOps.assertHALisDNOrganizationUnitOf(ISSUER, aliasResult, PRIVATE_KEY_ENTRY)
                    .assertHALisDNOrganizationUnitOf(SUBJECT, aliasResult, PRIVATE_KEY_ENTRY);
        } finally {
            disableHttpInterfaceSsl();
        }

    }

    /**
     * Testing configuration of http management interface with
     * <ul>
     *  <li>just one-way server authentication</li>
     *  <li>server key store to be created</li>
     *  <li>existing server certificate to be provided by user</li>
     * </ul>
     */
    @Test
    public void enableOneWayForExistingCertificate() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyStorePathValue = Ids.build(KEY_STORE, PATH, Random.name()),
            keyAliasValue = Ids.build(KEY_ALIAS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name());
        sslOps.createKeyStoreWithCertificate(Random.name(), keyStorePathValue, keyStorePasswordValue, keyAliasValue); // we need just cert store
        EnableSslWizard wizard = page.enableSslWizard()
            .disableMutualAuthentication()
            .createKeyStore()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        wizard.next();

        // Required fields validation test
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

        try {
            wizard
                .nextReview()
                .finishStayOpen()
                .verifySuccess()
                .close();

            reloadOneWaySecuredManagementWithClientUpdate(keyStoreNameValue, keyAliasValue);

            Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
            new ResourceVerifier(keyStoreAddress, client).verifyExists()
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue)
                    .verifyAttribute(PATH, keyStorePathValue);
            new ResourceVerifier(keyManagerAddress(keyManagerValue), client)
                    .verifyExists()
                    .verifyAttribute(KEY_STORE, keyStoreNameValue)
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
            new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client)
                    .verifyExists()
                    .verifyAttribute(KEY_MANAGER, keyManagerValue);
            new ResourceVerifier(HTTP_INTERFACE_ADDRESS, client)
                    .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
                    .verifyAttribute(SECURE_SOCKET_BINDING, MANAGEMENT_HTTPS);

        } finally {
            disableHttpInterfaceSsl();
        }

    }

    /**
     * Testing configuration of http management interface with
     * <ul>
     *  <li>just one-way server authentication</li>
     *  <li>existing server key store with certificate to be provided by user</li>
     * </ul>
     */
    @Test
    public void enableOneWayForExistingKeyStoreAndCerificate() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyAliasValue = Ids.build(KEY_ALIAS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name());
        sslOps.createKeyStoreWithCertificate(keyStoreNameValue, Random.name(), keyStorePasswordValue, keyAliasValue); // we need just cert store
        EnableSslWizard wizard = page.enableSslWizard()
            .disableMutualAuthentication()
            .reuseKeyStore()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        wizard.next();

        // Required fields validation test
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

        try {
            wizard
                .nextReview()
                .finishStayOpen()
                .verifySuccess()
                .close();

            reloadOneWaySecuredManagementWithClientUpdate(keyStoreNameValue, keyAliasValue);

            Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
            new ResourceVerifier(keyStoreAddress, client).verifyExists()
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
            new ResourceVerifier(keyManagerAddress(keyManagerValue), client)
                    .verifyExists()
                    .verifyAttribute(KEY_STORE, keyStoreNameValue)
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
            new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client)
                    .verifyExists()
                    .verifyAttribute(KEY_MANAGER, keyManagerValue);
            new ResourceVerifier(HTTP_INTERFACE_ADDRESS, client)
                    .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
                    .verifyAttribute(SECURE_SOCKET_BINDING, MANAGEMENT_HTTPS);

        } finally {
            disableHttpInterfaceSsl();
        }

    }

    /**
     * Testing configuration of http management interface with
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
            clientKeyStorePathValue = Ids.build(CLIENT, KEY_STORE_PATH, Random.name()),
            clientKeyStorePasswordValue = Ids.build(CLIENT, KEY_STORE, PASS, Random.name()),
            trustStoreNameValue = Ids.build(TRUST_STORE, Random.name()),
            trustStorePasswordValue = Ids.build(TRUST_STORE_PASSWORD, Random.name()),
            trustStorePathValue = Ids.build(TRUST_STORE_PATH, Random.name());
        FILE_NAMES_TO_BE_DELETED.add(keyStorePathValue);
        FILE_NAMES_TO_BE_DELETED.add(trustStorePathValue);
        Address clientKeyStoreAddress = sslOps.createKeyStoreWithCertificate(Random.name(), clientKeyStorePathValue,
                clientKeyStorePasswordValue, clientCertificateAliasValue);

        // Creating client certificate which needs to be provided by user
        sslOps.createCertificate(clientCertificatePathValue, clientCertificateAliasValue, clientKeyStoreAddress);

        EnableSslWizard wizard = page.enableSslWizard()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY)
            .enableMutualAuthentication()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY)
            .createAllResources()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        configForm.editTextFiringExtraChangeEvent(KEY_DN_ORGANIZATION, HAL);
        wizard.next();

        // Required fields validation test
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

        try {
            wizard
                .nextReview()
                .finishStayOpen()
                .verifySuccess()
                .close();

            reloadMutualSecuredManagementWithClientUpdate(keyStoreNameValue, keyAliasValue, clientCertificateAliasValue,
                clientKeyStorePathValue, clientKeyStorePasswordValue);

            Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
            new ResourceVerifier(keyStoreAddress, client).verifyExists()
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue)
                    .verifyAttribute(PATH, keyStorePathValue);
            Address trustStoreAddress = keyStoreAddress(trustStoreNameValue);
            new ResourceVerifier(trustStoreAddress, client).verifyExists()
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, trustStorePasswordValue)
                    .verifyAttribute(PATH, trustStorePathValue);
            new ResourceVerifier(trustManagerAddress(trustManagerValue), client).verifyExists()
                    .verifyAttribute(KEY_STORE, trustStoreNameValue);
            new ResourceVerifier(keyManagerAddress(keyManagerValue), client).verifyExists()
                    .verifyAttribute(KEY_STORE, keyStoreNameValue)
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue);
            new ResourceVerifier(serverSslContextAddress(serverSslContextValue), client).verifyExists()
                    .verifyAttribute(KEY_MANAGER, keyManagerValue).verifyAttribute(TRUST_MANAGER, trustManagerValue);
            new ResourceVerifier(HTTP_INTERFACE_ADDRESS, client).verifyAttribute(SSL_CONTEXT, serverSslContextValue)
                    .verifyAttribute(SECURE_SOCKET_BINDING, MANAGEMENT_HTTPS);

            // Alias validation
            ModelNodeResult serverAliasResult = ops.invoke(READ_ALIAS, keyStoreAddress,
                    Values.of(ALIAS, keyAliasValue));
            serverAliasResult.assertSuccess();
            sslOps.assertHALisDNOrganizationUnitOf(ISSUER, serverAliasResult, PRIVATE_KEY_ENTRY)
                    .assertHALisDNOrganizationUnitOf(SUBJECT, serverAliasResult, PRIVATE_KEY_ENTRY);
            ModelNodeResult clientAliasResult = ops.invoke(READ_ALIAS, trustStoreAddress,
                    Values.of(ALIAS, clientCertificateAliasValue));
            clientAliasResult.assertSuccess();
            sslOps.assertHALisDNOrganizationUnitOf(ISSUER, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY)
                    .assertHALisDNOrganizationUnitOf(SUBJECT, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY);
        } finally {
            disableHttpInterfaceSsl();
        }

    }

    /**
     * Testing configuration of http management interface with
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
            keyAliasValue = Ids.build(KEY_ALIAS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            trustManagerValue = Ids.build(TRUST_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name()),
            clientCertificateAliasValue = Ids.build(CLIENT_CERTIFICATE_ALIAS, Random.name()),
            clientCertificatePathValue = Ids.build(CLIENT_CERTIFICATE_PATH, '.', Random.name(), CERT),
            clientKeyStorePathValue = Ids.build(CLIENT, KEY_STORE_PATH, Random.name()),
            clientKeyStorePasswordValue = Ids.build(CLIENT, KEY_STORE, PASS, Random.name()),
            trustStoreNameValue = Ids.build(TRUST_STORE, Random.name()),
            trustStorePasswordValue = Ids.build(TRUST_STORE_PASSWORD, Random.name()),
            trustStorePathValue = Ids.build(TRUST_STORE_PATH, Random.name());
        FILE_NAMES_TO_BE_DELETED.add(trustStorePathValue);

        sslOps.createKeyStoreWithCertificate(Random.name(), keyStorePathValue, keyStorePasswordValue, keyAliasValue); // we need just cert store
        Address clientKeyStoreAddress = sslOps.createKeyStoreWithCertificate(Random.name(), clientKeyStorePathValue,
                clientKeyStorePasswordValue, clientCertificateAliasValue);
        // Creating client certificate which needs to be provided by user
        sslOps.createCertificate(clientCertificatePathValue, clientCertificateAliasValue, clientKeyStoreAddress);

        EnableSslWizard wizard = page.enableSslWizard()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY)
            .enableMutualAuthentication()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY)
            .createKeyStore()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        wizard.next();

        // Required fields validation test
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

        try {
            wizard
                .nextReview()
                .finishStayOpen()
                .verifySuccess()
                .close();

            reloadMutualSecuredManagementWithClientUpdate(keyStoreNameValue, keyAliasValue, clientCertificateAliasValue,
                clientKeyStorePathValue, clientKeyStorePasswordValue);

            Address keyStoreAddress = keyStoreAddress(keyStoreNameValue);
            new ResourceVerifier(keyStoreAddress, client).verifyExists()
                    .verifyAttribute(CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR, keyStorePasswordValue)
                    .verifyAttribute(PATH, keyStorePathValue);
            Address trustStoreAddress = keyStoreAddress(trustStoreNameValue);
            new ResourceVerifier(trustStoreAddress, client).verifyExists()
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
            new ResourceVerifier(HTTP_INTERFACE_ADDRESS, client)
                    .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
                    .verifyAttribute(SECURE_SOCKET_BINDING, MANAGEMENT_HTTPS);

            // Alias validation
            ModelNodeResult serverAliasResult = ops.invoke(READ_ALIAS, keyStoreAddress,
                    Values.of(ALIAS, keyAliasValue));
            serverAliasResult.assertSuccess();
            sslOps.assertHALisDNOrganizationUnitOf(ISSUER, serverAliasResult, PRIVATE_KEY_ENTRY)
                    .assertHALisDNOrganizationUnitOf(SUBJECT, serverAliasResult, PRIVATE_KEY_ENTRY);
            ModelNodeResult clientAliasResult = ops.invoke(READ_ALIAS, trustStoreAddress,
                    Values.of(ALIAS, clientCertificateAliasValue));
            clientAliasResult.assertSuccess();
            sslOps.assertHALisDNOrganizationUnitOf(ISSUER, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY)
                    .assertHALisDNOrganizationUnitOf(SUBJECT, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY);
        } finally {
            disableHttpInterfaceSsl();
        }

    }

    /**
     * Testing configuration of http management interface with
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
            keyAliasValue = Ids.build(KEY_ALIAS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            trustManagerValue = Ids.build(TRUST_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name()),
            clientCertificateAliasValue = Ids.build(CLIENT_CERTIFICATE_ALIAS, Random.name()),
            clientCertificatePathValue = Ids.build(CLIENT_CERTIFICATE_PATH, '.', Random.name(), CERT),
            clientKeyStorePathValue = Ids.build(CLIENT, KEY_STORE_PATH, Random.name()),
            clientKeyStorePasswordValue = Ids.build(CLIENT, KEY_STORE, PASS, Random.name()),
            trustStoreNameValue = Ids.build(TRUST_STORE, Random.name()),
            trustStorePasswordValue = Ids.build(TRUST_STORE_PASSWORD, Random.name()),
            trustStorePathValue = Ids.build(TRUST_STORE_PATH, Random.name());
        FILE_NAMES_TO_BE_DELETED.add(trustStorePathValue);

        sslOps.createKeyStoreWithCertificate(keyStoreNameValue, Random.name(), keyStorePasswordValue, keyAliasValue); // we need just cert store
        Address clientKeyStoreAddress = sslOps.createKeyStoreWithCertificate(Random.name(), clientKeyStorePathValue,
                clientKeyStorePasswordValue, clientCertificateAliasValue);
        // Creating client certificate which needs to be provided by user
        sslOps.createCertificate(clientCertificatePathValue, clientCertificateAliasValue, clientKeyStoreAddress);

        EnableSslWizard wizard = page.enableSslWizard()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY)
            .enableMutualAuthentication()
            .tryNextToConfigurationWithExpectError(YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY)
            .reuseKeyStore()
            .nextConfiguration();
        FormFragment configForm = wizard.getConfigurationForm();
        wizard.next();

        // Required fields validation test
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

        try {
            wizard
                .nextReview()
                .finishStayOpen()
                .verifySuccess()
                .close();

            reloadMutualSecuredManagementWithClientUpdate(keyStoreNameValue, keyAliasValue, clientCertificateAliasValue,
                clientKeyStorePathValue, clientKeyStorePasswordValue);

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
            new ResourceVerifier(HTTP_INTERFACE_ADDRESS, client)
                    .verifyAttribute(SSL_CONTEXT, serverSslContextValue)
                    .verifyAttribute(SECURE_SOCKET_BINDING, MANAGEMENT_HTTPS);

            // Alias validation
            ModelNodeResult serverAliasResult = ops.invoke(READ_ALIAS, keyStoreAddress,
                    Values.of(ALIAS, keyAliasValue));
            serverAliasResult.assertSuccess();
            sslOps.assertHALisDNOrganizationUnitOf(ISSUER, serverAliasResult, PRIVATE_KEY_ENTRY)
                    .assertHALisDNOrganizationUnitOf(SUBJECT, serverAliasResult, PRIVATE_KEY_ENTRY);
            ModelNodeResult clientAliasResult = ops.invoke(READ_ALIAS, trustStoreAddress,
                    Values.of(ALIAS, clientCertificateAliasValue));
            clientAliasResult.assertSuccess();
            sslOps.assertHALisDNOrganizationUnitOf(ISSUER, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY)
                    .assertHALisDNOrganizationUnitOf(SUBJECT, clientAliasResult, TRUSTED_CERTIFICATE_ENTRY);
        } finally {
            disableHttpInterfaceSsl();
        }

    }

    /**
     * Testing the ability to disable SSL for http management interface
     */
    @Test
    public void disableSSL() throws Exception {

        String
            keyStoreNameValue = Ids.build(KEY_STORE, NAME, Random.name()),
            keyStorePasswordValue = Ids.build(KEY_STORE, PASS, Random.name()),
            keyManagerValue = Ids.build(KEY_MANAGER, Random.name()),
            serverSslContextValue = Ids.build(SERVER_SSL_CONTEXT, Random.name());
        sslOps.createKeyStoreWithCertificate(keyStoreNameValue, Random.name(), keyStorePasswordValue, Random.name());
        ops.add(keyManagerAddress(keyManagerValue), Values.of(KEY_STORE, keyStoreNameValue)
                .andObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, keyStorePasswordValue))).assertSuccess();
        ops.add(serverSslContextAddress(serverSslContextValue), Values.of(KEY_MANAGER, keyManagerValue)).assertSuccess();
        ops.batch(new Batch()
                .writeAttribute(HTTP_INTERFACE_ADDRESS, SSL_CONTEXT, serverSslContextValue)
                .writeAttribute(HTTP_INTERFACE_ADDRESS, SECURE_SOCKET_BINDING, MANAGEMENT_HTTPS))
            .assertSuccess();

        ResourceVerifier httpInterfaceVerifier = new ResourceVerifier(HTTP_INTERFACE_ADDRESS, client)
                .verifyExists()
                .verifyAttribute(SECURE_SOCKET_BINDING, MANAGEMENT_HTTPS)
                .verifyAttribute(SSL_CONTEXT, serverSslContextValue);

        page.navigateToHttpManagementPage()
            .disableSslWithReload();

        sslOps.waitUntilServerIsRunning();

        httpInterfaceVerifier
            .verifyAttributeIsUndefined(SECURE_SOCKET_BINDING)
            .verifyAttributeIsUndefined(SECURITY_REALM);

    }

    private void reloadOneWaySecuredManagementWithClientUpdate(String serverKeyStoreNameValue, String serverKeyAliasValue)
            throws IOException, InterruptedException, TimeoutException {
        SslOptionsCreator clientSslOptionsCreator = (creaperTrustStorePath, creaperTrustStorePassword) -> {
            return new SslOptions.Builder()
                    .trustStore(new File(keyStoresDirectory, creaperTrustStorePath))
                    .trustStorePassword(creaperTrustStorePassword)
                    .build();
        };
        reloadWithClientUpdate(serverKeyStoreNameValue, serverKeyAliasValue, clientSslOptionsCreator);
    }

    private void reloadMutualSecuredManagementWithClientUpdate(String serverKeyStoreNameValue, String serverKeyAliasValue,
            String clientCertificateAliasValue, String clientKeyStorePathValue, String clientKeyStorePasswordValue)
            throws IOException, InterruptedException, TimeoutException {

        SslOptionsCreator clientSslOptionsCreator = (creaperTrustStorePath, creaperTrustStorePassword) -> {
            return new SslOptions.Builder()
                    .keyStore(new File(keyStoresDirectory, clientKeyStorePathValue))
                    .keyStorePassword(clientKeyStorePasswordValue)
                    .key(clientCertificateAliasValue, clientKeyStorePasswordValue)
                    .trustStore(new File(keyStoresDirectory, creaperTrustStorePath))
                    .trustStorePassword(creaperTrustStorePassword)
                    .build();
        };
        reloadWithClientUpdate(serverKeyStoreNameValue, serverKeyAliasValue, clientSslOptionsCreator);
    }

    private void reloadWithClientUpdate(String serverKeyStoreNameValue, String serverKeyAliasValue, SslOptionsCreator sslOptionsCreator)
            throws IOException, InterruptedException, TimeoutException {
        String
            serverCertificatePathValue = Ids.build(SERVER_CERTIFICATE_PATH, '.', Random.name(), CERT),
            creaperTrustStoreName = Ids.build(TRUST_STORE, Random.name()),
            creaperTrustStorePath = Ids.build(TRUST_STORE_PATH, Random.name()),
            creaperTrustStorePassword = Ids.build(TRUST_STORE_PASSWORD, Random.name());
        FILE_NAMES_TO_BE_DELETED.add(serverCertificatePathValue);

        ops.invoke(EXPORT_CERTIFICATE, keyStoreAddress(serverKeyStoreNameValue),
                Values.of(ALIAS, serverKeyAliasValue).and(PATH, serverCertificatePathValue)).assertSuccess();
        Address creaperTrustStoreAddress = sslOps.createKeyStore(creaperTrustStoreName, creaperTrustStorePath,
                creaperTrustStorePassword);
        ops.invoke(IMPORT_CERTIFICATE, creaperTrustStoreAddress,
                Values.of(ALIAS, serverKeyAliasValue).and(PATH, serverCertificatePathValue).and(VALIDATE, false)
                        .and(TRUST_CACERTS, false)
                        .andObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, creaperTrustStorePassword)));
        ops.invoke(STORE, creaperTrustStoreAddress);

        ops.invoke(RELOAD, Address.root());
        changeClient(sslOptionsCreator.create(creaperTrustStorePath, creaperTrustStorePassword));
        sslOps.waitUntilServerIsRunning();
    }

    private void disableHttpInterfaceSsl() throws IOException, InterruptedException, TimeoutException {
        ops.undefineAttribute(HTTP_INTERFACE_ADDRESS, SECURE_SOCKET_BINDING).assertSuccess();
        ops.undefineAttribute(HTTP_INTERFACE_ADDRESS, SSL_CONTEXT).assertSuccess();
        ops.invoke(RELOAD, Address.root());
        changeClientToNonSSL();
        sslOps.waitUntilServerIsRunning();
    }

    private static void changeClient(SslOptions sslOptions) throws IOException {
        if (client != null) {
            client.close();
        }
        client = SslOperations.createSslClient(sslOptions);
        ops = new Operations(client);
        sslOps = new SslOperations(client).filesToBeCleanedUp(FILE_NAMES_TO_BE_DELETED);
    }

    private static void changeClientToNonSSL() throws IOException {
        if (client != null) {
            client.close();
        }
        client = ManagementClientProvider.createOnlineManagementClient();
        ops = new Operations(client);
        sslOps = new SslOperations(client).filesToBeCleanedUp(FILE_NAMES_TO_BE_DELETED);
    }

    @FunctionalInterface
    private static interface SslOptionsCreator {
        SslOptions create(String creaperTrustStorePath, String creaperTrustStorePassword);
    }
}
