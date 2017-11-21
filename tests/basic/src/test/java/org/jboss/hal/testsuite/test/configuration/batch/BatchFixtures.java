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

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.BATCH_JBERET;
import static org.jboss.hal.dmr.ModelDescriptionConstants.THREAD_FACTORY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.THREAD_POOL;

public interface BatchFixtures {

    String GROUP_NAME = "group-name";
    String THREAD_NAME_PATTERN = "thread-name-pattern";
    Address SUBSYSTEM_ADDRESS = Address.subsystem(BATCH_JBERET);

    // ------------------------------------------------------ in memory repository

    String IN_MEMORY_CREATE = Ids.build("im", "create", Random.name());
    String IN_MEMORY_DELETE = Ids.build("im", "delete", Random.name());

    static Address inMemoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("in-memory-job-repository", name);
    }


    // ------------------------------------------------------ jdbc repository

    String DATA_SOURCE = Ids.build("ds", Random.name());
    String JDBC_CREATE = Ids.build("jdbc", "create", Random.name());
    String JDBC_READ = Ids.build("jdbc", "read", Random.name());
    String JDBC_DELETE = Ids.build("jdbc", "remove", Random.name());

    static Address jdbcAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("jdbc-job-repository", name);
    }


    // ------------------------------------------------------ thread factory

    String THREAD_FACTORY_CREATE = Ids.build("tf", "create", Random.name());
    String THREAD_FACTORY_READ = Ids.build("tf", "read", Random.name());
    String THREAD_FACTORY_UPDATE = Ids.build("tf", "update", Random.name());
    String THREAD_FACTORY_DELETE = Ids.build("tf", "delete", Random.name());

    static Address threadFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(THREAD_FACTORY, name);
    }


    // ------------------------------------------------------ thread pool

    int MAX_THREADS_VALUE = 11;
    String THREAD_POOL_CREATE = Ids.build("tp", "create", Random.name());
    String THREAD_POOL_READ = Ids.build("tp", "read", Random.name());
    String THREAD_POOL_UPDATE = Ids.build("tp", "update", Random.name());
    String THREAD_POOL_DELETE = Ids.build("tp", "delete", Random.name());

    static Address threadPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(THREAD_POOL, name);
    }
}
