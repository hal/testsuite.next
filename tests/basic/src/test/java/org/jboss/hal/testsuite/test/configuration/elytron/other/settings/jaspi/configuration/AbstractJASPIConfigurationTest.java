package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.jaspi.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.ElytronOtherSettingsPage;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

public abstract class AbstractJASPIConfigurationTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);

    @AfterClass
    public static void tearDown() throws IOException {
        client.close();
    }

    protected static void createJASPIConfiguration(String name) throws IOException {
        createJASPIConfigurationWithServerAuthModuleClassName(name, Random.name());
    }

    protected static void createJASPIConfigurationWithServerAuthModuleClassName(String name, String... classNames)
        throws IOException {
        ModelNodeGenerator.ModelNodeListBuilder serverAuthModulesNodeBuilder =
            new ModelNodeGenerator.ModelNodeListBuilder();
        Arrays.stream(classNames).map(className -> new ServerAuthModuleBuilder().withClassName(className).build())
            .forEach(serverAuthModulesNodeBuilder::addNode);
        operations.add(ElytronFixtures.jaspiConfigurationAddress(name),
            Values.of("server-auth-modules", serverAuthModulesNodeBuilder.build())).assertSuccess();
    }

    @Drone
    protected WebDriver browser;

    @Inject
    protected Console console;

    @Inject
    protected CrudOperations crudOperations;

    @Page
    protected ElytronOtherSettingsPage page;

    protected static class ServerAuthModuleBuilder {
        private String className;
        private Optional<ModelNode> module = Optional.empty();
        private Optional<ModelNode> flag = Optional.empty();
        private Optional<ModelNode> options = Optional.empty();

        ServerAuthModuleBuilder withClassName(String className) {
            this.className = className;
            return this;
        }

        ServerAuthModuleBuilder withModule(String module) {
            this.module = Optional.of(new ModelNode(module));
            return this;
        }

        ServerAuthModuleBuilder withFlag(String flag) {
            this.flag = Optional.of(new ModelNode(flag.toUpperCase()));
            return this;
        }

        ServerAuthModuleBuilder withOptions(List<Property> options) {
            ModelNodeGenerator.ModelNodePropertiesBuilder propertiesBuilder =
                new ModelNodeGenerator.ModelNodePropertiesBuilder();
            options.forEach(
                property -> propertiesBuilder.addProperty(property.getName(),
                        property.getValue()).build());
            this.options = Optional.of(propertiesBuilder.build());
            return this;
        }

        ModelNode build() {
            ModelNodeGenerator.ModelNodePropertiesBuilder builder = new ModelNodeGenerator.ModelNodePropertiesBuilder();
            builder.addProperty("class-name", className);
            builder.addProperty("module", module.orElse(new ModelNode()));
            builder.addProperty("flag", flag.orElse(new ModelNode()));
            builder.addProperty("options", options.orElse(new ModelNode()));
            return builder.build();
        }
    }
}
