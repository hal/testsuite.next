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
    private static final String SOCKET_HANDLER_PREFIX = "soh";
    private static final String SYSLOG_HANDLER_PREFIX = "sh";
    private static final String CUSTOM_FORMATTER_PREFIX = "cf";
    private static final String PATTERN_FORMATTER_PREFIX = "pf";
    private static final String JSON_FORMATTER_PREFIX = "jf";
    private static final String XML_FORMATTER_PREFIX = "xf";
    private static final String PROFILE_PREFIX = "lp";

    public static final String ADD_LOGGING_API_DEPENDENCIES = "add-logging-api-dependencies";
    public static final String CATEGORY = "category";
    public static final String CLASS_VALUE = "org.jboss.as.logging.logmanager.Log4jAppenderHandler";
    public static final String COLOR_MAP = "color-map";
    public static final String COLOR_MAP_VALUE = "error:red";
    public static final String CUSTOM_FORMATTER_MODULE_VALUE = "org.jboss.logmanager";
    public static final String CUSTOM_FORMATTER_CLASS_1_VALUE = "org.jboss.logmanager.formatters.JsonFormatter";
    public static final String CUSTOM_FORMATTER_CLASS_2_VALUE = "org.jboss.logmanager.formatters.XmlFormatter";
    public static final String LOGGING_FORMATTER_ITEM = "logging-formatter-item";
    public static final String LOGGING_PROFILE_FORMATTER_ITEM = "logging-profile-formatter-item";
    public static final String LOGGING_HANDLER_ITEM = "logging-handler-item";
    public static final String LOGGING_PROFILE_HANDLER_ITEM = "logging-profile-handler-item";
    public static final String MODULE_VALUE = "org.jboss.as.logging";
    public static final String NAMESPACE_URI = "namespace-uri";
    public static final String NAMED_FORMATTER = "named-formatter";
    public static final String PATH_VALUE = "pa/th";
    public static final String RECORD_DELIMITER = "record-delimiter";
    public static final String RECORD_DELIMITER_PROPERTY_NAME = "recordDelimiter";
    public static final String SUFFIX = "suffix";
    public static final String SUFFIX_VALUE = "yyyy-MM-dd-HH-mm";
    public static final String NAME = "name";

    public static final Address SUBSYSTEM_ADDRESS = Address.subsystem(LOGGING);

    public static class Category {

        public static final String CATEGORY_UPDATE = Ids.build(CATEGORY_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String CATEGORY_READ = Ids.build(CATEGORY_PREFIX, CrudConstants.READ, Random.name());
        public static final String CATEGORY_DELETE = Ids.build(CATEGORY_PREFIX, CrudConstants.DELETE, Random.name());
        public static final String CATEGORY_CREATE = Ids.build(CATEGORY_PREFIX, CrudConstants.CREATE, Random.name());

        public static Address categoryAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(LOGGER, name);
        }
    }

    public static class ConsoleHandler {

        public static final String CONSOLE_HANDLER_CREATE = Ids.build(CONSOLE_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String CONSOLE_HANDLER_UPDATE = Ids.build(CONSOLE_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String CONSOLE_HANDLER_DELETE = Ids.build(CONSOLE_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address consoleHandlerAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(CONSOLE_HANDLER, name);
        }
    }

    public static class FileHandler {

        public static final String FILE_HANDLER_CREATE = Ids.build(FILE_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String FILE_HANDLER_READ = Ids.build(FILE_HANDLER_PREFIX, CrudConstants.READ, Random.name());
        public static final String FILE_HANDLER_UPDATE = Ids.build(FILE_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String FILE_HANDLER_DELETE = Ids.build(FILE_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address fileHandlerAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(FILE_HANDLER, name);
        }
    }

    public static class PeriodicHandler {

        public static final String PERIODIC_HANDLER_CREATE = Ids.build(PERIODIC_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String PERIODIC_HANDLER_READ = Ids.build(PERIODIC_HANDLER_PREFIX, CrudConstants.READ, Random.name());
        public static final String PERIODIC_HANDLER_UPDATE = Ids.build(PERIODIC_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String PERIODIC_HANDLER_DELETE = Ids.build(PERIODIC_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address periodicHandlerAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(PERIODIC_ROTATING_FILE_HANDLER, name);
        }
    }

    public static class PeriodicSizeHandler {

        public static final String PERIODIC_SIZE_HANDLER_CREATE = Ids.build(PERIODIC_SIZE_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String PERIODIC_SIZE_HANDLER_READ = Ids.build(PERIODIC_SIZE_HANDLER_PREFIX, CrudConstants.READ, Random.name());
        public static final String PERIODIC_SIZE_HANDLER_UPDATE = Ids.build(PERIODIC_SIZE_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String PERIODIC_SIZE_HANDLER_DELETE = Ids.build(PERIODIC_SIZE_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address periodicSizeHandlerAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(PERIODIC_SIZE_ROTATING_FILE_HANDLER, name);
        }
    }

    public static class SizeHandler {

        public static final String SIZE_HANDLER_CREATE = Ids.build(SIZE_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String SIZE_HANDLER_READ = Ids.build(SIZE_HANDLER_PREFIX, CrudConstants.READ, Random.name());
        public static final String SIZE_HANDLER_UPDATE = Ids.build(SIZE_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String SIZE_HANDLER_DELETE = Ids.build(SIZE_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address sizeHandlerAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(SIZE_ROTATING_FILE_HANDLER, name);
        }
    }

    public static class AsyncHandler {

        public static final String ASYNC_HANDLER_CREATE = Ids.build(ASYNC_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String ASYNC_HANDLER_UPDATE = Ids.build(ASYNC_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String ASYNC_HANDLER_DELETE = Ids.build(ASYNC_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address asyncHandlerAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(ASYNC_HANDLER, name);
        }
    }

    public static class CustomHandler {

        public static final String CUSTOM_HANDLER_CREATE = Ids.build(CUSTOM_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String CUSTOM_HANDLER_READ = Ids.build(CUSTOM_HANDLER_PREFIX, CrudConstants.READ, Random.name());
        public static final String CUSTOM_HANDLER_UPDATE = Ids.build(CUSTOM_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String CUSTOM_HANDLER_DELETE = Ids.build(CUSTOM_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address customHandlerAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(CUSTOM_HANDLER, name);
        }
    }

    public static class SocketHandler {

        public static final String SOCKET_HANDLER_CREATE = Ids.build(SOCKET_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String SOCKET_HANDLER_UPDATE = Ids.build(SOCKET_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String SOCKET_HANDLER_DELETE = Ids.build(SOCKET_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());
        public static final String SOCKET_HANDLER = "socket-handler";

        public static Address socketHandlerAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(SOCKET_HANDLER, name);
        }
    }

    public static class SyslogHandler {

        public static final String SYSLOG_HANDLER_CREATE = Ids.build(SYSLOG_HANDLER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String SYSLOG_HANDLER_UPDATE = Ids.build(SYSLOG_HANDLER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String SYSLOG_HANDLER_DELETE = Ids.build(SYSLOG_HANDLER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address syslogHandlerAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(SYSLOG_HANDLER, name);
        }
    }

    public static class PatternFormatter {

        public static final String PATTERN_FORMATTER_CREATE = Ids.build(PATTERN_FORMATTER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String PATTERN_FORMATTER_UPDATE = Ids.build(PATTERN_FORMATTER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String PATTERN_FORMATTER_DELETE = Ids.build(PATTERN_FORMATTER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address patternFormatterAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(PATTERN_FORMATTER, name);
        }
    }

    public static class JsonFormatter {

        public static final String JSON_FORMATTER = "json-formatter";
        public static final String JSON_FORMATTER_CREATE = Ids.build(JSON_FORMATTER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String JSON_FORMATTER_UPDATE = Ids.build(JSON_FORMATTER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String JSON_FORMATTER_RESET = Ids.build(JSON_FORMATTER_PREFIX, CrudConstants.RESET, Random.name());
        public static final String JSON_FORMATTER_DELETE = Ids.build(JSON_FORMATTER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address jsonFormatterAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(JSON_FORMATTER, name);
        }
    }

    public static class XmlFormatter {

        public static final String XML_FORMATTER = "xml-formatter";
        public static final String XML_FORMATTER_CREATE = Ids.build(XML_FORMATTER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String XML_FORMATTER_UPDATE = Ids.build(XML_FORMATTER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String XML_FORMATTER_RESET = Ids.build(XML_FORMATTER_PREFIX, CrudConstants.RESET, Random.name());
        public static final String XML_FORMATTER_DELETE = Ids.build(XML_FORMATTER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address xmlFormatterAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(XML_FORMATTER, name);
        }
    }

    public static class CustomFormatter {

        public static final String CUSTOM_FORMATTER_CREATE = Ids.build(CUSTOM_FORMATTER_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String CUSTOM_FORMATTER_UPDATE = Ids.build(CUSTOM_FORMATTER_PREFIX, CrudConstants.UPDATE, Random.name());
        public static final String CUSTOM_FORMATTER_RESET = Ids.build(CUSTOM_FORMATTER_PREFIX, CrudConstants.RESET, Random.name());
        public static final String CUSTOM_FORMATTER_DELETE = Ids.build(CUSTOM_FORMATTER_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address customFormatterAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(CUSTOM_FORMATTER, name);
        }
    }

    public static class LoggingProfile {

        public static final String PROFILE_CREATE = Ids.build(PROFILE_PREFIX, CrudConstants.CREATE, Random.name());
        public static final String PROFILE_READ = Ids.build(PROFILE_PREFIX, CrudConstants.READ, Random.name());
        public static final String PROFILE_DELETE = Ids.build(PROFILE_PREFIX, CrudConstants.DELETE, Random.name());

        public static Address loggingProfileAddress(String name) {
            return SUBSYSTEM_ADDRESS.and(LOGGING_PROFILE, name);
        }

        public static Address categoryAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(LOGGER, name);
        }

        public static Address asyncHandlerAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(ASYNC_HANDLER, name);
        }

        public static Address consoleHandlerAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(CONSOLE_HANDLER, name);
        }

        public static Address customHandlerAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(CUSTOM_HANDLER, name);
        }

        public static Address fileHandlerAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(FILE_HANDLER, name);
        }

        public static Address periodicHandlerAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(PERIODIC_ROTATING_FILE_HANDLER, name);
        }

        public static Address periodicSizeHandlerAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(PERIODIC_SIZE_ROTATING_FILE_HANDLER, name);
        }

        public static Address sizeHandlerAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(SIZE_ROTATING_FILE_HANDLER, name);
        }

        public static Address socketHandlerAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(SOCKET_HANDLER, name);
        }

        public static Address syslogHandlerAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(SYSLOG_HANDLER, name);
        }

        public static Address patternFormatterAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(PATTERN_FORMATTER, name);
        }

        public static Address jsonFormatterAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(JSON_FORMATTER, name);
        }

        public static Address xmlFormatterAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(XML_FORMATTER, name);
        }

        public static Address customFormatterAddress(String loggingProfile, String name) {
            return loggingProfileAddress(loggingProfile).and(CUSTOM_FORMATTER, name);
        }
    }

    private LoggingFixtures() {
    }
}
