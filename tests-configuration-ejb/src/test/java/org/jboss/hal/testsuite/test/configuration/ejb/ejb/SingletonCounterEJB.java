package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Remote;
import jakarta.ejb.Singleton;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.SLEEP_TIME;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote(RemoteEJBInterface.class)
public class SingletonCounterEJB implements RemoteEJBInterface {

    private int counter = 0;

    @Override
    public String invoke() throws InterruptedException {
        Thread.sleep(SLEEP_TIME);
        return "The counter is: " + counter++;
    }
}
