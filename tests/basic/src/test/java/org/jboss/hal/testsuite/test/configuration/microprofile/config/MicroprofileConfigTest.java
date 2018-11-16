/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.hal.testsuite.test.configuration.microprofile.config;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.MicroprofileConfigPage;
import org.jboss.hal.testsuite.util.ModuleUtils;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;


import static org.jboss.hal.testsuite.dmr.ModelNodeGenerator.ModelNodePropertiesBuilder;
import static org.jboss.hal.testsuite.test.configuration.microprofile.config.MicroprofileConfigFixtures.*;

@RunWith(Arquillian.class)
public class MicroprofileConfigTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();

    private static final Operations ops = new Operations(client);

    private static ModuleUtils moduleUtils;

    private static String customModuleName, customModuleForUpdateName;

    @Inject
    private CrudOperations crud;
    @Page
    private MicroprofileConfigPage page;

    @BeforeClass
    public static void setup() throws IOException {
        moduleUtils = new ModuleUtils(client);
        JavaArchive
            jar = ShrinkWrap.create(JavaArchive.class, ARCHIVE_NAME),
            jarForUpdate = ShrinkWrap.create(JavaArchive.class, ARCHIVE_FOR_UPDATE_NAME);
        jar.addClasses(CustomConfigSource.class, CustomConfigSourceProvider.class);
        jarForUpdate.addClasses(CustomConfigSourceForUpdate.class, CustomConfigSourceProviderForUpdate.class);
        customModuleName = moduleUtils.createModule(CUSTOM_MODULE_PATH, jar, "org.eclipse.microprofile.config.api");
        customModuleForUpdateName = moduleUtils.createModule(CUSTOM_MODULE_FOR_UPDATE_PATH, jar, "org.eclipse.microprofile.config.api");

        ops.add(getConfigSourceAddress(CONFIG_SOURCE_PROPS_UPDATE), Values.of(PROPERTIES, Random.properties(2)));
        ops.add(getConfigSourceAddress(CONFIG_SOURCE_PROPS_DELETE), Values.of(PROPERTIES, Random.properties(2)));
        ops.add(getConfigSourceAddress(CONFIG_SOURCE_CLASS_UPDATE), Values.of(CLASS, new ModelNodePropertiesBuilder()
                .addProperty(NAME, CustomConfigSource.class.getName())
                .addProperty(MODULE, customModuleName)
                .build()));
        ops.add(getConfigProviderAddress(CONFIG_PROVIDER_UPDATE), Values.of(CLASS, new ModelNodePropertiesBuilder()
                .addProperty(NAME, CustomConfigSourceProvider.class.getName())
                .addProperty(MODULE, customModuleName)
                .build()));
        ops.add(getConfigProviderAddress(CONFIG_PROVIDER_DELETE), Values.of(CLASS, new ModelNodePropertiesBuilder()
                .addProperty(NAME, CustomConfigSourceProvider.class.getName())
                .addProperty(MODULE, customModuleName)
                .build()));
    }

    @AfterClass
    public static void cleanUp() throws IOException, OperationException {
        try {
            ops.removeIfExists(getConfigSourceAddress(CONFIG_SOURCE_PROPS_CREATE));
            ops.removeIfExists(getConfigSourceAddress(CONFIG_SOURCE_PROPS_UPDATE));
            ops.removeIfExists(getConfigSourceAddress(CONFIG_SOURCE_PROPS_DELETE));
            ops.removeIfExists(getConfigSourceAddress(CONFIG_SOURCE_CLASS_CREATE));
            ops.removeIfExists(getConfigSourceAddress(CONFIG_SOURCE_CLASS_UPDATE));
            moduleUtils.removeModule(CUSTOM_MODULE_PATH);
            moduleUtils.removeModule(CUSTOM_MODULE_FOR_UPDATE_PATH);
        } finally {
            client.close();
        }
    }

    @Test
    public void propertiesSourceCreate() throws Exception {
        ModelNode propertiesNode = Random.properties();
        TableFragment configSourcesTable = page.navigateToConfigSourcesTable();
        crud.create(getConfigSourceAddress(CONFIG_SOURCE_PROPS_CREATE), configSourcesTable, form -> {
            form.text(NAME, CONFIG_SOURCE_PROPS_CREATE);
            form.properties(PROPERTIES).add(propertiesNode);
        }, verifier -> {
            verifier
                .verifyExists()
                .verifyAttribute(PROPERTIES, propertiesNode);
        });
    }

    @Test
    public void propertiesSourceUpdate() throws Exception {
        FormFragment configSourcesForm = page.openConfigSourceForm(CONFIG_SOURCE_PROPS_UPDATE);
        crud.update(getConfigSourceAddress(CONFIG_SOURCE_PROPS_UPDATE), configSourcesForm, PROPERTIES,
                Random.properties());
        crud.update(getConfigSourceAddress(CONFIG_SOURCE_PROPS_UPDATE), configSourcesForm, "ordinal",
                Random.number(0, 1000));
    }

    @Test
    public void propertiesSourceDelete() throws Exception {
        TableFragment configSourcesTable = page.navigateToConfigSourcesTable();
        crud.delete(getConfigSourceAddress(CONFIG_SOURCE_PROPS_DELETE), configSourcesTable, CONFIG_SOURCE_PROPS_DELETE);
    }

    @Test
    public void classSourceCreate() throws Exception {
        TableFragment configSourcesTable = page.navigateToConfigSourcesTable();
        crud.create(getConfigSourceAddress(CONFIG_SOURCE_CLASS_CREATE), configSourcesTable, form -> {
            form.text(NAME, CONFIG_SOURCE_CLASS_CREATE);
            form.textByLabel(CLASS_NAME_LABEL, CustomConfigSource.class.getName());
            form.textByLabel(MODULE_LABEL, customModuleName);
        }, verifier -> {
            verifier
                .verifyExists()
                .verifyAttribute(CLASS, new ModelNodePropertiesBuilder()
                        .addProperty(NAME, CustomConfigSource.class.getName())
                        .addProperty(MODULE, customModuleName)
                        .build());
        });
    }

    @Test
    public void classSourceUpdate() throws Exception {
        FormFragment configSourcesForm = page.openConfigSourceForm(CONFIG_SOURCE_CLASS_UPDATE);
        crud.update(getConfigSourceAddress(CONFIG_SOURCE_CLASS_UPDATE), configSourcesForm, form -> {
            form.textByLabel(CLASS_NAME_LABEL, CustomConfigSourceForUpdate.class.getName());
            form.textByLabel(MODULE_LABEL, customModuleForUpdateName);
        }, verifier -> {
            verifier
                .verifyAttribute(CLASS, new ModelNodePropertiesBuilder()
                        .addProperty(NAME, CustomConfigSourceForUpdate.class.getName())
                        .addProperty(MODULE, customModuleForUpdateName)
                        .build());
        });
    }

    @Test
    public void classProviderCreate() throws Exception {
        TableFragment configProviderTable = page.navigateToConfigProvidersTable();
        crud.create(getConfigProviderAddress(CONFIG_PROVIDER_CREATE), configProviderTable, form -> {
            form.text(NAME, CONFIG_PROVIDER_CREATE);
            form.textByLabel(CLASS_NAME_LABEL, CustomConfigSourceProvider.class.getName());
            form.textByLabel(MODULE_LABEL, customModuleName);
        }, verifier -> {
            verifier
                .verifyExists()
                .verifyAttribute(CLASS, new ModelNodePropertiesBuilder()
                        .addProperty(NAME, CustomConfigSourceProvider.class.getName())
                        .addProperty(MODULE, customModuleName)
                        .build());
        });
    }

    @Test
    public void classProviderUpdate() throws Exception {
        FormFragment configProvidersForm = page.openConfigProviderForm(CONFIG_PROVIDER_UPDATE);
        crud.update(getConfigProviderAddress(CONFIG_PROVIDER_UPDATE), configProvidersForm, form -> {
            form.textByLabel(CLASS_NAME_LABEL, CustomConfigSourceProviderForUpdate.class.getName());
            form.textByLabel(MODULE_LABEL, customModuleForUpdateName);
        }, verifier -> {
            verifier
                .verifyAttribute(CLASS, new ModelNodePropertiesBuilder()
                        .addProperty(NAME, CustomConfigSourceProviderForUpdate.class.getName())
                        .addProperty(MODULE, customModuleForUpdateName)
                        .build());
        });
    }

    @Test
    public void providerDelete() throws Exception {
        TableFragment configProviderTable = page.navigateToConfigProvidersTable();
        crud.delete(getConfigSourceAddress(CONFIG_PROVIDER_DELETE), configProviderTable, CONFIG_PROVIDER_DELETE);
    }
}
