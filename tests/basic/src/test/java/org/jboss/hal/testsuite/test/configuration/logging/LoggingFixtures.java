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
package org.jboss.hal.testsuite.test.configuration.logging;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public final class LoggingFixtures {

    private static final String CATEGORY_PREFIX = "cat";
    private static final String CONSOLE_HANDLER_PREFIX = "ch";
    private static final String FILE_HANDLER_PREFIX = "fh";
    private static final String PERIODIC_HANDLER_PREFIX = "ph";
    private static final String PERIODIC_SIZE_HANDLER_PREFIX = "psh";
    private static final String SIZE_HANDLER_PREFIX = "sh";
    private static final String ASYNC_HANDLER_PREFIX = "ah";
    private static final String CUSTOM_HANDLER_PREFIX = "ch";
    private static final String SYSLOG_HANDLER_PREFIX = "sh";
    private static final String PATTERN_FORMATTER_PREFIX = "pf";
    private static final String PROFILE_PREFIX = "lp";

    static String ADD_LOGGING_API_DEPENDENCIES = "add-logging-api-dependencies";
    static String CATEGORY = "category";
    static String CLASS_VALUE = "org.jboss.as.logging.logmanager.Log4jAppenderHandler";
    static String COLOR_MAP = "color-map";
    static String COLOR_MAP_VALUE = "error:red";
    static String LOGGING_FORMATTER_ITEM = "logging-formatter-item";
    static String LOGGING_HANDLER_ITEM = "logging-handler-item";
    static String MODULE_VALUE = "org.jboss.as.logging";
    static String PATH_VALUE = "pa/th";
    static String SUFFIX = "suffix";
    static String SUFFIX_VALUE = "yyyy-MM-dd-HH-mm";

    static Address SUBSYSTEM_ADDRESS = Address.subsystem(LOGGING);
    static Address ROOT_LOGGER_ADDRESS = Address.subsystem(LOGGING).and("root-logger", "ROOT");

    // ------------------------------------------------------ category

    static String CATEGORY_CREATE = Ids.build(CATEGORY_PREFIX, CrudConstants.CREATE, Random.name());
    static String CATEGORY_READ = Ids.build(CATEGORY_PREFIX, CrudConstants.READ, Random.name());
    static String CATEGORY_UPDATE = Ids.build(CATEGORY_PREFIX, CrudConstants.UPDATE, Random.name());
    static String CATEGORY_DELETE = Ids.build(CATEGORY_PREFIX, CrudConstants.DELETE, Random.name());

    static Address categoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LOGGER, name);
    }

    // ------------------------------------------------------ console handler

    static String CONSOLE_HANDLER_CREATE = Ids.build(CONSOLE_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
    static String CONSOLE_HANDLER_UPDATE = Ids.build(CONSOLE_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String CONSOLE_HANDLER_DELETE = Ids.build(CONSOLE_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address consoleHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSOLE_HANDLER, name);
    }

    // ------------------------------------------------------ file handler

    static String FILE_HANDLER_CREATE = Ids.build(FILE_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
    static String FILE_HANDLER_READ = Ids.build(FILE_HANDLER_PREFIX, CrudConstants.READ, Random.name());
    static String FILE_HANDLER_UPDATE = Ids.build(FILE_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String FILE_HANDLER_DELETE = Ids.build(FILE_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address fileHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(FILE_HANDLER, name);
    }

    // ------------------------------------------------------ periodic handler

    static String PERIODIC_HANDLER_CREATE = Ids.build(PERIODIC_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
    static String PERIODIC_HANDLER_READ = Ids.build(PERIODIC_HANDLER_PREFIX, CrudConstants.READ, Random.name());
    static String PERIODIC_HANDLER_UPDATE = Ids.build(PERIODIC_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String PERIODIC_HANDLER_DELETE = Ids.build(PERIODIC_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address periodicHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PERIODIC_ROTATING_FILE_HANDLER, name);
    }

    // ------------------------------------------------------ periodic size handler

    static String PERIODIC_SIZE_HANDLER_CREATE = Ids.build(PERIODIC_SIZE_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
    static String PERIODIC_SIZE_HANDLER_READ = Ids.build(PERIODIC_SIZE_HANDLER_PREFIX, CrudConstants.READ, Random.name());
    static String PERIODIC_SIZE_HANDLER_UPDATE = Ids.build(PERIODIC_SIZE_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String PERIODIC_SIZE_HANDLER_DELETE = Ids.build(PERIODIC_SIZE_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address periodicSizeHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PERIODIC_SIZE_ROTATING_FILE_HANDLER, name);
    }

    // ------------------------------------------------------ size handler

    static String SIZE_HANDLER_CREATE = Ids.build(SIZE_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
    static String SIZE_HANDLER_READ = Ids.build(SIZE_HANDLER_PREFIX, CrudConstants.READ, Random.name());
    static String SIZE_HANDLER_UPDATE = Ids.build(SIZE_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String SIZE_HANDLER_DELETE = Ids.build(SIZE_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address sizeHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SIZE_ROTATING_FILE_HANDLER, name);
    }

    // ------------------------------------------------------ async handler

    static String ASYNC_HANDLER_CREATE = Ids.build(ASYNC_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
    static String ASYNC_HANDLER_UPDATE = Ids.build(ASYNC_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String ASYNC_HANDLER_DELETE = Ids.build(ASYNC_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address asyncHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ASYNC_HANDLER, name);
    }

    // ------------------------------------------------------ custom handler

    static String CUSTOM_HANDLER_CREATE = Ids.build(CUSTOM_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
    static String CUSTOM_HANDLER_READ = Ids.build(CUSTOM_HANDLER_PREFIX, CrudConstants.READ, Random.name());
    static String CUSTOM_HANDLER_UPDATE = Ids.build(CUSTOM_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String CUSTOM_HANDLER_DELETE = Ids.build(CUSTOM_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address customHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CUSTOM_HANDLER, name);
    }

    // ------------------------------------------------------ syslog handler

    static String SYSLOG_HANDLER_CREATE = Ids.build(SYSLOG_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
    static String SYSLOG_HANDLER_UPDATE = Ids.build(SYSLOG_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String SYSLOG_HANDLER_DELETE = Ids.build(SYSLOG_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address syslogHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SYSLOG_HANDLER, name);
    }

    // ------------------------------------------------------ pattern formatter

    static String PATTERN_FORMATTER_CREATE = Ids.build(PATTERN_FORMATTER_PREFIX, CrudConstants.CREATE, Random.name());
    static String PATTERN_FORMATTER_UPDATE = Ids.build(PATTERN_FORMATTER_PREFIX, CrudConstants.UPDATE, Random.name());
    static String PATTERN_FORMATTER_DELETE = Ids.build(PATTERN_FORMATTER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address patternFormatterAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PATTERN_FORMATTER, name);
    }

    // ------------------------------------------------------ profiles

    static String PROFILE_CREATE = Ids.build(PROFILE_PREFIX, CrudConstants.CREATE, Random.name());
    static String PROFILE_READ = Ids.build(PROFILE_PREFIX, CrudConstants.READ, Random.name());
    static String PROFILE_DELETE = Ids.build(PROFILE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address profileAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LOGGING_PROFILE, name);
    }

    private LoggingFixtures() {
    }
}
