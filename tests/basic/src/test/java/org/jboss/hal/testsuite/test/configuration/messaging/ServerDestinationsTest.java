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
package org.jboss.hal.testsuite.test.configuration.messaging;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.MessagingServerDestinationsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ENTRIES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.QUEUE_ADDRESS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CORE_QUEUE;
import static org.jboss.hal.resources.Ids.MESSAGING_JMS_QUEUE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

@RunWith(Arquillian.class)
public class ServerDestinationsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeTests() throws Exception {
        operations.add(serverAddress(SRV_UPDATE));
        operations.add(coreQueueAddress(SRV_UPDATE, COREQUEUE_DELETE), Values.of(QUEUE_ADDRESS, Random.name()));
        operations.add(jmsQueueAddress(SRV_UPDATE, JMSQUEUE_UPDATE), Values.ofList(ENTRIES, Random.name()));
        operations.add(jmsQueueAddress(SRV_UPDATE, JMSQUEUE_DELETE), Values.ofList(ENTRIES, Random.name()));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(serverAddress(SRV_UPDATE));
    }

    @Page private MessagingServerDestinationsPage page;
    @Inject private Console console;
    @Inject private CrudOperations crudOperations;

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }

    // --------------- core queue
    // there is no update tests as the core queue's attributes are read-only

    @Test
    public void coreQueueCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_CORE_QUEUE + "-" + ITEM);
        TableFragment table = page.getCoreQueueTable();
        FormFragment form = page.getCoreQueueForm();
        table.bind(form);

        crudOperations.create(coreQueueAddress(SRV_UPDATE, COREQUEUE_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, COREQUEUE_CREATE);
                    formFragment.text(QUEUE_ADDRESS, Random.name());
                }
        );
    }

    @Test
    public void coreQueueRemove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_CORE_QUEUE + "-" + ITEM);
        TableFragment table = page.getCoreQueueTable();
        FormFragment form = page.getCoreQueueForm();
        table.bind(form);

        crudOperations.delete(coreQueueAddress(SRV_UPDATE, COREQUEUE_DELETE), table, COREQUEUE_DELETE);
    }

    // --------------- jms queue

    @Test
    public void jmsQueueCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_QUEUE + "-" + ITEM);
        TableFragment table = page.getJmsQueueTable();
        FormFragment form = page.getJmsQueueForm();
        table.bind(form);

        crudOperations.create(jmsQueueAddress(SRV_UPDATE, JMSQUEUE_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, JMSQUEUE_CREATE);
                    formFragment.properties(ENTRIES).add(Random.name());
                }
        );
    }

    @Test
    public void jmsQueueUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_QUEUE + "-" + ITEM);
        TableFragment table = page.getJmsQueueTable();
        FormFragment form = page.getJmsQueueForm();
        table.bind(form);
        String val= Random.name();

        table.select(JMSQUEUE_UPDATE);
        crudOperations.update(jmsQueueAddress(SRV_UPDATE, JMSQUEUE_UPDATE), form,
                formFragment -> {
                    formFragment.list(ENTRIES).add(val);
                },
                resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(ENTRIES, val));
    }

    @Test
    public void jmsQueueRemove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_QUEUE + "-" + ITEM);
        TableFragment table = page.getJmsQueueTable();
        FormFragment form = page.getJmsQueueForm();
        table.bind(form);

        crudOperations.delete(jmsQueueAddress(SRV_UPDATE, JMSQUEUE_DELETE), table, JMSQUEUE_DELETE);
    }

}
