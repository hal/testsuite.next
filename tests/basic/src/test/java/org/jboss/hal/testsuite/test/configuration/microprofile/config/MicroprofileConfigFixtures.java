/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.hal.testsuite.test.configuration.microprofile.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MICROPROFILE_CONFIG_SMALLRYE;
import static org.jboss.hal.testsuite.CrudConstants.*;

public class MicroprofileConfigFixtures {

    private static final String
        CONFIG_SOURCE = "config-source",
        CONFIG_PROVIDER = "config-provider",
        PROPS = "props";

    private static final Address SUBYSTEM_ADDRESS = Address.subsystem(MICROPROFILE_CONFIG_SMALLRYE);

    static final String
        ARCHIVE_NAME = "microprofile-config-custom.jar",
        ARCHIVE_FOR_UPDATE_NAME = "microprofile-config-custom-for-update.jar",
        CLASS = "class",
        NAME = "name",
        MODULE = "module",
        PROPERTIES = "properties",
        CLASS_NAME_LABEL = "Class Name",
        MODULE_LABEL = "Module",
        CONFIG_SOURCE_PROPS_CREATE = Ids.build(CONFIG_SOURCE, PROPS, CREATE, Random.name()),
        CONFIG_SOURCE_PROPS_UPDATE = Ids.build(CONFIG_SOURCE, PROPS, UPDATE, Random.name()),
        CONFIG_SOURCE_PROPS_DELETE = Ids.build(CONFIG_SOURCE, PROPS, DELETE, Random.name()),
        CONFIG_SOURCE_CLASS_CREATE = Ids.build(CONFIG_SOURCE, CLASS, CREATE, Random.name()),
        CONFIG_SOURCE_CLASS_UPDATE = Ids.build(CONFIG_SOURCE, CLASS, UPDATE, Random.name()),
        CONFIG_PROVIDER_CREATE = Ids.build(CONFIG_PROVIDER, CREATE, Random.name()),
        CONFIG_PROVIDER_UPDATE = Ids.build(CONFIG_PROVIDER, UPDATE, Random.name()),
        CONFIG_PROVIDER_DELETE = Ids.build(CONFIG_PROVIDER, DELETE, Random.name());

    static Path
        CUSTOM_MODULE_PATH = Paths.get("test", "configuration", "microprofile", "config"),
        CUSTOM_MODULE_FOR_UPDATE_PATH = Paths.get("test", "configuration", "microprofile", "config-for-update");

    static Address getConfigSourceAddress(String sourceName) {
        return SUBYSTEM_ADDRESS.and("config-source", sourceName);
    }

    static Address getConfigProviderAddress(String providerName) {
        return SUBYSTEM_ADDRESS.and("config-source-provider", providerName);
    }

    private MicroprofileConfigFixtures() {
    }

}
