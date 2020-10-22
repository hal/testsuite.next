#!/bin/bash

IMAGE=halconsole/hal-wildfly
CONTAINER=hal-wildfly
NETWORK=testnetwork
IP_ADDRESS=172.80.0.2
TMP_WILDFLY=$(mktemp -d -t wildfly.hal.testsuite.XXX)

echo "########## WILDFLY DIRECTORY associated with testsuite is ${TMP_WILDFLY} ##########"

docker network create --subnet 172.80.0.0/28 ${NETWORK} > /dev/null

pushd "${TMP_WILDFLY}" > /dev/null || exit
  docker run -p 8080:8080 -p 9990:9990 -d --name temp-wildfly halconsole/hal-wildfly /opt/jboss/wildfly/bin/standalone.sh -c standalone-full-ha-insecure.xml -b ${IP_ADDRESS} -bmanagement ${IP_ADDRESS} > /dev/null
  docker cp temp-wildfly:/opt/jboss/wildfly/. "${PWD}"
  docker stop temp-wildfly > /dev/null
  docker rm temp-wildfly > /dev/null
  # This is due to ModClusterFilterTest and ModClusterFilterAttributesTest,
  # as they require the server not to be bound to the 0.0.0.0 address
  docker run -p 8080:8080 -p 9990:9990 -d --name ${CONTAINER} --network ${NETWORK} -v "$(pwd)":/opt/jboss/wildfly ${IMAGE} /opt/jboss/wildfly/bin/standalone.sh -c standalone-full-ha-insecure.xml -b ${IP_ADDRESS} -bmanagement ${IP_ADDRESS} > /dev/null
popd > /dev/null || exit

echo "JBOSS_HOME=${TMP_WILDFLY}" > testsuite.properties
