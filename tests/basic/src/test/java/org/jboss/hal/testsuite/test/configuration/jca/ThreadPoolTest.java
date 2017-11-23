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
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.configuration.JcaPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static java.util.Arrays.asList;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MAX_THREADS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.QUEUE_LENGTH;
import static org.jboss.hal.testsuite.test.configuration.jca.JcaFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ThreadPoolTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(workmanagerAddress(WM_THREAD_POOL_CREATE), Values.of(NAME, WM_THREAD_POOL_CREATE));
        addWorkmanagerWithThreadPools(WM_THREAD_POOL_READ);
        addWorkmanagerWithThreadPools(WM_THREAD_POOL_UPDATE);
        addWorkmanagerWithThreadPools(WM_THREAD_POOL_DELETE);
    }

    private static void addWorkmanagerWithThreadPools(String workmanager) throws Exception {
        operations.add(workmanagerAddress(workmanager), Values.of(NAME, workmanager));
        String lrtName = threadPoolName(workmanager, LRT);
        String srtName = threadPoolName(workmanager, SRT);
        operations.add(longRunningAddress(workmanager, lrtName),
                Values.of(NAME, LRT).and(MAX_THREADS, 10).and(QUEUE_LENGTH, 5));
        operations.add(shortRunningAddress(workmanager, srtName),
                Values.of(NAME, SRT).and(MAX_THREADS, 10).and(QUEUE_LENGTH, 5));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(workmanagerAddress(WM_THREAD_POOL_CREATE));
        operations.removeIfExists(workmanagerAddress(WM_THREAD_POOL_READ));
        operations.removeIfExists(workmanagerAddress(WM_THREAD_POOL_UPDATE));
        operations.removeIfExists(workmanagerAddress(WM_THREAD_POOL_DELETE));
    }

    @Page private JcaPage page;
    @Inject private Console console;
    private TableFragment wmTable;
    private TableFragment tpTable;
    private TabsFragment tpTabs;
    private FormFragment tpAttributesForm;
    private FormFragment tpSizingForm;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.JCA_WORKMANAGER_ITEM);
        wmTable = page.getWmTable();
        tpTable = page.getWmThreadPoolTable();
        tpTabs = page.getWmThreadPoolTabs();
        tpAttributesForm = page.getWmThreadPoolAttributesForm();
        tpSizingForm = page.getWmThreadPoolSizingForm();
        tpTable.bind(asList(tpAttributesForm, tpSizingForm));
    }

    @Test
    public void create() throws Exception {
        wmTable.action(WM_THREAD_POOL_CREATE, Names.THREAD_POOLS);
        waitGui().until().element(tpTable.getRoot()).is().visible();

        String lrtName = threadPoolName(WM_THREAD_POOL_CREATE, LRT);
        AddResourceDialogFragment dialog = tpTable.add();
        dialog.getForm().text(NAME, lrtName);
        dialog.getForm().number(MAX_THREADS, 10);
        dialog.getForm().number(QUEUE_LENGTH, 5);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(longRunningAddress(WM_THREAD_POOL_CREATE, lrtName), client)
                .verifyExists();

        String srtName = threadPoolName(WM_THREAD_POOL_CREATE, SRT);
        dialog = tpTable.add();
        dialog.getForm().text(NAME, srtName);
        dialog.getForm().number(MAX_THREADS, 10);
        dialog.getForm().number(QUEUE_LENGTH, 5);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(shortRunningAddress(WM_THREAD_POOL_CREATE, srtName), client)
                .verifyExists();
    }

    @Test
    public void read() throws Exception {
        wmTable.action(WM_THREAD_POOL_READ, Names.THREAD_POOLS);
        waitGui().until().element(tpTable.getRoot()).is().visible();

        String lrtName = threadPoolName(WM_THREAD_POOL_READ, LRT);
        tpTable.select(lrtName);
        tpTabs.select(Ids.build(Ids.JCA_WORKMANAGER, Ids.JCA_THREAD_POOL_ATTRIBUTES_TAB));
        assertEquals(lrtName, tpAttributesForm.value(NAME));

        String srtName = threadPoolName(WM_THREAD_POOL_READ, SRT);
        tpTable.select(srtName);
        tpTabs.select(Ids.build(Ids.JCA_WORKMANAGER, Ids.JCA_THREAD_POOL_ATTRIBUTES_TAB));
        assertEquals(srtName, tpAttributesForm.value(NAME));
    }

    @Test
    public void update() throws Exception {
        wmTable.action(WM_THREAD_POOL_UPDATE, Names.THREAD_POOLS);
        waitGui().until().element(tpTable.getRoot()).is().visible();

        String lrtName = threadPoolName(WM_THREAD_POOL_UPDATE, LRT);
        tpTable.select(lrtName);
        tpTabs.select(Ids.build(Ids.JCA_WORKMANAGER, Ids.JCA_THREAD_POOL_ATTRIBUTES_TAB));
        tpAttributesForm.edit();
        tpAttributesForm.flip(ALLOW_CORE_TIMEOUT, true);
        tpAttributesForm.save();

        console.verifySuccess();
        new ResourceVerifier(longRunningAddress(WM_THREAD_POOL_UPDATE, lrtName), client)
                .verifyAttribute(ALLOW_CORE_TIMEOUT, true);

        String srtName = threadPoolName(WM_THREAD_POOL_UPDATE, SRT);
        tpTable.select(srtName);
        tpTabs.select(Ids.build(Ids.JCA_WORKMANAGER, Ids.JCA_THREAD_POOL_SIZING_TAB));
        tpSizingForm.edit();
        tpSizingForm.number(MAX_THREADS, 111);
        tpSizingForm.save();

        console.verifySuccess();
        new ResourceVerifier(shortRunningAddress(WM_THREAD_POOL_UPDATE, srtName), client)
                .verifyAttribute(MAX_THREADS, 111);
    }

    @Test
    public void delete() throws Exception {
        wmTable.action(WM_THREAD_POOL_DELETE, Names.THREAD_POOLS);
        waitGui().until().element(tpTable.getRoot()).is().visible();

        String lrtName = threadPoolName(WM_THREAD_POOL_DELETE, LRT);
        tpTable.remove(lrtName);
        console.verifySuccess();
        new ResourceVerifier(longRunningAddress(WM_THREAD_POOL_DELETE, lrtName), client)
                .verifyDoesNotExist();

        String srtName = threadPoolName(WM_THREAD_POOL_DELETE, SRT);
        tpTable.remove(srtName);
        console.verifySuccess();
        new ResourceVerifier(shortRunningAddress(WM_THREAD_POOL_DELETE, srtName), client)
                .verifyDoesNotExist();
    }
}