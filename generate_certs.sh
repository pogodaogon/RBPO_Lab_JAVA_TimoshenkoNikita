#!/bin/bash
set -e

STUDENT_ID="23325"
PASS="secret"
SERVER_ALIAS="myServerCert"

# Clean up old files
rm -f *.p12 *.crt *.csr *.key *.srl

# === ROOT CA ===
echo "Generating Root CA..."
openssl genrsa -out root.key 4096
openssl req -x509 -new -nodes -key root.key -sha256 -days 3650 -out root.crt -subj "/C=RU/ST=SPb/L=SPb/O=ITMO/OU=Student_$STUDENT_ID/CN=MyRootCA_Name"

# === INTERMEDIATE CA ===
echo "Generating Intermediate CA..."
openssl genrsa -out inter.key 2048
openssl req -new -key inter.key -out inter.csr -subj "/C=RU/ST=SPb/L=SPb/O=ITMO/OU=Student_$STUDENT_ID/CN=MyInterCA_Name"

# Create extension file for intermediate
cat > inter.ext << EOF
basicConstraints = critical, CA:true, pathlen:0
keyUsage = critical, digitalSignature, cRLSign, keyCertSign
EOF

openssl x509 -req -in inter.csr -CA root.crt -CAkey root.key -CAcreateserial -out inter.crt -days 1825 -sha256 -extfile inter.ext

# === SERVER CERTIFICATE ===
echo "Generating Server Certificate..."
openssl genrsa -out server.key 2048
openssl req -new -key server.key -out server.csr -subj "/C=RU/ST=SPb/L=SPb/O=ITMO/OU=Student_$STUDENT_ID/CN=localhost"

cat > server.ext << EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth, clientAuth
subjectAltName = @alt_names
[alt_names]
DNS.1 = localhost
EOF

openssl x509 -req -in server.csr -CA inter.crt -CAkey inter.key -CAcreateserial -out server.crt -days 365 -sha256 -extfile server.ext

# === CREATE PKCS12 SERVER KEYSTORE ===
echo "Creating Server PKCS12 Keystore..."
# We need to bundle server cert, its private key, and the CA chain.
cat server.crt inter.crt root.crt > fullchain.crt
openssl pkcs12 -export -out server.p12 -inkey server.key -in fullchain.crt -name "$SERVER_ALIAS" -passout pass:$PASS

echo "Certificates generated successfully!"
echo "  root.crt   -> root certificate (add to Trusted Root)"
echo "  server.p12 -> server keystore"

# Clean up temp files
rm -f *.key *.csr *.srl inter.ext server.ext fullchain.crt

mkdir -p src/main/resources
cp server.p12 src/main/resources/server.p12
