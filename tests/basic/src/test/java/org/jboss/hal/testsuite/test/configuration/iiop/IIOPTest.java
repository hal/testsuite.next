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
package org.jboss.hal.testsuite.test.configuration.iiop;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.page.configuration.IIOPPage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.testsuite.page.configuration.IIOPPage.IIOP_PREFIX;
import static org.jboss.hal.testsuite.test.configuration.iiop.IIOPFixtures.*;

@RunWith(Arquillian.class)
public class IIOPTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {

    }

    @AfterClass
    public static void afterClass() throws Exception {
        // restore the original values
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, PERSISTENT_SERVER_ID);
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, EXPORT_CORBALOC);
        operations.writeAttribute(SUBSYSTEM_ADDRESS, SECURITY, IDENTITY);
        operations.writeAttribute(SUBSYSTEM_ADDRESS, TRANSACTIONS, SPEC);
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, AUTH_METHOD);
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, CALLER_PROPAGATION);
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, HIGH_WATER_MARK);
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, ROOT_CONTEXT);
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, PROPERTIES);
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, SUPPORT_SSL);
        operations.undefineAttribute(SUBSYSTEM_ADDRESS, SECURITY_DOMAIN);
    }

    @Inject private Console console;
    @Page private IIOPPage page;
    private FormFragment form;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    @Test
    public void updateOrbPersistentId() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, "orb", Ids.TAB));
        form = page.getOrbForm();

        String persistentServerId = "23";
        form.edit();
        form.text(PERSISTENT_SERVER_ID, persistentServerId);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(PERSISTENT_SERVER_ID, persistentServerId);
    }

    @Test
    public void showOrbSocketBindingSensitive() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, "orb", Ids.TAB));
        form = page.getOrbForm();

        form.showSensitive(SOCKET_BINDING);
        String val = form.value(SOCKET_BINDING);

        Assert.assertEquals(val, IIOP);
    }

    @Test
    public void updateNaming() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, NAMING, Ids.TAB));
        form = page.getNamingForm();

        form.edit();
        form.flip(EXPORT_CORBALOC, false);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(EXPORT_CORBALOC, false);
    }

    @Test
    public void resetNaming() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, NAMING, Ids.TAB));
        form = page.getNamingForm();

        form.reset();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(EXPORT_CORBALOC, true)
                .verifyAttribute(ROOT_CONTEXT, DEFAULT_ROOT_CONTEXT);
    }

    @Test
    public void updateInitializers() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, "initializers", Ids.TAB));
        form = page.getInitializersForm();

        form.edit();
        form.select(SECURITY, ELYTRON);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(SECURITY, ELYTRON);
    }

    @Test
    public void resetInitializers() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, "initializers", Ids.TAB));
        form = page.getInitializersForm();
        form.reset();
        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(SECURITY, NONE)
                .verifyAttribute(TRANSACTIONS, NONE);
    }

    @Test
    public void updateAsContext() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, "as-context", Ids.TAB));
        form = page.getAsContextForm();

        form.edit();
        form.select(AUTH_METHOD, NONE);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(AUTH_METHOD, NONE);
    }

    @Test
    public void resetAsContext() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, "as-context", Ids.TAB));
        form = page.getAsContextForm();
        form.reset();
        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(AUTH_METHOD, USERNAME_PASSWORD);
    }

    @Test
    public void updateSasContext() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, "sas-context", Ids.TAB));
        form = page.getSasContextForm();
        form.edit();
        form.select(CALLER_PROPAGATION, SUPPORTED);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(CALLER_PROPAGATION, SUPPORTED);
    }

    @Test
    public void updateSecurity() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, SECURITY, Ids.TAB));
        form = page.getSecurityForm();
        form.edit();
        form.text(SECURITY_DOMAIN, "bar");
        form.save();
        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(SECURITY_DOMAIN, "bar");
    }

    @Test
    public void updateSecurityInvalidSSLSettings() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, SECURITY, Ids.TAB));
        form = page.getSecurityForm();
        form.edit();
        form.text(CLIENT_SSL_CONTEXT, "foo");
        form.text(SECURITY_DOMAIN, "bar");
        form.trySave();
        form.expectError(CLIENT_SSL_CONTEXT);
        form.expectError(SECURITY_DOMAIN);
    }

    @Test
    public void updateSecurityServerRequireClientSslContext() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, SECURITY, Ids.TAB));
        form = page.getSecurityForm();
        form.edit();
        form.text(CLIENT_SSL_CONTEXT, "foo");
        form.trySave();
        form.expectError(SERVER_SSL_CONTEXT);
    }

    @Test
    public void updateTcp() throws Exception {
        page.getTabs().select(Ids.build(IIOP_PREFIX, GROUP, "tcp", Ids.TAB));
        form = page.getTcpForm();
        int val = 11;
        form.edit();
        form.number(HIGH_WATER_MARK, val);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(HIGH_WATER_MARK, val);
    }

    @Test
    public void updateProperties() throws Exception {
        page.getTabs().select(Ids.build(IIOP_OPENJDK, PROPERTIES, Ids.TAB));
        form = page.getPropertiesForm();

        ModelNode props = new ModelNode();
        props.get("a").set("b");
        props.get("c").set("d");

        form.edit();
        form.properties(PROPERTIES).add(props);
        form.save();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttribute(PROPERTIES, props);
    }

    @Test
    public void resetProperties() throws Exception {
        page.getTabs().select(Ids.build(IIOP_OPENJDK, PROPERTIES, Ids.TAB));
        form = page.getPropertiesForm();

        form.reset();

        console.verifySuccess();
        new ResourceVerifier(SUBSYSTEM_ADDRESS, client)
                .verifyAttributeIsUndefined(PROPERTIES);
    }
}
