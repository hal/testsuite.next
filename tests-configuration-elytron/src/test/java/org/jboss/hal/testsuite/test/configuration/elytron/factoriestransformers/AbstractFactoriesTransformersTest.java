package org.jboss.hal.testsuite.test.configuration.elytron.factoriestransformers;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.ElytronFactoriesTransformersPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;

import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT_REALM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.FILTERS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MECHANISM_CONFIGURATIONS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MECHANISM_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MECHANISM_REALM_CONFIGURATIONS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATTERN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATTERN_FILTER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROVIDER_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALMS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALM_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SECURITY_DOMAIN;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.*;

public class AbstractFactoriesTransformersTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final Administration adminOps = new Administration(client);
    protected static final String ANY_STRING = Random.name();
    protected static final ModelNode FILTER_CREATE_MODEL = new ModelNode();
    protected static final ModelNode FILTER_UPDATE2_MODEL = new ModelNode();
    protected static final ModelNode FILTER_DELETE_MODEL = new ModelNode();

    @BeforeClass
    public static void beforeTests() throws Exception {

        ModelNode httpServerFactoriesModel = new ModelNode();
        httpServerFactoriesModel.add(PROV_HTTP_UPDATE);
        httpServerFactoriesModel.add(PROV_HTTP_UPDATE2);
        Values HTTP_PARAMS = Values.of(HTTP_SERVER_MECH_FACTORIES, httpServerFactoriesModel);

        ModelNode saslServerFactoriesModel = new ModelNode();
        saslServerFactoriesModel.add(PROV_SASL_UPDATE);
        saslServerFactoriesModel.add(PROV_SASL_UPDATE2);
        Values SASL_PARAMS = Values.of(SASL_SERVER_FACTORIES, saslServerFactoriesModel);

        ModelNode principalTransformersModel = new ModelNode();
        principalTransformersModel.add(CONS_PRI_TRANS_UPDATE);
        principalTransformersModel.add(CONS_PRI_TRANS_UPDATE2);
        Values AGG_PRI_PARAMS = Values.of(PRINCIPAL_TRANSFORMERS, principalTransformersModel);

        // order is important as there are resources that should are used elsewhere
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE2));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE3));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE4));
        operations.add(providerHttpServerMechanismFactoryAddress(PROV_HTTP_DELETE));
        operations.add(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE), HTTP_PARAMS);
        operations.add(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_TRY_UPDATE), HTTP_PARAMS);
        operations.add(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_DELETE), HTTP_PARAMS);
        operations.add(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE));
        operations.add(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_TRY_UPDATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE));
        operations.add(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_DELETE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE));

        ModelNode FILTER_UPDATE_MODEL = new ModelNode();
        ModelNode MECH_CONF_CREATE_MODEL = new ModelNode();
        ModelNode MECH_CONF_UPDATE_MODEL = new ModelNode();
        ModelNode MECH_CONF_UPDATE2_MODEL = new ModelNode();
        ModelNode MECH_CONF_DELETE_MODEL = new ModelNode();

        FILTER_CREATE_MODEL.get(PATTERN_FILTER).set(FILTERS_CREATE);
        FILTER_CREATE_MODEL.get(ENABLING).set(true);
        FILTER_UPDATE_MODEL.get(PATTERN_FILTER).set(FILTERS_UPDATE);
        FILTER_UPDATE_MODEL.get(ENABLING).set(true);
        FILTER_UPDATE2_MODEL.get(PATTERN_FILTER).set(FILTERS_UPDATE2);
        FILTER_UPDATE2_MODEL.get(ENABLING).set(true);
        FILTER_DELETE_MODEL.get(PATTERN_FILTER).set(FILTERS_DELETE);
        FILTER_DELETE_MODEL.get(ENABLING).set(true);

        operations.writeListAttribute(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE), FILTERS,
                FILTER_UPDATE_MODEL, FILTER_DELETE_MODEL);

        String realmName = "local";
        operations.add(securityDomainAddress(SEC_DOM_UPDATE),
                Values.of(DEFAULT_REALM, realmName)
                .and(REALMS, new ModelNodeGenerator.ModelNodeListBuilder().addNode(
                        new ModelNodeGenerator.ModelNodePropertiesBuilder().addProperty(REALM, realmName).build())
                .build()));
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_TRY_UPDATE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));
        operations.add(httpAuthenticationFactoryAddress(HTTP_AUTH_DELETE),
                Values.of(HTTP_SERVER_MECH_FACTORY, PROV_HTTP_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));

        ModelNode realmNameUpdate = new ModelNode();
        realmNameUpdate.get(REALM_NAME).set(MECH_RE_CONF_UPDATE);
        ModelNode realmNameDelete = new ModelNode();
        realmNameDelete.get(REALM_NAME).set(MECH_RE_CONF_DELETE);
        MECH_CONF_CREATE_MODEL.get(MECHANISM_NAME).set(MECH_CONF_CREATE);
        MECH_CONF_UPDATE_MODEL.get(MECHANISM_NAME).set(MECH_CONF_UPDATE);
        MECH_CONF_UPDATE_MODEL.get(MECHANISM_REALM_CONFIGURATIONS).add(realmNameUpdate);
        MECH_CONF_UPDATE_MODEL.get(MECHANISM_REALM_CONFIGURATIONS).add(realmNameDelete);
        MECH_CONF_UPDATE2_MODEL.get(MECHANISM_NAME).set(MECH_CONF_UPDATE2);
        MECH_CONF_DELETE_MODEL.get(MECHANISM_NAME).set(MECH_CONF_DELETE);

        operations.writeListAttribute(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE), MECHANISM_CONFIGURATIONS,
                MECH_CONF_UPDATE_MODEL, MECH_CONF_DELETE_MODEL);

        operations.add(providerLoaderAddress(PROV_LOAD_UPDATE));
        operations.add(providerLoaderAddress(PROV_LOAD_DELETE));

        operations.add(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_UPDATE));
        operations.add(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_DELETE));

        operations.add(providerSaslServerFactoryAddress(PROV_SASL_UPDATE));
        operations.add(providerSaslServerFactoryAddress(PROV_SASL_UPDATE2));
        operations.add(providerSaslServerFactoryAddress(PROV_SASL_UPDATE3));
        operations.add(providerSaslServerFactoryAddress(PROV_SASL_UPDATE4));
        operations.add(providerSaslServerFactoryAddress(PROV_SASL_DELETE));

        operations.add(aggregateSaslServerFactoryAddress(AGG_SASL_UPDATE), SASL_PARAMS);
        operations.add(aggregateSaslServerFactoryAddress(AGG_SASL_DELETE), SASL_PARAMS);

        operations.add(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));
        operations.add(configurableSaslServerFactoryAddress(CONF_SASL_DELETE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));

        operations.writeListAttribute(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE), FILTERS,
                FILTER_UPDATE_MODEL, FILTER_DELETE_MODEL);

        operations.add(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));
        operations.add(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_TRY_UPDATE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));
        operations.add(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_DELETE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE));

        ModelNode mechProvfilterUpdate = new ModelNode();
        mechProvfilterUpdate.get(PROVIDER_NAME).set(FILTERS_UPDATE);
        ModelNode mechProvfilterDelete = new ModelNode();
        mechProvfilterDelete.get(PROVIDER_NAME).set(FILTERS_DELETE);

        operations.writeListAttribute(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE), FILTERS,
                mechProvfilterUpdate, mechProvfilterDelete);

        operations.add(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));
        operations.add(saslAuthenticationFactoryAddress(SASL_AUTH_DELETE),
                Values.of(SASL_SERVER_FACTORY, PROV_SASL_UPDATE).and(SECURITY_DOMAIN, SEC_DOM_UPDATE));

        operations.writeListAttribute(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE), MECHANISM_CONFIGURATIONS,
                MECH_CONF_UPDATE_MODEL, MECH_CONF_DELETE_MODEL);

        operations.add(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_UPDATE));
        operations.add(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_DELETE));

        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE2), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE3), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE4), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE5), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE6), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE7), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE8), Values.of(CONSTANT, ANY_STRING));
        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_DELETE), Values.of(CONSTANT, ANY_STRING));

        operations.add(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_UPDATE), AGG_PRI_PARAMS);
        operations.add(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_TRY_UPDATE), AGG_PRI_PARAMS);
        operations.add(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_DELETE), AGG_PRI_PARAMS);

        operations.add(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_UPDATE), AGG_PRI_PARAMS);
        operations.add(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_DELETE), AGG_PRI_PARAMS);

        operations.add(regexPrincipalTransformerAddress(REG_PRI_TRANS_UPDATE),
                Values.of(PATTERN, ANY_STRING).and(REPLACEMENT, ANY_STRING));
        operations.add(regexPrincipalTransformerAddress(REG_PRI_TRANS_TRY_UPDATE),
                Values.of(PATTERN, ANY_STRING).and(REPLACEMENT, ANY_STRING));
        operations.add(regexPrincipalTransformerAddress(REG_PRI_TRANS_DELETE),
                Values.of(PATTERN, ANY_STRING).and(REPLACEMENT, ANY_STRING));

        operations.add(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_UPDATE),
                Values.of(PATTERN, ANY_STRING));
        operations.add(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_TRY_UPDATE),
                Values.of(PATTERN, ANY_STRING));
        operations.add(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_DELETE),
                Values.of(PATTERN, ANY_STRING));

        operations.add(kerberosSecurityFactoryAddress(KERB_UPDATE),
                Values.of(PATH, ANY_STRING).and(PRINCIPAL, ANY_STRING));
        operations.add(kerberosSecurityFactoryAddress(KERB_TRY_UPDATE),
                Values.of(PATH, ANY_STRING).and(PRINCIPAL, ANY_STRING));
        operations.add(kerberosSecurityFactoryAddress(KERB_DELETE),
                Values.of(PATH, ANY_STRING).and(PRINCIPAL, ANY_STRING));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_DELETE));
            operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_UPDATE));
            operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_TRY_UPDATE));
            operations.remove(aggregateHttpServerMechanismFactoryAddress(AGG_HTTP_CREATE));
            operations.remove(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_UPDATE));
            operations.remove(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_TRY_UPDATE));
            operations.remove(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_DELETE));
            operations.remove(configurableHttpServerMechanismFactoryAddress(CONF_HTTP_CREATE));
            operations.remove(httpAuthenticationFactoryAddress(HTTP_AUTH_UPDATE));
            operations.remove(httpAuthenticationFactoryAddress(HTTP_AUTH_TRY_UPDATE));
            operations.remove(httpAuthenticationFactoryAddress(HTTP_AUTH_DELETE));
            operations.remove(httpAuthenticationFactoryAddress(HTTP_AUTH_CREATE));
            operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE));
            operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE2));
            operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE3));
            operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_UPDATE4));
            operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_DELETE));
            operations.remove(providerHttpServerMechanismFactoryAddress(PROV_HTTP_CREATE));
            operations.remove(aggregateSaslServerFactoryAddress(AGG_SASL_UPDATE));
            operations.remove(aggregateSaslServerFactoryAddress(AGG_SASL_DELETE));
            operations.remove(aggregateSaslServerFactoryAddress(AGG_SASL_CREATE));
            operations.remove(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_CREATE));
            operations.remove(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_UPDATE));
            operations.remove(serviceLoaderHttpServerMechanismFactoryAddress(SVC_LOAD_HTTP_DELETE));
            operations.remove(configurableSaslServerFactoryAddress(CONF_SASL_UPDATE));
            operations.remove(configurableSaslServerFactoryAddress(CONF_SASL_DELETE));
            operations.remove(configurableSaslServerFactoryAddress(CONF_SASL_CREATE));
            operations.remove(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_CREATE));
            operations.remove(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_UPDATE));
            operations.remove(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_TRY_UPDATE));
            operations.remove(mechanismProviderFilteringSaslServerFactoryAddress(MECH_SASL_DELETE));
            operations.remove(saslAuthenticationFactoryAddress(SASL_AUTH_UPDATE));
            operations.remove(saslAuthenticationFactoryAddress(SASL_AUTH_DELETE));
            operations.remove(saslAuthenticationFactoryAddress(SASL_AUTH_CREATE));
            operations.remove(kerberosSecurityFactoryAddress(KERB_UPDATE));
            operations.remove(kerberosSecurityFactoryAddress(KERB_TRY_UPDATE));
            operations.remove(kerberosSecurityFactoryAddress(KERB_DELETE));
            operations.remove(kerberosSecurityFactoryAddress(KERB_CREATE));
            operations.remove(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_UPDATE));
            operations.remove(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_TRY_UPDATE));
            operations.remove(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_DELETE));
            operations.remove(aggregatePrincipalTransformerAddress(AGG_PRI_TRANS_CREATE));
            operations.remove(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_UPDATE));
            operations.remove(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_DELETE));
            operations.remove(chainedPrincipalTransformerAddress(CHA_PRI_TRANS_CREATE));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_CREATE));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE2));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE3));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE4));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE5));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE6));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE7));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE8));
            operations.remove(constantPrincipalTransformerAddress(CONS_PRI_TRANS_DELETE));
            operations.remove(regexPrincipalTransformerAddress(REG_PRI_TRANS_CREATE));
            operations.remove(regexPrincipalTransformerAddress(REG_PRI_TRANS_UPDATE));
            operations.remove(regexPrincipalTransformerAddress(REG_PRI_TRANS_TRY_UPDATE));
            operations.remove(regexPrincipalTransformerAddress(REG_PRI_TRANS_DELETE));
            operations.remove(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_CREATE));
            operations.remove(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_UPDATE));
            operations.remove(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_TRY_UPDATE));
            operations.remove(regexValidatingPrincipalTransformerAddress(REGV_PRI_TRANS_DELETE));
            operations.remove(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_CREATE));
            operations.remove(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_UPDATE));
            operations.remove(serviceLoaderSaslServerFactoryAddress(SVC_LOAD_SASL_DELETE));
            operations.remove(providerSaslServerFactoryAddress(PROV_SASL_CREATE));
            operations.remove(providerSaslServerFactoryAddress(PROV_SASL_UPDATE));
            operations.remove(providerSaslServerFactoryAddress(PROV_SASL_UPDATE2));
            operations.remove(providerSaslServerFactoryAddress(PROV_SASL_UPDATE3));
            operations.remove(providerSaslServerFactoryAddress(PROV_SASL_UPDATE4));
            operations.remove(providerSaslServerFactoryAddress(PROV_SASL_DELETE));
            operations.remove(providerLoaderAddress(PROV_LOAD_CREATE));
            operations.remove(providerLoaderAddress(PROV_LOAD_UPDATE));
            operations.remove(providerLoaderAddress(PROV_LOAD_DELETE));
            operations.remove(securityDomainAddress(SEC_DOM_UPDATE));
            adminOps.reloadIfRequired();
        } finally {
            client.close();
        }
    }

    @Page
    protected ElytronFactoriesTransformersPage page;
    @Inject
    protected Console console;
    @Inject
    protected CrudOperations crud;

    public AbstractFactoriesTransformersTest() {
        super();
    }

    @Before
    public void setUp() throws Exception {
        adminOps.reloadIfRequired();
        page.navigate();
    }

}
