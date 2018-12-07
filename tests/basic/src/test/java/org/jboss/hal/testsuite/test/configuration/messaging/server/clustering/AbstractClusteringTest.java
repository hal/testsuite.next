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
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

public class AbstractClusteringTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);
    protected static String anyString = Random.name();
    protected static String crTab = Ids.build(MESSAGING_SERVER, BRIDGE, CREDENTIAL_REFERENCE, TAB);
    protected static Values CC_PARAMS = Values.of(CLUSTER_CONNECTION_ADDRESS, anyString)
            .and(CONNECTOR_NAME, HTTP_CONNECTOR)
            .and(DISCOVERY_GROUP, anyString);
    protected static Values GH_PARAMS = Values.of(GROUPING_HANDLER_ADDRESS, anyString)
            .and(TYPE, REMOTE);
    protected static Values BRIDGE_PARAMS = Values.of(QUEUE_NAME, anyString)
            .andList(STATIC_CONNECTORS, HTTP_CONNECTOR);

    @BeforeClass
    public static void beforeTests() throws Exception {
        Batch batchSrvUpd = new Batch();
        batchSrvUpd.add(serverAddress(SRV_UPDATE));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvUpd);
        operations.add(broadcastGroupAddress(SRV_UPDATE, BG_UPDATE));
        operations.add(broadcastGroupAddress(SRV_UPDATE, BG_DELETE));
        operations.add(discoveryGroupAddress(SRV_UPDATE, DG_UPDATE));
        operations.add(discoveryGroupAddress(SRV_UPDATE, DG_UPDATE_ALTERNATIVES));
        operations.add(discoveryGroupAddress(SRV_UPDATE, DG_DELETE));
        operations.add(clusterConnectionAddress(SRV_UPDATE, CC_UPDATE), CC_PARAMS);
        operations.add(clusterConnectionAddress(SRV_UPDATE, CC_UPDATE_ALTERNATIVES), CC_PARAMS);
        operations.add(clusterConnectionAddress(SRV_UPDATE, CC_DELETE), CC_PARAMS);
        operations.add(groupingHandlerAddress(SRV_UPDATE, GH_UPDATE), GH_PARAMS);
        operations.add(groupingHandlerAddress(SRV_UPDATE, GH_DELETE), GH_PARAMS);
        operations.add(bridgeAddress(SRV_UPDATE, BRIDGE_UPDATE), BRIDGE_PARAMS);
        operations.add(bridgeAddress(SRV_UPDATE, BRIDGE_DELETE), BRIDGE_PARAMS);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(serverAddress(SRV_UPDATE));
    }

    @Page protected MessagingServerClusteringPage page;
    @Inject protected Console console;
    @Inject protected CrudOperations crudOperations;
    @Drone protected WebDriver browser;

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }
}
