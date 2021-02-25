package org.jboss.hal.testsuite.tooling.ssl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jboss.hal.testsuite.Random;
import org.junit.Assert;
import org.wildfly.extras.creaper.core.ManagementClient;
import org.wildfly.extras.creaper.core.online.Constants;
import org.wildfly.extras.creaper.core.online.ManagementProtocol;
import org.wildfly.extras.creaper.core.online.ModelNodeResult;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.OnlineOptions;
import org.wildfly.extras.creaper.core.online.SslOptions;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ALGORITHM;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ALIAS;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CLEAR_TEXT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CREDENTIAL_REFERENCE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.DISTINGUISHED_NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.EXPORT_CERTIFICATE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.GENERATE_KEY_PAIR;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PATH;
import static org.jboss.hal.dmr.ModelDescriptionConstants.STORE;
import static org.jboss.hal.dmr.ModelDescriptionConstants.TYPE;
import static org.jboss.hal.testsuite.fixtures.ElytronFixtures.keyStoreAddress;
import static org.jboss.hal.testsuite.fixtures.ManagementSslFixtures.HAL;

public class SslOperations {

    private OnlineManagementClient client;
    private Operations ops;
    private List<String> filesToBeCleanedUp;

    public SslOperations(OnlineManagementClient client) {
        this.client = client;
        this.ops = new Operations(client);
    }

    public SslOperations filesToBeCleanedUp(List<String> filesToBeCleanedUp) {
        this.filesToBeCleanedUp = filesToBeCleanedUp;
        return this;
    }

    /**
     * Creates standalone {@link OnlineManagementClient} for comunication with SSL-secured management
     */
    public static OnlineManagementClient createSslClient(SslOptions sslOptions) throws IOException {
        return ManagementClient.onlineLazy(OnlineOptions.standalone().hostAndPort("localhost", 9993).ssl(sslOptions)
                .protocol(ManagementProtocol.HTTPS_REMOTING).build());
    }

    /**
     * Verifies that alias contains distinguished name with organization 'HAL'
     */
    public SslOperations assertHALisDNOrganizationUnitOf(String target, ModelNodeResult aliasResult, KeyEntryType keyEntryType) {
        String[] issuerArray = keyEntryType.getDNArray(aliasResult.value(), target);
        Optional<String> org = Arrays.stream(issuerArray).filter(e -> e.startsWith("O=")).findFirst();
        Assert.assertTrue("Organization unit missing for " + target + " DN.", org.isPresent());
        Assert.assertEquals("Unexpected value of " + target + " DN organization unit.", HAL, org.get().split("=")[1]);
        return this;
    }

    /**
     * Creates key store
     */
    public Address createKeyStore(String ksName, String ksPath, String ksPass) throws IOException {
        filesToBeCleanedUp.add(ksPath);
        Address ksAddress = keyStoreAddress(ksName);
        ops.add(ksAddress, Values.of(PATH, ksPath).andObject(CREDENTIAL_REFERENCE, Values.of(CLEAR_TEXT, ksPass))
                .and(TYPE, "JKS")).assertSuccess();
        return ksAddress;
    }

    /**
     * Creates key store with self-signed certificate
     */
    public Address createKeyStoreWithCertificate(String ksName, String ksPath, String ksPass, String aliasName) throws IOException {
        Address ksAddress = createKeyStore(ksName, ksPath, ksPass);
        ops.invoke(GENERATE_KEY_PAIR, ksAddress, Values.of(ALIAS, aliasName)
                .and(DISTINGUISHED_NAME, "c=US,CN=Wildfly,O=" + HAL + ",OU=Eng,ST=AZ").and(ALGORITHM, "RSA")).assertSuccess();
        ops.invoke(STORE, ksAddress).assertSuccess();
        return ksAddress;
    }

    /**
     * Exports certificate from {@code keyStoreAddress}
     */
    public SslOperations createCertificate(String certPath, String certAlias, Address keyStoreAddress) throws IOException {
        ops.invoke(EXPORT_CERTIFICATE, keyStoreAddress, Values.of(PATH, certPath).and(ALIAS, certAlias)).assertSuccess();
        filesToBeCleanedUp.add(certPath);
        return this;
    }

    /**
     * Creates self-signed certificate
     */
    public SslOperations createCertificate(String certPath, String certAlias) throws IOException {
        Address ksAddress = createKeyStoreWithCertificate(Random.name(), Random.name(), Random.name(), certAlias);
        return createCertificate(certPath, certAlias, ksAddress);
    }

    /**
     * @return default key stores directory
     */
    public File getKeyStoresDirectory() throws IOException {
        String jbossDir = ops.readAttribute(Address.of(PATH, "jboss.home.dir"), PATH).stringValue();
        return new File(jbossDir, "bin");
    }

    /**
     * Intended to be used as a validation that reload is finished.
     */
    public void waitUntilServerIsRunning() throws IOException, InterruptedException, TimeoutException {

        Thread.sleep(500);
        int timeoutInSeconds = 60;
        client.reconnect(timeoutInSeconds);

        long endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutInSeconds);
        while (System.currentTimeMillis() < endTime) {
            try {
                if (isServerRunning()) {
                    break;
                }
            } catch (Throwable ignored) {
                // server is probably down, will retry
            }

            Thread.sleep(200);
        }

        boolean running = false;
        try {
            running = isServerRunning();
        } catch (Throwable ignored) {
            // server probably down
        }
        if (!running) {
            throw new TimeoutException("Waiting for server timed out");
        }
    }

    private boolean isServerRunning() throws IOException {
        ModelNodeResult result = ops.readAttribute(Address.root(), Constants.SERVER_STATE);
        result.assertDefinedValue();
        String serverState = result.stringValue();
        return Constants.CONTROLLER_PROCESS_STATE_RUNNING.equals(serverState)
                || Constants.CONTROLLER_PROCESS_STATE_RELOAD_REQUIRED.equals(serverState)
                || Constants.CONTROLLER_PROCESS_STATE_RESTART_REQUIRED.equals(serverState);
    }

}
