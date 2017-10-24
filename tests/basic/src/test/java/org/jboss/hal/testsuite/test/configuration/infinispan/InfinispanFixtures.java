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
package org.jboss.hal.testsuite.test.configuration.infinispan;

import org.apache.commons.text.RandomStringGenerator;
import org.jboss.hal.resources.Ids;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CACHE_CONTAINER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.INFINISPAN;

public interface InfinispanFixtures {

    Address SUBSYSTEM_ADDRESS = Address.subsystem(INFINISPAN);
    RandomStringGenerator GENERATOR = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

    // ------------------------------------------------------ cache container

    String CC_CREATE = Ids.build("cc", "create", GENERATOR.generate(10));
    String CC_READ = Ids.build("cc", "read", GENERATOR.generate(10));
    String CC_UPDATE = Ids.build("cc", "update", GENERATOR.generate(10));
    String CC_DELETE = Ids.build("cc", "update", GENERATOR.generate(10));

    static Address cacheContainerAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CACHE_CONTAINER, name);
    }
}
