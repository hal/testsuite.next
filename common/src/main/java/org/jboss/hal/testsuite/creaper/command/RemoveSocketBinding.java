package org.jboss.hal.testsuite.creaper.command;

import org.wildfly.extras.creaper.core.online.OnlineCommandContext;
import org.wildfly.extras.creaper.core.online.operations.Address;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING;
import static org.jboss.hal.dmr.ModelDescriptionConstants.SOCKET_BINDING_GROUP;

public class RemoveSocketBinding extends SocketBindingCommand {

    public RemoveSocketBinding(String name) {
        super(name);
    }

    public RemoveSocketBinding(String name, String socketBindingGroup) {
        super(name, socketBindingGroup);
    }

    @Override
    public void apply(OnlineCommandContext ctx) throws Exception {
        Operations ops = new Operations(ctx.client);
        String socketBindingGroup = resolveSocketBindingGroup(ctx);
        Address socketBindingRefAddress = Address.of(SOCKET_BINDING_GROUP, socketBindingGroup)
            .and(SOCKET_BINDING, name);
        ops.removeIfExists(socketBindingRefAddress);
    }
}
