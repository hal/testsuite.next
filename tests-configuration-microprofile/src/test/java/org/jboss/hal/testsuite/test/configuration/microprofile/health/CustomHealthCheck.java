package org.jboss.hal.testsuite.test.configuration.microprofile.health;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;


@Liveness
@ApplicationScoped
public class CustomHealthCheck implements HealthCheck {

    public static final String
            CHECK_NAME = "The best check ever.",
            DOWN = "DOWN",
            UP = "UP",
            PROPERTIES_FILENAME = "/WEB-INF/classes/check.properties",
            PROPERTIES_PATH = "/check.properties",
            PROPERTIES_KEY = "check",
            DOWN_DATA_1 = "naughty service",
            DOWN_DATA_2 = "not available",
            UP_DATA_1 = "good code",
            UP_DATA_2 = "running like a charm";

    @Override
    public HealthCheckResponse call() {

        String checkDecision = getConfiguredCheckDecision();
        HealthCheckResponseBuilder builder = HealthCheckResponse.named(CHECK_NAME);
        switch (checkDecision) {
            case DOWN: builder.withData(DOWN_DATA_1, DOWN_DATA_2).down(); break;
            case UP: builder.withData(UP_DATA_1, UP_DATA_2).up(); break;
            default: throw new IllegalArgumentException("Unsupported check decision " + checkDecision);
        }
        return builder.build();
    }

    private String getConfiguredCheckDecision() {
        Properties props = new Properties();
        try {
            try (InputStream propertyStream = CustomHealthCheck.class.getResourceAsStream(PROPERTIES_PATH)) {
                props.load(propertyStream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load check.properties");
        }
        return (String) props.get(PROPERTIES_KEY);
    }
}
