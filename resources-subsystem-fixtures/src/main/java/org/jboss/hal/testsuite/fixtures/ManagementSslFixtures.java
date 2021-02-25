package org.jboss.hal.testsuite.fixtures;

import org.jboss.hal.resources.Ids;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_STORE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;

public final class ManagementSslFixtures {

    public static final String
        KEY_STORE_NAME = Ids.build(KEY_STORE, NAME),
        KEY_STORE_PASSWORD = Ids.build(KEY_STORE, "password"),
        KEY_STORE_PATH = Ids.build(KEY_STORE, PATH),
        KEY_DN_ORGANIZATION = "key-dn-organization",
        HAL = "HAL",
        CREDENTIAL_REFERENCE_CLEAR_TEXT_ATTR = CREDENTIAL_REFERENCE + "." + CLEAR_TEXT,
        CLIENT_CERTIFICATE_ALIAS = "client-certificate-alias",
        CLIENT_CERTIFICATE_PATH = "client-certificate-path",
        CLIENT_CERTIFICATE_VALIDATE = "client-certificate-validate",
        TRUST_STORE = "trust-store",
        TRUST_STORE_NAME = "trust-store-name",
        TRUST_STORE_PASSWORD = "trust-store-password",
        TRUST_STORE_PATH = "trust-store-path",
        PASS = "pass",
        SSL_SESSION_TIMEOUT = "ssl-session-timeout",
        SSL_SESSION_CACHE_SIZE = "ssl-session-cache-size",
        ENABLED_PROTOCOLS = "enabled-protocols",
        ENABLED_CIPHER_SUITES = "enabled-cipher-suites",
        NOT_REQUESTED = "NOT_REQUESTED",
        VERIFY_CLIENT = "verify-client",
        SUBJECT = "subject",
        ISSUER = "issuer",
        YOU_NEED_TO_SELECT_KEY_STORE_MANIPULATION_STATEGY = "You need to select key store manipulation stategy",
        YOU_NEED_TO_SELECT_AUTHENTICATION_AS_WELL_AS_KEY_STORE_MANIPULATION_STATEGY =
            "You need to select authentication as well as key store manipulation stategy",
        CERT = "cert",
        APPLICATION_REALM = "ApplicationRealm",
        SERVER_CERTIFICATE_PATH = "server-certificate-path",
        CLIENT = "client";

    private ManagementSslFixtures() { }

}
