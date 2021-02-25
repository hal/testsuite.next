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

import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.JPA;

public final class JpaFixtures {

    public static final String DEFAULT_EXTENDED_PERSISTENCE_INHERITANCE = "default-extended-persistence-inheritance";
    public static final String SHALLOW = "SHALLOW";
    public static final Address SUBSYSTEM_ADDRESS = Address.subsystem(JPA);

    private JpaFixtures() {
    }
}
