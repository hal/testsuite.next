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
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.JGroupsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STATISTICS_ENABLED;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TRANSPORT;
import static org.jboss.hal.testsuite.test.configuration.jgroups.JGroupsFixtures.*;

@RunWith(Arquillian.class)
public class StackTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        Batch stackUpdate = new Batch();
        stackUpdate.add(stackAddress(STACK_UPDATE));
        stackUpdate.add(transportAddress(STACK_UPDATE, TRANSPORT_CREATE), Values.of(SOCKET_BINDING, JGROUPS_TCP));
        Batch stackDelete = new Batch();
        stackDelete.add(stackAddress(STACK_DELETE));
        stackDelete.add(transportAddress(STACK_DELETE, TRANSPORT_CREATE), Values.of(SOCKET_BINDING, JGROUPS_TCP));
        operations.batch(stackUpdate);
        operations.batch(stackDelete);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(stackAddress(STACK_CREATE));
        operations.removeIfExists(stackAddress(STACK_UPDATE));
        operations.removeIfExists(stackAddress(STACK_DELETE));
    }

    @Page private JGroupsPage page;
    @Inject private Console console;
    private FormFragment form;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("jgroups-stack-item");

        table = page.getStackTable();
        form = page.getStackForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        String transportName = Random.name();
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, STACK_CREATE);
        dialog.getForm().text(TRANSPORT, transportName);
        dialog.getForm().text(SOCKET_BINDING, JGROUPS_TCP);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(stackAddress(STACK_CREATE), client)
                .verifyExists();
    }

    @Test
    public void update() throws Exception {
        table.select(STACK_UPDATE);
        form.edit();
        form.flip(STATISTICS_ENABLED, true);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(stackAddress(STACK_UPDATE), client)
                .verifyAttribute(STATISTICS_ENABLED, true);
    }

    @Test
    public void delete() throws Exception {
        table.remove(STACK_DELETE);

        console.verifySuccess();
        new ResourceVerifier(stackAddress(STACK_DELETE), client).verifyDoesNotExist();
    }
}
