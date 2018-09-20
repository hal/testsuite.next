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
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
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
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.OUTBOUND_SOCKET_BINDING_REF;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROPERTY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.test.configuration.remoting.RemotingFixtures.BACKLOG;
import static org.jboss.hal.testsuite.test.configuration.remoting.RemotingFixtures.LOCAL_OUTBOUND_CREATE;
import static org.jboss.hal.testsuite.test.configuration.remoting.RemotingFixtures.LOCAL_OUTBOUND_DELETE;
import static org.jboss.hal.testsuite.test.configuration.remoting.RemotingFixtures.LOCAL_OUTBOUND_READ;
import static org.jboss.hal.testsuite.test.configuration.remoting.RemotingFixtures.LOCAL_OUTBOUND_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.OUTBOUND_LOCAL_READ;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class OutboundLocalTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(new AddLocalSocketBinding(OUTBOUND_LOCAL_READ));

        operations.add(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_READ),
                Values.of(OUTBOUND_SOCKET_BINDING_REF, OUTBOUND_LOCAL_READ));
        operations.add(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_UPDATE),
                Values.of(OUTBOUND_SOCKET_BINDING_REF, OUTBOUND_LOCAL_READ));
        operations.add(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_DELETE),
                Values.of(OUTBOUND_SOCKET_BINDING_REF, OUTBOUND_LOCAL_READ));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_CREATE));
        operations.removeIfExists(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_READ));
        operations.removeIfExists(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_UPDATE));
        operations.removeIfExists(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_DELETE));

        client.apply(new RemoveLocalSocketBinding(OUTBOUND_LOCAL_READ));
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
                "remoting-local-outbound-sub-item");
        table = page.getLocalOutboundTable();
        form = page.getLocalOutboundForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_CREATE), table, form -> {
            form.text(NAME, LOCAL_OUTBOUND_CREATE);
            form.text(OUTBOUND_SOCKET_BINDING_REF, OUTBOUND_LOCAL_READ);
        });
    }

    @Test
    public void read() {
        table.select(LOCAL_OUTBOUND_READ);
        form.showSensitive(OUTBOUND_SOCKET_BINDING_REF);
        assertEquals(OUTBOUND_LOCAL_READ, form.value(OUTBOUND_SOCKET_BINDING_REF));
    }

    @Test
    public void update() throws Exception {
        ModelNode properties = Random.properties(BACKLOG, "12");

        table.select(LOCAL_OUTBOUND_UPDATE);
        crud.update(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_UPDATE), form,
                f -> f.properties(PROPERTY).add(properties),
                resourceVerifier -> {
                    // properties are nested resources!
                    ResourceVerifier propertyVerifier = new ResourceVerifier(
                            RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_UPDATE).and(PROPERTY, BACKLOG), client);
                    propertyVerifier.verifyAttribute(VALUE, "12");
                });
    }

    @Test
    public void delete() throws Exception {
        crud.delete(RemotingFixtures.outboundLocalAddress(LOCAL_OUTBOUND_DELETE), table, LOCAL_OUTBOUND_DELETE);
    }
}
