/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
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
package org.jboss.hal.testsuite.test.configuration.elytron.othersettings;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT_REALM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_MANAGER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_STORE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROVIDER_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALMS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RESULT;
import static org.jboss.hal.resources.Ids.ELYTRON_KEY_MANAGER;
import static org.jboss.hal.resources.Ids.ELYTRON_TRUST_MANAGER;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class SslSettingsTest extends AbstractOtherSettingsTest {

    // --------------- aggregate-providers

    @Test
    public void aggregateProvidersCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, AGGREGATE_PROVIDERS_ITEM);
        TableFragment table = page.getAggregateProvidersTable();

        crud.create(aggregateProvidersAddress(AGG_PRV_CREATE), table, f -> {
            f.text(NAME, AGG_PRV_CREATE);
            f.list(PROVIDERS).add(PROV_LOAD_UPDATE).add(PROV_LOAD_UPDATE2);
        });
    }

    @Test
    public void aggregateProvidersTryCreate() {
        console.verticalNavigation().selectSecondary(SSL_ITEM, AGGREGATE_PROVIDERS_ITEM);
        TableFragment table = page.getAggregateProvidersTable();

        crud.createWithErrorAndCancelDialog(table, AGG_PRV_CREATE, PROVIDERS);
    }

    @Test
    public void aggregateProvidersUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, AGGREGATE_PROVIDERS_ITEM);
        TableFragment table = page.getAggregateProvidersTable();
        FormFragment form = page.getAggregateProvidersForm();
        table.bind(form);
        table.select(AGG_PRV_UPDATE);

        crud.update(aggregateProvidersAddress(AGG_PRV_UPDATE), form, f -> f.list(PROVIDERS).add(PROV_LOAD_UPDATE3), verify -> verify.verifyListAttributeContainsValue(PROVIDERS, PROV_LOAD_UPDATE3));
    }

    @Test
    public void aggregateProvidersDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, AGGREGATE_PROVIDERS_ITEM);
        TableFragment table = page.getAggregateProvidersTable();

        crud.delete(aggregateProvidersAddress(AGG_PRV_DELETE), table, AGG_PRV_DELETE);
    }

    // --------------- client-ssl-context

    @Test
    public void clientSslContextCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, CLIENT_SSL_CONTEXT_ITEM);
        TableFragment table = page.getClientSslContextTable();

        crud.create(clientSslContextAddress(CLI_SSL_CREATE), table, CLI_SSL_CREATE);
    }

    @Test
    public void clientSslContextUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, CLIENT_SSL_CONTEXT_ITEM);
        TableFragment table = page.getClientSslContextTable();
        FormFragment form = page.getClientSslContextForm();
        table.bind(form);
        table.select(CLI_SSL_UPDATE);

        crud.update(clientSslContextAddress(CLI_SSL_UPDATE), form, PROVIDER_NAME);
    }

    @Test
    public void clientSslContextDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, CLIENT_SSL_CONTEXT_ITEM);
        TableFragment table = page.getClientSslContextTable();

        crud.delete(clientSslContextAddress(CLI_SSL_DELETE), table, CLI_SSL_DELETE);
    }

    // --------------- key-manager

    @Test
    public void keyManagerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();

        crud.create(keyManagerAddress(KEY_MAN_CREATE), table, f -> {
            f.text(NAME, KEY_MAN_CREATE);
            f.text(KEY_STORE, KEY_ST_UPDATE);
            f.text(CLEAR_TEXT, ANY_STRING);
        });
    }

    @Test
    public void keyManagerTryCreate() {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();

        crud.createWithErrorAndCancelDialog(table, KEY_MAN_CREATE, KEY_STORE);
    }

    @Test
    public void keyManagerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();
        FormFragment form = page.getKeyManagerForm();
        table.bind(form);
        table.select(KEY_MAN_UPDATE);
        page.getKeyManagerTab().select(Ids.build(ELYTRON_KEY_MANAGER, ATTRIBUTES, TAB));

        crud.update(keyManagerAddress(KEY_MAN_UPDATE), form, PROVIDER_NAME);
    }

    @Test
    public void keyManagerTryUpdate() {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();
        FormFragment form = page.getKeyManagerForm();
        table.bind(form);
        table.select(KEY_MAN_TRY_UPDATE);
        page.getKeyManagerTab().select(Ids.build(ELYTRON_KEY_MANAGER, ATTRIBUTES, TAB));

        crud.updateWithError(form, f -> f.clear(KEY_STORE), KEY_STORE);
    }

    @Test
    public void keyManagerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();

        crud.delete(keyManagerAddress(KEY_MAN_DELETE), table, KEY_MAN_DELETE);
    }

    @Test
    public void keyManagerCredentialReferenceUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();
        FormFragment form = page.getKeyManagerCredentialReferenceForm();
        table.bind(form);
        table.select(KEY_MAN_UPDATE);
        page.getKeyManagerTab().select(Ids.build(ELYTRON_KEY_MANAGER, CREDENTIAL_REFERENCE, TAB));
        crud.update(keyManagerAddress(KEY_MAN_UPDATE), form, f -> f.text(CLEAR_TEXT, ANY_STRING),
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + PROPERTY_DELIMITER + CLEAR_TEXT, ANY_STRING));
    }

    // --------------- provider-loader

    @Test
    public void providerLoaderCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, PROVIDER_LOADER_ITEM);
        TableFragment table = page.getProviderLoaderTable();

        crud.create(providerLoaderAddress(PROV_LOAD_CREATE), table, PROV_LOAD_CREATE);
    }

    @Test
    public void providerLoaderUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, PROVIDER_LOADER_ITEM);
        TableFragment table = page.getProviderLoaderTable();
        FormFragment form = page.getProviderLoaderForm();
        table.bind(form);
        table.select(PROV_LOAD_UPDATE);
        crud.update(providerLoaderAddress(PROV_LOAD_UPDATE), form, PATH, ANY_STRING);
    }

    @Test
    public void providerLoaderDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, PROVIDER_LOADER_ITEM);
        TableFragment table = page.getProviderLoaderTable();

        crud.delete(providerLoaderAddress(PROV_LOAD_DELETE), table, PROV_LOAD_DELETE);
    }

    // --------------- provider-loader

    @Test
    public void serverSslContextCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SERVER_SSL_CONTEXT_ITEM);
        TableFragment table = page.getServerSslContextTable();

        crud.create(serverSslContextAddress(SRV_SSL_CREATE), table, f -> {
            f.text(NAME, SRV_SSL_CREATE);
            f.text(KEY_MANAGER, KEY_MAN_UPDATE);
        });
    }

    @Test
    public void serverSslContextTryCreate() {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SERVER_SSL_CONTEXT_ITEM);
        TableFragment table = page.getServerSslContextTable();

        crud.createWithErrorAndCancelDialog(table, SRV_SSL_CREATE, KEY_MANAGER);
    }

    @Test
    public void serverSslContextUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SERVER_SSL_CONTEXT_ITEM);
        TableFragment table = page.getServerSslContextTable();
        FormFragment form = page.getServerSslContextForm();
        table.bind(form);
        table.select(SRV_SSL_UPDATE);

        crud.update(serverSslContextAddress(SRV_SSL_UPDATE), form, PROVIDER_NAME, ANY_STRING);
    }

    @Test
    public void serverSslContextTryUpdate() {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SERVER_SSL_CONTEXT_ITEM);
        TableFragment table = page.getServerSslContextTable();
        FormFragment form = page.getServerSslContextForm();
        table.bind(form);
        table.select(SRV_SSL_UPDATE);

        crud.updateWithError(form, f -> f.clear(KEY_MANAGER), KEY_MANAGER);
    }

    @Test
    public void serverSslContextDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SERVER_SSL_CONTEXT_ITEM);
        TableFragment table = page.getServerSslContextTable();

        crud.delete(serverSslContextAddress(SRV_SSL_DELETE), table, SRV_SSL_DELETE);
    }

    // --------------- security-domain

    @Test
    public void securityDomainCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SECURITY_DOMAIN_ITEM);
        TableFragment table = page.getSecurityDomainTable();

        crud.create(securityDomainAddress(SEC_DOM_CREATE), table, f -> {
            f.text(NAME, SEC_DOM_CREATE);
            f.text(DEFAULT_REALM, FILESYS_RLM_UPDATE);
        });
    }

    @Test
    public void securityDomainUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SECURITY_DOMAIN_ITEM);
        TableFragment table = page.getSecurityDomainTable();
        FormFragment form = page.getSecurityDomainForm();
        table.bind(form);
        table.select(SEC_DOM_UPDATE);
        crud.update(securityDomainAddress(SEC_DOM_UPDATE), form,
                f -> f.list(OUTFLOW_SECURITY_DOMAINS).add(SEC_DOM_UPDATE2),
                verify -> verify.verifyListAttributeContainsValue(OUTFLOW_SECURITY_DOMAINS, SEC_DOM_UPDATE2));
    }

    @Test
    public void securityDomainDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SECURITY_DOMAIN_ITEM);
        TableFragment table = page.getSecurityDomainTable();
        crud.delete(securityDomainAddress(SEC_DOM_DELETE), table, SEC_DOM_DELETE);
    }

    @Test
    public void securityDomainRealmsCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SECURITY_DOMAIN_ITEM);
        TableFragment secDomaintable = page.getSecurityDomainTable();
        TableFragment table = page.getSecurityDomainRealmsTable();

        secDomaintable.action(SEC_DOM_UPDATE, ElytronFixtures.REALMS);
        waitGui().until().element(table.getRoot()).is().visible();

        try {
            crud.create(securityDomainAddress(SEC_DOM_UPDATE), table, f -> f.text(REALM, FILESYS_RLM_CREATE),
                    vc -> vc.verifyListAttributeContainsSingleValue(REALMS, REALM, FILESYS_RLM_CREATE));
        } finally {
            // getting rid of action selection
            page.getSecurityDomainPages().breadcrumb().getBackToMainPage();
        }
    }

    @Test
    public void securityDomainRealmsTryCreate() {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SECURITY_DOMAIN_ITEM);
        TableFragment secDomaintable = page.getSecurityDomainTable();
        TableFragment table = page.getSecurityDomainRealmsTable();

        secDomaintable.action(SEC_DOM_UPDATE, ElytronFixtures.REALMS);
        waitGui().until().element(table.getRoot()).is().visible();

        try {
            crud.createWithErrorAndCancelDialog(table, f -> f.text("role-decoder", ANY_STRING), REALM);
        } finally {
            // getting rid of action selection
            page.getSecurityDomainPages().breadcrumb().getBackToMainPage();
        }
    }

    @Test
    public void securityDomainRealmsUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SECURITY_DOMAIN_ITEM);
        TableFragment secDomaintable = page.getSecurityDomainTable();
        TableFragment table = page.getSecurityDomainRealmsTable();
        FormFragment form = page.getSecurityDomainRealmsForm();

        secDomaintable.action(SEC_DOM_UPDATE2, ElytronFixtures.REALMS);
        waitGui().until().element(table.getRoot()).is().visible();
        table.bind(form);
        table.select(FILESYS_RLM_UPDATE);

        try {
            crud.update(securityDomainAddress(SEC_DOM_UPDATE2), form,
                    f -> f.text(PRINCIPAL_TRANSFORMER, CONS_PRI_TRANS_UPDATE),
                    vc -> vc.verifyListAttributeContainsSingleValue(REALMS, PRINCIPAL_TRANSFORMER, CONS_PRI_TRANS_UPDATE));
        } finally {
            // getting rid of action selection
            page.getSecurityDomainPages().breadcrumb().getBackToMainPage();
        }
    }

    @Test
    public void securityDomainRealmsDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SECURITY_DOMAIN_ITEM);
        TableFragment secDomaintable = page.getSecurityDomainTable();
        TableFragment table = page.getSecurityDomainRealmsTable();

        secDomaintable.action(SEC_DOM_UPDATE3, ElytronFixtures.REALMS);
        waitGui().until().element(table.getRoot()).is().visible();

        try {
            crud.delete(securityDomainAddress(SEC_DOM_UPDATE3), table, FILESYS_RLM_CREATE,
                    vc -> vc.verifyListAttributeDoesNotContainSingleValue(REALMS, REALM, FILESYS_RLM_CREATE));
        } finally {
            // getting rid of action selection
            page.getSecurityDomainPages().breadcrumb().getBackToMainPage();
        }
    }

    // --------------- trust-manager

    @Test
    public void trustManagerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();

        crud.create(trustManagerAddress(TRU_MAN_CREATE), table, f -> {
            f.text(NAME, TRU_MAN_CREATE);
            f.text(KEY_STORE, KEY_ST_UPDATE);
        });
    }

    @Test
    public void trustManagerTryCreate() {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        crud.createWithErrorAndCancelDialog(table, TRU_MAN_CREATE, KEY_STORE);
    }

    @Test
    public void trustManagerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        FormFragment form = page.getTrustManagerForm();
        table.bind(form);
        table.select(TRU_MAN_UPDATE);
        page.getTrustManagerTab().select(Ids.build(ELYTRON_TRUST_MANAGER, ATTRIBUTES, TAB));
        crud.update(trustManagerAddress(TRU_MAN_UPDATE), form, f -> f.text(PROVIDER_NAME, ANY_STRING),
                verify -> verify.verifyAttribute(PROVIDER_NAME, ANY_STRING));
    }

    @Test
    public void trustManagerCertificateRevocationListAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        FormFragment form = page.getTrustManagerCertificateRevocationListForm();
        table.bind(form);
        table.select(TRU_MAN_CRL_CRT);
        page.getTrustManagerTab().select(Ids.build(ELYTRON_TRUST_MANAGER, CERTIFICATE_REVOCATION_LIST, TAB));
        form.emptyState().mainAction();
        console.verifySuccess();
        // the UI "add" operation adds a certificate-revocation-list with no inner attributes, as they are not required
        ModelNodeResult actualResult = operations.readAttribute(trustManagerAddress(TRU_MAN_CRL_CRT),
                CERTIFICATE_REVOCATION_LIST);
        Assert.assertTrue("attribute certificate-revocation-list should exist", actualResult.get(RESULT).isDefined());
    }

    @Test
    public void trustManagerCertificateRevocationListUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        FormFragment form = page.getTrustManagerCertificateRevocationListForm();
        table.bind(form);
        table.select(TRU_MAN_CRL_UPD);
        page.getTrustManagerTab().select(Ids.build(ELYTRON_TRUST_MANAGER, CERTIFICATE_REVOCATION_LIST, TAB));
        crud.update(trustManagerAddress(TRU_MAN_CRL_UPD), form, f -> f.text(PATH, ANY_STRING),
                verify -> verify.verifyAttribute(CERTIFICATE_REVOCATION_LIST + PROPERTY_DELIMITER + PATH, ANY_STRING));
    }

    @Test
    public void trustManagerCertificateRevocationListDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        FormFragment form = page.getTrustManagerCertificateRevocationListForm();
        table.bind(form);
        table.select(TRU_MAN_CRL_DEL);
        page.getTrustManagerTab().select(Ids.build(ELYTRON_TRUST_MANAGER, CERTIFICATE_REVOCATION_LIST, TAB));
        crud.deleteSingleton(trustManagerAddress(TRU_MAN_CRL_DEL), form,
                verify -> verify.verifyAttributeIsUndefined(CERTIFICATE_REVOCATION_LIST));
    }

    @Test
    public void trustManagerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        crud.delete(trustManagerAddress(TRU_MAN_DELETE), table, TRU_MAN_DELETE);
    }
}
