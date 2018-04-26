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
package org.jboss.hal.testsuite.test.configuration.elytron.factoriestransformers;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.fragment.AddResourceDialogFragment;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATTERN;
import static org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures.*;

@RunWith(Arquillian.class)
public class PrincipalTransformersTest extends AbstractFactoriesTransformersTest {

    // --------------- aggregate-principal-transformer

    @Test
    public void aggregatePrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();

        console.waitNoNotification();
        crud.create(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, AGG_PRI_TRANS_CREATE);
            f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE).add(CONS_PRI_TRANS_UPDATE2);
        });
    }

    @Test
    public void aggregatePrincipalTransformerTryCreate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, AGG_PRI_TRANS_CREATE, PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void aggregatePrincipalTransformerTryCreateMin() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();

        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, AGG_PRI_TRANS_CREATE);
        dialog.getForm().list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE3);
        dialog.add();

        console.verifyError();
    }

    @Test
    public void aggregatePrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();
        FormFragment form = page.getAggregatePrincipalTransformerForm();
        table.bind(form);

        ModelNode expected = new ModelNode();
        expected.add(CONS_PRI_TRANS_UPDATE).add(CONS_PRI_TRANS_UPDATE2).add(CONS_PRI_TRANS_UPDATE4);
        table.select(AGG_PRI_TRANS_UPDATE);
        crud.update(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_UPDATE), form,
                f -> f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE4),
                changes -> changes.verifyAttribute(PRINCIPAL_TRANSFORMERS, expected));
    }

    @Test
    public void aggregatePrincipalTransformerTryUpdate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();
        FormFragment form = page.getAggregatePrincipalTransformerForm();
        table.bind(form);

        table.select(AGG_PRI_TRANS_TRY_UPDATE);
        crud.updateWithError(form, f -> f.list(PRINCIPAL_TRANSFORMERS).removeTags(),
                PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void aggregatePrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, AGGREGATE_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getAggregatePrincipalTransformerTable();
        crud.delete(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_DELETE), table, AGG_PRI_TRANS_DELETE);
    }

    // --------------- chained-principal-transformer

    @Test
    public void chainedPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();

        console.waitNoNotification();
        crud.create(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, CHA_PRI_TRANS_CREATE);
            f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE5).add(CONS_PRI_TRANS_UPDATE6);
        });
    }

    @Test
    public void chainedPrincipalTransformerTryCreate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, CHA_PRI_TRANS_CREATE, PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void chainedPrincipalTransformerTryCreateMin() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();

        AddResourceDialogFragment dialog = table.add();
        dialog.getForm().text(NAME, CHA_PRI_TRANS_CREATE);
        dialog.getForm().list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE7);
        dialog.add();

        console.verifyError();
    }

    @Test
    public void chainedPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();
        FormFragment form = page.getChainedPrincipalTransformerForm();
        table.bind(form);

        ModelNode expected = new ModelNode();
        expected.add(CONS_PRI_TRANS_UPDATE).add(CONS_PRI_TRANS_UPDATE2).add(CONS_PRI_TRANS_UPDATE8);
        table.select(CHA_PRI_TRANS_UPDATE);
        crud.update(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_UPDATE), form,
                f -> f.list(PRINCIPAL_TRANSFORMERS).add(CONS_PRI_TRANS_UPDATE8),
                changes -> changes.verifyAttribute(PRINCIPAL_TRANSFORMERS, expected));
    }

    @Test
    public void chainedPrincipalTransformerTryUpdate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();
        FormFragment form = page.getChainedPrincipalTransformerForm();
        table.bind(form);

        table.select(CHA_PRI_TRANS_UPDATE);
        crud.updateWithError(form, f -> f.list(PRINCIPAL_TRANSFORMERS).removeTags(),
                PRINCIPAL_TRANSFORMERS);
    }

    @Test
    public void chainedPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CHAINED_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getChainedPrincipalTransformerTable();
        crud.delete(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_DELETE), table, CHA_PRI_TRANS_DELETE);
    }

    // --------------- constant-principal-transformer

    @Test
    public void constantPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();

        console.waitNoNotification();
        crud.create(constantPrincipalTransformerAddress(CONS_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, CONS_PRI_TRANS_CREATE);
            f.text(CONSTANT, ANY_STRING);
        });
    }

    @Test
    public void constantPrincipalTransformerTryCreate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, CONS_PRI_TRANS_CREATE, CONSTANT);
    }

    @Test
    public void constantPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();
        FormFragment form = page.getConstantPrincipalTransformerForm();
        table.bind(form);

        table.select(CONS_PRI_TRANS_UPDATE);
        crud.update(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE), form, CONSTANT);
    }

    @Test
    public void constantPrincipalTransformerTryUpdate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();
        FormFragment form = page.getConstantPrincipalTransformerForm();
        table.bind(form);

        table.select(CONS_PRI_TRANS_UPDATE);
        crud.updateWithError(form, f -> f.clear(CONSTANT), CONSTANT);
    }

    @Test
    public void constantPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, CONSTANT_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getConstantPrincipalTransformerTable();
        crud.delete(constantPrincipalTransformerAddress(CONS_PRI_TRANS_DELETE), table, CONS_PRI_TRANS_DELETE);
    }


    // --------------- regex-principal-transformer

    @Test
    public void regexPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();

        console.waitNoNotification();
        crud.create(regexPrincipalTransformerAddress(REG_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, REG_PRI_TRANS_CREATE);
            f.text(PATTERN, ANY_STRING);
            f.text(REPLACEMENT, ANY_STRING);
        });
    }

    @Test
    public void regexPrincipalTransformerTryCreate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, REG_PRI_TRANS_CREATE, PATTERN);
    }

    @Test
    public void regexPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();
        FormFragment form = page.getRegexPrincipalTransformerForm();
        table.bind(form);

        table.select(REG_PRI_TRANS_UPDATE);
        crud.update(regexPrincipalTransformerAddress(REG_PRI_TRANS_UPDATE), form, PATTERN);
    }

    @Test
    public void regexPrincipalTransformerTryUpdate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();
        FormFragment form = page.getRegexPrincipalTransformerForm();
        table.bind(form);

        table.select(REG_PRI_TRANS_TRY_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATTERN), PATTERN);
    }

    @Test
    public void regexPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexPrincipalTransformerTable();
        crud.delete(regexPrincipalTransformerAddress(REG_PRI_TRANS_DELETE), table, REG_PRI_TRANS_DELETE);
    }

    // --------------- regex-validating-principal-transformer

    @Test
    public void regexValidatingPrincipalTransformerCreate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();

        console.waitNoNotification();
        crud.create(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_CREATE), table, f -> {
            f.text(NAME, REGV_PRI_TRANS_CREATE);
            f.text(PATTERN, ANY_STRING);
        });
    }

    @Test
    public void regexValidatingPrincipalTransformerTryCreate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();

        console.waitNoNotification();
        crud.createWithErrorAndCancelDialog(table, REGV_PRI_TRANS_CREATE, PATTERN);
    }

    @Test
    public void regexValidatingPrincipalTransformerUpdate() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();
        FormFragment form = page.getRegexValidatingPrincipalTransformerForm();
        table.bind(form);

        table.select(REGV_PRI_TRANS_UPDATE);
        crud.update(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_UPDATE), form, PATTERN);
    }

    @Test
    public void regexValidatingPrincipalTransformerTryUpdate() {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();
        FormFragment form = page.getRegexValidatingPrincipalTransformerForm();
        table.bind(form);

        table.select(REGV_PRI_TRANS_TRY_UPDATE);
        crud.updateWithError(form, f -> f.clear(PATTERN), PATTERN);
    }

    @Test
    public void regexValidatingPrincipalTransformerDelete() throws Exception {
        console.verticalNavigation().selectSecondary(TRANSFORMERS_ITEM, REGEX_VALIDATING_PRINCIPAL_TRANSFORMER_ITEM);
        TableFragment table = page.getRegexValidatingPrincipalTransformerTable();
        crud.delete(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_DELETE), table,
                REGV_PRI_TRANS_DELETE);
    }


}
