package org.jboss.hal.testsuite.test.configuration.paths;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class PathsFixtures {

    private PathsFixtures() {

    }

    public static Address pathAddress(String path) {
        return Address.of("path", path);
    }

    public static final String PATH = "path";

}
