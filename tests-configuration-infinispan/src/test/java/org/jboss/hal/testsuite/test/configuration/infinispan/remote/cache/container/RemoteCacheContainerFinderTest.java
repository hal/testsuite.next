package org.jboss.hal.testsuite.test.configuration.infinispan.remote.cache.container;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.creaper.command.RemoveSocketBinding;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.INFINISPAN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.REMOTE_CC_CREATE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.REMOTE_CC_DELETE;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.REMOTE_CC_READ;
import static org.jboss.hal.testsuite.fixtures.InfinispanFixtures.remoteCacheContainerAddress;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class RemoteCacheContainerFinderTest extends AbstractRemoteCacheContainerTest {

    private static final String REMOTE_SOCKET_BINDING_CREATE = "remote-socket-binding-create-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_READ = "remote-socket-binding-read-" + Random.name();
    private static final String REMOTE_SOCKET_BINDING_DELETE = "remote-socket-binding-delete-" + Random.name();

    @BeforeClass
    public static void beforeClass() throws Exception {
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_CREATE);
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_READ);
        createRemoteSocketBinding(REMOTE_SOCKET_BINDING_DELETE);
        createRemoteCacheContainer(REMOTE_CC_READ, REMOTE_SOCKET_BINDING_READ);
        createRemoteCacheContainer(REMOTE_CC_DELETE, REMOTE_SOCKET_BINDING_DELETE);
        administration.reloadIfRequired();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CC_CREATE));
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CC_READ));
        operations.removeIfExists(remoteCacheContainerAddress(REMOTE_CC_DELETE));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_CREATE));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_READ));
        client.apply(new RemoveSocketBinding(REMOTE_SOCKET_BINDING_DELETE));
        administration.reloadIfRequired();
    }

    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        browser.navigate().refresh();
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(INFINISPAN))
            .column(Ids.CACHE_CONTAINER);
    }

    @Test
    public void create() throws Exception {
        String defaultRemoteCluster = Random.name();
        column.dropdownAction("cc-add-actions", "rcc-add");
        AddResourceDialogFragment dialog = console.addResourceDialog();
        FormFragment formFragment = dialog.getForm();
        formFragment.text(NAME, REMOTE_CC_CREATE);
        formFragment.text("default-remote-cluster", defaultRemoteCluster);
        formFragment.list("socket-bindings").add(REMOTE_SOCKET_BINDING_CREATE);
        dialog.add();
        console.verifySuccess();
        assertTrue(column.containsItem(remoteCacheContainerId(REMOTE_CC_CREATE)));
        new ResourceVerifier(remoteCacheContainerAddress(REMOTE_CC_CREATE), client).verifyExists();
        administration.reloadIfRequired();
    }

    private String remoteCacheContainerId(String name) {
        return Ids.build("rcc", name);
    }

    @Test
    public void read() {
        assertTrue(column.containsItem(remoteCacheContainerId(REMOTE_CC_READ)));
    }

    @Test
    public void view() {
        column.selectItem(remoteCacheContainerId(REMOTE_CC_READ)).view();
        PlaceRequest placeRequest =
            new PlaceRequest.Builder().nameToken("remote-cache-container").with("name", REMOTE_CC_READ).build();
        console.verify(placeRequest);
    }

    @Test
    public void delete() throws Exception {
        column.selectItem(remoteCacheContainerId(REMOTE_CC_DELETE)).dropdown().click("Remove");
        console.confirmationDialog().confirm();
        console.verifySuccess();
        assertFalse(column.containsItem(remoteCacheContainerId(REMOTE_CC_DELETE)));
        new ResourceVerifier(remoteCacheContainerAddress(REMOTE_CC_DELETE), client).verifyDoesNotExist();
        administration.reloadIfRequired();
    }
}
