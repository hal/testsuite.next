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
package org.jboss.hal.testsuite.test.configuration.resourceadapter;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.RESOURCE_ADAPTER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RESOURCE_ADAPTERS;

public interface ResourceAdapterFixtures {

    String BEANVALIDATIONGROUPS = "beanvalidationgroups";
    String BOOTSTRAP_CONTEXT = "bootstrap-context";
    String LOCAL_TRANSACTION = "LocalTransaction";
    String WM_SECURITY = "wm-security";
    String WM_SECURITY_DEFAULT_GROUPS = "wm-security-default-groups";
    String WM_SECURITY_DEFAULT_PRINCIPAL = "wm-security-default-principal";
    String WM_SECURITY_MAPPING_REQUIRED = "wm-security-mapping-required";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(RESOURCE_ADAPTERS);

    // ------------------------------------------------------ resource adapter

    String RESOURCE_ADAPTER_CREATE = Ids.build("ra", "create", Random.name());
    String RESOURCE_ADAPTER_READ = Ids.build("ra", "read", Random.name());
    String RESOURCE_ADAPTER_UPDATE = Ids.build("ra", "update", Random.name());
    String RESOURCE_ADAPTER_DELETE = Ids.build("ra", "delete", Random.name());

    static Address resourceAdapterAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(RESOURCE_ADAPTER, name);
    }
}
