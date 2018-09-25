# Tests for authentication using Keycloak SSO

## Feature request
[HAL-1471 Support for authentication in Keycloak SSO](https://issues.jboss.org/browse/HAL-1471)

## Requirements for tests run
See example-setup.sh file for example of Wildfly and Keycloak setup.

## How to run tests
```bash
mvn test -Pkeycloak,standalone,<browser> -Dkeycloak.home=<path to local keycloak installation> -Dssl.truststore.file=<path to truststore to be able to connect management secured with SSL>
```

