package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.OperationException;

import static org.jboss.hal.dmr.ModelDescriptionConstants.PATTERN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ROLE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.CONSUME;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.MESSAGING_SECURITY_SETTING_ROLE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.ROLE_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SECSET_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SECSET_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SECSET_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.securitySettingAddress;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.securitySettingRoleAddress;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.serverAddress;

@RunWith(Arquillian.class)
public class SecuritySettingTest extends AbstractServerDestinationsTest {

    @BeforeClass
    public static void createResources() throws IOException {
        createServer(SRV_UPDATE);
        createSecuritySettingsWithRoles(SRV_UPDATE, SECSET_DELETE);
        createSecuritySettingsWithRoles(SRV_UPDATE, SECSET_UPDATE);
    }

    private static void createSecuritySettingsWithRoles(String serverName, String securitySetting) throws IOException {
        Batch securitySettingBatch = new Batch();
        securitySettingBatch.add(securitySettingAddress(serverName, securitySetting));
        securitySettingBatch.add(
            securitySettingRoleAddress(serverName, securitySetting, ROLE_CREATE));
        operations.batch(securitySettingBatch).assertSuccess();
    }

    @AfterClass
    public static void removeResources() throws IOException, OperationException {
        operations.removeIfExists(serverAddress(SRV_UPDATE));
    }

    @Before
    public void navigate() {
        page.navigate(SERVER, SRV_UPDATE);
    }

    @Test
    public void create() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SECURITY_SETTING_ROLE + ID_DELIMITER + ITEM);
        TableFragment table = page.getSecuritySettingTable();
        FormFragment form = page.getSecuritySettingForm();
        table.bind(form);

        crudOperations.create(securitySettingAddress(SRV_UPDATE, SECSET_CREATE), table,
            formFragment -> {
                formFragment.text(PATTERN, SECSET_CREATE);
                formFragment.text(ROLE, Random.name());
            }
        );
    }

    @Test
    public void editConsume() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SECURITY_SETTING_ROLE + ID_DELIMITER + ITEM);
        TableFragment table = page.getSecuritySettingTable();
        FormFragment form = page.getSecuritySettingForm();
        table.bind(form);

        table.select(SECSET_UPDATE);
        crudOperations.update(securitySettingRoleAddress(SRV_UPDATE, SECSET_UPDATE, ROLE_CREATE), form, CONSUME, true);
    }

    @Test
    public void remove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_SECURITY_SETTING_ROLE + ID_DELIMITER + ITEM);
        TableFragment table = page.getSecuritySettingTable();
        FormFragment form = page.getSecuritySettingForm();
        table.bind(form);

        crudOperations.delete(securitySettingAddress(SRV_UPDATE, SECSET_DELETE), table, SECSET_DELETE);
    }
}
