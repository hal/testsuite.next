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
package org.jboss.hal.testsuite.test.configuration.interfce;

import org.apache.commons.text.RandomStringGenerator;
import org.jboss.hal.resources.Ids;
import org.wildfly.extras.creaper.core.online.operations.Address;

public interface InterfaceFixtures {

    RandomStringGenerator GENERATOR = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

    String CREATE = Ids.build("interface", "create", GENERATOR.generate(10));
    String READ = Ids.build("interface", "read", GENERATOR.generate(10));
    String UPDATE = Ids.build("interface", "update", GENERATOR.generate(10));
    String DELETE = Ids.build("interface", "delete", GENERATOR.generate(10));

    static Address interfaceAddress(String name) {
        return Address.of("interface", name);
    }
}
