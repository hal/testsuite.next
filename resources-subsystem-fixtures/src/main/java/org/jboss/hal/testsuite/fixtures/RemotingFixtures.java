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

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.CrudConstants;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CONNECTOR;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_CONNECTOR;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REMOTING;

public final class RemotingFixtures {

    private static final String CONNECTOR_PREFIX = "con";
    private static final String HTTP_CONNECTOR_PREFIX = "http";
    private static final String LOCAL_OUTBOUND_PREFIX = "lout";
    private static final String OUTBOUND_PREFIX = "out";
    private static final String REMOTE_OUTBOUND_PREFIX = "rout";
    private static final String POLICY = "policy";
    private static final String SECURITY = "security";

    public static final String AUTH_REALM = "auth-realm";
    public static final String AUTHENTICATION_PROVIDER = "authentication-provider";
    public static final String BACKLOG = "BACKLOG";
    public static final String BUFFER_REGION_SIZE = "buffer-region-size";
    public static final String CONNECTOR_REF = "connector-ref";
    public static final String FORWARD_SECRECY = "forward-secrecy";
    public static final String INCLUDE_MECHANISMS = "include-mechanisms";
    public static final String MAX_INBOUND_CHANNELS = "max-inbound-channels";
    public static final String SASL = "sasl";
    public static final String SASL_POLICY = "sasl-policy";
    public static final String URI = "uri";

    public static final Address SUBSYSTEM_ADDRESS = Address.subsystem(REMOTING);

    // ------------------------------------------------------ connector

    public static final String CONNECTOR_CREATE = Ids.build(CONNECTOR_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String CONNECTOR_READ = Ids.build(CONNECTOR_PREFIX, CrudConstants.READ, Random.name());
    public static final String CONNECTOR_UPDATE = Ids.build(CONNECTOR_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String CONNECTOR_DELETE = Ids.build(CONNECTOR_PREFIX, CrudConstants.DELETE, Random.name());
    public static final String CONNECTOR_SECURITY = Ids.build(CONNECTOR_PREFIX, SECURITY, Random.name());
    public static final String CONNECTOR_POLICY = Ids.build(CONNECTOR_PREFIX, POLICY, Random.name());

    public static Address connectorAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONNECTOR, name);
    }

    public static Address connectorSecurityAddress(String name) {
        return connectorAddress(name).and(SECURITY, SASL);
    }

    public static Address connectorPolicyAddress(String name) {
        return connectorSecurityAddress(name).and(SASL_POLICY, POLICY);
    }

    // ------------------------------------------------------ http connector

    public static final String HTTP_CONNECTOR_CREATE = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String HTTP_CONNECTOR_READ = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, CrudConstants.READ, Random.name());
    public static final String HTTP_CONNECTOR_UPDATE = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String HTTP_CONNECTOR_DELETE = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, CrudConstants.DELETE, Random.name());
    public static final String HTTP_CONNECTOR_SECURITY = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, SECURITY, Random.name());
    public static final String HTTP_CONNECTOR_POLICY = Ids.build(HTTP_CONNECTOR_PREFIX, CONNECTOR_PREFIX, POLICY, Random.name());

    public static String httpConnectorRef(String name) {
        return Ids.build(name, "ref");
    }

    public static Address httpConnectorAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(HTTP_CONNECTOR, name);
    }

    public static Address httpConnectorSecurityAddress(String name) {
        return httpConnectorAddress(name).and(SECURITY, SASL);
    }

    public static Address httpConnectorPolicyAddress(String name) {
        return httpConnectorSecurityAddress(name).and(SASL_POLICY, POLICY);
    }

    // ------------------------------------------------------ local outbound

    public static final String LOCAL_OUTBOUND_CREATE = Ids.build(LOCAL_OUTBOUND_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String LOCAL_OUTBOUND_READ = Ids.build(LOCAL_OUTBOUND_PREFIX, CrudConstants.READ, Random.name());
    public static final String LOCAL_OUTBOUND_UPDATE = Ids.build(LOCAL_OUTBOUND_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String LOCAL_OUTBOUND_DELETE = Ids.build(LOCAL_OUTBOUND_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address outboundLocalAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("local-outbound-connection", name);
    }

    // ------------------------------------------------------ local outbound

    public static final String OUTBOUND_CREATE = Ids.build(OUTBOUND_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String OUTBOUND_READ = Ids.build(OUTBOUND_PREFIX, CrudConstants.READ, Random.name());
    public static final String OUTBOUND_UPDATE = Ids.build(OUTBOUND_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String OUTBOUND_DELETE = Ids.build(OUTBOUND_PREFIX, CrudConstants.DELETE, Random.name());

    public static String uri(String name) {
        return Ids.build(name, "uri");
    }

    public static Address outboundAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("outbound-connection", name);
    }

    // ------------------------------------------------------ remote outbound

    public static final String REMOTE_OUTBOUND_CREATE = Ids.build(REMOTE_OUTBOUND_PREFIX, CrudConstants.CREATE, Random.name());
    public static final String REMOTE_OUTBOUND_READ = Ids.build(REMOTE_OUTBOUND_PREFIX, CrudConstants.READ, Random.name());
    public static final String REMOTE_OUTBOUND_UPDATE = Ids.build(REMOTE_OUTBOUND_PREFIX, CrudConstants.UPDATE, Random.name());
    public static final String REMOTE_OUTBOUND_DELETE = Ids.build(REMOTE_OUTBOUND_PREFIX, CrudConstants.DELETE, Random.name());

    public static Address outboundRemoteAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("remote-outbound-connection", name);
    }

    private RemotingFixtures() {
    }
}
