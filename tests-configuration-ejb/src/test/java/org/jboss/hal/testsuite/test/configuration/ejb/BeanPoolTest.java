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

import static org.jboss.hal.dmr.ModelDescriptionConstants.MAX_POOL_SIZE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SUSPEND_TIMEOUT;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.BP_CREATE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.BP_DELETE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.BP_READ;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.BP_UPDATE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.DERIVE_SIZE;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.FROM_WORKER_POOLS;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.beanPoolAddress;

@RunWith(Arquillian.class)
public class BeanPoolTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(beanPoolAddress(BP_READ));
        operations.add(beanPoolAddress(BP_UPDATE));
        operations.add(beanPoolAddress(BP_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(beanPoolAddress(BP_CREATE));
        operations.removeIfExists(beanPoolAddress(BP_READ));
        operations.removeIfExists(beanPoolAddress(BP_UPDATE));
        operations.removeIfExists(beanPoolAddress(BP_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EJBConfigurationPage page;
    private FormFragment form;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("ejb3-bean-pool-item");

        table = page.getBeanPoolTable();
        form = page.getBeanPoolForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(beanPoolAddress(BP_CREATE), table, BP_CREATE);
    }

    @Test
    public void update() throws Exception {
        table.select(BP_UPDATE);
        crud.update(beanPoolAddress(BP_UPDATE), form, MAX_POOL_SIZE, 123);
    }

    @Test
    public void updateInvalidAlternatives() {
        table.select(BP_UPDATE);
        crud.updateWithError(form, f -> {
            f.number(MAX_POOL_SIZE, 234);
            f.select(DERIVE_SIZE, FROM_WORKER_POOLS);
        }, MAX_POOL_SIZE, DERIVE_SIZE);
    }

    @Test
    public void updateInvalidTimeout() {
        table.select(BP_UPDATE);
        crud.updateWithError(form, SUSPEND_TIMEOUT, 0);
    }

    @Test
    public void updateTimeout() throws Exception {
        table.select(BP_UPDATE);
        crud.update(beanPoolAddress(BP_UPDATE), form, SUSPEND_TIMEOUT, 11L);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(beanPoolAddress(BP_DELETE), table, BP_DELETE);
    }
}
