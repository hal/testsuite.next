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
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.DataSourcePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.*;

@RunWith(Arquillian.class)
public class DataSourceConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddDataSource.Builder<>(DATA_SOURCE_UPDATE)
                .driverName("h2")
                .jndiName(Random.jndiName(DATA_SOURCE_UPDATE))
                .connectionUrl(h2ConnectionUrl(DATA_SOURCE_UPDATE))
                .build());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(dataSourceAddress(DATA_SOURCE_UPDATE));
    }

    @Inject private CrudOperations crud;
    @Page private DataSourcePage page;
    @Inject private Console console;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, DATA_SOURCE_UPDATE);
    }

    @Test
    public void attributes() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, ATTRIBUTES, Ids.TAB));
        form = page.getAttributesForm();

        String jndiName = Random.jndiName();
        crud.update(dataSourceAddress(DATA_SOURCE_UPDATE), form, JNDI_NAME, jndiName);
    }

    @Test
    public void connection() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, CONNECTION, Ids.TAB));
        form = page.getConnectionForm();

        String urlDelimiter = Random.name();
        crud.update(dataSourceAddress(DATA_SOURCE_UPDATE), form, URL_DELIMITER, urlDelimiter);
    }

    @Test
    public void connectionAttributesProperties() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, CONNECTION, Ids.TAB));
        form = page.getConnectionForm();

        ModelNode properties = Random.properties();
        String urlDelimiter = Random.name();

        // there are two test related to xa-datasource-properties, because the backend uses a composite operation
        // to write the attribute and add the properties.
        form.edit();
        form.text(URL_DELIMITER, urlDelimiter);
        form.properties(CONNECTION_PROPERTIES).removeTags();
        form.properties(CONNECTION_PROPERTIES).add(properties);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_UPDATE), client)
                .verifyAttribute(URL_DELIMITER, urlDelimiter);
        for (Property key: properties.asPropertyList()) {
            String value = key.getValue().asString();
            Address address = dataSourceAddress(DATA_SOURCE_UPDATE).and(CONNECTION_PROPERTIES, key.getName());
            new ResourceVerifier(address, client).verifyAttribute(VALUE, value);
        }
    }

    @Test
    public void connectionProperties() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, CONNECTION, Ids.TAB));
        form = page.getConnectionForm();

        ModelNode properties = Random.properties();

        form.edit();
        form.properties(CONNECTION_PROPERTIES).removeTags();
        form.properties(CONNECTION_PROPERTIES).add(properties);
        form.save();

        console.verifySuccess();
        for (Property key: properties.asPropertyList()) {
            String value = key.getValue().asString();
            Address address = dataSourceAddress(DATA_SOURCE_UPDATE).and(CONNECTION_PROPERTIES, key.getName());
            new ResourceVerifier(address, client).verifyAttribute(VALUE, value);
        }
    }

    @Test
    public void connectionPropertiesSpecialCharacters() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, CONNECTION, Ids.TAB));
        form = page.getConnectionForm();

        ModelNode properties = new ModelNode();
        properties.get("key1").set("jdbc:sybase:Tds:localhost:5000/mydatabase?JCONNECT_VERSION=6");
        properties.get("key2").set("jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=MyDatabase");
        properties.get("key3").set("jdbc:oracle:thin:@localhost:1521:orcalesid");
        properties.get("key4").set("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        properties.get("key4").set("value!@#$%&-?=man");

        form.edit();
        form.properties(CONNECTION_PROPERTIES).removeTags();
        form.properties(CONNECTION_PROPERTIES).add(properties);
        form.save();

        console.verifySuccess();
        for (Property key: properties.asPropertyList()) {
            String value = key.getValue().asString();
            Address address = dataSourceAddress(DATA_SOURCE_UPDATE).and(CONNECTION_PROPERTIES, key.getName());
            new ResourceVerifier(address, client).verifyAttribute(VALUE, value);
        }
    }

    @Test
    public void pool() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "pool", Ids.TAB));
        form = page.getPoolForm();

        int minPoolSize = Random.number(1, 10);
        int maxPoolSize = Random.number(10, 100);
        //noinspection Duplicates
        crud.update(dataSourceAddress(DATA_SOURCE_UPDATE), form,
                f -> {
                    f.number(MIN_POOL_SIZE, minPoolSize);
                    f.number(MAX_POOL_SIZE, maxPoolSize);
                },
                resourceVerifier -> {
                    resourceVerifier.verifyAttribute(MIN_POOL_SIZE, minPoolSize);
                    resourceVerifier.verifyAttribute(MAX_POOL_SIZE, maxPoolSize);
                });
    }

    @Test
    public void security() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "security", Ids.TAB));
        form = page.getSecurityForm();

        String username = Random.name();
        crud.update(dataSourceAddress(DATA_SOURCE_UPDATE), form, USER_NAME, username);
    }

    @Test
    public void credentialReference() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "credential-reference", Ids.TAB));
        form = page.getCredentialReferenceForm();
        String clearText = Random.name();

        crud.createSingleton(dataSourceAddress(DATA_SOURCE_UPDATE), form,
                f -> f.text(CLEAR_TEXT, clearText),
                resourceVerifier -> resourceVerifier.verifyAttribute(CredentialReference.fqName(CLEAR_TEXT),
                        clearText));
    }

    @Test
    public void validation() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "validation", Ids.TAB));
        form = page.getValidationForm();

        String className = Random.name();
        ModelNode properties = Random.properties();
        long millis = Random.number(1000L, 2000L);

        crud.update(dataSourceAddress(DATA_SOURCE_UPDATE), form,
                f -> {
                    f.text(VALID_CONNECTION_CHECKER_CLASS_NAME, className);
                    f.properties(VALID_CONNECTION_CHECKER_PROPERTIES).add(properties);
                    f.flip(BACKGROUND_VALIDATION, true);
                    f.number(BACKGROUND_VALIDATION_MILLIS, millis);
                },
                resourceVerifier -> {
                    resourceVerifier.verifyAttribute(VALID_CONNECTION_CHECKER_CLASS_NAME, className);
                    resourceVerifier.verifyAttribute(VALID_CONNECTION_CHECKER_PROPERTIES, properties);
                    resourceVerifier.verifyAttribute(BACKGROUND_VALIDATION, true);
                    resourceVerifier.verifyAttribute(BACKGROUND_VALIDATION_MILLIS, millis);
                });
    }

    @Test
    public void timeouts() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "timeouts", Ids.TAB));
        form = page.getTimeoutsForm();

        long locks = Random.number(10L, 20L);
        long millis = Random.number(1000L, 2000L);
        //noinspection Duplicates
        crud.update(dataSourceAddress(DATA_SOURCE_UPDATE), form,
                f -> {
                    f.number(USE_TRY_LOCK, locks);
                    f.number(BLOCKING_TIMEOUT_WAIT_MILLIS, millis);
                },
                resourceVerifier -> {
                    resourceVerifier.verifyAttribute(USE_TRY_LOCK, locks);
                    resourceVerifier.verifyAttribute(BLOCKING_TIMEOUT_WAIT_MILLIS, millis);
                });
    }

    @Test
    public void statementsTracking() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "statements-tracking", Ids.TAB));
        form = page.getStatementsTrackingForm();

        crud.update(dataSourceAddress(DATA_SOURCE_UPDATE), form,
                f -> {
                    f.flip(SPY, true);
                    f.flip(TRACKING, true);
                },
                resourceVerifier -> {
                    resourceVerifier.verifyAttribute(SPY, true);
                    resourceVerifier.verifyAttribute(TRACKING, true);
                });
    }
}
