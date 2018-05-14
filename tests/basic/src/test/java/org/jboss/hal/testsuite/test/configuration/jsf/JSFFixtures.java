package org.jboss.hal.testsuite.test.configuration.jsf;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class JSFFixtures {

    public static final Address JSF_ADDRESS = Address.subsystem("jsf");
    public static final String DEFAULT_JSF_IMPL_SLOT = "default-jsf-impl-slot";
    public static final String DISALLOW_DOCTYPE_DECL = "disallow-doctype-decl";
    public static final String JSF = "jsf";

    private JSFFixtures() {

    }
}
