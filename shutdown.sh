#!/bin/bash

SERVER_ZIP=$1
MODE=$2

# Locate server directory
SERVER_DIR="$(jar tf ${SERVER_ZIP} | head -1 | cut -d '/' -f 1)"

if [ "$MODE" == "domain" ]; then
    $SERVER_DIR/bin/jboss-cli.sh -c '/host=master:shutdown'
elif [ "$MODE" == "domain-hc-dc" ]; then
    $SERVER_DIR/bin/jboss-cli.sh -c '/host=master:shutdown'
    # Clean dc, please clean hc manually
    rm -r -d $SERVER_DIR/dc/*
elif [ "$MODE" == "standalone" ]; then
    $SERVER_DIR/bin/jboss-cli.sh -c ':shutdown'
else
    echo "Error: Unknown mode. Specify either domain, domain-hc-dc or standalone"
fi