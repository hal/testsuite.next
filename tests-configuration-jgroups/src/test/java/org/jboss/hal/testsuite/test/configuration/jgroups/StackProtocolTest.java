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
package org.jboss.hal.testsuite.test.configuration.jgroups;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.JGroupsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROPERTIES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.testsuite.fixtures.JGroupsFixtures.*;

@RunWith(Arquillian.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class StackProtocolTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration administration = new Administration(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        Batch stackCreate = new Batch();
        stackCreate.add(stackAddress(STACK_CREATE));
        stackCreate.add(transportAddress(STACK_CREATE, TRANSPORT_CREATE), Values.of(SOCKET_BINDING, JGROUPS_TCP));
        operations.batch(stackCreate).assertSuccess();
        operations.add(protocolAddress(STACK_CREATE, PROTOCOL_UPDATE)).assertSuccess();
        operations.add(protocolAddress(STACK_CREATE, PROTOCOL_DELETE)).assertSuccess();
        administration.reloadIfRequired();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(stackAddress(STACK_CREATE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private JGroupsPage page;
    private FormFragment protocolForm;
    private TableFragment stackTable;
    private TableFragment protocolTable;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("jgroups-stack-item");

        stackTable = page.getStackTable();
        protocolTable = page.getProtocolTable();
        protocolForm = page.getProtocolForm();
        protocolTable.bind(protocolForm);
    }

    //----- order of tests are important

    @Test
    public void create() throws Exception {
        stackTable.action(STACK_CREATE, Names.PROTOCOL);
        waitGui().until().element(protocolTable.getRoot()).is().visible();
        crud.create(protocolAddress(STACK_CREATE, PROTOCOL_CREATE), protocolTable, PROTOCOL_CREATE);
    }

    @Test()
    public void update() throws Exception {
        stackTable.action(STACK_CREATE, Names.PROTOCOL);
        waitGui().until().element(protocolTable.getRoot()).is().visible();
        protocolTable.select(PROTOCOL_UPDATE);
        crud.update(protocolAddress(STACK_CREATE, PROTOCOL_UPDATE), protocolForm, PROPERTIES, Random.properties());
    }

    @Test
    public void zzzDelete() throws Exception {
        stackTable.action(STACK_CREATE, Names.PROTOCOL);
        waitGui().until().element(protocolTable.getRoot()).is().visible();
        crud.delete(protocolAddress(STACK_CREATE, PROTOCOL_DELETE), protocolTable, PROTOCOL_DELETE);
    }
}
