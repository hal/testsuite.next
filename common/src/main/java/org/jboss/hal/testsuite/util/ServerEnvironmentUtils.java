package org.jboss.hal.testsuite.util;

import java.io.IOException;

import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

public class ServerEnvironmentUtils {

    private final Operations operations;
    private static final Address SERVER_ENVIRONMENT_ADDRESS = Address.coreService("server-environment");

    public ServerEnvironmentUtils(OnlineManagementClient client) {
        this.operations = new Operations(client);
    }

    public String getServerHostName() throws IOException {
        return operations.readAttribute(SERVER_ENVIRONMENT_ADDRESS, "host-name").stringValue();
    }

    public boolean isEAP() {
        try {
            return operations.readAttribute(Address.root(), "product-name").stringValue().contains("EAP");
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to read product-name from server CLI", ex);
        }
    }
}
