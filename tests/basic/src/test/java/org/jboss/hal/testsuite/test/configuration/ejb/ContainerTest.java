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

import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.DEFAULT_DISTINCT_NAME;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.DEFAULT_SINGLETON_BEAN_ACCESS_TIMEOUT;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.SUBSYSTEM_ADDRESS;

@RunWith(Arquillian.class)
public class ContainerTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @AfterClass
    public static void tearDown() throws Exception {
        operations.undefineAttribute(EJBFixtures.SUBSYSTEM_ADDRESS, DEFAULT_DISTINCT_NAME);
        operations.writeAttribute(EJBFixtures.SUBSYSTEM_ADDRESS, DEFAULT_SINGLETON_BEAN_ACCESS_TIMEOUT, 5000);
    }


    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private EJBConfigurationPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("ejb3-container-item", "ejb3-configuration-item");
        form = page.getConfigurationForm();
    }

    @Test
    public void testInvalidDefaultSingletonBeanAccessTimeout() {
        console.waitNoNotification();
        crud.updateWithError(form, DEFAULT_SINGLETON_BEAN_ACCESS_TIMEOUT, 0);
    }

    @Test
    public void updateDefaultDistinctName() throws Exception {
        crud.update(SUBSYSTEM_ADDRESS, form, DEFAULT_DISTINCT_NAME);
    }

    @Test
    public void updateDefaultSingletonBeanAccessTimeout() throws Exception {
        crud.update(SUBSYSTEM_ADDRESS, form, DEFAULT_SINGLETON_BEAN_ACCESS_TIMEOUT, 6123L);
    }
}
