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
package org.jboss.hal.testsuite.test.configuration.remoting;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.RemotingPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fixtures.RemotingFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class    HttpConnectorTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(httpConnectorAddress(HTTP_CONNECTOR_READ),
                Values.of(CONNECTOR_REF, httpConnectorRef(HTTP_CONNECTOR_READ)));
        operations.add(httpConnectorAddress(HTTP_CONNECTOR_UPDATE),
                Values.of(CONNECTOR_REF, httpConnectorRef(HTTP_CONNECTOR_UPDATE)));
        operations.add(httpConnectorAddress(HTTP_CONNECTOR_DELETE),
                Values.of(CONNECTOR_REF, httpConnectorRef(HTTP_CONNECTOR_DELETE)));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_CREATE));
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_READ));
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_UPDATE));
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private RemotingPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("remoting-remote-connector-item",
                "remoting-http-connector-sub-item");
        table = page.getHttpConnectorTable();
        form = page.getHttpConnectorAttributesForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(httpConnectorAddress(HTTP_CONNECTOR_CREATE), table, form -> {
            form.text(NAME, HTTP_CONNECTOR_CREATE);
            form.text(CONNECTOR_REF, httpConnectorRef(HTTP_CONNECTOR_CREATE));
        });
    }

    @Test
    public void read() {
        table.select(HTTP_CONNECTOR_READ);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_TAB);
        assertEquals(httpConnectorRef(HTTP_CONNECTOR_READ), form.value(CONNECTOR_REF));
    }

    @Test
    public void update() throws Exception {
        table.select(HTTP_CONNECTOR_UPDATE);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_TAB);
        crud.update(httpConnectorAddress(HTTP_CONNECTOR_UPDATE), form, CONNECTOR_REF, Random.name());
    }

    @Test
    public void reset() throws Exception {
        table.select(HTTP_CONNECTOR_UPDATE);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_TAB);
        crud.reset(httpConnectorAddress(HTTP_CONNECTOR_UPDATE), form);
    }
}
