package org.jboss.hal.testsuite.test.configuration.transaction;

import org.apache.commons.lang3.RandomStringUtils;
import org.wildfly.extras.creaper.core.online.operations.Address;

public class TransactionFixtures {

    public static final String DEFAULT_TIMEOUT = "default-timeout";
    public static final String ENABLE_TSM_STATUS = "enable-tsm-status";
    public static final String JOURNAL_STORE_ENABLE_ASYNC_IO = "journal-store-enable-async-io";
    public static final String JTS = "jts";
    public static final String USE_JOURNAL_STORE = "use-journal-store";
    public static final String STATISTICS_ENABLED = "statistics-enabled";
    public static final String JDBC_ACTION_STORE_DROP_TABLE = "jdbc-action-store-drop-table";
    public static final String JDBC_ACTION_STORE_TABLE_PREFIX = "jdbc-action-store-table-prefix";
    public static final String JDBC_COMMUNICATION_STORE_DROP_TABLE = "jdbc-communication-store-drop-table";
    public static final String JDBC_COMMUNICATION_STORE_TABLE_PREFIX = "jdbc-communication-store-table-prefix";
    public static final String JDBC_STATE_STORE_DROP_TABLE = "jdbc-state-store-drop-table";
    public static final String JDBC_STATE_STORE_TABLE_PREFIX = "jdbc-state-store-table-prefix";
    public static final String JDBC_STORE_DATASOURCE = "jdbc-store-datasource";
    public static final String SOCKET_BINDING = "socket-binding";
    public static final String STATUS_SOCKET_BINDING = "status-socket-binding";
    public static final String RECOVERY_LISTENER = "recovery-listener";
    public static final String PROCESS_ID_SOCKET_BINDING = "process-id-socket-binding";
    public static final String PROCESS_ID_UUID = "process-id-uuid";
    public static final String PROCESS_ID_SOCKET_MAX_PORTS = "process-id-socket-max-ports";
    public static final String OBJECT_STORE_RELATIVE_TO = "object-store-relative-to";

    public static final String PATH_EDIT = "path-to-be-edited-" + RandomStringUtils.randomAlphanumeric(7);
    public static final String USE_JDBC_STORE = "use-jdbc-store";
    public static final String JDBC_DATASOURCE =
        "jdbc-datasource-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);
    public static final String PROCESS_SOCKET_BINDING_CREATE =
            "socket-binding-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);
    public static final String PROCESS_SOCKET_BINDING_WITH_PROCESS_ID_UUID =
                "socket-binding-with-process-id-uuid-" + RandomStringUtils.randomAlphanumeric(7);
    public static final String RECOVERY_SOCKET_BINDING_CREATE = "socket-binding-to-be-created-" + RandomStringUtils.randomAlphanumeric(7);
    public static final String RECOVERY_STATUS_SOCKET_BINDING = "status-socket-binding-to-be-edited" + RandomStringUtils.randomAlphanumeric(7);

    private TransactionFixtures() {

    }

    public static final Address TRANSACTIONS_ADDRESS = Address.subsystem("transactions");

}
