package org.jboss.hal.testsuite.test.runtime;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
@Category(Domain.class)
public class KillDestroyServerGroupsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final FinderPath SERVER_GROUP_FINDER_PATH =
        new FinderPath().append(Ids.DOMAIN_BROWSE_BY, "server-groups");
    private static final String OTHER_SERVER_GROUP = "other-server-group";
    private static final String OTHER_SERVER_GROUP_ID = Ids.build("sg", OTHER_SERVER_GROUP);

    private static String[] SERVERS_IN_OTHER_SERVER_GROUP;

    @BeforeClass
    public static void setUp() throws IOException {
        SERVERS_IN_OTHER_SERVER_GROUP =
            operations.invoke("read-children-resources", Address.host(ConfigUtils.getDefaultHost()),
                Values.of("child-type", "server-config")).listValue()
                .stream()
                .map(ModelNode::asProperty)
                .filter(property -> property.getValue().get("group").asString().equals(OTHER_SERVER_GROUP))
                .map(Property::getName)
                .toArray(String[]::new);
    }

    @AfterClass
    public static void tearDown() throws IOException {
        client.close();
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Before
    public void startServersInServerGroupIfNeeded() throws IOException {
        operations.invoke("start-servers",
            Address.root().and("server-group", OTHER_SERVER_GROUP), Values.of("blocking", true));
    }

    private void verifyServersAreStopped() throws IOException, InterruptedException {
        for (String serverName : SERVERS_IN_OTHER_SERVER_GROUP) {
            verifyServerHasState(ConfigUtils.getDefaultHost(), serverName, "STOPPED", 15000);
        }
    }

    private void verifyServerHasState(String host, String server, String state, int timeout)
        throws InterruptedException, IOException {
        String currentState;
        for (long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeout);
            System.currentTimeMillis() < endTime; Thread.sleep(200L)) {
            currentState =
                operations.readAttribute(Address.host(host).and("server", server), "server-state").stringValue();
            if (currentState.equals(state)) {
                return;
            }
        }
        Assert.fail("Server: [" + server + "] on host:[" + host + "] "
            + "could not get into the desired server-state:[" + state + "] after a timeout of " + timeout + " seconds");
    }

    @Test
    public void kill() throws IOException, InterruptedException {
        console.finder(NameTokens.RUNTIME, SERVER_GROUP_FINDER_PATH)
            .column(Ids.SERVER_GROUP)
            .selectItem(OTHER_SERVER_GROUP_ID)
            .dropdown()
            .click("Kill");
        console.confirmationDialog().confirm();
        console.verifySuccess();
        verifyServersAreStopped();
    }

    @Test
    public void destroy() throws IOException, InterruptedException {
        console.finder(NameTokens.RUNTIME, SERVER_GROUP_FINDER_PATH)
            .column(Ids.SERVER_GROUP)
            .selectItem(OTHER_SERVER_GROUP_ID)
            .dropdown()
            .click("Destroy");
        console.confirmationDialog().confirm();
        console.verifySuccess();
        verifyServersAreStopped();
    }
}
