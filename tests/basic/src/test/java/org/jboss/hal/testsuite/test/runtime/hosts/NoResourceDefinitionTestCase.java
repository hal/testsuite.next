/*
 * Copyright 2015-2021 Red Hat, Inc, and individual contributors.
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
/**
 * @author Petr Adamec
 */
package org.jboss.hal.testsuite.test.runtime.hosts;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.DomainHcDc;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.runtime.HostsPage;
import org.jboss.hal.testsuite.test.deployment.Deployment;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.commands.deployments.Undeploy;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
@Category(DomainHcDc.class)
public class NoResourceDefinitionTestCase {
    private static final String TEMP_DEPLOYMENT =
            "temp-deployment-" + RandomStringUtils.randomAlphanumeric(7) + ".war";
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    @Page private HostsPage page;
    @Inject private Console console;
    @Drone protected WebDriver browser;


    private static final Operations operations = new Operations(client);


    @BeforeClass
    public static void deploy() throws CommandFailedException {
        Deployment deployment = new Deployment.Builder(TEMP_DEPLOYMENT)
                .textFile("index.html", "<h1>HAL to rule them all</h1>")
                .build();

        client.apply(deployment.deployEnabledCommand(HostsPage.MAIN_SERVER_GROUP));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException, CommandFailedException {
        client.apply(new Undeploy.Builder(TEMP_DEPLOYMENT).build());
    }

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    /**
     * Test for https://issues.redhat.com/browse/HAL-1750.
     * Test if error is displayed after click on deploy application in Runtime > Hosts > HC host > server-one > Web (Undertow) > Deployment
     * @throws IOException
     */
    @Test
    public void noResourceDefinition() throws IOException, InterruptedException {
        page.goToDeploymentOnHcServer();
        WebElement element = page.getUndertowRuntimeDeploymentColumn();
        element.findElement(By.id(TEMP_DEPLOYMENT.replace(".","").toLowerCase())).click();
        page.getHalFinderPreview();
        Assert.assertTrue("No resource definition ... For more information see HAL-1750", console.verifyNoError());

    }

}
