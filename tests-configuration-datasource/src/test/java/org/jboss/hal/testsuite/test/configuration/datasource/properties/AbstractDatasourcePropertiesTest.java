package org.jboss.hal.testsuite.test.configuration.datasource.properties;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.util.PathOperations;
import org.jboss.modules.maven.ArtifactCoordinates;
import org.jboss.modules.maven.MavenResolver;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TemporaryFolder;
import org.wildfly.extras.creaper.commands.datasources.AddJdbcDriver;
import org.wildfly.extras.creaper.commands.datasources.RemoveJdbcDriver;
import org.wildfly.extras.creaper.commands.modules.RemoveModule;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

public abstract class AbstractDatasourcePropertiesTest {

    protected static final String PG_DRIVER_NAME = "postgres";
    protected static final String PG_MODULE_NAME = "org.postgres";
    protected static final Class<?> PG_DRIVER_CLASS = org.postgresql.Driver.class;
    protected static final String PG_DRIVER_CLASS_NAME = PG_DRIVER_CLASS.getName();
    protected static final Class<?> PG_XA_DATASOURCE_CLASS = org.postgresql.xa.PGXADataSource.class;
    protected static final String PG_XA_DATASOURCE_CLASS_NAME = PG_XA_DATASOURCE_CLASS.getName();
    protected static final Class<?> PG_DATASOURCE_CLASS = org.postgresql.ds.PGSimpleDataSource.class;
    protected static final String PG_DATASOURCE_CLASS_NAME = PG_DATASOURCE_CLASS.getName();

    protected static final String MYSQL_DRIVER_NAME = "mysql";
    protected static final String MYSQL_MODULE_NAME = "com.mysql";
    protected static final Class<?> MYSQL_DRIVER_CLASS = com.mysql.cj.jdbc.Driver.class;
    protected static final String MYSQL_DRIVER_CLASS_NAME = MYSQL_DRIVER_CLASS.getName();
    protected static final Class<?> MYSQL_DATASOURCE_CLASS = com.mysql.cj.jdbc.MysqlDataSource.class;
    protected static final String MYSQL_DATASOURCE_CLASS_NAME = MYSQL_DATASOURCE_CLASS.getName();
    protected static final Class<?> MYSQL_XA_DATASOURCE_CLASS = com.mysql.cj.jdbc.MysqlXADataSource.class;
    protected static final String MYSQL_XA_DATASOURCE_CLASS_NAME = MYSQL_XA_DATASOURCE_CLASS.getName();

    protected static final String MSSQL_DRIVER_NAME = "sqlserver";
    protected static final String MSSQL_MODULE_NAME = "com.microsoft.sqlserver";
    protected static final Class<?> MSSQL_DRIVER_CLASS = com.microsoft.sqlserver.jdbc.SQLServerDriver.class;
    protected static final String MSSQL_DRIVER_CLASS_NAME = MSSQL_DRIVER_CLASS.getName();
    protected static final Class<?> MSSQL_DATASOURCE_CLASS = com.microsoft.sqlserver.jdbc.SQLServerDataSource.class;
    protected static final String MSSQL_DATASOURCE_CLASS_NAME = MSSQL_DATASOURCE_CLASS.getName();
    protected static final Class<?> MSSQL_XA_DATASOURCE_CLASS = com.microsoft.sqlserver.jdbc.SQLServerXADataSource.class;
    protected static final String MSSQL_XA_DATASOURCE_CLASS_NAME = MSSQL_XA_DATASOURCE_CLASS.getName();

