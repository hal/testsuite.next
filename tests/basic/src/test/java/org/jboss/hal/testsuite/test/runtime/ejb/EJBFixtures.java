package org.jboss.hal.testsuite.test.runtime.ejb;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class EJBFixtures {

    private EJBFixtures() {

    }

    public static final String ROLE_1 = "role1";
    public static final String ROLE_2 = "role2";

    public static final int SLEEP_TIME = 500;

    public static Address singletonEJBAddress(String deploymentName, Class<?> singletonBeanClass) {
        return ejb3SubsystemAddress(deploymentName).and("singleton-bean", singletonBeanClass.getSimpleName());
    }

    private static Address ejb3SubsystemAddress(String deploymentName) {
        return Address.deployment(deploymentName).and("subsystem", "ejb3");
    }

    public static Address statelessEJBAddress(String deploymentName, Class<?> statelessBeanClass) {
        return ejb3SubsystemAddress(deploymentName).and("stateless-session-bean", statelessBeanClass.getSimpleName());
    }

    public static Address statefulEJBAddress(String deploymentName, Class<?> statefulBeanClass) {
        return ejb3SubsystemAddress(deploymentName).and("stateful-session-bean", statefulBeanClass.getSimpleName());
    }

    public static Address messageDrivenEJBAddress(String deploymentName, Class<?> messageDrivenBeanClass) {
        return ejb3SubsystemAddress(deploymentName).and("message-driven-bean", messageDrivenBeanClass.getSimpleName());
    }
}
