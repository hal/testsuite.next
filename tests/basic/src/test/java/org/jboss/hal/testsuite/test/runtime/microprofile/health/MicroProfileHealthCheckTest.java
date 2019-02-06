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
package org.jboss.hal.testsuite.test.runtime.microprofile.health;

import java.io.IOException;
import java.util.Arrays;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Standalone;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.preview.MicroProfileHealthPreviewFragment;
import org.jboss.hal.testsuite.test.deployment.Deployment;
import org.jboss.hal.testsuite.test.deployment.DeploymentOperations;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MICROPROFILE_HEALTH_SMALLRYE;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.runtimeSubsystemPath;
import static org.jboss.hal.testsuite.test.runtime.microprofile.health.CustomHealthCheck.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@Category(Standalone.class)
public class MicroProfileHealthCheckTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private final DeploymentOperations deploymentOps = new DeploymentOperations(client);
    private final Deployment
        upDeployment = new Deployment.Builder("up.war")
                .classFile(CustomHealthCheck.class)
                .textFile(PROPERTIES_FILENAME, PROPERTIES_KEY + "=" + UP)
                .build(),
        downDeployment = new Deployment.Builder("down.war")
                .classFile(CustomHealthCheck.class)
                .textFile(PROPERTIES_FILENAME, PROPERTIES_KEY + "=" + DOWN)
                .build();
    private FinderFragment previewFinder;

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }


    @Inject private Console console;

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }

    @Test
    public void checkUp() {
        assertTrue(getPreview().getAlert().isSuccess());
    }

    @Test
    public void customHealthCheck() throws Exception {
        try {
            doWithDeployment(downDeployment, () -> {
                MicroProfileHealthPreviewFragment preview = openPreview().getPreview();
                assertTrue(preview.getAlert().isError());
                assertEquals(DOWN, preview.getCheckState(CHECK_NAME));
                String shownData = preview.getCheckData(CHECK_NAME);
                assertTrue(shownData.startsWith(DOWN_DATA_1));
                assertTrue(shownData.endsWith(DOWN_DATA_2));
            });
            doWithDeployment(upDeployment, () -> {
                MicroProfileHealthPreviewFragment preview = getPreview();
                preview.refresh();
                assertTrue(preview.getAlert().isSuccess());
                assertEquals(UP, preview.getCheckState(CHECK_NAME));
                String shownData = preview.getCheckData(CHECK_NAME);
                assertTrue(shownData.startsWith(UP_DATA_1));
                assertTrue(shownData.endsWith(UP_DATA_2));
            });
        } finally {
            previewFinder = null;
        }
    }

    private MicroProfileHealthPreviewFragment getPreview() {
        if (previewFinder == null) {
            throw new IllegalStateException("Preview should be opened first.");
        }
        return previewFinder.preview(MicroProfileHealthPreviewFragment.class);
    }

    private MicroProfileHealthCheckTest openPreview() {
        try {
            previewFinder = console.finder(NameTokens.RUNTIME,
                    runtimeSubsystemPath(serverEnvironmentUtils.getServerHostName(), MICROPROFILE_HEALTH_SMALLRYE));
        } catch (IOException e) {
            throw new RuntimeException("Unable to get server host name", e);
        }
        ColumnFragment column = previewFinder.column(Ids.RUNTIME_SUBSYSTEM);
        column.selectItem(MICROPROFILE_HEALTH_SMALLRYE); // we only get a preview in this case if we select the item
        return this;
    }

    private void doWithDeployment(Deployment deployment, Runnable action) throws Exception {
        try {
            client.apply(deployment.deployEnabledCommand());
            action.run();
        } finally {
            deploymentOps.removeDeploymentsIfExist(Arrays.asList(deployment.getName()));
        }
    }

}
