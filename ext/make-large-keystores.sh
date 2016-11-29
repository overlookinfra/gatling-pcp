#!/bin/bash
set -e

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <path to pcp certs>" >&2
    exit 1
fi

cacert=$1/ca_crt.pem
echo Adding ca cert $cacert

keytool -importcert \
        -file $cacert \
        -storepass foobarbaz \
        -keystore src/test/resources/truststore.ks \
        -noprompt

# Assump pairs of _crt/_key files.
certspath=$1/test
for f in $certspath/*agent.example.com_crt.pem
do
  certname=${f%_crt.pem}
  certname=${certname#$certspath/}
  echo $certname

  cat $certspath/${certname}_key.pem $certspath/${certname}_crt.pem > $certname.pem
  openssl pkcs12 \
          -export \
          -in $certname.pem \
          -out $certname.p12 \
          -name $certname \
          -passout pass:foobarbaz
  keytool -importkeystore \
          -storepass foobarbaz \
          -destkeystore src/test/resources/keystore.ks \
          -srckeystore $certname.p12 \
          -srcstorepass foobarbaz \
          -srcstoretype PKCS12 \
          -alias $certname \
          -noprompt
  rm $certname.p12
  rm $certname.pem
done
