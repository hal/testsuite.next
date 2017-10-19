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

import org.apache.commons.text.RandomStringGenerator;
import org.jboss.hal.resources.Ids;
import org.wildfly.extras.creaper.core.online.operations.Address;

public interface BatchFixtures {

    Address SUBSYSTEM_ADDRESS = Address.subsystem("batch-jberet");
    RandomStringGenerator GENERATOR = new RandomStringGenerator.Builder().withinRange('a', 'z').build();


    // ------------------------------------------------------ in memory repository

    String IN_MEMORY_CREATE = Ids.build("in-memory", "create", GENERATOR.generate(10));
    String IN_MEMORY_DELETE = Ids.build("in-memory", "delete", GENERATOR.generate(10));

    static Address inMemoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("in-memory-job-repository", name);
    }


    // ------------------------------------------------------ jdbc repository

    String DATA_SOURCE = Ids.build("ds", GENERATOR.generate(10));
    String JDBC_CREATE = Ids.build("jdbc", "create", GENERATOR.generate(10));
    String JDBC_READ = Ids.build("jdbc", "read", GENERATOR.generate(10));
    String JDBC_DELETE = Ids.build("jdbc", "remove", GENERATOR.generate(10));

    static Address jdbcAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("jdbc-job-repository", name);
    }


    // ------------------------------------------------------ thread factory

    String THREAD_FACTORY_CREATE = Ids.build("thread-factory", "create", GENERATOR.generate(10));
    String THREAD_FACTORY_READ = Ids.build("thread-factory", "read", GENERATOR.generate(10));
    String THREAD_FACTORY_UPDATE = Ids.build("thread-factory", "update", GENERATOR.generate(10));
    String THREAD_FACTORY_DELETE = Ids.build("thread-factory", "delete", GENERATOR.generate(10));

    static Address threadFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("thread-factory", name);
    }


    // ------------------------------------------------------ thread pool

    int MAX_THREADS = 11;
    String THREAD_POOL_CREATE = Ids.build("thread-pool", "create", GENERATOR.generate(10));
    String THREAD_POOL_READ = Ids.build("thread-pool", "read", GENERATOR.generate(10));
    String THREAD_POOL_UPDATE = Ids.build("thread-pool", "update", GENERATOR.generate(10));
    String THREAD_POOL_DELETE = Ids.build("thread-pool", "delete", GENERATOR.generate(10));

    static Address threadPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("thread-pool", name);
    }
}
