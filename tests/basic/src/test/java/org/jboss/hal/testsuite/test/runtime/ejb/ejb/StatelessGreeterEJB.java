package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.hal.testsuite.test.runtime.ejb.EJBFixtures;

@Stateless
@Remote(RemoteEJBInterface.class)
public class StatelessGreeterEJB implements RemoteEJBInterface {

    public String invoke() throws InterruptedException {
        Thread.sleep(EJBFixtures.SLEEP_TIME);
        return "Hello from stateless greeter";
    }

}
