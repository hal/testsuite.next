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
package org.jboss.hal.testsuite.test.configuration.messaging;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MESSAGING_ACTIVEMQ;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;

public interface MessagingFixtures {

    String GLOBAL_MAX_SIZE = "global-client-scheduled-thread-pool-max-size";
    String CONNECTION_TTL_OVERRIDE = "connection-ttl-override";
    String PERSISTENCE_ENABLED = "persistence-enabled";
    String JMX_DOMAIN = "jmx-domain";
    String ELYTRON_DOMAIN = "elytron-domain";
    String APPLICATION_DOMAIN = "ApplicationDomain";
    String JOURNAL_BINDING_TABLE = "journal-bindings-table";
    String CLUSTER_CREDENTIAL_REFERENCE = "cluster-credential-reference";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(MESSAGING_ACTIVEMQ);

    // ------------------------------------------------------ server

    String SRV_CREATE = Ids.build("srv", "create", Random.name());
    String SRV_CREATE2 = Ids.build("srv", "create2", Random.name());
    String SRV_READ = Ids.build("srv", "read", Random.name());
    String SRV_UPDATE = Ids.build("srv", "update", Random.name());
    String SRV_DELETE = Ids.build("srv", "delete", Random.name());

    static Address serverAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVER, name);
    }



}
