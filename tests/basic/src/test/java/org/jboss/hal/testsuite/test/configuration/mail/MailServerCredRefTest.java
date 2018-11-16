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
package org.jboss.hal.testsuite.test.configuration.mail;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.CredentialReference;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.MailPage;
import org.jboss.hal.testsuite.test.configuration.CredentialReferenceTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.test.configuration.mail.MailFixtures.MAIL_SMTP;
import static org.jboss.hal.testsuite.test.configuration.mail.MailFixtures.SESSION_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.mail.MailFixtures.serverAddress;
import static org.jboss.hal.testsuite.test.configuration.mail.MailFixtures.sessionAddress;

@RunWith(Arquillian.class)
public class MailServerCredRefTest extends CredentialReferenceTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(sessionAddress(SESSION_UPDATE), Values.of(JNDI_NAME, Random.jndiName(SESSION_UPDATE)));
        operations.add(serverAddress(SESSION_UPDATE, SMTP), Values.of(OUTBOUND_SOCKET_BINDING_REF, MAIL_SMTP));
        operations.writeAttribute(serverAddress(SESSION_UPDATE, SMTP),
                CREDENTIAL_REFERENCE, CredentialReference.storeAlias());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(sessionAddress(SESSION_UPDATE));
    }

    @Inject private Console console;
    @Page private MailPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, SESSION_UPDATE);
        console.waitNoNotification();
        console.verticalNavigation().selectPrimary(Ids.MAIL_SERVER_ITEM);

        page.getMailServerTabs().select(Ids.build(Ids.MAIL_SERVER, CREDENTIAL_REFERENCE, Ids.TAB));
        form = page.getMailServerCrForm();
        page.getMailServerTable().bind(form);
        page.getMailServerTable().select(SMTP.toUpperCase());
    }

    @Override
    protected FormFragment form() {
        return form;
    }

    @Override
    protected Address address() {
        return serverAddress(SESSION_UPDATE, SMTP);
    }
}
