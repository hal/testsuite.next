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
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

public interface RemotingFixtures {

    String AUTH_REALM = "auth-realm";
    String AUTHENTICATION_PROVIDER = "authentication-provider";
    String BUFFER_REGION_SIZE = "buffer-region-size";
    String CONNECTOR_REF = "connector-ref";
    String FORWARD_SECRECY = "forward-secrecy";
    String INCLUDE_MECHANISMS = "include-mechanisms";
    String MAX_INBOUND_CHANNELS = "max-inbound-channels";
    String SASL = "sasl";
    String SASL_POLICY = "sasl-policy";
    String URI = "uri";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(REMOTING);
    Address ENDPOINT_ADDRESS = SUBSYSTEM_ADDRESS.and(CONFIGURATION, ENDPOINT);

    // ------------------------------------------------------ connector

    String CONNECTOR_CREATE = Ids.build("con", "create", Random.name());
    String CONNECTOR_READ = Ids.build("con", "read", Random.name());
    String CONNECTOR_UPDATE = Ids.build("con", "update", Random.name());
    String CONNECTOR_DELETE = Ids.build("con", "delete", Random.name());
    String CONNECTOR_SECURITY = Ids.build("con", "security", Random.name());
    String CONNECTOR_POLICY = Ids.build("con", "policy", Random.name());

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

    String HTTP_CONNECTOR_CREATE = Ids.build("http", "con", "create", Random.name());
    String HTTP_CONNECTOR_READ = Ids.build("http", "con", "read", Random.name());
    String HTTP_CONNECTOR_UPDATE = Ids.build("http", "con", "update", Random.name());
    String HTTP_CONNECTOR_DELETE = Ids.build("http", "con", "delete", Random.name());
    String HTTP_CONNECTOR_SECURITY = Ids.build("http", "con", "security", Random.name());
    String HTTP_CONNECTOR_POLICY = Ids.build("http", "con", "policy", Random.name());

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

    String LOCAL_OUTBOUND_CREATE = Ids.build("lout", "create", Random.name());
    String LOCAL_OUTBOUND_READ = Ids.build("lout", "read", Random.name());
    String LOCAL_OUTBOUND_UPDATE = Ids.build("lout", "update", Random.name());
    String LOCAL_OUTBOUND_DELETE = Ids.build("lout", "delete", Random.name());

    static Address outboundLocalAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("local-outbound-connection", name);
    }

    // ------------------------------------------------------ local outbound

    String OUTBOUND_CREATE = Ids.build("out", "create", Random.name());
    String OUTBOUND_READ = Ids.build("out", "read", Random.name());
    String OUTBOUND_UPDATE = Ids.build("out", "update", Random.name());
    String OUTBOUND_DELETE = Ids.build("out", "delete", Random.name());

    static String uri(String name) {
        return Ids.build(name, "uri");
    }

    static Address outboundAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("outbound-connection", name);
    }

    // ------------------------------------------------------ remote outbound

    String REMOTE_OUTBOUND_CREATE = Ids.build("rout", "create", Random.name());
    String REMOTE_OUTBOUND_READ = Ids.build("rout", "read", Random.name());
    String REMOTE_OUTBOUND_UPDATE = Ids.build("rout", "update", Random.name());
    String REMOTE_OUTBOUND_DELETE = Ids.build("rout", "delete", Random.name());

    static Address utboundRemoteAddress(String name) {
        return SUBSYSTEM_ADDRESS.and("remote-outbound-connection", name);
    }
}
