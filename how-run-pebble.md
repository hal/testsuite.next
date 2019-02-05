How to run pebble server locally to run LetsEncrypt test
-------------------------------------------------------

Install golang, pebble and minica
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

For fedora, just run `sudo dnf install golang` or download and install from https://golang.org/dl/[go website].

Dowload and install pebble.
[source,bash]
go get -u github.com/letsencrypt/pebble/...
cd $GOPATH/src/github.com/letsencrypt/pebble && go install ./...


Dowload and install minica, a small CA management tool.
[source,bash]
go get github.com/jsha/minica
cd $GOPATH/src/github.com/jsha/minica && go install ./...

Add two virtual IP to bind pebble daemon and wildfly server
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

For linux, consider the interface name as `enp0s31f6`.

*10.10.10.10* is for the pebble server and *10.10.10.11* for the wildfly server.
[source,bash]
sudo ifconfig enp0s31f6:0 10.10.10.10  netmask 255.255.255.0  up
sudo ifconfig enp0s31f6:1 10.10.10.11  netmask 255.255.255.0  up


Set the hosts mapping in /etc/hosts
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
[source,bash]
10.10.10.10    acme-v02.api.letsencrypt.org
10.10.10.10    acme-staging-v02.api.letsencrypt.org
10.10.10.11    www.foobar.com

*www.foobar.com* is the domain-names parameter to set in the obtain-certificate operation

Change pebble/wfe/wfe.go to set the correct endpoint names
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Pebble have different endpoint names, so we must adjust it to make the ACME implementation works.

Edit `go/src/github.com/letsencrypt/pebble/wfe/wfe.go` and change accordingly.

[source,go]
DirectoryPath     = "/directory"
RootCertPath      = "/root"
noncePath         = "/new-nonce"
newAccountPath    = "/new-acct"
acctPath          = "/my-account/"
newOrderPath      = "/new-order"

Create the certificates with letsencrypt fully qualified domain names
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

pebble by default have the certificates with `CN=localhost`, but that will fail to validate, the pebble certificate must set the same name as the public letsecrypt dns names: `acme-v02.api.letsencrypt.org` and `acme-staging-v02.api.letsencrypt.org`

Create the new certificates
[source,bash]
cd go/src/github.com/letsencrypt/pebble/test/certs
minica -ca-cert pebble.minica.pem \
    -ca-key pebble.minica.key.pem \
    -domains acme-v02.api.letsencrypt.org,acme-staging-v02.api.letsencrypt.org \
    -ip-addresses 10.10.10.10

Change the test/config/pebble-config.json
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Edit the `go/src/github.com/letsencrypt/pebble/test/config/pebble-config.json` to set the recently created certificate and bind the ip address.

[source,json]
{
  "pebble": {
    "listenAddress": "10.10.10.10:443",
    "certificate": "test/certs/acme-v02.api.letsencrypt.org/cert.pem",
    "privateKey": "test/certs/acme-v02.api.letsencrypt.org/key.pem",
    "httpPort": 5002,
    "tlsPort": 5001
  }
}

Launch pebble
~~~~~~~~~~~~~

The `PEBBLE_VA_ALWAYS_VALID` parameter is to relax the SSL validation.
The `PEBBLE_VA_NOSLEEP` parameter is to not delay the validation process.
You must run as root as it binds the 443 port.
[source,bash]
cd go/src/github.com/letsencrypt/pebble
sudo PEBBLE_VA_ALWAYS_VALID=1 PEBBLE_VA_NOSLEEP=1 go/bin/pebble &

Import minica.pem to a java truststore
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Export to DER format
[source,bash]
openssl x509 -in test/certs/pebble.minica.pem -inform pem -out pebble.minica.der -outform der

Import to a java truststore
[source,bash]
keytool -importcert -alias pebble.minica -keystore wildfly-15.0.0.Alpha1-SNAPSHOT/standalone/configuration/truststore.jks -storepass admin123 -file pebble.minica.der

Launch Wildfly
~~~~~~~~~~~~~~

If JAVA_HOME is not set for root user, you can pass as parameter.
[source,bash]
sudo JAVA_HOME=/opt/javavm ./standalone.sh -c standalone-full-ha.xml \
    -Djboss.http.port=80 -Djboss.https.port=443 \
    -b 10.10.10.11 -bmanagement 10.10.10.11 \
    -Djavax.net.ssl.trustStore=../standalone/configuration/truststore.jks \
    -Djavax.net.ssl.trustStorePassword=admin123

This should be enough to work.

Local Test
~~~~~~~~~~

Add a `key-store` and `certificate-authority-account` to test it.

[source]
/subsystem=elytron/key-store=my_ks1:add(credential-reference={clear-text=admin123},type=jks,path=my_ks1.jks)
/subsystem=elytron/certificate-authority-account=caa_lets1:add(alias=caa_lets1,key-store=my_ks1)

Run `obtain-certificate` to test it and the outcome should be `success`

[source]
/subsystem=elytron/key-store=my_ks1:obtain-certificate(certificate-authority-account=caa_lets1,alias=my_new_cert2,domain-names=[www.foobar.com],agree-to-terms-of-service,staging)
{"outcome" => "success"}


You can display the certificate as:
[source]
/subsystem=elytron/key-store=my_ks1:read-alias(alias=my_new_cert2)
