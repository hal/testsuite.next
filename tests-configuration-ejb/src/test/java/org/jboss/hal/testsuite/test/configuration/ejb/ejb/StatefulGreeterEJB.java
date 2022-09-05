package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.ejb.Stateful;

@Stateful
public class StatefulGreeterEJB implements RemoteEJBInterface {

    @Override
    public String invoke() {
        return "Hello from stateful greeter";
    }
}
