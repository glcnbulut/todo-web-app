# user_api — Hızlı Başlatma

Bu dökümantasyon `user_api` servisini hızlıca çalıştırmak ve temel auth akışını test etmek içindir.

1) Ortam değişkenleri örneği

```bash
export SPRING_DATASOURCE_URL='jdbc:sqlserver://localhost:1433;databaseName=GoToDB;trustServerCertificate=true'
export SPRING_DATASOURCE_USERNAME='sa'
export SPRING_DATASOURCE_PASSWORD='s3cret'
export JWT_SECRET='<BASE64_OR_RAW_SECRET>'
export SERVER_PORT='8082'
```

Not: Production için `JWT_SECRET` güçlü, tercihen base64-encoded 256-bit bir anahtar kullanın (bkz. repo kök README).

2) Uygulamayı çalıştırma

```bash
cd user_api
./mvnw -DskipTests spring-boot:run
```

3) Kullanıcı kaydı (curl örnek)

payload.json:

```json
{"email":"user@example.local","name":"User","surname":"Local","password":"pass"}
```

Komut:

```bash
curl -v -i -X POST http://localhost:8082/api/auth/register \
  -H 'Content-Type: application/json' \
  --data-binary @payload.json
```

4) Login ve protected endpoint örneği

login.json:
```json
{"email":"user@example.local","password":"pass"}
```

```bash
# obtain token
curl -s -X POST http://localhost:8082/api/auth/login -H 'Content-Type: application/json' --data-binary @login.json | jq -r .jwt

# use token
curl -v -i -X GET http://localhost:8082/api/users/me -H "Authorization: Bearer <token>"
```

5) Eğer GitHub Actions workflow'u remote'a pushlarken yetki hatası alırsanız

- Repo → Add file → `.github/workflows/maven.yml` içerik yapıştırıp commit edebilirsiniz.
