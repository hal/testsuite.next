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
package org.jboss.hal.testsuite.fixtures;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SYSTEM_PROPERTY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;

public interface SystemPropertyFixtures {

    String CREATE_NAME = Ids.build(SYSTEM_PROPERTY, CrudConstants.CREATE, NAME, Random.name());
    String CREATE_VALUE = Ids.build(SYSTEM_PROPERTY, CrudConstants.CREATE, VALUE, Random.name());
    String READ_NAME = Ids.build(SYSTEM_PROPERTY, CrudConstants.READ, NAME, Random.name());
    String READ_VALUE = Ids.build(SYSTEM_PROPERTY, CrudConstants.READ, VALUE, Random.name());
    String UPDATE_NAME = Ids.build(SYSTEM_PROPERTY, CrudConstants.UPDATE, NAME, Random.name());
    String UPDATE_VALUE = Ids.build(SYSTEM_PROPERTY, CrudConstants.UPDATE, VALUE, Random.name());
    String DELETE_NAME = Ids.build(SYSTEM_PROPERTY, CrudConstants.DELETE, NAME, Random.name());
    String DELETE_VALUE = Ids.build(SYSTEM_PROPERTY, CrudConstants.DELETE, VALUE, Random.name());

    static Address systemPropertyAddress(String name) {
        return Address.of(SYSTEM_PROPERTY, name);
    }
}
