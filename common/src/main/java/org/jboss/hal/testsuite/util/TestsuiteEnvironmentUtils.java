package org.jboss.hal.testsuite.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestsuiteEnvironmentUtils {

    private static final String JBOSS_HOME = "JBOSS_HOME";
    private static final Properties properties = new Properties();

    private TestsuiteEnvironmentUtils() {
    }

    static {
        InputStream inputStream = TestsuiteEnvironmentUtils.class.getResourceAsStream("/testsuite.properties");
        if (inputStream != null) {
            try (InputStream is = inputStream) {
                properties.load(is);
            } catch (IOException e) {
                throw new RuntimeException("Unable to load testsuite.properties", e);
            }
        } else {
            String customPropertyLocation = System.getProperty("testsuite.config.location");
            if (customPropertyLocation != null) {
                try (InputStream customConfig = new FileInputStream(customPropertyLocation)) {
                    properties.load(customConfig);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to load testsuite.properties from " + customPropertyLocation, e);
                }
            } else {
                String jbossHome = System.getProperty(JBOSS_HOME);
                if (jbossHome == null) {
                    jbossHome = System.getenv(JBOSS_HOME);
                }
                if (jbossHome != null) {
                    properties.put(JBOSS_HOME, jbossHome);
                } else {
                    throw new RuntimeException("Unable to load JBOSS_HOME from either testsuite.properties, "
                            + "system property, or environment variable. "
                            + "Specify via -DJBOSS_HOME=<path> or export JBOSS_HOME=<path>.");
                }
            }
        }
    }

    public static String getJbossHome() {
        return properties.getProperty(JBOSS_HOME);
    }
}
