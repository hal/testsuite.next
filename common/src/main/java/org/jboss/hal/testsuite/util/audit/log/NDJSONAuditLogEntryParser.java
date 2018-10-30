package org.jboss.hal.testsuite.util.audit.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jboss.hal.testsuite.util.audit.log.deserializer.LogEntryDeserializer;

public class NDJSONAuditLogEntryParser implements AuditLogEntryParser {

    private final String dateFormatRegex;
    private final String dateSeparatorRegex;

    public NDJSONAuditLogEntryParser(String dateFormatRegex, String dateSeparatorRegex) {
        this.dateFormatRegex = dateFormatRegex;
        this.dateSeparatorRegex = dateSeparatorRegex;
    }

    public NDJSONAuditLogEntryParser() {
        this("", "");
    }

    @Override
    public List<AuditLog.LogEntry> parse(String jsonString) {
        List<AuditLog.LogEntry> result = new ArrayList<>();
        String regex = "(^"
            + dateFormatRegex
            + dateSeparatorRegex
            + Pattern.quote("{")
            + "$.*?^"
            + Pattern.quote("}")
            + "$)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(jsonString);
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule deserializationModule = new SimpleModule();
        deserializationModule.addDeserializer(AuditLog.LogEntry.class, new LogEntryDeserializer());
        mapper.registerModule(deserializationModule);
        try {
            while (matcher.find()) {
                AuditLog.LogEntry logEntry = mapper.readValue(matcher.group(), AuditLog.LogEntry.class);
                result.add(logEntry);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
