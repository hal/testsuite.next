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
package org.jboss.hal.testsuite.test.runtime.messaging;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MESSAGING_ACTIVEMQ;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;

class MessagingFixtures {

    private static final String SERVER_PREFIX = "srv";

    static final String BINDINGS_DIRECTORY = "bindings-directory";
    static final String JOURNAL_DIRECTORY = "journal-directory";
    static final String LARGE_MESSAGES_DIRECTORY = "large-messages-directory";
    static final String PAGING_DIRECTORY = "paging-directory";

    static final Address SUBSYSTEM_ADDRESS = Address.subsystem(MESSAGING_ACTIVEMQ);
    static final String SRV_READ = Ids.build(SERVER_PREFIX, CrudConstants.READ, Random.name());
    static final String SRV_RESET = Ids.build(SERVER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SRV_FAILOVER = Ids.build(SERVER_PREFIX, CrudConstants.UPDATE, Random.name());

    static Address serverAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVER, name);
    }

    static Address serverPathAddress(String server, String path) {
        return serverAddress(server).and(PATH, path);
    }

    private MessagingFixtures() {
    }
}
