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
package org.jboss.hal.testsuite.test.configuration.io;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.IO;

public interface IOFixtures {

    Address SUBSYSTEM_ADDRESS = Address.subsystem(IO);

    // ------------------------------------------------------ worker

    String WORKER_CREATE = Ids.build("worker", "create", Random.name());
    String WORKER_READ = Ids.build("worker", "read", Random.name());
    String WORKER_UPDATE = Ids.build("worker", "update", Random.name());
    String WORKER_DELETE = Ids.build("worker", "delete", Random.name());

    static Address workerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("worker", name);
    }


    // ------------------------------------------------------ buffer pool

    String BUFFER_SIZE = "buffer-size";
    String BUFFERS_PER_SLICE = "buffers-per-slice";
    String DIRECT_BUFFERS = "direct-buffers";

    String BP_CREATE = Ids.build("bp", "create", Random.name());
    String BP_READ = Ids.build("bp", "read", Random.name());
    String BP_UPDATE = Ids.build("bp", "update", Random.name());
    String BP_DELETE = Ids.build("bp", "delete", Random.name());

    static Address bufferPool(String name) {
        return SUBSYSTEM_ADDRESS.and("buffer-pool", name);
    }
}
