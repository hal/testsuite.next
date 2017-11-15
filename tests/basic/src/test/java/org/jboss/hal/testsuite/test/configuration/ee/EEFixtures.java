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
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public interface EEFixtures {

    Address SUBSYSTEM_ADDRESS = Address.subsystem(EE);
    Address DEFAULT_BINDINGS_ADDRESS = SUBSYSTEM_ADDRESS.and(SERVICE, DEFAULT_BINDINGS);


    // ------------------------------------------------------ global module

    String GLOBAL_MODULES_CREATE = Ids.build("gm", "create", Random.name());
    String GLOBAL_MODULES_DELETE = Ids.build("gm", "delete", Random.name());

    static ModelNode globalModule(String name) {
        ModelNode globalModule = new ModelNode();
        globalModule.get(NAME).set(name);
        globalModule.get("meta-inf").set(true);
        return globalModule;
    }


    // ------------------------------------------------------ context service

    String CONTEXT_SERVICE_CREATE = Ids.build("cs", "create", Random.name());
    String CONTEXT_SERVICE_READ = Ids.build("cs", "read", Random.name());
    String CONTEXT_SERVICE_UPDATE = Ids.build("cs", "update", Random.name());
    String CONTEXT_SERVICE_DELETE = Ids.build("cs", "delete", Random.name());

    static Address contextServiceAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONTEXT_SERVICE, name);
    }


    // ------------------------------------------------------ executor

    String EXECUTOR_CREATE = Ids.build("exe", "create", Random.name());
    String EXECUTOR_READ = Ids.build("exe", "read", Random.name());
    String EXECUTOR_UPDATE = Ids.build("exe", "update", Random.name());
    String EXECUTOR_DELETE = Ids.build("exe", "delete", Random.name());

    static Address executorAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MANAGED_EXECUTOR_SERVICE, name);
    }


    // ------------------------------------------------------ scheduled executor

    String SCHEDULED_EXECUTOR_CREATE = Ids.build("se", "create", Random.name());
    String SCHEDULED_EXECUTOR_READ = Ids.build("se", "read", Random.name());
    String SCHEDULED_EXECUTOR_UPDATE = Ids.build("se", "update", Random.name());
    String SCHEDULED_EXECUTOR_DELETE = Ids.build("se", "delete", Random.name());

    static Address scheduledExecutorAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MANAGED_SCHEDULED_EXECUTOR_SERVICE, name);
    }


    // ------------------------------------------------------ thread factory

    String THREAD_FACTORY_CREATE = Ids.build("tf", "create", Random.name());
    String THREAD_FACTORY_READ = Ids.build("tf", "read", Random.name());
    String THREAD_FACTORY_UPDATE = Ids.build("tf", "update", Random.name());
    String THREAD_FACTORY_DELETE = Ids.build("tf", "delete", Random.name());

    static Address threadFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MANAGED_THREAD_FACTORY, name);
    }
}
