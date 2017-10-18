# Testsuite

Testsuite for the HAL management console based on [Drone & Graphene](http://arquillian.org/guides/functional_testing_using_graphene/) Arquillian extensions.

## Profiles

The testsuite uses various profiles to decide how and which tests to run. The following profiles are available:

- `chrome` | `firefox` | `safari`: Defined the browser to run the tests
- `basic`: Runs basic tests (module `tests/basic`)
- `standalone` | `domain`: Runs tests w/ category `org.jboss.hal.testsuite.category.Standalone` or `org.jboss.hal.testsuite.category.Domain`

Combine multiple profiles to define your setup.

## Run Tests 

Running tests requires a running WildFly / JBoss EAP server with an insecure management interface. Use the following command to remove the security realm from the management interface:

**Standalone**

```
/core-service=management/management-interface=http-interface:undefine-attribute(name=security-realm)
:reload
```

**Domain**

```
/host=master/core-service=management/management-interface=http-interface:undefine-attribute(name=security-realm)
/host=master:reload
```

### Run Tests

```bash
mvn test -P<profiles>
```

If you want to run a single test use 

```bash
mvn test -P<profiles> -Dtest=<fully qualified classname>
```

To debug the test(s) add the option `-Dmaven.surefire.debug`. 
