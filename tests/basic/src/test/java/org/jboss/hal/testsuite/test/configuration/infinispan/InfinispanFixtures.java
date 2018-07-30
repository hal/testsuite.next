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
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CACHE_CONTAINER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.INFINISPAN;

public final class InfinispanFixtures {

    private static final String CACHE_CONTAINER_PREFIX = "cc";
    private static final String LOCAL_CACHE_PREFIX = "lc";
    private static final String COMPONENT = "component";

    static final String ACQUIRE_TIMEOUT = "acquire-timeout";
    static final String CONCURRENCY_LEVEL = "concurrency-level";
    static final String INTERVAL = "interval";
    static final String ISOLATION = "isolation";
    static final String LIFESPAN = "lifespan";
    static final String MAX_ENTRIES = "max-entries";
    static final String MAX_IDLE = "max-idle";
    static final String STRATEGY = "strategy";
    public static final String PATH = "path";
    public static final String WRITE = "write";
    public static final String BEHIND = "behind";
    public static final String THROUGH = "through";
    public static final String CONSISTENT_HASH_STRATEGY = "consistent-hash-strategy";

    public static Address SUBSYSTEM_ADDRESS = Address.subsystem(INFINISPAN);

    // ------------------------------------------------------ cache container

    static final String CC_CREATE = Ids.build(CACHE_CONTAINER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CC_READ = Ids.build(CACHE_CONTAINER_PREFIX, CrudConstants.READ, Random.name());
    static final String CC_UPDATE = Ids.build(CACHE_CONTAINER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CC_DELETE = Ids.build(CACHE_CONTAINER_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address cacheContainerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CACHE_CONTAINER, name);
    }


    // ------------------------------------------------------ local cache

    static final String LC_CREATE = Ids.build(LOCAL_CACHE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String LC_UPDATE = Ids.build(LOCAL_CACHE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String LC_UPDATE_ATTRIBUTES = Ids.build(LOCAL_CACHE_PREFIX, "update-attributes", Random.name());
    static final String LC_RESET = Ids.build(LOCAL_CACHE_PREFIX, "reset", Random.name());
    static final String LC_RESET_TRANSACTION = Ids.build(LOCAL_CACHE_PREFIX, "reset-transaction", Random.name());
    static final String LC_REMOVE = Ids.build(LOCAL_CACHE_PREFIX, "remove", Random.name());

    static Address localCacheAddress(String cacheContainer, String localCache) {
        return cacheContainerAddress(cacheContainer).and("local-cache", localCache);
    }

    public static Address scatteredCacheAddress(String cacheContainer, String scatteredCache) {
        return cacheContainerAddress(cacheContainer).and("scattered-cache", scatteredCache);
    }

    public static Address expirationAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and(COMPONENT, "expiration");
    }

    public static Address lockingAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and(COMPONENT, "locking");
    }

    public static Address partitionHandlingAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and(COMPONENT, "partition-handling");
    }

    public static Address stateTransferAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and(COMPONENT, "state-transfer");
    }

    public static Address transactionAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and(COMPONENT, "transaction");
    }

    public static Address objectMemoryAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and("memory", "object");
    }

    public static Address binaryMemoryAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and("memory", "binary");
    }

    public static Address offHeapMemoryAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and("memory", "off-heap");
    }

    public static Address backupAddress(String cacheContainer, String scatteredCache, String backup) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and(COMPONENT, "backups").and("backup", backup);
    }

    static Address componentAddress(String cacheContainer, String localCache, String component) {
        return localCacheAddress(cacheContainer, localCache).and(COMPONENT, component);
    }

    public static Address fileStoreAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and("store", "file");
    }

    public static Address customStoreAddress(String cacheContainer, String scatteredCache) {
        return scatteredCacheAddress(cacheContainer, scatteredCache).and("store", "custom");
    }


    // ------------------------------------------------------ local cache store

    static final String LC_NO_STORE = Ids.build(LOCAL_CACHE_PREFIX, "no-store", Random.name());
    static final String LC_FILE_STORE = Ids.build(LOCAL_CACHE_PREFIX, "file-store", Random.name());

    static Address storeAddress(String cacheContainer, String localCache, String store) {
        return localCacheAddress(cacheContainer, localCache).and("store", store);
    }

    private InfinispanFixtures() {
    }
}
