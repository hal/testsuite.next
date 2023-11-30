#!/bin/bash

SERVER_ZIP=$1
MODE=$2

# Check if unzip is successful
if unzip -q "${SERVER_ZIP}"; then
    echo "Server unzip successful"
else
    echo "Error: Failed to unzip ${SERVER_ZIP}"
    exit 1
fi

# Locate server directory
SERVER_DIR="$(jar tf ${SERVER_ZIP} | head -1 | cut -d '/' -f 1)"
echo "Server directory: $SERVER_DIR"

# Set JBOSS_HOME
export JBOSS_HOME=$SERVER_DIR

# Check if the server has "master" or "primary" configuration
if find $SERVER_DIR -name "host-primary.xml" -print -quit | grep -q .; then
    HOST="primary"
    HOST_CONFIG_FILE="host-primary.xml"
else
    HOST="master"
    HOST_CONFIG_FILE="host-master.xml"
fi
echo "Host: $HOST"
echo "Host configuration file: $HOST_CONFIG_FILE"

if [ "$MODE" == "domain" ]; then
    $SERVER_DIR/bin/domain.sh --host-config=$HOST_CONFIG_FILE &
    $SERVER_DIR/bin/jboss-cli.sh -c "/host=$HOST/core-service=management/management-interface=http-interface
                                      :undefine-attribute(name=security-realm)
                                      :reload"
    sleep 40
elif [ "$MODE" == "domain-hc-dc" ]; then
    # Setup dc, please setup hc manually
    cp -r $SERVER_DIR/domain/ $SERVER_DIR/dc/
    $SERVER_DIR/bin/domain.sh --host-config=$HOST_CONFIG_FILE -Djboss.domain.base.dir=$SERVER_DIR/dc &
    $SERVER_DIR/bin/jboss-cli.sh -c "/host=$HOST/core-service=management/management-interface=http-interface
                                      :undefine-attribute(name=security-realm)
                                     /host=$HOST:reload"
    sleep 40
elif [ "$MODE" == "standalone" ]; then
    $SERVER_DIR/bin/standalone.sh -c standalone-full-ha.xml &
    $SERVER_DIR/bin/jboss-cli.sh -c "/core-service=management/management-interface=http-interface
                                      :undefine-attribute(name=security-realm)
                                      :reload"
    sleep 40
else
    echo "Error: Unknown mode. Specify either domain, domain-hc-dc or standalone"
fi