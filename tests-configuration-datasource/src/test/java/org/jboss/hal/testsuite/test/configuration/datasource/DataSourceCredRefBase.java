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

import java.util.List;

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.DataSourcePage;
import org.jboss.hal.testsuite.util.Library;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CREATE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PASSWORD;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RELATIVE_TO;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.DATA_SOURCE_UPDATE;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.H2_PASSWORD;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.H2_USER_NAME;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.dataSourceAddress;
import static org.jboss.hal.testsuite.fixtures.DataSourceFixtures.h2ConnectionUrl;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.CRED_ST_UPDATE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.credentialStoreAddress;

public abstract class DataSourceCredRefBase {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);
    protected static final Administration admin = new Administration(client);
    protected static final String ALIAS_VALUE = Random.name();

    @BeforeClass
    public static void beforeClass() throws Exception {
        Values credParams = Values
                .of(PATH, CRED_ST_UPDATE)
                .and(RELATIVE_TO, "jboss.server.config.dir")
                .and(CREATE, true)
                .and(CREDENTIAL_REFERENCE, CredentialReference.clearText("secret"));
        operations.add(credentialStoreAddress(CRED_ST_UPDATE), credParams);
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_UPDATE)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_UPDATE))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_UPDATE))
                .usernameAndPassword(H2_USER_NAME, H2_PASSWORD)
                .build());
        Batch credRef = new Batch();
        credRef.undefineAttribute(dataSourceAddress(DATA_SOURCE_UPDATE), PASSWORD);
        credRef.writeAttribute(dataSourceAddress(DATA_SOURCE_UPDATE), CREDENTIAL_REFERENCE,
                CredentialReference.storeAlias(CRED_ST_UPDATE, ALIAS_VALUE, H2_PASSWORD));
        operations.batch(credRef);
        admin.reloadIfRequired();
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_UPDATE));
        operations.removeIfExists(credentialStoreAddress(CRED_ST_UPDATE));
        admin.reloadIfRequired();
    }

    @Page
    protected DataSourcePage page;
    protected FormFragment form;

    @Before
    public void setUp() {
        page.navigate(NAME, DATA_SOURCE_UPDATE);
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, CREDENTIAL_REFERENCE, Ids.TAB));
        form = page.getCredentialReferenceForm();
    }

    protected void reload() throws Exception {
        admin.reloadIfRequired();
        Library.letsSleep(3000);
    }

    protected boolean assertAlias(ModelNodeResult result, String expectedAlias) {
        if (result.isSuccess()) {
            List<ModelNode> aliases = result.value().asList();
            if (!aliases.isEmpty()) {
                return aliases.stream()
                        .map(ModelNode::asString)
                        .anyMatch(actualAlias -> actualAlias.equals(expectedAlias));
            }
        }
        return false;
    }
}
