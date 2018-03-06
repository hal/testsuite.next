package org.jboss.hal.testsuite.test.runtime.batch;

import java.io.IOException;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.finder.ColumnFragment;
import org.jboss.hal.testsuite.util.ServerEnvironmentUtils;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.commands.deployments.Deploy;
import org.wildfly.extras.creaper.commands.deployments.Undeploy;
import org.wildfly.extras.creaper.core.CommandFailedException;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.testsuite.fragment.finder.FinderFragment.runtimeSubsystemPath;

@RunWith(Arquillian.class)
public class BatchFinderTest {

    private static final String NAME_FILTER_PREFIX = "filtered";
    private static final String STARTED_STATE = "started";
    private static final String BATCH_MONITOR_NAME = "batch-jberet";

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final ServerEnvironmentUtils serverEnvironmentUtils = new ServerEnvironmentUtils(client);
    private static final Operations operations = new Operations(client);

    private static final BatchJob READ_BATCH_JOB = new BatchJob.Builder().deploymentName(
        String.format("my_batch_job_%s_to_be_read.war", RandomStringUtils.randomAlphanumeric(7)))
        .jobName("reading_this_job")
        .stoppingInterval(500)
        .build();

    private static final BatchJob VIEW_BATCH_JOB = new BatchJob.Builder().deploymentName(
        String.format("my_batch_job_%s_to_be_viewed.war", RandomStringUtils.randomAlphanumeric(7)))
        .jobName("viewing_this_job")
        .stoppingInterval(500)
        .build();

    private static final BatchJob REFRESH_BATCH_JOB = new BatchJob.Builder().deploymentName(
        String.format("my_batch_job_%s_to_be_refreshed.war", RandomStringUtils.randomAlphanumeric(7)))
        .jobName("refreshing_batch_job")
        .stoppingInterval(500)
        .build();

    private static final BatchJob TO_BE_FILTERED_BY_NAME_BATCH_JOB = new BatchJob.Builder().deploymentName(
        String.format("%s_batch_job_%s.war", NAME_FILTER_PREFIX, RandomStringUtils.randomAlphanumeric(7)))
        .jobName(String.format("%s_%s", NAME_FILTER_PREFIX, RandomStringUtils.randomAlphanumeric(7)))
        .build();

    private static final BatchJob TO_BE_FILTERED_BY_STATE_BATCH_JOB = new BatchJob.Builder().deploymentName(
        String.format("batch_job_%s.war", RandomStringUtils.randomAlphanumeric(7)))
        .jobName(String.format("random_name_%s", RandomStringUtils.randomAlphanumeric(7)))
        .build();

    @Inject
    private Console console;

    @Drone
    private WebDriver browser;

    private ColumnFragment batchRuntimeColumn;

    @BeforeClass
    public static void setUp() throws CommandFailedException {
        READ_BATCH_JOB.deploy();
        VIEW_BATCH_JOB.deploy();
        REFRESH_BATCH_JOB.deploy();
        TO_BE_FILTERED_BY_NAME_BATCH_JOB.deploy();
        TO_BE_FILTERED_BY_STATE_BATCH_JOB.deploy();
    }

    @AfterClass
    public static void cleanUp() throws CommandFailedException, IOException, OperationException {
        try {
            READ_BATCH_JOB.undeployIfExists();
            VIEW_BATCH_JOB.undeployIfExists();
            REFRESH_BATCH_JOB.undeployIfExists();
            TO_BE_FILTERED_BY_NAME_BATCH_JOB.undeployIfExists();
            TO_BE_FILTERED_BY_STATE_BATCH_JOB.undeployIfExists();
        } finally {
            IOUtils.closeQuietly(client);
        }
    }

    @Before
    public void initializeColumn() throws IOException {
        batchRuntimeColumn = console.finder(NameTokens.RUNTIME,
            runtimeSubsystemPath(serverEnvironmentUtils.getServerHostName(), BATCH_MONITOR_NAME))
            .column(Ids.JOB);
    }

    @Test
    public void read() {
        Assert.assertTrue("Added batch job should be present in the column",
            batchRuntimeColumn.containsItem(READ_BATCH_JOB.getBatchJobId()));
    }

