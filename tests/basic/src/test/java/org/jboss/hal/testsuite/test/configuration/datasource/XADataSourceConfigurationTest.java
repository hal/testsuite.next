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

import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.DataSourcePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.datasources.AddXADataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.*;

@RunWith(Arquillian.class)
public class XADataSourceConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddXADataSource.Builder<>(XA_DATA_SOURCE_UPDATE)
                .driverName("h2")
                .jndiName(Random.jndiName(XA_DATA_SOURCE_UPDATE))
                .addXaDatasourceProperty("URL", h2ConnectionUrl(XA_DATA_SOURCE_UPDATE))
                .build());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(dataSourceAddress(XA_DATA_SOURCE_UPDATE));
    }

    @Page private DataSourcePage page;
    @Inject private Console console;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(NAME, XA_DATA_SOURCE_UPDATE);
        params.put("xa", "true");
        page.navigate(params);
    }

    @Test
    public void connectionAttributesProperties() throws Exception {
        page.getXaTabs().select(Ids.build(Ids.XA_DATA_SOURCE, "connection", Ids.TAB));
        form = page.getXaConnectionForm();

        ModelNode properties = Random.properties();
        String urlDelimiter = Random.name();

        // there are two test related to xa-datasource-properties, because the backend uses a composite operation
        // to write the attribute and add the properties.
        form.edit();
        form.text(URL_DELIMITER, urlDelimiter);
        form.properties(XA_DATASOURCE_PROPERTIES).removeTags();
        form.properties(XA_DATASOURCE_PROPERTIES).add(properties);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(xaDataSourceAddress(XA_DATA_SOURCE_UPDATE), client)
                .verifyAttribute(URL_DELIMITER, urlDelimiter);
        for (Property key: properties.asPropertyList()) {
            String value = key.getValue().asString();
            Address address = xaDataSourceAddress(XA_DATA_SOURCE_UPDATE).and(XA_DATASOURCE_PROPERTIES, key.getName());
            new ResourceVerifier(address, client).verifyAttribute(VALUE, value);
        }
    }

    @Test
    public void connectionProperties() throws Exception {
        page.getXaTabs().select(Ids.build(Ids.XA_DATA_SOURCE, "connection", Ids.TAB));
        form = page.getXaConnectionForm();

        ModelNode properties = Random.properties();

        form.edit();
        form.properties(XA_DATASOURCE_PROPERTIES).removeTags();
        form.properties(XA_DATASOURCE_PROPERTIES).add(properties);
        form.save();

        console.verifySuccess();
        for (Property key: properties.asPropertyList()) {
            String value = key.getValue().asString();
            Address address = xaDataSourceAddress(XA_DATA_SOURCE_UPDATE).and(XA_DATASOURCE_PROPERTIES, key.getName());
            new ResourceVerifier(address, client).verifyAttribute(VALUE, value);
        }
    }


}
