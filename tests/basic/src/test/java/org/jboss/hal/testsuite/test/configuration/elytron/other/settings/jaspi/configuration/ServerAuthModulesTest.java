package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.jaspi.configuration;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

@RunWith(Arquillian.class)
public class ServerAuthModulesTest extends AbstractJASPIConfigurationTest {

    private static final String JASPI_CONFIGURATION = "jaspi-configuration-server-auth-modules";
    private static String SERVER_AUTH_MODULE_CLASS_NAME_UPDATE =
        "server-auth-modules-class-name-update-" + Random.name();
    private static final String SERVER_AUTH_MODULE_CLASS_NAME_CREATE =
        "server-auth-module-class-name-create-" + Random.name();
    private static final String SERVER_AUTH_MODULE_CLASS_NAME_DELETE =
        "server-auth-module-class-name-delete-" + Random.name();

    @BeforeClass
    public static void createResources() throws IOException {
        createJASPIConfigurationWithServerAuthModuleClassName(JASPI_CONFIGURATION, SERVER_AUTH_MODULE_CLASS_NAME_UPDATE);
        operations.writeListAttribute(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            "server-auth-modules", new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty("class-name",
                SERVER_AUTH_MODULE_CLASS_NAME_DELETE).build()).assertSuccess();
    }

    @AfterClass
    public static void removeResources() throws IOException, OperationException {
        operations.removeIfExists(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION));
    }

    @Before
    public void navigateToServerAuthModules() {
        page.navigate();
        console.verticalNavigation()
            .selectSecondary(ElytronFixtures.OTHER_ITEM, ElytronFixtures.JASPI_CONFIGURATION_ITEM);
        page.getJaspiConfigurationTable().action(JASPI_CONFIGURATION, "Server Auth Modules");
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            page.getJaspiServerAuthModulesTable(),
            formFragment -> formFragment.text("class-name", SERVER_AUTH_MODULE_CLASS_NAME_CREATE),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue("server-auth-modules",
                new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty("class-name",
                    SERVER_AUTH_MODULE_CLASS_NAME_CREATE).build()));
    }

    @Test
    public void delete() throws Exception {
        crudOperations.delete(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            page.getJaspiServerAuthModulesTable(), SERVER_AUTH_MODULE_CLASS_NAME_DELETE,
            resourceVerifier -> resourceVerifier.verifyListAttributeDoesNotContainValue("server-auth-modules",
                new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty("class-name",
                    SERVER_AUTH_MODULE_CLASS_NAME_DELETE).build()));
    }

    @Test
    public void editClassName() throws Exception {
        String className = Random.name();
        page.getJaspiServerAuthModulesTable().select(SERVER_AUTH_MODULE_CLASS_NAME_UPDATE);
        crudOperations.update(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            page.getJaspiServerAuthModulesForm(), formFragment -> formFragment.text("class-name", className),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue("server-auth-modules",
                new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty("class-name", className).build()));
        ServerAuthModulesTest.SERVER_AUTH_MODULE_CLASS_NAME_UPDATE = className;
    }

    @Test
    public void editModule() throws Exception {
        String module = Random.name();
        page.getJaspiServerAuthModulesTable().select(SERVER_AUTH_MODULE_CLASS_NAME_UPDATE);
        crudOperations.update(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            page.getJaspiServerAuthModulesForm(), formFragment -> formFragment.text("module", module),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue("server-auth-modules",
                new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty("class-name",
                    SERVER_AUTH_MODULE_CLASS_NAME_UPDATE)
                    .addProperty("module", module).build()));
    }

    @Test
    public void editOptions() {

    }
}
