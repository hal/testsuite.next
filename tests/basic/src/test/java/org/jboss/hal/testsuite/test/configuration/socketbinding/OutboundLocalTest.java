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
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.AddLocalSocketBinding;
import org.jboss.hal.testsuite.creaper.command.RemoveLocalSocketBinding;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.SocketBindingPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.socketbindings.AddSocketBinding;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING_REF;
import static org.jboss.hal.testsuite.creaper.command.SocketBindingCommand.refName;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.*;

@RunWith(Arquillian.class)
public class OutboundLocalTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    @BeforeClass
    public static void beforeClass() throws Exception {
        client.apply(
                new AddSocketBinding.Builder(refName(OUTBOUND_LOCAL_CREATE)).build(),
                new AddLocalSocketBinding(OUTBOUND_LOCAL_UPDATE),
                new AddLocalSocketBinding(OUTBOUND_LOCAL_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        client.apply(
                new RemoveLocalSocketBinding(OUTBOUND_LOCAL_CREATE),
                new RemoveLocalSocketBinding(OUTBOUND_LOCAL_UPDATE),
                new RemoveLocalSocketBinding(OUTBOUND_LOCAL_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private SocketBindingPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, STANDARD_SOCKETS);
        console.verticalNavigation().selectPrimary(Ids.SOCKET_BINDING_GROUP_OUTBOUND_LOCAL + "-" + Ids.ITEM);

        table = page.getOutboundLocalTable();
        form = page.getOutboundLocalForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(outboundLocalAddress(STANDARD_SOCKETS, OUTBOUND_LOCAL_CREATE), table, form -> {
            form.text(NAME, OUTBOUND_LOCAL_CREATE);
            form.text(SOCKET_BINDING_REF, refName(OUTBOUND_LOCAL_CREATE));
        });
    }

    @Test
    public void update() throws Exception {
        table.select(OUTBOUND_LOCAL_UPDATE);
        crud.update(outboundLocalAddress(STANDARD_SOCKETS, OUTBOUND_LOCAL_UPDATE), form, SOURCE_PORT, 1234);
    }

    @Test
    public void updateInvalidPort() {
        table.select(OUTBOUND_LOCAL_UPDATE);
        crud.updateWithError(form, SOURCE_PORT, -1);
    }

    @Test
    public void reset() throws Exception {
        table.select(OUTBOUND_LOCAL_UPDATE);
        crud.reset(outboundLocalAddress(STANDARD_SOCKETS, OUTBOUND_LOCAL_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(outboundLocalAddress(STANDARD_SOCKETS, OUTBOUND_LOCAL_DELETE), table, OUTBOUND_LOCAL_DELETE);
    }
}
