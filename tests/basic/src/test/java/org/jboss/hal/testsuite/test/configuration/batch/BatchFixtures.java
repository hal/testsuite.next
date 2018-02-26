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
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.BATCH_JBERET;
import static org.jboss.hal.dmr.ModelDescriptionConstants.THREAD_FACTORY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.THREAD_POOL;

public final class BatchFixtures {

    private static final String IN_MEMORY_PREFIX = "im";
    private static final String JDBC_PREFIX = "jdbc";
    private static final String THREAD_FACTORY_PREFIX = "tf";
    private static final String THREAD_POOL_PREFIX = "tf";

    static String GROUP_NAME = "group-name";
    static String THREAD_NAME_PATTERN = "thread-name-pattern";
    static Address SUBSYSTEM_ADDRESS = Address.subsystem(BATCH_JBERET);

    // ------------------------------------------------------ in memory repository

    static String IN_MEMORY_CREATE = Ids.build(IN_MEMORY_PREFIX, CrudConstants.CREATE, Random.name());
    static String IN_MEMORY_DELETE = Ids.build(IN_MEMORY_PREFIX, CrudConstants.DELETE, Random.name());

    static Address inMemoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("in-memory-job-repository", name);
    }


    // ------------------------------------------------------ jdbc repository

    static String DATA_SOURCE = Ids.build("ds", Random.name());
    static String JDBC_CREATE = Ids.build(JDBC_PREFIX, CrudConstants.CREATE, Random.name());
    static String JDBC_READ = Ids.build(JDBC_PREFIX, CrudConstants.READ, Random.name());
    static String JDBC_DELETE = Ids.build(JDBC_PREFIX, "remove", Random.name());

    static Address jdbcAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("jdbc-job-repository", name);
    }


    // ------------------------------------------------------ thread factory

    static String THREAD_FACTORY_CREATE = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.CREATE, Random.name());
    static String THREAD_FACTORY_READ = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.READ, Random.name());
    static String THREAD_FACTORY_UPDATE = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.UPDATE, Random.name());
    static String THREAD_FACTORY_DELETE = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.DELETE, Random.name());

    static Address threadFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(THREAD_FACTORY, name);
    }


    // ------------------------------------------------------ thread pool

    static int MAX_THREADS_VALUE = 11;
    static String THREAD_POOL_CREATE = Ids.build(THREAD_POOL_PREFIX, CrudConstants.CREATE, Random.name());
    static String THREAD_POOL_READ = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.READ, Random.name());
    static String THREAD_POOL_UPDATE = Ids.build(THREAD_POOL_PREFIX, CrudConstants.UPDATE, Random.name());
    static String THREAD_POOL_DELETE = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.DELETE, Random.name());

    static Address threadPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(THREAD_POOL, name);
    }

    private BatchFixtures() {
    }
}
