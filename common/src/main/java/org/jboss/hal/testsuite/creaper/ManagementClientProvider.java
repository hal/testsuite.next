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
package org.jboss.hal.testsuite.creaper;

import org.jboss.hal.testsuite.util.ConfigUtils;
import org.wildfly.extras.creaper.core.ManagementClient;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.OnlineOptions;

/** Provider for Creaper's OnlineManagementClient */
public class ManagementClientProvider {

    /**
     * Creates default OnlineManagementClient for domain for the specified profile
     *
     * @param profile - profile managed by domain online management client
     *
     * @return Initialized domain OnlineManagementClient for specified profile, don't forget to close it
     */
    public static OnlineManagementClient withProfile(String profile) {
        return ManagementClient.onlineLazy(OnlineOptions.domain()
                .forHost(ConfigUtils.getDefaultHost()).forProfile(profile).build()
                .hostAndPort(System.getProperty("as.managementAddress", "localhost"),
                        Integer.parseInt(System.getProperty("as.managementPort", "9990")))
                .build());
    }

    /**
     * Creates OnlineManagementClient based on ConfigUtils (automatically decides whether it is client for domain or
     * standalone server
     *
     * @return Initialized OnlineManagementClient, don't forget to close it
     */
    public static OnlineManagementClient createOnlineManagementClient() {
        OnlineManagementClient managementClient;
        if (ConfigUtils.isDomain()) {
            String profile = ConfigUtils.getDefaultProfile();
            String host = ConfigUtils.getDefaultHost();
            managementClient = ManagementClient.onlineLazy(OnlineOptions.domain()
                    .forHost(host).forProfile(profile).build()
                    .hostAndPort(System.getProperty("as.managementAddress", "localhost"),
                            Integer.parseInt(System.getProperty("as.managementPort", "9990")))
                    .build());
        } else {
            managementClient = ManagementClient.onlineLazy(OnlineOptions.standalone().
                    hostAndPort(System.getProperty("as.managementAddress", "localhost"),
                            Integer.parseInt(System.getProperty("as.managementPort", "9990")))
                    .build());
        }

        return managementClient;
    }

    private ManagementClientProvider() {
    }
}
