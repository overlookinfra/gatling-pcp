#!/bin/sh
set -e

if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <cacert> <clientcert> <clientkey> <certname>" >&2
    exit 1
fi

keytool -importcert \
        -file $1 \
        -storepass foobarbaz \
        -keystore src/test/resources/truststore.ks \
        -noprompt

cat $3 $2 > combined.pem

openssl pkcs12 \
        -export \
        -in combined.pem \
        -out $4.p12 \
        -name $4 \
        -passout pass:foobarbaz

keytool -importkeystore \
        -storepass foobarbaz \
        -destkeystore src/test/resources/keystore.ks \
        -srckeystore $4.p12 \
        -srcstorepass foobarbaz \
        -srcstoretype PKCS12 \
        -alias $4 \
        -noprompt

rm combined.pem
rm $4.p12
