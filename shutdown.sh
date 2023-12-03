#!/bin/bash

SERVER_ZIP=$1
MODE=$2

# Locate server directory
SERVER_DIR="$(jar tf ${SERVER_ZIP} | head -1 | cut -d '/' -f 1)"

# Check if the server has "master" or "primary" configuration
if find $SERVER_DIR -name "host-primary.xml" -print -quit | grep -q .; then
    HOST="primary"
else
    HOST="master"
fi

if [ "$MODE" == "domain" ]; then
    $SERVER_DIR/bin/jboss-cli.sh -c "/host=$HOST:shutdown"
elif [ "$MODE" == "domain-hc-dc" ]; then
    $SERVER_DIR/bin/jboss-cli.sh -c "/host=$HOST:shutdown"
    # Clean dc, please clean hc manually
    rm -r -d $SERVER_DIR/dc/*
elif [ "$MODE" == "standalone" ]; then
    $SERVER_DIR/bin/jboss-cli.sh -c ":shutdown"
else
    echo "Error: Unknown mode. Specify either domain, domain-hc-dc or standalone"
fi