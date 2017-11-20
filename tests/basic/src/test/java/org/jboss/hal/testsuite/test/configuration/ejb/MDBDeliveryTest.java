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

import static org.jboss.hal.dmr.ModelDescriptionConstants.ACTIVE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.*;

@RunWith(Arquillian.class)
public class MDBDeliveryTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(mdbDeliveryAddress(MDB_READ));
        operations.add(mdbDeliveryAddress(MDB_UPDATE));
        operations.add(mdbDeliveryAddress(MDB_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(mdbDeliveryAddress(MDB_CREATE));
        operations.removeIfExists(mdbDeliveryAddress(MDB_READ));
        operations.removeIfExists(mdbDeliveryAddress(MDB_UPDATE));
        operations.removeIfExists(mdbDeliveryAddress(MDB_DELETE));
    }

    @Page private EJBConfigurationPage page;
    @Inject private Console console;
    private FormFragment form;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("ejb-mdb-delivery-group-item");

        table = page.getMdbDeliveryTable();
        form = page.getMdbDeliveryForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, MDB_CREATE);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(mdbDeliveryAddress(MDB_CREATE), client).verifyExists();
    }

    @Test
    public void update() throws Exception {
        table.select(MDB_UPDATE);
        form.edit();
        form.flip(ACTIVE, false);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(mdbDeliveryAddress(MDB_UPDATE), client)
                .verifyAttribute(ACTIVE, false);
    }

    @Test
    public void delete() throws Exception {
        table.remove(MDB_DELETE);

        console.verifySuccess();
        new ResourceVerifier(mdbDeliveryAddress(MDB_DELETE), client).verifyDoesNotExist();
    }
}
