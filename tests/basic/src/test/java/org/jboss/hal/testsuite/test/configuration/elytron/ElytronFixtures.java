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
package org.jboss.hal.testsuite.test.configuration.elytron;

import org.jboss.hal.resources.Ids;
import org.jboss.hal.testsuite.Random;
import org.wildfly.extras.creaper.core.online.operations.Address;

import static org.jboss.hal.dmr.ModelDescriptionConstants.AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.ELYTRON;
import static org.jboss.hal.dmr.ModelDescriptionConstants.HTTP_AUTHENTICATION_FACTORY;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PROVIDER_HTTP_SERVER_MECHANISM_FACTORY;
import static org.jboss.hal.resources.Ids.ELYTRON_AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY;
import static org.jboss.hal.resources.Ids.ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY;
import static org.jboss.hal.resources.Ids.ITEM;

public interface ElytronFixtures {

    String INITIAL_PROVIDERS = "initial-providers";
    String HTTP_SERVER_MECH_FACTORIES = "http-server-mechanism-factories";
    String HTTP_SERVER_MECH_FACTORY = "http-server-mechanism-factory";
    String GLOBAL = "global";
    String HTTP_FACTORIES_ITEM = "http-factories-item";
    String AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(ELYTRON_AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY, ITEM);
    String CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY_ITEM = Ids.build(ELYTRON_CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY, ITEM);

    Address SUBSYSTEM_ADDRESS = Address.subsystem(ELYTRON);

    // -------- http factory
    // -------------- aggregate-http-server-mechanism-factory

    String AGG_HTTP_CREATE = Ids.build("agg-http", "create", Random.name());
    String AGG_HTTP_UPDATE = Ids.build("agg-http", "update", Random.name());
    String AGG_HTTP_DELETE = Ids.build("agg-http", "delete", Random.name());

    static Address aggregateHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(AGGREGATE_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- configurable-http-server-mechanism-factory

    String CONF_HTTP_CREATE = Ids.build("conf-http", "create", Random.name());
    String CONF_HTTP_UPDATE = Ids.build("conf-http", "update", Random.name());
    String CONF_HTTP_DELETE = Ids.build("conf-http", "delete", Random.name());

    static Address configurableHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(CONFIGURABLE_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- http-authentication-factory

    String HTTP_AUTH_CREATE = Ids.build("http-auth", "create", Random.name());
    String HTTP_AUTH_UPDATE = Ids.build("http-auth", "update", Random.name());
    String HTTP_AUTH_DELETE = Ids.build("http-auth", "delete", Random.name());

    static Address httpAuthenticationFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(HTTP_AUTHENTICATION_FACTORY, name);
    }

    // -------------- provider-http-server-mechanism-factory

    String PROV_HTTP_CREATE = Ids.build("prov-http", "create", Random.name());
    String PROV_HTTP_UPDATE = Ids.build("prov-http", "update", Random.name());
    String PROV_HTTP_UPDATE2 = Ids.build("prov-http2", "update", Random.name());
    String PROV_HTTP_UPDATE3 = Ids.build("prov-http3", "update", Random.name());
    String PROV_HTTP_DELETE = Ids.build("prov-http", "delete", Random.name());

    static Address providerHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROVIDER_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

    // -------------- service-loader-http-server-mechanism-factory

    String SVC_LOAD_HTTP_CREATE = Ids.build("svc-loa-http", "create", Random.name());
    String SVC_LOAD_HTTP_UPDATE = Ids.build("svc-loa-http", "update", Random.name());
    String SVC_LOAD_HTTP_DELETE = Ids.build("svc-loa-http", "delete", Random.name());

    static Address serviceLoaderHttpServerMechanismFactoryAddress(String name) {
        return SUBSYSTEM_ADDRESS.and(PROVIDER_HTTP_SERVER_MECHANISM_FACTORY, name);
    }

}
