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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Created by jcechace on 15/02/14. */
public class PropUtils {

    private static final Properties props = new Properties();

    static {
        try {
            try (InputStream in = PropUtils.class.getResourceAsStream("/label.properties")) {
                props.load(in);
                props.putAll(System.getProperties());
                String pass = System.getenv("RHACCESS_PASSWORD");
                if (pass != null) {
                    props.put("rhaccess.password", pass);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load label.properties");
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    private PropUtils() {
    }
}
