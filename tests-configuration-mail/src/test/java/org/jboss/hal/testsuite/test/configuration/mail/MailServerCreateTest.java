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
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.MailPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.IMAP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.JNDI_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.OUTBOUND_SOCKET_BINDING_REF;
import static org.jboss.hal.dmr.ModelDescriptionConstants.POP3;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SMTP;
import static org.jboss.hal.testsuite.fixtures.MailFixtures.MAIL_SMTP;
import static org.jboss.hal.testsuite.fixtures.MailFixtures.SESSION_CREATE;
import static org.jboss.hal.testsuite.fixtures.MailFixtures.serverAddress;
import static org.jboss.hal.testsuite.fixtures.MailFixtures.sessionAddress;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class MailServerCreateTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(sessionAddress(SESSION_CREATE), Values.of(JNDI_NAME, Random.jndiName(SESSION_CREATE)));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(sessionAddress(SESSION_CREATE));
    }

    @Inject private Console console;
    @Inject private CrudOperations crud;
    @Page private MailPage page;
    private TableFragment table;

    @Before
    public void setUp() throws Exception {
        page.navigate(NAME, SESSION_CREATE);
        console.verticalNavigation().selectPrimary(Ids.MAIL_SERVER_ITEM);
        table = page.getMailServerTable();
    }

    @Test
    public void createIMAP() throws Exception {
        createServer(IMAP);
    }

    @Test
    public void createPOP3() throws Exception {
        createServer(POP3);
    }

    @Test
    public void createSMTP() throws Exception {
        createServer(SMTP);
    }

    private void createServer(String type) throws Exception {
        crud.create(serverAddress(SESSION_CREATE, type), table,
                form -> form.text(OUTBOUND_SOCKET_BINDING_REF, MAIL_SMTP));
    }
}
