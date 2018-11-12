package org.jboss.hal.testsuite.util.audit.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.dmr.ModelNode;
import org.wildfly.extras.creaper.core.online.operations.Address;

public class AuditLog {

    private List<LogEntry> logEntries = new ArrayList<>();

    public void addEntry(LogEntry entry) {
        this.logEntries.add(entry);
    }

    public void addEntries(Collection<LogEntry> logEntries) {
        this.logEntries.addAll(logEntries);
    }

    public List<LogEntry> getLogEntries() {
        return Collections.unmodifiableList(logEntries);
    }

    public static class LogEntry {

        private String type;

        private boolean readOnly;

        private boolean booting;

        private String version;

        private String user;

        private String domainUUID;

        private String access;

        private String remoteAdress;

        private boolean success;

        private List<Operation> operations = new ArrayList<>();

        private boolean compositeOperation;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isReadOnly() {
            return readOnly;
        }

        public void setReadOnly(boolean readOnly) {
            this.readOnly = readOnly;
        }

        public boolean isBooting() {
            return booting;
        }

        public void setBooting(boolean booting) {
            this.booting = booting;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getDomainUUID() {
            return domainUUID;
        }

        public void setDomainUUID(String domainUUID) {
            this.domainUUID = domainUUID;
        }

        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }

        public String getRemoteAddress() {
            return remoteAdress;
        }

        public void setRemoteAddress(String remoteAddress) {
            this.remoteAdress = remoteAddress;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<Operation> getOperations() {
            return Collections.unmodifiableList(operations);
        }

        public void addOperation(Operation operation) {
            this.operations.add(operation);
        }

        public void addOperations(List<Operation> operations) {
            this.operations.addAll(operations);
        }

        public boolean isCompositeOperation() {
            return compositeOperation;
        }

        public void setCompositeOperation(boolean compositeOperation) {
            this.compositeOperation = compositeOperation;
        }
    }


    public static class Operation {

        private String operationName;

        private Address address;

        private Map<String, ModelNode> arguments = new HashMap<>();

        public String getOperationName() {
            return operationName;
        }

        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public void addArguments(Map<String, ModelNode> arguments) {
            this.arguments.putAll(arguments);
        }

        public void addArgument(String key, ModelNode value) {
            this.arguments.put(key, value);
        }

        public Map<String, ModelNode> getArguments() {
            return Collections.unmodifiableMap(arguments);
        }
    }
}
