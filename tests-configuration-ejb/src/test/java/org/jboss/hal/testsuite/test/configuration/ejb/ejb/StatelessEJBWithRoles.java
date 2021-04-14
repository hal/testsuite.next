package org.jboss.hal.testsuite.test.configuration.ejb.ejb;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_1;
import static org.jboss.hal.testsuite.fixtures.EJBFixtures.ROLE_2;

@Stateless
@DeclareRoles({ROLE_1, ROLE_2})
@RunAs(ROLE_1)
public class StatelessEJBWithRoles implements RemoteEJBInterface {
    @Override
    public String invoke() throws InterruptedException {
        return "Hello from stateless EJB with roles";
    }
}
