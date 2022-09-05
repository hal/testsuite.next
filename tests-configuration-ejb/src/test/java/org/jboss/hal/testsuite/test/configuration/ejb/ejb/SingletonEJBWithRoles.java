package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.Singleton;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_1;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_2;

@DeclareRoles({ROLE_1, ROLE_2})
@RunAs(ROLE_1)
@Singleton
public class SingletonEJBWithRoles implements RemoteEJBInterface {

    private int counter = 0;

    @Override
    public String invoke() {
        return "The counter is: " + counter++;
    }

}
