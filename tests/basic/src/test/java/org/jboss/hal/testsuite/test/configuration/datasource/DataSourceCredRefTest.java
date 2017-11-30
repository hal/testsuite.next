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

import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.DataSourcePage;
import org.jboss.hal.testsuite.test.configuration.CredentialReferenceTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.DATA_SOURCE_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.dataSourceAddress;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.h2ConnectionUrl;

@RunWith(Arquillian.class)
public class DataSourceCredRefTest extends CredentialReferenceTest {

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

    @Override
    protected FormFragment form() {
        return form;
    }

    @Override
    protected ResourceVerifier resourceVerifier() {
        return new ResourceVerifier(dataSourceAddress(DATA_SOURCE_UPDATE), client);
    }
}
