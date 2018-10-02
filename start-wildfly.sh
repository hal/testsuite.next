#!/bin/bash

CONTAINER=hal-wildfly
NETWORK=testnetwork

# This is due to ModClusterFilterTest and ModClusterFilterAttributesTest,
# as they require the server not to be bound to the 0.0.0.0 address
docker network create --subnet 172.80.0.0/28 testnetwork
docker run -p 8080:8080 -p 9990:9990 -d --name ${CONTAINER} --network ${NETWORK} halconsole/hal-wildfly-nightly /opt/jboss/wildfly/bin/standalone.sh -c standalone-full-ha-insecure.xml -b 172.80.0.2 -bmanagement 172.80.0.2
