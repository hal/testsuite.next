package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

import org.jboss.hal.testsuite.test.runtime.ejb.EJBFixtures;

@Stateless
@DeclareRoles({EJBFixtures.ROLE_1, EJBFixtures.ROLE_2})
@RunAs(EJBFixtures.ROLE_1)
public class StatelessEJBWithRoles implements RemoteEJBInterface {
    @Override
    public String invoke() throws InterruptedException {
        return "Hello from stateless EJB with roles";
    }
}
