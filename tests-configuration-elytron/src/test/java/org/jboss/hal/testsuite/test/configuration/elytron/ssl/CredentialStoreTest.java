/*
 * Copyright 2015-2021 Red Hat, Inc, and individual contributors.
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

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.RequiresLetsEncrypt;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.runtime.elytron.ElytronRuntimeStoresPage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.wildfly.extras.creaper.core.online.CliException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.*;

@Category(RequiresLetsEncrypt.class)
@RunWith(Arquillian.class)
public class CredentialStoreTest {
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String LOCATION =
            "cred_stores-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String MY_STORE_FILE =
            "my_store-" + RandomStringUtils.randomAlphanumeric(7) + ".jceks";
    private static final String MY_ALIAS = "my_alias";
    private static final String MY_STORE = "my_store";
    private static final String HAL_UID_0 = "hal-uid-0";
    private static final String RELOAD = ":reload";
    private static final String CREDENTIAL_STORE = "credential-store";

    @BeforeClass
    public static void setUp() throws CliException, IOException {
        client.executeCli("/subsystem=elytron/" + CREDENTIAL_STORE + "=" + MY_STORE + ":add(location=\"" + LOCATION + "/" + MY_STORE_FILE + "\", relative-to=jboss.server.data.dir,  credential-reference={clear-text=supersecretstorepassword},create=true)");
        client.executeCli(RELOAD);
    }

    @AfterClass
    public static void tearDown() throws CliException, IOException, OperationException {
        try {

            operations.removeIfExists(SUBSYSTEM_ADDRESS.and(CREDENTIAL_STORE, MY_STORE));
            client.executeCli(RELOAD);
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
    }

    /**
     * Test for HAL-1762 - Aliases are removed from the credential store when passwords are updated from the admin console
     *
     */
    @Test
    public void load() {
        console.verticalNavigation().selectPrimary(Ids.ELYTRON_CREDENTIAL_STORE);
        page.getCredentialStoreTable().getRoot().findElement(By.id(HAL_UID_0)).click();
        page.getCredentialStoreAliasTable().button("Add alias").click();

        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(ModelDescriptionConstants.ALIAS, MY_ALIAS);
        dialog.getPrimaryButton().click();

        TableFragment tf = page.getCredentialStoreAliasTable();
        tf.select(MY_ALIAS);
        page.getCredentialStoreAliasTable().button("Set secret").click();

        console.addResourceDialog();
        dialog.getForm().text("Secret value", "1");
        dialog.getPrimaryButton().click();

        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.ELYTRON_CREDENTIAL_STORE);
        page.getCredentialStoreTable().select(MY_STORE);
        page.getCredentialStoreTable().getRoot().findElement(By.id(HAL_UID_0)).click();
        try {
            page.getCredentialStoreAliasTable().select(MY_ALIAS);
        } catch (NoSuchElementException e) {
            Assert.fail("Test fails. Maybe due to HAL-1762. " + e.getLocalizedMessage());
        }

    }
}
