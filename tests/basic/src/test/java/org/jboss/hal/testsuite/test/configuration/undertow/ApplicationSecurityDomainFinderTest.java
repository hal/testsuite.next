package org.jboss.hal.testsuite.test.configuration.undertow;

import java.io.IOException;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.fragment.finder.FinderFragment;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;

@RunWith(Arquillian.class)
public class ApplicationSecurityDomainFinderTest {

    @Inject
    private Console console;

    @Drone
    private WebDriver browser;

    private ColumnFragment applicationSecurityColumn;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations operations = new Operations(client);

    private static final String APPLICATION_SECURITY_DOMAIN_NAME_CREATE =
        "new_application_security_domain_" + RandomStringUtils.randomAlphanumeric(7);

    private static final String APPLICATION_SECURITY_DOMAIN_NAME_REMOVE =
        "application_security_domain_to_be_removed" + RandomStringUtils.randomAlphanumeric(7);

    private static final String APPLICATION_SECURITY_DOMAIN_NAME_REFRESH =
        "application_security_domain_to_be_refreshed" + RandomStringUtils.randomAlphanumeric(7);

    private static final String APPLICATION_SECURITY_DOMAIN_NAME_VIEW =
        "application_security_domain_to_be_viewed" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void setUp() throws IOException {
        createApplicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_REMOVE);
        createApplicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_VIEW);
    }

    private static void createApplicationSecurityDomain(String applicationSecurityDomainNameRemove) throws IOException {
        operations.add(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(applicationSecurityDomainNameRemove),
            Values.of(ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, "application-http-authentication"));
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        operations.removeIfExists(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_CREATE));
        operations.removeIfExists(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_REMOVE));
        operations.removeIfExists(ApplicationSecurityDomainFixtures.applicationSecurityDomain(
            APPLICATION_SECURITY_DOMAIN_NAME_REFRESH));
        operations.removeIfExists(
            ApplicationSecurityDomainFixtures.applicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_VIEW));
    }

    @Before
    public void initColumn() {
        applicationSecurityColumn =
            console.finder(NameTokens.CONFIGURATION, FinderFragment.configurationSubsystemPath(NameTokens.UNDERTOW)
                .append(Ids.UNDERTOW_SETTINGS, "application-security-domain"))
                .column(Ids.UNDERTOW_APP_SECURITY_DOMAIN);
    }

    @Test
    public void create() {
        AddResourceDialogFragment addResourceDialogFragment = applicationSecurityColumn.add();
        FormFragment formFragment = addResourceDialogFragment.getForm();
        formFragment.text(NAME, APPLICATION_SECURITY_DOMAIN_NAME_CREATE);
        formFragment.text(ApplicationSecurityDomainFixtures.HTTP_AUTHENTICATION_FACTORY, "application-http-authentication");
        addResourceDialogFragment.add();
        console.verifySuccess();
        Assert.assertTrue("Newly added application security domain should be present in the column",
            applicationSecurityColumn.containsItem(
                Ids.undertowApplicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_CREATE)));
    }

    @Test
    public void refresh() throws IOException {
        Assert.assertFalse("Application security domain should not be visible before refresh",
            applicationSecurityColumn.containsItem(
                Ids.undertowApplicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_REFRESH)));
        createApplicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_REFRESH);
        applicationSecurityColumn.refresh();
        Assert.assertTrue("Application security domain should be visible after refresh",
            applicationSecurityColumn.containsItem(Ids.undertowApplicationSecurityDomain(
                APPLICATION_SECURITY_DOMAIN_NAME_REFRESH)));
    }

    @Test
    public void remove() {
        Assert.assertTrue("Application security domain should be visible before removal",
            applicationSecurityColumn.containsItem(
                Ids.undertowApplicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_REMOVE)));
        applicationSecurityColumn.selectItem(
            Ids.undertowApplicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_REMOVE))
            .dropdown().click("Remove");
        console.confirmationDialog().confirm();
        console.verifySuccess();
        Assert.assertFalse("Application security domain should not be present in the column anymore",
            applicationSecurityColumn.containsItem(
                Ids.undertowApplicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_REMOVE)));
    }

    @Test
    public void view() {
        applicationSecurityColumn.selectItem(Ids.undertowApplicationSecurityDomain(APPLICATION_SECURITY_DOMAIN_NAME_VIEW))
            .view();
        console.verify(new PlaceRequest.Builder().nameToken(NameTokens.UNDERTOW_APPLICATION_SECURITY_DOMAIN)
            .with("name", APPLICATION_SECURITY_DOMAIN_NAME_VIEW).build());
    }
}
