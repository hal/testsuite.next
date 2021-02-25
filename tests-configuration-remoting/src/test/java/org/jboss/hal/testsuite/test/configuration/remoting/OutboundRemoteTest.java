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
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.AddRemoteSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.RemotingPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.fixtures.RemotingFixtures.*;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.LOCALHOST;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.OUTBOUND_REMOTE_PORT;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.OUTBOUND_REMOTE_READ;
import static org.jboss.hal.testsuite.fixtures.SocketBindingFixtures.STANDARD_SOCKETS;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class OutboundRemoteTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddRemoteSocketBinding(OUTBOUND_REMOTE_READ, LOCALHOST, OUTBOUND_REMOTE_PORT));

        operations.add(outboundRemoteAddress(REMOTE_OUTBOUND_READ),
                Values.of(OUTBOUND_SOCKET_BINDING_REF, OUTBOUND_REMOTE_READ));
        operations.add(outboundRemoteAddress(REMOTE_OUTBOUND_UPDATE),
                Values.of(OUTBOUND_SOCKET_BINDING_REF, OUTBOUND_REMOTE_READ));
        operations.add(outboundRemoteAddress(REMOTE_OUTBOUND_DELETE),
                Values.of(OUTBOUND_SOCKET_BINDING_REF, OUTBOUND_REMOTE_READ));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(outboundRemoteAddress(REMOTE_OUTBOUND_CREATE));
        operations.removeIfExists(outboundRemoteAddress(REMOTE_OUTBOUND_READ));
        operations.removeIfExists(outboundRemoteAddress(REMOTE_OUTBOUND_UPDATE));
        operations.removeIfExists(outboundRemoteAddress(REMOTE_OUTBOUND_DELETE));

        client.apply(new RemoveLocalSocketBinding(OUTBOUND_REMOTE_READ));
        Address socketBindingAddress = Address.of(SOCKET_BINDING_GROUP, STANDARD_SOCKETS)
                .and(REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING, OUTBOUND_REMOTE_READ);
        operations.removeIfExists(socketBindingAddress);
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private RemotingPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("remoting-outbound-connection-item",
                "remoting-remote-outbound-sub-item");
        table = page.getRemoteOutboundTable();
        form = page.getRemoteOutboundForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(outboundRemoteAddress(REMOTE_OUTBOUND_CREATE), table, form -> {
            form.text(NAME, REMOTE_OUTBOUND_CREATE);
            form.text(OUTBOUND_SOCKET_BINDING_REF, OUTBOUND_REMOTE_READ);
        });
    }

    @Test
    public void read() {
        table.select(REMOTE_OUTBOUND_READ);
        form.showSensitive(OUTBOUND_SOCKET_BINDING_REF);
        assertEquals(OUTBOUND_REMOTE_READ, form.value(OUTBOUND_SOCKET_BINDING_REF));
    }

    @Test
    public void update() throws Exception {
        ModelNode properties = Random.properties(BACKLOG, "34");

        table.select(REMOTE_OUTBOUND_UPDATE);
        crud.update(outboundRemoteAddress(REMOTE_OUTBOUND_UPDATE), form,
                f -> f.properties(PROPERTY).add(properties),
                resourceVerifier -> {
                    // properties are nested resources!
                    ResourceVerifier propertyVerifier = new ResourceVerifier(
                            outboundRemoteAddress(REMOTE_OUTBOUND_UPDATE).and(PROPERTY, BACKLOG), client);
                    propertyVerifier.verifyAttribute(VALUE, "34");
                });
    }

    @Test
    public void delete() throws Exception {
        crud.delete(outboundRemoteAddress(REMOTE_OUTBOUND_DELETE), table, REMOTE_OUTBOUND_DELETE);
    }
}
