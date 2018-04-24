/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.testsuite.test.configuration.elytron.factoriestransformers;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REQUIRED;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class OtherFactoriesTest extends AbstractFactoriesTransformersTest {

    // --------------- kerberos-security-factory

    @Test
    public void kerberosSecurityFactoryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();

        console.waitNoNotification();
        crud.create(kerberosSecurityFactoryAddress(KERB_CREATE), table, f -> {
            f.text(NAME, KERB_CREATE);
            f.text(PATH, ANY_STRING);
            f.text(PRINCIPAL, ANY_STRING);
        });
    }

    @Test
    public void kerberosSecurityFactoryTryCreate() {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, KERB_CREATE, PATH);
    }

    @Test
    public void kerberosSecurityFactoryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();
        FormFragment form = page.getKerberosSecurityForm();
        table.bind(form);

        table.select(KERB_UPDATE);
        crud.update(kerberosSecurityFactoryAddress(KERB_UPDATE), form, REQUIRED, true);
    }

    @Test
    public void kerberosSecurityFactoryTryUpdate() {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();
        FormFragment form = page.getKerberosSecurityForm();
        table.bind(form);

        table.select(KERB_TRY_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void kerberosSecurityFactoryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(OTHER_FACTORIES_ITEM, KERBEROS_SECURITY_FACTORY_ITEM);
        TableFragment table = page.getKerberosSecurityTable();
        crud.delete(kerberosSecurityFactoryAddress(KERB_DELETE), table, KERB_DELETE);
    }
}
