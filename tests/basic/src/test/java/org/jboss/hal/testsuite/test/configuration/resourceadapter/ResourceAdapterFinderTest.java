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
package org.jboss.hal.testsuite.test.configuration.resourceadapter;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.page.Places;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ARCHIVE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RESOURCE_ADAPTERS;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.jboss.hal.testsuite.test.configuration.resourceadapter.ResourceAdapterFixtures.RESOURCE_ADAPTER_CREATE;
import static org.jboss.hal.testsuite.test.configuration.resourceadapter.ResourceAdapterFixtures.RESOURCE_ADAPTER_DELETE;
import static org.jboss.hal.testsuite.test.configuration.resourceadapter.ResourceAdapterFixtures.RESOURCE_ADAPTER_READ;
import static org.jboss.hal.testsuite.test.configuration.resourceadapter.ResourceAdapterFixtures.resourceAdapterAddress;
import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
public class ResourceAdapterFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(resourceAdapterAddress(RESOURCE_ADAPTER_READ), Values.of(ARCHIVE, Random.name()));
        operations.add(resourceAdapterAddress(RESOURCE_ADAPTER_DELETE), Values.of(ARCHIVE, Random.name()));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(resourceAdapterAddress(RESOURCE_ADAPTER_CREATE));
        operations.removeIfExists(resourceAdapterAddress(RESOURCE_ADAPTER_READ));
        operations.removeIfExists(resourceAdapterAddress(RESOURCE_ADAPTER_DELETE));
    }

    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        FinderFragment finder = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(RESOURCE_ADAPTERS));
        column = finder.column(Ids.RESOURCE_ADAPTER);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = column.add();
        dialog.getForm().text(NAME, RESOURCE_ADAPTER_CREATE);
        dialog.getForm().text(ARCHIVE, Random.name());
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(resourceAdapterAddress(RESOURCE_ADAPTER_CREATE), client)
                .verifyExists();
    }

    @Test
    public void select() {
        column.selectItem(Ids.resourceAdapter(RESOURCE_ADAPTER_READ));
        PlaceRequest place = Places.finderPlace(NameTokens.CONFIGURATION, configurationSubsystemPath(RESOURCE_ADAPTERS)
                .append(Ids.RESOURCE_ADAPTER, Ids.resourceAdapter(RESOURCE_ADAPTER_READ)));
        console.verify(place);
    }

    @Test
    public void view() {
        column.selectItem(Ids.resourceAdapter(RESOURCE_ADAPTER_READ)).view();
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.RESOURCE_ADAPTER)
                .with(NAME, RESOURCE_ADAPTER_READ)
                .build();
        console.verify(placeRequest);
    }

    @Test
    public void delete() throws Exception {
        column.selectItem(Ids.resourceAdapter(RESOURCE_ADAPTER_DELETE))
                .dropdown()
                .click("Remove");
        console.confirmationDialog().confirm();

        console.verifySuccess();
        assertFalse(column.containsItem(Ids.resourceAdapter(RESOURCE_ADAPTER_DELETE)));
        new ResourceVerifier(resourceAdapterAddress(RESOURCE_ADAPTER_DELETE), client)
                .verifyDoesNotExist();
    }
}
