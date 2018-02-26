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
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.RESOURCE_ADAPTER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.RESOURCE_ADAPTERS;

public final class ResourceAdapterFixtures {

    private static final String RESOURCE_ADAPTERS_PREFIX = "ra";

    static String BEANVALIDATIONGROUPS = "beanvalidationgroups";
    static String BOOTSTRAP_CONTEXT = "bootstrap-context";
    static String LOCAL_TRANSACTION = "LocalTransaction";
    static String WM_SECURITY = "wm-security";
    static String WM_SECURITY_DEFAULT_GROUPS = "wm-security-default-groups";
    static String WM_SECURITY_DEFAULT_PRINCIPAL = "wm-security-default-principal";
    static String WM_SECURITY_MAPPING_REQUIRED = "wm-security-mapping-required";

    static Address SUBSYSTEM_ADDRESS = Address.subsystem(RESOURCE_ADAPTERS);

    // ------------------------------------------------------ resource adapter

    static String RESOURCE_ADAPTER_CREATE = Ids.build(RESOURCE_ADAPTERS_PREFIX, CrudConstants.CREATE, Random.name());
    static String RESOURCE_ADAPTER_READ = Ids.build(RESOURCE_ADAPTERS_PREFIX, CrudConstants.READ, Random.name());
    static String RESOURCE_ADAPTER_UPDATE = Ids.build(RESOURCE_ADAPTERS_PREFIX, CrudConstants.UPDATE, Random.name());
    static String RESOURCE_ADAPTER_DELETE = Ids.build(RESOURCE_ADAPTERS_PREFIX, CrudConstants.DELETE, Random.name());

    static Address resourceAdapterAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(RESOURCE_ADAPTER, name);
    }

    private ResourceAdapterFixtures() {

    }
}
