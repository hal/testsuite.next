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
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.infinispan.cache.AddLocalCache;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.JNDI_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STATISTICS_ENABLED;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.*;

@RunWith(Arquillian.class)
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
        operations.removeIfExists(localCacheAddress(CC_UPDATE, LC_UPDATE));
        operations.removeIfExists(cacheContainerAddress(CC_UPDATE));
    }

    @Drone private WebDriver browser;
    @Inject private Console console;
    @Page private CacheContainerPage page;
    private TableFragment table;
    private TabsFragment tabs;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, CC_UPDATE);
        page.getLocalCacheItem().click();
        page.bindForms();
        table = page.getLocalCacheTable();
        tabs = page.getLocalCacheTabs();
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, LC_CREATE);
        dialog.add();

        console.success();
        new ResourceVerifier(localCacheAddress(CC_UPDATE, LC_CREATE), client).verifyExists();
    }

    @Test
    public void resetAttributes() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.TAB));
        page.getLocalCacheForm().reset();
        console.success();
    }

    @Test
    public void updateAttributes() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.TAB));
        FormFragment form = page.getLocalCacheForm();

        String jndiName = Random.jndiName();
        form.edit();
        form.text(JNDI_NAME, jndiName);
        form.bootstrapSwitch(STATISTICS_ENABLED, true);
        form.save();

        console.success();
        new ResourceVerifier(localCacheAddress(CC_UPDATE, LC_UPDATE), client)
                .verifyAttribute(JNDI_NAME, jndiName)
                .verifyAttribute(STATISTICS_ENABLED, true);
    }

    @Test
    public void resetEviction() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EVICTION, Ids.TAB));
        page.getEvictionForm().reset();
        console.success();
    }

    @Test
    public void updateEviction() throws Exception {

    }

    @Test
    public void resetExpiration() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_EXPIRATION, Ids.TAB));
        page.getExpirationForm().reset();
        console.success();

    }

    @Test
    public void updateExpiration() throws Exception {

    }

    @Test
    public void resetLocking() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_LOCKING, Ids.TAB));
        page.getLockingForm().reset();
        console.success();
    }

    @Test
    public void updateLocking() throws Exception {

    }

    @Test
    public void resetTransaction() throws Exception {
        table.select(LC_UPDATE);
        tabs.select(Ids.build(Ids.LOCAL_CACHE, Ids.CACHE_COMPONENT_TRANSACTION, Ids.TAB));
        page.getTransactionForm().reset();
        console.success();
    }

    @Test
    public void updateTransaction() throws Exception {

    }
}
