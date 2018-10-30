package org.jboss.hal.testsuite.util.audit.log;

import java.io.InputStream;
import java.util.List;

public interface LogEntryParser {
    List<AuditLog.LogEntry> parse(InputStream inputStream);
}
