package org.jboss.hal.testsuite.test.configuration.elytron.mappers.decoders.role.mapper;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.fixtures.ElytronFixtures;
import org.jboss.hal.testsuite.page.configuration.ElytronMappersDecodersPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.MAPPED_ROLE_MAPPER_ITEM;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.ROLE_MAPPERS_ITEM;

@RunWith(Arquillian.class)
public class MappedRoleMapperTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String MAPPED_ROLE_MAPPER_CREATE = "mapped-role-mapper-to-be-created-" + Random.name();
    private static final String MAPPED_ROLE_MAPPER_UPDATE = "mapped-role-mapper-to-be-updated-" + Random.name();
    private static final String MAPPED_ROLE_MAPPER_DELETE = "mapped-role-mapper-to-be-deleted-" + Random.name();

    @BeforeClass
    public static void setUp() throws IOException {
        ModelNode roleMap = new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty("from", Random.name())
                .addProperty("to", new ModelNodeGenerator.ModelNodeListBuilder().addAll(Random.name()).build()).build();
        operations.add(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_UPDATE),
            Values.ofList(ElytronFixtures.ROLE_MAP, roleMap)).assertSuccess();
        operations.add(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_DELETE),
            Values.ofList(ElytronFixtures.ROLE_MAP, roleMap)).assertSuccess();
    }

    @AfterClass
    public static void tearDown() throws IOException, OperationException {
        try {
            operations.removeIfExists(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_CREATE));
            operations.removeIfExists(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_UPDATE));
            operations.removeIfExists(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_DELETE));
        } finally {
            client.close();
        }
    }

    @Drone
    private WebDriver browser;

    @Page
    private ElytronMappersDecodersPage page;

    @Inject
    private Console console;

    @Inject
    private CrudOperations crudOperations;

    @Before
    public void navigate() {
        page.navigate();
        console.verticalNavigation()
            .selectSecondary(ROLE_MAPPERS_ITEM, MAPPED_ROLE_MAPPER_ITEM);
    }

    @Test
    public void create() throws Exception {
        crudOperations.create(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_CREATE),
            page.getMappedRoleMapperTable(), formFragment -> {
                formFragment.text("name", MAPPED_ROLE_MAPPER_CREATE);
                formFragment.properties(ElytronFixtures.ROLE_MAP).add(Random.name(), Random.name())
                    .add(Random.name(), Random.name());
            }, ResourceVerifier::verifyExists);
    }

    @Test
    public void delete() throws Exception {
        crudOperations.delete(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_DELETE),
            page.getMappedRoleMapperTable(), MAPPED_ROLE_MAPPER_DELETE);
    }

    @Test
    public void toggleKeepMapped() throws Exception {
        boolean keepMapped =
            operations.readAttribute(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_UPDATE), "keep-mapped")
                .booleanValue(false);
        page.getMappedRoleMapperTable().select(MAPPED_ROLE_MAPPER_UPDATE);
        crudOperations.update(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_UPDATE),
            page.getMappedRoleMapperForm(), "keep-mapped", !keepMapped);
    }

    @Test
    public void toggleKeepNonMapped() throws Exception {
        boolean keepNonMapped =
            operations.readAttribute(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_UPDATE),
                "keep-non-mapped")
                .booleanValue(false);
        page.getMappedRoleMapperTable().select(MAPPED_ROLE_MAPPER_UPDATE);
        crudOperations.update(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_UPDATE),
            page.getMappedRoleMapperForm(), "keep-non-mapped", !keepNonMapped);
    }

    @Test
    public void editRoleMap() throws Exception {
        String key = Random.name();
        String value = Random.name();
        ModelNode roleMap = new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty("from", key)
                .addProperty("to", new ModelNodeGenerator.ModelNodeListBuilder().addAll(value).build()).build();
        page.getMappedRoleMapperTable().select(MAPPED_ROLE_MAPPER_UPDATE);
        crudOperations.update(ElytronFixtures.mappedRoleMapperAddress(MAPPED_ROLE_MAPPER_UPDATE),
            page.getMappedRoleMapperForm(),
            formFragment -> {
            formFragment.properties(ElytronFixtures.ROLE_MAP).removeTags();
            formFragment.properties(ElytronFixtures.ROLE_MAP).add(key, value);
            },
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(ElytronFixtures.ROLE_MAP, roleMap));
    }
}
