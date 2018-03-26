package org.jboss.hal.testsuite.test.configuration.undertow;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class ApplicationSecurityDomainFixtures {

    static Address applicationSecurityDomain(String name) {
        return UndertowFixtures.UNDERTOW_ADDRESS.and("application-security-domain", name);
    }

    private ApplicationSecurityDomainFixtures() {

    }

}
