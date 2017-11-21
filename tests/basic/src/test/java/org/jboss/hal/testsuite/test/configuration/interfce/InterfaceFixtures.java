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

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.INTERFACE;

public interface InterfaceFixtures {

    String LOCALHOST = "127.0.0.1";

    String CREATE = Ids.build("ifc", "create", Random.name());
    String READ = Ids.build("ifc", "read", Random.name());
    String UPDATE = Ids.build("ifc", "update", Random.name());
    String DELETE = Ids.build("ifc", "delete", Random.name());

    static Address interfaceAddress(String name) {
        return Address.of(INTERFACE, name);
    }
}
