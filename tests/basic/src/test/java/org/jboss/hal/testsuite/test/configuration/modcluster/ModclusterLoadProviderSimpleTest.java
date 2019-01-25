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
package org.jboss.hal.testsuite.test.configuration.modcluster;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.ModclusterPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.LISTENER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.modcluster.ModclusterFixtures.*;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class ModclusterLoadProviderSimpleTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(proxyAddress(PROXY_UPDATE), Values.of(LISTENER, DEFAULT));
        operations.remove(loadProviderSimpleAddress(PROXY_UPDATE));
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.remove(proxyAddress(PROXY_UPDATE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private ModclusterPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, PROXY_UPDATE);
        console.verticalNavigation().selectPrimary("load-provider-simple-item");
        form = page.getLoadProviderSimpleForm();
    }

    @Test
    public void _0create() throws Exception {
        crud.createSingleton(loadProviderSimpleAddress(PROXY_UPDATE), form);
    }

    @Test
    public void _1noDynamicProvider() {
        console.verticalNavigation().selectPrimary("load-provider-dynamic-item");
        EmptyState empty = page.getLoadProviderDynamicEmpty();
        assertTrue(empty.getRoot().isDisplayed());
    }

    @Test
    public void _2customLoadMetricsWarning() {
        console.verticalNavigation().selectPrimary("custom-load-metrics-item");
        assertTrue(page.getCustomLoadMetricAlert().isWarning());
    }

    @Test
    public void _3loadMetricsWarning() {
        console.verticalNavigation().selectPrimary("load-metrics-item");
        assertTrue(page.getLoadMetricAlert().isWarning());
    }

    @Test
    public void _4reset() throws Exception {
        crud.reset(loadProviderSimpleAddress(PROXY_UPDATE), form);
    }

    @Test
    public void _5update() throws Exception {
        crud.update(loadProviderSimpleAddress(PROXY_UPDATE), form, FACTOR, Random.number());
    }

    @Test
    public void _6delete() throws Exception {
        crud.deleteSingleton(loadProviderSimpleAddress(PROXY_UPDATE), form);
    }
}
