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
package org.jboss.hal.testsuite.test.configuration.messaging.server.connections;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.MessagingServerConnectionsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ENDPOINT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

public abstract class AbstractServerConnectionsTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);
    protected static String anyString = Random.name();
    protected static String crTab = Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, CREDENTIAL_REFERENCE, TAB);

    @BeforeClass
    public static void beforeTests() throws Exception {

        Batch batchSrvUpd = new Batch();
        batchSrvUpd.add(serverAddress(SRV_UPDATE));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvUpd);
        operations.add(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_UPDATE), Values.of(FACTORY_CLASS, anyString));
        operations.add(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_TRY_UPDATE), Values.of(FACTORY_CLASS, anyString));
        operations.add(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_DELETE), Values.of(FACTORY_CLASS, anyString));
        operations.add(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_UPDATE), Values.of(SERVER_ID, 11));
        operations.add(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_TRY_UPDATE), Values.of(SERVER_ID, 12));
        operations.add(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_DELETE), Values.of(SERVER_ID, 22));
        operations.add(acceptorHttpAddress(SRV_UPDATE, ACCP_HTTP_UPDATE), Values.of(HTTP_LISTENER, DEFAULT));
        operations.add(acceptorHttpAddress(SRV_UPDATE, ACCP_HTTP_DELETE), Values.of(HTTP_LISTENER, DEFAULT));
        operations.add(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_UPDATE), Values.of(SOCKET_BINDING, HTTP));
        operations.add(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_TRY_UPDATE), Values.of(SOCKET_BINDING, HTTP));
        operations.add(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_DELETE), Values.of(SOCKET_BINDING, HTTP));

        operations.add(connectorGenericAddress(SRV_UPDATE, CONN_GEN_UPDATE), Values.of(FACTORY_CLASS, anyString));
        operations.add(connectorGenericAddress(SRV_UPDATE, CONN_GEN_DELETE), Values.of(FACTORY_CLASS, anyString));
        operations.add(connectorInVMAddress(SRV_UPDATE, CONN_INVM_UPDATE), Values.of(SERVER_ID, 11));
        operations.add(connectorInVMAddress(SRV_UPDATE, CONN_INVM_TRY_UPDATE), Values.of(SERVER_ID, 12));
        operations.add(connectorInVMAddress(SRV_UPDATE, CONN_INVM_DELETE), Values.of(SERVER_ID, 22));
        operations.add(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_UPDATE),
                Values.of(ENDPOINT, HTTP_ACCEPTOR).and(SOCKET_BINDING, HTTP));
        operations.add(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_TRY_UPDATE),
                Values.of(ENDPOINT, HTTP_ACCEPTOR).and(SOCKET_BINDING, HTTP));
        operations.add(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_DELETE),
                Values.of(ENDPOINT, DEFAULT).and(SOCKET_BINDING, HTTP));
        operations.add(connectorRemoteAddress(SRV_UPDATE, CONN_REM_UPDATE), Values.of(SOCKET_BINDING, HTTP));
        operations.add(connectorRemoteAddress(SRV_UPDATE, CONN_REM_DELETE), Values.of(SOCKET_BINDING, HTTP));

        operations.add(connectorServiceAddress(SRV_UPDATE, CONN_SVC_UPDATE), Values.of(FACTORY_CLASS, anyString));
        operations.add(connectorServiceAddress(SRV_UPDATE, CONN_SVC_DELETE), Values.of(FACTORY_CLASS, anyString));

        operations.add(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_UPDATE),
                Values.ofList(ENTRIES, anyString).and(DISCOVERY_GROUP, anyString));
        operations.add(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_TRY_UPDATE),
                Values.ofList(ENTRIES, anyString).and(DISCOVERY_GROUP, anyString));
        operations.add(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_DELETE),
                Values.ofList(ENTRIES, anyString).and(DISCOVERY_GROUP, anyString));

        operations.add(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE),
                Values.ofList(ENTRIES, anyString).and(DISCOVERY_GROUP, anyString));
        operations.add(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_TRY_UPDATE),
                Values.ofList(ENTRIES, anyString).and(DISCOVERY_GROUP, anyString));
        operations.add(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_DELETE),
                Values.ofList(ENTRIES, anyString).and(DISCOVERY_GROUP, anyString));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(serverAddress(SRV_UPDATE));
    }

    @Page protected MessagingServerConnectionsPage page;
    @Inject protected Console console;
    @Inject protected CrudOperations crudOperations;
    @Drone protected WebDriver browser;

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }
}
