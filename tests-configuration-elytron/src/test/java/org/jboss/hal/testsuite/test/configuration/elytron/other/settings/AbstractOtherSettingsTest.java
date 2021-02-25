package org.jboss.hal.testsuite.test.configuration.elytron.other.settings;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.page.configuration.ElytronOtherSettingsPage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREATE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DEFAULT_REALM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DIR_CONTEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_MANAGER;
import static org.jboss.hal.dmr.ModelDescriptionConstants.KEY_STORE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.LOCATION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NEW_ITEM_ATTRIBUTES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NEW_ITEM_PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NEW_ITEM_RDN;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NEW_ITEM_TEMPLATE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PORT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REALMS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SEARCH_PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TYPE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.URL;
import static org.jboss.hal.dmr.ModelDescriptionConstants.VALUE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.*;
public class AbstractOtherSettingsTest {

    protected static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    protected static final Operations operations = new Operations(client);
    protected static final String PROPERTY_DELIMITER = ".";

    @BeforeClass
    public static void beforeTests() throws Exception {

        // used in key-store, as trust-manager requires a key-store with providers attribute set
        operations.add(providerLoaderAddress(PROV_LOAD_UPDATE));
        operations.add(providerLoaderAddress(PROV_LOAD_UPDATE2));
        operations.add(providerLoaderAddress(PROV_LOAD_UPDATE3));
        operations.add(providerLoaderAddress(PROV_LOAD_DELETE));

        // Stores
        ModelNode credRef = new ModelNode();
        credRef.get(CLEAR_TEXT).set(ANY_STRING);
        Values credParams = Values.of(CREATE, true).and(CREDENTIAL_REFERENCE, credRef).and(LOCATION, ANY_STRING);
        operations.add(credentialStoreAddress(CRED_ST_UPDATE), credParams);
        operations.add(credentialStoreAddress(CRED_ST_DELETE), credParams);

        Values ksParams = Values.of(TYPE, JKS).and(CREDENTIAL_REFERENCE, credRef);
        operations.add(keyStoreAddress(KEY_ST_UPDATE), ksParams);
        operations.add(keyStoreAddress(KEY_ST_CR_UPDATE), ksParams);
        operations.add(keyStoreAddress(KEY_ST_DELETE), ksParams);
        operations.writeAttribute(keyStoreAddress(KEY_ST_UPDATE), PROVIDERS, PROV_LOAD_UPDATE);

        operations.add(filteringKeyStoreAddress(FILT_ST_DELETE),
                Values.of(ALIAS_FILTER, ANY_STRING).and(KEY_STORE, KEY_ST_UPDATE));
        operations.add(filteringKeyStoreAddress(FILT_ST_UPDATE),
                Values.of(ALIAS_FILTER, ANY_STRING).and(KEY_STORE, KEY_ST_UPDATE));

        operations.add(dirContextAddress(DIR_UPDATE), Values.of(URL, ANY_STRING));
        operations.add(dirContextAddress(DIR_DELETE), Values.of(URL, ANY_STRING));

        Values dirCtxParams = Values.of(URL, ANY_STRING)
                .andObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, ANY_STRING));
        operations.add(dirContextAddress(DIR_CR_CRT), Values.of(URL, ANY_STRING));
        operations.add(dirContextAddress(DIR_CR_UPD), dirCtxParams);
        operations.add(dirContextAddress(DIR_CR_DEL), dirCtxParams);

        Values ldapKsValues = Values.of(DIR_CONTEXT, DIR_UPDATE).and(SEARCH_PATH, ANY_STRING);
        ModelNode props = new ModelNode();
        props.get(NAME).set("p1");
        props.get(VALUE).add(Random.name());
        ModelNode newItemTemplate = new ModelNode();
        newItemTemplate.get(NEW_ITEM_PATH).set(ANY_STRING);
        newItemTemplate.get(NEW_ITEM_RDN).set(ANY_STRING);
        newItemTemplate.get(NEW_ITEM_ATTRIBUTES).add(props);

        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_DELETE), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_TEMP1_UPDATE), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_TEMP2_DELETE), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_TEMP3_ADD), ldapKsValues);
        operations.add(ldapKeyStoreAddress(LDAPKEY_ST_TEMP4_TRY_ADD), ldapKsValues);
        operations.writeAttribute(ldapKeyStoreAddress(LDAPKEY_ST_TEMP1_UPDATE), NEW_ITEM_TEMPLATE, newItemTemplate);
        operations.writeAttribute(ldapKeyStoreAddress(LDAPKEY_ST_TEMP2_DELETE), NEW_ITEM_TEMPLATE, newItemTemplate);

        // SSL
        Values aggValues = Values.ofList(PROVIDERS, PROV_LOAD_UPDATE, PROV_LOAD_UPDATE2);
        operations.add(aggregateProvidersAddress(AGG_PRV_DELETE), aggValues);
        operations.add(aggregateProvidersAddress(AGG_PRV_UPDATE), aggValues);

        operations.add(clientSslContextAddress(CLI_SSL_DELETE));
        operations.add(clientSslContextAddress(CLI_SSL_UPDATE));

        Values keyManagerValues = Values.of(KEY_STORE, KEY_ST_UPDATE)
                .andObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, ANY_STRING));
        operations.add(keyManagerAddress(KEY_MAN_UPDATE), keyManagerValues);
        operations.add(keyManagerAddress(KEY_MAN_TRY_UPDATE), keyManagerValues);
        operations.add(keyManagerAddress(KEY_MAN_DELETE), keyManagerValues);

        Values serverSslContextValues = Values.of(KEY_MANAGER, KEY_MAN_UPDATE);
        operations.add(serverSslContextAddress(SRV_SSL_DELETE), serverSslContextValues);
        operations.add(serverSslContextAddress(SRV_SSL_UPDATE), serverSslContextValues);

        // a realm is required for new security-domain
        operations.add(filesystemRealmAddress(FILESYS_RLM_CREATE), Values.of(PATH, ANY_STRING));
        operations.add(filesystemRealmAddress(FILESYS_RLM_UPDATE), Values.of(PATH, ANY_STRING));
        ModelNode realmNode1 = new ModelNode();
        realmNode1.get(REALM).set(FILESYS_RLM_UPDATE);
        ModelNode realmNode2 = new ModelNode();
        realmNode2.get(REALM).set(FILESYS_RLM_CREATE);
        Values secDomainParams = Values.of(DEFAULT_REALM, FILESYS_RLM_UPDATE).andList(REALMS, realmNode1);
        operations.add(securityDomainAddress(SEC_DOM_UPDATE), secDomainParams);
        operations.add(securityDomainAddress(SEC_DOM_UPDATE2), secDomainParams);
        operations.add(securityDomainAddress(SEC_DOM_UPDATE3),
                Values.of(DEFAULT_REALM, FILESYS_RLM_UPDATE).andList(REALMS, realmNode1, realmNode2));
        operations.add(securityDomainAddress(SEC_DOM_DELETE));

        operations.add(trustManagerAddress(TRU_MAN_UPDATE), Values.of(KEY_STORE, KEY_ST_UPDATE));
        operations.add(trustManagerAddress(TRU_MAN_DELETE), Values.of(KEY_STORE, KEY_ST_UPDATE));

        Values trustParams = Values.of(KEY_STORE, KEY_ST_UPDATE).andObject(CERTIFICATE_REVOCATION_LIST,
                        Values.of(PATH, "${jboss.server.config.dir}/logging.properties"));
        operations.add(trustManagerAddress(TRU_MAN_CRL_CRT), Values.of(KEY_STORE, KEY_ST_UPDATE));
        operations.add(trustManagerAddress(TRU_MAN_CRL_UPD), trustParams);
        operations.add(trustManagerAddress(TRU_MAN_CRL_DEL), trustParams);

        operations.add(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE), Values.of(CONSTANT, ANY_STRING));

        operations.add(authenticationConfigurationAddress(AUT_CF_UPDATE));
        operations.add(authenticationConfigurationAddress(AUT_CF_DELETE));

        Values autParams = Values.ofObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, ANY_STRING));
        operations.add(authenticationConfigurationAddress(AUT_CF_CR_CRT));
        operations.add(authenticationConfigurationAddress(AUT_CF_CR_UPD), autParams);
        operations.add(authenticationConfigurationAddress(AUT_CF_CR_DEL), autParams);

        operations.add(authenticationContextAddress(AUT_CT_DELETE));
        operations.add(authenticationContextAddress(AUT_CT_UPDATE));
        ModelNode matchRuleUpdate = new ModelNode();
        matchRuleUpdate.get(MATCH_ABSTRACT_TYPE).set(AUT_CT_MR_UPDATE);
        ModelNode matchRuleDelete = new ModelNode();
        matchRuleDelete.get(MATCH_ABSTRACT_TYPE).set(AUT_CT_MR_DELETE);
        operations.add(authenticationContextAddress(AUT_CT_UPDATE2),
                Values.ofList(MATCH_RULES, matchRuleUpdate, matchRuleDelete));

        operations.add(fileAuditLogAddress(FILE_LOG_DELETE), Values.of(PATH, ANY_STRING));
        operations.add(fileAuditLogAddress(FILE_LOG_UPDATE), Values.of(PATH, ANY_STRING));
        operations.add(fileAuditLogAddress(FILE_LOG_TRY_UPDATE), Values.of(PATH, ANY_STRING));

        Values params = Values.of(PATH, ANY_STRING).and(SUFFIX, SUFFIX_LOG);
        operations.add(periodicRotatingFileAuditLogAddress(PER_LOG_DELETE), params);
        operations.add(periodicRotatingFileAuditLogAddress(PER_LOG_UPDATE), params);
        operations.add(periodicRotatingFileAuditLogAddress(PER_LOG_TRY_UPDATE), params);

        operations.add(sizeRotatingFileAuditLogAddress(SIZ_LOG_DELETE), Values.of(PATH, ANY_STRING));
        operations.add(sizeRotatingFileAuditLogAddress(SIZ_LOG_UPDATE), Values.of(PATH, ANY_STRING));

        Values syslogParams = Values.of(HOSTNAME, ANY_STRING).and(PORT, Random.number()).and(SERVER_ADDRESS, LOCALHOST);
        operations.add(syslogAuditLogAddress(SYS_LOG_UPDATE), syslogParams);
        operations.add(syslogAuditLogAddress(SYS_LOG_TRY_UPDATE), syslogParams);
        operations.add(syslogAuditLogAddress(SYS_LOG_DELETE), syslogParams);

        Values secEventParams = Values.ofList(SECURITY_EVENT_LISTENERS, SYS_LOG_UPDATE, SIZ_LOG_UPDATE);
        operations.add(aggregateSecurityEventListenerAddress(AGG_SEC_UPDATE), secEventParams);
        operations.add(aggregateSecurityEventListenerAddress(AGG_SEC_DELETE), secEventParams);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            // Stores
            operations.removeIfExists(credentialStoreAddress(CRED_ST_DELETE));
            operations.removeIfExists(credentialStoreAddress(CRED_ST_UPDATE));
            operations.removeIfExists(credentialStoreAddress(CRED_ST_CREATE));
            operations.removeIfExists(filteringKeyStoreAddress(FILT_ST_DELETE));
            operations.removeIfExists(filteringKeyStoreAddress(FILT_ST_UPDATE));
            operations.removeIfExists(filteringKeyStoreAddress(FILT_ST_CREATE));
            operations.removeIfExists(keyStoreAddress(KEY_ST_CREATE));
            operations.removeIfExists(keyStoreAddress(KEY_ST_DELETE));
            operations.removeIfExists(ldapKeyStoreAddress(LDAPKEY_ST_DELETE));
            operations.removeIfExists(ldapKeyStoreAddress(LDAPKEY_ST_UPDATE));
            operations.removeIfExists(ldapKeyStoreAddress(LDAPKEY_ST_TEMP1_UPDATE));
            operations.removeIfExists(ldapKeyStoreAddress(LDAPKEY_ST_TEMP2_DELETE));
            operations.removeIfExists(ldapKeyStoreAddress(LDAPKEY_ST_TEMP3_ADD));
            operations.removeIfExists(ldapKeyStoreAddress(LDAPKEY_ST_TEMP4_TRY_ADD));
            operations.removeIfExists(ldapKeyStoreAddress(LDAPKEY_ST_CREATE));
            operations.removeIfExists(dirContextAddress(DIR_UPDATE));
            operations.removeIfExists(dirContextAddress(DIR_DELETE));
            operations.removeIfExists(dirContextAddress(DIR_CREATE));
            operations.removeIfExists(dirContextAddress(DIR_CR_CRT));
            operations.removeIfExists(dirContextAddress(DIR_CR_UPD));
            operations.removeIfExists(dirContextAddress(DIR_CR_DEL));
            // SSL
            operations.removeIfExists(aggregateProvidersAddress(AGG_PRV_DELETE));
            operations.removeIfExists(aggregateProvidersAddress(AGG_PRV_UPDATE));
            operations.removeIfExists(aggregateProvidersAddress(AGG_PRV_CREATE));
            operations.removeIfExists(clientSslContextAddress(CLI_SSL_UPDATE));
            operations.removeIfExists(clientSslContextAddress(CLI_SSL_CREATE));
            operations.removeIfExists(clientSslContextAddress(CLI_SSL_DELETE));
            // removeIfExists() the server-ssl-context before removing key-manager
            operations.removeIfExists(serverSslContextAddress(SRV_SSL_UPDATE));
            operations.removeIfExists(serverSslContextAddress(SRV_SSL_CREATE));
            operations.removeIfExists(serverSslContextAddress(SRV_SSL_DELETE));
            operations.removeIfExists(keyManagerAddress(KEY_MAN_CREATE));
            operations.removeIfExists(keyManagerAddress(KEY_MAN_UPDATE));
            operations.removeIfExists(keyManagerAddress(KEY_MAN_TRY_UPDATE));
            operations.removeIfExists(keyManagerAddress(KEY_MAN_DELETE));
            operations.removeIfExists(securityDomainAddress(SEC_DOM_UPDATE));
            operations.removeIfExists(securityDomainAddress(SEC_DOM_UPDATE2));
            operations.removeIfExists(securityDomainAddress(SEC_DOM_UPDATE3));
            operations.removeIfExists(securityDomainAddress(SEC_DOM_DELETE));
            operations.removeIfExists(securityDomainAddress(SEC_DOM_CREATE));
            operations.removeIfExists(trustManagerAddress(TRU_MAN_UPDATE));
            operations.removeIfExists(trustManagerAddress(TRU_MAN_CREATE));
            operations.removeIfExists(trustManagerAddress(TRU_MAN_DELETE));
            operations.removeIfExists(trustManagerAddress(TRU_MAN_CRL_CRT));
            operations.removeIfExists(trustManagerAddress(TRU_MAN_CRL_UPD));
            operations.removeIfExists(trustManagerAddress(TRU_MAN_CRL_DEL));
            // key-store is a dependency on key-manager and trust-manager, removeIfExists() it after key-manager and trust-manager
            operations.removeIfExists(keyStoreAddress(KEY_ST_UPDATE));
            operations.removeIfExists(keyStoreAddress(KEY_ST_CR_UPDATE));
            operations.removeIfExists(providerLoaderAddress(PROV_LOAD_UPDATE));
            operations.removeIfExists(providerLoaderAddress(PROV_LOAD_UPDATE2));
            operations.removeIfExists(providerLoaderAddress(PROV_LOAD_UPDATE3));
            operations.removeIfExists(providerLoaderAddress(PROV_LOAD_CREATE));
            operations.removeIfExists(providerLoaderAddress(PROV_LOAD_DELETE));
            operations.removeIfExists(filesystemRealmAddress(FILESYS_RLM_UPDATE));
            operations.removeIfExists(filesystemRealmAddress(FILESYS_RLM_CREATE));
            operations.removeIfExists(constantPrincipalTransformerAddress(CONS_PRI_TRANS_UPDATE));
            operations.removeIfExists(authenticationContextAddress(AUT_CT_UPDATE));
            operations.removeIfExists(authenticationContextAddress(AUT_CT_UPDATE2));
            operations.removeIfExists(authenticationContextAddress(AUT_CT_DELETE));
            operations.removeIfExists(authenticationContextAddress(AUT_CT_CREATE));
            operations.removeIfExists(authenticationConfigurationAddress(AUT_CF_CREATE));
            operations.removeIfExists(authenticationConfigurationAddress(AUT_CF_UPDATE));
            operations.removeIfExists(authenticationConfigurationAddress(AUT_CF_DELETE));
            operations.removeIfExists(authenticationConfigurationAddress(AUT_CF_CR_CRT));
            operations.removeIfExists(authenticationConfigurationAddress(AUT_CF_CR_UPD));
            operations.removeIfExists(authenticationConfigurationAddress(AUT_CF_CR_DEL));
            operations.removeIfExists(fileAuditLogAddress(FILE_LOG_DELETE));
            operations.removeIfExists(fileAuditLogAddress(FILE_LOG_UPDATE));
            operations.removeIfExists(fileAuditLogAddress(FILE_LOG_TRY_UPDATE));
            operations.removeIfExists(fileAuditLogAddress(FILE_LOG_CREATE));
            // removeIfExists() the aggregate-security-event-listener first, as they require size audit log and syslog
            operations.removeIfExists(aggregateSecurityEventListenerAddress(AGG_SEC_UPDATE));
            operations.removeIfExists(aggregateSecurityEventListenerAddress(AGG_SEC_CREATE));
            operations.removeIfExists(aggregateSecurityEventListenerAddress(AGG_SEC_DELETE));
            operations.removeIfExists(periodicRotatingFileAuditLogAddress(PER_LOG_UPDATE));
            operations.removeIfExists(periodicRotatingFileAuditLogAddress(PER_LOG_TRY_UPDATE));
            operations.removeIfExists(periodicRotatingFileAuditLogAddress(PER_LOG_DELETE));
            operations.removeIfExists(periodicRotatingFileAuditLogAddress(PER_LOG_CREATE));
            operations.removeIfExists(sizeRotatingFileAuditLogAddress(SIZ_LOG_DELETE));
            operations.removeIfExists(sizeRotatingFileAuditLogAddress(SIZ_LOG_UPDATE));
            operations.removeIfExists(sizeRotatingFileAuditLogAddress(SIZ_LOG_CREATE));
            operations.removeIfExists(syslogAuditLogAddress(SYS_LOG_DELETE));
            operations.removeIfExists(syslogAuditLogAddress(SYS_LOG_CREATE));
            operations.removeIfExists(syslogAuditLogAddress(SYS_LOG_UPDATE));
            operations.removeIfExists(syslogAuditLogAddress(SYS_LOG_TRY_UPDATE));
            operations.removeIfExists(policyAddress(POL_CREATE));
        } finally {
            client.close();
        }
    }

    @Page
    protected ElytronOtherSettingsPage page;
    @Inject
    protected Console console;
    @Inject
    protected CrudOperations crud;

    public AbstractOtherSettingsTest() {
        super();
    }

    @Before
    public void setUp() throws Exception {
        page.navigate();
    }

}
