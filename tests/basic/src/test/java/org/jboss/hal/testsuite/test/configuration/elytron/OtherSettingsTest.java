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
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronOtherSettingsPage;
import org.jboss.hal.testsuite.util.Library;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class OtherSettingsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String ANY_STRING = Random.name();

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
        operations.add(keyStoreAddress(KEY_ST_DELETE), ksParams);
        operations.writeAttribute(keyStoreAddress(KEY_ST_UPDATE), PROVIDERS, PROV_LOAD_UPDATE);

        operations.add(filteringKeyStoreAddress(FILT_ST_DELETE),
                Values.of(ALIAS_FILTER, ANY_STRING).and(KEY_STORE, KEY_ST_UPDATE));
        operations.add(filteringKeyStoreAddress(FILT_ST_UPDATE),
                Values.of(ALIAS_FILTER, ANY_STRING).and(KEY_STORE, KEY_ST_UPDATE));

        operations.add(dirContextAddress(DIR_UPDATE), Values.of(URL, ANY_STRING));
        operations.add(dirContextAddress(DIR_DELETE), Values.of(URL, ANY_STRING));

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
        operations.add(keyManagertAddress(KEY_MAN_DELETE), keyManagerValues);

        Values serverSslContextValues = Values.of(KEY_MANAGER, KEY_MAN_UPDATE);
        operations.add(serverSslContextAddress(SRV_SSL_DELETE), serverSslContextValues);
        operations.add(serverSslContextAddress(SRV_SSL_UPDATE), serverSslContextValues);

        // a realm is required for new security-domain
        operations.add(filesystemRealmAddress(FILESYS_REALM_UPDATE), Values.of(PATH, ANY_STRING));
        operations.add(securityDomainAddress(SEC_DOM_UPDATE));
        operations.add(securityDomainAddress(SEC_DOM_UPDATE2));
        operations.add(securityDomainAddress(SEC_DOM_DELETE));

        operations.add(trustManagertAddress(TRU_MAN_UPDATE), Values.of(KEY_STORE, KEY_ST_UPDATE));
        operations.add(trustManagertAddress(TRU_MAN_DELETE), Values.of(KEY_STORE, KEY_ST_UPDATE));
        ModelNode params = new ModelNode();
        // the path attribute must be a valid file
        params.get(PATH).set("${jboss.server.config.dir}/logging.properties");
        operations.add(trustManagertAddress(TRU_MAN_UPDATE2), Values.of(KEY_STORE, KEY_ST_UPDATE)
                // .and(CERTIFICATE_REVOCATION_LIST, params));
                .andObject(CERTIFICATE_REVOCATION_LIST, Values.of(PATH, "${jboss.server.config.dir}/logging.properties")));
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

        operations.remove(dirContextAddress(DIR_UPDATE));
        operations.remove(dirContextAddress(DIR_DELETE));
        operations.remove(dirContextAddress(DIR_CREATE));

        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_DELETE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_TEMP1_UPDATE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_TEMP2_DELETE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_CREATE));

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
        operations.remove(keyManagertAddress(KEY_MAN_DELETE));

        operations.remove(securityDomainAddress(SEC_DOM_UPDATE));
        operations.remove(securityDomainAddress(SEC_DOM_UPDATE2));
        operations.remove(securityDomainAddress(SEC_DOM_DELETE));
        operations.remove(securityDomainAddress(SEC_DOM_CREATE));

        operations.remove(trustManagertAddress(TRU_MAN_UPDATE));
        operations.remove(trustManagertAddress(TRU_MAN_UPDATE2));
        operations.remove(trustManagertAddress(TRU_MAN_CREATE));
        operations.remove(trustManagertAddress(TRU_MAN_DELETE));

        // key-store is a dependency on key-manager and trust-manager, remove it after key-manager and trust-manager
        operations.remove(keyStoreAddress(KEY_ST_UPDATE));

        operations.remove(providerLoaderAddress(PROV_LOAD_UPDATE));
        operations.remove(providerLoaderAddress(PROV_LOAD_UPDATE2));
        operations.remove(providerLoaderAddress(PROV_LOAD_UPDATE3));
        operations.remove(providerLoaderAddress(PROV_LOAD_CREATE));
        operations.remove(providerLoaderAddress(PROV_LOAD_DELETE));

        operations.remove(filesystemRealmAddress(FILESYS_REALM_UPDATE));

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
    public void credentialStoreTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, CREDENTIAL_STORE_ITEM);
        TableFragment table = page.getCredentialStoreTable();

        crud.createWithError(table, f -> {
            f.text(NAME, CRED_ST_CREATE);
            f.flip(CREATE, true);
        }, CLEAR_TEXT);
    }

    @Test
    public void credentialStoreUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, CREDENTIAL_STORE_ITEM);
        page.getCredentialStoreTab().select(Ids.build(ELYTRON_CREDENTIAL_STORE, ATTRIBUTES, TAB));
        TableFragment table = page.getCredentialStoreTable();
        FormFragment form = page.getCredentialStoreForm();
        table.bind(form);
        table.select(CRED_ST_UPDATE);

        crud.update(credentialStoreAddress(CRED_ST_UPDATE), form, PROVIDER_NAME, ANY_STRING);
    }

    // TODO: investigate why the save operation doesnt work
    @Ignore
    public void credentialStoreCredentialReferenceUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, CREDENTIAL_STORE_ITEM);
        page.getCredentialStoreTab().select(Ids.build(ELYTRON_CREDENTIAL_STORE, CREDENTIAL_REFERENCE, TAB));
        TableFragment table = page.getCredentialStoreTable();
        FormFragment form = page.getCredentialStoreCredentialReferenceForm();
        table.bind(form);
        table.select(CRED_ST_UPDATE);

        crud.update(credentialStoreAddress(CRED_ST_UPDATE), form, CLEAR_TEXT);
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
    public void filteringKeyStoreTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, FILTERING_KEY_STORE_ITEM);
        TableFragment table = page.getFilteringKeyStoreTable();

        crud.createWithError(table, f -> {
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
    public void keyStoreTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, KEY_STORE_ITEM);
        TableFragment table = page.getKeyStoreTable();

        crud.createWithError(table, f -> {
            f.text(NAME, KEY_ST_CREATE);
        }, TYPE);
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
    public void ldapKeyStoreTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();

        crud.createWithError(table, f -> {
            f.text(NAME, LDAPKEY_ST_CREATE);
        }, DIR_CONTEXT);
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
    public void ldapKeyStoreNewItemTemplateTryAdd() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();
        FormFragment form = page.getLdapKeyStoreNewItemTemplateForm();
        table.bind(form);
        table.select(LDAPKEY_ST_UPDATE);
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
        table.bind(form);
        table.select(LDAPKEY_ST_UPDATE);
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

        crud.createSingleton(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE), form, f -> {
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
    public void aggregateProvidersTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, AGGREGATE_PROVIDERS_ITEM);
        TableFragment table = page.getAggregateProvidersTable();

        crud.createWithError(table, AGG_PRV_CREATE, PROVIDERS);
    }

    @Test
    public void aggregateProvidersUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, AGGREGATE_PROVIDERS_ITEM);
        TableFragment table = page.getAggregateProvidersTable();
        FormFragment form = page.getAggregateProvidersForm();
        table.bind(form);
        table.select(AGG_PRV_UPDATE);

        crud.update(aggregateProvidersAddress(AGG_PRV_UPDATE), form, f -> {
            f.list(PROVIDERS).add(PROV_LOAD_UPDATE3);
        }, verify -> verify.verifyListAttributeContainsValue(PROVIDERS, PROV_LOAD_UPDATE3));
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
    public void keyManagerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();

        crud.createWithError(table, KEY_MAN_CREATE, KEY_STORE);
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
    public void keyManagerTryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();
        FormFragment form = page.getKeyManagerForm();
        table.bind(form);
        table.select(KEY_MAN_UPDATE);
        page.getKeyManagerTab().select(Ids.build(ELYTRON_KEY_MANAGER, ATTRIBUTES, TAB));

        crud.updateWithError(form, f -> f.clear(KEY_STORE), KEY_STORE);
    }

    @Test
    public void keyManagerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, KEY_MANAGER_ITEM);
        TableFragment table = page.getKeyManagerTable();

        crud.delete(keyManagertAddress(KEY_MAN_DELETE), table, KEY_MAN_DELETE);
    }
    // TODO: tests for key manager credential reference, cr doesnt save attributes, fix that in hal.next

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
    public void serverSslContextTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, SERVER_SSL_CONTEXT_ITEM);
        TableFragment table = page.getServerSslContextTable();

        crud.createWithError(table, SRV_SSL_CREATE, KEY_MANAGER);
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
    public void serverSslContextTryUpdate() throws Exception {
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
            f.text(DEFAULT_REALM, FILESYS_REALM_UPDATE);
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

    // --------------- security-domain

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
    public void trustManagerTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        crud.createWithError(table, TRU_MAN_CREATE, KEY_STORE);
    }

    @Test
    public void trustManagerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        FormFragment form = page.getTrustManagerForm();
        table.bind(form);
        table.select(TRU_MAN_UPDATE);
        page.getTrustManagerTab().select(Ids.build(ELYTRON_TRUST_MANAGER, ATTRIBUTES, TAB));
        crud.update(trustManagertAddress(TRU_MAN_UPDATE), form, f -> {
            f.text(PROVIDER_NAME, ANY_STRING);
        }, verify -> verify.verifyAttribute(PROVIDER_NAME, ANY_STRING));
    }

    @Test
    public void trustManagerCertificateRevocationListAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        FormFragment form = page.getTrustManagerCertificateRevocationListForm();
        table.bind(form);
        table.select(TRU_MAN_UPDATE);
        page.getTrustManagerTab().select(Ids.build(ELYTRON_TRUST_MANAGER, CERTIFICATE_REVOCATION_LIST, TAB));
        form.emptyState().mainAction();
        console.verifySuccess();
        // the UI "add" operation adds a certificate-revocation-list with no inner attributes, as they are not required
        ModelNodeResult actualResult = operations.readAttribute(trustManagertAddress(TRU_MAN_UPDATE),
                CERTIFICATE_REVOCATION_LIST);
        Assert.assertTrue("attribute certificate-revocation-list should exist", actualResult.get(RESULT).isDefined());
    }

    // TODO: fix certificarte-revocation-list save operation
    @Ignore
    public void trustManagerCertificateRevocationListUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        FormFragment form = page.getTrustManagerCertificateRevocationListForm();
        table.bind(form);
        table.select(TRU_MAN_UPDATE);
        page.getTrustManagerTab().select(Ids.build(ELYTRON_TRUST_MANAGER, CERTIFICATE_REVOCATION_LIST, TAB));
        String path = "${jboss.server.config.dir}/logging.properties";
        crud.update(trustManagertAddress(TRU_MAN_UPDATE), form, f -> {
            f.text(PATH, path);
        }, verify -> verify.verifyAttribute(CERTIFICATE_REVOCATION_LIST + "." + PATH, path));
    }

    @Test
    public void trustManagerCertificateRevocationListDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        FormFragment form = page.getTrustManagerCertificateRevocationListForm();
        table.bind(form);
        Library.letsSleep(3000);
        table.select(TRU_MAN_UPDATE2);
        page.getTrustManagerTab().select(Ids.build(ELYTRON_TRUST_MANAGER, CERTIFICATE_REVOCATION_LIST, TAB));
        crud.deleteSingleton(trustManagertAddress(TRU_MAN_UPDATE2), form,
                verify -> verify.verifyAttributeIsUndefined(CERTIFICATE_REVOCATION_LIST));
    }

    @Test
    public void trustManagerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SSL_ITEM, TRUST_MANAGER_ITEM);
        TableFragment table = page.getTrustManagerTable();
        crud.delete(trustManagertAddress(TRU_MAN_DELETE), table, TRU_MAN_DELETE);
    }
}
