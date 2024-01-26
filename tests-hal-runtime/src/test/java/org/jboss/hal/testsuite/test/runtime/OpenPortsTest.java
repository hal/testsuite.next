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
package org.jboss.hal.testsuite.test.runtime;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.google.common.primitives.Ints;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Standalone;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.ServerPreviewFragment;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static java.util.stream.Collectors.toList;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
@Category(Standalone.class)
public class OpenPortsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private static final Operations operations = new Operations(client);

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }


    @Inject
    private Console console;
    private ServerPreviewFragment preview;

    @Before
    public void setUp() throws Exception {
        FinderFragment finder = console.finder(NameTokens.RUNTIME);
        finder.column(Ids.STANDALONE_SERVER_COLUMN)
                .selectItem(Ids.hostServer(Ids.STANDALONE_HOST, serverEnvironmentUtils.getServerHostName()));
        waitGui().until().element(By.id("open-ports")).is().present();
        preview = finder.preview(ServerPreviewFragment.class);
    }

    @Test
    public void openPorts() throws IOException {
        ModelNodeResult result = operations.readChildrenNames(Address.root(), SOCKET_BINDING_GROUP);
        result.assertSuccess();
        List<String> socketBindingGroups = result.stringListValue(Collections.emptyList());
        assertFalse(socketBindingGroups.isEmpty());

        Address address = Address.of(SOCKET_BINDING_GROUP, socketBindingGroups.get(0)).and(SOCKET_BINDING, "*");
        ModelNode select = new ModelNode().add(NAME).add(BOUND_PORT);
        ModelNode where = new ModelNode().set("bound", true);
        result = operations.invoke(QUERY, address, Values.of(SELECT, select).and(WHERE, where));
        result.assertSuccess();

        // The names of the ports will be different in the result and the UI
        // So we just make sure the ports themselves are the same
        int[] expectedOpenPorts = Ints.toArray(result.value().asList().stream()
                .map(modelNode -> modelNode.get(RESULT).get(BOUND_PORT).asInt())
                .sorted()
                .collect(toList()));
        int[] actualOpenPorts = Ints.toArray(preview.getOpenPorts().values().stream().sorted().collect(toList()));
        assertArrayEquals(expectedOpenPorts, actualOpenPorts);
    }
}
