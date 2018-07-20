package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.ejb.Stateful;

import org.jboss.hal.testsuite.test.runtime.ejb.EJBFixtures;

@Stateful
public class StatefulEJBExecutionTime implements RemoteEJBInterface {
    @Override
    public String invoke() throws InterruptedException {
        Thread.sleep(EJBFixtures.SLEEP_TIME);
        return "Hello from stateful EJB for measuring the amount of invocations";
    }
}
