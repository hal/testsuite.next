package org.jboss.hal.testsuite.test.configuration.undertow;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class UndertowFiltersFixtures {

    private static final String CONFIGURATION = "configuration";

    private static final Address FILTERS_ADDRESS = UndertowFixtures.UNDERTOW_ADDRESS.and(CONFIGURATION, "filter");

    public static final String HEADER_NAME = "header-name";

    public static final String HEADER_VALUE = "header-value";

    public static Address rewriteAddress(String rewriteName) {
        return FILTERS_ADDRESS.and("rewrite", rewriteName);
    }

    public static Address responseHeaderAddress(String responseHeaderName) {
        return FILTERS_ADDRESS.and("response-header", responseHeaderName);
    }

    public static Address requestLimitAddress(String requestLimitName) {
        return FILTERS_ADDRESS.and("request-limit", requestLimitName);
    }

    public static Address modClusterFilterAdress(String modClusterFilterName) {
        return FILTERS_ADDRESS.and("mod-cluster", modClusterFilterName);
    }

    public static Address gzipFilterAddress(String gzipFilterName) {
        return FILTERS_ADDRESS.and("gzip", gzipFilterName);
    }

    public static Address expressionFilterAddress(String expressionFilterName) {
        return FILTERS_ADDRESS.and("expression-filter", expressionFilterName);
    }

    public static Address errorPageAddress(String errorPageName) {
        return FILTERS_ADDRESS.and("error-page", errorPageName);
    }

    public static Address customFilterAddress(String customFilterName) {
        return FILTERS_ADDRESS.and("custom-filter", customFilterName);
    }

    private UndertowFiltersFixtures() {

    }

}
