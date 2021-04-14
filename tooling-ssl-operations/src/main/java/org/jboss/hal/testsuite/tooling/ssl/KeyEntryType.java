package org.jboss.hal.testsuite.tooling.ssl;

import org.jboss.dmr.ModelNode;

/**
 * Key entry type to be used for reading key store alias details
 *
 */
public enum KeyEntryType {

    PRIVATE_KEY_ENTRY("PrivateKeyEntry", (aliasValue, target) -> {
        return aliasValue.get("certificate-chain").asList().get(0).get(target).asString().split(",");
    }),
    TRUSTED_CERTIFICATE_ENTRY("TrustedCertificateEntry", (aliasValue, target) -> {
        return aliasValue.get("certificate").get(target).asString().split(",");
    });

    private String type;
    private DNReader dnReader;

    public String getType() {
        return type;
    }

    public String[] getDNArray(ModelNode aliasValue, String target) {
        return dnReader.getDNArray(aliasValue, target);
    }

     KeyEntryType(String type, DNReader dnReader) {
        this.type = type;
        this.dnReader = dnReader;
    }

    @FunctionalInterface
    private static interface DNReader {
        String[] getDNArray(ModelNode aliasValue, String target);
    }
}
