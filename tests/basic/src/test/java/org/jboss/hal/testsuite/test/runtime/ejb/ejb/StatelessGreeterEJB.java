package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;

@Stateless
@Remote(RemoteEJBInterface.class)
public class StatelessGreeterEJB implements RemoteEJBInterface {

    public String invoke() throws InterruptedException {
        Thread.sleep(5000);
        return "Hello from stateless greeter";
    }

}
