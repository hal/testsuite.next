package org.jboss.hal.testsuite.test.runtime.multihosts;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.DOMAIN_BROWSE_BY;
import static org.junit.Assert.assertTrue;


@Category(Domain.class)
@RunWith(Arquillian.class)
public class HostInvalidResourceMessage2Test {

    @Inject private Console console;

    @Test
    public void checkHostInvalidMessage2() throws Exception {
        FinderFragment finder = console.finder(NameTokens.RUNTIME, new FinderPath().append(DOMAIN_BROWSE_BY, HOSTS));
        finder.column(HOST).selectItem(HOST + "-" + ConfigUtils.getDefaultHost()).view();
        assertTrue(console.verifyNoError());
    }
}
