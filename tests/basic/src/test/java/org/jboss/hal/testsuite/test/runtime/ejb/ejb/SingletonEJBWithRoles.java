package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RunAs;
import javax.ejb.Singleton;

import org.jboss.hal.testsuite.test.runtime.ejb.EJBFixtures;

@DeclareRoles({EJBFixtures.ROLE_1, EJBFixtures.ROLE_2})
@RunAs(EJBFixtures.ROLE_1)
@Singleton
public class SingletonEJBWithRoles implements RemoteEJBInterface {

    private int counter = 0;

    @Override
    public String invoke() {
        return "The counter is: " + counter++;
    }

}
