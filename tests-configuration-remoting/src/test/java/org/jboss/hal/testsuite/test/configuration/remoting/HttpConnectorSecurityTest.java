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
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.RemotingPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.socketbindings.AddSocketBinding;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static java.util.Arrays.asList;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.testsuite.fixtures.RemotingFixtures.*;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.STANDARD_SOCKETS;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.inboundAddress;
import static org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures.DEFAULT_SERVER;
import static org.jboss.hal.testsuite.fixtures.undertow.UndertowFixtures.httpListenerAddress;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class HttpConnectorSecurityTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddSocketBinding.Builder(HTTP_CONNECTOR_SECURITY_SOCKET).build());
        operations.add(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_SECURITY_LISTENER),
                Values.of(SOCKET_BINDING, HTTP_CONNECTOR_SECURITY_SOCKET)).assertSuccess();
        operations.add(httpConnectorAddress(HTTP_CONNECTOR_SECURITY),
                Values.of(CONNECTOR_REF, HTTP_CONNECTOR_SECURITY_LISTENER)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_SECURITY));
        operations.removeIfExists(httpListenerAddress(DEFAULT_SERVER, HTTP_CONNECTOR_SECURITY_LISTENER));
        operations.removeIfExists(inboundAddress(STANDARD_SOCKETS, HTTP_CONNECTOR_SECURITY_SOCKET));
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
        form = page.getHttpConnectorSecurityForm();
        table.bind(form);
    }

    @Test
    public void _0create() throws Exception {
        table.select(HTTP_CONNECTOR_SECURITY);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_SECURITY_TAB);
        crud.createSingleton(httpConnectorSecurityAddress(HTTP_CONNECTOR_SECURITY), form);
    }

    @Test
    public void _1update() throws Exception {
        table.select(HTTP_CONNECTOR_SECURITY);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_SECURITY_TAB);
        crud.update(httpConnectorSecurityAddress(HTTP_CONNECTOR_SECURITY), form, INCLUDE_MECHANISMS,
                asList("foo", "bar"));
    }

    @Test
    public void _2reset() throws Exception {
        table.select(HTTP_CONNECTOR_SECURITY);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_SECURITY_TAB);
        crud.reset(httpConnectorSecurityAddress(HTTP_CONNECTOR_SECURITY), form);
    }

    @Test
    public void _3delete() throws Exception {
        table.select(HTTP_CONNECTOR_SECURITY);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_SECURITY_TAB);
        crud.deleteSingleton(httpConnectorSecurityAddress(HTTP_CONNECTOR_SECURITY), form);
    }
}
