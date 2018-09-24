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
package org.jboss.hal.testsuite.test.configuration.infinispan.cache.container;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.page.Places;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.TimeoutException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.INFINISPAN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.CC_CREATE;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.CC_DELETE;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.CC_READ;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class CacheContainerFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(cacheContainerAddress(CC_READ));
        operations.add(cacheContainerAddress(CC_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(cacheContainerAddress(CC_CREATE));
        operations.removeIfExists(cacheContainerAddress(CC_READ));
        operations.removeIfExists(cacheContainerAddress(CC_DELETE));
    }

    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(INFINISPAN))
                .column(Ids.CACHE_CONTAINER);
    }

    @Test
    public void create() throws Exception {
        column.dropdownAction(Ids.CACHE_CONTAINER_ADD_ACTIONS, Ids.CACHE_CONTAINER_ADD);
        AddResourceDialogFragment dialog = console.addResourceDialog();
        dialog.getForm().text(NAME, CC_CREATE);
        dialog.add();

        console.verifySuccess();
        assertTrue(column.containsItem(Ids.cacheContainer(CC_CREATE)));
        new ResourceVerifier(cacheContainerAddress(CC_CREATE), client).verifyExists();
    }

    @Test
    public void read() {
        assertTrue(column.containsItem(Ids.cacheContainer(CC_READ)));
    }

    @Test
    public void select() {
        column.selectItem(Ids.cacheContainer(CC_READ));
        PlaceRequest placeRequest = Places.finderPlace(NameTokens.CONFIGURATION,
                configurationSubsystemPath(INFINISPAN)
                        .append(Ids.CACHE_CONTAINER, Ids.cacheContainer(CC_READ)));
        console.verify(placeRequest);
    }

    @Test
    public void view() {
        try {
            column.selectItem(Ids.cacheContainer(CC_READ)).view();
        } catch (TimeoutException e) {
            Assert.fail("Not possible to open Cache container detail probably due to https://issues.jboss.org/browse/HAL-1442");
        }

        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.CACHE_CONTAINER)
                .with(NAME, CC_READ)
                .build();
        console.verify(placeRequest);
    }

    @Test
    public void delete() throws Exception {
        column.selectItem(Ids.cacheContainer(CC_DELETE)).dropdown().click("Remove");
        console.confirmationDialog().confirm();

        console.verifySuccess();
        assertFalse(column.containsItem(Ids.cacheContainer(CC_DELETE)));
        new ResourceVerifier(cacheContainerAddress(CC_DELETE), client).verifyDoesNotExist();
    }
}
