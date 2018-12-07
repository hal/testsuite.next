package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEAD_LETTER_ADDRESS;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_ADDRESS_SETTING;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.AS_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.AS_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.AS_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.addressSettingAddress;

@RunWith(Arquillian.class)
public class AddressSettingTest extends AbstractServerDestinationsTest {

    @Test
    public void create() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_ADDRESS_SETTING + ID_DELIMITER + ITEM);
        TableFragment table = page.getAddressSettingTable();
        FormFragment form = page.getAddressSettingForm();
        table.bind(form);

        crudOperations.create(addressSettingAddress(SRV_UPDATE, AS_CREATE), table, AS_CREATE);
    }

    @Test
    public void editDeadLetterAddress() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_ADDRESS_SETTING + ID_DELIMITER + ITEM);
        TableFragment table = page.getAddressSettingTable();
        FormFragment form = page.getAddressSettingForm();
        table.bind(form);

        table.select(AS_UPDATE);
        crudOperations.update(addressSettingAddress(SRV_UPDATE, AS_UPDATE), form, DEAD_LETTER_ADDRESS);
    }

    @Test
    public void remove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_ADDRESS_SETTING + ID_DELIMITER + ITEM);
        TableFragment table = page.getAddressSettingTable();
        FormFragment form = page.getAddressSettingForm();
        table.bind(form);

        crudOperations.delete(addressSettingAddress(SRV_UPDATE, AS_DELETE), table, AS_DELETE);
    }

}
