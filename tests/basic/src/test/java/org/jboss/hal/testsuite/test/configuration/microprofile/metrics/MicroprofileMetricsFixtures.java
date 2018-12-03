package org.jboss.hal.testsuite.test.configuration.microprofile.metrics;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class MicroprofileMetricsFixtures {

    public static final Address MICROPROFILE_METRICS_ADDRESS = Address.subsystem("microprofile-metrics-smallrye");
    public static final String EXPOSE_ALL_SUBSYSTEMS = "expose-all-subsystems";
    public static final String EXPOSED_SUBSYSTEMS = "exposed-subsystems";
    public static final String SECURITY_ENABLED = "security-enabled";

    private MicroprofileMetricsFixtures() { }
}
