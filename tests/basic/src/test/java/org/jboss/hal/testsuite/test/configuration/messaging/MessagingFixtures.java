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

    private static final String ACCEPTOR_GENERIC_PREFIX = "acceptor-gen";
    private static final String ACCEPTOR_HTTP_PREFIX = "acceptor-http";
    private static final String ACCEPTOR_IN_VM_PREFIX = "acceptor-invm";
    private static final String ACCEPTOR_REMOTE_PREFIX = "acceptor-rem";
    private static final String ADDRESS_SETTINGS_PREFIX = "as";
    private static final String BRIDGE_PREFIX = "bridge";
    private static final String BROADCAST_GROUP_PREFIX = "bg";
    private static final String CLUSTER_CONNECTION_PREFIX = "cc";
    private static final String CONNECTOR_FACTORY_PREFIX = "connector-fac";
    private static final String CONNECTOR_GENERIC_PREFIX = "connector-gen";
    private static final String CONNECTOR_HTTP_PREFIX = "connector-http";
    private static final String CONNECTOR_INVM_PREFIX = "connector-invm";
    private static final String CONNECTOR_REMOTE_PREFIX = "connector-rem";
    private static final String CONNECTOR_SERVICE_PREFIX = "connector-svc";
    private static final String CORE_PREFIX = "core";
    private static final String DISCOVERY_GROUP_PREFIX = "dg";
    private static final String DIVERT_PREFIX = "divert";
    private static final String GROUPING_HANDLER = "gh";
    private static final String JMS_QUEUE_PREFIX = "jmsqueue";
    private static final String JMS_TOPIC_PREFIX = "topic";
    private static final String JMSBRIDGE_PREFIX = "jmsbridge";
    private static final String POOLED_CONNECTION_FACTORY = "pcf";
    private static final String SECURITY_SETTINGS_PREFIX = "sec-set";
    private static final String SERVER_PREFIX = "srv";
    private static final String TRY_UPDATE = "try-update";

    public static final String APPLICATION_DOMAIN = "ApplicationDomain";
    public static final String AT_MOST_ONCE = "AT_MOST_ONCE";
    public static final String BINDINGS_DIRECTORY = "bindings-directory";
    public static final String BROADCAST_PERIOD = "broadcast-period";
    public static final String CALL_TIMEOUT = "call-timeout";
    public static final String CHECK_PERIOD = "check-period";
    public static final String CLUSTER_CONNECTION_ADDRESS = "cluster-connection-address";
    public static final String CLUSTER_CREDENTIAL_REFERENCE = "cluster-credential-reference";
    public static final String CONNECTION_FACTORY_VALUE = "ConnectionFactory";
    public static final String CONNECTION_TTL_OVERRIDE = "connection-ttl-override";
    public static final String CONNECTOR_FACTORY_CLASS = "org.apache.activemq.artemis.utils.ClassloadingUtil";
    public static final String CONSUME = "consume";
    public static final String DESTINATION_QUEUE = "jms/queue/DLQ";
    public static final String DIVERT_ADDRESS = "divert-address";
    public static final String ELYTRON_DOMAIN = "elytron-domain";
    public static final String FACTORY_CLASS = "factory-class";
    public static final String FAILURE_RETRY_INTERVAL = "failure-retry-interval";
    public static final String FORWARDING_ADDRESS = "forwarding-address";
    public static final String GLOBAL_MAX_SIZE = "global-client-scheduled-thread-pool-max-size";
    public static final String GROUPING_HANDLER_ADDRESS = "grouping-handler-address";
    public static final String JGROUPS_CHANNEL = "jgroups-channel";
    public static final String JGROUPS_CLUSTER = "jgroups-cluster";
    public static final String JMX_DOMAIN = "jmx-domain";
    public static final String JOURNAL_BINDING_TABLE = "journal-bindings-table";
    public static final String JOURNAL_DIRECTORY = "journal-directory";
    public static final String JOURNAL_FILE_OPEN_TIMEOUT = "journal-file-open-timeout";
    public static final String LARGE_MESSAGES_DIRECTORY = "large-messages-directory";
    public static final String MAX_BATCH_SIZE = "max-batch-size";
    public static final String MAX_BATCH_TIME = "max-batch-time";
    public static final String MESSAGING_SECURITY_SETTING_ROLE = "messaging-security-setting-role";
    public static final String PAGING_DIRECTORY = "paging-directory";
    public static final String PATH_BINDING_DIRECTORY = "path-bindings-directory";
    public static final String PATH_JOURNAL_DIRECTORY = "path-journal-directory";
    public static final String PATH_LARGE_MESSAGES_DIRECTORY = "path-large-messages-directory";
    public static final String PATH_PAGING_DIRECTORY = "path-paging-directory";
    public static final String PERSISTENCE_ENABLED = "persistence-enabled";
    public static final String QUALITY_OF_SERVICE = "quality-of-service";
    public static final String REFRESH_TIMEOUT = "refresh-timeout";
    public static final String REMOTE_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    public static final String SERVER_ID = "server-id";
    public static final String SERVER_NAME = "server-name";
    public static final String SOURCE_CONNECTION_FACTORY = "source-connection-factory";
    public static final String SOURCE_DESTINATION = "source-destination";
    public static final String TARGET_CONNECTION_FACTORY = "target-connection-factory";
    public static final String TARGET_CONTEXT = "target-context";
    public static final String TARGET_DESTINATION = "target-destination";
    public static final String UPGRADE_LEGACY = "upgrade-legacy";

    public static final Address SUBSYSTEM_ADDRESS = Address.subsystem(MESSAGING_ACTIVEMQ);

    // ------------------------------------------------------ server

    public static final String SRV_CREATE = Ids.build(SERVER_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String SRV_CREATE2 = Ids.build(SERVER_PREFIX, "create2", Random.name());
    public static final String SRV_READ = Ids.build(SERVER_PREFIX, CrudConstants.READ, Random.name());
    public static final String SRV_UPDATE = Ids.build(SERVER_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String SRV_DELETE = Ids.build(SERVER_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address serverAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(SERVER, name);
    }


    // ------------------------------------------------------ server / path

    public static Address serverPathAddress(String server, String path) {
        return serverAddress(server).and(PATH, path);
    }


    // ------------------------------------------------------ server / destinations / core-queue

    public static final String COREQUEUE_CREATE = Ids.build(CORE_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String COREQUEUE_DELETE = Ids.build(CORE_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address coreQueueAddress(String server, String queue) {
        return serverAddress(server).and(QUEUE, queue);
    }


    // ------------------------------------------------------ server / destinations / jms-queue

    public static final String JMSQUEUE_CREATE = Ids.build(JMS_QUEUE_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String JMSQUEUE_UPDATE = Ids.build(JMS_QUEUE_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String JMSQUEUE_DELETE = Ids.build(JMS_QUEUE_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address jmsQueueAddress(String server, String queue) {
        return serverAddress(server).and(JMS_QUEUE, queue);
    }


    // ------------------------------------------------------ server / destinations / jms-topic

    public static final String JMSTOPIC_CREATE = Ids.build(JMS_TOPIC_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String JMSTOPIC_UPDATE = Ids.build(JMS_TOPIC_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String JMSTOPIC_DELETE = Ids.build(JMS_TOPIC_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address jmsTopicAddress(String server, String topic) {
        return serverAddress(server).and(JMS_TOPIC, topic);
    }


    // ------------------------------------------------------ server / destinations / security setting

    public static final String SECSET_CREATE = Ids.build(SECURITY_SETTINGS_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String SECSET_UPDATE = Ids.build(SECURITY_SETTINGS_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String SECSET_DELETE = Ids.build(SECURITY_SETTINGS_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address securitySettingAddress(String server, String secsetting) {
        return serverAddress(server).and(SECURITY_SETTING, secsetting);
    }

    public static final String ROLE_CREATE = Ids.build("role", CrudConstants.CREATE, Random.name());

    public static Address securitySettingRoleAddress(String server, String secsetting, String role) {
        return securitySettingAddress(server, secsetting).and(ROLE, role);
    }


    // ------------------------------------------------------ server / destinations / address setting

    public static final String AS_CREATE = Ids.build(ADDRESS_SETTINGS_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String AS_UPDATE = Ids.build(ADDRESS_SETTINGS_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String AS_DELETE = Ids.build(ADDRESS_SETTINGS_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address addressSettingAddress(String server, String name) {
        return serverAddress(server).and(ADDRESS_SETTING, name);
    }


    // ------------------------------------------------------ server / destinations / divert

    public static final String DIVERT_CREATE = Ids.build(DIVERT_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String DIVERT_UPDATE = Ids.build(DIVERT_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String DIVERT_DELETE = Ids.build(DIVERT_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address divertAddress(String server, String name) {
        return serverAddress(server).and(DIVERT, name);
    }


    // ------------------------------------------------------ server / connections / acceptor - generic

    public static final String ACCP_GEN_CREATE = Ids.build(ACCEPTOR_GENERIC_PREFIX, CrudConstants.CREATE,
            Random.name());
    public static final String ACCP_GEN_UPDATE = Ids.build(ACCEPTOR_GENERIC_PREFIX, CrudConstants.UPDATE,
            Random.name());
    public static final String ACCP_GEN_TRY_UPDATE = Ids.build(ACCEPTOR_GENERIC_PREFIX, TRY_UPDATE, Random.name());
    public static final String ACCP_GEN_DELETE = Ids.build(ACCEPTOR_GENERIC_PREFIX, CrudConstants.DELETE,
            Random.name());

    public static Address acceptorGenericAddress(String server, String name) {
        return serverAddress(server).and(ACCEPTOR, name);
    }


    // ------------------------------------------------------ server / connections / acceptor - in-vm

    public static final String ACCP_INVM_CREATE = Ids.build(ACCEPTOR_IN_VM_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String ACCP_INVM_UPDATE = Ids.build(ACCEPTOR_IN_VM_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String ACCP_INVM_TRY_UPDATE = Ids.build(ACCEPTOR_IN_VM_PREFIX, TRY_UPDATE, Random.name());
    public static final String ACCP_INVM_DELETE = Ids.build(ACCEPTOR_IN_VM_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address acceptorInVMAddress(String server, String name) {
        return serverAddress(server).and(IN_VM_ACCEPTOR, name);
    }


    // ------------------------------------------------------ server / connections / acceptor - http

    public static final String ACCP_HTTP_CREATE = Ids.build(ACCEPTOR_HTTP_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String ACCP_HTTP_UPDATE = Ids.build(ACCEPTOR_HTTP_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String ACCP_HTTP_DELETE = Ids.build(ACCEPTOR_HTTP_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address acceptorHttpAddress(String server, String name) {
        return serverAddress(server).and(HTTP_ACCEPTOR, name);
    }


    // ------------------------------------------------------ server / connections / acceptor - remote

    public static final String ACCP_REM_CREATE = Ids.build(ACCEPTOR_REMOTE_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String ACCP_REM_UPDATE = Ids.build(ACCEPTOR_REMOTE_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String ACCP_REM_TRY_UPDATE = Ids.build(ACCEPTOR_REMOTE_PREFIX, TRY_UPDATE, Random.name());
    public static final String ACCP_REM_DELETE = Ids.build(ACCEPTOR_REMOTE_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address acceptorRemoteAddress(String server, String name) {
        return serverAddress(server).and(REMOTE_ACCEPTOR, name);
    }


    // ------------------------------------------------------ server / connections / connector - generic

    public static final String CONN_GEN_CREATE = Ids.build(CONNECTOR_GENERIC_PREFIX, CrudConstants.CREATE,
            Random.name());
    public static final String CONN_GEN_UPDATE = Ids.build(CONNECTOR_GENERIC_PREFIX, CrudConstants.UPDATE,
            Random.name());
    public static final String CONN_GEN_DELETE = Ids.build(CONNECTOR_GENERIC_PREFIX, CrudConstants.DELETE,
            Random.name());

    public static Address connectorGenericAddress(String server, String name) {
        return serverAddress(server).and(CONNECTOR, name);
    }


    // ------------------------------------------------------ server / connections / connector - in-vm

    public static final String CONN_INVM_CREATE = Ids.build(CONNECTOR_INVM_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String CONN_INVM_UPDATE = Ids.build(CONNECTOR_INVM_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String CONN_INVM_TRY_UPDATE = Ids.build(CONNECTOR_INVM_PREFIX, TRY_UPDATE, Random.name());
    public static final String CONN_INVM_DELETE = Ids.build(CONNECTOR_INVM_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address connectorInVMAddress(String server, String name) {
        return serverAddress(server).and(IN_VM_CONNECTOR, name);
    }


    // ------------------------------------------------------ server / connections / connector - http

    public static final String CONN_HTTP_CREATE = Ids.build(CONNECTOR_HTTP_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String CONN_HTTP_UPDATE = Ids.build(CONNECTOR_HTTP_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String CONN_HTTP_TRY_UPDATE = Ids.build(CONNECTOR_HTTP_PREFIX, TRY_UPDATE, Random.name());
    public static final String CONN_HTTP_DELETE = Ids.build(CONNECTOR_HTTP_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address connectorHttpAddress(String server, String name) {
        return serverAddress(server).and(HTTP_CONNECTOR, name);
    }


    // ------------------------------------------------------ server / connections / connector - remote

    public static final String CONN_REM_CREATE = Ids.build(CONNECTOR_REMOTE_PREFIX, CrudConstants.CREATE,
            Random.name());
    public static final String CONN_REM_UPDATE = Ids.build(CONNECTOR_REMOTE_PREFIX, CrudConstants.UPDATE,
            Random.name());
    public static final String CONN_REM_DELETE = Ids.build(CONNECTOR_REMOTE_PREFIX, CrudConstants.DELETE,
            Random.name());

    public static Address connectorRemoteAddress(String server, String name) {
        return serverAddress(server).and(REMOTE_CONNECTOR, name);
    }


    // ------------------------------------------------------ server / connections / connector service

    public static final String CONN_SVC_CREATE = Ids.build(CONNECTOR_SERVICE_PREFIX, CrudConstants.CREATE,
            Random.name());
    public static final String CONN_SVC_UPDATE = Ids.build(CONNECTOR_SERVICE_PREFIX, CrudConstants.UPDATE,
            Random.name());
    public static final String CONN_SVC_DELETE = Ids.build(CONNECTOR_SERVICE_PREFIX, CrudConstants.DELETE,
            Random.name());

    public static Address connectorServiceAddress(String server, String name) {
        return serverAddress(server).and(CONNECTOR_SERVICE, name);
    }


    // ------------------------------------------------------ server / connections / connector factory

    public static final String CONN_FAC_CREATE = Ids.build(CONNECTOR_FACTORY_PREFIX, CrudConstants.CREATE,
            Random.name());
    public static final String CONN_FAC_CREATE_ENTRY = Ids.build(CONNECTOR_FACTORY_PREFIX, "create-entry",
            Random.name());
    public static final String CONN_FAC_UPDATE = Ids.build(CONNECTOR_FACTORY_PREFIX, CrudConstants.UPDATE,
            Random.name());
    public static final String CONN_FAC_TRY_UPDATE = Ids.build(CONNECTOR_FACTORY_PREFIX, TRY_UPDATE, Random.name());
    public static final String CONN_FAC_DELETE = Ids.build(CONNECTOR_FACTORY_PREFIX, CrudConstants.DELETE,
            Random.name());

    public static Address connectionFactoryAddress(String server, String name) {
        return serverAddress(server).and(CONNECTION_FACTORY, name);
    }


    // ------------------------------------------------------ server / connections / pooled connection factory

    public static final String POOL_CONN_CREATE = Ids.build(POOLED_CONNECTION_FACTORY, CrudConstants.CREATE,
            Random.name());
    public static final String POOL_CONN_CREATE_ENTRY = Ids.build(POOLED_CONNECTION_FACTORY, "-entry", Random.name());
    public static final String POOL_CONN_UPDATE =
            Ids.build(POOLED_CONNECTION_FACTORY, CrudConstants.UPDATE, Random.name());
    public static final String POOL_CONN_TRY_UPDATE = Ids.build(POOLED_CONNECTION_FACTORY, TRY_UPDATE, Random.name());
    public static final String POOL_CONN_DELETE =
            Ids.build(POOLED_CONNECTION_FACTORY, CrudConstants.DELETE, Random.name());

    public static Address pooledConnectionFactoryAddress(String server, String name) {
        return serverAddress(server).and(ModelDescriptionConstants.POOLED_CONNECTION_FACTORY, name);
    }


    // ------------------------------------------------------ server / broadcast-group

    public static final String BG_CREATE = Ids.build(BROADCAST_GROUP_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String BG_UPDATE = Ids.build(BROADCAST_GROUP_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String BG_DELETE = Ids.build(BROADCAST_GROUP_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address broadcastGroupAddress(String server, String name) {
        return serverAddress(server).and(BROADCAST_GROUP, name);
    }


    // ------------------------------------------------------ server / discovery-group

    public static final String DG_CREATE = Ids.build(DISCOVERY_GROUP_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String DG_UPDATE = Ids.build(DISCOVERY_GROUP_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String DG_UPDATE_ALTERNATIVES =
            Ids.build(DISCOVERY_GROUP_PREFIX, "update-alternatives", Random.name());
    public static final String DG_DELETE = Ids.build(DISCOVERY_GROUP_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address discoveryGroupAddress(String server, String name) {
        return serverAddress(server).and(DISCOVERY_GROUP, name);
    }


    // ------------------------------------------------------ server / cluster-connection

    public static final String CC_CREATE = Ids.build(CLUSTER_CONNECTION_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String CC_UPDATE = Ids.build(CLUSTER_CONNECTION_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String CC_UPDATE_ALTERNATIVES =
            Ids.build(CLUSTER_CONNECTION_PREFIX, "update-alternatives", Random.name());
    public static final String CC_DELETE = Ids.build(CLUSTER_CONNECTION_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address clusterConnectionAddress(String server, String name) {
        return serverAddress(server).and(CLUSTER_CONNECTION, name);
    }


    // ------------------------------------------------------ server / grouping-handler

    public static final String GH_CREATE = Ids.build(GROUPING_HANDLER, CrudConstants.CREATE, Random.name());
    public static final String GH_UPDATE = Ids.build(GROUPING_HANDLER, CrudConstants.UPDATE, Random.name());
    public static final String GH_DELETE = Ids.build(GROUPING_HANDLER, CrudConstants.DELETE, Random.name());

    public static Address groupingHandlerAddress(String server, String name) {
        return serverAddress(server).and(ModelDescriptionConstants.GROUPING_HANDLER, name);
    }


    // ------------------------------------------------------ server / bridge

    public static final String BRIDGE_CREATE = Ids.build(BRIDGE_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String BRIDGE_UPDATE = Ids.build(BRIDGE_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String BRIDGE_DELETE = Ids.build(BRIDGE_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address bridgeAddress(String server, String name) {
        return serverAddress(server).and(BRIDGE, name);
    }


    // ------------------------------------------------------ server / ha-policy

    public static Address haPolicyAddress(String server, String name) {
        return serverAddress(server).and(HA_POLICY, name);
    }


    // ------------------------------------------------------ jms-bridge

    public static final String JMSBRIDGE_CREATE = Ids.build(JMSBRIDGE_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String JMSBRIDGE_CREATE2 = Ids.build(JMSBRIDGE_PREFIX, "create2", Random.name());
    public static final String JMSBRIDGE_UPDATE = Ids.build(JMSBRIDGE_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String JMSBRIDGE_DELETE = Ids.build(JMSBRIDGE_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address jmsBridgeAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(JMS_BRIDGE, name);
    }

    public static class RemoteActiveMQServer {

        public static Address REMOTE_ACTIVEMQ_SERVER_ADDRESS = SUBSYSTEM_ADDRESS;

        public static Address genericConnectorAddress(String name) {
            return REMOTE_ACTIVEMQ_SERVER_ADDRESS.and(CONNECTOR, name);
        }

        public static Address inVMConnectorAddress(String name) {
            return REMOTE_ACTIVEMQ_SERVER_ADDRESS.and(IN_VM_CONNECTOR, name);
        }

        public static Address httpConnectorAddress(String name) {
            return REMOTE_ACTIVEMQ_SERVER_ADDRESS.and(HTTP_CONNECTOR, name);
        }

        public static Address remoteConnectorAddress(String name) {
            return REMOTE_ACTIVEMQ_SERVER_ADDRESS.and(REMOTE_CONNECTOR, name);
        }

        public static Address discoveryGroupAddress(String name) {
            return REMOTE_ACTIVEMQ_SERVER_ADDRESS.and(DISCOVERY_GROUP, name);
        }

        public static Address connectionFactoryAddress(String name) {
            return REMOTE_ACTIVEMQ_SERVER_ADDRESS.and(CONNECTION_FACTORY, name);
        }

        public static Address pooledConnectionFactoryAddress(String name) {
            return REMOTE_ACTIVEMQ_SERVER_ADDRESS.and("pooled-connection-factory", name);
        }

        public static Address externalJMSQueueAddress(String name) {
            return REMOTE_ACTIVEMQ_SERVER_ADDRESS.and(EXTERNAL_JMS_QUEUE, name);
        }

        public static Address externalJMSTopicAddress(String name) {
            return REMOTE_ACTIVEMQ_SERVER_ADDRESS.and(EXTERNAL_JMS_TOPIC, name);
        }

        private RemoteActiveMQServer() {

        }
    }

    private MessagingFixtures() {
    }
}
