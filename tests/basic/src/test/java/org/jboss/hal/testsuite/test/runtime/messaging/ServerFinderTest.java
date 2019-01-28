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
package org.jboss.hal.testsuite.test.runtime.messaging;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.ItemFragment;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MESSAGING_ACTIVEMQ;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RESET_ALL_MESSAGE_COUNTERS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RESET_ALL_MESSAGE_COUNTER_HISTORIES;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.runtimeSubsystemPath;
import static org.jboss.hal.testsuite.test.runtime.messaging.MessagingFixtures.*;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class ServerFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        Batch batchSrvRead = new Batch();
        batchSrvRead.add(serverAddress(SRV_READ));
        batchSrvRead.add(serverPathAddress(SRV_READ, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvRead.add(serverPathAddress(SRV_READ, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvRead.add(serverPathAddress(SRV_READ, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvRead.add(serverPathAddress(SRV_READ, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvRead);

        Batch batchSrvReset = new Batch();
        batchSrvReset.add(serverAddress(SRV_RESET));
        batchSrvReset.add(serverPathAddress(SRV_RESET, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvReset.add(serverPathAddress(SRV_RESET, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvReset.add(serverPathAddress(SRV_RESET, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvReset.add(serverPathAddress(SRV_RESET, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvReset);

        Batch batchSrvFailover = new Batch();
        batchSrvFailover.add(serverAddress(SRV_FAILOVER));
        batchSrvFailover.add(serverPathAddress(SRV_FAILOVER, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvFailover.add(serverPathAddress(SRV_FAILOVER, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvFailover.add(serverPathAddress(SRV_FAILOVER, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvFailover.add(serverPathAddress(SRV_FAILOVER, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvFailover);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(serverAddress(SRV_READ));
        operations.removeIfExists(serverAddress(SRV_RESET));
        operations.removeIfExists(serverAddress(SRV_FAILOVER));
    }

    @Inject private Console console;
    private FinderFragment finder;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        FinderPath path = runtimeSubsystemPath(serverEnvironmentUtils.getServerHostName(), MESSAGING_ACTIVEMQ)
                .append(Ids.MESSAGING_SERVER_RUNTIME, Ids.messagingServer(SRV_READ));
        finder = console.finder(NameTokens.RUNTIME, path);
        column = finder.column(Ids.MESSAGING_SERVER_RUNTIME);
    }

    @After
    public void tearDown() throws Exception {
        console.waitNoNotification();
    }

    @Test
    public void upAndRunning() {
        assertTrue(finder.preview().getAlert().isSuccess());
    }

    @Test
    public void resetAllMessageCounters() {
        ItemFragment server = column.selectItem(Ids.messagingServer(SRV_RESET));
        server.dropdown().click("Reset");
        ResetDialog dialog = console.dialog(ResetDialog.class);
        dialog.getForm().flip(RESET_ALL_MESSAGE_COUNTERS, true);
        dialog.primaryButton();
        console.verifySuccess();
    }

    @Test
    public void resetAllMessageCounterHistories() {
        ItemFragment server = column.selectItem(Ids.messagingServer(SRV_RESET));
        server.dropdown().click("Reset");
        ResetDialog dialog = console.dialog(ResetDialog.class);
        dialog.getForm().flip(RESET_ALL_MESSAGE_COUNTER_HISTORIES, true);
        dialog.primaryButton();
        console.verifySuccess();
    }

    @Test
    public void forceFailover() {
        ItemFragment server = column.selectItem(Ids.messagingServer(SRV_FAILOVER));
        server.dropdown().click("Force Failover");
        console.confirmationDialog().confirm();
        console.verifySuccess();
    }
}
