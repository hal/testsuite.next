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
package org.jboss.hal.testsuite.test.configuration.jmx;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.JmxPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ENABLED;
import static org.jboss.hal.testsuite.test.configuration.jmx.JmxFixtures.*;

@RunWith(Arquillian.class)
public class JmxTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void beforeClass() throws CommandFailedException {
        backup = new BackupAndRestoreAttributes.Builder(SUBSYSTEM_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void afterClass() throws CommandFailedException {
        client.apply(backup.restore());
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private JmxPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    // ------------------------------------------------------ configuration

    @Test
    public void updateConfiguration() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.JMX_CONFIGURATION_ITEM);
        form = page.getConfigurationForm();
        crud.update(SUBSYSTEM_ADDRESS, form, NON_CORE_MBEAN_SENSITIVITY, true);
    }

    @Test
    public void resetConfiguration() throws Exception {
        console.verticalNavigation().selectPrimary(Ids.JMX_CONFIGURATION_ITEM);
        form = page.getConfigurationForm();
        crud.reset(SUBSYSTEM_ADDRESS, form);
    }

    // ------------------------------------------------------ audit log

    @Test
    public void createAuditLog() throws Exception {
        if (operations.removeIfExists(AUDIT_LOG_ADDRESS)) {
            console.reload();
        }

        console.verticalNavigation().selectPrimary(Ids.JMX_AUDIT_LOG_ITEM);
        form = page.getAuditForm();
        crud.createSingleton(AUDIT_LOG_ADDRESS, form);
    }

    @Test
    public void updateAuditLog() throws Exception {
        if (!operations.exists(AUDIT_LOG_ADDRESS)) {
            operations.add(AUDIT_LOG_ADDRESS);
            console.reload();
        }
        console.verticalNavigation().selectPrimary(Ids.JMX_AUDIT_LOG_ITEM);
        form = page.getAuditForm();
        crud.update(AUDIT_LOG_ADDRESS, form, ENABLED, false);
    }

    @Test
    public void resetAuditLog() throws Exception {
        if (!operations.exists(AUDIT_LOG_ADDRESS)) {
            operations.add(AUDIT_LOG_ADDRESS);
            console.reload();
        }
        console.verticalNavigation().selectPrimary(Ids.JMX_AUDIT_LOG_ITEM);
        form = page.getAuditForm();
        crud.reset(AUDIT_LOG_ADDRESS, form);
    }

    @Test
    public void deleteAuditLog() throws Exception {
        if (!operations.exists(AUDIT_LOG_ADDRESS)) {
            operations.add(AUDIT_LOG_ADDRESS);
            console.reload();
        }
        console.verticalNavigation().selectPrimary(Ids.JMX_AUDIT_LOG_ITEM);
        form = page.getAuditForm();
        crud.deleteSingleton(AUDIT_LOG_ADDRESS, form);
    }

    // ------------------------------------------------------ remoting connector

    @Test
    public void createRemotingConnector() throws Exception {
        if (operations.removeIfExists(REMOTING_CONNECTOR_ADDRESS)) {
            console.reload();
        }

        console.verticalNavigation().selectPrimary(Ids.JMX_REMOTING_CONNECTOR_ITEM);
        form = page.getRemotingConnectorForm();
        crud.createSingleton(REMOTING_CONNECTOR_ADDRESS, form);
    }

    @Test
    public void updateRemotingConnector() throws Exception {
        if (!operations.exists(REMOTING_CONNECTOR_ADDRESS)) {
            operations.add(REMOTING_CONNECTOR_ADDRESS);
            console.reload();
        }
        console.verticalNavigation().selectPrimary(Ids.JMX_REMOTING_CONNECTOR_ITEM);
        form = page.getRemotingConnectorForm();
        crud.update(REMOTING_CONNECTOR_ADDRESS, form, USE_MANAGEMENT_ENDPOINT, false);
    }

    @Test
    public void resetRemotingConnector() throws Exception {
        if (!operations.exists(REMOTING_CONNECTOR_ADDRESS)) {
            operations.add(REMOTING_CONNECTOR_ADDRESS);
            console.reload();
        }
        console.verticalNavigation().selectPrimary(Ids.JMX_REMOTING_CONNECTOR_ITEM);
        form = page.getRemotingConnectorForm();
        crud.reset(REMOTING_CONNECTOR_ADDRESS, form);
    }

    @Test
    public void deleteRemotingConnector() throws Exception {
        if (!operations.exists(REMOTING_CONNECTOR_ADDRESS)) {
            operations.add(REMOTING_CONNECTOR_ADDRESS);
            console.reload();
        }
        console.verticalNavigation().selectPrimary(Ids.JMX_REMOTING_CONNECTOR_ITEM);
        form = page.getRemotingConnectorForm();
        crud.deleteSingleton(REMOTING_CONNECTOR_ADDRESS, form);
    }
}
