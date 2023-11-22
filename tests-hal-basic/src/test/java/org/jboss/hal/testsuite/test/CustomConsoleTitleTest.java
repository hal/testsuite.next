package org.jboss.hal.testsuite.test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Console;
import org.jboss.hal.testsuite.category.Domain;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.fragment.DialogFragment;
import org.jboss.hal.testsuite.fragment.FooterFragment;
import org.jboss.hal.testsuite.page.HomePage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.admin.Administration;


/**
 * Test case verifying users can change the console title to custom value.
 *
 * https://issues.jboss.org/browse/HAL-1578
 */
@RunWith(Arquillian.class)
public class CustomConsoleTitleTest {

    private static final String HAL_MANAGEMENT_CONSOLE = "HAL Management Console";
    private static final String HAL_TEST_SUITE = "HAL Test Suite";
    private static final String CONSOLE_DEFAULT_TITLE_TEMPLATE = "%s | Management Console";

    private static final String ORGANIZATION_PLACEHOLDER = "%o";
    private static final String NAME_PLACEHOLDER = "%n";

    private static final Address NAME_ATTRIBUTE_ADDRESS = Address.root();
    private static final String NAME_ATTRIBUTE_NAME = "name";

    private static final Address ORGANIZATION_ADDRESS = Address.root();
    private static final String ORGANIZATION_ATTRIBUTE_NAME_DOMAIN =  "domain-organization";
    private static final String ORGANIZATION_ATTRIBUTE_NAME_STANDALONE =  "organization";

    @Drone
    private static WebDriver browser;

    @Page
    private HomePage page;

    @Inject
    private Console console;

    @Before
    public void before() {
        page.navigate();
    }

    @After
    public void after() {
        //clear cookies including custom title
        browser.manage().deleteAllCookies();
        console.reload();
    }

    /**
     * Test default value - expected is "%n | Management Console"
     */
    @Test
    public void testDefaultValue() throws IOException {
        try (OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient()) {

            final Operations ops = new Operations(client);

            final ModelNodeResult result = ops.readAttribute(NAME_ATTRIBUTE_ADDRESS, NAME_ATTRIBUTE_NAME);
            result.assertSuccess();
            final String nameAttributeValue = result.stringValue();

            final String titleValue = String.format("%s | Management Console", nameAttributeValue);

            Assert.assertEquals(CONSOLE_DEFAULT_TITLE_TEMPLATE, titleValue, browser.getTitle());
        }
    }

