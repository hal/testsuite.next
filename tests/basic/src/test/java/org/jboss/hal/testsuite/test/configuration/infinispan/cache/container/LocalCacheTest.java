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
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.LocalCachePage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.wildfly.extras.creaper.commands.infinispan.cache.AddLocalCache;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.*;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalCacheTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(cacheContainerAddress(CC_UPDATE));
        client.apply(new AddLocalCache.Builder(LC_UPDATE).cacheContainer(CC_UPDATE).build());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(cacheContainerAddress(CC_UPDATE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private LocalCachePage page;

    @Before
    public void setUp() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put(CACHE_CONTAINER, CC_UPDATE);
        params.put(NAME, LC_UPDATE);
        page.navigate(params);
        console.verticalNavigation().selectPrimary(Ids.LOCAL_CACHE + "-" + Ids.ITEM);
    }

    // ------------------------------------------------------ attributes

    @Test
    public void attributesEdit() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-tab");
        FormFragment form = page.getConfigurationForm();

        String moduleName = Random.name();
        crud.update(localCacheAddress(CC_UPDATE, LC_UPDATE), form,
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
    public void attributesReset() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-tab");
        FormFragment form = page.getConfigurationForm();
        crud.reset(localCacheAddress(CC_UPDATE, LC_UPDATE), form);
    }

    // ------------------------------------------------------ expiration

    @Test
    public void expiration1Edit() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-cache-component-expiration-tab");
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
    public void expiration2Reset() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-cache-component-expiration-tab");
        FormFragment form = page.getExpirationForm();
        crud.reset(componentAddress(CC_UPDATE, LC_UPDATE, EXPIRATION), form);
    }

    @Test
    public void expiration3Remove() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-cache-component-expiration-tab");
        FormFragment form = page.getExpirationForm();
        crud.deleteSingleton(componentAddress(CC_UPDATE, LC_UPDATE, EXPIRATION), form);
    }

    // ------------------------------------------------------ locking

    @Test
    public void locking1Edit() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-cache-component-locking-tab");
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
    public void locking2Reset() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-cache-component-locking-tab");
        FormFragment form = page.getLockingForm();
        crud.reset(componentAddress(CC_UPDATE, LC_UPDATE, LOCKING), form);
    }

    @Test
    public void locking3Remove() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-cache-component-locking-tab");
        FormFragment form = page.getLockingForm();
        crud.deleteSingleton(componentAddress(CC_UPDATE, LC_UPDATE, LOCKING), form);
    }

    // ------------------------------------------------------ transaction

    @Test
    public void transaction1Edit() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-cache-component-transaction-tab");
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
    public void transaction2Reset() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-cache-component-transaction-tab");
        FormFragment form = page.getTransactionForm();
        crud.reset(componentAddress(CC_UPDATE, LC_UPDATE, TRANSACTION), form);
    }

    @Test
    public void transaction3Remove() throws Exception {
        console.verticalNavigation().selectPrimary(LOCAL_CACHE_ITEM);
        page.getLocalCacheTabs().select("local-cache-cache-component-transaction-tab");
        FormFragment form = page.getTransactionForm();
        crud.deleteSingleton(componentAddress(CC_UPDATE, LC_UPDATE, TRANSACTION), form);
    }
}
