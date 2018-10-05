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

package org.jboss.hal.testsuite.page.configuration;

import org.jboss.hal.meta.token.NameTokens;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.jboss.hal.testsuite.page.BasePage;
import org.jboss.hal.testsuite.page.Place;
import org.openqa.selenium.support.FindBy;

import static org.jboss.hal.dmr.ModelDescriptionConstants.*;
import static org.jboss.hal.resources.Ids.*;
import static org.jboss.hal.resources.Ids.TABLE;
import static org.jboss.hal.testsuite.Selectors.WRAPPER;

@Place(NameTokens.ELYTRON_MAPPERS_DECODERS)
public class ElytronMappersDecodersPage extends BasePage {

    private static final String MAPPERS_DECODERS = "mappers-decoders";

    // Add Prefix Role Mapper
    @FindBy(id = MAPPERS_DECODERS + "-" + ADD_PREFIX_ROLE_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment addPrefixRoleMapperTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + ADD_PREFIX_ROLE_MAPPER + "-" + FORM) private FormFragment addPrefixRoleMapperForm;

    // Add Suffix Role Mapper
    @FindBy(id = MAPPERS_DECODERS + "-" + ADD_SUFFIX_ROLE_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment addSuffixRoleMapperTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + ADD_SUFFIX_ROLE_MAPPER + "-" + FORM) private FormFragment addSuffixRoleMapperForm;

    // Add Aggregate Role Mapper
    @FindBy(id = MAPPERS_DECODERS + "-" + AGGREGATE_ROLE_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment aggregateRoleMapperTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + AGGREGATE_ROLE_MAPPER + "-" + FORM) private FormFragment aggregateRoleMapperForm;

    // Add Constant Role Mapper
    @FindBy(id = MAPPERS_DECODERS + "-" + CONSTANT_ROLE_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment constantRoleMapperTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + CONSTANT_ROLE_MAPPER + "-" + FORM) private FormFragment constantRoleMapperForm;

    // Add Custom Role Mapper
    @FindBy(id = MAPPERS_DECODERS + "-" + CUSTOM_ROLE_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment customRoleMapperTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + CUSTOM_ROLE_MAPPER + "-" + FORM) private FormFragment customRoleMapperForm;

    // Add Logical Role Mapper
    @FindBy(id = MAPPERS_DECODERS + "-" + LOGICAL_ROLE_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment logicalRoleMapperTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + LOGICAL_ROLE_MAPPER + "-" + FORM) private FormFragment logicalRoleMapperForm;

    // Custom Permission Mapper
    @FindBy(id = MAPPERS_DECODERS + "-" + CUSTOM_PERMISSION_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment customPermissionMapperTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + CUSTOM_PERMISSION_MAPPER + "-" + FORM) private FormFragment customPermissionMapperForm;

    // Logical Permission Mapper
    @FindBy(id = MAPPERS_DECODERS + "-" + LOGICAL_PERMISSION_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment logicalPermissionMapperTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + LOGICAL_PERMISSION_MAPPER + "-" + FORM) private FormFragment logicalPermissionMapperForm;

