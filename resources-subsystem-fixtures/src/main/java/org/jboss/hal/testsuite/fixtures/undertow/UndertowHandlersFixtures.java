package org.jboss.hal.testsuite.fixtures.undertow;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class UndertowHandlersFixtures {

    private static final Address HANDLERS_ADDRESS = UndertowFixtures.UNDERTOW_ADDRESS.and("configuration","handler");

    public static Address fileHandlerAddress(String fileHandlerName) {
        return HANDLERS_ADDRESS.and("file", fileHandlerName);
    }

    public static Address reverseProxyAddress(String reverseProxyName) {
        return HANDLERS_ADDRESS.and("reverse-proxy", reverseProxyName);
    }

    private UndertowHandlersFixtures() {

    }

}
