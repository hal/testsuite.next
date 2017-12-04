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

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public interface MessagingFixtures {

    String GLOBAL_MAX_SIZE = "global-client-scheduled-thread-pool-max-size";
    String CONNECTION_TTL_OVERRIDE = "connection-ttl-override";
    String PERSISTENCE_ENABLED = "persistence-enabled";
    String JMX_DOMAIN = "jmx-domain";
    String ELYTRON_DOMAIN = "elytron-domain";
    String APPLICATION_DOMAIN = "ApplicationDomain";
    String JOURNAL_BINDING_TABLE = "journal-bindings-table";
    String CLUSTER_CREDENTIAL_REFERENCE = "cluster-credential-reference";
    String BINDINGS_DIRECTORY = "bindings-directory";
    String JOURNAL_DIRECTORY = "journal-directory";
    String LARGE_MESSAGES_DIRECTORY = "large-messages-directory";
    String PAGING_DIRECTORY = "paging-directory";

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

    // ------------------------------------------------------ server / path

    static Address serverPathAddress(String server, String path) {
        return serverAddress(server).and(PATH, path);
    }

    // ------------------------------------------------------ server / destinations / core-queue

    String COREQUEUE_CREATE = Ids.build("core", "create", Random.name());
    String COREQUEUE_DELETE = Ids.build("core", "delete", Random.name());

    static Address coreQueueAddress(String server, String queue) {
        return serverAddress(server).and(QUEUE, queue);
    }

    // ------------------------------------------------------ server / destinations / jms-queue

    String JMSQUEUE_CREATE = Ids.build("jmsqueue", "create", Random.name());
    String JMSQUEUE_UPDATE = Ids.build("jmsqueue", "update", Random.name());
    String JMSQUEUE_DELETE = Ids.build("jmsqueue", "delete", Random.name());

    static Address jmsQueueAddress(String server, String queue) {
        return serverAddress(server).and(JMS_QUEUE, queue);
    }

    // ------------------------------------------------------ server / destinations / jms-topic

    String JMSTOPIC_CREATE = Ids.build("topic", "create", Random.name());
    String JMSTOPIC_UPDATE = Ids.build("topic", "update", Random.name());
    String JMSTOPIC_DELETE = Ids.build("topic", "delete", Random.name());

    static Address jmsTopicAddress(String server, String topic) {
        return serverAddress(server).and(JMS_TOPIC, topic);
    }

    // ------------------------------------------------------ server / destinations / security setting

    String SECSET_CREATE = Ids.build("sec-set", "create", Random.name());
    String SECSET_UPDATE = Ids.build("sec-set", "update", Random.name());
    String SECSET_DELETE = Ids.build("sec-set", "delete", Random.name());

    static Address securitySettingAddress(String server, String secsetting) {
        return serverAddress(server).and(SECURITY_SETTING, secsetting);
    }

    // ------------------------------------------------------ server / destinations / address setting

    String AS_CREATE = Ids.build("as", "create", Random.name());
    String AS_UPDATE = Ids.build("as", "update", Random.name());
    String AS_DELETE = Ids.build("as", "delete", Random.name());

    static Address addressSettingAddress(String server, String name) {
        return serverAddress(server).and(ADDRESS_SETTING, name);
    }

    // ------------------------------------------------------ server / destinations / divert

    String DIVERT_CREATE = Ids.build("divert", "create", Random.name());
    String DIVERT_UPDATE = Ids.build("divert", "update", Random.name());
    String DIVERT_DELETE = Ids.build("divert", "delete", Random.name());

    static Address divertAddress(String server, String name) {
        return serverAddress(server).and(DIVERT, name);
    }


}
