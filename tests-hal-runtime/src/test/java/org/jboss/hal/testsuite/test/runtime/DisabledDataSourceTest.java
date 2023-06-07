/*
 * Copyright 2015-2023 Red Hat, Inc, and individual contributors.
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
package org.jboss.hal.testsuite.test.runtime;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Standalone;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.runtime.DataSourceRuntimePage;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.foundation.online.SnapshotBackup;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.CliException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;



/**
 * @author <a href="mailto:padamec@redhat.com">Petr Adamec</a>
 */
@RunWith(Arquillian.class)
@Category(Standalone.class)
public class DisabledDataSourceTest {
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    protected static final Administration administration = new Administration(client);

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Page
    private DataSourceRuntimePage page;

    private SnapshotBackup snapshot = new SnapshotBackup();

    @Before
    public void before() throws CommandFailedException, IOException, CliException, InterruptedException, TimeoutException {
        client.apply(snapshot.backup());
        client.executeCli("/subsystem=datasources/data-source=ExampleDS:disable");
        administration.reloadIfRequired();
        page.navigate();
        page.getDatasource().click();
    }

    @After
    public void restoreBackup() throws IOException, InterruptedException, TimeoutException, CommandFailedException {
        client.apply(snapshot.restore());
        administration.reloadIfRequired();

    }

    /**
     * Test if error is thrown when datasource is disabled and click on Runtime > Server > Datasources.
     * For more information look at HAL-1848
     * @throws IOException
     */
    @Test
    public void testDisabledDatasourceThrowsError() throws IOException {
        page.getDataSourceRuntime();
        Assert.assertTrue("Internal error is displayed! See https://issues.redhat.com/browse/HAL-1848",
                console.verifyNoError());
    }
}
