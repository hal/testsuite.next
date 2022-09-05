package org.jboss.hal.testsuite.util.audit.log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.validation.constraints.NotNull;

/**
 * Represents an action to watch audit-log changes and provide changes of {@link AuditLog}.
 */
public class AuditLogWatcher implements Runnable {

    /**
     * Path to audit-log file, which is being updated by server
     */
    private final Path auditLogPath;

    private final AuditLogEntryParser logEntryParser;

    /**
     * Serves as a synchronization mechanism to provide changes created by server.
     */
    private final BlockingQueue<AuditLog> auditLogQueue;

    private final AtomicBoolean stop = new AtomicBoolean(false);

    public AuditLogWatcher(@NotNull Path auditLogPath, @NotNull BlockingQueue<AuditLog> auditLogQueue, @NotNull AuditLogEntryParser parser) {
        this.auditLogPath = auditLogPath;
        this.auditLogQueue = auditLogQueue;
        this.logEntryParser = parser;
    }

    public AuditLogWatcher(@NotNull Path auditLogPath, @NotNull BlockingQueue<AuditLog> auditLogQueue) {
        this(auditLogPath, auditLogQueue, new NDJSONAuditLogEntryParser());
    }

    public boolean isStopped() {
        return stop.get();
    }

    public void stop() {
        stop.set(true);
    }

    @Override
    public void run() {
        final File auditLogFile = auditLogPath.toFile();
        final AuditLog auditLog = new AuditLog();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(auditLogFile, "r");
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                Path parentFolder = auditLogPath.getParent();
                if (parentFolder == null) {
                    throw new RuntimeException("Cannot use WatchService on file without parent directory");
                }
                WatchKey watchKey = parentFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
                while (!isStopped()) {
                    for (WatchEvent<?> event : watchKey.pollEvents()) {
                        Path changedFile = (Path) event.context();
                        Path changedFileName = changedFile.getFileName();
                        if (changedFileName == null) {
                            continue;
                        }
                        if (changedFileName.equals(auditLogPath.getFileName())) {
                            auditLog.addEntries(logEntryParser.parse(newContentToString(randomAccessFile)));
                            auditLogQueue.put(auditLog);
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String newContentToString(RandomAccessFile randomAccessFile) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = randomAccessFile.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }
}
