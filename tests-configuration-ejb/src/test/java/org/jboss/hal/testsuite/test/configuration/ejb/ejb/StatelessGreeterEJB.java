package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SLEEP_TIME;

@Stateless
@Remote(RemoteEJBInterface.class)
public class StatelessGreeterEJB implements RemoteEJBInterface {

    public String invoke() throws InterruptedException {
        Thread.sleep(SLEEP_TIME);
        return "Hello from stateless greeter";
    }

}
