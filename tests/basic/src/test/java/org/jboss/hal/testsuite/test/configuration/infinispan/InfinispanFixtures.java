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
package org.jboss.hal.testsuite.test.configuration.infinispan;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CACHE_CONTAINER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.INFINISPAN;

public interface InfinispanFixtures {

    String ACQUIRE_TIMEOUT = "acquire-timeout";
    String CONCURRENCY_LEVEL = "concurrency-level";
    String INTERVAL = "interval";
    String ISOLATION = "isolation";
    String LIFESPAN = "lifespan";
    String MAX_ENTRIES = "max-entries";
    String MAX_IDLE = "max-idle";
    String STRATEGY = "strategy";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(INFINISPAN);

    // ------------------------------------------------------ cache container

    String CC_CREATE = Ids.build("cc", "create", Random.name());
    String CC_READ = Ids.build("cc", "read", Random.name());
    String CC_UPDATE = Ids.build("cc", "update", Random.name());
    String CC_DELETE = Ids.build("cc", "update", Random.name());

    static Address cacheContainerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CACHE_CONTAINER, name);
    }


    // ------------------------------------------------------ local cache

    String LC_CREATE = Ids.build("lc", "create", Random.name());
    String LC_UPDATE = Ids.build("lc", "update", Random.name());
    String LC_RESET = Ids.build("lc", "reset", Random.name());
    String LC_REMOVE = Ids.build("lc", "remove", Random.name());

    static Address localCacheAddress(String cacheContainer, String localCache) {
        return cacheContainerAddress(cacheContainer).and("local-cache", localCache);
    }

    static Address componentAddress(String cacheContainer, String localCache, String component) {
        return localCacheAddress(cacheContainer, localCache).and("component", component);
    }


    // ------------------------------------------------------ local cache store

    String LC_NO_STORE = Ids.build("lc", "no-store", Random.name());
    String LC_FILE_STORE = Ids.build("lc", "file-store", Random.name());

    static Address storeAddress(String cacheContainer, String localCache, String store) {
        return localCacheAddress(cacheContainer, localCache).and("store", store);
    }
}
