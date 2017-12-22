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
package org.jboss.hal.testsuite.test.configuration.logging;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.resources.Names;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.page.Places;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LOGGING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.configurationSubsystemPath;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.PROFILE_CREATE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.PROFILE_DELETE;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.PROFILE_READ;
import static org.jboss.hal.testsuite.test.configuration.logging.LoggingFixtures.profileAddress;
import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
public class ProfileFinderTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeClass() throws Exception {
        operations.add(profileAddress(PROFILE_READ));
        operations.add(profileAddress(PROFILE_DELETE));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(profileAddress(PROFILE_CREATE));
        operations.removeIfExists(profileAddress(PROFILE_READ));
        operations.removeIfExists(profileAddress(PROFILE_DELETE));
    }

    @Inject private Console console;
    private ColumnFragment column;

    @Before
    public void setUp() throws Exception {
        column = console.finder(NameTokens.CONFIGURATION, configurationSubsystemPath(LOGGING)
                .append(Ids.LOGGING_CONFIG_AND_PROFILES, Ids.asId(Names.LOGGING_PROFILES)))
                .column(Ids.LOGGING_PROFILE);
    }

    @Test
    public void create() throws Exception {
        AddResourceDialogFragment dialog = column.add();
        dialog.getForm().text(NAME, PROFILE_CREATE);
        dialog.add();

        console.verifySuccess();
        new ResourceVerifier(profileAddress(PROFILE_CREATE), client)
                .verifyExists();
    }

    @Test
    public void select() {
        column.selectItem(Ids.loggingProfile(PROFILE_READ));
        PlaceRequest place = Places.finderPlace(NameTokens.CONFIGURATION,
                configurationSubsystemPath(LOGGING)
                        .append(Ids.LOGGING_CONFIG_AND_PROFILES, Ids.asId(Names.LOGGING_PROFILES))
                        .append(Ids.LOGGING_PROFILE, Ids.loggingProfile(PROFILE_READ)));
        console.verify(place);
    }

    @Test
    public void view() {
        column.selectItem(Ids.loggingProfile(PROFILE_READ)).view();
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.LOGGING_PROFILE)
                .with(NAME, PROFILE_READ)
                .build();
        console.verify(placeRequest);
    }

    @Test
    public void delete() throws Exception {
        column.selectItem(Ids.loggingProfile(PROFILE_DELETE))
                .dropdown()
                .click("Remove");
        console.confirmationDialog().confirm();

        console.verifySuccess();
        assertFalse(column.containsItem(Ids.loggingProfile(PROFILE_DELETE)));
        new ResourceVerifier(profileAddress(PROFILE_DELETE), client)
                .verifyDoesNotExist();
    }
}
