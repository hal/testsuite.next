package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.PATTERN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ROLE;
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

@RunWith(Arquillian.class)
public class SecuritySettingTest extends AbstractServerDestinationsTest {

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
