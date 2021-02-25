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

import java.util.HashMap;
import java.util.Map;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.SelectFragment;
import org.jboss.hal.testsuite.page.configuration.LocalCachePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.commands.infinispan.cache.AddLocalCache;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CACHE_CONTAINER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.FILE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STORE;
import static org.jboss.hal.resources.CSS.bootstrapSelect;
import static org.jboss.hal.resources.Ids.LOCAL_CACHE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.CC_UPDATE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.LC_NO_STORE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.MAX_BATCH_SIZE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.cacheContainerAddress;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.storeAddress;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalCacheStoreTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(cacheContainerAddress(CC_UPDATE));
        client.apply(new AddLocalCache.Builder(LC_NO_STORE).cacheContainer(CC_UPDATE).build());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // operations.removeIfExists(cacheContainerAddress(CC_UPDATE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private LocalCachePage page;

    @Before
    public void setUp() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(CACHE_CONTAINER, CC_UPDATE);
        params.put(NAME, LC_NO_STORE);
        page.navigate(params);
        console.verticalNavigation().selectPrimary(Ids.build(LOCAL_CACHE, STORE, Ids.ITEM));
    }

    @Test
    public void fileStoreCreate() throws Exception {
        EmptyState emptyState = page.getLocalCacheNoStore();
        WebElement selectElement = emptyState.getRoot().findElement(By.cssSelector("div." + bootstrapSelect));
        SelectFragment select = Graphene.createPageFragment(SelectFragment.class, selectElement);
        waitGui().until().element(emptyState.getRoot()).is().visible();
        select.select(Names.FILE, FILE);
        emptyState.mainAction();
        console.verifySuccess();
        new ResourceVerifier(storeAddress(CC_UPDATE, LC_NO_STORE, FILE), client)
                .verifyExists();
    }

    @Test
    public void fileStoreUpdate() throws Exception {
        FormFragment form = page.getFileStoreForm();
        crud.update(storeAddress(CC_UPDATE, LC_NO_STORE, FILE), form, MAX_BATCH_SIZE, 123);
    }
}
