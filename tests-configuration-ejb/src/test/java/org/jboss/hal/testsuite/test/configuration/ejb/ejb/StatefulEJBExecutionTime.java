package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import javax.ejb.Stateful;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SLEEP_TIME;

@Stateful
public class StatefulEJBExecutionTime implements RemoteEJBInterface {
    @Override
    public String invoke() throws InterruptedException {
        Thread.sleep(SLEEP_TIME);
        return "Hello from stateful EJB for measuring the amount of invocations";
    }
}
