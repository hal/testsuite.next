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
package org.jboss.hal.testsuite.test.configuration.interfce;

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
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.INET_ADDRESS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.interfce.InterfaceFixtures.CREATE;
import static org.jboss.hal.testsuite.test.configuration.interfce.InterfaceFixtures.DELETE;
import static org.jboss.hal.testsuite.test.configuration.interfce.InterfaceFixtures.LOCALHOST;
import static org.jboss.hal.testsuite.test.configuration.interfce.InterfaceFixtures.READ;
import static org.jboss.hal.testsuite.test.configuration.interfce.InterfaceFixtures.interfaceAddress;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class InterfaceFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(interfaceAddress(READ), Values.empty().and(INET_ADDRESS, LOCALHOST));
        operations.add(interfaceAddress(DELETE), Values.empty().and(INET_ADDRESS, LOCALHOST));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(interfaceAddress(CREATE));
        operations.removeIfExists(interfaceAddress(READ));
        operations.removeIfExists(interfaceAddress(DELETE));
    }


    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION)
                .select(new FinderPath().append(Ids.CONFIGURATION, Ids.asId(Names.INTERFACES)))
                .column(Ids.INTERFACE);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = column.add();
        dialog.getForm().text(NAME, CREATE);
        dialog.getForm().text(INET_ADDRESS, LOCALHOST);
        dialog.add();

        console.verifySuccess();
        assertTrue(column.containsItem(CREATE));
        new ResourceVerifier(interfaceAddress(CREATE), client).verifyExists();
    }

    @Test
    public void read() throws Exception {
        assertTrue(column.containsItem(READ));
    }

    @Test
    public void select() throws Exception {
        column.selectItem(READ);
        PlaceRequest placeRequest = Places.finderPlace(Ids.CONFIGURATION, new FinderPath()
                .append(Ids.CONFIGURATION, Ids.build(Names.INTERFACES))
                .append(Ids.INTERFACE, READ));
        console.assertPlace(placeRequest);
    }

    @Test
    public void view() throws Exception {
        column.selectItem(READ).view();

        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.INTERFACE)
                .with(NAME, READ)
                .build();
        console.assertPlace(placeRequest);
    }

    @Test
    public void delete() throws Exception {
        column.selectItem(DELETE).dropdown().click("Remove");
        console.confirmationDialog().confirm();

        console.verifySuccess();
        assertFalse(column.containsItem(DELETE));
        new ResourceVerifier(interfaceAddress(DELETE), client).verifyDoesNotExist();
    }
}
