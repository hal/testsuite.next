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

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.MessagingServerDestinationsPage;
import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.*;

public abstract class AbstractServerDestinationsTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);
    protected static final String ID_DELIMITER = "-";

    protected static void createServer(String name) throws IOException {
        Batch batchSrvUpd = new Batch();
        batchSrvUpd.add(serverAddress(name), Values.of(ELYTRON_DOMAIN, "ApplicationDomain"));
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

    @Drone protected WebDriver browser;
    @Page protected MessagingServerDestinationsPage page;
    @Inject protected Console console;
    @Inject protected CrudOperations crudOperations;
}
