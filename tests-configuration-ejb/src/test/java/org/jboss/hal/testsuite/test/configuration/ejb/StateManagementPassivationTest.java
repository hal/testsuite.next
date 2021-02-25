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
package org.jboss.hal.testsuite.test.configuration.ejb;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.EJBConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.MAX_SIZE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.PS_CREATE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.PS_DELETE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.PS_READ;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.PS_UPDATE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.passivationAddress;

@RunWith(Arquillian.class)
public class StateManagementPassivationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(passivationAddress(PS_READ));
        operations.add(passivationAddress(PS_UPDATE));
        operations.add(passivationAddress(PS_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(passivationAddress(PS_CREATE));
        operations.removeIfExists(passivationAddress(PS_READ));
        operations.removeIfExists(passivationAddress(PS_UPDATE));
        operations.removeIfExists(passivationAddress(PS_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EJBConfigurationPage page;
    private FormFragment form;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("ejb3-state-item", "ejb3-passivation-item");

        table = page.getPassivationTable();
        form = page.getPassivationForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(passivationAddress(PS_CREATE), table, PS_CREATE);
    }

    @Test
    public void update() throws Exception {
        table.select(PS_UPDATE);
        crud.update(passivationAddress(PS_UPDATE), form, MAX_SIZE, 123);
    }

    @Test
    public void updateInvalidMaxSize() {
        table.select(PS_UPDATE);
        crud.updateWithError(form, MAX_SIZE, -1);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(passivationAddress(PS_DELETE), table, PS_DELETE);
    }
}
