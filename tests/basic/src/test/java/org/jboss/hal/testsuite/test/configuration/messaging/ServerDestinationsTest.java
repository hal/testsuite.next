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
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ROLE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.*;
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
        operations.add(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_UPDATE), Values.ofList(ENTRIES, Random.name()));
        operations.add(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_DELETE), Values.ofList(ENTRIES, Random.name()));

        Batch addSecurity = new Batch();
        addSecurity.add(securitySettingAddress(SRV_UPDATE, SECSET_UPDATE));
        addSecurity.add(securitySettingRoleAddress(SRV_UPDATE, SECSET_UPDATE, ROLE_CREATE));
        Batch deleteSecurity = new Batch();
        deleteSecurity.add(securitySettingAddress(SRV_UPDATE, SECSET_DELETE));
        deleteSecurity.add(securitySettingRoleAddress(SRV_UPDATE, SECSET_DELETE, ROLE_CREATE));

        operations.batch(addSecurity);
        operations.batch(deleteSecurity);

        operations.add(addressSettingAddress(SRV_UPDATE, AS_UPDATE));
        operations.add(addressSettingAddress(SRV_UPDATE, AS_DELETE));

        operations.add(divertAddress(SRV_UPDATE, DIVERT_UPDATE),
                Values.of(DIVERT_ADDRESS, Random.name()).and(FORWARDING_ADDRESS, Random.name()));
        operations.add(divertAddress(SRV_UPDATE, DIVERT_DELETE),
                Values.of(DIVERT_ADDRESS, Random.name()).and(FORWARDING_ADDRESS, Random.name()));
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
        String val = Random.name();

        table.select(JMSQUEUE_UPDATE);
        crudOperations.update(jmsQueueAddress(SRV_UPDATE, JMSQUEUE_UPDATE), form,
                formFragment -> formFragment.list(ENTRIES).add(val),
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

    // --------------- jms topic

    @Test
    public void jmsTopicCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_TOPIC + "-" + ITEM);
        TableFragment table = page.getJmsTopicTable();
        FormFragment form = page.getJmsTopicForm();
        table.bind(form);

        crudOperations.create(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, JMSTOPIC_CREATE);
                    formFragment.properties(ENTRIES).add(Random.name());
                }
        );
    }

    @Test
    public void jmsTopicUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_TOPIC + "-" + ITEM);
        TableFragment table = page.getJmsTopicTable();
        FormFragment form = page.getJmsTopicForm();
        table.bind(form);
        String val = Random.name();

        table.select(JMSTOPIC_UPDATE);
        crudOperations.update(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_UPDATE), form,
                formFragment -> formFragment.list(ENTRIES).add(val),
                resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(ENTRIES, val));
    }

    @Test
    public void jmsTopicRemove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_TOPIC + "-" + ITEM);
        TableFragment table = page.getJmsTopicTable();
        FormFragment form = page.getJmsTopicForm();
        table.bind(form);

        crudOperations.delete(jmsTopicAddress(SRV_UPDATE, JMSTOPIC_DELETE), table, JMSTOPIC_DELETE);
    }

    // --------------- security setting

    @Test
    public void securitySettingCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SECURITY_SETTING_ROLE + "-" + ITEM);
        TableFragment table = page.getSecuritySettingTable();
        FormFragment form = page.getSecuritySettingForm();
        table.bind(form);

        crudOperations.create(securitySettingAddress(SRV_UPDATE, SECSET_CREATE), table,
                formFragment -> {
                    formFragment.text(PATTERN, SECSET_CREATE);
                    formFragment.text(ROLE, Random.name());
                }
        );
    }

    @Test
    public void securitySettingUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SECURITY_SETTING_ROLE + "-" + ITEM);
        TableFragment table = page.getSecuritySettingTable();
        FormFragment form = page.getSecuritySettingForm();
        table.bind(form);

        table.select(SECSET_UPDATE);
        crudOperations.update(securitySettingRoleAddress(SRV_UPDATE, SECSET_UPDATE, ROLE_CREATE), form, CONSUME, true);
    }

    @Test
    public void securitySettingRemove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SECURITY_SETTING_ROLE + "-" + ITEM);
        TableFragment table = page.getSecuritySettingTable();
        FormFragment form = page.getSecuritySettingForm();
        table.bind(form);

        crudOperations.delete(securitySettingAddress(SRV_UPDATE, SECSET_DELETE), table, SECSET_DELETE);
    }

    // --------------- address setting

    @Test
    public void addressSettingCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_ADDRESS_SETTING + "-" + ITEM);
        TableFragment table = page.getAddressSettingTable();
        FormFragment form = page.getAddressSettingForm();
        table.bind(form);

        crudOperations.create(addressSettingAddress(SRV_UPDATE, AS_CREATE), table, AS_CREATE);
    }

    @Test
    public void addressSettingUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_ADDRESS_SETTING + "-" + ITEM);
        TableFragment table = page.getAddressSettingTable();
        FormFragment form = page.getAddressSettingForm();
        table.bind(form);

        table.select(AS_UPDATE);
        crudOperations.update(addressSettingAddress(SRV_UPDATE, AS_UPDATE), form, DEAD_LETTER_ADDRESS);
    }

    @Test
    public void addressSettingRemove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_ADDRESS_SETTING + "-" + ITEM);
        TableFragment table = page.getAddressSettingTable();
        FormFragment form = page.getAddressSettingForm();
        table.bind(form);

        crudOperations.delete(addressSettingAddress(SRV_UPDATE, AS_DELETE), table, AS_DELETE);
    }

    // --------------- divert

    @Test
    public void divertCreate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + "-" + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        crudOperations.create(divertAddress(SRV_UPDATE, DIVERT_CREATE), table, f -> {
            f.text(NAME, DIVERT_CREATE);
            f.text(DIVERT_ADDRESS, Random.name());
            f.text(FORWARDING_ADDRESS, Random.name());
        });
    }

    @Test
    public void divertCreateRequiredField() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + "-" + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        crudOperations.createWithError(table, DIVERT_CREATE, DIVERT_ADDRESS);
    }

    @Test
    public void divertUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + "-" + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        table.select(DIVERT_UPDATE);
        crudOperations.update(divertAddress(SRV_UPDATE, DIVERT_UPDATE), form, DIVERT_ADDRESS);
    }

    @Test
    public void divertUpdateRequiredField() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + "-" + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        table.select(DIVERT_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(DIVERT_ADDRESS), DIVERT_ADDRESS);
    }

    @Test
    public void divertRemove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + "-" + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        crudOperations.delete(divertAddress(SRV_UPDATE, DIVERT_DELETE), table, DIVERT_DELETE);
    }

}
