param (
    [string]$studentId = "000000"
)

$keytool = "C:\Users\nekst\.jdks\ms-21.0.8\bin\keytool.exe"
if (-not (Test-Path $keytool)) {
    Write-Host "Keytool not found at $keytool"
    exit 1
}

# Clean up old files
Remove-Item *.jks, *.p12, *.crt, *.csr -ErrorAction SilentlyContinue

$rootAlias = "labRootCA"
$interAlias = "labInterCA"
$serverAlias = "labServer"

$pass = "secret"

# === ROOT CA ===
Write-Host "Generating Root CA (PKCS12)..."
& $keytool -genkeypair -alias $rootAlias -keyalg RSA -keysize 4096 -validity 3650 `
    -storetype PKCS12 -keystore root.p12 -storepass $pass `
    -dname "CN=TimoshenkoRootCA, OU=Student_$studentId, O=ITMO, L=SPb, ST=SPb, C=RU" `
    -ext bc=ca:true -ext KeyUsage=digitalSignature,keyCertSign

& $keytool -export -alias $rootAlias -storetype PKCS12 -keystore root.p12 -storepass $pass -file root.crt

# === INTERMEDIATE CA ===
Write-Host "Generating Intermediate CA (PKCS12)..."
& $keytool -genkeypair -alias $interAlias -keyalg RSA -keysize 2048 -validity 1825 `
    -storetype PKCS12 -keystore inter.p12 -storepass $pass `
    -dname "CN=TimoshenkoInterCA, OU=Student_$studentId, O=ITMO, L=SPb, ST=SPb, C=RU"

& $keytool -certreq -alias $interAlias -storetype PKCS12 -keystore inter.p12 -storepass $pass -file inter.csr

& $keytool -gencert -alias $rootAlias -storetype PKCS12 -keystore root.p12 -storepass $pass `
    -infile inter.csr -outfile inter.crt -validity 1825 `
    -ext "BasicConstraints:critical:true,CA:true,pathLen:0" `
    -ext "KeyUsage=digitalSignature,keyCertSign"

& $keytool -import -alias $rootAlias -storetype PKCS12 -keystore inter.p12 -storepass $pass -file root.crt -noprompt
& $keytool -import -alias $interAlias -storetype PKCS12 -keystore inter.p12 -storepass $pass -file inter.crt -noprompt

# === SERVER CERTIFICATE ===
Write-Host "Generating Server Certificate (PKCS12)..."
& $keytool -genkeypair -alias $serverAlias -keyalg RSA -keysize 2048 -validity 365 `
    -storetype PKCS12 -keystore server.p12 -storepass $pass `
    -dname "CN=localhost, OU=Student_$studentId, O=ITMO, L=SPb, ST=SPb, C=RU" `
    -ext "SAN=dns:localhost"

& $keytool -certreq -alias $serverAlias -storetype PKCS12 -keystore server.p12 -storepass $pass -file server.csr

& $keytool -gencert -alias $interAlias -storetype PKCS12 -keystore inter.p12 -storepass $pass `
    -infile server.csr -outfile server.crt -validity 365 `
    -ext KeyUsage=digitalSignature,keyEncipherment `
    -ext EKU=serverAuth,clientAuth `
    -ext san=dns:localhost

& $keytool -import -alias $rootAlias   -storetype PKCS12 -keystore server.p12 -storepass $pass -file root.crt  -noprompt
& $keytool -import -alias $interAlias  -storetype PKCS12 -keystore server.p12 -storepass $pass -file inter.crt -noprompt
& $keytool -import -alias $serverAlias -storetype PKCS12 -keystore server.p12 -storepass $pass -file server.crt -noprompt

Write-Host "Certificates generated successfully!"
Write-Host "  root.crt   -> root certificate (add to Windows Trusted Root)"
Write-Host "  server.p12 -> server keystore (copy to src/main/resources/)"
