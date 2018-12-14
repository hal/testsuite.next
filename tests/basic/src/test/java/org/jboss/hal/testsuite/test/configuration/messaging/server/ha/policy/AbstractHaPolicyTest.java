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
package org.jboss.hal.testsuite.test.configuration.messaging.server.ha.policy;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.WizardFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.page.configuration.MessagingServerHaPolicyPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.*;

public abstract class AbstractHaPolicyTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeTests() throws Exception {
        Batch batchSrvUpd = new Batch();
        batchSrvUpd.add(serverAddress(SRV_UPDATE));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, BINDINGS_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, JOURNAL_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, LARGE_MESSAGES_DIRECTORY), Values.of(PATH, Random.name()));
        batchSrvUpd.add(serverPathAddress(SRV_UPDATE, PAGING_DIRECTORY), Values.of(PATH, Random.name()));
        operations.batch(batchSrvUpd).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            operations.removeIfExists(serverAddress(SRV_UPDATE));
        } finally {
            client.close();
        }
    }

    @Page protected MessagingServerHaPolicyPage page;
    @Inject protected Console console;
    @Inject protected CrudOperations crudOperations;
    @Drone protected WebDriver browser;
    protected ColumnFragment column;
    protected WizardFragment wizard;

    enum HAPolicy {
        LIVE_ONLY(MESSAGING_HA_REPLICATION, MESSAGING_HA_REPLICATION_LIVE_ONLY,
            haPolicyAddress(SRV_UPDATE, ModelDescriptionConstants.LIVE_ONLY)),
        REPLICATION_MASTER(MESSAGING_HA_REPLICATION, MESSAGING_HA_REPLICATION_MASTER,
            haPolicyAddress(SRV_UPDATE, ModelDescriptionConstants.REPLICATION_MASTER)),
        REPLICATION_SLAVE(MESSAGING_HA_REPLICATION, MESSAGING_HA_REPLICATION_SLAVE,
            haPolicyAddress(SRV_UPDATE, ModelDescriptionConstants.REPLICATION_SLAVE)),
        REPLICATION_COLOCATED(MESSAGING_HA_REPLICATION, MESSAGING_HA_REPLICATION_COLOCATED,
            haPolicyAddress(SRV_UPDATE, ModelDescriptionConstants.REPLICATION_COLOCATED)),
        SHARED_STORE_MASTER(MESSAGING_HA_SHARED_STORE, MESSAGING_HA_SHARED_STORE_MASTER,
            haPolicyAddress(SRV_UPDATE, ModelDescriptionConstants.SHARED_STORE_MASTER)),
        SHARED_STORE_SLAVE(MESSAGING_HA_SHARED_STORE, MESSAGING_HA_SHARED_STORE_SLAVE,
            haPolicyAddress(SRV_UPDATE, ModelDescriptionConstants.SHARED_STORE_SLAVE)),
        SHARED_STORE_COLOCATED(MESSAGING_HA_SHARED_STORE, MESSAGING_HA_SHARED_STORE_COLOCATED,
            haPolicyAddress(SRV_UPDATE, ModelDescriptionConstants.SHARED_STORE_COLOCATED));
        final String basicStrategy;
        final String serverRole;
        final Address haPolicyAddress;

        HAPolicy(String basicStrategy, String serverRole, Address haPolicyAddress) {
            this.basicStrategy = basicStrategy;
            this.serverRole = serverRole;
            this.haPolicyAddress = haPolicyAddress;
        }

        public void create(FinderTest.HAPolicyConsumer consumer) throws Exception {
            consumer.accept(this);
        }

        public void remove(FinderTest.HAPolicyConsumer consumer) throws Exception {
            consumer.accept(this);
        }
    }

    @FunctionalInterface
    interface HAPolicyConsumer {
        void accept(HAPolicy policy) throws Exception;
    }
}
