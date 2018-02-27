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
package org.jboss.hal.testsuite.test.configuration.systemproperty;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.SYSTEM_PROPERTY;

public final class SystemPropertyFixtures {

    private static final String SYSTEM_PROPERTY_PREFIX = "sp";
    private static final String NAME = "name";
    private static final String VALUE = "value";

    static final String CREATE_NAME = Ids.build(SYSTEM_PROPERTY_PREFIX, CrudConstants.CREATE, NAME, Random.name());
    static final String CREATE_VALUE = Ids.build(SYSTEM_PROPERTY_PREFIX, CrudConstants.CREATE, VALUE, Random.name());
    static final String READ_NAME = Ids.build(SYSTEM_PROPERTY_PREFIX, CrudConstants.READ, NAME, Random.name());
    static final String READ_VALUE = Ids.build(SYSTEM_PROPERTY_PREFIX, CrudConstants.READ, VALUE, Random.name());
    static final String UPDATE_NAME = Ids.build(SYSTEM_PROPERTY_PREFIX, CrudConstants.UPDATE, NAME, Random.name());
    static final String UPDATE_VALUE = Ids.build(SYSTEM_PROPERTY_PREFIX, CrudConstants.UPDATE, VALUE, Random.name());
    static final String DELETE_NAME = Ids.build(SYSTEM_PROPERTY_PREFIX, CrudConstants.DELETE, NAME, Random.name());
    static final String DELETE_VALUE = Ids.build(SYSTEM_PROPERTY_PREFIX, CrudConstants.DELETE, VALUE, Random.name());

    public static Address systemPropertyAddress(String name) {
        return Address.of(SYSTEM_PROPERTY, name);
    }

    private SystemPropertyFixtures() {

    }
}
