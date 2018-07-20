package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.ejb.Stateful;

@Stateful
public class StatefulEJBPeakInvocations implements RemoteEJBInterface {
    @Override
    public String invoke() {
        return "Hello from stateful with peak invocations";
    }
}
