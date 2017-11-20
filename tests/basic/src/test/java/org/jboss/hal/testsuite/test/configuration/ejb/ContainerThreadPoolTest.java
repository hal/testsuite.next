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
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MAX_THREADS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.*;

@RunWith(Arquillian.class)
public class ContainerThreadPoolTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(threadPoolAddress(TP_READ), Values.of(MAX_THREADS, 22));
        operations.add(threadPoolAddress(TP_UPDATE), Values.of(MAX_THREADS, 33));
        operations.add(threadPoolAddress(TP_DELETE), Values.of(MAX_THREADS, 44));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(threadPoolAddress(TP_CREATE));
        operations.removeIfExists(threadPoolAddress(TP_READ));
        operations.removeIfExists(threadPoolAddress(TP_UPDATE));
        operations.removeIfExists(threadPoolAddress(TP_DELETE));
    }

    @Page private EJBConfigurationPage page;
    @Inject private Console console;
    private FormFragment form;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("ejb3-container-item", "ejb3-thread-pool-item");

        table = page.getThreadPoolTable();
        form = page.getThreadPoolForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, TP_CREATE);
        dialog.getForm().number(MAX_THREADS, 11);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(threadPoolAddress(TP_CREATE), client).verifyExists();
    }

    @Test
    public void createInvalidMaxThreads() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, TP_CREATE);
        dialog.getForm().number(MAX_THREADS, -1);
        dialog.getPrimaryButton().click();
        dialog.getForm().expectError(MAX_THREADS);
    }

    @Test
    public void update() throws Exception {
        table.select(TP_UPDATE);
        form.edit();
        form.number(MAX_THREADS, 331);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(threadPoolAddress(TP_UPDATE), client)
                .verifyAttribute(MAX_THREADS, 331);
    }

    @Test
    public void updateInvalidMaxThreads() throws Exception {
        table.select(TP_UPDATE);
        form.edit();
        form.number(MAX_THREADS, -1);
        form.trySave();
        form.expectError(MAX_THREADS);
    }

    @Test
    public void delete() throws Exception {
        table.remove(TP_DELETE);

        console.verifySuccess();
        new ResourceVerifier(threadPoolAddress(TP_DELETE), client).verifyDoesNotExist();
    }
}
