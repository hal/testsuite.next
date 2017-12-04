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
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.ModclusterPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_ALIAS;
import static org.jboss.hal.testsuite.test.configuration.modcluster.ModclusterFixtures.SSL_ADDRESS;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class ModclusterSslTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.removeIfExists(SSL_ADDRESS);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        operations.removeIfExists(SSL_ADDRESS);
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private ModclusterPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("modcluster-ssl-item");
        form = page.getSslForm();
    }

    @Test
    public void create() throws Exception {
        crud.createSingleton(SSL_ADDRESS, form);
    }

    @Test
    public void update() throws Exception {
        crud.update(SSL_ADDRESS, form, KEY_ALIAS);
    }

    @Test
    public void reset() throws Exception {
        crud.reset(SSL_ADDRESS, form);
    }

    @Test
    public void zzzDelete() throws Exception {
        crud.deleteSingleton(SSL_ADDRESS, form);
    }
}
