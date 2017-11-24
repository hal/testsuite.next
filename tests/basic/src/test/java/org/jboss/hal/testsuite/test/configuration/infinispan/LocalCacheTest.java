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
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.fragment.TabsFragment;
import org.jboss.hal.testsuite.page.configuration.CacheContainerPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.commands.infinispan.cache.AddLocalCache;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.*;

@RunWith(Arquillian.class)
public class LocalCacheTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(cacheContainerAddress(CC_UPDATE));
        client.apply(new AddLocalCache.Builder(LC_UPDATE).cacheContainer(CC_UPDATE).build());
        client.apply(new AddLocalCache.Builder(LC_RESET).cacheContainer(CC_UPDATE).build());
        client.apply(new AddLocalCache.Builder(LC_REMOVE).cacheContainer(CC_UPDATE).build());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_UPDATE));
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_RESET));
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_REMOVE));
        operations.removeIfExists(cacheContainerAddress(CC_UPDATE));
    }

    @Inject private Console console;
    @Page private CacheContainerPage page;
    private TableFragment table;
    private TabsFragment tabs;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, CC_UPDATE);
        console.verticalNavigation().selectPrimary(Ids.LOCAL_CACHE + "-" + Ids.ITEM);

        page.bindForms();
        table = page.getLocalCacheTable();
        tabs = page.getLocalCacheTabs();
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, LC_CREATE);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(localCacheAddress(CC_UPDATE, LC_CREATE), client).verifyExists();
    }

    // ------------------------------------------------------ attributes

    @Test
    public void updateAttributes() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.TAB));
        FormFragment form = page.getLocalCacheForm();

        String jndiName = Random.jndiName();
        form.edit();
        form.text(JNDI_NAME, jndiName);
        form.flip(STATISTICS_ENABLED, true);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(localCacheAddress(CC_UPDATE, LC_UPDATE), client)
                .verifyAttribute(JNDI_NAME, jndiName)
                .verifyAttribute(STATISTICS_ENABLED, true);
    }

    @Test
    public void resetAttributes() throws Exception {
        table.select(LC_RESET);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.TAB));
        page.getLocalCacheForm().reset();

        console.verifySuccess();
        new ResourceVerifier(localCacheAddress(CC_UPDATE, LC_RESET), client)
                .verifyReset();
    }

    // ------------------------------------------------------ eviction

    @Test
    public void updateEviction() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EVICTION, Ids.TAB));
        FormFragment form = page.getEvictionForm();

        form.edit();
        form.number(MAX_ENTRIES, 23);
        form.select(STRATEGY, "LRU");
        form.save();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_UPDATE, EVICTION), client)
                .verifyAttribute(MAX_ENTRIES, 23L)
                .verifyAttribute(STRATEGY, "LRU");
    }

    @Test
    public void resetEviction() throws Exception {
        table.select(LC_RESET);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EVICTION, Ids.TAB));
        page.getEvictionForm().reset();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_RESET, EVICTION), client)
                .verifyReset();
    }

    @Test
    public void removeEviction() throws Exception {
        table.select(LC_REMOVE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EVICTION, Ids.TAB));
        page.getEvictionForm().remove();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_REMOVE, EVICTION), client)
                .verifyDoesNotExist();
    }

    // ------------------------------------------------------ expiration

    @Test
    public void updateExpiration() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EXPIRATION, Ids.TAB));
        FormFragment form = page.getExpirationForm();

        form.edit();
        form.number(INTERVAL, 1);
        form.number(LIFESPAN, 2);
        form.number(MAX_IDLE, 3);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_UPDATE, EXPIRATION), client)
                .verifyAttribute(INTERVAL, 1L)
                .verifyAttribute(LIFESPAN, 2L)
                .verifyAttribute(MAX_IDLE, 3L);
    }

    @Test
    public void resetExpiration() throws Exception {
        table.select(LC_RESET);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EXPIRATION, Ids.TAB));
        page.getExpirationForm().reset();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_RESET, EXPIRATION), client)
                .verifyReset();
    }

    @Test
    public void removeExpiration() throws Exception {
        table.select(LC_REMOVE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EXPIRATION, Ids.TAB));
        page.getExpirationForm().remove();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_REMOVE, EXPIRATION), client)
                .verifyDoesNotExist();
    }

    // ------------------------------------------------------ locking

    @Test
    public void updateLocking() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_LOCKING, Ids.TAB));
        FormFragment form = page.getLockingForm();

        form.edit();
        form.number(ACQUIRE_TIMEOUT, 1);
        form.number(CONCURRENCY_LEVEL, 100);
        form.select(ISOLATION, "NONE");
        form.save();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_UPDATE, LOCKING), client)
                .verifyAttribute(ACQUIRE_TIMEOUT, 1L)
                .verifyAttribute(CONCURRENCY_LEVEL, 100)
                .verifyAttribute(ISOLATION, "NONE");
    }

    @Test
    public void resetLocking() throws Exception {
        table.select(LC_RESET);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_LOCKING, Ids.TAB));
        page.getLockingForm().reset();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_RESET, LOCKING), client)
                .verifyReset();
    }

    @Test
    public void removeLocking() throws Exception {
        table.select(LC_REMOVE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_LOCKING, Ids.TAB));
        page.getLockingForm().remove();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_REMOVE, LOCKING), client)
                .verifyDoesNotExist();
    }

    // ------------------------------------------------------ transaction

    @Test
    public void updateTransaction() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_TRANSACTION, Ids.TAB));
        FormFragment form = page.getTransactionForm();

        form.edit();
        form.select(LOCKING, "OPTIMISTIC");
        form.select(MODE, "BATCH");
        form.save();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_UPDATE, TRANSACTION), client)
                .verifyAttribute(LOCKING, "OPTIMISTIC")
                .verifyAttribute(MODE, "BATCH");
    }

    @Test
    public void resetTransaction() throws Exception {
        table.select(LC_RESET);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_TRANSACTION, Ids.TAB));
        page.getTransactionForm().reset();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_RESET, TRANSACTION), client)
                .verifyReset();
    }

    @Test
    public void removeTransaction() throws Exception {
        table.select(LC_REMOVE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_TRANSACTION, Ids.TAB));
        page.getTransactionForm().remove();

        console.verifySuccess();
        new ResourceVerifier(componentAddress(CC_UPDATE, LC_REMOVE, TRANSACTION), client)
                .verifyDoesNotExist();
    }
}
