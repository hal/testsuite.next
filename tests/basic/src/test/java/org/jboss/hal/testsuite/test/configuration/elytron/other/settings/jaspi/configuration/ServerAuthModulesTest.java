package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.jaspi.configuration;

import java.io.IOException;
import java.util.Collections;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.SERVER_AUTH_MODULES;

@RunWith(Arquillian.class)
public class ServerAuthModulesTest extends AbstractJASPIConfigurationTest {

    private static final String JASPI_CONFIGURATION = "jaspi-configuration-server-auth-modules";
    private static final String SERVER_AUTH_MODULE_CREATE =
        "server-auth-module-create-" + Random.name();
    private static final String SERVER_AUTH_MODULE_DELETE =
        "server-auth-module-delete-" + Random.name();
    private static final String SERVER_AUTH_MODULE_CLASS_NAME_UPDATE =
        "server-auth-modules-class-name-update-" + Random.name();
    private static String SERVER_AUTH_MODULE_MODULE_UPDATE =
        "server-auth-module-module-update-" + Random.name();
    private static String SERVER_AUTH_MODULE_FLAG_UPDATE =
        "server-auth-module-flag-update-" + Random.name();
    private static String SERVER_AUTH_MODULE_OPTIONS_UPDATE =
        "server-auth-module-options-update-" + Random.name();

    @BeforeClass
    public static void createResources() throws IOException {
        createJASPIConfigurationWithServerAuthModuleClassName(JASPI_CONFIGURATION, SERVER_AUTH_MODULE_DELETE,
            SERVER_AUTH_MODULE_CLASS_NAME_UPDATE, SERVER_AUTH_MODULE_MODULE_UPDATE, SERVER_AUTH_MODULE_FLAG_UPDATE,
            SERVER_AUTH_MODULE_OPTIONS_UPDATE);
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
            formFragment -> formFragment.text("class-name", SERVER_AUTH_MODULE_CREATE),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(SERVER_AUTH_MODULES,
                new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty("class-name",
                    SERVER_AUTH_MODULE_CREATE).build()));
    }

    @Test
    public void delete() throws Exception {
        crudOperations.delete(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            page.getJaspiServerAuthModulesTable(), SERVER_AUTH_MODULE_DELETE,
            resourceVerifier -> resourceVerifier.verifyListAttributeDoesNotContainValue(SERVER_AUTH_MODULES,
                new ServerAuthModuleBuilder().withClassName(SERVER_AUTH_MODULE_DELETE).build()));
    }

    @Test
    public void editClassName() throws Exception {
        String className = Random.name();
        page.getJaspiServerAuthModulesTable().select(SERVER_AUTH_MODULE_CLASS_NAME_UPDATE);
        crudOperations.update(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            page.getJaspiServerAuthModulesForm(), formFragment -> formFragment.text("class-name", className),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(SERVER_AUTH_MODULES,
                new ServerAuthModuleBuilder().withClassName(className).build()));
    }

    @Test
    public void editFlag() throws Exception {
        String[] flags = {"required", "requisite", "sufficient", "optional"};
        String flag = flags[Random.number(0, flags.length)];
        page.getJaspiServerAuthModulesTable().select(SERVER_AUTH_MODULE_FLAG_UPDATE);
        crudOperations.update(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            page.getJaspiServerAuthModulesForm(), formFragment -> formFragment.select("flag", flag),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(SERVER_AUTH_MODULES,
                new ServerAuthModuleBuilder().withClassName(SERVER_AUTH_MODULE_FLAG_UPDATE)
                    .withFlag(flag)
                    .build()));
    }

    @Test
    public void editModule() throws Exception {
        String module = Random.name();
        page.getJaspiServerAuthModulesTable().select(SERVER_AUTH_MODULE_MODULE_UPDATE);
        crudOperations.update(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            page.getJaspiServerAuthModulesForm(), formFragment -> formFragment.text("module", module),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(SERVER_AUTH_MODULES,
                new ServerAuthModuleBuilder().withClassName(SERVER_AUTH_MODULE_MODULE_UPDATE)
                    .withModule(module).build()));
    }

    @Test
    public void editOptions() throws Exception {
        String key = Random.name();
        String value = Random.name();
        page.getJaspiServerAuthModulesTable().select(SERVER_AUTH_MODULE_OPTIONS_UPDATE);
        crudOperations.update(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION),
            page.getJaspiServerAuthModulesForm(), formFragment -> formFragment.properties("options").add(key, value),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(SERVER_AUTH_MODULES,
                new ServerAuthModuleBuilder().withClassName(SERVER_AUTH_MODULE_OPTIONS_UPDATE)
                    .withOptions(Collections.singletonList(new Property(key, new ModelNode(value))))
                    .build()));
    }
}
