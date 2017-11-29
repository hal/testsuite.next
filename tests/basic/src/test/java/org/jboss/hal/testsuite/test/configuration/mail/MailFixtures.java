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
package org.jboss.hal.testsuite.test.configuration.mail;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.MAIL;
import static org.jboss.hal.dmr.ModelDescriptionConstants.MAIL_SESSION;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SERVER;

public interface MailFixtures {

    String MAIL_SMTP = "mail-smtp";
    String SECRET = "secret";

    Address SUBSYSTEM_ADDRESS = Address.subsystem(MAIL);

    // ------------------------------------------------------ session

    String SESSION_CREATE = Ids.build("ms", "create", Random.name());
    String SESSION_READ = Ids.build("ms", "read", Random.name());
    String SESSION_UPDATE = Ids.build("ms", "update", Random.name());
    String SESSION_DELETE = Ids.build("ms", "delete", Random.name());

    static Address sessionAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(MAIL_SESSION, name);
    }

    // ------------------------------------------------------ server

    static Address serverAddress(String session, String type) {
        return sessionAddress(session).and(SERVER, type);
    }
}
