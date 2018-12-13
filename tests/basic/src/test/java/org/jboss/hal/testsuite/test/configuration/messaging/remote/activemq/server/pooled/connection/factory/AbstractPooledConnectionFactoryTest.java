package org.jboss.hal.testsuite.test.configuration.messaging.remote.activemq.server.pooled.connection.factory;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.MessagingRemoteActiveMQPage;
import org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Batch;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

public abstract class AbstractPooledConnectionFactoryTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);
    protected static final Administration administration = new Administration(client);

    protected static void createDiscoveryGroup(String name, String jgroupsChannel) throws IOException {
        Batch batch = new Batch();
        batch.add(MessagingFixtures.RemoteActiveMQServer.discoveryGroupAddress(name));
        batch.writeAttribute(MessagingFixtures.RemoteActiveMQServer.discoveryGroupAddress(name), "jgroups-channel", jgroupsChannel);
        batch.writeAttribute(
            MessagingFixtures.RemoteActiveMQServer.discoveryGroupAddress(name), "jgroups-cluster", Random.name());
        operations.batch(batch).assertSuccess();
    }

    protected static void createPooledConnectionFactory(String name, String discoveryGroup) throws IOException {
        operations.add(MessagingFixtures.RemoteActiveMQServer.pooledConnectionFactoryAddress(name),
            Values.of(ModelDescriptionConstants.DISCOVERY_GROUP, discoveryGroup)
                .and("entries",
                    new ModelNodeGenerator.ModelNodeListBuilder().addAll(Random.name(), Random.name(), Random.name())
                        .build())).assertSuccess();
    }

    protected static void createGenericConnector(String name) throws IOException {
        operations.add(
            MessagingFixtures.RemoteActiveMQServer.genericConnectorAddress(name), Values.of("factory-class", Random.name()))
            .assertSuccess();
    }

    @Drone
    protected WebDriver browser;

    @Inject
    protected Console console;

    @Inject
    protected CrudOperations crudOperations;

    @Page
    protected MessagingRemoteActiveMQPage page;

}
