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
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.INBOUND_READ;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.STANDARD_SOCKETS;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.inboundAddress;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ConnectorTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddSocketBinding.Builder(INBOUND_READ).build());
        operations.add(connectorAddress(CONNECTOR_READ), Values.of(SOCKET_BINDING, INBOUND_READ));
        operations.add(connectorAddress(CONNECTOR_UPDATE), Values.of(SOCKET_BINDING, INBOUND_READ));
        operations.add(connectorAddress(CONNECTOR_DELETE), Values.of(SOCKET_BINDING, INBOUND_READ));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(connectorAddress(CONNECTOR_CREATE));
        operations.removeIfExists(connectorAddress(CONNECTOR_READ));
        operations.removeIfExists(connectorAddress(CONNECTOR_UPDATE));
        operations.removeIfExists(connectorAddress(CONNECTOR_DELETE));
        operations.removeIfExists(inboundAddress(STANDARD_SOCKETS, INBOUND_READ));
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
                "remoting-connector-sub-item");
        table = page.getConnectorTable();
        form = page.getConnectorAttributesForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(connectorAddress(CONNECTOR_CREATE), table, form -> {
            form.text(NAME, CONNECTOR_CREATE);
            form.text(SOCKET_BINDING, INBOUND_READ);
        });
    }

    @Test
    public void read() {
        table.select(CONNECTOR_READ);
        page.getConnectorTabs().select(Ids.REMOTING_CONNECTOR_TAB);
        form.showSensitive(SOCKET_BINDING);
        assertEquals(INBOUND_READ, form.value(SOCKET_BINDING));
    }

    @Test
    public void update() throws Exception {
        table.select(CONNECTOR_UPDATE);
        page.getConnectorTabs().select(Ids.REMOTING_CONNECTOR_TAB);
        crud.update(connectorAddress(CONNECTOR_UPDATE), form, AUTHENTICATION_PROVIDER, Random.name());
    }

    @Test
    public void reset() throws Exception {
        table.select(CONNECTOR_UPDATE);
        page.getConnectorTabs().select(Ids.REMOTING_CONNECTOR_TAB);
        crud.reset(connectorAddress(CONNECTOR_UPDATE), form);
    }
}
