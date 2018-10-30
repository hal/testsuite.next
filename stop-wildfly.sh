#!/bin/bash

CONTAINER=hal-wildfly-nightly
NETWORK=testnetwork
RUNNING=$(docker inspect --format="{{.State.Running}}" ${CONTAINER} 2> /dev/null)
RESTARTING=$(docker inspect --format="{{.State.Restarting}}" ${CONTAINER} 2> /dev/null)
MOUNT=$(docker inspect --format="{{(index (.Mounts) 0).Source}}" ${CONTAINER})

if [ "$RUNNING" == "true" ] || [ "$RESTARTING" == "true" ]; then
    docker stop ${CONTAINER}
    rm -rf ${MOUNT}
    docker rm ${CONTAINER}
fi

docker network rm ${NETWORK}
