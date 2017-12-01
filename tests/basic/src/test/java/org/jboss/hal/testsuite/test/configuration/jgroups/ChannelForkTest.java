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
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
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
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STACK;
import static org.jboss.hal.testsuite.test.configuration.jgroups.JGroupsFixtures.*;

@RunWith(Arquillian.class)
public class ChannelForkTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(channelAddress(CHANNEL_CREATE), Values.of(STACK, TCP));
        operations.add(forkAddress(CHANNEL_CREATE, FORK_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(channelAddress(CHANNEL_CREATE));
    }

    @Page private JGroupsPage page;
    @Inject private Console console;
    private TableFragment channelTable;
    private TableFragment forkTable;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("jgroups-channel-item");

        channelTable = page.getChannelTable();
        forkTable = page.getChannelForkTable();
    }

    @Test
    public void create() throws Exception {
        channelTable.action(CHANNEL_CREATE, Names.FORK);
        waitGui().until().element(forkTable.getRoot()).is().visible();

        AddResourceDialogFragment dialog = forkTable.add();
        dialog.getForm().text(NAME, FORK_CREATE);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(forkAddress(CHANNEL_CREATE, FORK_CREATE), client)
                .verifyExists();
    }

    @Test
    public void remove() throws Exception {
        channelTable.action(CHANNEL_CREATE, Names.FORK);
        waitGui().until().element(forkTable.getRoot()).is().visible();

        forkTable.remove(FORK_DELETE);

        console.verifySuccess();
        new ResourceVerifier(forkAddress(CHANNEL_CREATE, FORK_DELETE), client)
                .verifyDoesNotExist();
    }

}
