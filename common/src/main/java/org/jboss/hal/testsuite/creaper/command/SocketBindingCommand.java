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
package org.jboss.hal.testsuite.creaper.command;

import org.jboss.hal.resources.Ids;
import org.wildfly.extras.creaper.core.online.OnlineCommand;
import org.wildfly.extras.creaper.core.online.OnlineCommandContext;

public abstract class SocketBindingCommand implements OnlineCommand {

    public static String refName(String name) {
        return Ids.build(name + "ref");
    }

    final String name;
    private final String socketBindingGroup;

    SocketBindingCommand(String name) {
        this(name, null);
    }

    SocketBindingCommand(String name, String socketBindingGroup) {
        this.name = name;
        this.socketBindingGroup = socketBindingGroup;
    }

    String resolveSocketBindingGroup(OnlineCommandContext ctx) {
        String socketBindingGroup = this.socketBindingGroup;
        if (socketBindingGroup == null) {
            socketBindingGroup = ctx.client.options().isDomain ? "full-sockets" : "standard-sockets";
        }
        return socketBindingGroup;
    }
}
