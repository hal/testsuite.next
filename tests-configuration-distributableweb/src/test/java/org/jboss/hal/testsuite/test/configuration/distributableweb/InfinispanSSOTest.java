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
package org.jboss.hal.testsuite.test.configuration.distributableweb;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.DistributableWebPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CACHE_CONTAINER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.INFINISPAN_SSO_CREATE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.INFINISPAN_SSO_DELETE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.INFINISPAN_SSO_UPDATE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.infinispanSSOAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.CC_CREATE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.distributableweb.DistributableWebOperations.addCacheContainer;

@RunWith(Arquillian.class)
public class InfinispanSSOTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        addCacheContainer(client, operations, CC_CREATE);
        Values values = Values.of(CACHE_CONTAINER, CC_CREATE);
        operations.add(infinispanSSOAddress(INFINISPAN_SSO_UPDATE), values);
        operations.add(infinispanSSOAddress(INFINISPAN_SSO_DELETE), values);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(infinispanSSOAddress(INFINISPAN_SSO_DELETE));
        operations.removeIfExists(infinispanSSOAddress(INFINISPAN_SSO_UPDATE));
        operations.removeIfExists(infinispanSSOAddress(INFINISPAN_SSO_CREATE));
        operations.removeIfExists(cacheContainerAddress(CC_CREATE));
    }

    @Page private DistributableWebPage page;
    @Inject private CrudOperations crud;
    @Inject private Console console;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() {
        page.navigate();
        console.verticalNavigation().selectPrimary("dw-infinispan-sso-management-item");
        table = page.getInfinispanSSOManagementTable();
        form = page.getInfinispanSSOManagementForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(infinispanSSOAddress(INFINISPAN_SSO_CREATE), table, f -> {
            f.text(NAME, INFINISPAN_SSO_CREATE);
            f.text(CACHE_CONTAINER, CC_CREATE);
        });
    }

    @Test
    public void reset() throws Exception {
        table.select(INFINISPAN_SSO_UPDATE);
        crud.reset(infinispanSSOAddress(INFINISPAN_SSO_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        table.select(INFINISPAN_SSO_DELETE);
        crud.delete(infinispanSSOAddress(INFINISPAN_SSO_DELETE), table, INFINISPAN_SSO_DELETE);
    }
}
