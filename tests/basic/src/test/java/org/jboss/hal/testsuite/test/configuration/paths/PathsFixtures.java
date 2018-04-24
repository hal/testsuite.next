package org.jboss.hal.testsuite.test.configuration.paths;

import org.apache.commons.lang3.RandomStringUtils;
import org.wildfly.extras.creaper.core.online.operations.Address;

public class PathsFixtures {

    private PathsFixtures() {

    }

    public static Address pathAddress(String path) {
        return Address.of("path", path);
    }

    public static final String PATH = "path";

    static final String PATH_CREATE = "path-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);
    static final String PATH_DELETE = "path-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7);
    static final String PATH_EDIT = "path-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);
    static final String RELATIVE_TO_PATH = "relative-to-path-" + RandomStringUtils.randomAlphanumeric(7);

}
