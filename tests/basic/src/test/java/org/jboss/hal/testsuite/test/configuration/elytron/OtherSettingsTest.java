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
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronOtherSettingsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.ELYTRON_CREDENTIAL_STORE;
import static org.jboss.hal.resources.Ids.ELYTRON_KEY_STORE;
import static org.jboss.hal.resources.Ids.ELYTRON_LDAP_KEY_STORE;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class OtherSettingsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String ANY_STRING = Random.name();

    @BeforeClass
    public static void beforeTests() throws Exception {
        // Stores
        ModelNode credRef = new ModelNode();
        credRef.get(CLEAR_TEXT).set(ANY_STRING);
        Values credParams = Values.of(CREATE, true).and(CREDENTIAL_REFERENCE, credRef);
        operations.add(credentialStoreAddress(CRED_ST_UPDATE), credParams);
        operations.add(credentialStoreAddress(CRED_ST_DELETE), credParams);
        Values ksParams = Values.of(TYPE, JKS).and(CREDENTIAL_REFERENCE, credRef);
        operations.add(keyStoreAddress(KEY_ST_UPDATE), ksParams);
        operations.add(keyStoreAddress(KEY_ST_DELETE), ksParams);
        operations.add(filteringKeyStoreAddress(FILT_ST_DELETE), Values.of(ALIAS_FILTER, ANY_STRING).and(KEY_STORE, KEY_ST_UPDATE));
        operations.add(filteringKeyStoreAddress(FILT_ST_UPDATE), Values.of(ALIAS_FILTER, ANY_STRING).and(KEY_STORE, KEY_ST_UPDATE));

        operations.add(dirContextAddress(DIR_UPDATE), Values.of(URL, ANY_STRING));
        operations.add(dirContextAddress(DIR_DELETE), Values.of(URL, ANY_STRING));

        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE), Values.of(DIR_CONTEXT, DIR_UPDATE).and(SEARCH_PATH, ANY_STRING));
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_DELETE), Values.of(DIR_CONTEXT, DIR_UPDATE).and(SEARCH_PATH, ANY_STRING));

    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(credentialStoreAddress(CRED_ST_DELETE));
        operations.remove(credentialStoreAddress(CRED_ST_UPDATE));
        operations.remove(credentialStoreAddress(CRED_ST_CREATE));

        operations.remove(filteringKeyStoreAddress(FILT_ST_DELETE));
        operations.remove(filteringKeyStoreAddress(FILT_ST_UPDATE));
        operations.remove(filteringKeyStoreAddress(FILT_ST_CREATE));

        operations.remove(keyStoreAddress(KEY_ST_CREATE));
        operations.remove(keyStoreAddress(KEY_ST_DELETE));
        operations.remove(keyStoreAddress(KEY_ST_UPDATE));

        operations.remove(dirContextAddress(DIR_UPDATE));
        operations.remove(dirContextAddress(DIR_DELETE));
        operations.remove(dirContextAddress(DIR_CREATE));

        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_DELETE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE));
        operations.remove(ldapKeyStoreAddress(LDAPKEY_ST_CREATE));
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
        page.getLdapKeyStoreTab().select(Ids.build(ELYTRON_LDAP_KEY_STORE, TAB));
        TableFragment table = page.getLdapKeyStoreTable();
        FormFragment form = page.getLdapKeyStoreForm();
        table.bind(form);
        table.select(LDAPKEY_ST_UPDATE);

        crud.update(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE), form, FILTER_ALIAS);
    }

    @Test
    public void ldapKeyStoreDelete() throws Exception {
        console.verticalNavigation().selectSecondary(STORES_ITEM, LDAP_KEY_STORE_ITEM);
        TableFragment table = page.getLdapKeyStoreTable();

        crud.delete(ldapKeyStoreAddress(LDAPKEY_ST_DELETE), table, LDAPKEY_ST_DELETE);
    }

}
