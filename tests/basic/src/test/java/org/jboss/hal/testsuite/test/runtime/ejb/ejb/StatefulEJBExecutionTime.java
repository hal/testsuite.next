package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.ejb.Stateful;

@Stateful
public class StatefulEJBExecutionTime implements RemoteEJBInterface {
    @Override
    public String invoke() throws InterruptedException {
        Thread.sleep(5000);
        return "Hello from stateful EJB for measuring the amount of invocations";
    }
}
