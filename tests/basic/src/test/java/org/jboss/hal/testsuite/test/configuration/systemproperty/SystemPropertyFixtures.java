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

import org.apache.commons.text.RandomStringGenerator;
import org.jboss.hal.resources.Ids;
import org.wildfly.extras.creaper.core.online.operations.Address;

public interface SystemPropertyFixtures {

    RandomStringGenerator GENERATOR = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

    String CREATE_NAME = Ids.build("system-property", "create", "name", GENERATOR.generate(10));
    String CREATE_VALUE = Ids.build("system-property", "create", "value", GENERATOR.generate(10));
    String READ_NAME = Ids.build("system-property", "read", "name", GENERATOR.generate(10));
    String READ_VALUE = Ids.build("system-property", "read", "value", GENERATOR.generate(10));
    String UPDATE_NAME = Ids.build("system-property", "update", "name", GENERATOR.generate(10));
    String UPDATE_VALUE = Ids.build("system-property", "update", "value", GENERATOR.generate(10));
    String DELETE_NAME = Ids.build("system-property", "delete", "name", GENERATOR.generate(10));
    String DELETE_VALUE = Ids.build("system-property", "delete", "value", GENERATOR.generate(10));

    static Address systemPropertyAddress(String name) {
        return Address.of("system-property", name);
    }
}
