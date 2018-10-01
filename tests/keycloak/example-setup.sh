#!/bin/bash
set -x

rm -rf jboss-eap-7.2 rh-sso-7.2 keycloak.jks keycloak.cert truststore.jks

unzip -q rh-sso-7.2.4.zip
unzip -q jboss-eap-7.2.0.CD14.zip

keytool -genkey -noprompt -alias localhost -dname "CN=James Bond, OU=Top, O=Secret, L=Saigon, ST=SV, C=VN" -keyalg RSA -keystore keycloak.jks -storepass secure -keypass secure -validity 10950
keytool -export -noprompt -alias localhost -keystore keycloak.jks -rfc -file keycloak.cert -storepass secure
keytool -import -noprompt -trustcacerts -alias localhost -file keycloak.cert -keystore truststore.jks -storepass secure -keypass secure

cp keycloak.jks rh-sso-7.2/standalone/configuration/
cp keycloak.jks jboss-eap-7.2/standalone/configuration/

pushd rh-sso-7.2/bin
  ./add-user-keycloak.sh -r master -u kcadmin -p kcadmin
popd

cp rh-sso-7.2.4-eap7-adapter.zip jboss-eap-7.2
pushd jboss-eap-7.2
  unzip -q rh-sso-7.2.4-eap7-adapter.zip
  pushd bin
    ./jboss-cli.sh --file=adapter-elytron-install-offline.cli
  popd
popd

