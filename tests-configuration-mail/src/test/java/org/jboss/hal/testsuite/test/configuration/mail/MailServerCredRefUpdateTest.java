/*
 * Copyright 2015-2022 Red Hat, Inc, and individual contributors.
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
import org.jboss.hal.testsuite.page.configuration.MailPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;


import static org.jboss.hal.dmr.ModelDescriptionConstants.*;

/**
 * @author Petr Adamec
 */
@RunWith(Arquillian.class)
public class MailServerCredRefUpdateTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Administration administration = new Administration(client);

    @After
    public void clear() throws Exception {
        page.getMailServerCrForm().remove();
        administration.reload();
    }

    @Inject private Console console;
    @Page private MailPage page;


    /**
     * The first navigate browser to Configuration > Subsystem > Mail > Mail Session > default > Server > SMTP
     * Then create credential reference with empty store and alias. Only clear-text will set (random value).
     * @throws Exception During reload console can be thrown Exception.
     */
    @Before
    public void setUp() throws Exception {
        //navigate to Configuration > Subsystem > Mail > Mail Session > default > Server > SMTP
        page.navigateToSMTP("default");

        //Create credential resource with random value in CLEAR_TEXT. Other attributes will be empty
        page.getMailServerCrForm().getRoot().findElement(By.className("btn-primary")).click();
        console.waitNoNotification();
        console.addResourceDialog().getForm().text(CLEAR_TEXT, Random.name());
        console.addResourceDialog().add();
        console.waitNoNotification();
        administration.reload();
    }


    /**
     * Test if <i>credential reference<i/> (only with <i>>clear-text<i/ set) can be update like erase <i>clear-text<i/> and set <i>store<i/> and <i>alias<i/>.
     * Test for <a href="https://issues.redhat.com/browse/HAL-1760">HAL-1760</a>
     */
    @Test
    public void eraseClearTextAndSetStoreAndAlias() {
        String store = Ids.build(STORE, Random.name());
        String alias = Ids.build(ALIAS, Random.name());
        page.getMailServerCrForm().edit();
        page.getMailServerCrForm().text(STORE, store);
        page.getMailServerCrForm().text(ALIAS, alias);
        page.getMailServerCrForm().text(CLEAR_TEXT, "");
        page.getMailServerCrForm().save();
        Assert.assertTrue("Test can't update credential reference with empty CLEAR_TEXT -  maybe due to HAL-1760. ", console.verifyNoError());
    }

}
