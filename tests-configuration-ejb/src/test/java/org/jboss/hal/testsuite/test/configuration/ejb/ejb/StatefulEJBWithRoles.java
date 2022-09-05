package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RunAs;
import jakarta.ejb.Stateful;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_1;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_2;

@Stateful
@DeclareRoles({ROLE_1, ROLE_2})
@RunAs(ROLE_1)
public class StatefulEJBWithRoles implements RemoteEJBInterface {
    @Override
    public String invoke() {
        return "Hello from stateful with roles";
    }
}
