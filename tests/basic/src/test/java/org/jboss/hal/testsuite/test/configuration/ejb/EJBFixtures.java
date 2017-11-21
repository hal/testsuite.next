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
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.EJB3;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ELYTRON;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVICE;

public interface EJBFixtures {

    String ALIASES = "aliases";
    String CLUSTER = "cluster";
    String DEFAULT_DISTINCT_NAME = "default-distinct-name";
    String DEFAULT_SINGLETON_BEAN_ACCESS_TIMEOUT = "default-singleton-bean-access-timeout";
    String DERIVE_SIZE = "derive-size";
    String FROM_WORKER_POOLS = "from-worker-pools";
    String MAX_SIZE = "max-size";
    String OUTFLOW_SECURITY_DOMAINS = "outflow-security-domains";
    String THREAD_POOL_NAME = "thread-pool-name";
    String USE_QUALIFIED_NAME = "use-qualified-name";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(EJB3);

    // ------------------------------------------------------ container / thread pool

    String TP_CREATE = Ids.build("tp", "create", Random.name());
    String TP_READ = Ids.build("tp", "read", Random.name());
    String TP_UPDATE = Ids.build("tp", "update", Random.name());
    String TP_DELETE = Ids.build("tp", "delete", Random.name());

    static Address threadPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("thread-pool", name);
    }

    // ------------------------------------------------------ container / remoting profile

    String RP_CREATE = Ids.build("rp", "create", Random.name());
    String RP_READ = Ids.build("rp", "read", Random.name());
    String RP_UPDATE = Ids.build("rp", "update", Random.name());
    String RP_DELETE = Ids.build("rp", "delete", Random.name());

    static Address remotingProfileAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("remoting-profile", name);
    }

    // ------------------------------------------------------ bean pool

    String BP_CREATE = Ids.build("bp", "create", Random.name());
    String BP_READ = Ids.build("bp", "read", Random.name());
    String BP_UPDATE = Ids.build("bp", "update", Random.name());
    String BP_DELETE = Ids.build("bp", "delete", Random.name());

    static Address beanPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("strict-max-bean-instance-pool", name);
    }

    // ------------------------------------------------------ state management / cache

    String CACHE_CREATE = Ids.build("cache", "create", Random.name());
    String CACHE_READ = Ids.build("cache", "read", Random.name());
    String CACHE_UPDATE = Ids.build("cache", "update", Random.name());
    String CACHE_DELETE = Ids.build("cache", "delete", Random.name());

    static Address cacheAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("cache", name);
    }

    // ------------------------------------------------------ state management / passivation

    String PS_CREATE = Ids.build("passivation", "create", Random.name());
    String PS_READ = Ids.build("passivation", "read", Random.name());
    String PS_UPDATE = Ids.build("passivation", "update", Random.name());
    String PS_DELETE = Ids.build("passivation", "delete", Random.name());

    Address SERVICE_ASYNC_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "async");
    Address SERVICE_IDENTITY_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "identity");
    Address SERVICE_IIOP_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "iiop");
    Address SERVICE_REMOTE_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "remote");
    Address SERVICE_TIMER_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "timer-service");

    static Address passivationAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("passivation-store", name);
    }

    // ------------------------------------------------------ mdb delivery group

    String MDB_CREATE = Ids.build("mdb", "create", Random.name());
    String MDB_READ = Ids.build("mdb", "read", Random.name());
    String MDB_UPDATE = Ids.build("mdb", "update", Random.name());
    String MDB_DELETE = Ids.build("mdb", "delete", Random.name());

    static Address mdbDeliveryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("mdb-delivery-group", name);
    }

    // ------------------------------------------------------ application security domain

    String ASD_CREATE = Ids.build("asd", "create", Random.name());
    String ASD_READ = Ids.build("asd", "read", Random.name());
    String ASD_UPDATE = Ids.build("asd", "update", Random.name());
    String ASD_DELETE = Ids.build("asd", "delete", Random.name());

    Address ELYTRON_ADDRESS = Address.subsystem(ELYTRON);

    static Address applicationSecurityDomainAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("application-security-domain", name);
    }
}
