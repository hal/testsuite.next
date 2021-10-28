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
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.SelectFragment;
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

import static org.jboss.hal.dmr.ModelDescriptionConstants.AFFINITY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CACHE_CONTAINER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RANKED;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.ATTRIBUTE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.GRANULARITY;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.INFINISPAN_SESSION_AFFINITY;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.INFINISPAN_SESSION_CREATE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.INFINISPAN_SESSION_DELETE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.INFINISPAN_SESSION_UPDATE;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.SESSION;
import static org.jboss.hal.testsuite.fixtures.DistributableWebFixtures.infinispanSessionAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.CC_CREATE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.test.configuration.distributableweb.DistributableWebOperations.addCacheContainer;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class InfinispanSessionTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        addCacheContainer(client, operations, CC_CREATE);
        Values values = Values.of(CACHE_CONTAINER, CC_CREATE).and(GRANULARITY, SESSION);
        operations.add(infinispanSessionAddress(INFINISPAN_SESSION_UPDATE), values);
        operations.add(infinispanSessionAddress(INFINISPAN_SESSION_AFFINITY), values);
        operations.add(infinispanSessionAddress(INFINISPAN_SESSION_DELETE), values);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(infinispanSessionAddress(INFINISPAN_SESSION_DELETE));
        operations.removeIfExists(infinispanSessionAddress(INFINISPAN_SESSION_UPDATE));
        operations.removeIfExists(infinispanSessionAddress(INFINISPAN_SESSION_AFFINITY));
        operations.removeIfExists(infinispanSessionAddress(INFINISPAN_SESSION_CREATE));
        operations.removeIfExists(cacheContainerAddress(CC_CREATE));
    }

    @Page
    private DistributableWebPage page;
    @Inject
    private CrudOperations crud;
    @Inject
    private Console console;
    private TableFragment table;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("dw-infinispan-session-management-item");
        table = page.getInfinispanSessionManagementTable();
        form = page.getInfinispanSessionManagementForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(infinispanSessionAddress(INFINISPAN_SESSION_CREATE), table, f -> {
            f.text(NAME, INFINISPAN_SESSION_CREATE);
            f.text(CACHE_CONTAINER, CC_CREATE);
        });
    }

    @Test
    public void update() throws Exception {
        table.select(INFINISPAN_SESSION_UPDATE);
        crud.update(infinispanSessionAddress(INFINISPAN_SESSION_UPDATE), form,
                f -> f.select(GRANULARITY, ATTRIBUTE),
                verifier -> verifier.verifyAttribute(GRANULARITY, ATTRIBUTE));
    }

    @Test
    public void switchAffinity() throws Exception {
        table.select(INFINISPAN_SESSION_AFFINITY);
        page.getInfinispanSessionManagementTabs().select("dw-infinispan-session-management-affinity-tab");
        SelectFragment select = page.getSwitchAffinity();
        if (select != null) {
            select.select(RANKED);
            console.verifySuccess();
            new ResourceVerifier(infinispanSessionAddress(INFINISPAN_SESSION_AFFINITY).and(AFFINITY, RANKED), client)
                    .verifyExists();
        } else {
            fail("Select control to switch affinity not found!");
        }

    }

    @Test
    public void reset() throws Exception {
        table.select(INFINISPAN_SESSION_UPDATE);
        crud.reset(infinispanSessionAddress(INFINISPAN_SESSION_UPDATE), form);
    }

    @Test
    public void delete() throws Exception {
        table.select(INFINISPAN_SESSION_DELETE);
        crud.delete(infinispanSessionAddress(INFINISPAN_SESSION_DELETE), table, INFINISPAN_SESSION_DELETE);
    }
}
