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

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
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

import static org.jboss.hal.dmr.ModelDescriptionConstants.MESSAGING_ACTIVEMQ;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class ServerFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(serverAddress(SRV_READ));
        operations.add(serverAddress(SRV_UPDATE));
        operations.add(serverAddress(SRV_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(serverAddress(SRV_CREATE));
        operations.removeIfExists(serverAddress(SRV_CREATE2));
        operations.removeIfExists(serverAddress(SRV_READ));
        operations.removeIfExists(serverAddress(SRV_UPDATE));
        operations.removeIfExists(serverAddress(SRV_DELETE));
    }

    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(MESSAGING_ACTIVEMQ)
                .append(Ids.MESSAGING_CATEGORY, SERVER))
                .column(Ids.MESSAGING_SERVER_CONFIGURATION);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = column.add();
        dialog.getForm().text(NAME, SRV_CREATE);
        dialog.add();

        console.verifySuccess();
        assertTrue(column.containsItem(Ids.messagingServer(SRV_CREATE)));
        new ResourceVerifier(serverAddress(SRV_CREATE), client).verifyExists();
    }

    @Test
    public void read() {
        assertTrue(column.containsItem(Ids.messagingServer(SRV_READ)));
    }

    @Test
    public void refresh() throws Exception {
        operations.add(serverAddress(SRV_CREATE2));
        console.waitNoNotification();
        column.refresh();
        assertTrue(column.containsItem(Ids.messagingServer(SRV_CREATE2)));
    }

    @Test
    public void select() {
        column.selectItem(Ids.messagingServer(SRV_READ));
        PlaceRequest placeRequest = Places.finderPlace(NameTokens.CONFIGURATION, new FinderPath()
                .append(Ids.CONFIGURATION, Ids.asId(Names.SUBSYSTEMS))
                .append(Ids.CONFIGURATION_SUBSYSTEM, MESSAGING_ACTIVEMQ)
                .append(Ids.MESSAGING_CATEGORY, SERVER)
                .append(Ids.MESSAGING_SERVER_CONFIGURATION, Ids.messagingServer(SRV_READ)));
        console.verify(placeRequest);
    }

    @Test
    public void view() {
        column.selectItem(Ids.messagingServer(SRV_READ)).view();

        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.MESSAGING_SERVER)
                .with(SERVER, SRV_READ)
                .build();
        console.verify(placeRequest);
    }

    @Test
    public void delete() throws Exception {
        column.selectItem(Ids.messagingServer(SRV_DELETE)).dropdown().click("Remove");
        console.confirmationDialog().confirm();

        console.verifySuccess();
        assertFalse(column.containsItem(Ids.messagingServer(SRV_DELETE)));
        new ResourceVerifier(serverAddress(SRV_DELETE), client).verifyDoesNotExist();
    }
}
