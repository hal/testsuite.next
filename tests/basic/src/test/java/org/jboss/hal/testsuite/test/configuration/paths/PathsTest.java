package org.jboss.hal.testsuite.test.configuration.paths;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.PathsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;

@RunWith(Arquillian.class)
public class PathsTest {

    @Inject
    private CrudOperations crud;

    @Page
    private PathsPage page;

    @Drone
    private WebDriver browser;

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    private static final String PATH_CREATE = "path-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String PATH_DELETE = "path-to-be-removed-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String PATH_EDIT = "path-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);
    private static final String RELATIVE_TO_PATH = "relative-to-path-" + RandomStringUtils.randomAlphanumeric(7);

    @BeforeClass
    public static void beforeClass() throws Exception {
        createPathWithRandomPathValue(PATH_EDIT);
        createPathWithRandomPathValue(PATH_DELETE);
        createPathWithRandomPathValue(RELATIVE_TO_PATH);
    }

    private static void createPathWithRandomPathValue(String pathName) throws IOException {
        operations.add(PathsFixtures.pathAddress(pathName), Values.of(PathsFixtures.PATH, Random.name()));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(PathsFixtures.pathAddress(PATH_CREATE));
        operations.removeIfExists(PathsFixtures.pathAddress(PATH_EDIT));
        operations.removeIfExists(PathsFixtures.pathAddress(PATH_DELETE));
        operations.removeIfExists(PathsFixtures.pathAddress(RELATIVE_TO_PATH));
    }

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    @Test
    public void create() throws Exception {
        String path = Random.name();
        crud.create(PathsFixtures.pathAddress(PATH_CREATE), page.getPathsTable(), form -> {
            form.text(NAME, PATH_CREATE);
            form.text(PathsFixtures.PATH, path);
        }, resourceVerifier -> {
            resourceVerifier.verifyExists();
            resourceVerifier.verifyAttribute(PathsFixtures.PATH, path);
        });
    }

    @Test
    public void editPath() throws Exception {
        String path = Random.name();
        page.getPathsTable().select(PATH_EDIT);
        crud.update(PathsFixtures.pathAddress(PATH_EDIT), page.getPathsForm(), PathsFixtures.PATH, path);
    }

    @Test
    public void editRelativeTo() throws Exception {
        page.getPathsTable().select(PATH_EDIT);
        crud.update(PathsFixtures.pathAddress(PATH_EDIT), page.getPathsForm(), "relative-to", RELATIVE_TO_PATH);
    }


    @Test
    public void delete() throws Exception {
        crud.delete(PathsFixtures.pathAddress(PATH_DELETE), page.getPathsTable(), PATH_DELETE);
    }

}
