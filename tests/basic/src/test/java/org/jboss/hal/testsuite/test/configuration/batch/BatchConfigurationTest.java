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
package org.jboss.hal.testsuite.test.configuration.batch;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.BatchPage;
import org.jboss.hal.testsuite.util.Notification;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

@RunWith(Arquillian.class)
public class BatchConfigurationTest {

    private static final String RESTART_JOBS_ON_RESUME = "restart-jobs-on-resume";
    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static BackupAndRestoreAttributes backup;

    @BeforeClass
    public static void beforeClass() throws CommandFailedException {
        backup = new BackupAndRestoreAttributes.Builder(BatchFixtures.SUBSYSTEM_ADDRESS).build();
        client.apply(backup.backup());
    }

    @AfterClass
    public static void afterClass() throws CommandFailedException {
        client.apply(backup.restore());
    }

    @Drone private WebDriver browser;
    @Page private BatchPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
        page.getConfigurationItem().click();

        form = page.getConfigurationForm();
    }

    @Test
    public void update() throws Exception {
        form.edit();
        form.bootstrapSwitch(RESTART_JOBS_ON_RESUME, false);
        form.save();
        Notification.withBrowser(browser).success();

        new ResourceVerifier(BatchFixtures.SUBSYSTEM_ADDRESS, client, 500)
                .verifyAttribute(RESTART_JOBS_ON_RESUME, false);
    }
}
