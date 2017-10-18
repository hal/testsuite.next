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
package org.jboss.hal.testsuite.arquillian;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.graphene.location.decider.HTTPLocationDecider;
import org.jboss.arquillian.graphene.spi.location.Scheme;
import org.jboss.hal.testsuite.util.ConfigUtils;

import static org.jboss.hal.testsuite.arquillian.HalLocationDecider.Mode.DOMAIN;

/**
 * Appends the default profile to the location, if running in domain mode. Use this scheme for configuration pages:
 * <pre>
 * {@code @}Location(scheme = HalScheme.class, value = "#batch-jberet-configuration${profile}")
 * public class BatchPage {
 *     ...
 * }
 * </pre>
 */
public class HalLocationDecider extends HTTPLocationDecider {

    private final Scheme scheme;
    private final Map<Token, String> variables;

    public HalLocationDecider() {
        scheme = new HalScheme();
        variables = new HashMap<>();
        variables.put(Token.PROFILE, ConfigUtils.getDefaultProfile());
        variables.put(Token.HOST, ConfigUtils.getDefaultHost());
        variables.put(Token.SERVER_CONFIG, ConfigUtils.getDefaultServer());
        variables.put(Token.SERVER, ConfigUtils.getDefaultServer());
    }

    @Override
    public Scheme canDecide() {
        return scheme;
    }

    @Override
    public String decide(String location) {
        String url = super.decide(location);
        for (Map.Entry<Token, String> entry : variables.entrySet()) {
            Token token = entry.getKey();
            Pattern pattern = Pattern.compile(token.pattern());
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                if (token.matchesOperationMode()) {
                    String resolved = entry.getValue();
                    url = matcher.replaceAll(token.eval(resolved));
                } else {
                    url = matcher.replaceAll("");
                }
            }
        }
        return url;
    }


    enum Mode {
        STANDALONE, DOMAIN
    }


    enum Token {

        PROFILE("profile", DOMAIN),
        HOST("host", DOMAIN),
        SERVER_CONFIG("server-config", DOMAIN),
        SERVER("server", DOMAIN);

        private final String variable;
        private final Mode mode;

        Token(String variable, Mode mode) {
            this.variable = variable;
            this.mode = mode;
        }

        String pattern() {
            return "\\$\\{" + variable + "\\}";
        }

        boolean matchesOperationMode() {
            return mode == DOMAIN && ConfigUtils.isDomain() ||
                    mode == Mode.STANDALONE && !ConfigUtils.isDomain();
        }

        String eval(String value) {
            return ";" + variable + "=" + value;
        }
    }
}

