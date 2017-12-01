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
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
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
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MAX_THREADS;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.*;

@RunWith(Arquillian.class)
public class ContainerRemotingProfileTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(remotingProfileAddress(RP_READ), Values.of(MAX_THREADS, 22));
        operations.add(remotingProfileAddress(RP_UPDATE), Values.of(MAX_THREADS, 33));
        operations.add(remotingProfileAddress(RP_DELETE), Values.of(MAX_THREADS, 44));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(remotingProfileAddress(RP_CREATE));
        operations.removeIfExists(remotingProfileAddress(RP_READ));
        operations.removeIfExists(remotingProfileAddress(RP_UPDATE));
        operations.removeIfExists(remotingProfileAddress(RP_DELETE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EJBConfigurationPage page;
    private FormFragment form;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("ejb3-container-item", "ejb3-remoting-profile-item");

        table = page.getRemotingProfileTable();
        form = page.getRemotingProfileForm();
        table.bind(form);
    }

    @Test
    public void create() throws Exception {
        crud.create(remotingProfileAddress(RP_CREATE), table, RP_CREATE);
    }

    @Test
    public void update() throws Exception {
        table.select(RP_UPDATE);
        crud.update(remotingProfileAddress(RP_UPDATE), form, LOCAL_RECEIVER_PASS_BY_VALUE, true);
    }

    @Test
    public void delete() throws Exception {
        crud.delete(remotingProfileAddress(RP_DELETE), table, RP_DELETE);
    }
}
