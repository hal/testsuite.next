package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.ejb.Stateful;

@Stateful
public class StatefulEJBPeakInvocations implements RemoteEJBInterface {
    @Override
    public String invoke() {
        return "Hello from stateful with peak invocations";
    }
}
