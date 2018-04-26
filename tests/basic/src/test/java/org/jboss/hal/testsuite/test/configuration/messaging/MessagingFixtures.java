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

import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public final class MessagingFixtures {

    private static final String SERVER_PREFIX = "srv";
    private static final String CORE_PREFIX = "core";
    private static final String JMS_QUEUE_PREFIX = "jmsqueue";
    private static final String JMS_TOPIC_PREFIX = "topic";
    private static final String SECURITY_SETTINGS_PREFIX = "sec-set";
    private static final String ADDRESS_SETTINGS_PREFIX = "as";
    private static final String DIVERT_PREFIX = "divert";
    private static final String ACCEPTOR_GENERIC_PREFIX = "acceptor-gen";
    private static final String ACCEPTOR_IN_VM_PREFIX = "acceptor-invm";
    private static final String ACCEPTOR_HTTP_PREFIX = "acceptor-http";
    private static final String ACCEPTOR_REMOTE_PREFIX = "acceptor-rem";
    private static final String CONNECTOR_GENERIC_PREFIX = "connector-gen";

    private static final String TRY_UPDATE = "try-update";
    private static final String CONNECTOR_INVM_PREFIX = "connector-invm";
    private static final String CONNECTOR_HTTP_PREFIX = "connector-http";
    private static final String CONNECTOR_REMOTE_PREFIX = "connector-rem";
    private static final String CONNECTOR_SERVICE_PREFIX = "connector-svc";
    private static final String CONNECTOR_FACTORY_PREFIX = "connector-fac";
    private static final String POOLED_CONNECTION_FACTORY = "pcf";
    private static final String BROADCAST_GROUP_PREFIX = "bg";
    private static final String DISCOVERY_GROUP_PREFIX = "dg";
    private static final String CLUSTER_CONNECTION_PREFIX = "cc";
    private static final String GROUPING_HANDLER = "gh";
    private static final String BRIDGE_PREFIX = "bridge";
    private static final String JMSBRIDGE_PREFIX = "jmsbridge";

    static final String APPLICATION_DOMAIN = "ApplicationDomain";
    static final String AT_MOST_ONCE = "AT_MOST_ONCE";
    static final String BINDINGS_DIRECTORY = "bindings-directory";
    static final String BROADCAST_PERIOD = "broadcast-period";
    static final String CALL_TIMEOUT = "call-timeout";
    static final String CHECK_PERIOD = "check-period";
    static final String CLUSTER_CONNECTION_ADDRESS = "cluster-connection-address";
    static final String CLUSTER_CREDENTIAL_REFERENCE = "cluster-credential-reference";
    static final String CLUSTER_NAME =  "cluster-name";
    static final String CONNECTION_FACTORY_VALUE = "ConnectionFactory";
    static final String CONNECTION_TTL_OVERRIDE = "connection-ttl-override";
    static final String CONSUME = "consume";
    static final String DESTINATION_QUEUE = "jms/queue/DLQ";
    static final String DIVERT_ADDRESS = "divert-address";
    static final String ELYTRON_DOMAIN = "elytron-domain";
    static final String FACTORY_CLASS = "factory-class";
    static final String FAILOVER_ON_SERVER_SHUTDOWN =  "failover-on-server-shutdown";
    static final String FAILURE_RETRY_INTERVAL = "failure-retry-interval";
    static final String FORWARDING_ADDRESS = "forwarding-address";
    static final String GLOBAL_MAX_SIZE = "global-client-scheduled-thread-pool-max-size";
    static final String GROUPING_HANDLER_ADDRESS = "grouping-handler-address";
    static final String JGROUPS_CHANNEL = "jgroups-channel";
    static final String JGROUPS_CLUSTER = "jgroups-cluster";
    static final String JMX_DOMAIN = "jmx-domain";
    static final String JOURNAL_BINDING_TABLE = "journal-bindings-table";
    static final String JOURNAL_DIRECTORY = "journal-directory";
    static final String LARGE_MESSAGES_DIRECTORY = "large-messages-directory";
    static final String MAX_BACKUPS =  "max-backups";
    static final String MAX_BATCH_SIZE = "max-batch-size";
    static final String MAX_BATCH_TIME = "max-batch-time";
    static final String MAX_RETRIES = "max-retries";
    static final String MESSAGING_SECURITY_SETTING_ROLE = "messaging-security-setting-role";
    static final String PAGING_DIRECTORY = "paging-directory";
    static final String PERSISTENCE_ENABLED = "persistence-enabled";
    static final String QUALITY_OF_SERVICE = "quality-of-service";
    static final String REFRESH_TIMEOUT = "refresh-timeout";
    static final String REMOTE_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    static final String SCALE_DOWN_CLUSTER_NAME =  "scale-down-cluster-name";
    static final String SERVER_ID = "server-id";
    static final String SERVER_NAME = "server-name";
    static final String SOURCE_CONNECTION_FACTORY = "source-connection-factory";
    static final String SOURCE_DESTINATION = "source-destination";
    static final String TARGET_CONNECTION_FACTORY = "target-connection-factory";
    static final String TARGET_CONTEXT = "target-context";
    static final String TARGET_DESTINATION = "target-destination";
    static final String UPGRADE_LEGACY = "upgrade-legacy";

    static final Address SUBSYSTEM_ADDRESS = Address.subsystem(MESSAGING_ACTIVEMQ);

    // ------------------------------------------------------ server

    static final String SRV_CREATE = Ids.build(SERVER_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SRV_CREATE2 = Ids.build(SERVER_PREFIX, "create2", Random.name());
    static final String SRV_READ = Ids.build(SERVER_PREFIX, CrudConstants.READ, Random.name());
    static final String SRV_UPDATE = Ids.build(SERVER_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SRV_DELETE = Ids.build(SERVER_PREFIX, CrudConstants.DELETE, Random.name());

    static Address serverAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVER, name);
    }

    // ------------------------------------------------------ server / path

    static Address serverPathAddress(String server, String path) {
        return serverAddress(server).and(PATH, path);
    }

    // ------------------------------------------------------ server / destinations / core-queue

    static final String COREQUEUE_CREATE = Ids.build(CORE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String COREQUEUE_DELETE = Ids.build(CORE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address coreQueueAddress(String server, String queue) {
        return serverAddress(server).and(QUEUE, queue);
    }

    // ------------------------------------------------------ server / destinations / jms-queue

    static final String JMSQUEUE_CREATE = Ids.build(JMS_QUEUE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String JMSQUEUE_UPDATE = Ids.build(JMS_QUEUE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String JMSQUEUE_DELETE = Ids.build(JMS_QUEUE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address jmsQueueAddress(String server, String queue) {
        return serverAddress(server).and(JMS_QUEUE, queue);
    }

    // ------------------------------------------------------ server / destinations / jms-topic

    static final String JMSTOPIC_CREATE = Ids.build(JMS_TOPIC_PREFIX, CrudConstants.CREATE, Random.name());
    static final String JMSTOPIC_UPDATE = Ids.build(JMS_TOPIC_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String JMSTOPIC_DELETE = Ids.build(JMS_TOPIC_PREFIX, CrudConstants.DELETE, Random.name());

    static Address jmsTopicAddress(String server, String topic) {
        return serverAddress(server).and(JMS_TOPIC, topic);
    }

    // ------------------------------------------------------ server / destinations / security setting

    static final String SECSET_CREATE = Ids.build(SECURITY_SETTINGS_PREFIX, CrudConstants.CREATE, Random.name());
    static final String SECSET_UPDATE = Ids.build(SECURITY_SETTINGS_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String SECSET_DELETE = Ids.build(SECURITY_SETTINGS_PREFIX, CrudConstants.DELETE, Random.name());

    static Address securitySettingAddress(String server, String secsetting) {
        return serverAddress(server).and(SECURITY_SETTING, secsetting);
    }

    static final String ROLE_CREATE = Ids.build("role", CrudConstants.CREATE, Random.name());

    static Address securitySettingRoleAddress(String server, String secsetting, String role) {
        return securitySettingAddress(server, secsetting).and(ROLE, role);
    }

    // ------------------------------------------------------ server / destinations / address setting

    static final String AS_CREATE = Ids.build(ADDRESS_SETTINGS_PREFIX, CrudConstants.CREATE, Random.name());
    static final String AS_UPDATE = Ids.build(ADDRESS_SETTINGS_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String AS_DELETE = Ids.build(ADDRESS_SETTINGS_PREFIX, CrudConstants.DELETE, Random.name());

    static Address addressSettingAddress(String server, String name) {
        return serverAddress(server).and(ADDRESS_SETTING, name);
    }

    // ------------------------------------------------------ server / destinations / divert

    static final String DIVERT_CREATE = Ids.build(DIVERT_PREFIX, CrudConstants.CREATE, Random.name());
    static final String DIVERT_UPDATE = Ids.build(DIVERT_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String DIVERT_DELETE = Ids.build(DIVERT_PREFIX, CrudConstants.DELETE, Random.name());

    static Address divertAddress(String server, String name) {
        return serverAddress(server).and(DIVERT, name);
    }

    // ------------------------------------------------------ server / connections / acceptor - generic

    static final String ACCP_GEN_CREATE = Ids.build(ACCEPTOR_GENERIC_PREFIX, CrudConstants.CREATE, Random.name());
    static final String ACCP_GEN_UPDATE = Ids.build(ACCEPTOR_GENERIC_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String ACCP_GEN_TRY_UPDATE = Ids.build(ACCEPTOR_GENERIC_PREFIX, TRY_UPDATE, Random.name());
    static final String ACCP_GEN_DELETE = Ids.build(ACCEPTOR_GENERIC_PREFIX, CrudConstants.DELETE, Random.name());

    static Address acceptorGenericAddress(String server, String name) {
        return serverAddress(server).and(ACCEPTOR, name);
    }

    // ------------------------------------------------------ server / connections / acceptor - in-vm

    static final String ACCP_INVM_CREATE = Ids.build(ACCEPTOR_IN_VM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String ACCP_INVM_UPDATE = Ids.build(ACCEPTOR_IN_VM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String ACCP_INVM_TRY_UPDATE = Ids.build(ACCEPTOR_IN_VM_PREFIX, TRY_UPDATE, Random.name());
    static final String ACCP_INVM_DELETE = Ids.build(ACCEPTOR_IN_VM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address acceptorInVMAddress(String server, String name) {
        return serverAddress(server).and(IN_VM_ACCEPTOR, name);
    }

    // ------------------------------------------------------ server / connections / acceptor - http

    static final String ACCP_HTTP_CREATE = Ids.build(ACCEPTOR_HTTP_PREFIX, CrudConstants.CREATE, Random.name());
    static final String ACCP_HTTP_UPDATE = Ids.build(ACCEPTOR_HTTP_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String ACCP_HTTP_DELETE = Ids.build(ACCEPTOR_HTTP_PREFIX, CrudConstants.DELETE, Random.name());

    static Address acceptorHttpAddress(String server, String name) {
        return serverAddress(server).and(HTTP_ACCEPTOR, name);
    }

    // ------------------------------------------------------ server / connections / acceptor - remote

    static final String ACCP_REM_CREATE = Ids.build(ACCEPTOR_REMOTE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String ACCP_REM_UPDATE = Ids.build(ACCEPTOR_REMOTE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String ACCP_REM_TRY_UPDATE = Ids.build(ACCEPTOR_REMOTE_PREFIX, TRY_UPDATE, Random.name());
    static final String ACCP_REM_DELETE = Ids.build(ACCEPTOR_REMOTE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address acceptorRemoteAddress(String server, String name) {
        return serverAddress(server).and(REMOTE_ACCEPTOR, name);
    }


    // ------------------------------------------------------ server / connections / connector - generic

    static final String CONN_GEN_CREATE = Ids.build(CONNECTOR_GENERIC_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CONN_GEN_UPDATE = Ids.build(CONNECTOR_GENERIC_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CONN_GEN_DELETE = Ids.build(CONNECTOR_GENERIC_PREFIX, CrudConstants.DELETE, Random.name());

    static Address connectorGenericAddress(String server, String name) {
        return serverAddress(server).and(CONNECTOR, name);
    }

    // ------------------------------------------------------ server / connections / connector - in-vm

    static final String CONN_INVM_CREATE = Ids.build(CONNECTOR_INVM_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CONN_INVM_UPDATE = Ids.build(CONNECTOR_INVM_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CONN_INVM_TRY_UPDATE = Ids.build(CONNECTOR_INVM_PREFIX, TRY_UPDATE, Random.name());
    static final String CONN_INVM_DELETE = Ids.build(CONNECTOR_INVM_PREFIX, CrudConstants.DELETE, Random.name());

    static Address connectorInVMAddress(String server, String name) {
        return serverAddress(server).and(IN_VM_CONNECTOR, name);
    }

    // ------------------------------------------------------ server / connections / connector - http

    static final String CONN_HTTP_CREATE = Ids.build(CONNECTOR_HTTP_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CONN_HTTP_UPDATE = Ids.build(CONNECTOR_HTTP_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CONN_HTTP_TRY_UPDATE = Ids.build(CONNECTOR_HTTP_PREFIX, TRY_UPDATE, Random.name());
    static final String CONN_HTTP_DELETE = Ids.build(CONNECTOR_HTTP_PREFIX, CrudConstants.DELETE, Random.name());

    static Address connectorHttpAddress(String server, String name) {
        return serverAddress(server).and(HTTP_CONNECTOR, name);
    }

    // ------------------------------------------------------ server / connections / connector - remote

    static final String CONN_REM_CREATE = Ids.build(CONNECTOR_REMOTE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CONN_REM_UPDATE = Ids.build(CONNECTOR_REMOTE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CONN_REM_DELETE = Ids.build(CONNECTOR_REMOTE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address connectorRemoteAddress(String server, String name) {
        return serverAddress(server).and(REMOTE_CONNECTOR, name);
    }

    // ------------------------------------------------------ server / connections / connector service

    static final String CONN_SVC_CREATE = Ids.build(CONNECTOR_SERVICE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CONN_SVC_UPDATE = Ids.build(CONNECTOR_SERVICE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CONN_SVC_DELETE = Ids.build(CONNECTOR_SERVICE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address connectorServiceAddress(String server, String name) {
        return serverAddress(server).and(CONNECTOR_SERVICE, name);
    }

    // ------------------------------------------------------ server / connections / connector factory

    static final String CONN_FAC_CREATE = Ids.build(CONNECTOR_FACTORY_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CONN_FAC_CREATE_ENTRY = Ids.build(CONNECTOR_FACTORY_PREFIX, "create-entry", Random.name());
    static final String CONN_FAC_UPDATE = Ids.build(CONNECTOR_FACTORY_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CONN_FAC_TRY_UPDATE = Ids.build(CONNECTOR_FACTORY_PREFIX, TRY_UPDATE, Random.name());
    static final String CONN_FAC_DELETE = Ids.build(CONNECTOR_FACTORY_PREFIX, CrudConstants.DELETE, Random.name());

    static Address connectionFactoryAddress(String server, String name) {
        return serverAddress(server).and(CONNECTION_FACTORY, name);
    }

    // ------------------------------------------------------ server / connections / pooled connection factory

    static final String POOL_CONN_CREATE = Ids.build(POOLED_CONNECTION_FACTORY, CrudConstants.CREATE, Random.name());
    static final String POOL_CONN_CREATE_ENTRY = Ids.build(POOLED_CONNECTION_FACTORY, "-entry", Random.name());
    static final String POOL_CONN_UPDATE = Ids.build(POOLED_CONNECTION_FACTORY, CrudConstants.UPDATE, Random.name());
    static final String POOL_CONN_TRY_UPDATE = Ids.build(POOLED_CONNECTION_FACTORY, TRY_UPDATE, Random.name());
    static final String POOL_CONN_DELETE = Ids.build(POOLED_CONNECTION_FACTORY, CrudConstants.DELETE, Random.name());

    static Address pooledConnectionFactoryAddress(String server, String name) {
        return serverAddress(server).and(ModelDescriptionConstants.POOLED_CONNECTION_FACTORY, name);
    }

    // ------------------------------------------------------ server / broadcast-group

    static final String BG_CREATE = Ids.build(BROADCAST_GROUP_PREFIX, CrudConstants.CREATE, Random.name());
    static final String BG_UPDATE = Ids.build(BROADCAST_GROUP_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String BG_DELETE = Ids.build(BROADCAST_GROUP_PREFIX, CrudConstants.DELETE, Random.name());

    static Address broadcastGroupAddress(String server, String name) {
        return serverAddress(server).and(BROADCAST_GROUP, name);
    }

    // ------------------------------------------------------ server / discovery-group

    static final String DG_CREATE = Ids.build(DISCOVERY_GROUP_PREFIX, CrudConstants.CREATE, Random.name());
    static final String DG_UPDATE = Ids.build(DISCOVERY_GROUP_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String DG_UPDATE_ALTERNATIVES = Ids.build(DISCOVERY_GROUP_PREFIX, "update-alternatives", Random.name());
    static final String DG_DELETE = Ids.build(DISCOVERY_GROUP_PREFIX, CrudConstants.DELETE, Random.name());

    static Address discoveryGroupAddress(String server, String name) {
        return serverAddress(server).and(DISCOVERY_GROUP, name);
    }

    // ------------------------------------------------------ server / cluster-connection

    static final String CC_CREATE = Ids.build(CLUSTER_CONNECTION_PREFIX, CrudConstants.CREATE, Random.name());
    static final String CC_UPDATE = Ids.build(CLUSTER_CONNECTION_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String CC_UPDATE_ALTERNATIVES = Ids.build(CLUSTER_CONNECTION_PREFIX, "update-alternatives", Random.name());
    static final String CC_DELETE = Ids.build(CLUSTER_CONNECTION_PREFIX, CrudConstants.DELETE, Random.name());

    static Address clusterConnectionAddress(String server, String name) {
        return serverAddress(server).and(CLUSTER_CONNECTION, name);
    }

    // ------------------------------------------------------ server / grouping-handler

    static final String GH_CREATE = Ids.build(GROUPING_HANDLER, CrudConstants.CREATE, Random.name());
    static final String GH_UPDATE = Ids.build(GROUPING_HANDLER, CrudConstants.UPDATE, Random.name());
    static final String GH_DELETE = Ids.build(GROUPING_HANDLER, CrudConstants.DELETE, Random.name());

    static Address groupingHandlerAddress(String server, String name) {
        return serverAddress(server).and(ModelDescriptionConstants.GROUPING_HANDLER, name);
    }

    // ------------------------------------------------------ server / bridge

    static final String BRIDGE_CREATE = Ids.build(BRIDGE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String BRIDGE_UPDATE = Ids.build(BRIDGE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String BRIDGE_DELETE = Ids.build(BRIDGE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address bridgeAddress(String server, String name) {
        return serverAddress(server).and(BRIDGE, name);
    }

    // ------------------------------------------------------ server / ha-policy

    static Address haPolicyAddress(String server, String name) {
        return serverAddress(server).and(HA_POLICY, name);
    }

    // ------------------------------------------------------ jms-bridge

    static final String JMSBRIDGE_CREATE = Ids.build(JMSBRIDGE_PREFIX, CrudConstants.CREATE, Random.name());
    static final String JMSBRIDGE_CREATE2 = Ids.build(JMSBRIDGE_PREFIX, "create2", Random.name());
    static final String JMSBRIDGE_UPDATE = Ids.build(JMSBRIDGE_PREFIX, CrudConstants.UPDATE, Random.name());
    static final String JMSBRIDGE_DELETE = Ids.build(JMSBRIDGE_PREFIX, CrudConstants.DELETE, Random.name());

    static Address jmsBridgeAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(JMS_BRIDGE, name);
    }

    private MessagingFixtures() {
    }
}
