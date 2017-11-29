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
package org.jboss.hal.testsuite.test.configuration.jgroups;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.configuration.JGroupsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static java.util.Arrays.asList;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.INTERNAL;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MAX_THREADS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.testsuite.test.configuration.jgroups.JGroupsFixtures.*;

@RunWith(Arquillian.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class StackTransportTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);


    @BeforeClass
    public static void beforeClass() throws Exception {
        Batch stackCreate = new Batch();
        stackCreate.add(stackAddress(STACK_CREATE));
        stackCreate.add(transportAddress(STACK_CREATE, TRANSPORT_CREATE), Values.of(SOCKET_BINDING, JGROUPS_TCP));
        operations.batch(stackCreate);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(stackAddress(STACK_CREATE));
    }

    @Page private JGroupsPage page;
    @Inject private Console console;
    private TableFragment stackTable;
    private TableFragment transportTable;
    private FormFragment transportAttributesForm;
    private FormFragment transportTPDefaultForm;
    private FormFragment transportTPInternalForm;
    private TabsFragment threadPoolTab;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("jgroups-stack-item");

        threadPoolTab = page.getTransportThreadPoolTab();
        stackTable = page.getStackTable();
        transportTable = page.getTransportTable();
        transportAttributesForm = page.getTransportAttributesForm();
        transportTPDefaultForm = page.getTransportThreadPoolDefaultForm();
        transportTPInternalForm = page.getTransportThreadPoolInternalForm();
        transportTable.bind(asList(transportAttributesForm, transportTPDefaultForm, transportTPInternalForm));
    }

    @Test()
    public void updateAttributes() throws Exception {
        stackTable.action(STACK_CREATE, Names.TRANSPORT);
        waitGui().until().element(transportTable.getRoot()).is().visible();

        String site = Random.name();
        transportTable.select(TRANSPORT_CREATE);
        transportAttributesForm.edit();
        transportAttributesForm.text(SITE, site);
        transportAttributesForm.save();

        console.verifySuccess();
        new ResourceVerifier(transportAddress(STACK_CREATE, TRANSPORT_CREATE), client)
                .verifyAttribute(SITE, site);
    }

    @Test()
    public void threadPoolDefaultEdit() throws Exception {
        stackTable.action(STACK_CREATE, Names.TRANSPORT);
        waitGui().until().element(transportTable.getRoot()).is().visible();

        transportTable.select(TRANSPORT_CREATE);
        threadPoolTab.select(Ids.build(Ids.JGROUPS_TRANSPORT_THREADPOOL_DEFAULT_TAB));
        transportTPDefaultForm.edit();
        transportTPDefaultForm.number(MAX_THREADS, 123);
        transportTPDefaultForm.save();

        console.verifySuccess();
        new ResourceVerifier(transportThreadPoolAddress(STACK_CREATE, TRANSPORT_CREATE, DEFAULT), client)
                .verifyAttribute(MAX_THREADS, 123);
    }

    @Test()
    public void threadPoolDefaultReset() throws Exception {
        stackTable.action(STACK_CREATE, Names.TRANSPORT);
        waitGui().until().element(transportTable.getRoot()).is().visible();

        transportTable.select(TRANSPORT_CREATE);
        threadPoolTab.select(Ids.build(Ids.JGROUPS_TRANSPORT_THREADPOOL_DEFAULT_TAB));
        transportTPDefaultForm.reset();

        console.verifySuccess();
        new ResourceVerifier(transportThreadPoolAddress(STACK_CREATE, TRANSPORT_CREATE, DEFAULT), client)
                .verifyReset();
    }

    @Test()
    public void threadPoolInternalEdit() throws Exception {
        stackTable.action(STACK_CREATE, Names.TRANSPORT);
        waitGui().until().element(transportTable.getRoot()).is().visible();

        transportTable.select(TRANSPORT_CREATE);
        threadPoolTab.select(Ids.build(Ids.JGROUPS_TRANSPORT_THREADPOOL_INTERNAL_TAB));
        transportTPInternalForm.edit();
        long val = 5123;
        transportTPInternalForm.number(KEEPALIVE_TIME, val);
        transportTPInternalForm.save();

        console.verifySuccess();
        new ResourceVerifier(transportThreadPoolAddress(STACK_CREATE, TRANSPORT_CREATE, INTERNAL), client)
                .verifyAttribute(KEEPALIVE_TIME, val);
    }

    @Test()
    public void threadPoolInternalReset() throws Exception {
        stackTable.action(STACK_CREATE, Names.TRANSPORT);
        waitGui().until().element(transportTable.getRoot()).is().visible();

        transportTable.select(TRANSPORT_CREATE);
        threadPoolTab.select(Ids.build(Ids.JGROUPS_TRANSPORT_THREADPOOL_INTERNAL_TAB));
        transportTPInternalForm.reset();

        console.verifySuccess();
        new ResourceVerifier(transportThreadPoolAddress(STACK_CREATE, TRANSPORT_CREATE, INTERNAL), client)
                .verifyReset();
    }


}