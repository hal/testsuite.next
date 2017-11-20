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
package org.jboss.hal.testsuite;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.text.RandomStringGenerator;

public class Random {

    private static final int LENGTH = 12;
    private static final String JNDI_PREFIX = "java:jboss/";
    private static final RandomStringGenerator GENERATOR = new RandomStringGenerator.Builder().withinRange('a', 'z')
            .build();

    /** Returns a random name useable for resource names. */
    public static String name() {
        return GENERATOR.generate(LENGTH);
    }

    /** Returns a JNDI name starting with "java:jboss/" followed by a random name. */
    public static String jndiName() {
        return jndiName(name());
    }

    /** Returns a JNDI name starting with "java:jboss/" followed by the specified name. */
    public static String jndiName(String name) {
        return JNDI_PREFIX + name;
    }

    /** Returns a random integer between 0 and 99 */
    public static int number() {
        return RandomUtils.nextInt(0, 100);
    }

    /**
     * Returns a random integer within the specified range.
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive   the upper bound (not included)
     */
    public static int number(int startInclusive, int endExclusive) {
        return RandomUtils.nextInt(startInclusive, endExclusive);
    }

    public static long number(long startInclusive, long endExclusive) {
        return RandomUtils.nextLong(startInclusive, endExclusive);
    }

    private Random() {
    }
}
