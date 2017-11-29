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
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public interface LoggingFixtures {

    String ADD_LOGGING_API_DEPENDENCIES = "add-logging-api-dependencies";
    String CATEGORY = "category";
    String CLASS_VALUE = "org.jboss.as.logging.logmanager.Log4jAppenderHandler";
    String COLOR_MAP = "color-map";
    String COLOR_MAP_VALUE = "error:red";
    String LOGGING_FORMATTER_ITEM = "logging-formatter-item";
    String LOGGING_HANDLER_ITEM = "logging-handler-item";
    String MODULE_VALUE = "org.jboss.as.logging";
    String PATH_VALUE = "pa/th";
    String SUFFIX = "suffix";
    String SUFFIX_VALUE = "yyyy-MM-dd-HH-mm";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(LOGGING);
    Address ROOT_LOGGER_ADDRESS = Address.subsystem(LOGGING).and("root-logger", "ROOT");

    // ------------------------------------------------------ category

    String CATEGORY_CREATE = Ids.build("cat", "create", Random.name());
    String CATEGORY_READ = Ids.build("cat", "read", Random.name());
    String CATEGORY_UPDATE = Ids.build("cat", "update", Random.name());
    String CATEGORY_DELETE = Ids.build("cat", "delete", Random.name());

    static Address categoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LOGGER, name);
    }

    // ------------------------------------------------------ console handler

    String CONSOLE_HANDLER_CREATE = Ids.build("ch", "create", Random.name());
    String CONSOLE_HANDLER_UPDATE = Ids.build("ch", "update", Random.name());
    String CONSOLE_HANDLER_DELETE = Ids.build("ch", "delete", Random.name());

    static Address consoleHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONSOLE_HANDLER, name);
    }

    // ------------------------------------------------------ file handler

    String FILE_HANDLER_CREATE = Ids.build("fh", "create", Random.name());
    String FILE_HANDLER_READ = Ids.build("fh", "read", Random.name());
    String FILE_HANDLER_UPDATE = Ids.build("fh", "update", Random.name());
    String FILE_HANDLER_DELETE = Ids.build("fh", "delete", Random.name());

    static Address fileHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(FILE_HANDLER, name);
    }

    // ------------------------------------------------------ periodic handler

    String PERIODIC_HANDLER_CREATE = Ids.build("ph", "create", Random.name());
    String PERIODIC_HANDLER_READ = Ids.build("ph", "read", Random.name());
    String PERIODIC_HANDLER_UPDATE = Ids.build("ph", "update", Random.name());
    String PERIODIC_HANDLER_DELETE = Ids.build("ph", "delete", Random.name());

    static Address periodicHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PERIODIC_ROTATING_FILE_HANDLER, name);
    }

    // ------------------------------------------------------ periodic size handler

    String PERIODIC_SIZE_HANDLER_CREATE = Ids.build("psh", "create", Random.name());
    String PERIODIC_SIZE_HANDLER_READ = Ids.build("psh", "read", Random.name());
    String PERIODIC_SIZE_HANDLER_UPDATE = Ids.build("psh", "update", Random.name());
    String PERIODIC_SIZE_HANDLER_DELETE = Ids.build("psh", "delete", Random.name());

    static Address periodicSizeHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PERIODIC_SIZE_ROTATING_FILE_HANDLER, name);
    }

    // ------------------------------------------------------ size handler

    String SIZE_HANDLER_CREATE = Ids.build("sh", "create", Random.name());
    String SIZE_HANDLER_READ = Ids.build("sh", "read", Random.name());
    String SIZE_HANDLER_UPDATE = Ids.build("sh", "update", Random.name());
    String SIZE_HANDLER_DELETE = Ids.build("sh", "delete", Random.name());

    static Address sizeHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SIZE_ROTATING_FILE_HANDLER, name);
    }

    // ------------------------------------------------------ async handler

    String ASYNC_HANDLER_CREATE = Ids.build("ah", "create", Random.name());
    String ASYNC_HANDLER_UPDATE = Ids.build("ah", "update", Random.name());
    String ASYNC_HANDLER_DELETE = Ids.build("ah", "delete", Random.name());

    static Address asyncHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(ASYNC_HANDLER, name);
    }

    // ------------------------------------------------------ custom handler

    String CUSTOM_HANDLER_CREATE = Ids.build("ch", "create", Random.name());
    String CUSTOM_HANDLER_READ = Ids.build("ch", "read", Random.name());
    String CUSTOM_HANDLER_UPDATE = Ids.build("ch", "update", Random.name());
    String CUSTOM_HANDLER_DELETE = Ids.build("ch", "delete", Random.name());

    static Address customHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CUSTOM_HANDLER, name);
    }

    // ------------------------------------------------------ syslog handler

    String SYSLOG_HANDLER_CREATE = Ids.build("sh", "create", Random.name());
    String SYSLOG_HANDLER_UPDATE = Ids.build("sh", "update", Random.name());
    String SYSLOG_HANDLER_DELETE = Ids.build("sh", "delete", Random.name());

    static Address syslogHandlerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SYSLOG_HANDLER, name);
    }

    // ------------------------------------------------------ pattern formatter

    String PATTERN_FORMATTER_CREATE = Ids.build("pf", "create", Random.name());
    String PATTERN_FORMATTER_UPDATE = Ids.build("pf", "update", Random.name());
    String PATTERN_FORMATTER_DELETE = Ids.build("pf", "delete", Random.name());

    static Address patternFormatterAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PATTERN_FORMATTER, name);
    }

    // ------------------------------------------------------ profiles

    String PROFILE_CREATE = Ids.build("lp", "create", Random.name());
    String PROFILE_READ = Ids.build("lp", "read", Random.name());
    String PROFILE_DELETE = Ids.build("lp", "delete", Random.name());

    static Address profileAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(LOGGING_PROFILE, name);
    }
}
