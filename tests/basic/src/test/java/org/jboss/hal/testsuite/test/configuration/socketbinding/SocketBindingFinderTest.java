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
package org.jboss.hal.testsuite.test.configuration.socketbinding;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
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
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT_INTERFACE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.SBG_CREATE;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.SBG_DELETE;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.SBG_READ;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.socketBindingGroupAddress;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@Category(Domain.class)
public class SocketBindingFinderTest {

    private static final String PUBLIC = "public";
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(socketBindingGroupAddress(SBG_READ), Values.empty().and(DEFAULT_INTERFACE, PUBLIC));
        operations.add(socketBindingGroupAddress(SBG_DELETE), Values.empty().and(DEFAULT_INTERFACE, PUBLIC));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(socketBindingGroupAddress(SBG_CREATE));
        operations.removeIfExists(socketBindingGroupAddress(SBG_READ));
        operations.removeIfExists(socketBindingGroupAddress(SBG_DELETE));
    }


    @Drone private WebDriver browser;
    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION)
                .select(new FinderPath().append(Ids.CONFIGURATION, Ids.asId(Names.SOCKET_BINDINGS)))
                .column(Ids.SOCKET_BINDING_GROUP);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = column.add();
        dialog.getForm().text(NAME, SBG_CREATE);
        dialog.getForm().text(DEFAULT_INTERFACE, PUBLIC);
        dialog.add();

        console.verifySuccess();
        assertTrue(column.containsItem(SBG_CREATE));
        new ResourceVerifier(socketBindingGroupAddress(SBG_CREATE), client).verifyExists();
    }

    @Test
    public void read() throws Exception {
        assertTrue(column.containsItem(SBG_READ));
    }

    @Test
    public void select() throws Exception {
        column.selectItem(SBG_READ);
        PlaceRequest placeRequest = Places.finderPlace(Ids.CONFIGURATION, new FinderPath()
                .append(Ids.CONFIGURATION, Ids.build(Names.SOCKET_BINDINGS))
                .append(Ids.SOCKET_BINDING_GROUP, SBG_READ));
        console.assertPlace(placeRequest);
    }

    @Test
    public void view() throws Exception {
        column.selectItem(SBG_READ).view();

        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.SOCKET_BINDING_GROUP)
                .with(NAME, SBG_READ)
                .build();
        console.assertPlace(placeRequest);
    }

    @Test
    public void delete() throws Exception {
        column.selectItem(SBG_DELETE).dropdown().click("Remove");
        console.confirmationDialog().confirm();

        console.verifySuccess();
        assertFalse(column.containsItem(SBG_DELETE));
        new ResourceVerifier(socketBindingGroupAddress(SBG_DELETE), client).verifyDoesNotExist();
    }
}
