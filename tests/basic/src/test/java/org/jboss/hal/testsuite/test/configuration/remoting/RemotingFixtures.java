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
package org.jboss.hal.testsuite.test.configuration.remoting;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public final class RemotingFixtures {

    private static final String CONNECTOR_PREFIX = "con";
    private static final String HTTP_CONNECTOR_PREFIX = "http";
    private static final String LOCAL_OUTBOUND_PREFIX = "lout";
    private static final String OUTBOUND_PREFIX = "out";
    private static final String REMOTE_OUTBOUND_PREFIX = "rout";
    private static final String POLICY = "policy";
    private static final String SECURITY = "security";

    static String AUTH_REALM = "auth-realm";
    static String AUTHENTICATION_PROVIDER = "authentication-provider";
    static String BUFFER_REGION_SIZE = "buffer-region-size";
    static String CONNECTOR_REF = "connector-ref";
    static String FORWARD_SECRECY = "forward-secrecy";
    static String INCLUDE_MECHANISMS = "include-mechanisms";
    static String MAX_INBOUND_CHANNELS = "max-inbound-channels";
    static String SASL = "sasl";
    static String SASL_POLICY = "sasl-policy";
    static String URI = "uri";

    static Address SUBSYSTEM_ADDRESS = Address.subsystem(REMOTING);
    static Address ENDPOINT_ADDRESS = SUBSYSTEM_ADDRESS.and(CONFIGURATION, ENDPOINT);

    // ------------------------------------------------------ connector

    static String CONNECTOR_CREATE = Ids.build(CONNECTOR_PREFIX, CrudConstants.CREATE, Random.name());
    static String CONNECTOR_READ = Ids.build(CONNECTOR_PREFIX, CrudConstants.READ, Random.name());
    static String CONNECTOR_UPDATE = Ids.build(CONNECTOR_PREFIX, CrudConstants.UPDATE, Random.name());
    static String CONNECTOR_DELETE = Ids.build(CONNECTOR_PREFIX, CrudConstants.DELETE, Random.name());
    static String CONNECTOR_SECURITY = Ids.build(CONNECTOR_PREFIX, SECURITY, Random.name());
    static String CONNECTOR_POLICY = Ids.build(CONNECTOR_PREFIX, POLICY, Random.name());

    static Address connectorAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONNECTOR, name);
    }

    static Address connectorSecurityAddress(String name) {
        return connectorAddress(name).and(SECURITY, SASL);
    }

    static Address connectorPolicyAddress(String name) {
        return connectorSecurityAddress(name).and(SASL_POLICY, POLICY);
    }

    // ------------------------------------------------------ http connector

    static String HTTP_CONNECTOR_CREATE = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, CrudConstants.CREATE, Random.name());
    static String HTTP_CONNECTOR_READ = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, CrudConstants.READ, Random.name());
    static String HTTP_CONNECTOR_UPDATE = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, CrudConstants.UPDATE, Random.name());
    static String HTTP_CONNECTOR_DELETE = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, CrudConstants.DELETE, Random.name());
    static String HTTP_CONNECTOR_SECURITY = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, SECURITY, Random.name());
    static String HTTP_CONNECTOR_POLICY = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, POLICY, Random.name());

    static String httpConnectorRef(String name) {
        return Ids.build(name, "ref");
    }

    static Address httpConnectorAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(HTTP_CONNECTOR, name);
    }

    static Address httpConnectorSecurityAddress(String name) {
        return httpConnectorAddress(name).and(SECURITY, SASL);
    }

    static Address httpConnectorPolicyAddress(String name) {
        return httpConnectorSecurityAddress(name).and(SASL_POLICY, POLICY);
    }

    // ------------------------------------------------------ local outbound

    static String LOCAL_OUTBOUND_CREATE = Ids.build(LOCAL_OUTBOUND_PREFIX, CrudConstants.CREATE, Random.name());
    static String LOCAL_OUTBOUND_READ = Ids.build(LOCAL_OUTBOUND_PREFIX, CrudConstants.READ, Random.name());
    static String LOCAL_OUTBOUND_UPDATE = Ids.build(LOCAL_OUTBOUND_PREFIX, CrudConstants.UPDATE, Random.name());
    static String LOCAL_OUTBOUND_DELETE = Ids.build(LOCAL_OUTBOUND_PREFIX, CrudConstants.DELETE, Random.name());

    static Address outboundLocalAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("local-outbound-connection", name);
    }

    // ------------------------------------------------------ local outbound

    static String OUTBOUND_CREATE = Ids.build(OUTBOUND_PREFIX, CrudConstants.CREATE, Random.name());
    static String OUTBOUND_READ = Ids.build(OUTBOUND_PREFIX, CrudConstants.READ, Random.name());
    static String OUTBOUND_UPDATE = Ids.build(OUTBOUND_PREFIX, CrudConstants.UPDATE, Random.name());
    static String OUTBOUND_DELETE = Ids.build(OUTBOUND_PREFIX, CrudConstants.DELETE, Random.name());

    static String uri(String name) {
        return Ids.build(name, "uri");
    }

    static Address outboundAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("outbound-connection", name);
    }

    // ------------------------------------------------------ remote outbound

    static String REMOTE_OUTBOUND_CREATE = Ids.build(REMOTE_OUTBOUND_PREFIX, CrudConstants.CREATE, Random.name());
    static String REMOTE_OUTBOUND_READ = Ids.build(REMOTE_OUTBOUND_PREFIX, CrudConstants.READ, Random.name());
    static String REMOTE_OUTBOUND_UPDATE = Ids.build(REMOTE_OUTBOUND_PREFIX, CrudConstants.UPDATE, Random.name());
    static String REMOTE_OUTBOUND_DELETE = Ids.build(REMOTE_OUTBOUND_PREFIX, CrudConstants.DELETE, Random.name());

    static Address outboundRemoteAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("remote-outbound-connection", name);
    }
}
