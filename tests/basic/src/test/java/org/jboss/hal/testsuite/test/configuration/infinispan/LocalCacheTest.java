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
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
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
        client.apply(new AddLocalCache.Builder(LC_UPDATE_ATTRIBUTES).cacheContainer(CC_UPDATE).build());
        client.apply(new AddLocalCache.Builder(LC_RESET).cacheContainer(CC_UPDATE).build());
        client.apply(new AddLocalCache.Builder(LC_RESET_TRANSACTION).cacheContainer(CC_UPDATE).build());
        client.apply(new AddLocalCache.Builder(LC_REMOVE).cacheContainer(CC_UPDATE).build());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_UPDATE));
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_UPDATE_ATTRIBUTES));
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_RESET));
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_RESET_TRANSACTION));
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_REMOVE));
        operations.removeIfExists(cacheContainerAddress(CC_UPDATE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
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
        crud.create(localCacheAddress(CC_UPDATE, LC_CREATE), table, LC_CREATE);
    }

    // ------------------------------------------------------ attributes

    @Test
    public void updateAttributes() throws Exception {
        table.select(LC_UPDATE_ATTRIBUTES);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.TAB));
        FormFragment form = page.getLocalCacheForm();

        String moduleName = Random.name();
        crud.update(localCacheAddress(CC_UPDATE, LC_UPDATE_ATTRIBUTES), form,
                f -> {
                    f.text(MODULE, moduleName);
                    f.flip(STATISTICS_ENABLED, true);
                },
                resourceVerifier -> {
                    resourceVerifier.verifyAttribute(MODULE, moduleName);
                    resourceVerifier.verifyAttribute(STATISTICS_ENABLED, true);

                });
    }

    @Test
    public void resetAttributes() throws Exception {
        table.select(LC_RESET);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.TAB));
        FormFragment form = page.getLocalCacheForm();
        crud.reset(localCacheAddress(CC_UPDATE, LC_RESET), form);
    }

    // ------------------------------------------------------ expiration

    @Test
    public void updateExpiration() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EXPIRATION, Ids.TAB));
        FormFragment form = page.getExpirationForm();

        crud.update(componentAddress(CC_UPDATE, LC_UPDATE, EXPIRATION), form,
                f -> {
                    f.number(INTERVAL, 1);
                    f.number(LIFESPAN, 2);
                    f.number(MAX_IDLE, 3);
                },
                resourceVerifier -> {
                    resourceVerifier.verifyAttribute(INTERVAL, 1L);
                    resourceVerifier.verifyAttribute(LIFESPAN, 2L);
                    resourceVerifier.verifyAttribute(MAX_IDLE, 3L);
                });
    }

    @Test
    public void resetExpiration() throws Exception {
        table.select(LC_RESET);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EXPIRATION, Ids.TAB));
        FormFragment form = page.getExpirationForm();
        crud.reset(componentAddress(CC_UPDATE, LC_RESET, EXPIRATION), form);
    }

    @Test
    public void removeExpiration() throws Exception {
        table.select(LC_REMOVE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EXPIRATION, Ids.TAB));
        FormFragment form = page.getExpirationForm();
        crud.deleteSingleton(componentAddress(CC_UPDATE, LC_REMOVE, EXPIRATION), form);
    }

    // ------------------------------------------------------ locking

    @Test
    public void updateLocking() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_LOCKING, Ids.TAB));
        FormFragment form = page.getLockingForm();

        crud.update(componentAddress(CC_UPDATE, LC_UPDATE, LOCKING), form,
                f -> {
                    f.number(ACQUIRE_TIMEOUT, 1);
                    f.number(CONCURRENCY_LEVEL, 100);
                    f.select(ISOLATION, "NONE");
                },
                resourceVerifier -> {
                    resourceVerifier.verifyAttribute(ACQUIRE_TIMEOUT, 1L);
                    resourceVerifier.verifyAttribute(CONCURRENCY_LEVEL, 100);
                    resourceVerifier.verifyAttribute(ISOLATION, "NONE");
                });
    }

    @Test
    public void resetLocking() throws Exception {
        table.select(LC_RESET);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_LOCKING, Ids.TAB));
        FormFragment form = page.getLockingForm();
        crud.reset(componentAddress(CC_UPDATE, LC_RESET, LOCKING), form);
    }

    @Test
    public void removeLocking() throws Exception {
        table.select(LC_REMOVE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_LOCKING, Ids.TAB));
        FormFragment form = page.getLockingForm();
        crud.deleteSingleton(componentAddress(CC_UPDATE, LC_REMOVE, LOCKING), form);
    }

    // ------------------------------------------------------ transaction

    @Test
    public void updateTransaction() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_TRANSACTION, Ids.TAB));
        FormFragment form = page.getTransactionForm();

        crud.update(componentAddress(CC_UPDATE, LC_UPDATE, TRANSACTION), form,
                f -> {
                    f.select(LOCKING, "OPTIMISTIC");
                    f.select(MODE, "BATCH");
                },
                resourceVerifier -> {
                    resourceVerifier.verifyAttribute(LOCKING, "OPTIMISTIC");
                    resourceVerifier.verifyAttribute(MODE, "BATCH");
                });
    }

    @Test
    public void resetTransaction() throws Exception {
        table.select(LC_RESET_TRANSACTION);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_TRANSACTION, Ids.TAB));
        FormFragment form = page.getTransactionForm();
        crud.reset(componentAddress(CC_UPDATE, LC_RESET_TRANSACTION, TRANSACTION), form);
    }

    @Test
    public void removeTransaction() throws Exception {
        table.select(LC_REMOVE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_TRANSACTION, Ids.TAB));
        FormFragment form = page.getTransactionForm();
        crud.deleteSingleton(componentAddress(CC_UPDATE, LC_REMOVE, TRANSACTION), form);
    }
}
