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
package org.jboss.hal.testsuite.test.configuration.ee;

import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public final class EEFixtures {

    private static final String CONTEXT_SERVICE_PREFIX = "cs";
    private static final String EXECUTOR_PREFIX = "exe";
    private static final String SCHEDULED_EXECUTOR_PREFIX = "se";
    private static final String THREAD_FACTORY_PREFIX = "tf";


    static final Address SUBSYSTEM_ADDRESS = Address.subsystem(EE);
    static final Address DEFAULT_BINDINGS_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, DEFAULT_BINDINGS);


    // ------------------------------------------------------ global module

    static final String GLOBAL_MODULES_CREATE = Ids.build("gm", CrudConstants.CREATE, Random.name());
    static final String GLOBAL_MODULES_DELETE = Ids.build("gm", CrudConstants.DELETE, Random.name());

    static ModelNode globalModule(String name) {
        ModelNode globalModule = new ModelNode();
        globalModule.get(NAME).set(name);
        globalModule.get("meta-inf").set(true);
        return globalModule;
    }


    // ------------------------------------------------------ context service

    static final String CONTEXT_SERVICE_CREATE = Ids.build(CONTEXT_SERVICE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CONTEXT_SERVICE_READ = Ids.build(CONTEXT_SERVICE_PREFIX, CrudConstants.READ, Random.name());
    static final String CONTEXT_SERVICE_UPDATE = Ids.build(CONTEXT_SERVICE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CONTEXT_SERVICE_DELETE = Ids.build(CONTEXT_SERVICE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address contextServiceAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONTEXT_SERVICE, name);
    }


    // ------------------------------------------------------ executor

    static final String EXECUTOR_CREATE = Ids.build(EXECUTOR_PREFIX, CrudConstants.CREATE, Random.name());
    static final String EXECUTOR_READ = Ids.build(EXECUTOR_PREFIX, CrudConstants.READ, Random.name());
    static final String EXECUTOR_UPDATE = Ids.build(EXECUTOR_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String EXECUTOR_DELETE = Ids.build(EXECUTOR_PREFIX, CrudConstants.DELETE, Random.name());

    static Address executorAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MANAGED_EXECUTOR_SERVICE, name);
    }


    // ------------------------------------------------------ scheduled executor

    static final String SCHEDULED_EXECUTOR_CREATE = Ids.build(SCHEDULED_EXECUTOR_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SCHEDULED_EXECUTOR_READ = Ids.build(SCHEDULED_EXECUTOR_PREFIX, CrudConstants.READ, Random.name());
    static final String SCHEDULED_EXECUTOR_UPDATE = Ids.build(SCHEDULED_EXECUTOR_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SCHEDULED_EXECUTOR_DELETE = Ids.build(SCHEDULED_EXECUTOR_PREFIX, CrudConstants.DELETE, Random.name());

    static Address scheduledExecutorAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MANAGED_SCHEDULED_EXECUTOR_SERVICE, name);
    }


    // ------------------------------------------------------ thread factory

    static final String THREAD_FACTORY_CREATE = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.CREATE, Random.name());
    static final String THREAD_FACTORY_READ = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.READ, Random.name());
    static final String THREAD_FACTORY_UPDATE = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String THREAD_FACTORY_DELETE = Ids.build(THREAD_FACTORY_PREFIX, CrudConstants.DELETE, Random.name());

    static Address threadFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MANAGED_THREAD_FACTORY, name);
    }

    private EEFixtures() {
    }
}
