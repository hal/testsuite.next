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
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
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

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TIMEOUT;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.*;

@RunWith(Arquillian.class)
public class BeanPoolTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private String DERIVE_SIZE = "derive-size";
    private String MAX_POOL_SIZE = "max-pool-size";
    private String FROM_WORKER_POOLS = "from-worker-pools";

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

    @Page private EJBConfigurationPage page;
    @Inject private Console console;
    private FormFragment form;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("ejb-bean-pool-item");

        table = page.getBeanPoolTable();
        form = page.getBeanPoolForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, BP_CREATE);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(beanPoolAddress(BP_CREATE), client).verifyExists();
    }

    @Test
    public void update() throws Exception {
        int val = 123;
        table.select(BP_UPDATE);
        form.edit();
        form.number(MAX_POOL_SIZE, val);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(beanPoolAddress(BP_UPDATE), client)
                .verifyAttribute(MAX_POOL_SIZE, val);
    }

    @Test
    public void updateInvalidAlternatives() throws Exception {
        table.select(BP_UPDATE);
        form.edit();
        form.number(MAX_POOL_SIZE, 234);
        form.select(DERIVE_SIZE, FROM_WORKER_POOLS);
        form.trySave();
        form.expectError(MAX_POOL_SIZE);
        form.expectError(DERIVE_SIZE);
    }

    @Test
    public void updateInvalidTimeout() throws Exception {
        table.select(BP_UPDATE);
        form.edit();
        form.number(TIMEOUT, 0);
        form.trySave();
        form.expectError(TIMEOUT);
    }

    @Test
    public void updateTimeout() throws Exception {
        long val = 11;
        table.select(BP_UPDATE);
        form.edit();
        form.number(TIMEOUT, val);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(beanPoolAddress(BP_UPDATE), client)
                .verifyAttribute(TIMEOUT, val);
    }

    @Test
    public void delete() throws Exception {
        table.remove(BP_DELETE);

        console.verifySuccess();
        new ResourceVerifier(beanPoolAddress(BP_DELETE), client).verifyDoesNotExist();
    }
}
