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
package org.jboss.hal.testsuite.test.configuration.remoting;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.RemotingPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import static org.jboss.hal.testsuite.fixtures.RemotingFixtures.AUTH_REALM;
import static org.jboss.hal.testsuite.fixtures.RemotingFixtures.BUFFER_REGION_SIZE;
import static org.jboss.hal.testsuite.fixtures.RemotingFixtures.MAX_INBOUND_CHANNELS;
import static org.jboss.hal.testsuite.fixtures.RemotingFixtures.SUBSYSTEM_ADDRESS;

@RunWith(Arquillian.class)
public class ConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void beforeClass() throws Exception {
        backup = new BackupAndRestoreAttributes.Builder(SUBSYSTEM_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        client.apply(backup.restore());
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private RemotingPage page;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("remoting-configuration-item");
    }

    @Test
    public void updateAttributes() throws Exception {
        page.getConfigurationTabs().select("remoting-configuration-attributes-tab");
        FormFragment form = page.getConfigurationAttributesForm();
        crud.update(SUBSYSTEM_ADDRESS, form, BUFFER_REGION_SIZE, Random.number());
    }

    @Test
    public void updateSecurity() throws Exception {
        page.getConfigurationTabs().select("remoting-configuration-security-tab");
        FormFragment form = page.getConfigurationSecurityForm();
        crud.update(SUBSYSTEM_ADDRESS, form, AUTH_REALM, Random.name());
    }

    @Test
    public void updateChannels() throws Exception {
        page.getConfigurationTabs().select("remoting-configuration-channels-tab");
        FormFragment form = page.getConfigurationChannelsForm();
        crud.update(SUBSYSTEM_ADDRESS, form, MAX_INBOUND_CHANNELS, Random.number());
    }
}
