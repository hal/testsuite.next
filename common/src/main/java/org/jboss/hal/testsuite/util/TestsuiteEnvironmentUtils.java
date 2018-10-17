package org.jboss.hal.testsuite.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestsuiteEnvironmentUtils {

    private static final String JBOSS_HOME = "JBOSS_HOME";
    private static final Properties properties = new Properties();

    static {
        try {
            try (InputStream inputStream = TestsuiteEnvironmentUtils.class.getResourceAsStream("/testsuite.properties")) {
                if (inputStream != null) {
                    properties.load(inputStream);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load testsuite.properties", e);
        }
        String customPropertyLocation = System.getProperty("testsuite.config.location");
        if (customPropertyLocation != null) {
            try {
                try (InputStream customConfig = new FileInputStream(customPropertyLocation)) {
                    properties.load(customConfig);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to load testsuite.properties", e);
            }
        }
        String jbossHome = System.getProperty(JBOSS_HOME);
        if (jbossHome != null) {
            properties.put(JBOSS_HOME, jbossHome);
        }
    }

    public static String getJbossHome() {
        return properties.getProperty(JBOSS_HOME);
    }
}
