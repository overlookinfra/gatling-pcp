A test suite for triggering Puppet runs over PCP. Simulates a large number of connections with low message traffic.

# Usage

Start a PCP broker (with default address of broker.example.com:8142) from a checkout of pcp-broker

    $ lein tk

Test configuration is specified in `src/test/resources/gatling.conf`.

(Re-)generate keystores for TLS configuration.

    $ PCP_CERTS=<path_to_pcp_broker_repo>/test-resources/ssl
    $ rm src/test/resources/*.ks
    $ ./ext/make-keystores.sh $PCP_CERTS/certs/ca.pem $PCP_CERTS/certs/client01.example.com.pem $PCP_CERTS/private_keys/client01.example.com.pem client01.example.com

Run tests against it

    $ sbt compile gatling:test

Note that the test will currently report failure, because requests are not being registered with Gatling. This needs fixing, but I'm not yet sure how.

Write gatling test scenarios and put them in `src/test/scala/<scenario>.scala`, then they will be used as a basis for testing by sbt-gatling.
