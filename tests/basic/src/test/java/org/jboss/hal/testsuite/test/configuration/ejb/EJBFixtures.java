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
package org.jboss.hal.testsuite.test.configuration.ejb;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.EJB3;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ELYTRON;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVICE;

public final class EJBFixtures {

    private static final String THREAD_POOL_PREFIX = "tp";
    private static final String REMOTING_PROFILE_PREFIX = "rp";
    private static final String BEAN_POOL_PREFIX = "bp";
    private static final String CACHE_PREFIX = "cache";
    private static final String PASSIVATION_PREFIX = "passivation";
    private static final String MDB_PREFIX = "mdb";
    private static final String ASD_PREFIX = "asd";

    static String ALIASES = "aliases";
    static String CLUSTER = "cluster";
    static String DEFAULT_DISTINCT_NAME = "default-distinct-name";
    static String DEFAULT_SINGLETON_BEAN_ACCESS_TIMEOUT = "default-singleton-bean-access-timeout";
    static String DERIVE_SIZE = "derive-size";
    static String FROM_WORKER_POOLS = "from-worker-pools";
    static String LOCAL_RECEIVER_PASS_BY_VALUE = "local-receiver-pass-by-value";
    static String MAX_SIZE = "max-size";
    static String OUTFLOW_SECURITY_DOMAINS = "outflow-security-domains";
    static String THREAD_POOL_NAME = "thread-pool-name";
    static String USE_QUALIFIED_NAME = "use-qualified-name";

    public static Address SUBSYSTEM_ADDRESS = Address.subsystem(EJB3);

    // ------------------------------------------------------ container / thread pool

    static String TP_CREATE = Ids.build(THREAD_POOL_PREFIX, CrudConstants.CREATE, Random.name());
    static String TP_READ = Ids.build(THREAD_POOL_PREFIX, CrudConstants.READ, Random.name());
    static String TP_UPDATE = Ids.build(THREAD_POOL_PREFIX, CrudConstants.UPDATE, Random.name());
    static String TP_DELETE = Ids.build(THREAD_POOL_PREFIX, CrudConstants.DELETE, Random.name());

    static Address threadPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("thread-pool", name);
    }

    // ------------------------------------------------------ container / remoting profile

    static String RP_CREATE = Ids.build(REMOTING_PROFILE_PREFIX, CrudConstants.CREATE, Random.name());
    static String RP_READ = Ids.build(REMOTING_PROFILE_PREFIX, CrudConstants.READ, Random.name());
    static String RP_UPDATE = Ids.build(REMOTING_PROFILE_PREFIX, CrudConstants.UPDATE, Random.name());
    static String RP_DELETE = Ids.build(REMOTING_PROFILE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address remotingProfileAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("remoting-profile", name);
    }

    // ------------------------------------------------------ bean pool

    static String BP_CREATE = Ids.build(BEAN_POOL_PREFIX, CrudConstants.CREATE, Random.name());
    static String BP_READ = Ids.build(BEAN_POOL_PREFIX, CrudConstants.READ, Random.name());
    static String BP_UPDATE = Ids.build(BEAN_POOL_PREFIX, CrudConstants.UPDATE, Random.name());
    static String BP_DELETE = Ids.build(BEAN_POOL_PREFIX, CrudConstants.DELETE, Random.name());

    static Address beanPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("strict-max-bean-instance-pool", name);
    }

    // ------------------------------------------------------ state management / cache

    static String CACHE_CREATE = Ids.build(CACHE_PREFIX, CrudConstants.CREATE, Random.name());
    static String CACHE_READ = Ids.build(CACHE_PREFIX, CrudConstants.READ, Random.name());
    static String CACHE_UPDATE = Ids.build(CACHE_PREFIX, CrudConstants.UPDATE, Random.name());
    static String CACHE_DELETE = Ids.build(CACHE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address cacheAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("cache", name);
    }

    // ------------------------------------------------------ state management / passivation

    static String PS_CREATE = Ids.build(PASSIVATION_PREFIX, CrudConstants.CREATE, Random.name());
    static String PS_READ = Ids.build(PASSIVATION_PREFIX, CrudConstants.READ, Random.name());
    static String PS_UPDATE = Ids.build(PASSIVATION_PREFIX, CrudConstants.UPDATE, Random.name());
    static String PS_DELETE = Ids.build(PASSIVATION_PREFIX, CrudConstants.DELETE, Random.name());

    static Address SERVICE_ASYNC_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "async");
    static Address SERVICE_IDENTITY_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "identity");
    static Address SERVICE_IIOP_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "iiop");
    static Address SERVICE_REMOTE_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "remote");
    static Address SERVICE_TIMER_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "timer-service");

    static Address passivationAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("passivation-store", name);
    }

    // ------------------------------------------------------ mdb delivery group

    static String MDB_CREATE = Ids.build(MDB_PREFIX, CrudConstants.CREATE, Random.name());
    static String MDB_READ = Ids.build(MDB_PREFIX, CrudConstants.READ, Random.name());
    static String MDB_UPDATE = Ids.build(MDB_PREFIX, CrudConstants.UPDATE, Random.name());
    static String MDB_DELETE = Ids.build(MDB_PREFIX, CrudConstants.DELETE, Random.name());

    static Address mdbDeliveryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("mdb-delivery-group", name);
    }

    // ------------------------------------------------------ application security domain

    static String ASD_CREATE = Ids.build(ASD_PREFIX, CrudConstants.CREATE, Random.name());
    static String ASD_UPDATE = Ids.build(ASD_PREFIX, CrudConstants.UPDATE, Random.name());
    static String ASD_DELETE = Ids.build(ASD_PREFIX, CrudConstants.DELETE, Random.name());

    static Address ELYTRON_ADDRESS = Address.subsystem(ELYTRON);

    static Address applicationSecurityDomainAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("application-security-domain", name);
    }

    private EJBFixtures() {
    }
}
