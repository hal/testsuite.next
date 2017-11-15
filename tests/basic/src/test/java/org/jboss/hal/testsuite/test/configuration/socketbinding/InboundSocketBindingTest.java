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
package org.jboss.hal.testsuite.test.configuration.socketbinding;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.SocketBindingPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PORT;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.*;

@RunWith(Arquillian.class)
public class InboundSocketBindingTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(socketBindingAddress(STANDARD_SOCKETS, INBOUND_UPDATE));
        operations.add(socketBindingAddress(STANDARD_SOCKETS, INBOUND_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(socketBindingAddress(STANDARD_SOCKETS, INBOUND_CREATE));
        operations.removeIfExists(socketBindingAddress(STANDARD_SOCKETS, INBOUND_UPDATE));
        operations.removeIfExists(socketBindingAddress(STANDARD_SOCKETS, INBOUND_DELETE));
    }

    @Inject private Console console;
    @Page private SocketBindingPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, STANDARD_SOCKETS);
        console.verticalNavigation().selectPrimary(Ids.SOCKET_BINDING_GROUP_INBOUND + "-" + Ids.ITEM);

        table = page.getInboundTable();
        form = page.getInboundForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, INBOUND_CREATE);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(socketBindingAddress(STANDARD_SOCKETS, INBOUND_CREATE), client)
                .verifyExists();
    }

    @Test
    public void update() throws Exception {
        int port = 1234;

        table.select(INBOUND_UPDATE);
        form.edit();
        form.number(PORT, port);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(socketBindingAddress(STANDARD_SOCKETS, INBOUND_UPDATE), client)
                .verifyAttribute(PORT, port);
    }

    @Test
    public void updateInvalidPort() throws Exception {
        int port = -1;

        table.select(INBOUND_UPDATE);
        form.edit();
        form.number(PORT, port);
        form.trySave();
        form.expectError(PORT);
    }

    @Test
    public void reset() throws Exception {
        table.select(INBOUND_UPDATE);
        form.reset();

        console.verifySuccess();
        new ResourceVerifier(socketBindingAddress(STANDARD_SOCKETS, INBOUND_UPDATE), client)
                .verifyReset();
    }

    @Test
    public void delete() throws Exception {
        table.remove(INBOUND_DELETE);

        console.verifySuccess();
        new ResourceVerifier(socketBindingAddress(STANDARD_SOCKETS, INBOUND_DELETE), client)
                .verifyDoesNotExist();
    }
}
