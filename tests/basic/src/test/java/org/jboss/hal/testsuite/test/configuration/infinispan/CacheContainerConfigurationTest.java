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
package org.jboss.hal.testsuite.test.configuration.infinispan;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.BreadcrumbFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.CacheContainerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.CC_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class CacheContainerConfigurationTest {

    private static final String ALIASES = "aliases";
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(cacheContainerAddress(CC_UPDATE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(cacheContainerAddress(CC_UPDATE));
    }


    @Drone private WebDriver browser;
    @Inject private Console console;
    @Page private CacheContainerPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, CC_UPDATE);
        page.getConfigurationItem().click();
        form = page.getConfigurationForm();
    }

    @Test
    public void view() throws Exception {
        assertEquals(BreadcrumbFragment.abbreviate(CC_UPDATE), console.header().breadcrumb().lastValue());
    }

    @Test
    public void reset() throws Exception {
        // TODO Test reset
    }

    @Test
    public void update() throws Exception {
        String aliases = Random.name();
        form.edit();
        form.list(ALIASES).add(aliases);
        form.save();

        console.success();
        new ResourceVerifier(cacheContainerAddress(CC_UPDATE), client, 500)
                .verifyListAttributeContainsValue(ALIASES, aliases);
    }
}
