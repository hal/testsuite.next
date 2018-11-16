package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.jaspi.configuration;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

@RunWith(Arquillian.class)
public class JASPIConfigurationTest extends AbstractJASPIConfigurationTest {

    private static final String JASPI_CONFIGURATION_CREATE = "jaspi-configuration-create-" + Random.name();
    private static final String JASPI_CONFIGURATION_UPDATE = "jaspi-configuration-update-" + Random.name();
    private static final String JASPI_CONFIGURATION_DELETE = "jaspi-configuration-delete-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException {
        createJASPIConfiguration(JASPI_CONFIGURATION_UPDATE);
        createJASPIConfiguration(JASPI_CONFIGURATION_DELETE);
    }

    @AfterClass
    public static void removeResources() throws IOException, OperationException {
        operations.removeIfExists(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION_CREATE));
        operations.removeIfExists(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION_UPDATE));
        operations.removeIfExists(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION_DELETE));
    }

    @Before
    public void navigateToJASPIConfiguration() {
        page.navigate();
        console.verticalNavigation()
            .selectSecondary(ElytronFixtures.OTHER_ITEM, ElytronFixtures.JASPI_CONFIGURATION_ITEM);
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION_CREATE),
            page.getJaspiConfigurationTable(), formFragment -> {
                formFragment.text("name", JASPI_CONFIGURATION_CREATE);
                formFragment.text("class-name", Random.name());
            }, ResourceVerifier::verifyExists);
    }

    @Test
    public void remove() throws Exception {
        crudOperations.delete(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION_DELETE),
            page.getJaspiConfigurationTable(), JASPI_CONFIGURATION_DELETE);
    }

    @Test
    public void editApplicationContext() throws Exception {
        page.getJaspiConfigurationTable().select(JASPI_CONFIGURATION_UPDATE);
        crudOperations.update(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION_UPDATE),
            page.getJaspiConfigurationForm(), "application-context");
    }

    @Test
    public void editDescription() throws Exception {
        page.getJaspiConfigurationTable().select(JASPI_CONFIGURATION_UPDATE);
        crudOperations.update(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION_UPDATE),
            page.getJaspiConfigurationForm(), "description");
    }

    @Test
    public void editLayer() throws Exception {
        page.getJaspiConfigurationTable().select(JASPI_CONFIGURATION_UPDATE);
        crudOperations.update(ElytronFixtures.jaspiConfigurationAddress(JASPI_CONFIGURATION_UPDATE),
            page.getJaspiConfigurationForm(), "layer");
    }
}
