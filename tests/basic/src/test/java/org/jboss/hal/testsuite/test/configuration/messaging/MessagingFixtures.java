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
    String CONSUME = "consume";
    String DIVERT_ADDRESS = "divert-address";
    String FORWARDING_ADDRESS = "forwarding-address";
    String FACTORY_CLASS = "factory-class";
    String SERVER_ID = "server-id";
    String UPGRADE_LEGACY = "upgrade-legacy";
    String SERVER_NAME = "server-name";
    String CALL_TIMEOUT = "call-timeout";
    String BROADCAST_PERIOD = "broadcast-period";
    String JGROUPS_CHANNEL = "jgroups-channel";
    String REFRESH_TIMEOUT = "refresh-timeout";
    String CLUSTER_CONNECTION_ADDRESS = "cluster-connection-address";
    String GROUPING_HANDLER_ADDRESS = "grouping-handler-address";
    String CHECK_PERIOD = "check-period";
    String SCALE_DOWN_CLUSTER_NAME =  "scale-down-cluster-name";
    String CLUSTER_NAME =  "cluster-name";
    String MAX_BACKUPS =  "max-backups";
    String FAILOVER_ON_SERVER_SHUTDOWN =  "failover-on-server-shutdown";

    String QUALITY_OF_SERVICE = "quality-of-service";
    String AT_MOST_ONCE = "AT_MOST_ONCE";
    String FAILURE_RETRY_INTERVAL = "failure-retry-interval";
    String MAX_RETRIES = "max-retries";
    String MAX_BATCH_SIZE = "max-batch-size";
    String MAX_BATCH_TIME = "max-batch-time";
    String TARGET_CONTEXT = "target-context";
    String SOURCE_CONNECTION_FACTORY = "source-connection-factory";
    String CONNECTION_FACTORY_VALUE = "ConnectionFactory";
    String SOURCE_DESTINATION = "source-destination";
    String DESTINATION_QUEUE = "jms/queue/DLQ";
    String TARGET_CONNECTION_FACTORY = "target-connection-factory";
    String REMOTE_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    String TARGET_DESTINATION = "target-destination";


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

    String ROLE_CREATE = Ids.build("role", "create", Random.name());

    static Address securitySettingRoleAddress(String server, String secsetting, String role) {
        return securitySettingAddress(server, secsetting).and(ROLE, role);
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

    // ------------------------------------------------------ server / connections / acceptor - generic

    String ACCP_GEN_CREATE = Ids.build("acceptor-gen", "create", Random.name());
    String ACCP_GEN_UPDATE = Ids.build("acceptor-gen", "update", Random.name());
    String ACCP_GEN_DELETE = Ids.build("acceptor-gen", "delete", Random.name());

    static Address acceptorGenericAddress(String server, String name) {
        return serverAddress(server).and(ACCEPTOR, name);
    }

    // ------------------------------------------------------ server / connections / acceptor - in-vm

    String ACCP_INVM_CREATE = Ids.build("acceptor-invm", "create", Random.name());
    String ACCP_INVM_UPDATE = Ids.build("acceptor-invm", "update", Random.name());
    String ACCP_INVM_DELETE = Ids.build("acceptor-invm", "delete", Random.name());

    static Address acceptorInVMAddress(String server, String name) {
        return serverAddress(server).and(IN_VM_ACCEPTOR, name);
    }

    // ------------------------------------------------------ server / connections / acceptor - http

    String ACCP_HTTP_CREATE = Ids.build("acceptor-http", "create", Random.name());
    String ACCP_HTTP_UPDATE = Ids.build("acceptor-http", "update", Random.name());
    String ACCP_HTTP_DELETE = Ids.build("acceptor-http", "delete", Random.name());

    static Address acceptorHttpAddress(String server, String name) {
        return serverAddress(server).and(HTTP_ACCEPTOR, name);
    }

    // ------------------------------------------------------ server / connections / acceptor - remote

    String ACCP_REM_CREATE = Ids.build("acceptor-rem", "create", Random.name());
    String ACCP_REM_UPDATE = Ids.build("acceptor-rem", "update", Random.name());
    String ACCP_REM_DELETE = Ids.build("acceptor-rem", "delete", Random.name());

    static Address acceptorRemoteAddress(String server, String name) {
        return serverAddress(server).and(REMOTE_ACCEPTOR, name);
    }

    // ------------------------------------------------------ server / connections / connector - generic

    String CONN_GEN_CREATE = Ids.build("connector-gen", "create", Random.name());
    String CONN_GEN_UPDATE = Ids.build("connector-gen", "update", Random.name());
    String CONN_GEN_DELETE = Ids.build("connector-gen", "delete", Random.name());

    static Address connectorGenericAddress(String server, String name) {
        return serverAddress(server).and(CONNECTOR, name);
    }

    // ------------------------------------------------------ server / connections / connector - in-vm

    String CONN_INVM_CREATE = Ids.build("connector-invm", "create", Random.name());
    String CONN_INVM_UPDATE = Ids.build("connector-invm", "update", Random.name());
    String CONN_INVM_DELETE = Ids.build("connector-invm", "delete", Random.name());

    static Address connectorInVMAddress(String server, String name) {
        return serverAddress(server).and(IN_VM_CONNECTOR, name);
    }

    // ------------------------------------------------------ server / connections / connector - http

    String CONN_HTTP_CREATE = Ids.build("connector-http", "create", Random.name());
    String CONN_HTTP_UPDATE = Ids.build("connector-http", "update", Random.name());
    String CONN_HTTP_DELETE = Ids.build("connector-http", "delete", Random.name());

    static Address connectorHttpAddress(String server, String name) {
        return serverAddress(server).and(HTTP_CONNECTOR, name);
    }

    // ------------------------------------------------------ server / connections / connector - remote

    String CONN_REM_CREATE = Ids.build("connector-rem", "create", Random.name());
    String CONN_REM_UPDATE = Ids.build("connector-rem", "update", Random.name());
    String CONN_REM_DELETE = Ids.build("connector-rem", "delete", Random.name());

    static Address connectorRemoteAddress(String server, String name) {
        return serverAddress(server).and(REMOTE_CONNECTOR, name);
    }

    // ------------------------------------------------------ server / connections / connector service

    String CONN_SVC_CREATE = Ids.build("connector-svc", "create", Random.name());
    String CONN_SVC_UPDATE = Ids.build("connector-svc", "update", Random.name());
    String CONN_SVC_DELETE = Ids.build("connector-svc", "delete", Random.name());

    static Address connectorServiceAddress(String server, String name) {
        return serverAddress(server).and(CONNECTOR_SERVICE, name);
    }

    // ------------------------------------------------------ server / connections / connector factory

    String CONN_FAC_CREATE = Ids.build("connector-fac", "create", Random.name());
    String CONN_FAC_CREATE_ENTRY = Ids.build("connector-fac", "create-entry", Random.name());
    String CONN_FAC_UPDATE = Ids.build("connector-fac", "update", Random.name());
    String CONN_FAC_DELETE = Ids.build("connector-fac", "delete", Random.name());

    static Address connectionFactoryAddress(String server, String name) {
        return serverAddress(server).and(CONNECTION_FACTORY, name);
    }

    // ------------------------------------------------------ server / connections / pooled connection factory

    String POOL_CONN_CREATE = Ids.build("pcf", "create", Random.name());
    String POOL_CONN_CREATE_ENTRY = Ids.build("pcf", "-entry", Random.name());
    String POOL_CONN_UPDATE = Ids.build("pcf", "update", Random.name());
    String POOL_CONN_DELETE = Ids.build("pcf", "delete", Random.name());

    static Address pooledConnectionFactoryAddress(String server, String name) {
        return serverAddress(server).and(POOLED_CONNECTION_FACTORY, name);
    }

    // ------------------------------------------------------ server / broadcast-group

    String BG_CREATE = Ids.build("bg", "create", Random.name());
    String BG_UPDATE = Ids.build("bg", "update", Random.name());
    String BG_DELETE = Ids.build("bg", "delete", Random.name());

    static Address broadcastGroupAddress(String server, String name) {
        return serverAddress(server).and(BROADCAST_GROUP, name);
    }

    // ------------------------------------------------------ server / discovery-group

    String DG_CREATE = Ids.build("dg", "create", Random.name());
    String DG_UPDATE = Ids.build("dg", "update", Random.name());
    String DG_UPDATE_ALTERNATIVES = Ids.build("dg", "update-alternatives", Random.name());
    String DG_DELETE = Ids.build("dg", "delete", Random.name());

    static Address discoveryGroupAddress(String server, String name) {
        return serverAddress(server).and(DISCOVERY_GROUP, name);
    }

    // ------------------------------------------------------ server / cluster-connection

    String CC_CREATE = Ids.build("cc", "create", Random.name());
    String CC_UPDATE = Ids.build("cc", "update", Random.name());
    String CC_UPDATE_ALTERNATIVES = Ids.build("cc", "update-alternatives", Random.name());
    String CC_DELETE = Ids.build("cc", "delete", Random.name());

    static Address clusterConnectionAddress(String server, String name) {
        return serverAddress(server).and(CLUSTER_CONNECTION, name);
    }

    // ------------------------------------------------------ server / grouping-handler

    String GH_CREATE = Ids.build("gh", "create", Random.name());
    String GH_UPDATE = Ids.build("gh", "update", Random.name());
    String GH_DELETE = Ids.build("gh", "delete", Random.name());

    static Address groupingHandlerAddress(String server, String name) {
        return serverAddress(server).and(GROUPING_HANDLER, name);
    }

    // ------------------------------------------------------ server / bridge

    String BRIDGE_CREATE = Ids.build("bridge", "create", Random.name());
    String BRIDGE_UPDATE = Ids.build("bridge", "update", Random.name());
    String BRIDGE_DELETE = Ids.build("bridge", "delete", Random.name());

    static Address bridgeAddress(String server, String name) {
        return serverAddress(server).and(BRIDGE, name);
    }

    // ------------------------------------------------------ server / ha-policy

    static Address haPolicyAddress(String server, String name) {
        return serverAddress(server).and(HA_POLICY, name);
    }

    // ------------------------------------------------------ jms-bridge

    String JMSBRIDGE_CREATE = Ids.build("jmsbridge", "create", Random.name());
    String JMSBRIDGE_CREATE2 = Ids.build("jmsbridge", "create2", Random.name());
    String JMSBRIDGE_UPDATE = Ids.build("jmsbridge", "update", Random.name());
    String JMSBRIDGE_DELETE = Ids.build("jmsbridge", "delete", Random.name());

    static Address jmsBridgeAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(JMS_BRIDGE, name);
    }
}
