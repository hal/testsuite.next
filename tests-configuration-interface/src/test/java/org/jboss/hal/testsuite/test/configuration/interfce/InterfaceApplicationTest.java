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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.HeaderBreadcrumbFragment;
import org.jboss.hal.testsuite.page.configuration.InterfacePage;
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
import static org.jboss.hal.testsuite.fixtures.InterfaceFixtures.LOCALHOST;
import static org.jboss.hal.testsuite.fixtures.InterfaceFixtures.UPDATE;
import static org.jboss.hal.testsuite.fixtures.InterfaceFixtures.interfaceAddress;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class InterfaceApplicationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(interfaceAddress(UPDATE), Values.empty().and(INET_ADDRESS, LOCALHOST));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(interfaceAddress(UPDATE));
    }


    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private InterfacePage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, UPDATE);
        form = page.getForm();
    }

    @Test
    public void read() {
        assertEquals(HeaderBreadcrumbFragment.abbreviate(UPDATE), console.header().breadcrumb().lastValue());
        assertEquals(UPDATE, form.value(NAME));
    }

    @Test
    public void update() throws Exception {
        crud.update(interfaceAddress(UPDATE), form, INET_ADDRESS, "127.0.0.2");
    }
}
