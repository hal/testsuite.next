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
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.EJBConfigurationPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.TP_CREATE;
import static org.jboss.hal.testsuite.test.configuration.ejb.EJBFixtures.threadPoolAddress;

@RunWith(Arquillian.class)
public class ServiceAsyncTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String THREAD_POOL_NAME = "thread-pool-name";

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(threadPoolAddress(TP_CREATE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.writeAttribute(EJBFixtures.SERVICE_ASYNC_ADDRESS, THREAD_POOL_NAME, DEFAULT);
        operations.removeIfExists(threadPoolAddress(TP_CREATE));
    }

    @Page private EJBConfigurationPage page;
    @Inject private Console console;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectSecondary("ejb-service-item", "ejb-service-async-item");

        form = page.getServiceAsyncForm();
    }

    @Test
    public void update() throws Exception {
        form.edit();
        form.text(THREAD_POOL_NAME, TP_CREATE);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(EJBFixtures.SERVICE_ASYNC_ADDRESS, client)
                .verifyAttribute(THREAD_POOL_NAME, TP_CREATE);
    }

}
