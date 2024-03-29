package org.jboss.hal.testsuite.test.configuration.io;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.command.BackupAndRestoreAttributes;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.fragment.finder.ServerPreviewFragment;
import org.jboss.hal.testsuite.util.ConfigUtils;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;



@RunWith(Arquillian.class)
@Category(Domain.class)
public class UrlConnectionTestCase {
    private static final String SERVER_GROUP = "server-group";
    private static final String PROFILE = "profile";
    private static final String DEFAULT = "default";

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private static final Operations operations = new Operations(client);

    private static final Address hostPrimary = Address.of("host","primary");
    private static final Address serverGroupMain = Address.of(SERVER_GROUP,"main-server-group");

    private static final BackupAndRestoreAttributes backupHost = new BackupAndRestoreAttributes.Builder(hostPrimary).build();
    private static final BackupAndRestoreAttributes backupOtherServerGroup = new BackupAndRestoreAttributes.Builder(Address.of(SERVER_GROUP,"other-server-group")).build();
    private static final BackupAndRestoreAttributes backupMainServerGroup = new BackupAndRestoreAttributes.Builder(Address.of(SERVER_GROUP,"main-server-group")).build();
    private static final BackupAndRestoreAttributes backupProfile = new BackupAndRestoreAttributes.Builder(Address.of(PROFILE, DEFAULT)).build();
    private static final Administration administration = new Administration(client);

    @BeforeClass
    public static void setUp() throws IOException, TimeoutException, InterruptedException {
        backupHost.backup();
        backupOtherServerGroup.backup();
        backupMainServerGroup.backup();
        backupProfile.backup();

        operations.batch(new Batch().writeAttribute(serverGroupMain,PROFILE, DEFAULT)).assertSuccess();
        operations.batch(new Batch().writeAttribute(serverGroupMain,"socket-binding-group","standard-sockets")).assertSuccess();
        administration.reloadIfRequired();

        operations.batch(new Batch().remove(Address.of(PROFILE,DEFAULT).and("subsystem","undertow").and("server","default-server").and("http-listener","default"))).assertSuccess();
        operations.batch(new Batch().writeAttribute(serverGroupMain.and("jvm",DEFAULT),"heap-size", "256m")).assertSuccess();
        operations.batch(new Batch().writeAttribute(serverGroupMain.and("jvm",DEFAULT),"max-heap-size", "256m")).assertSuccess();
        administration.reloadIfRequired();

    }
    @AfterClass
    public static void tearDown()
            throws IOException, InterruptedException, TimeoutException {
        try {
            backupHost.restore();
            backupOtherServerGroup.restore();
            backupMainServerGroup.restore();
            backupProfile.restore();
            administration.reloadIfRequired();
        } finally {
            client.close();
        }
    }


    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Test
    public void testUrlHttps() throws IOException {
        FinderFragment fragment = console.finder(NameTokens.RUNTIME, new FinderPath()
                .append(Ids.DOMAIN_BROWSE_BY, "hosts")
                .append(Ids.HOST, Ids.build("host", ConfigUtils.getDefaultHost())));
        fragment.column(Ids.SERVER).selectItem(Ids.build(ConfigUtils.getDefaultHost(), ConfigUtils.getDefaultServer()));
        ServerPreviewFragment serverPreviewFragment = fragment.preview(ServerPreviewFragment.class);
        if (!serverPreviewFragment.getUrlAttributeItem().getValueElement().getText().contains("https")) {
            Assert.fail("URL should contain https! See WFLY-12115");
        }
    }
}