    // Constant Permission Mapper
    @FindBy(id = ELYTRON_CONSTANT_PERMISSION_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment constantPermissionMapperTable;

    // Constant Permission Mapper / Permissions
    @FindBy(id = ELYTRON_CONSTANT_PERMISSION_MAPPER + "-" + PERMISSIONS + "-" + TABLE +  WRAPPER) private TableFragment constantPermissionMapperPermissionsTable;
    @FindBy(id = ELYTRON_CONSTANT_PERMISSION_MAPPER + "-" + PERMISSIONS + "-" + FORM) private FormFragment constantPermissionMapperPermissionsForm;

    // Simple Permission Mapper
    @FindBy(id = ELYTRON_SIMPLE_PERMISSION_MAPPER + "-" + TABLE +  WRAPPER) private TableFragment simplePermissionMapperTable;
    @FindBy(id = ELYTRON_SIMPLE_PERMISSION_MAPPER + "-" + FORM) private FormFragment simplePermissionMapperForm;

    // Simple Permission Mapper / Permission Mappings
    @FindBy(id = "elytron-permission-mappings-" + TABLE +  WRAPPER) private TableFragment simplePMPermissionMappingsTable;
    @FindBy(id = "elytron-permission-mappings-" + FORM) private FormFragment simplePMPermissionMappingsForm;

    // Simple Permission Mapper / Permission Mappings / Permissions
    @FindBy(id = "elytron-permissions-" + TABLE +  WRAPPER) private TableFragment simplePMPermissionsTable;
    @FindBy(id = "elytron-permissions-" + FORM) private FormFragment simplePMPermissionsForm;

    // Mapped Role Mapper

    @FindBy(id = MAPPERS_DECODERS + "-" + "mapped-role-mapper" + "-" + TABLE + WRAPPER) private TableFragment mappedRoleMapperTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + "mapped-role-mapper" + "-" + FORM) private FormFragment mappedRoleMapperForm;

