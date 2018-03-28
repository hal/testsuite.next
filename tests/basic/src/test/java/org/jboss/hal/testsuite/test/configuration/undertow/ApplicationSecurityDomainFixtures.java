package org.jboss.hal.testsuite.test.configuration.undertow;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class ApplicationSecurityDomainFixtures {

    public static final String ENABLE_JACC = "enable-jacc";
    public static final String HTTP_AUTHENTICATION_FACTORY = "http-authentication-factory";
    public static final String OVERRIDE_DEPLOYMENT_CONFIG = "override-deployment-config";

    public static final String SINGLE_SIGN_ON_KEY_STORE = "key-store";
    public static final String SINGLE_SIGN_ON_KEY_ALIAS = "key-alias";

    public static Address applicationSecurityDomain(String name) {
        return UndertowFixtures.UNDERTOW_ADDRESS.and("application-security-domain", name);
    }

    public static Address singleSignOnAddress(String applicationSecurityDomainName) {
        return applicationSecurityDomain(applicationSecurityDomainName).and("setting", "single-sign-on");
    }

    private ApplicationSecurityDomainFixtures() {

    }

}
