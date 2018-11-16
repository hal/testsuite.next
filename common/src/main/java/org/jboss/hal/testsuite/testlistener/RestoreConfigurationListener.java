package org.jboss.hal.testsuite.testlistener;

import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.extras.creaper.commands.foundation.online.SnapshotBackup;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

/**
 * This class (if registered) backups server configuration file before each test case run and restore it afterwards.
 */
public class RestoreConfigurationListener extends RunListener {

    private static final Logger log = LoggerFactory.getLogger(RestoreConfigurationListener.class);
    private SnapshotBackup snapshotBackup;

    @Override
    public void testRunStarted(Description description) throws Exception {
        snapshotBackup = new SnapshotBackup();
        try (OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient()) {
            log.debug("Going to backup server configuration before '{}' run.", description.getChildren());
            if (client != null) {
                client.apply(snapshotBackup.backup());
            }
        }
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        try (OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient()) {
            log.debug("Going to restore server configuration.");
            if (client != null) {
                client.apply(snapshotBackup.restore());
            }
        }
    }
}
