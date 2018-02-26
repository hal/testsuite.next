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
package org.jboss.hal.testsuite.test.configuration.jca;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.BEAN_VALIDATION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.JCA;
import static org.jboss.hal.dmr.ModelDescriptionConstants.WORKMANAGER;

public final class JcaFixtures {

    private static final String BOOTSTRAP_CONTEXT_PREFIX = "bc";
    private static final String WORKMANAGER_PREFIX = "wm";
    private static final String WORKMANAGER_THREADPOOL_PREFIX = "wm-tp";

    static String ALLOW_CORE_TIMEOUT = "allow-core-timeout";
    static String ARCHIVE_VALIDATION = "archive-validation";
    static String BOOTSTRAP_CONTEXT = "bootstrap-context";
    static String CACHED_CONNECTION_MANAGER = "cached-connection-manager";
    static String DEBUG = "debug";
    static String TRACER = "tracer";

    static Address SUBSYSTEM_ADDRESS = Address.subsystem(JCA);

    // ------------------------------------------------------ configuration

    static Address CACHED_CONNECTION_MANAGER_ADDRESS = SUBSYSTEM_ADDRESS.and(CACHED_CONNECTION_MANAGER,
            CACHED_CONNECTION_MANAGER);
    static Address ARCHIVE_VALIDATION_ADDRESS = SUBSYSTEM_ADDRESS.and(ARCHIVE_VALIDATION, ARCHIVE_VALIDATION);
    static Address BEAN_VALIDATION_ADDRESS = SUBSYSTEM_ADDRESS.and(BEAN_VALIDATION, BEAN_VALIDATION);

    // ------------------------------------------------------ tracer

    static Address TRACER_ADDRESS = SUBSYSTEM_ADDRESS.and(TRACER, TRACER);

    // ------------------------------------------------------ bootstrap context

    static String BC_CREATE = Ids.build(BOOTSTRAP_CONTEXT_PREFIX, CrudConstants.CREATE, Random.name());
    static String BC_READ = Ids.build(BOOTSTRAP_CONTEXT_PREFIX, CrudConstants.READ, Random.name());
    static String BC_DELETE = Ids.build(BOOTSTRAP_CONTEXT_PREFIX, CrudConstants.DELETE, Random.name());

    static Address bootstrapContextAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(BOOTSTRAP_CONTEXT, name);
    }

    // ------------------------------------------------------ workmanager

    static String WM_CREATE = Ids.build(WORKMANAGER_PREFIX, CrudConstants.CREATE, Random.name());
    static String WM_UPDATE = Ids.build(WORKMANAGER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String WM_DELETE = Ids.build(WORKMANAGER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address workmanagerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(WORKMANAGER, name);
    }

    // ------------------------------------------------------ wm thread pool

    static String LRT = "lrt";
    static String SRT = "srt";

    static String WM_THREAD_POOL_CREATE = Ids.build(WORKMANAGER_THREADPOOL_PREFIX, CrudConstants.CREATE, Random.name());
    static String WM_THREAD_POOL_READ = Ids.build(WORKMANAGER_THREADPOOL_PREFIX, CrudConstants.READ, Random.name());
    static String WM_THREAD_POOL_UPDATE = Ids.build(WORKMANAGER_THREADPOOL_PREFIX, CrudConstants.UPDATE, Random.name());
    static String WM_THREAD_POOL_DELETE = Ids.build(WORKMANAGER_THREADPOOL_PREFIX, CrudConstants.DELETE, Random.name());

    static Address longRunningAddress(String workmanager, String threadPool) {
        return workmanagerAddress(workmanager).and("long-running-threads", threadPool);
    }

    static Address shortRunningAddress(String workmanager, String threadPool) {
        return workmanagerAddress(workmanager).and("short-running-threads", threadPool);
    }

    static String threadPoolName(String workmanager, String threadPool) {
        return Ids.build(workmanager, threadPool);
    }

    private JcaFixtures() {
    }
}
