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
package org.jboss.hal.testsuite.test.configuration.jca;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.JcaPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ENABLED;
import static org.jboss.hal.testsuite.test.configuration.jca.JcaFixtures.ARCHIVE_VALIDATION_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.jca.JcaFixtures.BEAN_VALIDATION_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.jca.JcaFixtures.CACHED_CONNECTION_MANAGER_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.jca.JcaFixtures.DEBUG;

@RunWith(Arquillian.class)
public class JcaConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void beforeClass() throws CommandFailedException {
        backup = new BackupAndRestoreAttributes.Builder(JcaFixtures.SUBSYSTEM_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void afterClass() throws CommandFailedException {
        client.apply(backup.restore());
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private JcaPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.JCA_CONFIGURATION_ITEM);
    }

    @Test
    public void updateCachedConnectionManager() throws Exception {
        page.getConfigurationTabs().select(Ids.JCA_CCM_TAB);
        form = page.getCachedConnectionManagerForm();
        crud.update(CACHED_CONNECTION_MANAGER_ADDRESS, form, DEBUG, true);
    }

    @Test
    public void resetCachedConnectionManager() throws Exception {
        page.getConfigurationTabs().select(Ids.JCA_CCM_TAB);
        form = page.getCachedConnectionManagerForm();
        crud.reset(CACHED_CONNECTION_MANAGER_ADDRESS, form);
    }

    @Test
    public void updateArchiveValidation() throws Exception {
        page.getConfigurationTabs().select(Ids.JCA_ARCHIVE_VALIDATION_TAB);
        form = page.getArchiveValidationForm();
        crud.update(ARCHIVE_VALIDATION_ADDRESS, form, ENABLED, false);
    }

    @Test
    public void resetArchiveValidation() throws Exception {
        page.getConfigurationTabs().select(Ids.JCA_ARCHIVE_VALIDATION_TAB);
        form = page.getArchiveValidationForm();
        crud.reset(ARCHIVE_VALIDATION_ADDRESS, form);
    }

    @Test
    public void updateBeanValidation() throws Exception {
        page.getConfigurationTabs().select(Ids.JCA_BEAN_VALIDATION_TAB);
        form = page.getBeanValidationForm();
        crud.update(BEAN_VALIDATION_ADDRESS, form, ENABLED, false);
    }

    @Test
    public void resetBeanValidation() throws Exception {
        page.getConfigurationTabs().select(Ids.JCA_BEAN_VALIDATION_TAB);
        form = page.getBeanValidationForm();
        crud.reset(BEAN_VALIDATION_ADDRESS, form);
    }
}
