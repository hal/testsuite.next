# Testsuite

Testsuite for the HAL management console based on [Drone & Graphene](http://arquillian.org/guides/functional_testing_using_graphene/) Arquillian extensions.

## Profiles

The testsuite uses various profiles to decide how and which tests to run. The following profiles are available:

- `chrome` | `firefox` | `safari`: Defined the browser to run the tests
- `basic`: Runs basic tests (module `tests/basic`)
- `standalone` | `domain`: Runs tests w/ category `org.jboss.hal.testsuite.category.Standalone` or `org.jboss.hal.testsuite.category.Domain`

Combine multiple profiles to define your setup. You have to choose one profile from each line. But you cannot combine profiles which are on the same line. 

Valid combinations:

- `chrome,basic,standalone`
- `firefox,basic,domain`
- `safari,basic,domain`

Invalid combinations:

- `safari,firefox`
- `basic`
- `standalone,domain`
- `chrome,basic,standalone,,domain`

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

Run all tests:

```bash
mvn test -P<profiles>
```

Run a single test: 

```bash
mvn test -P<profiles> -Dtest=<fully qualified classname>
```

To debug the test(s) use the `maven.surefire.debug` property: 
 
```bash
mvn test -P<profiles> -Dtest=<fully qualified classname> -Dmaven.surefire.debug
```

The tests will automatically pause and await a remote debugger on port 5005. You can then attach to the running tests using your IDE. 
