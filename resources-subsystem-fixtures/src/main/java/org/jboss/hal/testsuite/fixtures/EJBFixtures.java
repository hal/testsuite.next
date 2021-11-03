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

import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLASS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.EJB3;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ELYTRON;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MODULE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVICE;

public final class EJBFixtures {

    public static final String ROLE_1 = "role1";
    public static final String ROLE_2 = "role2";

    public static final int SLEEP_TIME = 500;

    private static final String THREAD_POOL_PREFIX = "tp";
    private static final String REMOTING_PROFILE_PREFIX = "rp";
    private static final String BEAN_POOL_PREFIX = "bp";
    private static final String CACHE_PREFIX = "cache";
    private static final String PASSIVATION_PREFIX = "passivation";
    private static final String MDB_PREFIX = "mdb";
    private static final String ASD_PREFIX = "asd";
    private static final String SI_PREFIX = "si";

    public static final String ALIASES = "aliases";
    public static final String CLUSTER = "cluster";
    public static final String DEFAULT_DISTINCT_NAME = "default-distinct-name";
    public static final String DEFAULT_SINGLETON_BEAN_ACCESS_TIMEOUT = "default-singleton-bean-access-timeout";
    public static final String DERIVE_SIZE = "derive-size";
    public static final String FROM_WORKER_POOLS = "from-worker-pools";
    public static final String LOCAL_RECEIVER_PASS_BY_VALUE = "local-receiver-pass-by-value";
    public static final String MAX_SIZE = "max-size";
    public static final String OUTFLOW_SECURITY_DOMAINS = "outflow-security-domains";
    public static final String SERVER_INTERCEPTORS = "server-interceptors";
    public static final String THREAD_POOL_NAME = "thread-pool-name";
    public static final String USE_QUALIFIED_NAME = "use-qualified-name";

    // ------------------------------------------------------ address

    public static final Address SUBSYSTEM_ADDRESS = Address.subsystem(EJB3);

    public static Address singletonEJBAddress(String deploymentName, Class<?> singletonBeanClass) {
        return ejb3SubsystemAddress(deploymentName).and("singleton-bean", singletonBeanClass.getSimpleName());
    }

    private static Address ejb3SubsystemAddress(String deploymentName) {
        return Address.deployment(deploymentName).and("subsystem", "ejb3");
    }

    public static Address statelessEJBAddress(String deploymentName, Class<?> statelessBeanClass) {
        return ejb3SubsystemAddress(deploymentName).and("stateless-session-bean", statelessBeanClass.getSimpleName());
    }

    public static Address statefulEJBAddress(String deploymentName, Class<?> statefulBeanClass) {
        return ejb3SubsystemAddress(deploymentName).and("stateful-session-bean", statefulBeanClass.getSimpleName());
    }

    public static Address messageDrivenEJBAddress(String deploymentName, Class<?> messageDrivenBeanClass) {
        return ejb3SubsystemAddress(deploymentName).and("message-driven-bean", messageDrivenBeanClass.getSimpleName());
    }

    // ------------------------------------------------------ container / thread pool

    public static final String TP_CREATE = Ids.build(THREAD_POOL_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String TP_READ = Ids.build(THREAD_POOL_PREFIX, CrudConstants.READ, Random.name());
    public static final String TP_UPDATE = Ids.build(THREAD_POOL_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String TP_DELETE = Ids.build(THREAD_POOL_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address threadPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("thread-pool", name);
    }

    // ------------------------------------------------------ container / remoting profile

    public static final String RP_CREATE = Ids.build(REMOTING_PROFILE_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String RP_READ = Ids.build(REMOTING_PROFILE_PREFIX, CrudConstants.READ, Random.name());
    public static final String RP_UPDATE = Ids.build(REMOTING_PROFILE_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String RP_DELETE = Ids.build(REMOTING_PROFILE_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address remotingProfileAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("remoting-profile", name);
    }

    // ------------------------------------------------------ bean pool

    public static final String BP_CREATE = Ids.build(BEAN_POOL_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String BP_READ = Ids.build(BEAN_POOL_PREFIX, CrudConstants.READ, Random.name());
    public static final String BP_UPDATE = Ids.build(BEAN_POOL_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String BP_DELETE = Ids.build(BEAN_POOL_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address beanPoolAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("strict-max-bean-instance-pool", name);
    }

    // ------------------------------------------------------ state management / cache

    public static final String CACHE_CREATE = Ids.build(CACHE_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String CACHE_READ = Ids.build(CACHE_PREFIX, CrudConstants.READ, Random.name());
    public static final String CACHE_UPDATE = Ids.build(CACHE_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String CACHE_DELETE = Ids.build(CACHE_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address cacheAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("cache", name);
    }

    // ------------------------------------------------------ state management / passivation

    public static final String PS_CREATE = Ids.build(PASSIVATION_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String PS_READ = Ids.build(PASSIVATION_PREFIX, CrudConstants.READ, Random.name());
    public static final String PS_UPDATE = Ids.build(PASSIVATION_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String PS_DELETE = Ids.build(PASSIVATION_PREFIX, CrudConstants.DELETE, Random.name());

    public static final Address SERVICE_ASYNC_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "async");
    public static final Address SERVICE_IDENTITY_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "identity");
    public static final Address SERVICE_IIOP_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "iiop");
    public static final Address SERVICE_REMOTE_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "remote");
    public static final Address SERVICE_TIMER_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, "timer-service");

    public static Address passivationAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("passivation-store", name);
    }

    // ------------------------------------------------------ mdb delivery group

    public static final String MDB_CREATE = Ids.build(MDB_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String MDB_READ = Ids.build(MDB_PREFIX, CrudConstants.READ, Random.name());
    public static final String MDB_UPDATE = Ids.build(MDB_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String MDB_DELETE = Ids.build(MDB_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address mdbDeliveryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("mdb-delivery-group", name);
    }

    // ------------------------------------------------------ application security domain

    public static final String ASD_CREATE = Ids.build(ASD_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String ASD_UPDATE = Ids.build(ASD_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String ASD_DELETE = Ids.build(ASD_PREFIX, CrudConstants.DELETE, Random.name());

    public static final Address ELYTRON_ADDRESS = Address.subsystem(ELYTRON);

    public static Address applicationSecurityDomainAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("application-security-domain", name);
    }

    // ------------------------------------------------------ server interceptors

    public static final String SI_CLASS_CREATE = Ids.build(SI_PREFIX, CLASS, CrudConstants.CREATE, Random.name());
    public static final String SI_MODULE_CREATE = Ids.build(SI_PREFIX, MODULE, CrudConstants.CREATE, Random.name());
    public static final String SI_CLASS_DELETE = Ids.build(SI_PREFIX, CLASS, CrudConstants.DELETE, Random.name());
    public static final String SI_MODULE_DELETE = Ids.build(SI_PREFIX, MODULE, CrudConstants.DELETE, Random.name());

    public static ModelNode serverInterceptor(String clazz, String module) {
        ModelNode serverInterceptor = new ModelNode();
        serverInterceptor.get(CLASS).set(clazz);
        serverInterceptor.get(MODULE).set(module);
        return serverInterceptor;
    }

    // ------------------------------------------------------ constructor

    private EJBFixtures() {
    }
}
