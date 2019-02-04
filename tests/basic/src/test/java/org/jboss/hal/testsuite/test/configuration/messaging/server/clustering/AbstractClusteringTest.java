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
package org.jboss.hal.testsuite.test.configuration.messaging.server.clustering;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.MessagingServerClusteringPage;
import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

public class AbstractClusteringTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);
    protected static final String CREDENTIAL_REFERENCE_TAB = Ids.build(MESSAGING_SERVER, BRIDGE, CREDENTIAL_REFERENCE, TAB);

    protected static void createServer(String name) throws IOException {
        Batch batchSrvUpd = new Batch();
        batchSrvUpd.add(serverAddress(name));
        batchSrvUpd.add(serverPathAddress(name, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(name, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(name, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(name, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvUpd).assertSuccess();
    }

    @AfterClass
    public static void closeClient() throws Exception {
        client.close();
    }

    @Page
    protected MessagingServerClusteringPage page;
    @Inject
    protected Console console;
    @Inject
    protected CrudOperations crudOperations;
    @Drone
    protected WebDriver browser;
}
