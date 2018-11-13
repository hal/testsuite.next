package org.jboss.hal.testsuite.test.configuration.elytron.other.settings.logs;

import java.util.Map;
import java.util.function.Consumer;

import org.wildfly.security.auth.server.event.SecurityAuthenticationFailedEvent;
import org.wildfly.security.auth.server.event.SecurityAuthenticationSuccessfulEvent;
import org.wildfly.security.auth.server.event.SecurityEvent;

public class DummySecurityEventListener implements Consumer<SecurityEvent> {

    public static String ATTRIBUTE = "myAttribute";

    private String myAttribute;

    public void initialize(Map<String, String> configuration) {
        myAttribute = configuration.get("myAttribute");
        System.out.println("DummySecurityEventListener initialized with myAttribute = " + myAttribute);
    }

    @Override
    public void accept(SecurityEvent securityEvent) {
        if (securityEvent instanceof SecurityAuthenticationSuccessfulEvent) {
            System.err.printf("Authenticated user \"%s\"\n", securityEvent.getSecurityIdentity().getPrincipal());
        } else if (securityEvent instanceof SecurityAuthenticationFailedEvent) {
            System.err.printf("Failed authentication as user \"%s\"\n", ((SecurityAuthenticationFailedEvent)securityEvent).getPrincipal());
        }

    }
}
