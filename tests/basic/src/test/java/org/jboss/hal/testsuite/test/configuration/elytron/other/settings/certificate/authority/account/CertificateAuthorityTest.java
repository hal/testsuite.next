package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.certificate.authority.account;

import java.io.IOException;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.dmr.ModelDescriptionConstants;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.test.configuration.elytron.ElytronFixtures;
import org.jboss.hal.testsuite.test.configuration.elytron.other.settings.AbstractOtherSettingsTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.operations.OperationException;
import org.wildfly.extras.creaper.core.online.operations.Values;

@RunWith(Arquillian.class)
public class CertificateAuthorityTest extends AbstractOtherSettingsTest {

    private static final String CERTIFICATE_AUTHORITY_CREATE =
        "certificate-authority-to-be-created-" + Random.name();

    private static final String CERTIFICATE_AUTHORITY_UPDATE =
        "certificate-authority-to-be-updated-" + Random.name();

    private static final String CERTIFICATE_AUTHORITY_DELETE =
        "certificate-authority-to-be-deleted-" + Random.name();

    private static final String LOCALHOST = "http://localhost";


    @BeforeClass
    public static void initResources() throws IOException {
        operations.add(ElytronFixtures.certificateAuthorityAddress(CERTIFICATE_AUTHORITY_UPDATE), Values.of(ModelDescriptionConstants.URL, LOCALHOST));
        operations.add(ElytronFixtures.certificateAuthorityAddress(CERTIFICATE_AUTHORITY_DELETE), Values.of(ModelDescriptionConstants.URL, LOCALHOST));
    }

    @AfterClass
    public static void cleanUp() throws IOException, OperationException {
        operations.removeIfExists(
            ElytronFixtures.certificateAuthorityAddress(CERTIFICATE_AUTHORITY_CREATE));
        operations.removeIfExists(
            ElytronFixtures.certificateAuthorityAddress(CERTIFICATE_AUTHORITY_UPDATE));
        operations.removeIfExists(
            ElytronFixtures.certificateAuthorityAddress(CERTIFICATE_AUTHORITY_DELETE));
    }

    @Before
    public void navigateToCertificateAuthority() {
        console.verticalNavigation()
            .selectSecondary(ElytronFixtures.OTHER_ITEM, ElytronFixtures.CERTIFICATE_AUTHORITY_ITEM);
    }

    @Test
    public void create() throws Exception {
        crud.create(ElytronFixtures.certificateAuthorityAddress(CERTIFICATE_AUTHORITY_CREATE),
            page.getCertificateAuthorityTable(), formFragment -> {
                formFragment.text("name", CERTIFICATE_AUTHORITY_CREATE);
                formFragment.text(ModelDescriptionConstants.URL, LOCALHOST);
            });
    }

    @Test
    public void delete() throws Exception {
        crud.delete(ElytronFixtures.certificateAuthorityAddress(CERTIFICATE_AUTHORITY_DELETE),
            page.getCertificateAuthorityTable(), CERTIFICATE_AUTHORITY_DELETE);
    }

    @Test
    public void editUrl() throws Exception {
        page.getCertificateAuthorityTable().select(CERTIFICATE_AUTHORITY_UPDATE);
        crud.update(ElytronFixtures.certificateAuthorityAddress(CERTIFICATE_AUTHORITY_UPDATE),
            page.getCertificateAuthorityForm(), ModelDescriptionConstants.URL, "https://example.com");
    }


    @Test
    public void editStagingUrl() throws Exception {
        page.getCertificateAuthorityTable().select(CERTIFICATE_AUTHORITY_UPDATE);
        crud.update(ElytronFixtures.certificateAuthorityAddress(CERTIFICATE_AUTHORITY_UPDATE),
            page.getCertificateAuthorityForm(), "staging-url", "https://example.com");
    }


}
