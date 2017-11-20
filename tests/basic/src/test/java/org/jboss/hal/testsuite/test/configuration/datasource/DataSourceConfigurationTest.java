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
import org.wildfly.extras.creaper.commands.datasources.AddDataSource;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.DATA_SOURCE_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.dataSourceAddress;
import static org.jboss.hal.testsuite.test.configuration.datasource.DataSourceFixtures.h2ConnectionUrl;

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

    @Inject private Console console;
    @Page private DataSourcePage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, DATA_SOURCE_UPDATE);
    }

    @Test
    public void attributes() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "attributes", Ids.TAB));
        form = page.getAttributesForm();

        String jndiName = Random.jndiName();
        form.edit();
        form.text(JNDI_NAME, jndiName);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_UPDATE), client)
                .verifyAttribute(JNDI_NAME, jndiName);
    }

    @Test
    public void connection() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "connection", Ids.TAB));
        form = page.getConnectionForm();

        String urlDelimiter = Random.name();
        form.edit();
        form.text("url-delimiter", urlDelimiter);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_UPDATE), client)
                .verifyAttribute("url-delimiter", urlDelimiter);
    }

    @Test
    public void pool() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "pool", Ids.TAB));
        form = page.getPoolForm();

        int minPoolSize = Random.number(1, 10);
        int maxPoolSize = Random.number(10, 100);
        form.edit();
        form.number(MIN_POOL_SIZE, minPoolSize);
        form.number(MAX_POOL_SIZE, maxPoolSize);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_UPDATE), client)
                .verifyAttribute(MIN_POOL_SIZE, minPoolSize)
                .verifyAttribute(MAX_POOL_SIZE, maxPoolSize);
    }

    @Test
    public void security() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "security", Ids.TAB));
        form = page.getSecurityForm();

        String username = Random.name();
        String password = Random.name();
        form.edit();
        form.text(USER_NAME, username);
        form.text(PASSWORD, password);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_UPDATE), client)
                .verifyAttribute(USER_NAME, username)
                .verifyAttribute(PASSWORD, password);

    }

    @Test
    public void credentialReference() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "credential-reference", Ids.TAB));
        form = page.getCredentialReferenceForm();
    }

    @Test
    public void validation() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "validation", Ids.TAB));
        form = page.getValidationForm();

        String className = Random.name();
        ModelNode modelNode = new ModelNode();
        modelNode.get("a").set("b");
        modelNode.get("c").set("d");
        long millis = Random.number(1000L, 2000L);

        form.edit();
        form.text("valid-connection-checker-class-name", className);
        form.properties("valid-connection-checker-properties").add(modelNode);
        form.flip("background-validation", true);
        form.number("background-validation-millis", millis);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_UPDATE), client)
                .verifyAttribute("valid-connection-checker-class-name", className)
                .verifyAttribute("valid-connection-checker-properties", modelNode)
                .verifyAttribute("background-validation", true)
                .verifyAttribute("background-validation-millis", millis);
    }

    @Test
    public void timeouts() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "timeouts", Ids.TAB));
        form = page.getTimeoutsForm();

        long locks = Random.number(10L, 20L);
        long millis = Random.number(1000L, 2000L);
        form.edit();
        form.number("use-try-lock", locks);
        form.number("blocking-timeout-wait-millis", millis);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_UPDATE), client)
                .verifyAttribute("use-try-lock", locks)
                .verifyAttribute("blocking-timeout-wait-millis", millis);
    }

    @Test
    public void statementsTracking() throws Exception {
        page.getTabs().select(Ids.build(Ids.DATA_SOURCE_CONFIGURATION, "statements-tracking", Ids.TAB));
        form = page.getStatementsTrackingForm();

        form.edit();
        form.flip("spy", true);
        form.flip("tracking", true);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(dataSourceAddress(DATA_SOURCE_UPDATE), client)
                .verifyAttribute("spy", true)
                .verifyAttribute("tracking", true);
    }
}
