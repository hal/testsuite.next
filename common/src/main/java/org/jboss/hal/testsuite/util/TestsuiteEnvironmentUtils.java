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
        boolean found;
        try (InputStream inputStream = TestsuiteEnvironmentUtils.class.getResourceAsStream("/testsuite.properties")) {
            properties.load(inputStream);
            found = true;
        } catch (IOException e) {
            String customPropertyLocation = System.getProperty("testsuite.config.location");
            if (customPropertyLocation != null) {
                try {
                    try (InputStream customConfig = new FileInputStream(customPropertyLocation)) {
                        properties.load(customConfig);
                        found = true;
                    }
                } catch (IOException e2) {
                    throw new RuntimeException("Unable to load testsuite.properties", e);
                }
            } else {
                String jbossHome = System.getProperty(JBOSS_HOME);
                if (jbossHome != null) {
                    properties.put(JBOSS_HOME, jbossHome);
                } else {
                    throw new RuntimeException("Unable to load JBOSS_HOME parameter from either testsuite.properties or as a parameter. Specify as a command line parameter.");
                }
            }
        }
    }

    public static String getJbossHome() {
        return properties.getProperty(JBOSS_HOME);
    }
}
