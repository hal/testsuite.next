package org.jboss.hal.testsuite.test.configuration.undertow;

import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

public class ApplicationSecurityDomainFixtures {

    public static final String ENABLE_JACC = "enable-jacc";
    public static final String HTTP_AUTHENTICATION_FACTORY = "http-authentication-factory";
    public static final String OVERRIDE_DEPLOYMENT_CONFIG = "override-deployment-config";

    public static final String SINGLE_SIGN_ON_KEY_STORE = "key-store";
    public static final String SINGLE_SIGN_ON_KEY_ALIAS = "key-alias";

    public static final String APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED = "app-sec-domain-to-be-tested-" + Random.name(7);
    public static final String APPLICATION_SECURITY_DOMAIN_TO_BE_TESTED2 = "app-sec-domain-to-be-tested-2-" + Random.name(7);
    public static final String KEY_STORE_TO_BE_ADDED = "key_store_to_add_" + Random.name(7);

    public static final String APPLICATION_SECURITY_DOMAIN_WITH_SINGLE_SIGN_ON = "app-sec-with-sso-" + Random.name(7);
    public static final String KEY_STORE_TO_BE_EDITED = "key_store_to_be_edited_" + Random.name(7);
    public static final String CLIENT_SSL_CONTEXT_TO_BE_ADDED = "client-ssl-context-" + Random.name(7);


    public static Address applicationSecurityDomain(String name) {
        return UndertowFixtures.UNDERTOW_ADDRESS.and("application-security-domain", name);
    }

    public static Address singleSignOnAddress(String applicationSecurityDomainName) {
        return applicationSecurityDomain(applicationSecurityDomainName).and("setting", "single-sign-on");
    }

    private ApplicationSecurityDomainFixtures() {

    }

}
