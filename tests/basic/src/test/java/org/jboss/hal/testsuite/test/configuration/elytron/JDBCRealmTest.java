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
package org.jboss.hal.testsuite.test.configuration.elytron;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.creaper.ResourceVerifier;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.EmptyState;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.configuration.ElytronSecurityRealmsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.ELYTRON_JDBC_REALM;
import static org.jboss.hal.resources.Ids.TAB;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class JDBCRealmTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);

    @BeforeClass
    public static void beforeTests() throws Exception {

        operations.add(datasourceAddress(JDBC_PQ_DS), Values.of(JNDI_NAME, Random.jndiName())
                .and(DRIVER_NAME, "h2")
                .and(USER_NAME, "sa")
                .and(CONNECTION_URL, "jdbc:h2:mem:anytemp;DB_CLOSE_DELAY=-1"));

        ModelNode cpm = new ModelNode();
        cpm.get(PASSWORD_INDEX).set(123);
        ModelNode ssdm = new ModelNode();
        ssdm.get(PASSWORD_INDEX).set(432);
        ssdm.get(SALT_INDEX).set(234);
        ModelNode bcrypt = new ModelNode();
        bcrypt.get(PASSWORD_INDEX).set(523);
        bcrypt.get(SALT_INDEX).set(323);
        bcrypt.get(ITERATION_COUNT_INDEX).set(345);
        // ssdm.get(ITERATION_COUNT_INDEX).set(345);
        Values jdbcParams = Values.ofList(PRINCIPAL_QUERY, createSqlNode(SQL_UPDATE), createSqlNode(SQL_DELETE),
                createSqlNode(SQL_CPM_CRT),
                createSqlNode(SQL_CPM_UPD, CLEAR_PASSWORD_MAPPER, cpm),
                createSqlNode(SQL_CPM_DEL, CLEAR_PASSWORD_MAPPER, cpm),
                createSqlNode(SQL_BCM_CRT),
                createSqlNode(SQL_BCM_UPD, BCRYPT_MAPPER, bcrypt),
                createSqlNode(SQL_BCM_DEL, BCRYPT_MAPPER, bcrypt),
                createSqlNode(SQL_SSDM_CRT),
                createSqlNode(SQL_SSDM_UPD, SALTED_SIMPLE_DIGEST_MAPPER, ssdm),
                createSqlNode(SQL_SSDM_DEL, SALTED_SIMPLE_DIGEST_MAPPER, ssdm),
                createSqlNode(SQL_SDM_CRT),
                createSqlNode(SQL_SDM_UPD, SIMPLE_DIGEST_MAPPER, cpm),
                createSqlNode(SQL_SDM_DEL, SIMPLE_DIGEST_MAPPER, cpm),
                createSqlNode(SQL_SM_CRT),
                createSqlNode(SQL_SM_UPD, SCRAM_MAPPER, bcrypt),
                createSqlNode(SQL_SM_DEL, SCRAM_MAPPER, bcrypt));
        operations.add(jdbcRealmAddress(JDBC_RLM_UPDATE), jdbcParams);
        operations.add(jdbcRealmAddress(JDBC_RLM_DELETE), jdbcParams);
    }

    private static ModelNode createSqlNode(String name) {
        return createSqlNode(name, null, null);
    }

    private static ModelNode createSqlNode(String name, String coName, ModelNode node) {
        ModelNode model = new ModelNode();
        model.get(SQL).set(name);
        model.get(DATA_SOURCE).set(JDBC_PQ_DS);
        if (coName != null) {
            model.get(coName).set(node);
        }
        return model;
    }

    @AfterClass
    public static void tearDown() throws Exception {

        operations.remove(jdbcRealmAddress(JDBC_RLM_DELETE));
        operations.remove(jdbcRealmAddress(JDBC_RLM_UPDATE));
        operations.remove(jdbcRealmAddress(JDBC_RLM_CREATE));

        operations.remove(datasourceAddress(JDBC_PQ_DS));

    }

    @Page private ElytronSecurityRealmsPage page;
    @Inject private Console console;
    @Inject private CrudOperations crud;

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

    // --------------- jdbc-realm

    @Test
    public void Create() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment table = page.getJdbcRealmTable();
        crud.create(jdbcRealmAddress(JDBC_RLM_CREATE), table, f -> {
            f.text(NAME, JDBC_RLM_CREATE);
            f.text(DATA_SOURCE, JDBC_PQ_DS);
            f.text(SQL, ANY_STRING);
        });
    }

    @Test
    public void TryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment table = page.getJdbcRealmTable();
        crud.createWithErrorAndCancelDialog(table, f -> f.text(NAME, JDBC_RLM_CREATE), SQL);
    }

    @Test
    public void Delete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment table = page.getJdbcRealmTable();
        crud.delete(jdbcRealmAddress(JDBC_RLM_DELETE), table, JDBC_RLM_DELETE);
    }

    @Test
    public void principalQueryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        crud.create(jdbcRealmAddress(JDBC_RLM_UPDATE), table, f -> {
            f.text(SQL, SQL_CREATE);
            f.text(DATA_SOURCE, JDBC_PQ_DS);
        }, verifier -> verifier.verifyListAttributeContainsSingleValue(PRINCIPAL_QUERY, SQL, SQL_CREATE));
    }

    @Test
    public void principalQueryTryCreate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        crud.createWithErrorAndCancelDialog(table, f -> f.text(SQL, SQL_CREATE), DATA_SOURCE);
    }

    @Test
    public void principalQueryUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryAttributesForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_UPDATE);
        page.getPrincipalQueryTabs().select(Ids.build(ELYTRON_JDBC_REALM, PRINCIPAL_QUERY, TAB));
        crud.update(jdbcRealmAddress(JDBC_RLM_UPDATE), form, f -> f.text(SQL, SQL_UPDATE2),
                verifier -> verifier.verifyListAttributeContainsSingleValue(PRINCIPAL_QUERY, SQL, SQL_UPDATE2));
    }

    @Test
    public void principalQueryDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryAttributesForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        crud.delete(jdbcRealmAddress(JDBC_RLM_UPDATE), table, SQL_DELETE,
                ver -> ver.verifyListAttributeDoesNotContainSingleValue(PRINCIPAL_QUERY, SQL, SQL_DELETE));
    }

    // clear password mapper tab
    @Test
    public void principalQueryClearPasswordMapperAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryClearPasswordForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.select(SQL_CPM_CRT);
        page.getPrincipalQueryTabs().select(CLEAR_PASSWORD_MAPPER_TAB);
        EmptyState emptyState = form.emptyState();
        emptyState.mainAction();
        int number = 12;
        AddResourceDialogFragment addDialog = console.addResourceDialog();
        addDialog.getForm().number(PASSWORD_INDEX, number);
        addDialog.add();
        console.verifySuccess();
        ModelNode mapper = new ModelNode();
        mapper.get(PASSWORD_INDEX).set(number);
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeContainsObjectValue(
                PRINCIPAL_QUERY, SQL, SQL_CPM_CRT, CLEAR_PASSWORD_MAPPER, mapper);
    }

    @Test
    public void principalQueryClearPasswordMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryClearPasswordForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.select(SQL_CPM_UPD);
        page.getPrincipalQueryTabs().select(CLEAR_PASSWORD_MAPPER_TAB);
        long number = 23;
        ModelNode mapper = new ModelNode();
        mapper.get(PASSWORD_INDEX).set(number);
        crud.update(jdbcRealmAddress(JDBC_RLM_UPDATE), form, f -> f.number(PASSWORD_INDEX, number),
                ver -> ver.verifyListAttributeContainsObjectValue(
                        PRINCIPAL_QUERY, SQL, SQL_CPM_UPD, CLEAR_PASSWORD_MAPPER, mapper));
    }

    @Test
    public void principalQueryClearPasswordMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryClearPasswordForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.select(SQL_CPM_DEL);
        page.getPrincipalQueryTabs().select(CLEAR_PASSWORD_MAPPER_TAB);
        form.remove();
        console.verifySuccess();
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeObjectIsUndefined(
                PRINCIPAL_QUERY, SQL, SQL_CPM_DEL, CLEAR_PASSWORD_MAPPER);
    }

    // bcrypt mapper tab
    @Test
    public void principalQueryBcryptMapperAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryBcryptForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.select(SQL_BCM_CRT);
        page.getPrincipalQueryTabs().select(BCRYPT_MAPPER_TAB);
        EmptyState emptyState = form.emptyState();
        emptyState.mainAction();
        int iterCount = 12;
        int passIdx = 23;
        int saltIdx = 34;
        AddResourceDialogFragment addDialog = console.addResourceDialog();
        addDialog.getForm().number(ITERATION_COUNT_INDEX, iterCount);
        addDialog.getForm().number(PASSWORD_INDEX, passIdx);
        addDialog.getForm().number(SALT_INDEX, saltIdx);
        addDialog.add();
        console.verifySuccess();
        ModelNode mapper = new ModelNode();
        mapper.get(ITERATION_COUNT_INDEX).set(iterCount);
        mapper.get(PASSWORD_INDEX).set(passIdx);
        mapper.get(SALT_INDEX).set(saltIdx);
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeContainsObjectValue(
                PRINCIPAL_QUERY, SQL, SQL_BCM_CRT, BCRYPT_MAPPER, mapper);
    }

    @Test
    public void principalQueryBcryptMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryBcryptForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.select(SQL_BCM_UPD);
        page.getPrincipalQueryTabs().select(BCRYPT_MAPPER_TAB);
        long iterCount = 91;
        long passIdx = 92;
        long saltIdx = 93;
        ModelNode mapper = new ModelNode();
        mapper.get(ITERATION_COUNT_INDEX).set(iterCount);
        mapper.get(PASSWORD_INDEX).set(passIdx);
        mapper.get(SALT_INDEX).set(saltIdx);
        crud.update(jdbcRealmAddress(JDBC_RLM_UPDATE), form, f -> {
                    f.number(ITERATION_COUNT_INDEX, iterCount);
                    f.number(PASSWORD_INDEX, passIdx);
                    f.number(SALT_INDEX, saltIdx);
                },
                ver -> ver.verifyListAttributeContainsObjectValue(
                        PRINCIPAL_QUERY, SQL, SQL_BCM_UPD, BCRYPT_MAPPER, mapper));
    }

    @Test
    public void principalQueryBcryptMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryBcryptForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.select(SQL_BCM_DEL);
        page.getPrincipalQueryTabs().select(BCRYPT_MAPPER_TAB);
        form.remove();
        console.verifySuccess();
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeObjectIsUndefined(
                PRINCIPAL_QUERY, SQL, SQL_BCM_DEL, BCRYPT_MAPPER);
    }

    // salted simple digest mapper tab
    @Test
    public void principalQuerySaltedSimpleDigestMapperAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQuerySaltedSimpleDigestForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_SSDM_CRT);
        page.getPrincipalQueryTabs().select(SALTED_SIMPLE_DIGEST_MAPPER_TAB);
        EmptyState emptyState = form.emptyState();
        emptyState.mainAction();
        int passIdx = 72;
        int saltIdx = 34;
        AddResourceDialogFragment addDialog = console.addResourceDialog();
        addDialog.getForm().number(PASSWORD_INDEX, passIdx);
        addDialog.getForm().number(SALT_INDEX, saltIdx);
        addDialog.add();
        console.verifySuccess();
        ModelNode mapper = new ModelNode();
        mapper.get(PASSWORD_INDEX).set(passIdx);
        mapper.get(SALT_INDEX).set(saltIdx);
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeContainsObjectValue(
                PRINCIPAL_QUERY, SQL, SQL_SSDM_CRT, SALTED_SIMPLE_DIGEST_MAPPER, mapper);
    }

    @Test
    public void principalQuerySaltedSimpleDigestMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQuerySaltedSimpleDigestForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_SSDM_UPD);
        page.getPrincipalQueryTabs().select(SALTED_SIMPLE_DIGEST_MAPPER_TAB);
        long passIdx = 81;
        long saltIdx = 82;
        ModelNode mapper = new ModelNode();
        mapper.get(PASSWORD_INDEX).set(passIdx);
        mapper.get(SALT_INDEX).set(saltIdx);
        crud.update(jdbcRealmAddress(JDBC_RLM_UPDATE), form, f -> {
                    f.number(PASSWORD_INDEX, passIdx);
                    f.number(SALT_INDEX, saltIdx);
                },
                ver -> ver.verifyListAttributeContainsObjectValue(PRINCIPAL_QUERY, SQL, SQL_SSDM_UPD,
                        SALTED_SIMPLE_DIGEST_MAPPER, mapper));
    }

    @Test
    public void principalQuerySaltedSimpleDigestMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQuerySaltedSimpleDigestForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_SSDM_DEL);
        page.getPrincipalQueryTabs().select(SALTED_SIMPLE_DIGEST_MAPPER_TAB);
        form.remove();
        console.verifySuccess();
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeObjectIsUndefined(
                PRINCIPAL_QUERY, SQL, SQL_SSDM_DEL, SALTED_SIMPLE_DIGEST_MAPPER);
    }

    // simple digest mapper tab
    @Test
    public void principalQuerySimpleDigestMapperAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQuerySimpleDigestForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_SDM_CRT);
        page.getPrincipalQueryTabs().select(SIMPLE_DIGEST_MAPPER_TAB);
        EmptyState emptyState = form.emptyState();
        emptyState.mainAction();
        int passIdx = 46;
        AddResourceDialogFragment addDialog = console.addResourceDialog();
        addDialog.getForm().number(PASSWORD_INDEX, passIdx);
        addDialog.add();
        console.verifySuccess();
        ModelNode mapper = new ModelNode();
        mapper.get(PASSWORD_INDEX).set(passIdx);
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeContainsObjectValue(
                PRINCIPAL_QUERY, SQL, SQL_SDM_CRT, SIMPLE_DIGEST_MAPPER, mapper);
    }

    @Test
    public void principalQuerySimpleDigestMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQuerySimpleDigestForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_SDM_UPD);
        page.getPrincipalQueryTabs().select(SIMPLE_DIGEST_MAPPER_TAB);
        long passIdx = 81;
        ModelNode mapper = new ModelNode();
        mapper.get(PASSWORD_INDEX).set(passIdx);
        crud.update(jdbcRealmAddress(JDBC_RLM_UPDATE), form, f -> {
                    f.number(PASSWORD_INDEX, passIdx);
                },
                ver -> ver.verifyListAttributeContainsObjectValue(PRINCIPAL_QUERY, SQL, SQL_SDM_UPD,
                        SIMPLE_DIGEST_MAPPER, mapper));
    }

    @Test
    public void principalQuerySimpleDigestMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQuerySimpleDigestForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_SDM_DEL);
        page.getPrincipalQueryTabs().select(SIMPLE_DIGEST_MAPPER_TAB);
        form.remove();
        console.verifySuccess();
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeObjectIsUndefined(
                PRINCIPAL_QUERY, SQL, SQL_SDM_DEL, SIMPLE_DIGEST_MAPPER);
    }

    // scram mapper tab
    @Test
    public void principalQueryScramMapperAdd() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryScramForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_SM_CRT);
        page.getPrincipalQueryTabs().select(SCRAM_MAPPER_TAB);
        EmptyState emptyState = form.emptyState();
        emptyState.mainAction();
        int iterCount = 91;
        int passIdx = 92;
        int saltIdx = 93;
        ModelNode mapper = new ModelNode();
        mapper.get(ITERATION_COUNT_INDEX).set(iterCount);
        mapper.get(PASSWORD_INDEX).set(passIdx);
        mapper.get(SALT_INDEX).set(saltIdx);
        AddResourceDialogFragment addDialog = console.addResourceDialog();
        addDialog.getForm().number(ITERATION_COUNT_INDEX, iterCount);
        addDialog.getForm().number(PASSWORD_INDEX, passIdx);
        addDialog.getForm().number(SALT_INDEX, saltIdx);
        addDialog.add();
        console.verifySuccess();
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeContainsObjectValue(
                PRINCIPAL_QUERY, SQL, SQL_SM_CRT, SCRAM_MAPPER, mapper);
    }

    @Test
    public void principalQueryScramMapperUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryScramForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_SM_UPD);
        page.getPrincipalQueryTabs().select(SCRAM_MAPPER_TAB);
        long iterCount = 61;
        long passIdx = 62;
        long saltIdx = 63;
        ModelNode mapper = new ModelNode();
        mapper.get(ITERATION_COUNT_INDEX).set(iterCount);
        mapper.get(PASSWORD_INDEX).set(passIdx);
        mapper.get(SALT_INDEX).set(saltIdx);
        crud.update(jdbcRealmAddress(JDBC_RLM_UPDATE), form, f -> {
                    f.number(ITERATION_COUNT_INDEX, iterCount);
                    f.number(PASSWORD_INDEX, passIdx);
                    f.number(SALT_INDEX, saltIdx);
                },
                ver -> ver.verifyListAttributeContainsObjectValue(PRINCIPAL_QUERY, SQL, SQL_SM_UPD,
                        SCRAM_MAPPER, mapper));
    }

    @Test
    public void principalQueryScramMapperDelete() throws Exception {
        console.verticalNavigation().selectSecondary(SECURITY_REALM_ITEM, JDBC_REALM_ITEM);
        TableFragment jdbcTable = page.getJdbcRealmTable();
        TableFragment table = page.getPrincipalQueryTable();
        FormFragment form = page.getPrincipalQueryScramForm();
        table.bind(form);
        jdbcTable.action(JDBC_RLM_UPDATE, PQ_LABEL);
        waitGui().until().element(table.getRoot()).is().visible();
        table.filterAndSelect(SQL_SM_DEL);
        page.getPrincipalQueryTabs().select(SCRAM_MAPPER_TAB);
        form.remove();
        console.verifySuccess();
        new ResourceVerifier(jdbcRealmAddress(JDBC_RLM_UPDATE), client).verifyListAttributeObjectIsUndefined(
                PRINCIPAL_QUERY, SQL, SQL_SM_DEL, SCRAM_MAPPER);
    }




}
