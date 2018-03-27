package org.jboss.hal.testsuite.test.configuration.undertow;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class ApplicationSecurityDomainFixtures {

    public static final String ENABLE_JACC = "enable-jacc";
    public static final String HTTP_AUTHENTICATION_FACTORY = "http-authentication-factory";
    public static final String OVERRIDE_DEPLOYMENT_CONFIG = "override-deployment-config";

    public static Address applicationSecurityDomain(String name) {
        return UndertowFixtures.UNDERTOW_ADDRESS.and("application-security-domain", name);
    }

    private ApplicationSecurityDomainFixtures() {

    }

}
