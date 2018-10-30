package org.jboss.hal.testsuite.util.audit.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jboss.hal.testsuite.util.audit.log.deserializer.LogEntryDeserializer;

public class NDJSONLogEntryParser implements LogEntryParser {

    private final String dateFormatRegex;
    private final String dateSeparatorRegex;

    public NDJSONLogEntryParser(String dateFormatRegex, String dateSeparatorRegex) {
        this.dateFormatRegex = dateFormatRegex;
        this.dateSeparatorRegex = dateSeparatorRegex;
    }

    public NDJSONLogEntryParser() {
        this("", "");
    }

    @Override
    public List<AuditLog.LogEntry> parse(InputStream inputStream) {
        List<AuditLog.LogEntry> result = new ArrayList<>();
        String regex = "(^"
            + dateFormatRegex
            + dateSeparatorRegex
            + Pattern.quote("{")
            + "$.*?^"
            + Pattern.quote("}")
            + "$)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        String text = convertInputStreamToString(inputStream, StandardCharsets.UTF_8);
        Matcher matcher = pattern.matcher(text);
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

    private static String convertInputStreamToString(InputStream inputStream, Charset charset) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }
}
