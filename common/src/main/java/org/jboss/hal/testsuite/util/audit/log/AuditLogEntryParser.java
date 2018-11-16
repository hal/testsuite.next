package org.jboss.hal.testsuite.util.audit.log;

import java.util.List;

public interface AuditLogEntryParser {
    List<AuditLog.LogEntry> parse(String jsonString);
}
