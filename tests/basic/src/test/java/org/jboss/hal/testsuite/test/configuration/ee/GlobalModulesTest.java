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
package org.jboss.hal.testsuite.test.configuration.ee;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.EEPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.GLOBAL_MODULES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.ee.EEFixtures.GLOBAL_MODULES_CREATE;
import static org.jboss.hal.testsuite.test.configuration.ee.EEFixtures.GLOBAL_MODULES_DELETE;
import static org.jboss.hal.testsuite.test.configuration.ee.EEFixtures.SUBSYSTEM_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.ee.EEFixtures.globalModule;

@RunWith(Arquillian.class)
public class GlobalModulesTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void beforeClass() throws Exception {

        operations.writeListAttribute(SUBSYSTEM_ADDRESS, GLOBAL_MODULES,
                globalModule(GLOBAL_MODULES_DELETE));
        backup = new BackupAndRestoreAttributes.Builder(SUBSYSTEM_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void afterClass() throws Exception {
        client.apply(backup.restore());
    }

    @Page private EEPage page;
    @Inject private Console console;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary(Ids.EE_GLOBAL_MODULES_ITEM);
        table = page.getGlobalModulesTable();
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, GLOBAL_MODULES_CREATE);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyListAttributeContainsValue(GLOBAL_MODULES, globalModule(GLOBAL_MODULES_CREATE));

    }

    @Test
    public void delete() throws Exception {
        table.remove(GLOBAL_MODULES_DELETE);

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyListAttributeDoesNotContainValue(GLOBAL_MODULES, globalModule(GLOBAL_MODULES_DELETE));
    }
}
