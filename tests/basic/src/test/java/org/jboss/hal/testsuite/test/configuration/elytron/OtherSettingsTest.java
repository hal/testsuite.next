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
package org.jboss.hal.testsuite.test.configuration.elytron;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronOtherSettingsPage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALMS;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.Selectors.contains;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class OtherSettingsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeTests() throws Exception {

        // used in key-store, as trust-manager requires a key-store with providers attribute set
        operations.add(providerLoaderAddress(PROV_LOAD_UPDATE));
        operations.add(providerLoaderAddress(PROV_LOAD_UPDATE2));
        operations.add(providerLoaderAddress(PROV_LOAD_UPDATE3));
        operations.add(providerLoaderAddress(PROV_LOAD_DELETE));

        // Stores
        ModelNode credRef = new ModelNode();
        credRef.get(CLEAR_TEXT).set(ANY_STRING);
        Values credParams = Values.of(CREATE, true).and(CREDENTIAL_REFERENCE, credRef);
        operations.add(credentialStoreAddress(CRED_ST_UPDATE), credParams);
        operations.add(credentialStoreAddress(CRED_ST_DELETE), credParams);

        Values ksParams = Values.of(TYPE, JKS).and(CREDENTIAL_REFERENCE, credRef);
        operations.add(keyStoreAddress(KEY_ST_UPDATE), ksParams);
        operations.add(keyStoreAddress(KEY_ST_CR_UPDATE), ksParams);
        operations.add(keyStoreAddress(KEY_ST_DELETE), ksParams);
        operations.writeAttribute(keyStoreAddress(KEY_ST_UPDATE), PROVIDERS, PROV_LOAD_UPDATE);

        operations.add(filteringKeyStoreAddress(FILT_ST_DELETE),
                Values.of(ALIAS_FILTER, ANY_STRING).and(KEY_STORE, KEY_ST_UPDATE));
        operations.add(filteringKeyStoreAddress(FILT_ST_UPDATE),
                Values.of(ALIAS_FILTER, ANY_STRING).and(KEY_STORE, KEY_ST_UPDATE));

        operations.add(dirContextAddress(DIR_UPDATE), Values.of(URL, ANY_STRING));
        operations.add(dirContextAddress(DIR_DELETE), Values.of(URL, ANY_STRING));

        Values dirCtxParams = Values.of(URL, ANY_STRING)
                .andObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, ANY_STRING));
        operations.add(dirContextAddress(DIR_CR_CRT), Values.of(URL, ANY_STRING));
        operations.add(dirContextAddress(DIR_CR_UPD), dirCtxParams);
        operations.add(dirContextAddress(DIR_CR_DEL), dirCtxParams);

        Values ldapKsValues = Values.of(DIR_CONTEXT, DIR_UPDATE).and(SEARCH_PATH, ANY_STRING);
        ModelNode props = new ModelNode();
        props.get(NAME).set("p1");
        props.get(VALUE).add(Random.name());
        ModelNode newItemTemplate = new ModelNode();
        newItemTemplate.get(NEW_ITEM_PATH).set(ANY_STRING);
        newItemTemplate.get(NEW_ITEM_RDN).set(ANY_STRING);
        newItemTemplate.get(NEW_ITEM_ATTRIBUTES).add(props);

        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_DELETE), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_TEMP1_UPDATE), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_TEMP2_DELETE), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_TEMP3_ADD), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_TEMP4_TRY_ADD), ldapKsValues);
        operations.writeAttribute(ldapKeyStoreAddress(LDAPKEY_ST_TEMP1_UPDATE), NEW_ITEM_TEMPLATE, newItemTemplate);
        operations.writeAttribute(ldapKeyStoreAddress(LDAPKEY_ST_TEMP2_DELETE), NEW_ITEM_TEMPLATE, newItemTemplate);

        // SSL
        Values aggValues = Values.ofList(PROVIDERS, PROV_LOAD_UPDATE, PROV_LOAD_UPDATE2);
        operations.add(aggregateProvidersAddress(AGG_PRV_DELETE), aggValues);
        operations.add(aggregateProvidersAddress(AGG_PRV_UPDATE), aggValues);

        operations.add(clientSslContextAddress(CLI_SSL_DELETE));
        operations.add(clientSslContextAddress(CLI_SSL_UPDATE));

        Values keyManagerValues = Values.of(KEY_STORE, KEY_ST_UPDATE)
                .andObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, ANY_STRING));
        operations.add(keyManagertAddress(KEY_MAN_UPDATE), keyManagerValues);
        operations.add(keyManagertAddress(KEY_MAN_TRY_UPDATE), keyManagerValues);
        operations.add(keyManagertAddress(KEY_MAN_DELETE), keyManagerValues);

        Values serverSslContextValues = Values.of(KEY_MANAGER, KEY_MAN_UPDATE);
        operations.add(serverSslContextAddress(SRV_SSL_DELETE), serverSslContextValues);
        operations.add(serverSslContextAddress(SRV_SSL_UPDATE), serverSslContextValues);

        // a realm is required for new security-domain
        operations.add(filesystemRealmAddress(FILESYS_RLM_CREATE), Values.of(PATH, ANY_STRING));
        operations.add(filesystemRealmAddress(FILESYS_RLM_UPDATE), Values.of(PATH, ANY_STRING));
        ModelNode realmNode1 = new ModelNode();
        realmNode1.get(REALM).set(FILESYS_RLM_UPDATE);
        ModelNode realmNode2 = new ModelNode();
        realmNode2.get(REALM).set(FILESYS_RLM_CREATE);
        Values secDomainParams = Values.of(DEFAULT_REALM, FILESYS_RLM_UPDATE).andList(REALMS, realmNode1);
        operations.add(securityDomainAddress(SEC_DOM_UPDATE), secDomainParams);
        operations.add(securityDomainAddress(SEC_DOM_UPDATE2), secDomainParams);
        operations.add(securityDomainAddress(SEC_DOM_UPDATE3),
                Values.of(DEFAULT_REALM, FILESYS_RLM_UPDATE).andList(REALMS, realmNode1, realmNode2));
        operations.add(securityDomainAddress(SEC_DOM_DELETE));

        operations.add(trustManagertAddress(TRU_MAN_UPDATE), Values.of(KEY_STORE, KEY_ST_UPDATE));
        operations.add(trustManagertAddress(TRU_MAN_DELETE), Values.of(KEY_STORE, KEY_ST_UPDATE));

        Values trustParams = Values.of(KEY_STORE, KEY_ST_UPDATE).andObject(CERTIFICATE_REVOCATION_LIST,
                        Values.of(PATH, "${jboss.server.config.dir}/logging.properties"));
        operations.add(trustManagertAddress(TRU_MAN_CRL_CRT), Values.of(KEY_STORE, KEY_ST_UPDATE));
        operations.add(trustManagertAddress(TRU_MAN_CRL_UPD), trustParams);
        operations.add(trustManagertAddress(TRU_MAN_CRL_DEL), trustParams);

        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE), Values.of(CONSTANT, ANY_STRING));

        operations.add(authenticationConfigurationAddress(AUT_CF_UPDATE));
        operations.add(authenticationConfigurationAddress(AUT_CF_DELETE));

        Values autParams = Values.ofObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, ANY_STRING));
        operations.add(authenticationConfigurationAddress(AUT_CF_CR_CRT));
        operations.add(authenticationConfigurationAddress(AUT_CF_CR_UPD), autParams);
        operations.add(authenticationConfigurationAddress(AUT_CF_CR_DEL), autParams);

        operations.add(authenticationContextAddress(AUT_CT_DELETE));
        operations.add(authenticationContextAddress(AUT_CT_UPDATE));
        ModelNode matchRuleUpdate = new ModelNode();
        matchRuleUpdate.get(MATCH_ABSTRACT_TYPE).set(AUT_CT_MR_UPDATE);
        ModelNode matchRuleDelete = new ModelNode();
        matchRuleDelete.get(MATCH_ABSTRACT_TYPE).set(AUT_CT_MR_DELETE);
        operations.add(authenticationContextAddress(AUT_CT_UPDATE2),
                Values.ofList(MATCH_RULES, matchRuleUpdate, matchRuleDelete));

        operations.add(fileAuditLogAddress(FILE_LOG_DELETE), Values.of(PATH, ANY_STRING));
        operations.add(fileAuditLogAddress(FILE_LOG_UPDATE), Values.of(PATH, ANY_STRING));
        operations.add(fileAuditLogAddress(FILE_LOG_TRY_UPDATE), Values.of(PATH, ANY_STRING));

        Values params = Values.of(PATH, ANY_STRING).and(SUFFIX, SUFFIX_LOG);
        operations.add(periodicRotatingFileAuditLogAddress(PER_LOG_DELETE), params);
        operations.add(periodicRotatingFileAuditLogAddress(PER_LOG_UPDATE), params);
        operations.add(periodicRotatingFileAuditLogAddress(PER_LOG_TRY_UPDATE), params);

        operations.add(sizeRotatingFileAuditLogAddress(SIZ_LOG_DELETE), Values.of(PATH, ANY_STRING));
        operations.add(sizeRotatingFileAuditLogAddress(SIZ_LOG_UPDATE), Values.of(PATH, ANY_STRING));

        Values syslogParams = Values.of(HOSTNAME, ANY_STRING).and(PORT, Random.number()).and(SERVER_ADDRESS, LOCALHOST);
        operations.add(syslogAuditLogAddress(SYS_LOG_UPDATE), syslogParams);
        operations.add(syslogAuditLogAddress(SYS_LOG_TRY_UPDATE), syslogParams);
        operations.add(syslogAuditLogAddress(SYS_LOG_DELETE), syslogParams);

        Values secEventParams = Values.ofList(SECURITY_EVENT_LISTENERS, SYS_LOG_UPDATE, SIZ_LOG_UPDATE);
        operations.add(aggregateSecurityEventListenerAddress(AGG_SEC_UPDATE), secEventParams);
        operations.add(aggregateSecurityEventListenerAddress(AGG_SEC_DELETE), secEventParams);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // Stores
        operations.remove(credentialStoreAddress(CRED_ST_DELETE));
        operations.remove(credentialStoreAddress(CRED_ST_UPDATE));
        operations.remove(credentialStoreAddress(CRED_ST_CREATE));

        operations.remove(filteringKeyStoreAddress(FILT_ST_DELETE));
        operations.remove(filteringKeyStoreAddress(FILT_ST_UPDATE));
        operations.remove(filteringKeyStoreAddress(FILT_ST_CREATE));

        operations.remove(keyStoreAddress(KEY_ST_CREATE));
        operations.remove(keyStoreAddress(KEY_ST_DELETE));

        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_DELETE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_TEMP1_UPDATE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_TEMP2_DELETE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_TEMP3_ADD));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_TEMP4_TRY_ADD));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_CREATE));

        operations.remove(dirContextAddress(DIR_UPDATE));
        operations.remove(dirContextAddress(DIR_DELETE));
        operations.remove(dirContextAddress(DIR_CREATE));
        operations.remove(dirContextAddress(DIR_CR_CRT));
        operations.remove(dirContextAddress(DIR_CR_UPD));
        operations.remove(dirContextAddress(DIR_CR_DEL));

        // SSL
        operations.remove(aggregateProvidersAddress(AGG_PRV_DELETE));
        operations.remove(aggregateProvidersAddress(AGG_PRV_UPDATE));
        operations.remove(aggregateProvidersAddress(AGG_PRV_CREATE));

        operations.remove(clientSslContextAddress(CLI_SSL_UPDATE));
        operations.remove(clientSslContextAddress(CLI_SSL_CREATE));
        operations.remove(clientSslContextAddress(CLI_SSL_DELETE));

        // remove the server-ssl-context before removing key-manager
        operations.remove(serverSslContextAddress(SRV_SSL_UPDATE));
        operations.remove(serverSslContextAddress(SRV_SSL_CREATE));
        operations.remove(serverSslContextAddress(SRV_SSL_DELETE));

        operations.remove(keyManagertAddress(KEY_MAN_CREATE));
        operations.remove(keyManagertAddress(KEY_MAN_UPDATE));
        operations.remove(keyManagertAddress(KEY_MAN_TRY_UPDATE));
        operations.remove(keyManagertAddress(KEY_MAN_DELETE));

        operations.remove(securityDomainAddress(SEC_DOM_UPDATE));
        operations.remove(securityDomainAddress(SEC_DOM_UPDATE2));
        operations.remove(securityDomainAddress(SEC_DOM_UPDATE3));
        operations.remove(securityDomainAddress(SEC_DOM_DELETE));
        operations.remove(securityDomainAddress(SEC_DOM_CREATE));

        operations.remove(trustManagertAddress(TRU_MAN_UPDATE));
        operations.remove(trustManagertAddress(TRU_MAN_CREATE));
        operations.remove(trustManagertAddress(TRU_MAN_DELETE));
        operations.remove(trustManagertAddress(TRU_MAN_CRL_CRT));
        operations.remove(trustManagertAddress(TRU_MAN_CRL_UPD));
        operations.remove(trustManagertAddress(TRU_MAN_CRL_DEL));

        // key-store is a dependency on key-manager and trust-manager, remove it after key-manager and trust-manager
        operations.remove(keyStoreAddress(KEY_ST_UPDATE));
        operations.remove(keyStoreAddress(KEY_ST_CR_UPDATE));

        operations.remove(providerLoaderAddress(PROV_LOAD_UPDATE));
        operations.remove(providerLoaderAddress(PROV_LOAD_UPDATE2));
        operations.remove(providerLoaderAddress(PROV_LOAD_UPDATE3));
        operations.remove(providerLoaderAddress(PROV_LOAD_CREATE));
        operations.remove(providerLoaderAddress(PROV_LOAD_DELETE));

        operations.remove(filesystemRealmAddress(FILESYS_RLM_UPDATE));
        operations.remove(filesystemRealmAddress(FILESYS_RLM_CREATE));

        operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE));

        operations.remove(authenticationContextAddress(AUT_CT_UPDATE));
        operations.remove(authenticationContextAddress(AUT_CT_UPDATE2));
        operations.remove(authenticationContextAddress(AUT_CT_DELETE));
        operations.remove(authenticationContextAddress(AUT_CT_CREATE));

        operations.remove(authenticationConfigurationAddress(AUT_CF_CREATE));
        operations.remove(authenticationConfigurationAddress(AUT_CF_UPDATE));
        operations.remove(authenticationConfigurationAddress(AUT_CF_DELETE));
        operations.remove(authenticationConfigurationAddress(AUT_CF_CR_CRT));
        operations.remove(authenticationConfigurationAddress(AUT_CF_CR_UPD));
        operations.remove(authenticationConfigurationAddress(AUT_CF_CR_DEL));

        operations.remove(fileAuditLogAddress(FILE_LOG_DELETE));
        operations.remove(fileAuditLogAddress(FILE_LOG_UPDATE));
        operations.remove(fileAuditLogAddress(FILE_LOG_TRY_UPDATE));
        operations.remove(fileAuditLogAddress(FILE_LOG_CREATE));

        // remove the aggregate-security-event-listener first, as they require size audit log and syslog
        operations.remove(aggregateSecurityEventListenerAddress(AGG_SEC_UPDATE));
        operations.remove(aggregateSecurityEventListenerAddress(AGG_SEC_CREATE));
        operations.remove(aggregateSecurityEventListenerAddress(AGG_SEC_DELETE));

        operations.remove(periodicRotatingFileAuditLogAddress(PER_LOG_UPDATE));
        operations.remove(periodicRotatingFileAuditLogAddress(PER_LOG_TRY_UPDATE));
        operations.remove(periodicRotatingFileAuditLogAddress(PER_LOG_DELETE));
        operations.remove(periodicRotatingFileAuditLogAddress(PER_LOG_CREATE));

        operations.remove(sizeRotatingFileAuditLogAddress(SIZ_LOG_DELETE));
        operations.remove(sizeRotatingFileAuditLogAddress(SIZ_LOG_UPDATE));
        operations.remove(sizeRotatingFileAuditLogAddress(SIZ_LOG_CREATE));

        operations.remove(syslogAuditLogAddress(SYS_LOG_DELETE));
        operations.remove(syslogAuditLogAddress(SYS_LOG_CREATE));
        operations.remove(syslogAuditLogAddress(SYS_LOG_UPDATE));
        operations.remove(syslogAuditLogAddress(SYS_LOG_TRY_UPDATE));

        operations.remove(policyAddress(POL_CREATE));

    }

    @Page private ElytronOtherSettingsPage page;
    @Inject private Console console;
    @Inject private CrudOperations crud;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    // --------------- credential store

    @Test
    public void credentialStoreCreate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, CREDENTIAL_STORE_ITEM);
        TableFragment table = page.getCredentialStoreTable();

        crud.create(credentialStoreAddress(CRED_ST_CREATE), table, f -> {
            f.text(NAME, CRED_ST_CREATE);
            f.flip(CREATE, true);
            f.text(CLEAR_TEXT, ANY_STRING);
        });
    }

    @Test
    public void credentialStoreTryCreate() {
        console.verticalNavigation().selectSecondary(STORES_ITEM, CREDENTIAL_STORE_ITEM);
        TableFragment table = page.getCredentialStoreTable();

        crud.createWithErrorAndCancelDialog(table, f -> {
            f.text(NAME, CRED_ST_CREATE);
            f.flip(CREATE, true);
        }, CLEAR_TEXT);
    }

    @Test
    public void credentialStoreUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, CREDENTIAL_STORE_ITEM);
        TableFragment table = page.getCredentialStoreTable();
        FormFragment form = page.getCredentialStoreForm();
        table.bind(form);
        table.select(CRED_ST_UPDATE);
        page.getCredentialStoreTab().select(Ids.build(ELYTRON_CREDENTIAL_STORE, ATTRIBUTES, TAB));
        crud.update(credentialStoreAddress(CRED_ST_UPDATE), form, PROVIDER_NAME, ANY_STRING);
    }

    @Test
    public void credentialStoreCredentialReferenceUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, CREDENTIAL_STORE_ITEM);
        TableFragment table = page.getCredentialStoreTable();
        FormFragment form = page.getCredentialStoreCredentialReferenceForm();
        table.bind(form);
        table.select(CRED_ST_UPDATE);
        page.getCredentialStoreTab().select(Ids.build(ELYTRON_CREDENTIAL_STORE, CREDENTIAL_REFERENCE, TAB));
        crud.update(credentialStoreAddress(CRED_ST_UPDATE), form, f -> f.text(CLEAR_TEXT, ANY_STRING),
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + "." + CLEAR_TEXT, ANY_STRING));
    }

    @Test
    public void credentialStoreDelete() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, CREDENTIAL_STORE_ITEM);
        TableFragment table = page.getCredentialStoreTable();

        crud.delete(credentialStoreAddress(CRED_ST_DELETE), table, CRED_ST_DELETE);
    }

    // --------------- filtering key store

    @Test
    public void filteringKeyStoreCreate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, FILTERING_KEY_STORE_ITEM);
        TableFragment table = page.getFilteringKeyStoreTable();

        crud.create(filteringKeyStoreAddress(FILT_ST_CREATE), table, f -> {
            f.text(NAME, FILT_ST_CREATE);
            f.text(ALIAS_FILTER, ANY_STRING);
            f.text(KEY_STORE, KEY_ST_UPDATE);
        });
    }

    @Test
    public void filteringKeyStoreTryCreate() {
        console.verticalNavigation().selectSecondary(STORES_ITEM, FILTERING_KEY_STORE_ITEM);
        TableFragment table = page.getFilteringKeyStoreTable();

        crud.createWithErrorAndCancelDialog(table, f -> {
            f.text(NAME, FILT_ST_CREATE);
            f.text(ALIAS_FILTER, ANY_STRING);
        }, KEY_STORE);
    }

    @Test
    public void filteringKeyStoreUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, FILTERING_KEY_STORE_ITEM);
        TableFragment table = page.getFilteringKeyStoreTable();
        FormFragment form = page.getFilteringKeyStoreForm();
        table.bind(form);
        table.select(FILT_ST_UPDATE);

        crud.update(filteringKeyStoreAddress(FILT_ST_UPDATE), form, ALIAS_FILTER);
    }

    @Test
    public void filteringKeyStoreDelete() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, FILTERING_KEY_STORE_ITEM);
        TableFragment table = page.getFilteringKeyStoreTable();

        crud.delete(filteringKeyStoreAddress(FILT_ST_DELETE), table, FILT_ST_DELETE);
    }

    // --------------- key store

    @Test
    public void keyStoreCreate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, KEY_STORE_ITEM);
        TableFragment table = page.getKeyStoreTable();

        crud.create(keyStoreAddress(KEY_ST_CREATE), table, f -> {
            f.text(NAME, KEY_ST_CREATE);
            f.text(TYPE, JKS);
            f.text(CLEAR_TEXT, ANY_STRING);
        });
    }

    @Test
    public void keyStoreTryCreate() {
        console.verticalNavigation().selectSecondary(STORES_ITEM, KEY_STORE_ITEM);
        TableFragment table = page.getKeyStoreTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, KEY_ST_CREATE), TYPE);
    }

    @Test
    public void keyStoreUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, KEY_STORE_ITEM);
        page.getKeyStoreTab().select(Ids.build(ELYTRON_KEY_STORE, ATTRIBUTES, TAB));
        TableFragment table = page.getKeyStoreTable();
        FormFragment form = page.getKeyStoreForm();
        table.bind(form);
        table.select(KEY_ST_UPDATE);
        crud.update(keyStoreAddress(KEY_ST_UPDATE), form, PATH);
    }

    @Test
    public void keyStoreCredentialReferenceUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, KEY_STORE_ITEM);
        TableFragment table = page.getKeyStoreTable();
        FormFragment form = page.getKeyStoreCredentialReferenceForm();
        table.bind(form);
        table.select(KEY_ST_CR_UPDATE);
        page.getKeyStoreTab().select(Ids.build(ELYTRON_KEY_STORE, CREDENTIAL_REFERENCE, TAB));
        crud.update(keyStoreAddress(KEY_ST_UPDATE), form, f -> f.text(CLEAR_TEXT, ANY_STRING),
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + "." + CLEAR_TEXT, ANY_STRING));
    }

    @Test
    public void keyStoreDelete() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, KEY_STORE_ITEM);
        TableFragment table = page.getKeyStoreTable();

        crud.delete(keyStoreAddress(KEY_ST_DELETE), table, KEY_ST_DELETE);
    }

    // --------------- ldap key store

    @Test
    public void ldapKeyStoreCreate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();

        crud.create(ldapKeyStoreAddress(LDAPKEY_ST_CREATE), table, f -> {
            f.text(NAME, LDAPKEY_ST_CREATE);
            f.text(DIR_CONTEXT, DIR_UPDATE);
            f.text(SEARCH_PATH, ANY_STRING);
        });
    }

    @Test
    public void ldapKeyStoreTryCreate() {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, LDAPKEY_ST_CREATE), DIR_CONTEXT);
    }

    @Test
    public void ldapKeyStoreUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();
        FormFragment form = page.getLdapKeyStoreForm();
        table.bind(form);
        table.select(LDAPKEY_ST_UPDATE);
        page.getLdapKeyStoreTab().select(Ids.build(ELYTRON_LDAP_KEY_STORE, TAB));

        crud.update(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE), form, FILTER_ALIAS);
    }

    @Test
    public void ldapKeyStoreDelete() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();

        crud.delete(ldapKeyStoreAddress(LDAPKEY_ST_DELETE), table, LDAPKEY_ST_DELETE);
    }

    @Test
    public void ldapKeyStoreNewItemTemplateTryAdd() {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();
        FormFragment form = page.getLdapKeyStoreNewItemTemplateForm();
        table.bindBlank(form);
        table.select(LDAPKEY_ST_TEMP4_TRY_ADD);
        page.getLdapKeyStoreTab().select(Ids.build(ELYTRON_LDAP_KEY_STORE, NEW_ITEM_TEMPLATE, TAB));
        EmptyState emptyState = form.emptyState();
        emptyState.mainAction();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(NEW_ITEM_PATH, ANY_STRING);
        addResource.getForm().text(NEW_ITEM_RDN, ANY_STRING);
        addResource.getPrimaryButton().click();
        addResource.getForm().expectError(NEW_ITEM_ATTRIBUTES);
    }

    @Test
    public void ldapKeyStoreNewItemTemplateAdd() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();
        FormFragment form = page.getLdapKeyStoreNewItemTemplateForm();
        table.bindBlank(form);
        table.select(LDAPKEY_ST_TEMP3_ADD);
        page.getLdapKeyStoreTab().select(Ids.build(ELYTRON_LDAP_KEY_STORE, NEW_ITEM_TEMPLATE, TAB));

        String rndName = "p1";
        String rndValue = Random.name();
        ModelNode props = new ModelNode();
        props.get(NAME).set(rndName);
        props.get(VALUE).add(rndValue);
        ModelNode newItemTemplate = new ModelNode();
        newItemTemplate.get(NEW_ITEM_PATH).set(ANY_STRING);
        newItemTemplate.get(NEW_ITEM_RDN).set(ANY_STRING);
        newItemTemplate.get(NEW_ITEM_ATTRIBUTES).add(props);

        crud.createSingleton(ldapKeyStoreAddress(LDAPKEY_ST_TEMP3_ADD), form, f -> {
            f.text(NEW_ITEM_PATH, ANY_STRING);
            f.text(NEW_ITEM_RDN, ANY_STRING);
            f.list(NEW_ITEM_ATTRIBUTES).add(rndName, rndValue);
        }, verifier -> verifier.verifyAttribute(NEW_ITEM_TEMPLATE, newItemTemplate));
    }

    @Test
    public void ldapKeyStoreNewItemTemplateUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();
        FormFragment form = page.getLdapKeyStoreNewItemTemplateForm();
        table.bind(form);
        table.select(LDAPKEY_ST_TEMP1_UPDATE);
        page.getLdapKeyStoreTab().select(Ids.build(ELYTRON_LDAP_KEY_STORE, NEW_ITEM_TEMPLATE, TAB));

        String rndValue = Random.name();

        crud.update(ldapKeyStoreAddress(LDAPKEY_ST_TEMP1_UPDATE), form, f -> f.text(NEW_ITEM_RDN, rndValue),
                verifier -> verifier.verifyAttribute(NEW_ITEM_TEMPLATE + "." + NEW_ITEM_RDN, rndValue));
    }

    @Test
    public void ldapKeyStoreNewItemTemplateRemove() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();
        FormFragment form = page.getLdapKeyStoreNewItemTemplateForm();
        table.bind(form);
        table.select(LDAPKEY_ST_TEMP2_DELETE);
        page.getLdapKeyStoreTab().select(Ids.build(ELYTRON_LDAP_KEY_STORE, NEW_ITEM_TEMPLATE, TAB));

        crud.deleteSingleton(ldapKeyStoreAddress(LDAPKEY_ST_TEMP2_DELETE), form,
                verifier -> verifier.verifyAttributeIsUndefined(NEW_ITEM_TEMPLATE));
    }

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

        crud.create(keyManagertAddress(KEY_MAN_CREATE), table, f -> {
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

        crud.update(keyManagertAddress(KEY_MAN_UPDATE), form, PROVIDER_NAME);
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

        crud.delete(keyManagertAddress(KEY_MAN_DELETE), table, KEY_MAN_DELETE);
    }

    @Test
    public void keyManagerCredentialReferenceUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();
        FormFragment form = page.getKeyManagerCredentialReferenceForm();
        table.bind(form);
        table.select(KEY_MAN_UPDATE);
        page.getKeyManagerTab().select(Ids.build(ELYTRON_KEY_MANAGER, CREDENTIAL_REFERENCE, TAB));
        crud.update(keyManagertAddress(KEY_MAN_UPDATE), form, f -> f.text(CLEAR_TEXT, ANY_STRING),
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + "." + CLEAR_TEXT, ANY_STRING));
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

        crud.create(trustManagertAddress(TRU_MAN_CREATE), table, f -> {
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
        crud.update(trustManagertAddress(TRU_MAN_UPDATE), form, f -> f.text(PROVIDER_NAME, ANY_STRING),
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
        ModelNodeResult actualResult = operations.readAttribute(trustManagertAddress(TRU_MAN_CRL_CRT),
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
        crud.update(trustManagertAddress(TRU_MAN_CRL_UPD), form, f -> f.text(PATH, ANY_STRING),
                verify -> verify.verifyAttribute(CERTIFICATE_REVOCATION_LIST + "." + PATH, ANY_STRING));
    }

    @Test
    public void trustManagerCertificateRevocationListDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        FormFragment form = page.getTrustManagerCertificateRevocationListForm();
        table.bind(form);
        table.select(TRU_MAN_CRL_DEL);
        page.getTrustManagerTab().select(Ids.build(ELYTRON_TRUST_MANAGER, CERTIFICATE_REVOCATION_LIST, TAB));
        crud.deleteSingleton(trustManagertAddress(TRU_MAN_CRL_DEL), form,
                verify -> verify.verifyAttributeIsUndefined(CERTIFICATE_REVOCATION_LIST));
    }

    @Test
    public void trustManagerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        crud.delete(trustManagertAddress(TRU_MAN_DELETE), table, TRU_MAN_DELETE);
    }

    // --------------- authentication-configuration

    @Test
    public void authenticationConfigurationCreate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();

        crud.create(authenticationConfigurationAddress(AUT_CF_CREATE), table, AUT_CF_CREATE);
    }

    @Test
    public void authenticationConfigurationUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        FormFragment form = page.getAuthenticationConfigurationForm();
        table.bind(form);
        table.select(AUT_CF_UPDATE);
        crud.update(authenticationConfigurationAddress(AUT_CF_UPDATE), form, AUTHENTICATION_NAME);
    }

    @Test
    public void authenticationConfigurationDelete() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        crud.delete(authenticationConfigurationAddress(AUT_CF_DELETE), table, AUT_CF_DELETE);
    }

    @Test
    public void authenticationConfigurationCredentialReferenceAdd() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        FormFragment form = page.getAuthConfigCredentialReferenceForm();
        table.bind(form);
        table.select(AUT_CF_CR_CRT);
        page.getAuthenticationConfigurationTabs().select(Ids.build(ELYTRON_AUTHENTICATION_CONFIGURATION, CREDENTIAL_REFERENCE, TAB));
        form.emptyState().mainAction();
        console.verifySuccess();
        // the UI "add" operation adds a credential-reference with no inner attributes, as they are not required
        ModelNodeResult actualResult = operations.readAttribute(authenticationConfigurationAddress(AUT_CF_CR_CRT),
                CREDENTIAL_REFERENCE);
        Assert.assertTrue("attribute credential-reference should exist", actualResult.value().isDefined());
    }

    @Test
    public void authenticationConfigurationCredentialReferenceUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        FormFragment form = page.getAuthConfigCredentialReferenceForm();
        table.bind(form);
        table.select(AUT_CF_CR_UPD);
        page.getAuthenticationConfigurationTabs()
                .select(Ids.build(ELYTRON_AUTHENTICATION_CONFIGURATION, CREDENTIAL_REFERENCE, TAB));
        crud.update(authenticationConfigurationAddress(AUT_CF_CR_UPD), form, f -> f.text(CLEAR_TEXT, ANY_STRING),
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + "." + CLEAR_TEXT, ANY_STRING));
    }

    @Test
    public void authenticationConfigurationCredentialReferenceDelete() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONFIGURATION_ITEM);
        TableFragment table = page.getAuthenticationConfigurationTable();
        FormFragment form = page.getAuthConfigCredentialReferenceForm();
        table.bind(form);
        table.select(AUT_CF_CR_DEL);
        page.getAuthenticationConfigurationTabs()
                .select(Ids.build(ELYTRON_AUTHENTICATION_CONFIGURATION, CREDENTIAL_REFERENCE, TAB));
        crud.deleteSingleton(authenticationConfigurationAddress(AUT_CF_CR_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(CREDENTIAL_REFERENCE));
    }


    // --------------- authentication-context

    @Test
    public void authenticationContextCreate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment table = page.getAuthenticationContextTable();

        crud.create(authenticationContextAddress(AUT_CT_CREATE), table, AUT_CT_CREATE);
    }

    @Test
    public void authenticationContextUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment table = page.getAuthenticationContextTable();
        FormFragment form = page.getAuthenticationContextForm();
        table.bind(form);
        table.select(AUT_CT_UPDATE);
        crud.update(authenticationContextAddress(AUT_CT_UPDATE), form, EXTENDS, AUT_CT_UPDATE2);
    }

    @Test
    public void authenticationContextDelete() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment table = page.getAuthenticationContextTable();
        crud.delete(authenticationContextAddress(AUT_CT_DELETE), table, AUT_CT_DELETE);
    }

    @Test
    public void authenticationContextMatchRulesCreate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment autTable = page.getAuthenticationContextTable();
        TableFragment table = page.getAuthenticationContextMatchRulesTable();

        autTable.action(AUT_CT_UPDATE, MATCH_RULES_TITLE);
        waitGui().until().element(table.getRoot()).is().visible();

        try {
            crud.create(authenticationContextAddress(AUT_CT_UPDATE), table,
                    f -> f.text(MATCH_ABSTRACT_TYPE, AUT_CT_MR_CREATE),
                    vc -> vc.verifyListAttributeContainsSingleValue(MATCH_RULES, MATCH_ABSTRACT_TYPE, AUT_CT_MR_CREATE));
        } finally {
            // getting rid of action selection
            page.getAuthenticationContextPages().breadcrumb().getBackToMainPage();
        }
    }

    @Test
    public void authenticationContextMatchRulesUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment autTable = page.getAuthenticationContextTable();
        TableFragment table = page.getAuthenticationContextMatchRulesTable();
        FormFragment form = page.getAuthenticationContextMatchRulesForm();
        table.bind(form);

        autTable.action(AUT_CT_UPDATE2, MATCH_RULES_TITLE);
        waitGui().until().element(table.getRoot()).is().visible();

        table.select(AUT_CT_MR_UPDATE);
        try {
            crud.update(authenticationContextAddress(AUT_CT_UPDATE2), form, f -> f.text(MATCH_HOST, ANY_STRING),
                    vc -> vc.verifyListAttributeContainsSingleValue(MATCH_RULES, MATCH_HOST, ANY_STRING));
        } finally {
            // getting rid of action selection
            page.getAuthenticationContextPages().breadcrumb().getBackToMainPage();
        }
    }

    @Test
    public void authenticationContextMatchRulesDelete() throws Exception {
        console.verticalNavigation().selectSecondary(AUTHENTICATION_ITEM, AUTHENTICATION_CONTEXT_ITEM);
        TableFragment autTable = page.getAuthenticationContextTable();
        TableFragment table = page.getAuthenticationContextMatchRulesTable();

        autTable.action(AUT_CT_UPDATE2, MATCH_RULES_TITLE);
        waitGui().until().element(table.getRoot()).is().visible();

        try {
            crud.delete(authenticationContextAddress(AUT_CT_UPDATE2), table, AUT_CT_MR_DELETE,
                    vc -> vc.verifyListAttributeDoesNotContainSingleValue(MATCH_RULES, MATCH_ABSTRACT_TYPE,
                            AUT_CT_MR_DELETE));
        } finally {
            // getting rid of action selection
            page.getAuthenticationContextPages().breadcrumb().getBackToMainPage();
        }
    }

    // --------------- file-audit-log

    @Test
    public void fileAuditLogCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();

        crud.create(fileAuditLogAddress(FILE_LOG_CREATE), table, f -> {
            f.text(NAME, FILE_LOG_CREATE);
            f.text(PATH, ANY_STRING);
        });
    }

    @Test
    public void fileAuditLogTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();
        crud.createWithErrorAndCancelDialog(table, NAME, PATH);
    }

    @Test
    public void fileAuditLogUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();
        FormFragment form = page.getFileAuditLogForm();
        table.bind(form);
        table.select(FILE_LOG_UPDATE);
        crud.update(fileAuditLogAddress(FILE_LOG_UPDATE), form, PATH);
    }

    @Test
    public void fileAuditLogTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();
        FormFragment form = page.getFileAuditLogForm();
        table.bind(form);
        table.select(FILE_LOG_TRY_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void fileAuditLogDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();
        crud.delete(fileAuditLogAddress(FILE_LOG_DELETE), table, FILE_LOG_DELETE);
    }

    // --------------- periodic-rotating-file-aaudit-log

    @Test
    public void periodicRotatingFileAuditLogCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();

        crud.create(periodicRotatingFileAuditLogAddress(PER_LOG_CREATE), table, f -> {
            f.text(NAME, PER_LOG_CREATE);
            f.text(PATH, ANY_STRING);
            f.text(SUFFIX, SUFFIX_LOG);
        });
    }

    @Test
    public void periodicRotatingFileAuditLogTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();
        crud.createWithErrorAndCancelDialog(table, NAME, PATH);
    }

    @Test
    public void periodicRotatingFileAuditLogUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();
        FormFragment form = page.getPeriodicRotatingFileAuditLogForm();
        table.bind(form);
        table.select(PER_LOG_UPDATE);
        crud.update(periodicRotatingFileAuditLogAddress(PER_LOG_UPDATE), form, PATH);
    }

    @Test
    public void periodicRotatingFileAuditLogTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();
        FormFragment form = page.getPeriodicRotatingFileAuditLogForm();
        table.bind(form);
        table.select(PER_LOG_TRY_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void periodicRotatingFileAuditLogDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();
        crud.delete(periodicRotatingFileAuditLogAddress(PER_LOG_DELETE), table, PER_LOG_DELETE);
    }


    // --------------- size-rotating-file-audit-log

    @Test
    public void sizeRotatingLogCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();

        crud.create(sizeRotatingFileAuditLogAddress(SIZ_LOG_CREATE), table, f -> {
            f.text(NAME, SIZ_LOG_CREATE);
            f.text(PATH, ANY_STRING);
        });
    }

    @Test
    public void sizeRotatingLogTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();
        crud.createWithErrorAndCancelDialog(table, NAME, PATH);
    }

    @Test
    public void sizeRotatingLogUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();
        FormFragment form = page.getSizeRotatingFileAuditLogForm();
        table.bind(form);
        table.select(SIZ_LOG_UPDATE);
        crud.update(sizeRotatingFileAuditLogAddress(SIZ_LOG_UPDATE), form, PATH);
    }

    @Test
    public void sizeRotatingLogTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();
        FormFragment form = page.getSizeRotatingFileAuditLogForm();
        table.bind(form);
        table.select(SIZ_LOG_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void sizeRotatingLogDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();
        crud.delete(sizeRotatingFileAuditLogAddress(SIZ_LOG_DELETE), table, SIZ_LOG_DELETE);
    }

    // --------------- syslog-audit-log

    @Test
    public void syslogAuditLogCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();

        crud.create(syslogAuditLogAddress(SYS_LOG_CREATE), table, f -> {
            f.text(NAME, SYS_LOG_CREATE);
            f.text(HOSTNAME, ANY_STRING);
            f.number(PORT, Random.number());
            f.text(SERVER_ADDRESS, LOCALHOST);
        });
    }

    @Test
    public void syslogAuditLogTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();
        crud.createWithErrorAndCancelDialog(table, NAME, HOSTNAME);
    }

    @Test
    public void syslogAuditLogUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();
        FormFragment form = page.getSyslogAuditLogForm();
        table.bind(form);
        table.select(SYS_LOG_UPDATE);
        crud.update(syslogAuditLogAddress(SYS_LOG_UPDATE), form, PORT, Random.number());
    }

    @Test
    public void syslogAuditLogTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();
        FormFragment form = page.getSyslogAuditLogForm();
        table.bind(form);
        table.select(SYS_LOG_TRY_UPDATE);
        crud.updateWithError(form, f -> f.clear(HOSTNAME), HOSTNAME);
    }

    @Test
    public void syslogAuditLogDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();
        crud.delete(syslogAuditLogAddress(SYS_LOG_DELETE), table, SYS_LOG_DELETE);
    }

    // --------------- aggregate-security-event-listener

    @Test
    public void aggregateSecurityEventListenerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();

        crud.create(aggregateSecurityEventListenerAddress(AGG_SEC_CREATE), table, f -> {
            f.text(NAME, AGG_SEC_CREATE);
            f.list(SECURITY_EVENT_LISTENERS).add(SIZ_LOG_UPDATE).add(SYS_LOG_UPDATE);
        });
    }

    @Test
    public void aggregateSecurityEventListenerTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();
        crud.createWithErrorAndCancelDialog(table, NAME, SECURITY_EVENT_LISTENERS);
    }

    @Test
    public void aggregateSecurityEventListenerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();
        FormFragment form = page.getAggregateSecurityEventListenerForm();
        table.bind(form);
        table.select(AGG_SEC_UPDATE);
        crud.update(aggregateSecurityEventListenerAddress(AGG_SEC_UPDATE), form,
                f -> f.list(SECURITY_EVENT_LISTENERS).add(PER_LOG_UPDATE),
                verify -> verify.verifyListAttributeContainsValue(SECURITY_EVENT_LISTENERS, PER_LOG_UPDATE));
    }

    @Test
    public void aggregateSecurityEventListenerTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();
        FormFragment form = page.getAggregateSecurityEventListenerForm();
        table.bind(form);
        table.select(AGG_SEC_UPDATE);
        crud.updateWithError(form, f -> f.list(SECURITY_EVENT_LISTENERS).removeTags(), SECURITY_EVENT_LISTENERS);
    }

    @Test
    public void aggregateSecurityEventListenerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();
        crud.delete(aggregateSecurityEventListenerAddress(AGG_SEC_DELETE), table, AGG_SEC_DELETE);
    }

    // --------------- jacc policy

    @Test
    public void jaccPolicyCreate() throws Exception {
        operations.removeIfExists(policyAddress(POL_CREATE));
        console.reload();
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        EmptyState emptyState = page.getEmptyPolicy();
        By selector = ByJQuery.selector("button" + contains(ADD_JACC_POLICY));
        emptyState.getRoot().findElement(selector).click();

        AddResourceDialogFragment addDialog = console.addResourceDialog();
        addDialog.getForm().text(NAME, POL_CREATE);
        addDialog.add();

        console.verifySuccess();
        new ResourceVerifier(policyAddress(POL_CREATE), client).verifyExists();
    }

    @Test
    public void jaccPolicyUpdate() throws Exception {
        if (!operations.exists(policyAddress(POL_CREATE))) {
            ModelNode empty = new ModelNode();
            empty.setEmptyObject();
            operations.add(policyAddress(POL_CREATE), Values.of(JACC_POLICY, empty));
            console.reload();
        }
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        FormFragment form = page.getPolicyJaccForm();
        crud.update(policyAddress(POL_CREATE), form, f -> f.text(POLICY, ANY_STRING),
                verify -> verify.verifyAttribute("jacc-policy.policy", ANY_STRING));
    }

    @Test
    public void jaccPolicyDelete() throws Exception {
        if (!operations.exists(policyAddress(POL_CREATE))) {
            ModelNode empty = new ModelNode();
            empty.setEmptyObject();
            operations.add(policyAddress(POL_CREATE), Values.of(JACC_POLICY, empty));
            console.reload();
        }
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        FormFragment form = page.getPolicyJaccForm();
        form.getRoot().findElement(By.cssSelector("a[data-operation=remove]")).click();
        console.confirmationDialog().confirm();
        waitGui().until().element(By.id(ELYTRON_CUSTOM_POLICY_EMPTY)).is().visible();
        // form.remove operation doesn't work because it waits for the blank-slate-pf css of form to become visible
        // but the emptyState div is outside the form div
        console.verifySuccess();
        new ResourceVerifier(policyAddress(POL_CREATE), client).verifyDoesNotExist();
    }

    // --------------- custom policy

    @Test
    public void customPolicyCreate() throws Exception {
        operations.removeIfExists(policyAddress(POL_CREATE));
        console.reload();
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        EmptyState emptyState = page.getEmptyPolicy();
        By selector = ByJQuery.selector("button" + contains(ADD_CUSTOM_POLICY));
        emptyState.getRoot().findElement(selector).click();

        AddResourceDialogFragment addDialog = console.addResourceDialog();
        addDialog.getForm().text(NAME, POL_CREATE);
        addDialog.getForm().text(CLASS_NAME, ANY_STRING);
        addDialog.add();

        console.verifySuccess();
        new ResourceVerifier(policyAddress(POL_CREATE), client).verifyExists();
    }

    @Test
    public void customPolicyUpdate() throws Exception {
        if (!operations.exists(policyAddress(POL_CREATE))) {
            ModelNode customPolicy = new ModelNode();
            customPolicy.get(CLASS_NAME).set(ANY_STRING);
            operations.add(policyAddress(POL_CREATE), Values.of(CUSTOM_POLICY, customPolicy));
            console.reload();
        }
        console.verticalNavigation().selectSecondary(OTHER_ITEM, POLICY_ITEM);
        FormFragment form = page.getPolicyCustomForm();
        String module = Random.name();
        crud.update(policyAddress(POL_CREATE), form, f -> f.text(MODULE, module),
                verify -> verify.verifyAttribute("custom-policy.module", module));
    }

    // There is no need for a customPolicyDelete test as the "remove" UI operation
    // is the same for jacc and custom policy

    // --------------- dir-context

    @Test
    public void dirContextCreate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();

        crud.create(dirContextAddress(DIR_CREATE), table, f -> {
            f.text(NAME, DIR_CREATE);
            f.text(URL, ANY_STRING);
        });
    }

    @Test
    public void dirContextTryCreate() {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, DIR_CREATE), URL);
    }

    @Test
    public void dirContextUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        FormFragment form = page.getDirContextForm();
        table.bind(form);
        table.select(DIR_UPDATE);
        crud.update(dirContextAddress(DIR_UPDATE), form, PRINCIPAL);
    }

    @Test
    public void dirContextDelete() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        crud.delete(dirContextAddress(DIR_DELETE), table, DIR_DELETE);
    }

    @Test
    public void dirContextCredentialReferenceAdd() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        FormFragment form = page.getDirContextCredentialReferenceForm();
        table.bind(form);
        table.select(DIR_CR_CRT);
        page.getDirContextTabs().select(Ids.build(ELYTRON_DIR_CONTEXT, CREDENTIAL_REFERENCE, TAB));
        form.emptyState().mainAction();
        console.verifySuccess();
        // the UI "add" operation adds a credential-reference with no inner attributes, as they are not required
        ModelNodeResult actualResult = operations.readAttribute(dirContextAddress(DIR_CR_CRT), CREDENTIAL_REFERENCE);
        Assert.assertTrue("attribute credential-reference should exist", actualResult.value().isDefined());
    }

    @Test
    public void dirContextCredentialReferenceUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        FormFragment form = page.getDirContextCredentialReferenceForm();
        table.bind(form);
        table.select(DIR_CR_UPD);
        page.getDirContextTabs().select(Ids.build(ELYTRON_DIR_CONTEXT, CREDENTIAL_REFERENCE, TAB));
        crud.update(dirContextAddress(DIR_CR_UPD), form, f -> f.text(CLEAR_TEXT, ANY_STRING),
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + "." + CLEAR_TEXT, ANY_STRING));
    }

    @Test
    public void dirContextCredentialReferenceDelete() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_ITEM, DIR_CONTEXT_ITEM);
        TableFragment table = page.getDirContextTable();
        FormFragment form = page.getDirContextCredentialReferenceForm();
        table.bind(form);
        table.select(DIR_CR_DEL);
        page.getDirContextTabs().select(Ids.build(ELYTRON_DIR_CONTEXT, CREDENTIAL_REFERENCE, TAB));
        crud.deleteSingleton(dirContextAddress(DIR_CR_DEL), form,
                ver -> ver.verifyAttributeIsUndefined(CREDENTIAL_REFERENCE));
    }
}
