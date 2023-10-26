package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.ejb.Stateful;

@Stateful
public class StatefulEJBInvocations implements RemoteEJBInterface {
    @Override
    public String invoke() {
        return "Hello from stateful EJB for measuring the amount of invocations";
    }
}