    protected static final String CUSTOM_DRIVER_NAME = "custom";
    protected static final String CUSTOM_MODULE_NAME = CustomJDBCDriver.class.getPackage().getName();
    protected static final Class<?> CUSTOM_DRIVER_CLASS = CustomJDBCDriver.class;
    protected static final String CUSTOM_DRIVER_CLASS_NAME = CUSTOM_DRIVER_CLASS.getName();
    protected static final Class<?> CUSTOM_DATASOURCE_CLASS = CustomDataSource.class;
    protected static final String CUSTOM_DATASOURCE_CLASS_NAME = CUSTOM_DATASOURCE_CLASS.getName();
    protected static final Class<?> CUSTOM_XA_DATASOURCE_CLASS = CustomXADataSource.class;
    protected static final String CUSTOM_XA_DATASOURCE_CLASS_NAME = CUSTOM_XA_DATASOURCE_CLASS.getName();

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Administration administration = new Administration(client);
    protected static final Operations operations = new Operations(client);

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    private static final String JAVAX_API = "javax.api";
    private static final String JAVAX_TRANSACTION_API = "jakarta.transaction.api";

    @BeforeClass
    public static void setupJDBCDrivers() throws CommandFailedException, IOException {
        JavaArchive customModuleArchive = ShrinkWrap.create(JavaArchive.class, "custom-jdbc-module.jar")
            .addAsServiceProvider(java.sql.Driver.class, CustomJDBCDriver.class)
            .addClass(CustomJDBCDriver.class)
            .addClass(CustomXADataSource.class)
            .addClass(CustomDataSource.class);
        File customModuleArchiveFile = temporaryFolder.newFile(customModuleArchive.getName());
        customModuleArchive.as(ZipExporter.class).exportTo(customModuleArchiveFile, true);
        MavenResolver resolver = MavenResolver.createDefaultResolver();
        AddModule addPGJDBCDriverModule = new AddModule.Builder(PG_MODULE_NAME)
            .resource(resolver.resolveJarArtifact(ArtifactCoordinates.fromString("org.postgresql:postgresql:42.2.4")))
            .dependency(JAVAX_API)
            .dependency(JAVAX_TRANSACTION_API)
            .moduleRootDir(new PathOperations(client).getModulesPath().toFile())
            .build();
        AddModule addMSQLJDBCDriverModule = new AddModule.Builder(MYSQL_MODULE_NAME)
            .resource(resolver.resolveJarArtifact(ArtifactCoordinates.fromString("mysql:mysql-connector-java:8.0.12")))
            .dependency(JAVAX_API)
            .dependency(JAVAX_TRANSACTION_API)
            .moduleRootDir(new PathOperations(client).getModulesPath().toFile())
            .build();
        AddModule addMSSQLJDBCDriverModule = new AddModule.Builder(MSSQL_MODULE_NAME)
            .resource(resolver.resolveJarArtifact(
                ArtifactCoordinates.fromString("com.microsoft.sqlserver:mssql-jdbc:7.0.0.jre8")))
            .dependency(JAVAX_API)
            .dependency(JAVAX_TRANSACTION_API)
            .moduleRootDir(new PathOperations(client).getModulesPath().toFile())
            .build();
        AddModule addCustomJDBCDriverModule = new AddModule.Builder(CUSTOM_MODULE_NAME)
            .dependency(JAVAX_API)
            .dependency(JAVAX_TRANSACTION_API)
            .resource(customModuleArchiveFile)
            .moduleRootDir(new PathOperations(client).getModulesPath().toFile())
            .build();
        client.apply(addPGJDBCDriverModule);
        client.apply(addMSQLJDBCDriverModule);
        client.apply(addMSSQLJDBCDriverModule);
        client.apply(addCustomJDBCDriverModule);
        AddJdbcDriver addPGJDBCDriver = new AddJdbcDriver.Builder(PG_DRIVER_NAME, PG_MODULE_NAME)
            .driverClass(PG_DRIVER_CLASS_NAME)
            .datasourceClass(PG_DATASOURCE_CLASS_NAME)
            .xaDatasourceClass(PG_XA_DATASOURCE_CLASS_NAME)
            .build();
        AddJdbcDriver addMSQLJDBCDriver = new AddJdbcDriver.Builder(MYSQL_DRIVER_NAME, MYSQL_MODULE_NAME)
            .driverClass(MYSQL_DRIVER_CLASS_NAME)
            .datasourceClass(MYSQL_DATASOURCE_CLASS_NAME)
            .xaDatasourceClass(MYSQL_XA_DATASOURCE_CLASS_NAME)
            .build();
        AddJdbcDriver addMSSQLJDBCDriver = new AddJdbcDriver.Builder(MSSQL_DRIVER_NAME, MSSQL_MODULE_NAME)
            .driverClass(MSSQL_DRIVER_CLASS_NAME)
            .datasourceClass(MSSQL_DATASOURCE_CLASS_NAME)
            .xaDatasourceClass(MSSQL_XA_DATASOURCE_CLASS_NAME)
            .build();
        AddJdbcDriver addCustomJDBCDriver = new AddJdbcDriver.Builder(CUSTOM_DRIVER_NAME, CUSTOM_MODULE_NAME)
            .driverClass(CUSTOM_DRIVER_CLASS_NAME)
            .datasourceClass(CUSTOM_DATASOURCE_CLASS_NAME)
            .xaDatasourceClass(CUSTOM_XA_DATASOURCE_CLASS_NAME)
            .build();
        client.apply(addPGJDBCDriver);
        client.apply(addMSQLJDBCDriver);
        client.apply(addMSSQLJDBCDriver);
        client.apply(addCustomJDBCDriver);
    }