    // Aggregate Principal Decoder
    @FindBy(id = MAPPERS_DECODERS + "-" + AGGREGATE_PRINCIPAL_DECODER + "-" + TABLE +  WRAPPER) private TableFragment aggregatePrincipalDecoderTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + AGGREGATE_PRINCIPAL_DECODER + "-" + FORM) private FormFragment aggregatePrincipalDecoderForm;

    // Concatenating Principal Decoder
    @FindBy(id = MAPPERS_DECODERS + "-" + CONCATENATING_PRINCIPAL_DECODER + "-" + TABLE +  WRAPPER) private TableFragment concatenatingPrincipalDecoderTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + CONCATENATING_PRINCIPAL_DECODER + "-" + FORM) private FormFragment concatenatingPrincipalDecoderForm;

    // Constant Principal Decoder
    @FindBy(id = MAPPERS_DECODERS + "-" + CONSTANT_PRINCIPAL_DECODER + "-" + TABLE +  WRAPPER) private TableFragment constantPrincipalDecoderTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + CONSTANT_PRINCIPAL_DECODER + "-" + FORM) private FormFragment constantPrincipalDecoderForm;

    // Custom Principal Decoder
    @FindBy(id = MAPPERS_DECODERS + "-" + CUSTOM_PRINCIPAL_DECODER + "-" + TABLE +  WRAPPER) private TableFragment customPrincipalDecoderTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + CUSTOM_PRINCIPAL_DECODER + "-" + FORM) private FormFragment customPrincipalDecoderForm;

    // X500 Attribute Principal Decoder
    @FindBy(id = MAPPERS_DECODERS + "-" + X500_ATTRIBUTE_PRINCIPAL_DECODER + "-" + TABLE +  WRAPPER) private TableFragment x500PrincipalDecoderTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + X500_ATTRIBUTE_PRINCIPAL_DECODER + "-" + FORM) private FormFragment x500PrincipalDecoderForm;

    // Custom Role Decoder
    @FindBy(id = MAPPERS_DECODERS + "-" + CUSTOM_ROLE_DECODER + "-" + TABLE +  WRAPPER) private TableFragment customRoleDecoderTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + CUSTOM_ROLE_DECODER + "-" + FORM) private FormFragment customRoleDecoderForm;

    // Simple Role Decoder
    @FindBy(id = MAPPERS_DECODERS + "-" + SIMPLE_ROLE_DECODER + "-" + TABLE +  WRAPPER) private TableFragment simpleRoleDecoderTable;
    @FindBy(id = MAPPERS_DECODERS + "-" + SIMPLE_ROLE_DECODER + "-" + FORM) private FormFragment simpleRoleDecoderForm;

    public TableFragment getAddPrefixRoleMapperTable() {
        return addPrefixRoleMapperTable;
    }

    public FormFragment getAddPrefixRoleMapperForm() {
        return addPrefixRoleMapperForm;
    }

    public TableFragment getAddSuffixRoleMapperTable() {
        return addSuffixRoleMapperTable;
    }

    public FormFragment getAddSuffixRoleMapperForm() {
        return addSuffixRoleMapperForm;
    }

    public TableFragment getAggregateRoleMapperTable() {
        return aggregateRoleMapperTable;
    }

    public FormFragment getAggregateRoleMapperForm() {
        return aggregateRoleMapperForm;
    }

    public TableFragment getConstantRoleMapperTable() {
        return constantRoleMapperTable;
    }

    public FormFragment getConstantRoleMapperForm() {
        return constantRoleMapperForm;
    }

    public TableFragment getCustomRoleMapperTable() {
        return customRoleMapperTable;
    }

    public FormFragment getCustomRoleMapperForm() {
        return customRoleMapperForm;
    }

    public TableFragment getLogicalRoleMapperTable() {
        return logicalRoleMapperTable;
    }

    public FormFragment getLogicalRoleMapperForm() {
        return logicalRoleMapperForm;
    }

    public TableFragment getCustomPermissionMapperTable() {
        return customPermissionMapperTable;
    }

    public FormFragment getCustomPermissionMapperForm() {
        return customPermissionMapperForm;
    }

    public TableFragment getLogicalPermissionMapperTable() {
        return logicalPermissionMapperTable;
    }

    public FormFragment getLogicalPermissionMapperForm() {
        return logicalPermissionMapperForm;
    }

    public TableFragment getConstantPermissionMapperTable() {
        return constantPermissionMapperTable;
    }

    public TableFragment getConstantPermissionMapperPermissionsTable() {
        return constantPermissionMapperPermissionsTable;
    }

    public FormFragment getConstantPermissionMapperPermissionsForm() {
        return constantPermissionMapperPermissionsForm;
    }

    public TableFragment getSimplePermissionMapperTable() {
        return simplePermissionMapperTable;
    }

    public FormFragment getSimplePermissionMapperForm() {
        return simplePermissionMapperForm;
    }

    public TableFragment getSimplePMPermissionMappingsTable() {
        return simplePMPermissionMappingsTable;
    }

    public FormFragment getSimplePMPermissionMappingsForm() {
        return simplePMPermissionMappingsForm;
    }

    public TableFragment getSimplePMPermissionsTable() {
        return simplePMPermissionsTable;
    }

    public FormFragment getSimplePMPermissionsForm() {
        return simplePMPermissionsForm;
    }

    public TableFragment getMappedRoleMapperTable() {
        return mappedRoleMapperTable;
    }

    public FormFragment getMappedRoleMapperForm() {
        return mappedRoleMapperForm;
    }

    public TableFragment getAggregatePrincipalDecoderTable() {
        return aggregatePrincipalDecoderTable;
    }

    public FormFragment getAggregatePrincipalDecoderForm() {
        return aggregatePrincipalDecoderForm;
    }

    public TableFragment getConcatenatingPrincipalDecoderTable() {
        return concatenatingPrincipalDecoderTable;
    }

    public FormFragment getConcatenatingPrincipalDecoderForm() {
        return concatenatingPrincipalDecoderForm;
    }

    public TableFragment getConstantPrincipalDecoderTable() {
        return constantPrincipalDecoderTable;
    }

    public FormFragment getConstantPrincipalDecoderForm() {
        return constantPrincipalDecoderForm;
    }

    public TableFragment getCustomPrincipalDecoderTable() {
        return customPrincipalDecoderTable;
    }

    public FormFragment getCustomPrincipalDecoderForm() {
        return customPrincipalDecoderForm;
    }

    public TableFragment getX500PrincipalDecoderTable() {
        return x500PrincipalDecoderTable;
    }

    public FormFragment getX500PrincipalDecoderForm() {
        return x500PrincipalDecoderForm;
    }

    public TableFragment getCustomRoleDecoderTable() {
        return customRoleDecoderTable;
    }

    public FormFragment getCustomRoleDecoderForm() {
        return customRoleDecoderForm;
    }

    public TableFragment getSimpleRoleDecoderTable() {
        return simpleRoleDecoderTable;
    }

    public FormFragment getSimpleRoleDecoderForm() {
        return simpleRoleDecoderForm;
    }
}
