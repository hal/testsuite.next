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
package org.jboss.hal.testsuite.test.configuration.modcluster;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.ModclusterPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONNECTOR;
import static org.jboss.hal.testsuite.test.configuration.modcluster.ModclusterFixtures.*;

@RunWith(Arquillian.class)
public class ModclusterConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void beforeClass() throws Exception {
        backup = new BackupAndRestoreAttributes.Builder(CONFIG_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        client.apply(backup.restore());
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private ModclusterPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("modcluster-configuration-item");
    }

    @Test
    public void updateAdvertising() throws Exception {
        page.getTabs().select("advertising-tab");
        form = page.getAdvertisingForm();
        crud.update(CONFIG_ADDRESS, form, CONNECTOR, "https");
    }

    @Test
    public void updateSessions() throws Exception {
        page.getTabs().select("sessions-tab");
        form = page.getSessionsForm();
        crud.update(CONFIG_ADDRESS, form, STICKY_SESSION, false);
    }

    @Test
    public void updateWebContexts() throws Exception {
        page.getTabs().select("web-contexts-tab");
        form = page.getWebContextsForm();
        crud.update(CONFIG_ADDRESS, form, EXCLUDED_CONTEXTS);
    }

    @Test
    public void updateProxies() throws Exception {
        page.getTabs().select("proxies-tab");
        form = page.getProxiesForm();
        crud.update(CONFIG_ADDRESS, form, PROXY_URL);
    }

    @Test
    public void updateNetworking() throws Exception {
        page.getTabs().select("networking-tab");
        form = page.getNetworkingForm();
        crud.update(CONFIG_ADDRESS, form, NODE_TIMEOUT, 123);
    }
}