    @AfterClass
    public static void cleanUpJDBCDrivers()
        throws IOException, CommandFailedException {
        RemoveJdbcDriver removePGJDBCDriver = new RemoveJdbcDriver(PG_DRIVER_NAME);
        RemoveModule removePGJDBCDriverModule = new RemoveModule(PG_MODULE_NAME);
        RemoveJdbcDriver removeMSQLJDBCDriver = new RemoveJdbcDriver(MYSQL_DRIVER_NAME);
        RemoveModule removeMSQLJDBCDriverModule = new RemoveModule(MYSQL_MODULE_NAME);
        RemoveJdbcDriver removeMSSQLJDBCDriver = new RemoveJdbcDriver(MSSQL_DRIVER_NAME);
        RemoveModule removeMSSQLJDBCDriverModule = new RemoveModule(MSSQL_MODULE_NAME);
        RemoveJdbcDriver removeCustomJDBCDriver = new RemoveJdbcDriver(CUSTOM_DRIVER_NAME);
        RemoveModule removeCustomJDBCDriverModule = new RemoveModule(CUSTOM_MODULE_NAME);
        try {
            client.apply(removePGJDBCDriver);
            client.apply(removePGJDBCDriverModule);
            client.apply(removeMSQLJDBCDriver);
            client.apply(removeMSQLJDBCDriverModule);
            client.apply(removeMSSQLJDBCDriver);
            client.apply(removeMSSQLJDBCDriverModule);
            client.apply(removeCustomJDBCDriver);
            client.apply(removeCustomJDBCDriverModule);
        } finally {
            client.close();
        }
    }

    protected static List<String> getDataSourcePropertiesFromClass(Class<?> datasourceClass) {
        return Arrays.stream(datasourceClass.getMethods())
            .filter(method -> method.getName().startsWith("set") && method.getParameterTypes().length == 1)
            .filter(method -> isBasicDataType(method.getParameterTypes()[0]))
            .map(method -> {
                String attributeName = method.getName().substring(3);
                char[] arr = attributeName.toCharArray();
                if (arr.length != 1 && !Character.isUpperCase(arr[1])) {
                    arr[0] = Character.toLowerCase(arr[0]);
                }
                return new String(arr);
            })
            .distinct()
            .sorted().collect(Collectors.toList());
    }

    private static boolean isBasicDataType(Class<?> clazz) {
        if (byte.class.isAssignableFrom(clazz) || Byte.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (short.class.isAssignableFrom(clazz) || Short.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (int.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (long.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (float.class.isAssignableFrom(clazz) || Float.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (double.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (char.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (String.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (boolean.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    protected static boolean containsAllIgnoreCase(Collection<String> elements, Collection<String> toBeCheckedElements) {
        return toBeCheckedElements.stream()
            .reduce(true, (a, b) -> a && elements.stream().anyMatch(elem2 -> elem2.equalsIgnoreCase(b)),
                Boolean::logicalAnd);
    }
}
