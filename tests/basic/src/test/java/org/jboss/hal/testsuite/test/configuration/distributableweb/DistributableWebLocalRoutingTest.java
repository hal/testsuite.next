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
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.SelectFragment;
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
import static org.jboss.hal.dmr.ModelDescriptionConstants.INFINISPAN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.LOCAL;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ROUTING;
import static org.jboss.hal.testsuite.test.configuration.distributableweb.DistributableWebFixtures.SUBSYSTEM_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.distributableweb.DistributableWebOperations.addCacheContainer;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.CC_READ;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.junit.Assert.fail;

/** Switch from infinispan to local routing */
@RunWith(Arquillian.class)
public class DistributableWebLocalRoutingTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        addCacheContainer(client, operations, CC_READ);
        operations.add(SUBSYSTEM_ADDRESS.and(ROUTING, INFINISPAN), Values.of(CACHE_CONTAINER, CC_READ));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(cacheContainerAddress(CC_READ));
    }

    @Page private DistributableWebPage page;
    @Inject private Console console;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("dw-routing-item");
    }

    @Test
    public void switchToInfinispan() {
        console.waitNoNotification();
        SelectFragment select = page.getSwitchRouting();
        if (select != null) {
            select.select(Names.LOCAL, LOCAL);
            console.verifySuccess();
        } else {
            fail("Select control to switch routing not found!");
        }
    }
}