    @Test
    public void view() {
        batchRuntimeColumn
            .selectItem(VIEW_BATCH_JOB.getBatchJobId())
            .view();
        PlaceRequest placeRequest = new PlaceRequest.Builder()
            .nameToken(NameTokens.JOB)
            .with(ModelDescriptionConstants.DEPLOYMENT, VIEW_BATCH_JOB.deploymentName)
            .with(ModelDescriptionConstants.NAME, VIEW_BATCH_JOB.jobName)
            .build();
        console.verify(placeRequest);
    }

    @Test
    public void refresh() throws CommandFailedException {
        client.apply(new Undeploy.Builder(REFRESH_BATCH_JOB.deploymentName).build());
        batchRuntimeColumn.refresh();
        Assert.assertFalse("Removed batch job should not be present in the column anymore",
            batchRuntimeColumn.containsItem(REFRESH_BATCH_JOB.getBatchJobId()));
    }

    @Test
    public void filterByName() {
        batchRuntimeColumn.filter(NAME_FILTER_PREFIX);
        Assert.assertTrue("Added batch job matching name filter should be present in the column",
            batchRuntimeColumn.containsItem(TO_BE_FILTERED_BY_NAME_BATCH_JOB.getBatchJobId()));
        batchRuntimeColumn.refresh();
    }

    @Test
    public void filterByBatchState() {
        batchRuntimeColumn.selectItem(TO_BE_FILTERED_BY_STATE_BATCH_JOB.getBatchJobId())
            .dropdown()
            .click("Start");
        console.confirmationDialog().confirm();
        batchRuntimeColumn.filter(STARTED_STATE);
        Assert.assertTrue("Recently started batch job should be present in the column",
            batchRuntimeColumn.containsItem(TO_BE_FILTERED_BY_STATE_BATCH_JOB.getBatchJobId()));
        batchRuntimeColumn.refresh();
    }

    private static class BatchJob {

        private final String deploymentName;
        private final String jobName;
        private final long stoppingInterval;

        String getJobName() {
            return jobName;
        }

        String getJobFileName() {
            return jobName + ".xml";
        }

        String getBatchJobId() {
            return Ids.build(Ids.JOB, deploymentName.replace(".", ""), jobName);
        }

        void deploy() throws CommandFailedException {
            WebArchive webAppArchive = ShrinkWrap.create(WebArchive.class, deploymentName)
                .addClass(Batchlet.class)
                .addAsWebInfResource(createConfigAsset(),
                    "classes/META-INF/batch-jobs/" + getJobFileName())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
            Deploy deploy =
                new Deploy.Builder(webAppArchive.as(ZipExporter.class).exportAsInputStream(), webAppArchive.getName(),
                    true).build();
            client.apply(deploy);
        }

        void undeployIfExists() throws CommandFailedException, IOException, OperationException {
            if (operations.exists(Address.deployment(deploymentName))) {
                client.apply(new Undeploy.Builder(deploymentName).build());
            }
        }

        private StringAsset createConfigAsset() {
            return new StringAsset(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    +
                    "<job id=\""
                    + jobName
                    + "\" xmlns=\"http://xmlns.jcp.org/xml/ns/javaee\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                    +
                    " xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/jobXML_1_0.xsd\" version=\"1.0\">\n"
                    +
                    "    <step id=\"run-batchlet-step\">\n"
                    +
                    "        <batchlet ref=\"testBatchlet\">\n"
                    +
                    "            <properties>\n"
                    +
                    "                <property name=\"stoppingInterval\" value=\""
                    + stoppingInterval
                    + "\"/>\n"
                    +
                    "            </properties>\n"
                    +
                    "        </batchlet>\n"
                    +
                    "    </step>\n"
                    +
                    "</job>");
        }

        static class Builder {

            private String deploymentName;
            private String jobName;
            private long stoppingInterval;

            Builder jobName(String jobName) {
                this.jobName = jobName;
                return this;
            }

            Builder deploymentName(String deploymentName) {
                this.deploymentName = deploymentName;
                return this;
            }

            Builder stoppingInterval(long stoppingInterval) {
                this.stoppingInterval = stoppingInterval;
                return this;
            }

            BatchJob build() {
                return new BatchJob(this);
            }
        }

        private BatchJob(Builder builder) {
            this.deploymentName = builder.deploymentName;
            this.jobName = builder.jobName;
            this.stoppingInterval = builder.stoppingInterval;
        }
    }
}
