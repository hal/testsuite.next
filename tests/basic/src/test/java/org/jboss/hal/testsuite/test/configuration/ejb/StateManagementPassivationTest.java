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
package org.jboss.hal.testsuite.test.configuration.ejb;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.EJBConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.*;

@RunWith(Arquillian.class)
public class StateManagementPassivationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String MAX_SIZE = "max-size";

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(passivationAddress(PS_READ));
        operations.add(passivationAddress(PS_UPDATE));
        operations.add(passivationAddress(PS_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(passivationAddress(PS_CREATE));
        operations.removeIfExists(passivationAddress(PS_READ));
        operations.removeIfExists(passivationAddress(PS_UPDATE));
        operations.removeIfExists(passivationAddress(PS_DELETE));
    }

    @Page private EJBConfigurationPage page;
    @Inject private Console console;
    private FormFragment form;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("ejb-state-item", "ejb-passivation-item");

        table = page.getPassivationTable();
        form = page.getPassivationForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, PS_CREATE);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(passivationAddress(PS_CREATE), client).verifyExists();
    }

    @Test
    public void update() throws Exception {
        int val = 123;
        table.select(PS_UPDATE);
        form.edit();
        form.number(MAX_SIZE, val);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(passivationAddress(PS_UPDATE), client)
                .verifyAttribute(MAX_SIZE, val);
    }

    @Test
    public void updateInvalidMaxSize() throws Exception {
        int val = -1;
        table.select(PS_UPDATE);
        form.edit();
        form.number(MAX_SIZE, val);
        form.trySave();
        form.expectError(MAX_SIZE);
    }

    @Test
    public void delete() throws Exception {
        table.remove(PS_DELETE);

        console.verifySuccess();
        new ResourceVerifier(passivationAddress(PS_DELETE), client).verifyDoesNotExist();
    }
}
