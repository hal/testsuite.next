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

import static org.jboss.hal.dmr.ModelDescriptionConstants.HOST;
import static org.jboss.hal.dmr.ModelDescriptionConstants.PORT;
import static org.jboss.hal.dmr.ModelDescriptionConstants.REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING_GROUP;

/** Command to add a {@code remote-destination-outbound-socket-binding}. */
public class AddRemoteSocketBinding extends SocketBindingCommand {

    private final String host;
    private final int port;

    /**
     * When socket binding group is not selected, default will be set. Default group for standalone mode is
     * <code>standard-sockets</code> and default group for domain mode is <code>full-sockets</code>.
     * <code>full-sockets</code> is <b>default socket group only for all started servers</b> in default server
     * configuration (<code>server-one</code> and <code>server-two</code>).
     */
    public AddRemoteSocketBinding(String name, String host, int port) {
        this(name, null, host, port);
    }

    public AddRemoteSocketBinding(String name, String socketBindingGroup, String host, int port) {
        super(name, socketBindingGroup);
        this.host = host;
        this.port = port;
    }

    @Override
    public void apply(OnlineCommandContext ctx) throws Exception {
        Operations ops = new Operations(ctx.client);
        String socketBindingGroup = resolveSocketBindingGroup(ctx);

        Address remoteSocketBindingAddress = Address.of(SOCKET_BINDING_GROUP, socketBindingGroup)
                .and(REMOTE_DESTINATION_OUTBOUND_SOCKET_BINDING, name);
        ops.add(remoteSocketBindingAddress, Values.of(HOST, host).and(PORT, port));
    }
}
