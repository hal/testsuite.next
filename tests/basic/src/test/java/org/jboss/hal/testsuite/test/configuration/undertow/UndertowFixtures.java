package org.jboss.hal.testsuite.test.configuration.undertow;

import org.wildfly.extras.creaper.core.online.operations.Address;

class UndertowFixtures {

    static final Address UNDERTOW_ADDRESS = Address.subsystem("undertow");

    static final String DEFAULT_SECURITY_DOMAIN = "default-security-domain";
    static final String DEFAULT_SERVER = "default-server";
    static final String DEFAULT_SERVLET_CONTAINER = "default-servlet-container";
    static final String DEFAULT_VIRTUAL_HOST = "default-virtual-host";
    static final String INSTANCE_ID = "instance-id";
    static final String STATISTICS_ENABLED = "statistics-enabled";

    static Address serverAddress(String serverName) {
        return UNDERTOW_ADDRESS.and("server", serverName);
    }

    static Address servletContainerAddress(String name) {
        return UNDERTOW_ADDRESS.and("servlet-container", name);
    }

    static Address virtualHostAddress(String serverName, String name) {
        return serverAddress(serverName).and("host", name);
    }

    private UndertowFixtures() {

    }
}
