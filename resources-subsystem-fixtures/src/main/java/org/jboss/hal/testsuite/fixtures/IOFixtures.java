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
package org.jboss.hal.testsuite.fixtures;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.IO;

public final class IOFixtures {

    private static final String WORKER_PREFIX = "worker";
    private static final String BUFFER_POOL_PREFIX = "bp";

    public static final String DEFAULT_IO_WORKER = "default";

    public static final Address SUBSYSTEM_ADDRESS = Address.subsystem(IO);

    // ------------------------------------------------------ worker

    public static final String WORKER_CREATE = Ids.build(WORKER_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String WORKER_READ = Ids.build(WORKER_PREFIX, CrudConstants.READ, Random.name());
    public static final String WORKER_UPDATE = Ids.build(WORKER_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String WORKER_DELETE = Ids.build(WORKER_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address workerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(WORKER_PREFIX, name);
    }


    // ------------------------------------------------------ buffer pool

    public static final String BUFFER_SIZE = "buffer-size";
    public static final String BUFFERS_PER_SLICE = "buffers-per-slice";
    public static final String DIRECT_BUFFERS = "direct-buffers";

    public static final String BP_CREATE = Ids.build(BUFFER_POOL_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String BP_READ = Ids.build(BUFFER_POOL_PREFIX, CrudConstants.READ, Random.name());
    public static final String BP_UPDATE = Ids.build(BUFFER_POOL_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String BP_DELETE = Ids.build(BUFFER_POOL_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address bufferPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("buffer-pool", name);
    }

    private IOFixtures() {
    }
}
