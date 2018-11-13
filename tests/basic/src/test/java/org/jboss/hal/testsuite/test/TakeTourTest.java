package org.jboss.hal.testsuite.test;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.category.EAP;
import org.jboss.hal.testsuite.category.Standalone;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.TourWizardFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderPath;
import org.jboss.hal.testsuite.page.HomePage;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.page.Places.finderPlace;
import static org.junit.Assert.*;

@Category(EAP.class)
@RunWith(Arquillian.class)
public class TakeTourTest {

    private static final String
        SERVER_GROUPS = "server-groups",
        RUNTIME_TITLE = "Runtime",
        HOMEPAGE_TITLE = "Homepage",
        DEPLOYMENT_TITLE = "Deployment",
        ACCESS_CONTROL_TITLE = "Access Control",
        VIEW_RUNTIME_INFORMATION = "View Runtime information";
    @Page private HomePage homePage;
    @Inject private Console console;
    private TourWizardFragment wizard;

    @Before
    public void openWizard() {
        homePage.navigate();
        wizard = homePage.openTourWizard();
    }

    @Test
    public void closeTest() throws Exception {
        assertTrue(wizard.close().isClosed());
        assertTrue(homePage.openTourWizard().next().next().back().next().close().isClosed());
    }

    @Category(Standalone.class)
    @Test
    public void standaloneWizardNavigation() throws Exception {
        try (OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient()) {
            String
                serverName = new Operations(client).readAttribute(Address.root(), "name").stringValue(),
                standaloneHostName = Ids.build(Ids.STANDALONE_HOST, serverName);

            verifyStatus(HOMEPAGE_TITLE, "Add, remove, and enable Deployments.",
                    finderPlace(NameTokens.HOMEPAGE, null))
            .verifyNext(HOMEPAGE_TITLE, "Handle the Configuration of available subsystems",
                    finderPlace(NameTokens.HOMEPAGE, null))
            .verifyNext(HOMEPAGE_TITLE, VIEW_RUNTIME_INFORMATION,
                    finderPlace(NameTokens.HOMEPAGE, null))
            .verifyNext(DEPLOYMENT_TITLE, "Enable, disable, and view the status of all deployments.",
                    finderPlace(NameTokens.DEPLOYMENTS, null))
            .verifyBack(HOMEPAGE_TITLE, VIEW_RUNTIME_INFORMATION,
                    finderPlace(NameTokens.HOMEPAGE, null))
            .verifyNext(DEPLOYMENT_TITLE, "Enable, disable, and view the status of all deployments.",
                    finderPlace(NameTokens.DEPLOYMENTS, null))
            .verifyNext(DEPLOYMENT_TITLE, "Add a deployment by uploading a new deployment",
                    finderPlace(NameTokens.DEPLOYMENTS, null))
            .verifyNext("Configuration", "View and modify the configuration for each available subsystem.",
                    finderPlace(NameTokens.CONFIGURATION, new FinderPath().append(Ids.CONFIGURATION, "subsystems")))
            .verifyNext(RUNTIME_TITLE, "View runtime information about the server",
                    finderPlace(NameTokens.RUNTIME, null))
            .verifyNext(RUNTIME_TITLE, "View subsystem-specific runtime information",
                    finderPlace(NameTokens.RUNTIME, new FinderPath()
                            .append(Ids.STANDALONE_SERVER_COLUMN, standaloneHostName)))
            .verifyNext(ACCESS_CONTROL_TITLE, "By default, EAP uses simple access controls",
                    finderPlace(NameTokens.ACCESS_CONTROL, null))
            .verifyNext(ACCESS_CONTROL_TITLE, "Add and assign roles to users and groups.",
                    finderPlace(NameTokens.ACCESS_CONTROL, new FinderPath().append(Ids.ACCESS_CONTROL_BROWSE_BY, "users")))
            .verifyNext(ACCESS_CONTROL_TITLE, "Manage the list of roles",
                    finderPlace(NameTokens.ACCESS_CONTROL, new FinderPath().append(Ids.ACCESS_CONTROL_BROWSE_BY, "roles")))
            .verifyFinish();
        }
    }

