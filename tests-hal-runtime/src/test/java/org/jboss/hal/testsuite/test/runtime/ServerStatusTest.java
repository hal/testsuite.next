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

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.ServerStatusPreviewFragment;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.RESULT;
import static org.junit.Assert.assertTrue;
import static org.wildfly.extras.creaper.core.online.operations.ReadResourceOption.ATTRIBUTES_ONLY;
import static org.wildfly.extras.creaper.core.online.operations.ReadResourceOption.INCLUDE_RUNTIME;

@RunWith(Arquillian.class)
public class ServerStatusTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private static final Operations operations = new Operations(client);

    @AfterClass
    public static void cleanUp() throws IOException {
        client.close();
    }

    @Inject
    private Console console;
    private ServerStatusPreviewFragment preview;
    private long heapUsed;
    private long heapCommitted;
    private long nonHeapUsed;
    private long nonHeapCommitted;
    private long daemons;

    @Before
    public void setUp() throws Exception {
        Address baseAddress = Address.root().and("core-service", "platform-mbean");
        ModelNode modelNode = operations.batch(new Batch()
                .readResource(baseAddress.and("type", "memory"), ATTRIBUTES_ONLY, INCLUDE_RUNTIME)
                .readResource(baseAddress.and("type", "threading"), ATTRIBUTES_ONLY, INCLUDE_RUNTIME));
        ModelNode heap = modelNode.get(RESULT).get("step-1").get(RESULT).get("heap-memory-usage");
        heapUsed = heap.get("used").asLong() / 1024 / 1024;
        heapCommitted = heap.get("committed").asLong() / 1024 / 1024;
        ModelNode nonHeap = modelNode.get(RESULT).get("step-1").get(RESULT).get("non-heap-memory-usage");
        nonHeapUsed = nonHeap.get("used").asLong() / 1024 / 1024;
        nonHeapCommitted = nonHeap.get("committed").asLong() / 1024 / 1024;
        ModelNode threads = modelNode.get(RESULT).get("step-2").get(RESULT);
        daemons = threads.get("daemon-thread-count").asLong();

        preview = console.finder(NameTokens.RUNTIME, new FinderPath()
                .append(Ids.STANDALONE_SERVER_COLUMN,
                        Ids.hostServer(Ids.STANDALONE_HOST, serverEnvironmentUtils.getServerHostName()))
                .append(Ids.RUNTIME_SUBSYSTEM, Ids.SERVER_RUNTIME_STATUS))
                .preview(ServerStatusPreviewFragment.class);
    }

    @Test
    public void memory() {
        assertUsage(preview.getHeapUsed(), "heap used", heapUsed, 10);
        assertUsage(preview.getHeapCommitted(), "heap committed", heapCommitted, 10);
        assertUsage(preview.getNonHeapUsed(), "non-heap used", nonHeapUsed, 10);
        assertUsage(preview.getNonHeapCommitted(), "non-heap committed", nonHeapCommitted, 10);
        assertUsage(preview.getThreads(), "threads", daemons, 5);
    }

    private void assertUsage(WebElement element, String message, long expected, long delta) {
        long actual = Long.parseLong(element.getAttribute("aria-valuenow"));
        assertTrue(message, Math.abs(expected - actual) <= delta);
    }
}
