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

import org.wildfly.extras.creaper.core.online.OnlineCommandContext;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING_GROUP;

public class RemoveLocalSocketBinding extends SocketBindingCommand {

    public RemoveLocalSocketBinding(String name) {
        super(name);
    }

    public RemoveLocalSocketBinding(String name, String socketBindingGroup) {
        super(name, socketBindingGroup);
    }

    @Override
    public void apply(OnlineCommandContext ctx) throws Exception {
        Operations ops = new Operations(ctx.client);
        String socketBindingGroup = resolveSocketBindingGroup(ctx);
        String refName = refName(name);

        // first remove the local socket binding
        Address localSocketBindingAddress = Address.of(SOCKET_BINDING_GROUP, socketBindingGroup)
                .and(LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING, name);
        ops.removeIfExists(localSocketBindingAddress);

        // then remove the socket binding
        Address socketBindingRefAddress = Address.of(SOCKET_BINDING_GROUP, socketBindingGroup)
                .and(SOCKET_BINDING, refName);
        ops.removeIfExists(socketBindingRefAddress);
    }
}
