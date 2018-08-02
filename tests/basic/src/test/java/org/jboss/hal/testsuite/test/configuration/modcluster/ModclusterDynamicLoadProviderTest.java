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

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONNECTOR;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.modcluster.ModclusterFixtures.HISTORY;
import static org.jboss.hal.testsuite.test.configuration.modcluster.ModclusterFixtures.PROXY_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.modcluster.ModclusterFixtures.dynamicLoadProviderAddress;
import static org.jboss.hal.testsuite.test.configuration.modcluster.ModclusterFixtures.proxyAddress;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class ModclusterDynamicLoadProviderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(proxyAddress(PROXY_UPDATE), Values.of(CONNECTOR, DEFAULT));
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
        console.verticalNavigation().selectPrimary("dynamic-load-provider-item");
        form = page.getDynamicLoadProviderForm();
    }

    @Test
    public void create() throws Exception {
        crud.createSingleton(dynamicLoadProviderAddress(PROXY_UPDATE), form);
    }

    @Test
    public void reset() throws Exception {
        crud.reset(dynamicLoadProviderAddress(PROXY_UPDATE), form);
    }

    @Test
    public void update() throws Exception {
        crud.update(dynamicLoadProviderAddress(PROXY_UPDATE), form, HISTORY, Random.number());
    }

    @Test
    public void zzzDelete() throws Exception {
        crud.deleteSingleton(dynamicLoadProviderAddress(PROXY_UPDATE), form);
    }
}
