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
package org.jboss.hal.testsuite.test.configuration.remoting;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.RemotingPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.fixtures.RemotingFixtures.*;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class HttpConnectorPolicyTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(httpConnectorAddress(HTTP_CONNECTOR_POLICY),
                Values.of(CONNECTOR_REF, httpConnectorRef(HTTP_CONNECTOR_POLICY)));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(httpConnectorAddress(HTTP_CONNECTOR_POLICY));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private RemotingPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("remoting-remote-connector-item",
                "remoting-http-connector-sub-item");
        table = page.getHttpConnectorTable();
        form = page.getHttpConnectorPolicyForm();
        table.bind(form);
    }

    @Test
    public void _0create() throws Exception {
        table.select(HTTP_CONNECTOR_POLICY);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_SECURITY_POLICY_TAB);
        crud.createSingleton(httpConnectorPolicyAddress(HTTP_CONNECTOR_POLICY), form);
    }

    @Test
    public void _1update() throws Exception {
        table.select(HTTP_CONNECTOR_POLICY);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_SECURITY_POLICY_TAB);
        crud.update(httpConnectorPolicyAddress(HTTP_CONNECTOR_POLICY), form, FORWARD_SECRECY, false);
    }

    @Test
    public void _2reset() throws Exception {
        table.select(HTTP_CONNECTOR_POLICY);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_SECURITY_POLICY_TAB);
        crud.reset(httpConnectorPolicyAddress(HTTP_CONNECTOR_POLICY), form);
    }

    @Test
    public void _3delete() throws Exception {
        table.select(HTTP_CONNECTOR_POLICY);
        page.getHttpConnectorTabs().select(Ids.REMOTING_HTTP_CONNECTOR_SECURITY_POLICY_TAB);
        crud.deleteSingleton(httpConnectorPolicyAddress(HTTP_CONNECTOR_POLICY), form);
    }
}
