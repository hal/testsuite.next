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
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.PagesFragment;
import org.jboss.hal.testsuite.fragment.SelectFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.CacheContainerPage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.commands.infinispan.cache.AddLocalCache;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Headers;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.FILE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.CSS.bootstrapSelect;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.*;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class LocalCacheStoreTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(cacheContainerAddress(CC_UPDATE));
        client.apply(new AddLocalCache.Builder(LC_NO_STORE).cacheContainer(CC_UPDATE).build());
        client.apply(new AddLocalCache.Builder(LC_FILE_STORE).cacheContainer(CC_UPDATE).build());
        operations.headers(Headers.allowResourceServiceRestart()).add(storeAddress(CC_UPDATE, LC_FILE_STORE, FILE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_NO_STORE));
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_FILE_STORE));
        operations.removeIfExists(cacheContainerAddress(CC_UPDATE));
    }

    @Inject private Console console;
    @Page private CacheContainerPage page;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        try {
            page.navigate(NAME, CC_UPDATE);
        } catch (TimeoutException e) {
            Assert.fail("Not possible to open Cache container detail probably due to https://issues.jboss.org/browse/HAL-1442");
        }
        console.verticalNavigation().selectPrimary(Ids.LOCAL_CACHE + "-" + Ids.ITEM);
        page.bindForms();
        table = page.getLocalCacheTable();
    }

    @Test
    public void createFileStore() throws Exception {
        EmptyState emptyState = page.getLocalCacheNoStore();
        WebElement selectElement = emptyState.getRoot().findElement(By.cssSelector("div." + bootstrapSelect));
        SelectFragment select = Graphene.createPageFragment(SelectFragment.class, selectElement);

        table.action(LC_NO_STORE, Names.STORE);
        waitGui().until().element(emptyState.getRoot()).is().visible();
        select.select(Names.FILE, FILE);
        emptyState.mainAction();

        console.verifySuccess();
        new ResourceVerifier(storeAddress(CC_UPDATE, LC_NO_STORE, FILE), client)
                .verifyExists();
    }

    @Test
    public void readFileStore() {
        PagesFragment pages = page.getLocalCachePages();

        table.action(LC_FILE_STORE, Names.STORE);
        waitGui().until().element(pages.getRoot()).is().visible();
        assertEquals(Names.STORE + ": " + Names.FILE, pages.breadcrumb().lastValue());
    }
}
