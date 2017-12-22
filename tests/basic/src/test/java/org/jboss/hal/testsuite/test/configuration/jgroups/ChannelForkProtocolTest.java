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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROPERTIES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STACK;
import static org.jboss.hal.testsuite.test.configuration.jgroups.JGroupsFixtures.*;

@RunWith(Arquillian.class)
public class ChannelForkProtocolTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(channelAddress(CHANNEL_CREATE), Values.of(STACK, TCP));
        operations.add(forkAddress(CHANNEL_CREATE, FORK_CREATE));
        operations.add(forkProtocolAddress(CHANNEL_CREATE, FORK_CREATE, FORK_PROTOCOL_UPDATE));
        operations.add(forkProtocolAddress(CHANNEL_CREATE, FORK_CREATE, FORK_PROTOCOL_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(channelAddress(CHANNEL_CREATE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private JGroupsPage page;
    private TableFragment channelTable;
    private TableFragment forkTable;
    private TableFragment forkProtocolTable;
    private FormFragment forkProtocolForm;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("jgroups-channel-item");

        channelTable = page.getChannelTable();
        forkTable = page.getChannelForkTable();
        forkProtocolTable = page.getForkProtocolTable();
        forkProtocolForm = page.getForkProtocolForm();
    }

    @Test
    public void create() throws Exception {
        channelTable.action(CHANNEL_CREATE, Names.FORK);
        waitGui().until().element(forkTable.getRoot()).is().visible();

        forkTable.select(FORK_CREATE);
        forkTable.action(FORK_CREATE, Names.PROTOCOL);
        waitGui().until().element(forkProtocolTable.getRoot()).is().visible();

        crud.create(forkProtocolAddress(CHANNEL_CREATE, FORK_CREATE, FORK_PROTOCOL_CREATE), forkProtocolTable,
                FORK_PROTOCOL_CREATE);
    }

    @Test
    public void update() throws Exception {
        channelTable.action(CHANNEL_CREATE, Names.FORK);
        waitGui().until().element(forkTable.getRoot()).is().visible();

        forkTable.select(FORK_CREATE);
        forkTable.action(FORK_CREATE, Names.PROTOCOL);
        waitGui().until().element(forkProtocolTable.getRoot()).is().visible();

        forkProtocolTable.select(FORK_PROTOCOL_UPDATE);
        crud.update(forkProtocolAddress(CHANNEL_CREATE, FORK_CREATE, FORK_PROTOCOL_UPDATE), forkProtocolForm,
                PROPERTIES, Random.properties());
    }

    @Test
    public void remove() throws Exception {
        channelTable.action(CHANNEL_CREATE, Names.FORK);
        waitGui().until().element(forkTable.getRoot()).is().visible();

        forkTable.select(FORK_CREATE);
        forkTable.action(FORK_CREATE, Names.PROTOCOL);
        waitGui().until().element(forkProtocolTable.getRoot()).is().visible();

        crud.delete(forkProtocolAddress(CHANNEL_CREATE, FORK_CREATE, FORK_PROTOCOL_DELETE), forkProtocolTable,
                FORK_PROTOCOL_DELETE);
    }
}
