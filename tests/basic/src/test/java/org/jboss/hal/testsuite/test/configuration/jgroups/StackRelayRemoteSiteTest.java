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

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.jgroups.JGroupsFixtures.*;

@RunWith(Arquillian.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class StackRelayRemoteSiteTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);


    @BeforeClass
    public static void beforeClass() throws Exception {
        Batch stackCreate = new Batch();
        stackCreate.add(stackAddress(STACK_CREATE));
        stackCreate.add(transportAddress(STACK_CREATE, TRANSPORT_CREATE), Values.of(SOCKET_BINDING, JGROUPS_TCP));
        operations.batch(stackCreate);
        operations.add(channelAddress(CHANNEL_CREATE), Values.of(STACK, TCP));
        operations.add(relayAddress(STACK_CREATE), Values.of(SITE, Random.name()));
        operations.add(relayRemoteSiteAddress(STACK_CREATE, REMOTESITE_UPDATE), Values.of(CHANNEL, EE));
        operations.add(relayRemoteSiteAddress(STACK_CREATE, REMOTESITE_DELETE), Values.of(CHANNEL, EE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(stackAddress(STACK_CREATE));
        operations.remove(channelAddress(CHANNEL_CREATE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private JGroupsPage page;
    private TableFragment stackTable;
    private TableFragment relayTable;
    private TableFragment remoteSiteTable;
    private FormFragment remoteSiteForm;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("jgroups-stack-item");

        stackTable = page.getStackTable();
        relayTable = page.getRelayTable();
        remoteSiteTable = page.getRelayRemoteSiteTable();
        remoteSiteForm = page.getRelayRemoteSiteForm();
        remoteSiteTable.bind(remoteSiteForm);
    }

    @Test
    public void create() throws Exception {
        stackTable.action(STACK_CREATE, Names.RELAY);
        waitGui().until().element(relayTable.getRoot()).is().visible();

        relayTable.select(RELAY.toUpperCase());
        relayTable.action(RELAY.toUpperCase(), Names.REMOTE_SITE);
        waitGui().until().element(remoteSiteTable.getRoot()).is().visible();

        crud.create(relayRemoteSiteAddress(STACK_CREATE, REMOTESITE_CREATE), remoteSiteTable,
                form -> {
                    form.text(NAME, REMOTESITE_CREATE);
                    form.text(CHANNEL, EE);
                },
                resourceVerifier -> resourceVerifier.verifyAttribute(CHANNEL, EE));
    }

    @Test()
    public void update() throws Exception {
        stackTable.action(STACK_CREATE, Names.RELAY);
        waitGui().until().element(relayTable.getRoot()).is().visible();

        relayTable.select(RELAY.toUpperCase());
        relayTable.action(RELAY.toUpperCase(), Names.REMOTE_SITE);
        waitGui().until().element(remoteSiteTable.getRoot()).is().visible();

        remoteSiteTable.select(REMOTESITE_UPDATE);
        crud.update(relayRemoteSiteAddress(STACK_CREATE, REMOTESITE_UPDATE), remoteSiteForm, CHANNEL, CHANNEL_CREATE);
    }

    @Test()
    public void updateEmptyEE() {
        stackTable.action(STACK_CREATE, Names.RELAY);
        waitGui().until().element(relayTable.getRoot()).is().visible();

        relayTable.select(RELAY.toUpperCase());
        relayTable.action(RELAY.toUpperCase(), Names.REMOTE_SITE);
        waitGui().until().element(remoteSiteTable.getRoot()).is().visible();

        remoteSiteTable.select(REMOTESITE_UPDATE);
        crud.updateWithError(remoteSiteForm, form -> form.clear(CHANNEL), CHANNEL);
    }

    @Test
    public void remove() throws Exception {
        stackTable.action(STACK_CREATE, Names.RELAY);
        waitGui().until().element(relayTable.getRoot()).is().visible();

        relayTable.select(RELAY.toUpperCase());
        relayTable.action(RELAY.toUpperCase(), Names.REMOTE_SITE);
        waitGui().until().element(remoteSiteTable.getRoot()).is().visible();

        crud.delete(relayRemoteSiteAddress(STACK_CREATE, REMOTESITE_DELETE), remoteSiteTable, REMOTESITE_DELETE);
    }
}
