package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import javax.ejb.Singleton;

import org.jboss.hal.testsuite.test.runtime.ejb.EJBFixtures;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote(RemoteEJBInterface.class)
public class SingletonCounterEJB implements RemoteEJBInterface {

    private int counter = 0;

    @Override
    public String invoke() throws InterruptedException {
        Thread.sleep(EJBFixtures.SLEEP_TIME);
        return "The counter is: " + counter++;
    }
}