    /**
     * Set custom title using "%o" placeholder when the {@code organization} attribute of {@code root} resource is
     * defined.
     */
    @Test
    public void testOrganizationPlaceholder() throws IOException, InterruptedException, TimeoutException {
        final String titlePrefix = RandomStringUtils.randomAlphanumeric(6);
        final String organizationValue = this.getClass().getCanonicalName() + " Bad Wolf, Inc.";

        try (OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient()) {

            final Operations ops = new Operations(client);
            final Administration adm = new Administration(client);

            final ModelNodeResult result = ops.readAttribute(ORGANIZATION_ADDRESS,
                    ORGANIZATION_ATTRIBUTE_NAME_STANDALONE);
            result.assertSuccess();
            final ModelNode organizationOriginalValue = result.value();

            ops.writeAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_STANDALONE, organizationValue)
                    .assertSuccess();

            adm.reloadIfRequired();

            try {
                setTitleAndReloadPage(titlePrefix + ORGANIZATION_PLACEHOLDER);

                Assert.assertEquals("Title was not set to expected value!", titlePrefix + organizationValue,
                        browser.getTitle());
            } finally {
                ops.writeAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_STANDALONE,
                        organizationOriginalValue).assertSuccess();
                adm.reloadIfRequired();
            }
        }
    }

    /**
     * Set custom title using "%o" placeholder when the {@code domain-organization} attribute of {@code root} resource
     * in domain mode is defined.
     */
    @Category(Domain.class)
    @Test
    public void testOrganizationPlaceholderDomain() throws IOException, InterruptedException, TimeoutException {
        final String titlePrefix = RandomStringUtils.randomAlphanumeric(6);
        final String organizationValue = this.getClass().getCanonicalName() + " Bad Wolf, Inc.";

        try (OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient()) {

            final Operations ops = new Operations(client);
            final Administration adm = new Administration(client);

            final ModelNodeResult result = ops.readAttribute(ORGANIZATION_ADDRESS,
                    ORGANIZATION_ATTRIBUTE_NAME_DOMAIN);
            result.assertSuccess();
            final ModelNode organizationOriginalValue = result.value();

            ops.writeAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_DOMAIN, organizationValue)
                    .assertSuccess();

            adm.reloadIfRequired();

            try {
                setTitleAndReloadPage(titlePrefix + ORGANIZATION_PLACEHOLDER);

                Assert.assertEquals("Title was not set to expected value!", titlePrefix + organizationValue,
                        browser.getTitle());
            } finally {
                ops.writeAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_DOMAIN,
                        organizationOriginalValue).assertSuccess();
                adm.reloadIfRequired();
            }
        }
    }

    /**
     * Set custom title using "%o" placeholder when the {@code organization} attribute of {@code root} resource is
     * undefined. This should cause web console into reverting to default title.
     */
    @Test
    public void testOrganizationPlaceholderUndefined() throws IOException, InterruptedException, TimeoutException {
        final String titlePrefix = RandomStringUtils.randomAlphanumeric(6);

        try (OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient()) {

            final Operations ops = new Operations(client);
            final Administration adm = new Administration(client);

            final ModelNodeResult result = ops.readAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_STANDALONE);
            result.assertSuccess();
            final ModelNode organizationOriginalValue = result.value();

            ops.undefineAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_STANDALONE).assertSuccess();

            adm.reloadIfRequired();

            try {
                setTitleAndReloadPage(titlePrefix + ORGANIZATION_PLACEHOLDER);

                Assert.assertEquals("Title was changed! It should remain at default value when placeholder is " +
                        "undefined!", HAL_MANAGEMENT_CONSOLE, browser.getTitle());
            } finally {
                ops.writeAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_STANDALONE, organizationOriginalValue).assertSuccess();
                adm.reloadIfRequired();
            }
        }
    }

    /**
     * Set custom title using "%o" placeholder when the {@code domain-organization} attribute of {@code root} resource
     * in domain mode is undefined. This should cause web console into reverting to default title.
     */
    @Category(Domain.class)
    @Test
    public void testOrganizationPlaceholderUndefinedDomain() throws IOException, InterruptedException, TimeoutException {
        final String titlePrefix = RandomStringUtils.randomAlphanumeric(6);

        try (OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient()) {

            final Operations ops = new Operations(client);
            final Administration adm = new Administration(client);

            final ModelNodeResult result = ops.readAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_DOMAIN);
            result.assertSuccess();
            final ModelNode organizationOriginalValue = result.value();

            ops.undefineAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_DOMAIN).assertSuccess();

            adm.reloadIfRequired();

            try {
                setTitleAndReloadPage(titlePrefix + ORGANIZATION_PLACEHOLDER);

                Assert.assertEquals("Title was changed! It should remain at default value when placeholder is " +
                        "undefined!", HAL_MANAGEMENT_CONSOLE, browser.getTitle());
            } finally {
                ops.writeAttribute(ORGANIZATION_ADDRESS, ORGANIZATION_ATTRIBUTE_NAME_DOMAIN, organizationOriginalValue).assertSuccess();
                adm.reloadIfRequired();
            }
        }
    }

    /**
     * Set custom title using "%o" placeholder when the {@code name} attribute of {@code root} resource is defined.
     * There is no need to test undefined state of this attribute because it always resolves to machine's hostname.
     */
    @Test
    public void testNamePlaceholder() throws IOException, InterruptedException, TimeoutException {
        final String titlePrefix = RandomStringUtils.randomAlphanumeric(6);

        final String nameValue = this.getClass().getCanonicalName() + "_long-name";

        try (OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient()) {

            final Operations ops = new Operations(client);
            final Administration adm = new Administration(client);

            final ModelNodeResult result = ops.readAttribute(NAME_ATTRIBUTE_ADDRESS, NAME_ATTRIBUTE_NAME);
            result.assertSuccess();
            final ModelNode nameOriginalValue = result.value();

            ops.writeAttribute(NAME_ATTRIBUTE_ADDRESS, NAME_ATTRIBUTE_NAME, nameValue).assertSuccess();

            adm.reloadIfRequired();

            try {
                setTitleAndReloadPage(titlePrefix + NAME_PLACEHOLDER);

                Assert.assertEquals("Title was not set!", titlePrefix + nameValue, browser.getTitle());
            } finally {
                ops.writeAttribute(NAME_ATTRIBUTE_ADDRESS, NAME_ATTRIBUTE_NAME, nameOriginalValue).assertSuccess();
                adm.reloadIfRequired();
            }
        }


    }

    /**
     * Test setting title to value consisting of various emoticons.
     */
    @Test
    public void testEmojiName() {
        final String testString = "❤️ \uD83D\uDC94 \uD83D\uDC8C \uD83D\uDC95 \uD83D\uDC9E \uD83D\uDC93 \uD83D\uDC97 " +
                "\uD83D\uDC96 \uD83D\uDC98 \uD83D\uDC9D \uD83D\uDC9F \uD83D\uDC9C \uD83D\uDC9B \uD83D\uDC9A \uD83D\uDC99";

        setTitleAndReloadPage(testString);

        Assert.assertEquals(testString, browser.getTitle());
    }

    /**
     * Test input using superstring
     * A super string recommended by VMware Inc. Globalization Team: can effectively cause rendering issues or
     * character-length issues to validate product globalization readiness.
     *
     * 表          CJK_UNIFIED_IDEOGRAPHS (U+8868)
     * ポ          KATAKANA LETTER PO (U+30DD)
     * あ          HIRAGANA LETTER A (U+3042)
     * A           LATIN CAPITAL LETTER A (U+0041)
     * 鷗          CJK_UNIFIED_IDEOGRAPHS (U+9DD7)
     * Œ           LATIN SMALL LIGATURE OE (U+0153)
     * é           LATIN SMALL LETTER E WITH ACUTE (U+00E9)
     * Ｂ           FULLWIDTH LATIN CAPITAL LETTER B (U+FF22)
     * 逍          CJK_UNIFIED_IDEOGRAPHS (U+900D)
     * Ü           LATIN SMALL LETTER U WITH DIAERESIS (U+00FC)
     * ß           LATIN SMALL LETTER SHARP S (U+00DF)
     * ª           FEMININE ORDINAL INDICATOR (U+00AA)
     * ą           LATIN SMALL LETTER A WITH OGONEK (U+0105)
     * ñ           LATIN SMALL LETTER N WITH TILDE (U+00F1)
     * 丂          CJK_UNIFIED_IDEOGRAPHS (U+4E02)
     * 㐀          CJK Ideograph Extension A, First (U+3400)
     * 𠀀          CJK Ideograph Extension B, First (U+20000)
     *
     */
    @Test
    public void testVMwareGlobalizationString() {
        final String testString = "表ポあA鷗ŒéＢ逍Üßªąñ丂㐀\uD840\uDC00";

        setTitleAndReloadPage(testString);

        Assert.assertEquals(testString, browser.getTitle());
    }

    /**
     * Test setting title to value consisting of unicode font characters.
     */
    @Test
    public void testUnicodeFontInput() {
        final String testString = "Ｔｈｅ \uD835\uDC2A\uD835\uDC2E\uD835\uDC22\uD835\uDC1C\uD835\uDC24 " +
                "\uD835\uDD87\uD835\uDD97\uD835\uDD94\uD835\uDD9C\uD835\uDD93 \uD835\uDC87\uD835\uDC90\uD835\uDC99";

        setTitleAndReloadPage(testString);

        Assert.assertEquals(testString, browser.getTitle());
    }

    /**
     * Test setting title to empty value.
     */
    @Test
    public void testEmptyInput() {
        final String testString = "FOOBAR";

        setTitleAndReloadPage(testString);

        Assert.assertEquals(testString, browser.getTitle());

        setTitleAndReloadPage("");

        // The default title is derived by browser from ts.html that is used in the test suite instead of index.html
        Assert.assertEquals(HAL_TEST_SUITE, browser.getTitle());
    }

    /**
     * Test setting title to very long value.
     */
    @Test
    public void testVeryLongInput() {
        final String testString = RandomStringUtils.randomAlphanumeric(1024);

        setTitleAndReloadPage(testString);

        Assert.assertEquals(testString, browser.getTitle());
    }

    /**
     * Open settings window, set title input to desired value, save and click yes in form informing about needed page
     * reload.
     * @param title title which should be set in form.
     */
    private void setTitleAndReloadPage(final String title) {
        final FooterFragment footer = console.footer();
        final DialogFragment settingsWindow = footer.openSettingsWindow();

        settingsWindow
                .getForm(Ids.SETTINGS_FORM)
                .text("title", title);

        settingsWindow.getPrimaryButton().click();

        console.dialog().getPrimaryButton().click();

        page.waitUntilHomePageIsLoaded();
    }


}
