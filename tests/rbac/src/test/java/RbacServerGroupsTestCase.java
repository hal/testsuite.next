import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.Constants;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

@RunWith(Arquillian.class)
@Category({Domain.class})
public class RbacServerGroupsTestCase {

    private static final String ACCESS_ADDR = "access";
    private static final String ACCESS_AUTHORIZATION_ADDR = "authorization";
    private static final String ROLE_MAPPING_ADDR = "role-mapping";
    private static final String MAIN_GROUP_DEPLOYER_ROLE_NAME = "MainGroupDeployer";
    private static final String INCLUDE_ADDR = "include";
    private static final String ADMIN_USERNAME = "admin";
    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "test";

    private static OnlineManagementClient client = ManagementClientProvider.withoutDefaultHost();
    private static OnlineManagementClient hostClient = ManagementClientProvider.createOnlineManagementClient();
    private static Administration administration = new Administration(hostClient);
    private static Operations hostOperations = new Operations(hostClient);
    private static Operations operations = new Operations(client);


    private static final String SERVER_GROUP = "sg-main-server-group";

    @Drone
    private WebDriver browser;
    @Inject
    private Console console;


    @BeforeClass
    public static void setUp() throws Exception {
        operations = new Operations(client);
        hostOperations = new Operations(hostClient);
        administration = new Administration(hostClient);


        execute(hostOperations, Constants.WRITE_ATTRIBUTE_OPERATION,
                Address.coreService(Ids.MANAGEMENT)
                   .and("management-interface", "http-interface"),
                Values.of(Constants.NAME, "security-realm")
                   .and(Constants.VALUE, "ManagementRealm"));
        execute(operations, Constants.ADD,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR)
                   .and(ROLE_MAPPING_ADDR, "SuperUser")
                   .and(INCLUDE_ADDR, ADMIN_USERNAME),
                Values.of(Constants.NAME, ADMIN_USERNAME)
                   .and("type", "USER"));
        execute(operations, Constants.WRITE_ATTRIBUTE_OPERATION,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR),
                Values.of(Constants.NAME, "provider")
                   .and(Constants.VALUE, "rbac"));


        execute(operations, Constants.ADD,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR)
                   .and("server-group-scoped-role", MAIN_GROUP_DEPLOYER_ROLE_NAME),
                Values.of("base-role", "Deployer")
                   .andList("server-groups", "main-server-group"));
        execute(operations, Constants.ADD,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR)
                   .and(ROLE_MAPPING_ADDR, MAIN_GROUP_DEPLOYER_ROLE_NAME));
        execute(operations, Constants.ADD,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR)
                   .and(ROLE_MAPPING_ADDR, MAIN_GROUP_DEPLOYER_ROLE_NAME)
                   .and(INCLUDE_ADDR, TEST_USERNAME),
                Values.of(Constants.NAME, TEST_USERNAME)
                   .and("type", "USER"));

        execute(operations, Constants.ADD,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR)
                   .and(ROLE_MAPPING_ADDR, "SuperUser")
                   .and(INCLUDE_ADDR, ADMIN_USERNAME),
                Values.of(Constants.NAME, ADMIN_USERNAME)
                   .and("type", "USER"));


        administration.reload();
    }

    @AfterClass
    public static void rollBack() throws Exception {
        execute(hostOperations,Constants.UNDEFINE_ATTRIBUTE_OPERATION,
                Address.coreService(Ids.MANAGEMENT)
                   .and("management-interface", "http-interface"),
                Values.of(Constants.NAME, "security-realm"));

        execute(operations, Constants.WRITE_ATTRIBUTE_OPERATION,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR),
                Values.of(Constants.NAME, "provider")
                   .and(Constants.VALUE, "simple"));

        execute(operations, Constants.REMOVE_OPERATION,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR)
                   .and(ROLE_MAPPING_ADDR, "SuperUser")
                   .and(INCLUDE_ADDR, ADMIN_USERNAME));

        execute(operations, Constants.REMOVE_OPERATION,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR)
                   .and(ROLE_MAPPING_ADDR, MAIN_GROUP_DEPLOYER_ROLE_NAME));
        execute(operations, Constants.REMOVE_OPERATION,
                Address.coreService(Ids.MANAGEMENT)
                   .and(ACCESS_ADDR, ACCESS_AUTHORIZATION_ADDR)
                   .and("server-group-scoped-role", MAIN_GROUP_DEPLOYER_ROLE_NAME));

        administration.reload();
    }

    private static void execute(Operations op, String name, Address a) throws IOException {
        op.invoke(name, a);
    }

    private static void execute(Operations op, String name, Address a, Values v) throws IOException {
        op.invoke(name, a, v);
    }

    @Test
    public void testMenuItems() {
        // authenticate as test/test. This hack doesn't work with safari
        Assume.assumeFalse("safari".equals(System.getProperty("browser")));
        browser.get("http://" + TEST_USERNAME + ":" + TEST_PASSWORD + "@localhost:9990/management");

        FinderFragment fragment;
        Assert.assertNotNull(console);
        fragment = console.finder(NameTokens.DEPLOYMENTS, new FinderPath()
           .append(Ids.DEPLOYMENT_BROWSE_BY, "server-groups")
           .append(Ids.DEPLOYMENT_SERVER_GROUP, SERVER_GROUP));

        ColumnFragment itemFragment = fragment.column(Ids.SERVER_GROUP_DEPLOYMENT);
        Assert.assertTrue(itemFragment.hasDropdownActions(Ids.SERVER_GROUP_DEPLOYMENT_ADD_ACTIONS,
                                                         Ids.SERVER_GROUP_DEPLOYMENT_UPLOAD,
                                                         Ids.SERVER_GROUP_DEPLOYMENT_UNMANAGED_ADD,
                                                         Ids.SERVER_GROUP_DEPLOYMENT_ADD));
    }
}