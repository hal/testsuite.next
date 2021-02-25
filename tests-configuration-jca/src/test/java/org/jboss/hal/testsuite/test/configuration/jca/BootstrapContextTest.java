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
package org.jboss.hal.testsuite.test.configuration.jca;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.JcaPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.WORKMANAGER;
import static org.jboss.hal.testsuite.fixtures.JcaFixtures.BC_CREATE;
import static org.jboss.hal.testsuite.fixtures.JcaFixtures.BC_DELETE;
import static org.jboss.hal.testsuite.fixtures.JcaFixtures.BC_READ;
import static org.jboss.hal.testsuite.fixtures.JcaFixtures.bootstrapContextAddress;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class BootstrapContextTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration administration = new Administration(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(bootstrapContextAddress(BC_READ), Values.of(NAME, BC_READ).and(WORKMANAGER, DEFAULT));
        operations.add(bootstrapContextAddress(BC_DELETE), Values.of(NAME, BC_DELETE).and(WORKMANAGER, DEFAULT));
        administration.reload();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(bootstrapContextAddress(BC_CREATE));
        operations.removeIfExists(bootstrapContextAddress(BC_READ));
        operations.removeIfExists(bootstrapContextAddress(BC_DELETE));
        administration.reload();
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private JcaPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.JCA_BOOTSTRAP_CONTEXT_ITEM);

        form = page.getBootstrapContextForm();
        table = page.getBootstrapContextTable();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(bootstrapContextAddress(BC_CREATE), table, form -> {
            form.text(NAME, BC_CREATE);
            form.text(WORKMANAGER, DEFAULT);
        });
    }

    @Test
    public void read() {
        table.select(BC_READ);
        assertEquals(BC_READ, form.value(NAME));
        assertEquals(DEFAULT, form.value(WORKMANAGER));
    }

    @Test
    public void delete() throws Exception {
        crud.delete(bootstrapContextAddress(BC_DELETE), table, BC_DELETE);
    }
}
