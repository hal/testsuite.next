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
package org.jboss.hal.testsuite.test.configuration.distributableweb;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.DistributableWebPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CACHE_CONTAINER;
import static org.jboss.hal.testsuite.test.configuration.distributableweb.DistributableWebFixtures.*;
import static org.jboss.hal.testsuite.test.configuration.distributableweb.DistributableWebOperations.addCacheContainer;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.CC_READ;
import static org.jboss.hal.testsuite.test.configuration.infinispan.InfinispanFixtures.cacheContainerAddress;

@RunWith(Arquillian.class)
public class DistributableWebConfigurationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static String defaultSession;
    private static String defaultSSO;

    @BeforeClass
    public static void beforeClass() throws Exception {
        defaultSession = operations.readAttribute(SUBSYSTEM_ADDRESS, DEFAULT_SESSION_MANAGEMENT).stringValue();
        defaultSSO = operations.readAttribute(SUBSYSTEM_ADDRESS, DEFAULT_SSO_MANAGEMENT).stringValue();
        addCacheContainer(client, operations, CC_READ);
        operations.add(infinispanSessionAddress(INFINISPAN_SESSION_REF), Values.of(CACHE_CONTAINER, CC_READ)
                .and(GRANULARITY, SESSION));
        operations.add(infinispanSSOAddress(INFINISPAN_SSO_REF), Values.of(CACHE_CONTAINER, CC_READ));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.writeAttribute(SUBSYSTEM_ADDRESS, DEFAULT_SESSION_MANAGEMENT, defaultSession);
        operations.writeAttribute(SUBSYSTEM_ADDRESS, DEFAULT_SSO_MANAGEMENT, defaultSSO);
        operations.removeIfExists(infinispanSSOAddress(INFINISPAN_SSO_REF));
        operations.removeIfExists(infinispanSessionAddress(INFINISPAN_SESSION_REF));
        operations.removeIfExists(cacheContainerAddress(CC_READ));
    }

    @Page private DistributableWebPage page;
    @Inject private CrudOperations crud;
    @Inject private Console console;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        console.verticalNavigation().selectPrimary("dw-configuration-item");
        form = page.getConfigurationForm();
    }

    @Test
    public void update() throws Exception {
        crud.update(SUBSYSTEM_ADDRESS, form,
                f -> {
                    f.text(DEFAULT_SESSION_MANAGEMENT, INFINISPAN_SESSION_REF);
                    f.text(DEFAULT_SSO_MANAGEMENT, INFINISPAN_SSO_REF);
                },
                verifier -> {
                    verifier.verifyAttribute(DEFAULT_SESSION_MANAGEMENT, INFINISPAN_SESSION_REF);
                    verifier.verifyAttribute(DEFAULT_SSO_MANAGEMENT, INFINISPAN_SSO_REF);
                });
    }
}
