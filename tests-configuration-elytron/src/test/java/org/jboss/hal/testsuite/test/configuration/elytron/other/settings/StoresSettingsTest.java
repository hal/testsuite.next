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
package org.jboss.hal.testsuite.test.configuration.elytron.other.settings;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREATE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DIR_CONTEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_STORE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NEW_ITEM_ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NEW_ITEM_PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NEW_ITEM_RDN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NEW_ITEM_TEMPLATE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROVIDER_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SEARCH_PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TYPE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.resources.Ids.ELYTRON_CREDENTIAL_STORE;
import static org.jboss.hal.resources.Ids.ELYTRON_KEY_STORE;
import static org.jboss.hal.resources.Ids.ELYTRON_LDAP_KEY_STORE;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ALIAS_FILTER;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ANY_STRING;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CREDENTIAL_STORE_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CRED_ST_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CRED_ST_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CRED_ST_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.DIR_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILTERING_KEY_STORE_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILTER_ALIAS;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILT_ST_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILT_ST_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.FILT_ST_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.JKS;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KEY_STORE_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KEY_ST_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KEY_ST_CR_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KEY_ST_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.KEY_ST_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAPKEY_ST_CREATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAPKEY_ST_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAPKEY_ST_TEMP1_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAPKEY_ST_TEMP2_DELETE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAPKEY_ST_TEMP3_ADD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAPKEY_ST_TEMP4_TRY_ADD;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAPKEY_ST_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.LDAP_KEY_STORE_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.STORES_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.credentialStoreAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.filteringKeyStoreAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.keyStoreAddress;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ldapKeyStoreAddress;
@RunWith(Arquillian.class)
public class StoresSettingsTest extends AbstractOtherSettingsTest {

    // --------------- credential store

    @Test
    public void credentialStoreCreate() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, CREDENTIAL_STORE_ITEM);
        TableFragment table = page.getCredentialStoreTable();

        crud.create(credentialStoreAddress(CRED_ST_CREATE), table, f -> {
            f.text(NAME, CRED_ST_CREATE);
            f.text(PATH, ANY_STRING);
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
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + PROPERTY_DELIMITER + CLEAR_TEXT, ANY_STRING));
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

        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, KEY_ST_CREATE), CLEAR_TEXT);
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
                ver -> ver.verifyAttribute(CREDENTIAL_REFERENCE + PROPERTY_DELIMITER + CLEAR_TEXT, ANY_STRING));
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
        addResource.getPrimaryButton().click();
        try {
            addResource.getForm().expectError(NEW_ITEM_RDN);
        } finally {
            addResource.getSecondaryButton().click(); // close dialog to cleanup
        }
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
                verifier -> verifier.verifyAttribute(NEW_ITEM_TEMPLATE + PROPERTY_DELIMITER + NEW_ITEM_RDN, rndValue));
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
}