    @Category(Domain.class)
    @Test
    public void domainWizardNavigation() throws Exception {
        verifyStatus(HOMEPAGE_TITLE, "Add, remove, and assign Deployments to server groups.",
                finderPlace(NameTokens.HOMEPAGE, null))
        .verifyNext(HOMEPAGE_TITLE, "Handle the Configuration of profiles",
                finderPlace(NameTokens.HOMEPAGE, null))
        .verifyNext(HOMEPAGE_TITLE, VIEW_RUNTIME_INFORMATION,
                finderPlace(NameTokens.HOMEPAGE, null))
        .verifyNext(DEPLOYMENT_TITLE, "Add deployments and assign them to server groups",
                finderPlace(NameTokens.DEPLOYMENTS, null))
        .verifyBack(HOMEPAGE_TITLE, VIEW_RUNTIME_INFORMATION,
                finderPlace(NameTokens.HOMEPAGE, null))
        .verifyNext(DEPLOYMENT_TITLE, "Add deployments and assign them to server groups",
                finderPlace(NameTokens.DEPLOYMENTS, null))
        .verifyNext(DEPLOYMENT_TITLE, "Add a deployment to a server group",
                finderPlace(NameTokens.DEPLOYMENTS, new FinderPath().append(Ids.DEPLOYMENT_BROWSE_BY, "content-repository")))
        .verifyNext("Configuration", "List of profiles available",
                finderPlace(NameTokens.CONFIGURATION, new FinderPath().append(Ids.CONFIGURATION, "profiles")))
        .verifyNext("Configuration", "View and modify the configuration",
                finderPlace(NameTokens.CONFIGURATION, new FinderPath().append(Ids.CONFIGURATION, "profiles")
                        .append(Ids.PROFILE, "default")))
        .verifyNext(RUNTIME_TITLE, "Manage servers by hosts or server groups",
                finderPlace(NameTokens.RUNTIME, null))
        .verifyNext(RUNTIME_TITLE, "View server group configuration and perform operations",
                finderPlace(NameTokens.RUNTIME, new FinderPath().append(Ids.DOMAIN_BROWSE_BY, SERVER_GROUPS)))
        .verifyNext(RUNTIME_TITLE, "Create a new server group",
                finderPlace(NameTokens.RUNTIME, new FinderPath().append(Ids.DOMAIN_BROWSE_BY, SERVER_GROUPS)))
        .verifyNext(RUNTIME_TITLE, "View server configuration and perform operations",
                finderPlace(NameTokens.RUNTIME, new FinderPath().append(Ids.DOMAIN_BROWSE_BY, SERVER_GROUPS)
                        .append(Ids.SERVER_GROUP, "sg-main-server-group")))
        .verifyNext(RUNTIME_TITLE, "Create a new server for a host",
                finderPlace(NameTokens.RUNTIME, new FinderPath().append(Ids.DOMAIN_BROWSE_BY, SERVER_GROUPS)
                        .append(Ids.SERVER_GROUP, "sg-main-server-group")))
        .verifyNext(ACCESS_CONTROL_TITLE, "By default, EAP uses simple access controls",
                finderPlace(NameTokens.ACCESS_CONTROL, null))
        .verifyNext(ACCESS_CONTROL_TITLE, "Add and assign roles to users and groups.",
                finderPlace(NameTokens.ACCESS_CONTROL, new FinderPath().append(Ids.ACCESS_CONTROL_BROWSE_BY, "users")))
        .verifyNext(ACCESS_CONTROL_TITLE, "Manage the list of roles",
                finderPlace(NameTokens.ACCESS_CONTROL, new FinderPath().append(Ids.ACCESS_CONTROL_BROWSE_BY, "roles")))
        .verifyFinish();
    }

    private TakeTourTest verifyStatus(String expectedTitle, String expectedContentStart, PlaceRequest expectedPlace) {
        assertEquals(expectedTitle, wizard.getTitle());
        assertTrue("Wizard step content '" + wizard.getContent() + " doesn't start with expected '" + expectedContentStart + "'.",
                wizard.getContent().startsWith(expectedContentStart));
        console.verify(expectedPlace);
        return this;
    }

    private TakeTourTest verifyNext(String expectedTitle, String expectedContentStart, PlaceRequest expectedPlace) {
        wizard.next();
        return verifyStatus(expectedTitle, expectedContentStart, expectedPlace);
    }

    private TakeTourTest verifyBack(String expectedTitle, String expectedContentStart, PlaceRequest expectedPlace) {
        wizard.back();
        return verifyStatus(expectedTitle, expectedContentStart, expectedPlace);
    }

    private TakeTourTest verifyFinish() {
        assertTrue(wizard.finish().isClosed());
        return this;
    }
}
