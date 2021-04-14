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
package org.jboss.hal.testsuite.test.configuration.messaging.jms.bridge;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.page.Places;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MESSAGING_ACTIVEMQ;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.Ids.JMS_BRIDGE;
import static org.jboss.hal.resources.Ids.JMS_BRIDGE_ITEM;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.AT_MOST_ONCE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.CONNECTION_FACTORY_VALUE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.DESTINATION_QUEUE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JMSBRIDGE_CREATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JMSBRIDGE_CREATE2;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JMSBRIDGE_DELETE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.JMSBRIDGE_UPDATE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.QUALITY_OF_SERVICE;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.REMOTE_CONNECTION_FACTORY;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SOURCE_CONNECTION_FACTORY;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.SOURCE_DESTINATION;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.TARGET_CONNECTION_FACTORY;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.TARGET_CONTEXT;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.TARGET_DESTINATION;
import static org.jboss.hal.testsuite.fixtures.MessagingFixtures.jmsBridgeAddress;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class FinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static Values PARAMS;
    private static ModelNode TARGET_CONTEXT_MODEL;

    static {
        TARGET_CONTEXT_MODEL = new ModelNode();
        TARGET_CONTEXT_MODEL.get("java.naming.factory.initial")
                .set("org.jboss.naming.remote.client.InitialContextFactory");
        TARGET_CONTEXT_MODEL.get("java.naming.provider.url").set("http-remoting://localhost:8180");

        PARAMS = Values.of(QUALITY_OF_SERVICE, AT_MOST_ONCE)
                .and(MODULE, "org.wildfly.extension.messaging-activemq")
                .and(TARGET_CONTEXT, TARGET_CONTEXT_MODEL)
                .and(SOURCE_CONNECTION_FACTORY, CONNECTION_FACTORY_VALUE)
                .and(SOURCE_DESTINATION, DESTINATION_QUEUE)
                .and(TARGET_CONNECTION_FACTORY, REMOTE_CONNECTION_FACTORY)
                .and(TARGET_DESTINATION, DESTINATION_QUEUE);
    }

    @BeforeClass
    public static void beforeTests() throws Exception {
        operations.add(jmsBridgeAddress(JMSBRIDGE_UPDATE), PARAMS);
        operations.add(jmsBridgeAddress(JMSBRIDGE_DELETE), PARAMS);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.remove(jmsBridgeAddress(JMSBRIDGE_CREATE));
        operations.remove(jmsBridgeAddress(JMSBRIDGE_CREATE2));
        operations.remove(jmsBridgeAddress(JMSBRIDGE_UPDATE));
        operations.remove(jmsBridgeAddress(JMSBRIDGE_DELETE));
    }

    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(MESSAGING_ACTIVEMQ)
                .append(Ids.MESSAGING_CATEGORY, JMS_BRIDGE_ITEM))
                .column(JMS_BRIDGE);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = column.add();
        dialog.getForm().text(NAME, JMSBRIDGE_CREATE);
        dialog.getForm().properties(TARGET_CONTEXT).add(TARGET_CONTEXT_MODEL);
        dialog.getForm().text(SOURCE_CONNECTION_FACTORY, CONNECTION_FACTORY_VALUE);
        dialog.getForm().text(SOURCE_DESTINATION, DESTINATION_QUEUE);
        dialog.getForm().text(TARGET_CONNECTION_FACTORY, REMOTE_CONNECTION_FACTORY);
        dialog.getForm().text(TARGET_DESTINATION, DESTINATION_QUEUE);
        dialog.getForm().text(MODULE, "org.wildfly.extension.messaging-activemq");
        dialog.add();
        console.verifySuccess();
        assertTrue(column.containsItem(Ids.jmsBridge(JMSBRIDGE_CREATE)));
        new ResourceVerifier(jmsBridgeAddress(JMSBRIDGE_CREATE), client).verifyExists();
    }

    @Test
    public void read() {
        assertTrue(column.containsItem(Ids.jmsBridge(JMSBRIDGE_UPDATE)));
    }

    @Test
    public void refresh() throws Exception {
        operations.add(jmsBridgeAddress(JMSBRIDGE_CREATE2), PARAMS);
        console.waitNoNotification();
        column.refresh();
        assertTrue(column.containsItem(Ids.jmsBridge(JMSBRIDGE_CREATE2)));
    }

    @Test
    public void select() {
        column.selectItem(Ids.jmsBridge(JMSBRIDGE_UPDATE));
        PlaceRequest placeRequest = Places.finderPlace(NameTokens.CONFIGURATION, new FinderPath()
                .append(Ids.CONFIGURATION, Ids.asId(Names.SUBSYSTEMS))
                .append(Ids.CONFIGURATION_SUBSYSTEM, MESSAGING_ACTIVEMQ)
                .append(Ids.MESSAGING_CATEGORY, JMS_BRIDGE_ITEM)
                .append(Ids.JMS_BRIDGE, Ids.jmsBridge(JMSBRIDGE_UPDATE)));
        console.verify(placeRequest);
    }

    @Test
    public void view() {
        column.selectItem(Ids.jmsBridge(JMSBRIDGE_UPDATE)).view();

        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.JMS_BRIDGE)
                .with(NAME, JMSBRIDGE_UPDATE)
                .build();
        console.verify(placeRequest);
    }

    @Test
    public void delete() throws Exception {
        column.selectItem(Ids.jmsBridge(JMSBRIDGE_DELETE)).dropdown().click("Remove");
        console.confirmationDialog().confirm();

        console.verifySuccess();
        assertFalse(column.containsItem(Ids.jmsBridge(JMSBRIDGE_DELETE)));
        new ResourceVerifier(jmsBridgeAddress(JMSBRIDGE_DELETE), client).verifyDoesNotExist();
    }
}
