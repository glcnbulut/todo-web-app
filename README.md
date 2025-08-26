# ToDo Web Uygulaması

Bu depo, microservice mimarisinde geliştirilmiş örnek bir ToDo uygulamasıdır. İçinde `user_api`, `todo_api`, `todo-frontend` gibi alt projeler bulunur.

Hızlı başlatma (user_api için):

1. Ortam değişkenlerini ayarlayın (örnek):

```bash
export SPRING_DATASOURCE_URL='jdbc:sqlserver://localhost:1433;databaseName=GoToDB;trustServerCertificate=true'
export SPRING_DATASOURCE_USERNAME='sa'
export SPRING_DATASOURCE_PASSWORD='s3cret'
export JWT_SECRET='ChangeThisVerySecretKey'  # Prod için güçlü, tercihen base64-encoded 256-bit anahtar kullanın
export SERVER_PORT='8082'
```

2. Uygulamayı çalıştırın:

```bash
cd user_api
./mvnw -DskipTests spring-boot:run
```

Notlar:
- `JWT_SECRET` için üretimde en az 256-bit güvenli bir anahtar kullanın. CI ve testlerde kısa string toleranslıdır çünkü `JwtTokenUtil` base64 değilse SHA-256 türetir.
- CI: `.github/workflows/maven.yml` dosyası push/PR başına `mvn test` çalıştırır.
---

Önemli notlar ve ipuçları

- JWT_SECRET üretimi (güçlü anahtar önerisi):

```bash
# 32 byte (256-bit) base64 olarak üret
openssl rand -base64 32
```

- Curl ile test yaparken shell-quoting kaynaklı hatalardan kaçınmak için payload'ı dosyaya yazıp `--data-binary @file` kullanın. Alternatif olarak payload'ı base64leyip shell içinde `base64 --decode` ile gönderin.

- Local test çalıştırma (user_api):

```bash
cd user_api
./mvnw -DskipTests=false test
```

- Eğer GitHub remote push sırasında workflow dosyaları reddedilirse (örn. Personal Access Token'ın `workflow` izni yoksa), iki çözüm:
	- 1) GitHub web UI kullanarak repo > Add file > Create new file yoluyla `.github/workflows/maven.yml` dosyasını ekleyin.
	- 2) Kendi hesabınızda PAT oluşturup `workflow` scope verin, sonra `git push` yapın.

