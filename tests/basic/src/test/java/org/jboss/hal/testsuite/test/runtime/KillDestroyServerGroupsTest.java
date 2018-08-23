package org.jboss.hal.testsuite.test.runtime;

import java.io.IOException;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

@RunWith(Arquillian.class)
@Category(Domain.class)
public class KillDestroyServerGroupsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final FinderPath SERVER_GROUP_FINDER_PATH = new FinderPath().append(Ids.DOMAIN_BROWSE_BY, "server-groups");
    private static final String OTHER_SERVER_GROUP_ID = Ids.build("sg", "other-server-group");

    @AfterClass
    public static void tearDown() throws IOException {
        client.close();
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Test
    public void kill() {
        console.finder(NameTokens.RUNTIME, SERVER_GROUP_FINDER_PATH)
            .column(Ids.SERVER_GROUP)
            .selectItem(OTHER_SERVER_GROUP_ID)
            .dropdown()
            .click("Kill");
        console.confirmationDialog().confirm();
    }

    @Test
    public void destroy() {
        console.finder(NameTokens.RUNTIME, SERVER_GROUP_FINDER_PATH)
            .column(Ids.SERVER_GROUP)
            .selectItem(OTHER_SERVER_GROUP_ID)
            .dropdown()
            .click("Destroy");
        console.confirmationDialog().confirm();
    }

}
