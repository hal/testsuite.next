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
import org.wildfly.extras.creaper.commands.socketbindings.AddSocketBinding;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.testsuite.fixtures.RemotingFixtures.*;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.STANDARD_SOCKETS;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.inboundAddress;
import static org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures.DEFAULT_SERVER;
import static org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures.httpListenerAddress;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class HttpConnectorTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        /**
         * HTTP connector needs valid HTTP listener with socket from EAP 7.3.4
         * Create first socket, second add listener to undertow server and finaly create connector.
         */
        client.apply(new AddSocketBinding.Builder(HTTP_CONNECTOR_CREATE_SOCKET).build());
        client.apply(new AddSocketBinding.Builder(HTTP_CONNECTOR_READ_SOCKET).build());
        client.apply(new AddSocketBinding.Builder(HTTP_CONNECTOR_UPDATE_SOCKET).build());
        client.apply(new AddSocketBinding.Builder(HTTP_CONNECTOR_DELETE_SOCKET).build());

        operations.add(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_CREATE_LISTENER),
                Values.of(SOCKET_BINDING, HTTP_CONNECTOR_CREATE_SOCKET)).assertSuccess();
        operations.add(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_READ_LISTENER),
                Values.of(SOCKET_BINDING, HTTP_CONNECTOR_READ_SOCKET)).assertSuccess();
        operations.add(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_UPDATE_LISTENER),
                Values.of(SOCKET_BINDING, HTTP_CONNECTOR_UPDATE_SOCKET)).assertSuccess();
        operations.add(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_DELETE_LISTENER),
                Values.of(SOCKET_BINDING, HTTP_CONNECTOR_DELETE_SOCKET)).assertSuccess();

        operations.add(httpConnectorAddress(HTTP_CONNECTOR_READ),
                Values.of(CONNECTOR_REF, HTTP_CONNECTOR_READ_LISTENER)).assertSuccess();
        operations.add(httpConnectorAddress(HTTP_CONNECTOR_UPDATE),
                Values.of(CONNECTOR_REF, HTTP_CONNECTOR_UPDATE_LISTENER)).assertSuccess();
        operations.add(httpConnectorAddress(HTTP_CONNECTOR_DELETE),
                Values.of(CONNECTOR_REF, HTTP_CONNECTOR_DELETE_LISTENER)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_CREATE));
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_READ));
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_UPDATE));
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_DELETE));

        operations.removeIfExists(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_CREATE_LISTENER));
        operations.removeIfExists(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_READ_LISTENER));
        operations.removeIfExists(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_UPDATE_LISTENER));
        operations.removeIfExists(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_DELETE_LISTENER));

        operations.removeIfExists(inboundAddress(STANDARD_SOCKETS, HTTP_CONNECTOR_CREATE_SOCKET));
        operations.removeIfExists(inboundAddress(STANDARD_SOCKETS, HTTP_CONNECTOR_READ_SOCKET));
        operations.removeIfExists(inboundAddress(STANDARD_SOCKETS, HTTP_CONNECTOR_UPDATE_SOCKET));
        operations.removeIfExists(inboundAddress(STANDARD_SOCKETS, HTTP_CONNECTOR_DELETE_SOCKET));
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
            form.text(CONNECTOR_REF, HTTP_CONNECTOR_CREATE_LISTENER);
        });
    }

    @Test
    public void read() {
        table.select(HTTP_CONNECTOR_READ);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_TAB);
        assertEquals(HTTP_CONNECTOR_READ_LISTENER, form.value(CONNECTOR_REF));
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
