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
package org.jboss.hal.testsuite.test.configuration.datasource;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.DataSourcePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STORE;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.DATA_SOURCE_UPDATE;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.dataSourceAddress;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.h2ConnectionUrl;

@Ignore // TODO Setup Elytron resources to be used by the credential reference
@RunWith(Arquillian.class)
public class DataSourceCredRefTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_UPDATE)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_UPDATE))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_UPDATE))
                .build());
        operations.writeAttribute(dataSourceAddress(DATA_SOURCE_UPDATE),
                CREDENTIAL_REFERENCE, CredentialReference.storeAlias());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_UPDATE));
    }

    @Page private DataSourcePage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, DATA_SOURCE_UPDATE);
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, CREDENTIAL_REFERENCE, Ids.TAB));
        form = page.getCredentialReferenceForm();
    }

    protected FormFragment form() {
        return form;
    }

    protected Address address() {
        return dataSourceAddress(DATA_SOURCE_UPDATE);
    }

    @Inject
    private CrudOperations crud;

    @Test
    public void updateAliasWithoutStore() {
        crud.updateWithError(form(), f -> {
            f.clear(STORE);
            f.text(ALIAS, Random.name());
            f.clear(CLEAR_TEXT);
        }, STORE);
    }

    @Test
    public void updateStoreWithoutAlias() {
        crud.updateWithError(form(), f -> {
            f.text(STORE, Random.name());
            f.clear(ALIAS);
            f.clear(CLEAR_TEXT);
        }, ALIAS);
    }

    @Test
    public void updateStoreAndClearText() {
        crud.updateWithError(form(), f -> {
            f.text(STORE, Random.name());
            f.text(ALIAS, Random.name());
            f.text(CLEAR_TEXT, Random.name());
        }, STORE, CLEAR_TEXT);
    }

    @Test
    public void updateStoreAndAlias() throws Exception {
        String store = Ids.build(STORE, Random.name());
        String alias = Ids.build(ALIAS, Random.name());

        crud.update(address(), form(),
                f -> {
                    f.text(STORE, store);
                    f.text(ALIAS, alias);
                    f.clear(CLEAR_TEXT);
                },
                resourceVerifier -> {
                    resourceVerifier.verifyAttribute(CredentialReference.fqName(STORE), store);
                    resourceVerifier.verifyAttribute(CredentialReference.fqName(ALIAS), alias);
                });
    }

    @Test
    public void updateClearText() throws Exception {
        String clearText = Ids.build(CLEAR_TEXT, Random.name());

        crud.update(address(), form(),
                f -> {
                    f.clear(STORE);
                    f.clear(ALIAS);
                    f.text(CLEAR_TEXT, clearText);
                },
                resourceVerifier -> resourceVerifier.verifyAttribute(CredentialReference.fqName(CLEAR_TEXT),
                        clearText));
    }

    @Test
    public void zzzDelete() throws Exception {
        crud.deleteSingleton(address(), form(),
                resourceVerifier -> resourceVerifier.verifyAttributeIsUndefined(CREDENTIAL_REFERENCE));
    }

}
