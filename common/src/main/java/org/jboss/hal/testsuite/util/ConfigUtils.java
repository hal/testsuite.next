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
package org.jboss.hal.testsuite.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jcechace on 21/02/14.
 */
public class ConfigUtils {

    private static final Properties config = new Properties();

    static {
        // Load default configuration
        try {
            try (InputStream defaultConfig = PropUtils.class.getResourceAsStream("/suite.properties")) {
                if (defaultConfig != null) {
                    config.load(defaultConfig);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load suite configuration");
        }

        // Override by custom configuration
        String customConfigLocation = System.getProperty("suite.config.location");
        if (customConfigLocation != null) {
            try {
                try (InputStream customConfig = new FileInputStream(customConfigLocation)) {
                    config.load(customConfig);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to load custom suite configuration");
            }
        }

        // Override by properties from command line
        config.putAll(System.getProperties());
    }


    public static String get(String key) {
        return config.getProperty(key);
    }

    public static String get(String key, String defval) {
        return config.getProperty(key, defval);
    }

    public static boolean isDomain() {
        return get("suite.mode", "standalone").toLowerCase().equals("domain");
    }

    public static String getDefaultProfile() {
        return get("suite.domain.default.profile", "full");
    }

    public static String getDefaultHost() {
        return get("suite.domain.default.host", "master");
    }

    public static String getDefaultServer() {
        return get("suite.domain.default.server", "server-one");
    }

    private ConfigUtils() {
    }
}
