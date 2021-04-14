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
package org.jboss.hal.testsuite.test.configuration.ee;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.EEPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.JNDI_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fixtures.EEFixtures.CONTEXT_SERVICE_CREATE;
import static org.jboss.hal.testsuite.fixtures.EEFixtures.CONTEXT_SERVICE_DELETE;
import static org.jboss.hal.testsuite.fixtures.EEFixtures.CONTEXT_SERVICE_READ;
import static org.jboss.hal.testsuite.fixtures.EEFixtures.CONTEXT_SERVICE_UPDATE;
import static org.jboss.hal.testsuite.fixtures.EEFixtures.contextServiceAddress;

@RunWith(Arquillian.class)
public class ContextServiceTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(contextServiceAddress(CONTEXT_SERVICE_READ), Values.of(JNDI_NAME, Random.jndiName()));
        operations.add(contextServiceAddress(CONTEXT_SERVICE_UPDATE), Values.of(JNDI_NAME, Random.jndiName()));
        operations.add(contextServiceAddress(CONTEXT_SERVICE_DELETE), Values.of(JNDI_NAME, Random.jndiName()));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(contextServiceAddress(CONTEXT_SERVICE_CREATE));
        operations.removeIfExists(contextServiceAddress(CONTEXT_SERVICE_READ));
        operations.removeIfExists(contextServiceAddress(CONTEXT_SERVICE_UPDATE));
        operations.removeIfExists(contextServiceAddress(CONTEXT_SERVICE_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EEPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() {
        page.navigate();
        console.verticalNavigation().selectSecondary(Ids.EE_SERVICES_ITEM, Ids.EE_CONTEXT_SERVICE);

        table = page.getContextServiceTable();
        form = page.getContextServiceForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(contextServiceAddress(CONTEXT_SERVICE_CREATE), table, form -> {
            form.text(NAME, CONTEXT_SERVICE_CREATE);
            form.text(JNDI_NAME, Random.jndiName());
        });
    }

    @Test
    public void update() throws Exception {
        table.select(CONTEXT_SERVICE_UPDATE);
        crud.update(contextServiceAddress(CONTEXT_SERVICE_UPDATE), form, JNDI_NAME, Random.jndiName());
    }

    @Test
    public void reset() throws Exception {
        table.select(CONTEXT_SERVICE_UPDATE);
        crud.reset(contextServiceAddress(CONTEXT_SERVICE_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(contextServiceAddress(CONTEXT_SERVICE_DELETE), table, CONTEXT_SERVICE_DELETE);
    }
}
