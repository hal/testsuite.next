package org.jboss.hal.testsuite.test.configuration.web.services;

import org.wildfly.extras.creaper.core.online.operations.Address;

public class WebServicesFixtures {

    public static final Address WEB_SERVICES_ADDRESS = Address.subsystem("webservices");

    public static Address clientConfigurationAddress(String clientConfigurationAddressName) {
        return WEB_SERVICES_ADDRESS.and("client-config", clientConfigurationAddressName);
    }

    public static Address endpointConfigurationAddress(String endpointConfigurationName) {
        return WEB_SERVICES_ADDRESS.and("endpoint-config", endpointConfigurationName);
    }

    private WebServicesFixtures() {

    }

    private enum ConfigurationType {

        END_POINT_CONFIGURATION("endpoint-config"),
        CLIENT_CONFIGURATION("client-config");

        private String type;

        ConfigurationType(String type) {
            this.type = type;
        }
    }

    private enum HandlerChainType {

        PRE_HANDLER_CHAIN("pre-handler-chain"),
        POST_HANDLER_CHAIN("post-handler-chain");

        private String type;

        HandlerChainType(String type) {
            this.type = type;
        }

    }

    public static class HandlerChain {

        private final String configurationName;
        private final String handlerChainName;
        private final ConfigurationType configurationType;
        private final HandlerChainType handlerChainType;

        private HandlerChain(Builder builder) {
            this.configurationName = builder.configurationName;
            this.handlerChainName = builder.handlerChainName;
            this.configurationType = builder.configurationType;
            this.handlerChainType = builder.handlerChainType;
        }

        public static class Builder {

            private final String configurationName;
            private String handlerChainName;
            private ConfigurationType configurationType;
            private HandlerChainType handlerChainType;

            public Builder(String configurationName) {
                this.configurationName = configurationName;
            }

            public Builder handlerChainName(String handlerChainName) {
                this.handlerChainName = handlerChainName;
                return this;
            }

            public Builder endpointConfiguration() {
                this.configurationType = ConfigurationType.END_POINT_CONFIGURATION;
                return this;
            }

            public Builder clientConfiguration() {
                this.configurationType = ConfigurationType.CLIENT_CONFIGURATION;
                return this;
            }

            public Builder preHandlerChain() {
                this.handlerChainType = HandlerChainType.PRE_HANDLER_CHAIN;
                return this;
            }

            public Builder postHandlerChain() {
                this.handlerChainType = HandlerChainType.POST_HANDLER_CHAIN;
                return this;
            }

            public HandlerChain build() {
                return new HandlerChain(this);
            }
        }

        public String getHandlerChainName() {
            return handlerChainName;
        }

        public Address handlerChainAddress() {
            return WEB_SERVICES_ADDRESS.and(configurationType.type, configurationName).and(handlerChainType.type, handlerChainName);
        }
    }
}
