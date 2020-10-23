/*
 * Copyright 2015-2018 Red Hat, Inc, and individual contributors.
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
package org.jboss.hal.testsuite.test;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.HeaderFragment;
import org.jboss.hal.testsuite.page.HomePage;
import org.jboss.hal.testsuite.test.deployment.Deployment;
import org.jboss.hal.testsuite.test.deployment.DeploymentOperations;
import org.jboss.hal.testsuite.test.runtime.management.operations.LongRunningServlet;
import org.jboss.hal.testsuite.test.runtime.management.operations.ManagementOperations;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.jboss.hal.testsuite.test.runtime.management.operations.LongRunningServlet.DURATION_IN_SECONDS;
import static org.jboss.hal.testsuite.test.runtime.management.operations.LongRunningServlet.DURATION_PROPERTIES_PATH;
import static org.junit.Assert.assertTrue;

@Ignore
@RunWith(Arquillian.class)
public class NonProgressingNotificationTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ManagementOperations managementOps = new ManagementOperations(client);
    private static final DeploymentOperations deploymentOps = new DeploymentOperations(client);
    private static final List<String> deploymentsToBeCleanedUp = new ArrayList<>();

    @Page private HomePage page;
    @Inject private Console console;

    @AfterClass
    public static void endTests() throws Exception {
        client.close();
        deploymentOps.removeDeploymentsIfExist(deploymentsToBeCleanedUp);
        deploymentsToBeCleanedUp.clear();
    }

    @Test
    public void displayNonProgressingOperation() throws Exception {
        page.navigate();
        HeaderFragment header = console.header();

        // must be at least 26s, that is the 15s of the hard coded exclusive lock of wildfly and 10s of the
        // poll time of HAL, plus some seconds for an additional time
        Deployment deployment = createSlowDeployment(30);
        CompletableFuture<Void> deployFuture = deploymentOps.deployAsync(deployment);
        managementOps.waitForNonProgressingOperation(32);

        // wait 10s, this is the default poll time of HAL
        // must be equal to the Settings.DEFAULT_POLL_TIME constant of hal-console/config project.
        int timeout = 10;
        for (int i = 0; i < timeout; i++) {
            TimeUnit.SECONDS.sleep(1);
            if (header.getNonProgressingOp().isDisplayed()) {
                break;
            }
        }
        assertTrue(header.getNonProgressingOp().isDisplayed());
        deployFuture.join();
    }

    private Deployment createSlowDeployment(Integer deploymentDurationInSeconds) {
        String propertiesContent = DURATION_IN_SECONDS + "=" + deploymentDurationInSeconds;
        Deployment deployment = new Deployment.Builder(Random.name() + ".war")
                .classFile(LongRunningServlet.class)
                .textFile(DURATION_PROPERTIES_PATH, propertiesContent)
                .build();

        deploymentsToBeCleanedUp.add(deployment.getName());
        return deployment;
    }
}
