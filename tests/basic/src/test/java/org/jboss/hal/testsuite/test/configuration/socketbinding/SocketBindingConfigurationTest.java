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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.HeaderBreadcrumbFragment;
import org.jboss.hal.testsuite.page.configuration.SocketBindingPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT_INTERFACE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.PRIVATE;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.PUBLIC;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.SBG_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.socketbinding.SocketBindingFixtures.socketBindingGroupAddress;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@Category(Domain.class)
public class SocketBindingConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(socketBindingGroupAddress(SBG_UPDATE), Values.empty().and(DEFAULT_INTERFACE, PUBLIC));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(socketBindingGroupAddress(SBG_UPDATE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private SocketBindingPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, SBG_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.SOCKET_BINDING_GROUP + "-" + Ids.CONFIGURATION + "-" + Ids.ITEM);
        form = page.getConfigurationForm();
    }

    @Test
    public void view() {
        assertEquals(HeaderBreadcrumbFragment.abbreviate(SBG_UPDATE), console.header().breadcrumb().lastValue());
    }

    @Test
    public void update() throws Exception {
        crud.update(socketBindingGroupAddress(SBG_UPDATE), form, DEFAULT_INTERFACE, PRIVATE);
    }
}
