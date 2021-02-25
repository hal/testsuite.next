package org.wildfly.security.examples;

import java.util.Map;
import java.util.function.Consumer;

import org.wildfly.security.auth.server.event.SecurityAuthenticationFailedEvent;
import org.wildfly.security.auth.server.event.SecurityAuthenticationSuccessfulEvent;
import org.wildfly.security.auth.server.event.SecurityEvent;

/**
 * Example of custom-security-event-listener
 * Taken from @see <a href="https://github.com/wildfly-security-incubator/elytron-examples/pull/2">
 *     https://github.com/wildfly-security-incubator/elytron-examples/pull/2</a>
 */
public class MySecurityEventListener implements Consumer<SecurityEvent> {
    private String myAttribute;

    // receiving configuration from subsystem
    public void initialize(Map<String, String> configuration) {
        myAttribute = configuration.get("myAttribute");
        System.out.println("MySecurityEventListener initialized with myAttribute = " + myAttribute);
    }

    @Override
    public void accept(SecurityEvent securityEvent) {
        if (securityEvent instanceof SecurityAuthenticationSuccessfulEvent) {
            System.err.printf("Authenticated user \"%s\"\n", securityEvent.getSecurityIdentity().getPrincipal());
        } else if (securityEvent instanceof SecurityAuthenticationFailedEvent) {
            System.err.printf("Failed authentication as user \"%s\"\n",
                ((SecurityAuthenticationFailedEvent) securityEvent).getPrincipal());
        }
    }
}