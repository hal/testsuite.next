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
import org.wildfly.extras.creaper.core.online.operations.Values;

import static org.jboss.hal.dmr.ModelDescriptionConstants.LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING_GROUP;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING_REF;

/** Command to add a {@code local-destination-outbound-socket-binding} and a socket binding for reference. */
public class AddLocalSocketBinding extends SocketBindingCommand {

    /**
     * When socket binding group is not selected, default will be set. Default group for standalone mode is
     * <code>standard-sockets</code> and default group for domain mode is <code>full-sockets</code>.
     * <code>full-sockets</code> is <b>default socket group only for all started servers</b> in default server
     * configuration (<code>server-one</code> and <code>server-two</code>).
     */
    public AddLocalSocketBinding(String name) {
        super(name);
    }

    public AddLocalSocketBinding(String name, String socketBindingGroup) {
        super(name, socketBindingGroup);
    }

    @Override
    public void apply(OnlineCommandContext ctx) throws Exception {
        Operations ops = new Operations(ctx.client);
        String socketBindingGroup = resolveSocketBindingGroup(ctx);
        String refName = refName(name);

        // first create a reference socket binding
        Address socketBindingRefAddress = Address.of(SOCKET_BINDING_GROUP, socketBindingGroup)
                .and(SOCKET_BINDING, refName);
        ops.add(socketBindingRefAddress);

        // then create the local socket binding
        Address localSocketBindingAddress = Address.of(SOCKET_BINDING_GROUP, socketBindingGroup)
                .and(LOCAL_DESTINATION_OUTBOUND_SOCKET_BINDING, name);
        ops.add(localSocketBindingAddress, Values.of(SOCKET_BINDING_REF, refName));
    }
}
