/*
 * Copyright 2015-2018 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.testsuite.test.configuration.elytron.ssl;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.category.RequiresLetsEncrypt;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.ConfirmationDialogFragment;
import org.jboss.hal.testsuite.page.runtime.elytron.ElytronRuntimeStoresPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ALIASES;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.JKS;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.RSA;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SHA_256_WITH_RSA;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.SIGNATURE_ALGORITHM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.certificateAuthorityAccountAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.keyStoreAddress;
@Category(RequiresLetsEncrypt.class)
@RunWith(Arquillian.class)
public class KeyStoreTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String KEY_STORE_NAME = "key-store-" + Random.name();
    private static final String KEY_STORE_NAME2 = "key-store2-" + Random.name();
    private static final String ALIAS_GEN_KEY = "genkey-" + Random.name();
    private static final String ALIAS_GEN_KEY_FULL = "genkey-full-" + Random.name();
    private static final String ALIAS_IMPORTED = "imported-" + Random.name();
    private static final String ALIAS_OBTAIN = "obtain-" + Random.name();
    private static final String ALIAS_OBTAIN_STAGING = "obtain-stag-" + Random.name();
    private static final String ALIAS_TO_RENAME = "to-rename-" + Random.name();
    private static final String ALIAS_TRY_TO_RENAME = "try-to-rename" + Random.name();
    private static final String ALIAS_RENAMED = "renamed-" + Random.name();
    private static final String ALIAS_TO_EXPORT = "to-export-" + Random.name();
    private static final String ALIAS_TO_REMOVE = "to-remove-" + Random.name();
    private static final String ALIAS_TO_REVOKE = "to-revoke-" + Random.name();
    private static final String CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE = "caa-update-" + Random.name();
    private static final String EXPORTED_CERT_FILENAME = "exported-" + Random.name() + ".cer";
    private static final String EXPORTED_CERT_FILENAME2 = "exported2-" + Random.name() + ".cer";
    private static final String EXPORTED_CSR_FILENAME = "exported-csr-" + Random.name() + ".cer";

    private static final String AGREE_TO_TERMS_OF_SERVICE = "agree-to-terms-of-service";
    private static final String STAGING = "staging";
    public static final String DOMAIN_NAMES = "domain-names";
    public static final String WWW_FOOBAR_COM = "www.foobar.com";

    @BeforeClass
    public static void setUp() throws IOException {
        ModelNode credentialReference = new ModelNode();
        credentialReference.get("clear-text").set(Random.name());
        // add a keystore
        operations.add(keyStoreAddress(KEY_STORE_NAME), Values.of(ModelDescriptionConstants.TYPE, JKS)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, credentialReference)
                .and(ModelDescriptionConstants.PATH, Random.name())
                .and(ModelDescriptionConstants.RELATIVE_TO, "jboss.server.config.dir"));
        // add a certificate-authority-account
        operations.add(certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE),
                Values.of(ElytronFixtures.CREDENTIAL_REFERENCE_ALIAS, Random.name())
                        .and(ModelDescriptionConstants.KEY_STORE, KEY_STORE_NAME).and("contact-urls", new ModelNodeGenerator.ModelNodeListBuilder()
                .addAll("mailto:example@org").build()));
        // generate and add a certificate to the keystore and export it to later run the test to import the certificate
        operations.add(keyStoreAddress(KEY_STORE_NAME2), Values.of(ModelDescriptionConstants.TYPE, JKS)
                .and(ElytronFixtures.CREDENTIAL_REFERENCE, credentialReference)
                .and(ModelDescriptionConstants.PATH, Random.name())
                .and(ModelDescriptionConstants.RELATIVE_TO, "jboss.server.config.dir"));
        operations.invoke(ModelDescriptionConstants.GENERATE_KEY_PAIR, keyStoreAddress(KEY_STORE_NAME2), Values.of(ModelDescriptionConstants.ALIAS, ALIAS_TO_RENAME)
                .and(ModelDescriptionConstants.DISTINGUISHED_NAME, "cn=selfsigned")
                .and(SIGNATURE_ALGORITHM, SHA_256_WITH_RSA)
                .and(ModelDescriptionConstants.ALGORITHM, RSA));
        operations.invoke(ModelDescriptionConstants.GENERATE_KEY_PAIR, keyStoreAddress(KEY_STORE_NAME2), Values.of(ModelDescriptionConstants.ALIAS, ALIAS_TRY_TO_RENAME)
                .and(ModelDescriptionConstants.DISTINGUISHED_NAME, "cn=selfsigned")
                .and(SIGNATURE_ALGORITHM, SHA_256_WITH_RSA)
                .and(ModelDescriptionConstants.ALGORITHM, RSA));
        operations.invoke(ModelDescriptionConstants.GENERATE_KEY_PAIR, keyStoreAddress(KEY_STORE_NAME2), Values.of(ModelDescriptionConstants.ALIAS, ALIAS_TO_EXPORT)
                .and(ModelDescriptionConstants.DISTINGUISHED_NAME, "cn=to-export")
                .and(SIGNATURE_ALGORITHM, SHA_256_WITH_RSA)
                .and(ModelDescriptionConstants.ALGORITHM, RSA));
        operations.invoke(ModelDescriptionConstants.GENERATE_KEY_PAIR, keyStoreAddress(KEY_STORE_NAME2), Values.of(ModelDescriptionConstants.ALIAS, ALIAS_TO_REMOVE)
                .and(ModelDescriptionConstants.DISTINGUISHED_NAME, "cn=to-remove")
                .and(SIGNATURE_ALGORITHM, SHA_256_WITH_RSA)
                .and(ModelDescriptionConstants.ALGORITHM, RSA));
        operations.invoke(ModelDescriptionConstants.STORE, keyStoreAddress(KEY_STORE_NAME2));
        operations.invoke(ModelDescriptionConstants.EXPORT_CERTIFICATE, keyStoreAddress(KEY_STORE_NAME2), Values.of(ModelDescriptionConstants.ALIAS, ALIAS_TO_RENAME)
                .and(ModelDescriptionConstants.PATH, EXPORTED_CERT_FILENAME));
        operations.invoke(ModelDescriptionConstants.OBTAIN_CERTIFICATE, keyStoreAddress(KEY_STORE_NAME2), Values.of(ModelDescriptionConstants.ALIAS, ALIAS_TO_REVOKE)
                .and(AGREE_TO_TERMS_OF_SERVICE, true)
                .and(ModelDescriptionConstants.CERTIFICATE_AUTHORITY_ACCOUNT, CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE)
                .andList(DOMAIN_NAMES, "www.foobar.com"));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(certificateAuthorityAccountAddress(CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE));
            operations.removeIfExists(keyStoreAddress(KEY_STORE_NAME));
            operations.removeIfExists(keyStoreAddress(KEY_STORE_NAME2));
        } finally {
            client.close();
        }
    }

    @Inject
    private Console console;

    @Page
    private ElytronRuntimeStoresPage page;

    @Before
    public void initPage() {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.ELYTRON_KEY_STORE);
    }

    @Test
    public void load() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Load").click();
        console.verifySuccess();
    }

    @Test
    public void store() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Store").click();
        console.verifySuccess();
    }

    @Test
    public void generateKeyPair() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Generate Key Pair").click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(ModelDescriptionConstants.ALIAS, ALIAS_GEN_KEY);
        dialog.getForm().text(ModelDescriptionConstants.DISTINGUISHED_NAME, "cn=" + ALIAS_GEN_KEY);
        dialog.getPrimaryButton().click();
        console.verifySuccess();
    }

    @Test
    public void tryGenerateKeyPair() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Generate Key Pair").click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError(ModelDescriptionConstants.ALIAS);
        dialog.getSecondaryButton().click();
    }

    @Test
    public void generateKeyPairFull() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Generate Key Pair").click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(ModelDescriptionConstants.ALGORITHM, RSA);
        dialog.getForm().text(ModelDescriptionConstants.ALIAS, ALIAS_GEN_KEY_FULL);
        dialog.getForm().text(ModelDescriptionConstants.DISTINGUISHED_NAME, "cn=" + ALIAS_GEN_KEY_FULL);
        dialog.getForm().number("key-size", 2048);
        dialog.getForm().text("not-before", "2019-01-29 11:11:11");
        dialog.getForm().text(SIGNATURE_ALGORITHM, SHA_256_WITH_RSA);
        dialog.getForm().number(ModelDescriptionConstants.VALIDITY, 250);
        dialog.getPrimaryButton().click();
        console.verifySuccess();
    }

    @Test
    public void obtainCertificateStaging() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Obtain").click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().flip(AGREE_TO_TERMS_OF_SERVICE, true);
        dialog.getForm().text(ModelDescriptionConstants.ALIAS, ALIAS_OBTAIN_STAGING);
        dialog.getForm().text(ModelDescriptionConstants.CERTIFICATE_AUTHORITY_ACCOUNT, CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        dialog.getForm().list(DOMAIN_NAMES).add(WWW_FOOBAR_COM);
        dialog.getForm().flip(STAGING, true);
        dialog.getPrimaryButton().click();
        console.verifySuccess();
    }

    @Test
    public void tryObtainCertificateStaging() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Obtain").click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().flip(AGREE_TO_TERMS_OF_SERVICE, true);
        dialog.getForm().text(ModelDescriptionConstants.ALIAS, ALIAS_OBTAIN_STAGING);
        dialog.getForm().text(ModelDescriptionConstants.CERTIFICATE_AUTHORITY_ACCOUNT, CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        dialog.getForm().flip(STAGING, true);
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError(DOMAIN_NAMES);
        dialog.getSecondaryButton().click();
    }

    @Test
    public void obtainCertificate() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Obtain").click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().flip(AGREE_TO_TERMS_OF_SERVICE, true);
        dialog.getForm().text(ModelDescriptionConstants.ALIAS, ALIAS_OBTAIN);
        dialog.getForm().text(ModelDescriptionConstants.CERTIFICATE_AUTHORITY_ACCOUNT, CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        dialog.getForm().list(DOMAIN_NAMES).add("www.foobar.com");
        dialog.getPrimaryButton().click();
        console.verifySuccess();
    }

    @Test
    public void importCertificate() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Import Certificate").click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(ModelDescriptionConstants.ALIAS, ALIAS_IMPORTED);
        dialog.getForm().text(ModelDescriptionConstants.PATH, EXPORTED_CERT_FILENAME);
        dialog.getForm().flip(ModelDescriptionConstants.TRUST_CACERTS, true);
        dialog.getForm().flip(ModelDescriptionConstants.VALIDATE, false);
        dialog.getPrimaryButton().click();
        console.verifySuccess();
    }

    @Test
    public void tryImportCertificate() {
        page.getKeyStoreTable().select(KEY_STORE_NAME);
        page.getKeyStoreTable().button("Import Certificate").click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError(ModelDescriptionConstants.ALIAS);
        dialog.getForm().expectError(ModelDescriptionConstants.PATH);
        dialog.getSecondaryButton().click();
    }

    //  -------------------- aliases operations

    @Test
    public void changeAlias() {
        page.getKeyStoreTable().action(KEY_STORE_NAME2, ALIASES);
        Graphene.waitGui().until().element(page.getKeyStoreAliasTable().getRoot()).is().visible();

        page.getKeyStoreAliasTable().select(ALIAS_TO_RENAME);
        page.getKeyStoreAliasTable().button("Change alias").click();

        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text("new-alias", ALIAS_RENAMED);
        dialog.getPrimaryButton().click();
        console.verifySuccess();
    }

    @Test
    public void tryChangeAlias() {
        page.getKeyStoreTable().action(KEY_STORE_NAME2, ALIASES);
        Graphene.waitGui().until().element(page.getKeyStoreAliasTable().getRoot()).is().visible();
        page.getKeyStoreAliasTable().select(ALIAS_TRY_TO_RENAME);
        page.getKeyStoreAliasTable().button("Change alias").click();
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError("new-alias");
        dialog.getSecondaryButton().click();
    }

    @Test
    public void exportAliasCertificate() {
        page.getKeyStoreTable().action(KEY_STORE_NAME2, ALIASES);
        Graphene.waitGui().until().element(page.getKeyStoreAliasTable().getRoot()).is().visible();

        page.getKeyStoreAliasTable().select(ALIAS_TO_EXPORT);
        page.getKeyStoreAliasTable().button("Export Certificate").click();

        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(ModelDescriptionConstants.PATH, EXPORTED_CERT_FILENAME2);
        dialog.getPrimaryButton().click();
        console.verifySuccess();
    }

    @Test
    public void tryExportAliasCertificate() {
        page.getKeyStoreTable().action(KEY_STORE_NAME2, ALIASES);
        Graphene.waitGui().until().element(page.getKeyStoreAliasTable().getRoot()).is().visible();

        page.getKeyStoreAliasTable().select(ALIAS_TO_EXPORT);
        page.getKeyStoreAliasTable().button("Export Certificate").click();

        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError(ModelDescriptionConstants.PATH);
        dialog.getSecondaryButton().click();
    }

    @Test
    public void generateAliasCSR() {
        page.getKeyStoreTable().action(KEY_STORE_NAME2, ALIASES);
        Graphene.waitGui().until().element(page.getKeyStoreAliasTable().getRoot()).is().visible();

        page.getKeyStoreAliasTable().select(ALIAS_TO_EXPORT);
        page.getKeyStoreAliasTable().button("Generate CSR").click();

        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(ModelDescriptionConstants.PATH, EXPORTED_CSR_FILENAME);
        dialog.getPrimaryButton().click();
        console.verifySuccess();
    }

    @Test
    public void removeAlias() {
        page.getKeyStoreTable().action(KEY_STORE_NAME2, ALIASES);
        Graphene.waitGui().until().element(page.getKeyStoreAliasTable().getRoot()).is().visible();

        page.getKeyStoreAliasTable().select(ALIAS_TO_REMOVE);
        page.getKeyStoreAliasTable().button("Remove Alias").click();

        ConfirmationDialogFragment dialog = console.confirmationDialog();
        dialog.confirm();
        console.verifySuccess();
    }

    @Test
    public void revokeAliasCertificate() {
        page.getKeyStoreTable().action(KEY_STORE_NAME2, ALIASES);
        Graphene.waitGui().until().element(page.getKeyStoreAliasTable().getRoot()).is().visible();

        page.getKeyStoreAliasTable().select(ALIAS_TO_REVOKE);
        page.getKeyStoreAliasTable().button("Revoke").click();

        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(ModelDescriptionConstants.CERTIFICATE_AUTHORITY_ACCOUNT, CERTIFICATE_AUTHORITY_ACCOUNT_UPDATE);
        dialog.getPrimaryButton().click();
        console.verifySuccess();
    }

    @Test
    public void verifyAliasRenew() {
        page.getKeyStoreTable().action(KEY_STORE_NAME2, ALIASES);
        Graphene.waitGui().until().element(page.getKeyStoreAliasTable().getRoot()).is().visible();

        page.getKeyStoreAliasTable().select(ALIAS_TO_EXPORT);
        page.getKeyStoreAliasTable().button("Verify Renew").click();

        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getPrimaryButton().click();
        console.confirmationDialog();
    }
}
