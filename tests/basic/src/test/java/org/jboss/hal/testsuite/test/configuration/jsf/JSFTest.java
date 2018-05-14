package org.jboss.hal.testsuite.test.configuration.jsf;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.page.configuration.JSFPage;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

@RunWith(Arquillian.class)
public class JSFTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @AfterClass
    public static void tearDown() throws IOException {
        client.close();
    }

    @Drone
    private WebDriver browser;

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Page
    private JSFPage page;

    @Test
    public void view() {
        console.finder(NameTokens.CONFIGURATION, new FinderPath().append(Ids.CONFIGURATION, Ids.asId(Names.SUBSYSTEMS)))
            .column(Ids.CONFIGURATION_SUBSYSTEM)
            .selectItem(JSFFixtures.JSF)
            .view();
        console.verify(page.getPlaceRequest());
    }

    @Test
    public void updateDefaultJSFImplSlot() throws Exception {
        page.navigate();
        crudOperations.update(JSFFixtures.JSF_ADDRESS, page.getDataForm(), JSFFixtures.DEFAULT_JSF_IMPL_SLOT);
    }

    @Test
    public void toggleDisallowDoctypeDecl() throws Exception {
        page.navigate();
        boolean disallowDoctypeDecl =
            operations.readAttribute(JSFFixtures.JSF_ADDRESS, JSFFixtures.DISALLOW_DOCTYPE_DECL).booleanValue(false);
        crudOperations.update(JSFFixtures.JSF_ADDRESS, page.getDataForm(), JSFFixtures.DISALLOW_DOCTYPE_DECL, !disallowDoctypeDecl);
    }
}
