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
package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.MessagingServerDestinationsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

public abstract class AbstractServerDestinationsTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);
    protected static final String ID_DELIMITER = "-";

    @BeforeClass
    public static void beforeTests() throws Exception {
        Batch batchSrvUpd = new Batch();
        batchSrvUpd.add(serverAddress(SRV_UPDATE));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvUpd);
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

    @Drone protected WebDriver browser;
    @Page protected MessagingServerDestinationsPage page;
    @Inject protected Console console;
    @Inject protected CrudOperations crudOperations;

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }
}
