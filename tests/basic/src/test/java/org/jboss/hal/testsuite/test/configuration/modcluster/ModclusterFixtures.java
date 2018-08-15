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
package org.jboss.hal.testsuite.test.configuration.modcluster;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MODCLUSTER;

public final class ModclusterFixtures {

    static final String EXCLUDED_CONTEXTS = "excluded-contexts";
    static final String LOAD_MET = "load-met";
    static final String NODE_TIMEOUT = "node-timeout";
    static final String PROXY = "proxy";
    static final String PROXY_URL = "proxy-url";
    static final String STICKY_SESSION = "sticky-session";
    static final String HISTORY = "history";
    static final String WEIGHT = "weight";

    static final Address SUBSYSTEM_ADDRESS = Address.subsystem(MODCLUSTER);

    // ----------------------- proxy

    static final String PROXY_CREATE = Ids.build(PROXY, CrudConstants.CREATE, Random.name());
    static final String PROXY_CREATE2 = Ids.build(PROXY, "create2", Random.name());
    static final String PROXY_READ = Ids.build(PROXY, CrudConstants.READ, Random.name());
    static final String PROXY_UPDATE = Ids.build(PROXY, CrudConstants.UPDATE, Random.name());
    static final String PROXY_DELETE = Ids.build(PROXY, CrudConstants.DELETE, Random.name());

    static Address proxyAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("proxy", name);
    }

    static Address loadProviderDynamicAddress(String proxy) {
        return SUBSYSTEM_ADDRESS.and("proxy", proxy).and("load-provider", "dynamic");
    }

    static Address customLoadMetricAddress(String proxy, String name) {
        return loadProviderDynamicAddress(proxy).and("custom-load-metric", name);
    }

    // ----------------------- load metric

    static final String LOAD_MET_CREATE = Ids.build(LOAD_MET, CrudConstants.CREATE, Random.name());
    static final String LOAD_MET_UPDATE = Ids.build(LOAD_MET, CrudConstants.UPDATE, Random.name());
    static final String LOAD_MET_DELETE = Ids.build(LOAD_MET, CrudConstants.DELETE, Random.name());

    static Address loadMetricAddress(String proxy, String name) {
        return loadProviderDynamicAddress(proxy).and("load-metric", name);
    }

    private ModclusterFixtures() {
    }
}
