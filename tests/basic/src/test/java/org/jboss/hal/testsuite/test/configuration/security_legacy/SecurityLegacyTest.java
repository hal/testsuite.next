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
package org.jboss.hal.testsuite.test.configuration.security_legacy;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.SecurityLegacyPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.JKS;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.KEY_ST_CREATE;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.KEY_ST_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.keyStoreAddress;
import static org.jboss.hal.testsuite.test.configuration.security_legacy.SecurityLegacyFixtures.*;

@RunWith(Arquillian.class)
public class SecurityLegacyTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration administration = new Administration(client);

    @BeforeClass
    public static void beforeTests() throws Exception {

        ModelNode credentialReference = new ModelNode();
        credentialReference.get(CLEAR_TEXT).set(SAMPLE_PASSWORD);
        operations.add(keyStoreAddress(KEY_ST_CREATE), Values.of(CREDENTIAL_REFERENCE, credentialReference)
                .and(PATH, SAMPLE_KEYSTORE_FILENAME)
                .and(RELATIVE_TO, JBOSS_SERVER_CONFIG_DIR)
                .and(TYPE, JKS));
        operations.add(keyStoreAddress(KEY_ST_UPDATE), Values.of(CREDENTIAL_REFERENCE, credentialReference)
                .and(PATH, SAMPLE_TRUSTSTORE_FILENAME)
                .and(RELATIVE_TO, JBOSS_SERVER_CONFIG_DIR)
                .and(TYPE, JKS));
        Values keyPairParam = Values.of("alias", "jboss")
                .and("distinguished-name", "c=US,CN=Wildfly,O=Red Hat,OU=Eng,ST=AZ");
        operations.invoke("generate-key-pair", keyStoreAddress(KEY_ST_CREATE), keyPairParam);
        // the "store" operation will generate the file in wildfly filesystem, but there is no operation to delete it
        operations.invoke("store", keyStoreAddress(KEY_ST_CREATE));
        operations.invoke("generate-key-pair", keyStoreAddress(KEY_ST_UPDATE), keyPairParam);
        operations.invoke("store", keyStoreAddress(KEY_ST_UPDATE));

        String keystoreFile = "${"+ JBOSS_SERVER_CONFIG_DIR + "}/" + SAMPLE_KEYSTORE_FILENAME;
        String truststoreFile = "${"+ JBOSS_SERVER_CONFIG_DIR + "}/" + SAMPLE_TRUSTSTORE_FILENAME;
        operations.add(securityDomainAddress(SEC_DOM_CREATE));
        operations.add(securityDomainAddress(SEC_DOM_UPDATE));
        Values jsseParams = Values.of(CLIENT_AUTH, true)
                .andObject(TRUSTSTORE, Values.of(PASSWORD, SAMPLE_PASSWORD).and(URL, truststoreFile))
                .andObject(KEYSTORE, Values.of(PASSWORD, SAMPLE_PASSWORD).and(URL, keystoreFile));
        operations.add(securityDomainJSSEAddress(SEC_DOM_CREATE), jsseParams);
        operations.add(securityDomainJSSEAddress(SEC_DOM_UPDATE), jsseParams);
        // must reload standalone, to take effect
        administration.reload();

        Values legacyParams = Values.of(LEGACY_JSSE_CONFIG, SEC_DOM_CREATE);
        operations.add(elytronKeyManagerAddress(EKM_UPDATE), legacyParams);
        operations.add(elytronKeyManagerAddress(EKM_DELETE), legacyParams);

        operations.add(elytronKeyStoreAddress(EKS_UPDATE), legacyParams);
        operations.add(elytronKeyStoreAddress(EKS_DELETE), legacyParams);

        Values realmLegacyParams = Values.of(LEGACY_JAAS_CONFIG, SEC_DOM_CREATE);
        operations.add(elytronRealmAddress(REALM_UPDATE), realmLegacyParams);
        operations.add(elytronRealmAddress(REALM_DELETE), realmLegacyParams);

        operations.add(elytronTrustManagerAddress(ETM_UPDATE), legacyParams);
        operations.add(elytronTrustManagerAddress(ETM_DELETE), legacyParams);

        operations.add(elytronTrustStoreAddress(ETS_UPDATE), legacyParams);
        operations.add(elytronTrustStoreAddress(ETS_DELETE), legacyParams);

    }

    @AfterClass
    public static void tearDown() throws Exception {

        operations.remove(elytronKeyManagerAddress(EKM_CREATE));
        operations.remove(elytronKeyManagerAddress(EKM_UPDATE));
        operations.remove(elytronKeyManagerAddress(EKM_DELETE));

        operations.remove(elytronKeyStoreAddress(EKS_CREATE));
        operations.remove(elytronKeyStoreAddress(EKS_UPDATE));
        operations.remove(elytronKeyStoreAddress(EKS_DELETE));

        operations.remove(elytronRealmAddress(REALM_CREATE));
        operations.remove(elytronRealmAddress(REALM_UPDATE));
        operations.remove(elytronRealmAddress(REALM_DELETE));

        operations.remove(elytronTrustManagerAddress(ETM_CREATE));
        operations.remove(elytronTrustManagerAddress(ETM_UPDATE));
        operations.remove(elytronTrustManagerAddress(ETM_DELETE));

        operations.remove(elytronTrustStoreAddress(ETS_CREATE));
        operations.remove(elytronTrustStoreAddress(ETS_UPDATE));
        operations.remove(elytronTrustStoreAddress(ETS_DELETE));

        operations.remove(vaultAddress());

        operations.remove(securityDomainAddress(SEC_DOM_CREATE));
        operations.remove(securityDomainAddress(SEC_DOM_UPDATE));
        operations.remove(keyStoreAddress(KEY_ST_CREATE));
        operations.remove(keyStoreAddress(KEY_ST_UPDATE));
    }

    @Page private SecurityLegacyPage page;
    @Inject private Console console;
    @Inject private CrudOperations crud;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    @Test
    public void configurationUpdate() throws Exception {
        FormFragment form = page.getConfigurationForm();
        console.verticalNavigation().selectPrimary(CONFIGURATION_ITEM);
        console.waitNoNotification();
        crud.update(SUBSYSTEM_ADDRESS, form, INITIALIZE_JACC, false);
        // undefine it as it was before the update
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, INITIALIZE_JACC);
    }

    @Test
    public void keyManagerCreate() throws Exception {
        templateCreate(KEY_MANAGER_ITEM, page.getElytronKeyManagerTable(), EKM_CREATE,
                elytronKeyManagerAddress(EKM_CREATE));
    }

    @Test
    public void keyManagerUpdate() throws Exception {
        templateUpdate(KEY_MANAGER_ITEM, page.getElytronKeyManagerTable(), page.getElytronKeyManagerForm(), EKM_UPDATE,
                elytronKeyManagerAddress(EKM_UPDATE));
    }

    @Test
    public void keyManagerDelete() throws Exception {
        templateDelete(KEY_MANAGER_ITEM, page.getElytronKeyManagerTable(), EKM_DELETE,
                elytronKeyManagerAddress(EKM_DELETE));
    }

    @Test
    public void keyStoreCreate() throws Exception {
        templateCreate(KEY_STORE_ITEM, page.getElytronKeyStoreTable(), EKS_CREATE,
                elytronKeyStoreAddress(EKS_CREATE));
    }

    @Test
    public void keyStoreUpdate() throws Exception {
        templateUpdate(KEY_STORE_ITEM, page.getElytronKeyStoreTable(), page.getElytronKeyStoreForm(), EKS_UPDATE,
                elytronKeyStoreAddress(EKS_UPDATE));
    }

    @Test
    public void keyStoreDelete() throws Exception {
        templateDelete(KEY_STORE_ITEM, page.getElytronKeyStoreTable(), EKS_DELETE,
                elytronKeyStoreAddress(EKS_DELETE));
    }

    @Test
    public void realmCreate() throws Exception {
        TableFragment table = page.getElytronRealmTable();
        console.verticalNavigation().selectPrimary(REALM_ITEM);
        console.waitNoNotification();
        crud.create(elytronRealmAddress(REALM_CREATE), table, f -> {
            f.text(NAME, REALM_CREATE);
            f.text(LEGACY_JAAS_CONFIG, SEC_DOM_CREATE);
        });
    }

    @Test
    public void realmUpdate() throws Exception {
        TableFragment table = page.getElytronRealmTable();
        FormFragment form = page.getElytronRealmForm();
        table.bind(form);
        console.verticalNavigation().selectPrimary(REALM_ITEM);
        console.waitNoNotification();
        table.select(REALM_UPDATE);
        crud.update(elytronRealmAddress(REALM_UPDATE), form,LEGACY_JAAS_CONFIG, SEC_DOM_UPDATE);
    }

    @Test
    public void realmDelete() throws Exception {
        templateDelete(REALM_ITEM, page.getElytronRealmTable(), REALM_DELETE,
                elytronRealmAddress(REALM_DELETE));
    }

    @Test
    public void trustManagerCreate() throws Exception {
        templateCreate(TRUST_MANAGER_ITEM, page.getElytronTrustManagerTable(), ETM_CREATE,
                elytronTrustManagerAddress(ETM_CREATE));
    }

    @Test
    public void trustManagerUpdate() throws Exception {
        templateUpdate(TRUST_MANAGER_ITEM, page.getElytronTrustManagerTable(), page.getElytronTrustManagerForm(), ETM_UPDATE,
                elytronTrustManagerAddress(ETM_UPDATE));
    }

    @Test
    public void trustManagerDelete() throws Exception {
        templateDelete(TRUST_MANAGER_ITEM, page.getElytronTrustManagerTable(), ETM_DELETE,
                elytronTrustManagerAddress(ETM_DELETE));
    }

    @Test
    public void trustStoreCreate() throws Exception {
        templateCreate(TRUST_STORE_ITEM, page.getElytronTrustStoreTable(), ETS_CREATE,
                elytronTrustStoreAddress(ETS_CREATE));
    }

    @Test
    public void trustStoreUpdate() throws Exception {
        templateUpdate(TRUST_STORE_ITEM, page.getElytronTrustStoreTable(), page.getElytronTrustStoreForm(), ETS_UPDATE,
                elytronTrustStoreAddress(ETS_UPDATE));
    }

    @Test
    public void trustStoreDelete() throws Exception {
        templateDelete(TRUST_STORE_ITEM, page.getElytronTrustStoreTable(), ETS_DELETE,
                elytronTrustStoreAddress(ETS_DELETE));
    }

    @Test
    public void vaultAddAndUpdate() throws Exception {
        operations.remove(vaultAddress());
        console.reload();
        console.verticalNavigation().selectPrimary(VAULT_ITEM);
        FormFragment form = page.getVaultForm();
        EmptyState emptyState = form.emptyState();
        emptyState.mainAction();
        console.verifySuccess();
        form.edit();
        form.text("code", ANY_STRING);
        form.save();
        new ResourceVerifier(vaultAddress(), client).verifyExists();

    }

    @Test
    public void vaultRemove() throws Exception {
        operations.add(vaultAddress());
        console.reload();
        console.verticalNavigation().selectPrimary(VAULT_ITEM);
        FormFragment form = page.getVaultForm();
        form.remove();
        console.verifySuccess();
        new ResourceVerifier(vaultAddress(), client).verifyDoesNotExist();

    }

    // ---------------- crud templates

    public void templateCreate(String item, TableFragment table, String resourceName, Address address) throws Exception {
        console.verticalNavigation().selectPrimary(item);
        crud.create(address, table, f -> {
            f.text(NAME, resourceName);
            f.text(LEGACY_JSSE_CONFIG, SEC_DOM_CREATE);
        });
    }

    public void templateUpdate(String item, TableFragment table, FormFragment form, String resourceName,
            Address address) throws Exception {
        console.verticalNavigation().selectPrimary(item);
        table.bind(form);
        table.select(resourceName);
        crud.update(address, form, LEGACY_JSSE_CONFIG, SEC_DOM_UPDATE);
    }

    public void templateDelete(String item, TableFragment table, String resourceName, Address address)
            throws Exception {
        console.verticalNavigation().selectPrimary(item);
        crud.delete(address, table, resourceName);
    }

}
