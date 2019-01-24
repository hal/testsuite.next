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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Standalone;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MICROPROFILE_HEALTH_SMALLRYE;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.runtimeSubsystemPath;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
@Category(Standalone.class)
public class MicroProfileHealthCheckTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);

    @Inject private Console console;

    @Test
    public void checkUp() throws IOException {
        FinderFragment finder = console.finder(NameTokens.RUNTIME,
                runtimeSubsystemPath(serverEnvironmentUtils.getServerHostName(), MICROPROFILE_HEALTH_SMALLRYE));
        ColumnFragment column = finder.column(Ids.RUNTIME_SUBSYSTEM);
        column.selectItem(MICROPROFILE_HEALTH_SMALLRYE); // we only get a preview in this case if we select the item
        assertTrue(finder.preview().getAlert().isSuccess());
    }
}
