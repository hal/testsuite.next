package org.jboss.hal.testsuite.util.audit.log.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.util.audit.log.AuditLog;
import org.wildfly.extras.creaper.core.online.operations.Address;

public class LogEntryDeserializer extends StdDeserializer<AuditLog.LogEntry> {

    public LogEntryDeserializer() {
        this(null);
    }

    protected LogEntryDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public AuditLog.LogEntry deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
        JsonNode logEntryNode = p.getCodec().readTree(p);
        AuditLog.LogEntry logEntry = new AuditLog.LogEntry();
        logEntry.setType(logEntryNode.get("type").asText());
        logEntry.setReadOnly(logEntryNode.get("r/o").asBoolean());
        logEntry.setBooting(logEntryNode.get("booting").asBoolean());
        logEntry.setVersion(logEntryNode.get("version").asText());
        logEntry.setUser(logEntryNode.get("user").asText());
        logEntry.setDomainUUID(logEntryNode.get("domainUUID").asText());
        logEntry.setAccess(logEntryNode.get("access").asText());
        logEntry.setRemoteAccess(logEntryNode.get("remote-access").asText());
        logEntry.setSuccess(logEntryNode.get("success").asBoolean());
        JsonNode operationsNode = logEntryNode.get("ops").get(0);
        List<AuditLog.Operation> operations = new ArrayList<>();
        if (operationsNode.get("operation").asText().equals("composite")) {
            logEntry.setCompositeOperation(true);
            operationsNode.get("steps").forEach(jsonNode -> parseOperation(jsonNode, operations));
        } else {
            parseOperation(operationsNode, operations);
        }
        logEntry.addOperations(operations);
        return logEntry;
    }

    private void parseOperation(JsonNode operationNode, Collection<AuditLog.Operation> operations) {
        AuditLog.Operation operation = new AuditLog.Operation();
        operation.setOperationName(operationNode.get("operation").asText());
        Address address = Address.root();
        AddressBuilder addressBuilder = new AddressBuilder(address);
        operationNode.get("address").forEach(addressNode -> parseAndUpdateAddress(addressNode, addressBuilder));
        operation.setAddress(addressBuilder.buildAddress());
        Iterable<Map.Entry<String, JsonNode>> fields = operationNode::fields;
        StreamSupport.stream(fields.spliterator(), false)
            .filter(entry -> !entry.getKey().equals("operation") || !entry.getKey().equals("address"))
            .forEach(entry -> operation.addArgument(entry.getKey(), JsonNodeToModelNodeParser.toBigDecimal(entry.getValue())));
        operations.add(operation);
    }

    private ModelNode fromJsonNode(JsonNode jsonNode) {
        if (jsonNode.isTextual()) {
            return new ModelNode(jsonNode.textValue());
        }
        if (jsonNode.isBoolean()) {
            return new ModelNode(jsonNode.booleanValue());
        }
        if (jsonNode.isIntegralNumber()) {
            if (jsonNode.isInt()) {
                return new ModelNode(jsonNode.intValue());
            }
            if (jsonNode.isLong()) {
                return new ModelNode(jsonNode.longValue());
            }
            if (jsonNode.isBigDecimal()) {
                return new ModelNode(jsonNode.decimalValue());
            }
        }
        if (jsonNode.isFloatingPointNumber()) {
            if (jsonNode.isFloat()) {
                return new ModelNode(jsonNode.floatValue());
            }
            if (jsonNode.isDouble()) {
                return new ModelNode(jsonNode.doubleValue());
            }
            if (jsonNode.isBigDecimal()) {
                return new ModelNode(jsonNode.decimalValue());
            }
        }
        return new ModelNode();
    }

    private void parseAndUpdateAddress(JsonNode jsonNode, AddressBuilder builder) {
        jsonNode.fields()
            .forEachRemaining(addressEntry -> builder.addEntry(addressEntry.getKey(), addressEntry.getValue().asText()));
    }

    private static class AddressBuilder {

        private final Address rootAddress;

        private List<Map.Entry<String, String>> entries = new ArrayList<>();

        AddressBuilder(Address address) {
            this.rootAddress = address;
        }

        void addEntry(String key, String value) {
            entries.add(new ImmutablePair<>(key, value));
        }

        Address buildAddress() {
            Address result = rootAddress;
            for (Map.Entry<String, String> entry : entries) {
                result = result.and(entry.getKey(), entry.getValue());
            }
            return result;
        }
    }
}
