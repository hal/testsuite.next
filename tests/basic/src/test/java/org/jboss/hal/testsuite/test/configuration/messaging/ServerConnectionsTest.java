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
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.MessagingServerConnectionsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ENDPOINT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

@RunWith(Arquillian.class)
public class ServerConnectionsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String anyString = Random.name();
    private static String crTab = Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, CREDENTIAL_REFERENCE, TAB);

    @BeforeClass
    public static void beforeTests() throws Exception {

        operations.add(serverAddress(SRV_UPDATE));
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

    @Page private MessagingServerConnectionsPage page;
    @Inject private Console console;
    @Inject private CrudOperations crudOperations;

    @Before
    public void setUp() throws Exception {
        page.navigate(SERVER, SRV_UPDATE);
    }

    // --------------- acceptor - generic

    @Test
    public void acceptorGenericCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);

        crudOperations.create(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, ACCP_GEN_CREATE);
                    formFragment.text(FACTORY_CLASS, Random.name());
                }
        );
    }

    @Test
    public void acceptorGenericTryCreate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, ACCP_GEN_CREATE, FACTORY_CLASS);
    }

    @Test
    public void acceptorGenericUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);
        table.select(ACCP_GEN_UPDATE);
        crudOperations.update(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_UPDATE), form, FACTORY_CLASS);
    }

    @Test
    public void acceptorGenericTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);
        table.select(ACCP_GEN_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(FACTORY_CLASS), FACTORY_CLASS);
    }

    @Test
    public void acceptorGenericRemove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM), Ids.build(MESSAGING_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorGenericTable();
        FormFragment form = page.getAcceptorGenericForm();
        table.bind(form);

        crudOperations.delete(acceptorGenericAddress(SRV_UPDATE, ACCP_GEN_DELETE), table, ACCP_GEN_DELETE);
    }

    // --------------- acceptor - in-vm

    @Test
    public void acceptorInVMCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);

        crudOperations.create(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, ACCP_INVM_CREATE);
                    formFragment.number(SERVER_ID, 123);
                }
        );
    }

    @Test
    public void acceptorInVMTryCreate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, ACCP_INVM_CREATE, SERVER_ID);
    }

    @Test
    public void acceptorInVMUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);
        table.select(ACCP_INVM_UPDATE);
        crudOperations.update(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_UPDATE), form, SERVER_ID, 89);
    }

    @Test
    public void acceptorInVMTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);
        table.select(ACCP_INVM_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(SERVER_ID), SERVER_ID);
    }

    @Test
    public void acceptorInVMRemove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorInVMTable();
        FormFragment form = page.getAcceptorInVMForm();
        table.bind(form);

        crudOperations.delete(acceptorInVMAddress(SRV_UPDATE, ACCP_INVM_DELETE), table, ACCP_INVM_DELETE);
    }

    // --------------- acceptor - http

    @Test
    public void acceptorHttpCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);

        crudOperations.create(acceptorHttpAddress(SRV_UPDATE, ACCP_HTTP_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, ACCP_HTTP_CREATE);
                    formFragment.text(HTTP_LISTENER, DEFAULT);
                }
        );
    }

    @Test
    public void acceptorHttpTryCreate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, ACCP_HTTP_CREATE, HTTP_LISTENER);
    }

    @Test
    public void acceptorHttpUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);
        table.select(ACCP_HTTP_UPDATE);
        crudOperations.update(acceptorHttpAddress(SRV_UPDATE, ACCP_HTTP_UPDATE), form, UPGRADE_LEGACY, false);
    }

    @Test
    public void acceptorHttpTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);
        table.select(ACCP_HTTP_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(HTTP_LISTENER), HTTP_LISTENER);
    }

    @Test
    public void acceptorHttpRemove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorHttpTable();
        FormFragment form = page.getAcceptorHttpForm();
        table.bind(form);

        crudOperations.delete(acceptorHttpAddress(SRV_UPDATE, ACCP_HTTP_DELETE), table, ACCP_HTTP_DELETE);
    }


    // --------------- acceptor - remote

    @Test
    public void acceptorRemoteCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);

        crudOperations.create(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, ACCP_REM_CREATE);
                    formFragment.text(SOCKET_BINDING, HTTP);
                }
        );
    }

    @Test
    public void acceptorRemoteTryCreate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, ACCP_REM_CREATE, SOCKET_BINDING);
    }

    @Test
    public void acceptorRemoteUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);
        table.select(ACCP_REM_UPDATE);
        crudOperations.update(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_UPDATE), form, SOCKET_BINDING, HTTPS);
    }

    @Test
    public void acceptorRemoteTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);
        table.select(ACCP_REM_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(SOCKET_BINDING), SOCKET_BINDING);
    }

    @Test
    public void acceptorRemoteRemove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_ACCEPTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_ACCEPTOR, ITEM));
        TableFragment table = page.getAcceptorRemoteTable();
        FormFragment form = page.getAcceptorRemoteForm();
        table.bind(form);

        crudOperations.delete(acceptorRemoteAddress(SRV_UPDATE, ACCP_REM_DELETE), table, ACCP_REM_DELETE);
    }

    // --------------- connector - generic

    @Test
    public void connectorGenericCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);

        crudOperations.create(connectorGenericAddress(SRV_UPDATE, CONN_GEN_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, CONN_GEN_CREATE);
                    formFragment.text(FACTORY_CLASS, Random.name());
                }
        );
    }

    @Test
    public void connectorGenericTryCreate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_GEN_CREATE, FACTORY_CLASS);
    }

    @Test
    public void connectorGenericUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);
        table.select(CONN_GEN_UPDATE);
        crudOperations.update(connectorGenericAddress(SRV_UPDATE, CONN_GEN_UPDATE), form, FACTORY_CLASS);
    }

    @Test
    public void connectorGenericTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);
        table.select(CONN_GEN_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(FACTORY_CLASS), FACTORY_CLASS);
    }

    @Test
    public void connectorGenericRemove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM), Ids.build(MESSAGING_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorGenericTable();
        FormFragment form = page.getConnectorGenericForm();
        table.bind(form);

        crudOperations.delete(connectorGenericAddress(SRV_UPDATE, CONN_GEN_DELETE), table, CONN_GEN_DELETE);
    }

    // --------------- connector - in-vm

    @Test
    public void connectorInVMCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);

        crudOperations.create(connectorInVMAddress(SRV_UPDATE, CONN_INVM_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, CONN_INVM_CREATE);
                    formFragment.number(SERVER_ID, 123);
                }
        );
    }

    @Test
    public void connectorInVMTryCreate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_INVM_CREATE, SERVER_ID);
    }

    @Test
    public void connectorInVMUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);
        table.select(CONN_INVM_UPDATE);
        crudOperations.update(connectorInVMAddress(SRV_UPDATE, CONN_INVM_UPDATE), form, SERVER_ID, 89);
    }

    @Test
    public void connectorInVMTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);
        table.select(CONN_INVM_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(SERVER_ID), SERVER_ID);
    }

    @Test
    public void connectorInVMRemove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_IN_VM_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorInVMTable();
        FormFragment form = page.getConnectorInVMForm();
        table.bind(form);

        crudOperations.delete(connectorInVMAddress(SRV_UPDATE, CONN_INVM_DELETE), table, CONN_INVM_DELETE);
    }

    // --------------- connector - http

    @Test
    public void connectorHttpCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);

        crudOperations.create(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, CONN_HTTP_CREATE);
                    formFragment.text(ENDPOINT, HTTP_ACCEPTOR);
                    formFragment.text(SOCKET_BINDING, HTTP);
                }
        );
    }

    @Test
    public void connectorHttpTryCreate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_HTTP_CREATE, ENDPOINT);
    }

    @Test
    public void connectorHttpUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);
        table.select(CONN_HTTP_UPDATE);
        crudOperations.update(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_UPDATE), form, SERVER_NAME);
    }

    @Test
    public void connectorHttpTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);
        table.select(CONN_HTTP_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(ENDPOINT), ENDPOINT);
    }

    @Test
    public void connectorHttpRemove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_HTTP_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorHttpTable();
        FormFragment form = page.getConnectorHttpForm();
        table.bind(form);

        crudOperations.delete(connectorHttpAddress(SRV_UPDATE, CONN_HTTP_DELETE), table, CONN_HTTP_DELETE);
    }


    // --------------- connector - remote

    @Test
    public void connectorRemoteCreate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);

        crudOperations.create(connectorRemoteAddress(SRV_UPDATE, CONN_REM_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, CONN_REM_CREATE);
                    formFragment.text(SOCKET_BINDING, HTTP);
                }
        );
    }

    @Test
    public void connectorRemoteTryCreate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_REM_CREATE, SOCKET_BINDING);
    }

    @Test
    public void connectorRemoteUpdate() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);
        table.select(CONN_REM_UPDATE);
        crudOperations.update(connectorRemoteAddress(SRV_UPDATE, CONN_REM_UPDATE), form, SOCKET_BINDING, HTTPS);
    }

    @Test
    public void connectorRemoteTryUpdate() {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);
        table.select(CONN_REM_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(SOCKET_BINDING), SOCKET_BINDING);
    }

    @Test
    public void connectorRemoteRemove() throws Exception {
        console.verticalNavigation()
                .selectSecondary(Ids.build(MESSAGING_CONNECTOR, GROUP, ITEM),
                        Ids.build(MESSAGING_REMOTE_CONNECTOR, ITEM));
        TableFragment table = page.getConnectorRemoteTable();
        FormFragment form = page.getConnectorRemoteForm();
        table.bind(form);

        crudOperations.delete(connectorRemoteAddress(SRV_UPDATE, CONN_REM_DELETE), table, CONN_REM_DELETE);
    }

    // --------------- connector - service

    @Test
    public void connectorServiceCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);

        crudOperations.create(connectorServiceAddress(SRV_UPDATE, CONN_SVC_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, CONN_SVC_CREATE);
                    formFragment.text(FACTORY_CLASS, anyString);
                }
        );
    }

    @Test
    public void connectorServiceTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_SVC_CREATE, FACTORY_CLASS);
    }

    @Test
    public void connectorServiceUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);
        table.select(CONN_SVC_UPDATE);
        crudOperations.update(connectorServiceAddress(SRV_UPDATE, CONN_SVC_UPDATE), form, FACTORY_CLASS);
    }

    @Test
    public void connectorServiceTryUpdate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);
        table.select(CONN_SVC_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(FACTORY_CLASS), FACTORY_CLASS);
    }

    @Test
    public void connectorServiceRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTOR_SERVICE, ITEM));
        TableFragment table = page.getConnectorServiceTable();
        FormFragment form = page.getConnectorServiceForm();
        table.bind(form);

        crudOperations.delete(connectorServiceAddress(SRV_UPDATE, CONN_SVC_DELETE), table, CONN_SVC_DELETE);
    }

    // --------------- connection - factory

    @Test
    public void connectionFactoryCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);

        crudOperations.create(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, CONN_FAC_CREATE);
                    formFragment.text(DISCOVERY_GROUP, anyString);
                    formFragment.list(ENTRIES).add(CONN_FAC_CREATE_ENTRY);
                }
        );
    }

    @Test
    public void connectionFactoryTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, CONN_FAC_CREATE, DISCOVERY_GROUP);
    }

    @Test
    public void connectionFactoryUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);
        table.select(CONN_FAC_UPDATE);
        crudOperations.update(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_UPDATE), form, CALL_TIMEOUT, 123L);
    }

    @Test
    public void connectionFactoryTryUpdate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        FormFragment form = page.getConnectionFactoryForm();
        table.bind(form);
        table.select(CONN_FAC_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.list(CONNECTORS).add(anyString), DISCOVERY_GROUP);
    }

    @Test
    public void connectionFactoryRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getConnectionFactoryTable();
        crudOperations.delete(connectionFactoryAddress(SRV_UPDATE, CONN_FAC_DELETE), table, CONN_FAC_DELETE);
    }

    // --------------- pooled connection factory

    @Test
    public void pooledConnectionFactoryCreate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);

        crudOperations.create(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_CREATE), table,
                formFragment -> {
                    formFragment.text(NAME, POOL_CONN_CREATE);
                    formFragment.text(DISCOVERY_GROUP, anyString);
                    formFragment.list(ENTRIES).add(POOL_CONN_CREATE_ENTRY);
                }
        );
    }

    @Test
    public void pooledConnectionFactoryTryCreate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, POOL_CONN_CREATE, DISCOVERY_GROUP);
    }

    @Test
    public void pooledConnectionFactoryUpdate() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        page.getPooledFormsTab().select(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ATTRIBUTES, TAB));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        crudOperations.update(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), form, CALL_TIMEOUT, 123L);
    }

    @Test
    public void pooledConnectionFactoryTryUpdate() {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);
        table.select(POOL_CONN_TRY_UPDATE);
        crudOperations.updateWithError(form, f -> f.list(CONNECTORS).add(anyString), DISCOVERY_GROUP);
    }

    @Test
    public void pooledConnectionFactoryRemove() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryForm();
        table.bind(form);

        crudOperations.delete(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_DELETE), table, POOL_CONN_DELETE);
    }

    // tests the credential-reference form

    @Test
    public void pooledConnectionFactoryTryAddCredentialReferenceRequires() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE);

        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        // the order of UI navigation is important
        // first select the table item, then navigate to the tab
        page.getPooledFormsTab().select(crTab);
        form.emptyState().mainAction();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(STORE, anyString);
        addResource.getPrimaryButton().click();
        try {
            addResource.getForm().expectError(ALIAS);
        } finally {
            addResource.getSecondaryButton().click(); // close dialog to cleanup
        }
    }

    @Test
    public void pooledConnectionFactoryTryAddCredentialReferenceEmpty() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE);
        page.navigate(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        EmptyState emptyState = form.emptyState();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        page.getPooledFormsTab().select(crTab);
        emptyState.mainAction();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getPrimaryButton().click();
        try {
            addResource.getForm().expectError(STORE);
            addResource.getForm().expectError(CLEAR_TEXT);
        } finally {
            addResource.getSecondaryButton().click(); // close dialog to cleanup
        }
    }

    @Test
    public void pooledConnectionFactoryTryAddCredentialReferenceAlternatives() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE);
        page.navigate(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        EmptyState emptyState = form.emptyState();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        page.getPooledFormsTab().select(crTab);
        emptyState.mainAction();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(STORE, anyString);
        addResource.getForm().text(CLEAR_TEXT, anyString);
        addResource.getPrimaryButton().click();
        try {
            addResource.getForm().expectError(STORE);
            addResource.getForm().expectError(CLEAR_TEXT);
        } finally {
            addResource.getSecondaryButton().click(); // close dialog to cleanup
        }
    }

    @Test
    public void pooledConnectionFactoryAddCredentialReference() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigate(SERVER, SRV_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        EmptyState emptyState = form.emptyState();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);
        page.getPooledFormsTab().select(crTab);
        emptyState.mainAction();

        AddResourceDialogFragment addResource = console.addResourceDialog();
        addResource.getForm().text(CLEAR_TEXT, anyString);
        addResource.add();

        console.verifySuccess();
        new ResourceVerifier(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), client)
                .verifyAttribute(CREDENTIAL_REFERENCE + "." + CLEAR_TEXT, anyString);

    }

    @Test
    public void pooledConnectionFactoryTryUpdateCredentialReferenceAlternatives() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.writeAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE, cr);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigate(SERVER, SRV_UPDATE);

        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        page.getPooledFormsTab().select(crTab);

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);

        crudOperations.updateWithError(form, f -> f.text(STORE, anyString), STORE);
    }

    @Test
    public void pooledConnectionFactoryTryUpdateCredentialReferenceEmpty() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.writeAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE, cr);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigate(SERVER, SRV_UPDATE);

        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        page.getPooledFormsTab().select(crTab);

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);

        crudOperations.updateWithError(form, f -> f.clear(CLEAR_TEXT), STORE, CLEAR_TEXT);
    }

    @Test
    public void pooledConnectionFactoryRemoveCredentialReference() throws Exception {
        operations.undefineAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), PASSWORD);
        ModelNode cr = new ModelNode();
        cr.get(CLEAR_TEXT).set(anyString);
        operations.writeAttribute(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), CREDENTIAL_REFERENCE, cr);
        // navigate again, to reload the page as new data were added with the operations above
        page.navigate(SERVER, SRV_UPDATE);

        console.verticalNavigation().selectPrimary(Ids.build(MESSAGING_SERVER, POOLED_CONNECTION_FACTORY, ITEM));
        page.getPooledFormsTab().select(crTab);

        TableFragment table = page.getPooledConnectionFactoryTable();
        FormFragment form = page.getPooledConnectionFactoryCRForm();
        table.bind(form);
        table.select(POOL_CONN_UPDATE);

        crudOperations.deleteSingleton(pooledConnectionFactoryAddress(SRV_UPDATE, POOL_CONN_UPDATE), form,
                resourceVerifier -> resourceVerifier.verifyAttributeIsUndefined(CREDENTIAL_REFERENCE));
    }
}
