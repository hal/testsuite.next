/*
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.hal.testsuite.test.configuration.elytron.other.settings;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PORT;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class LogsSettingsTest extends AbstractOtherSettingsTest {

    // --------------- file-audit-log

    @Test
    public void fileAuditLogCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();

        crud.create(fileAuditLogAddress(FILE_LOG_CREATE), table, f -> {
            f.text(NAME, FILE_LOG_CREATE);
            f.text(PATH, ANY_STRING);
        });
    }

    @Test
    public void fileAuditLogTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();
        crud.createWithErrorAndCancelDialog(table, NAME, PATH);
    }

    @Test
    public void fileAuditLogUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();
        FormFragment form = page.getFileAuditLogForm();
        table.bind(form);
        table.select(FILE_LOG_UPDATE);
        crud.update(fileAuditLogAddress(FILE_LOG_UPDATE), form, PATH);
    }

    @Test
    public void fileAuditLogTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();
        FormFragment form = page.getFileAuditLogForm();
        table.bind(form);
        table.select(FILE_LOG_TRY_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void fileAuditLogDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getFileAuditLogTable();
        crud.delete(fileAuditLogAddress(FILE_LOG_DELETE), table, FILE_LOG_DELETE);
    }

    // --------------- periodic-rotating-file-aaudit-log

    @Test
    public void periodicRotatingFileAuditLogCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();

        crud.create(periodicRotatingFileAuditLogAddress(PER_LOG_CREATE), table, f -> {
            f.text(NAME, PER_LOG_CREATE);
            f.text(PATH, ANY_STRING);
            f.text(SUFFIX, SUFFIX_LOG);
        });
    }

    @Test
    public void periodicRotatingFileAuditLogTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();
        crud.createWithErrorAndCancelDialog(table, NAME, PATH);
    }

    @Test
    public void periodicRotatingFileAuditLogUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();
        FormFragment form = page.getPeriodicRotatingFileAuditLogForm();
        table.bind(form);
        table.select(PER_LOG_UPDATE);
        crud.update(periodicRotatingFileAuditLogAddress(PER_LOG_UPDATE), form, PATH);
    }

    @Test
    public void periodicRotatingFileAuditLogTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();
        FormFragment form = page.getPeriodicRotatingFileAuditLogForm();
        table.bind(form);
        table.select(PER_LOG_TRY_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void periodicRotatingFileAuditLogDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, PERIODIC_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getPeriodicRotatingFileAuditLogTable();
        crud.delete(periodicRotatingFileAuditLogAddress(PER_LOG_DELETE), table, PER_LOG_DELETE);
    }


    // --------------- size-rotating-file-audit-log

    @Test
    public void sizeRotatingLogCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();

        crud.create(sizeRotatingFileAuditLogAddress(SIZ_LOG_CREATE), table, f -> {
            f.text(NAME, SIZ_LOG_CREATE);
            f.text(PATH, ANY_STRING);
        });
    }

    @Test
    public void sizeRotatingLogTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();
        crud.createWithErrorAndCancelDialog(table, NAME, PATH);
    }

    @Test
    public void sizeRotatingLogUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();
        FormFragment form = page.getSizeRotatingFileAuditLogForm();
        table.bind(form);
        table.select(SIZ_LOG_UPDATE);
        crud.update(sizeRotatingFileAuditLogAddress(SIZ_LOG_UPDATE), form, PATH);
    }

    @Test
    public void sizeRotatingLogTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();
        FormFragment form = page.getSizeRotatingFileAuditLogForm();
        table.bind(form);
        table.select(SIZ_LOG_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATH), PATH);
    }

    @Test
    public void sizeRotatingLogDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SIZE_ROTATING_FILE_AUDIT_LOG_ITEM);
        TableFragment table = page.getSizeRotatingFileAuditLogTable();
        crud.delete(sizeRotatingFileAuditLogAddress(SIZ_LOG_DELETE), table, SIZ_LOG_DELETE);
    }

    // --------------- syslog-audit-log

    @Test
    public void syslogAuditLogCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();

        crud.create(syslogAuditLogAddress(SYS_LOG_CREATE), table, f -> {
            f.text(NAME, SYS_LOG_CREATE);
            f.text(HOSTNAME, ANY_STRING);
            f.number(PORT, Random.number());
            f.text(SERVER_ADDRESS, LOCALHOST);
        });
    }

    @Test
    public void syslogAuditLogTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();
        crud.createWithErrorAndCancelDialog(table, NAME, HOSTNAME);
    }

    @Test
    public void syslogAuditLogUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();
        FormFragment form = page.getSyslogAuditLogForm();
        table.bind(form);
        table.select(SYS_LOG_UPDATE);
        crud.update(syslogAuditLogAddress(SYS_LOG_UPDATE), form, PORT, Random.number());
    }

    @Test
    public void syslogAuditLogTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();
        FormFragment form = page.getSyslogAuditLogForm();
        table.bind(form);
        table.select(SYS_LOG_TRY_UPDATE);
        crud.updateWithError(form, f -> f.clear(HOSTNAME), HOSTNAME);
    }

    @Test
    public void syslogAuditLogDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, SYSLOG_AUDIT_LOG_ITEM);
        TableFragment table = page.getSyslogAuditLogTable();
        crud.delete(syslogAuditLogAddress(SYS_LOG_DELETE), table, SYS_LOG_DELETE);
    }

    // --------------- aggregate-security-event-listener

    @Test
    public void aggregateSecurityEventListenerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();

        crud.create(aggregateSecurityEventListenerAddress(AGG_SEC_CREATE), table, f -> {
            f.text(NAME, AGG_SEC_CREATE);
            f.list(SECURITY_EVENT_LISTENERS).add(SIZ_LOG_UPDATE).add(SYS_LOG_UPDATE);
        });
    }

    @Test
    public void aggregateSecurityEventListenerTryCreate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();
        crud.createWithErrorAndCancelDialog(table, NAME, SECURITY_EVENT_LISTENERS);
    }

    @Test
    public void aggregateSecurityEventListenerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();
        FormFragment form = page.getAggregateSecurityEventListenerForm();
        table.bind(form);
        table.select(AGG_SEC_UPDATE);
        crud.update(aggregateSecurityEventListenerAddress(AGG_SEC_UPDATE), form,
                f -> f.list(SECURITY_EVENT_LISTENERS).add(PER_LOG_UPDATE),
                verify -> verify.verifyListAttributeContainsValue(SECURITY_EVENT_LISTENERS, PER_LOG_UPDATE));
    }

    @Test
    public void aggregateSecurityEventListenerTryUpdate() {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();
        FormFragment form = page.getAggregateSecurityEventListenerForm();
        table.bind(form);
        table.select(AGG_SEC_UPDATE);
        crud.updateWithError(form, f -> f.list(SECURITY_EVENT_LISTENERS).removeTags(), SECURITY_EVENT_LISTENERS);
    }

    @Test
    public void aggregateSecurityEventListenerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(LOGS_ITEM, AGGREGATE_SECURITY_EVENT_LISTENER_ITEM);
        TableFragment table = page.getAggregateSecurityEventListenerTable();
        crud.delete(aggregateSecurityEventListenerAddress(AGG_SEC_DELETE), table, AGG_SEC_DELETE);
    }
}
