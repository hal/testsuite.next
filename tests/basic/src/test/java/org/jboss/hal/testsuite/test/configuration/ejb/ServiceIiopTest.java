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
import org.jboss.hal.testsuite.page.configuration.EJBConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.SERVICE_IIOP_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.USE_QUALIFIED_NAME;

@RunWith(Arquillian.class)
public class ServiceIiopTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @AfterClass
    public static void tearDown() throws Exception {
        operations.writeAttribute(EJBFixtures.SERVICE_IIOP_ADDRESS, USE_QUALIFIED_NAME, false);
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EJBConfigurationPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("ejb3-service-item", "ejb3-service-iiop-item");
        form = page.getServiceIiopForm();
    }

    @Test
    public void update() throws Exception {
        console.waitNoNotification();
        crud.update(SERVICE_IIOP_ADDRESS, form, USE_QUALIFIED_NAME, true);
    }
}
