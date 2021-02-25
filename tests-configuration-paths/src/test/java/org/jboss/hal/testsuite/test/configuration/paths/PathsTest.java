package org.jboss.hal.testsuite.test.configuration.paths;

import java.io.IOException;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fixtures.PathsFixtures;
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

    @BeforeClass
    public static void beforeClass() throws Exception {
        createPathWithRandomPathValue(PathsFixtures.PATH_EDIT);
        createPathWithRandomPathValue(PathsFixtures.PATH_DELETE);
        createPathWithRandomPathValue(PathsFixtures.RELATIVE_TO_PATH);
    }

    private static void createPathWithRandomPathValue(String pathName) throws IOException {
        operations.add(PathsFixtures.pathAddress(pathName), Values.of(PathsFixtures.PATH, Random.name()));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        operations.removeIfExists(PathsFixtures.pathAddress(PathsFixtures.PATH_CREATE));
        operations.removeIfExists(PathsFixtures.pathAddress(PathsFixtures.PATH_EDIT));
        operations.removeIfExists(PathsFixtures.pathAddress(PathsFixtures.PATH_DELETE));
        operations.removeIfExists(PathsFixtures.pathAddress(PathsFixtures.RELATIVE_TO_PATH));
    }

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    @Test
    public void create() throws Exception {
        String path = Random.name();
        crud.create(PathsFixtures.pathAddress(PathsFixtures.PATH_CREATE), page.getPathsTable(), form -> {
            form.text("name", PathsFixtures.PATH_CREATE);
            form.text(PathsFixtures.PATH, path);
        }, resourceVerifier -> {
            resourceVerifier.verifyExists();
            resourceVerifier.verifyAttribute(PathsFixtures.PATH, path);
        });
    }

    @Test
    public void editPath() throws Exception {
        String path = Random.name();
        page.getPathsTable().select(PathsFixtures.PATH_EDIT);
        crud.update(PathsFixtures.pathAddress(PathsFixtures.PATH_EDIT), page.getPathsForm(), PathsFixtures.PATH, path);
    }

    @Test
    public void editRelativeTo() throws Exception {
        page.getPathsTable().select(PathsFixtures.PATH_EDIT);
        crud.update(PathsFixtures.pathAddress(PathsFixtures.PATH_EDIT), page.getPathsForm(), "relative-to", PathsFixtures.RELATIVE_TO_PATH);
    }


    @Test
    public void delete() throws Exception {
        crud.delete(PathsFixtures.pathAddress(PathsFixtures.PATH_DELETE), page.getPathsTable(), PathsFixtures.PATH_DELETE);
    }

}
