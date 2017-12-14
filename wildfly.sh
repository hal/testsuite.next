#!/bin/bash

CONTAINER=hal-wildfly
RUNNING=$(docker inspect --format="{{.State.Running}}" ${CONTAINER} 2> /dev/null)
RESTARTING=$(docker inspect --format="{{.State.Restarting}}" ${CONTAINER} 2> /dev/null)

if [ "$RUNNING" == "true" ] || [ "$RESTARTING" == "true" ]; then
    docker stop ${CONTAINER}
    docker rm ${CONTAINER}
fi

docker run -p 8080:8080 -p 9990:9990 -d --name ${CONTAINER} halconsole/hal-wildfly /opt/jboss/wildfly/bin/standalone.sh -c standalone-full-ha-insecure.xml -b 0.0.0.0 -bmanagement 0.0.0.0
