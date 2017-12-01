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
import static org.jboss.hal.testsuite.test.configuration.ee.EEFixtures.*;

@RunWith(Arquillian.class)
public class ThreadFactoryTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(threadFactoryAddress(THREAD_FACTORY_READ), Values.of(JNDI_NAME, Random.jndiName()));
        operations.add(threadFactoryAddress(THREAD_FACTORY_UPDATE), Values.of(JNDI_NAME, Random.jndiName()));
        operations.add(threadFactoryAddress(THREAD_FACTORY_DELETE), Values.of(JNDI_NAME, Random.jndiName()));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(threadFactoryAddress(THREAD_FACTORY_CREATE));
        operations.removeIfExists(threadFactoryAddress(THREAD_FACTORY_READ));
        operations.removeIfExists(threadFactoryAddress(THREAD_FACTORY_UPDATE));
        operations.removeIfExists(threadFactoryAddress(THREAD_FACTORY_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EEPage page;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary(Ids.EE_SERVICES_ITEM, Ids.EE_MANAGED_THREAD_FACTORY);

        table = page.getThreadFactoryTable();
        form = page.getThreadFactoryForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(threadFactoryAddress(THREAD_FACTORY_CREATE), table, form -> {
            form.text(NAME, THREAD_FACTORY_CREATE);
            form.text(JNDI_NAME, Random.jndiName());
        });
    }

    @Test
    public void update() throws Exception {
        table.select(THREAD_FACTORY_UPDATE);
        crud.update(threadFactoryAddress(THREAD_FACTORY_UPDATE), form, JNDI_NAME, Random.jndiName());
    }

    @Test
    public void reset() throws Exception {
        table.select(THREAD_FACTORY_UPDATE);
        crud.reset(threadFactoryAddress(THREAD_FACTORY_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(threadFactoryAddress(THREAD_FACTORY_DELETE), table, THREAD_FACTORY_DELETE);
    }
}
