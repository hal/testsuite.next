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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RELAY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STATISTICS_ENABLED;
import static org.jboss.hal.testsuite.test.configuration.jgroups.JGroupsFixtures.*;

@RunWith(Arquillian.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class StackRelayTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);


    @BeforeClass
    public static void beforeClass() throws Exception {
        Batch stackCreate = new Batch();
        stackCreate.add(stackAddress(STACK_CREATE));
        stackCreate.add(transportAddress(STACK_CREATE, TRANSPORT_CREATE), Values.of(SOCKET_BINDING, JGROUPS_TCP));
        operations.batch(stackCreate);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(stackAddress(STACK_CREATE));
    }

    @Page private JGroupsPage page;
    @Inject private Console console;
    private FormFragment relayForm;
    private TableFragment stackTable;
    private TableFragment relayTable;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("jgroups-stack-item");

        stackTable = page.getStackTable();
        relayTable = page.getRelayTable();
        relayForm = page.getRelayForm();
        relayTable.bind(relayForm);
    }

    //----- order of tests are important

    @Test
    public void create() throws Exception {
        stackTable.action(STACK_CREATE, Names.RELAY);
        waitGui().until().element(relayTable.getRoot()).is().visible();

        String site = Random.name();
        AddResourceDialogFragment dialog = relayTable.add();
        dialog.getForm().text(SITE, site);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(relayAddress(STACK_CREATE), client)
                .verifyAttribute(SITE, site);
    }

    @Test()
    public void relayUpdate() throws Exception {
        stackTable.action(STACK_CREATE, Names.RELAY);
        waitGui().until().element(relayTable.getRoot()).is().visible();

        relayTable.select(RELAY.toUpperCase());
        relayForm.edit();
        relayForm.flip(STATISTICS_ENABLED, true);
        relayForm.save();

        console.verifySuccess();
        new ResourceVerifier(relayAddress(STACK_CREATE), client)
                .verifyAttribute(STATISTICS_ENABLED, true);
    }

    @Test
    public void remove() throws Exception {
        stackTable.action(STACK_CREATE, Names.RELAY);
        waitGui().until().element(relayTable.getRoot()).is().visible();
        relayTable.remove(RELAY.toUpperCase());

        console.verifySuccess();
        new ResourceVerifier(relayAddress(STACK_CREATE), client).verifyDoesNotExist();
    }

}
