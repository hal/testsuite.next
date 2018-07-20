package org.jboss.hal.testsuite.test.runtime.ejb.ejb;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RunAs;
import javax.ejb.Stateful;

import org.jboss.hal.testsuite.test.runtime.ejb.EJBFixtures;

@Stateful
@DeclareRoles({EJBFixtures.ROLE_1, EJBFixtures.ROLE_2})
@RunAs(EJBFixtures.ROLE_1)
public class StatefulEJBWithRoles implements RemoteEJBInterface {
    @Override
    public String invoke() {
        return "Hello from stateful with roles";
    }
}
