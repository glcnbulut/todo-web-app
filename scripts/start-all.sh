#!/usr/bin/env bash
set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

echo "MSSQL container başlatılıyor veya yeniden kullanılıyor..."
# docker compose komutunu tespit et: önce `docker compose`, yoksa `docker-compose` (sudo ile) kullan
if command -v docker-compose >/dev/null 2>&1; then
  DC_CMD="sudo docker-compose"
elif sudo docker compose version >/dev/null 2>&1; then
  DC_CMD="sudo docker compose"
else
  echo "'docker compose' veya 'docker-compose' bulunamadı. Lütfen Docker Compose kurun." >&2
  exit 1
fi

# docker daemon erişilebilir mi kontrol et (sudo ile)
if ! sudo docker info >/dev/null 2>&1; then
  echo "HATA: Docker daemon’a erişilemiyor. Docker’ın çalıştığından emin olun (örn: 'sudo systemctl start docker')."
  exit 1
fi

# Eğer zaten bir mssql container varsa yeni oluşturmadan onu kullan
# Eğer bir container host port 1433’e maplenmişse, o container yeniden kullanılacak ve compose up atlanacak
EXISTING_PORT_CONTAINER=$(sudo docker ps --format '{{.Names}} {{.Ports}}' | awk '/0.0.0.0:1433->/ {print $1; exit}') || true
if [ -n "$EXISTING_PORT_CONTAINER" ]; then
  echo "Host:1433 portunu açmış bir container bulundu -> '$EXISTING_PORT_CONTAINER' kullanılacak, compose up atlanıyor."
  WAIT_CONTAINER="$EXISTING_PORT_CONTAINER"
elif sudo docker ps --format '{{.Names}}' | grep -q -E '^(mssql|todo_mssql)$'; then
  echo "Var olan bir mssql container bulundu, compose up atlanıyor ve o kullanılacak."
  if sudo docker ps --format '{{.Names}}' | grep -q '^mssql$'; then
    WAIT_CONTAINER='mssql'
  else
    WAIT_CONTAINER='todo_mssql'
  fi
else
  echo "Mevcut mssql container bulunamadı, ${DC_CMD} ile oluşturuluyor..."
  ${DC_CMD} up -d mssql || true
  # oluşturulan container’ın adını belirle (compose bazen 'todo_mssql' ismi verebilir)
  if sudo docker ps --format '{{.Names}}' | grep -q '^mssql$'; then
    WAIT_CONTAINER='mssql'
  elif sudo docker ps --format '{{.Names}}' | grep -q '^todo_mssql$'; then
    WAIT_CONTAINER='todo_mssql'
  else
    WAIT_CONTAINER='mssql'
  fi
fi

# sqlserver hazır olana kadar bekle
echo "MSSQL’in sağlıklı olması bekleniyor..."
echo "MSSQL’in '$WAIT_CONTAINER' container’ında bağlantı kabul etmesi bekleniyor..."
attempt=0
max_attempts=40
sleep_interval=8
while [ $attempt -lt $max_attempts ]; do
  attempt=$((attempt + 1))
  # Daha sessiz çıktı: her 2 denemede bir yazdır
  if [ $((attempt % 2)) -eq 1 ]; then
    echo "Deneme $attempt/$max_attempts: SQL Server hazır mı kontrol ediliyor..."
  fi

  # Bağlantıyı test et: mssql-tools imajını aynı network namespace içinde kullan
  # Bu yöntem Linux’ta host.docker.internal sorunlarını önler
  if sudo docker run --rm --network container:"$WAIT_CONTAINER" mcr.microsoft.com/mssql-tools bash -c "/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'gulcanB1.' -Q 'SELECT 1'" >/dev/null 2>&1; then
    echo "MSSQL: sqlcmd testi container ağında başarılı oldu"
    break
  fi

  # Yedek yöntem: host üzerindeki TCP 1433 açık mı kontrol et
  if ss -lnt | grep -q ':1433\b'; then
    echo "MSSQL: TCP 1433 host üzerinde açık görünüyor (deneme $attempt)"
  fi

  sleep $sleep_interval
done

if [ $attempt -ge $max_attempts ]; then
  echo "HATA: MSSQL $max_attempts denemeden sonra hazır olmadı. Hata bilgisi çıkarılıyor..."
  echo "--- docker ps -a ---"
  sudo docker ps -a || true
  echo "--- docker ps (mssql filtreli) ---"
  sudo docker ps --filter "name=mssql" --filter "name=todo_mssql" || true
  echo "--- docker inspect (mssql) ---"
  sudo docker inspect mssql || true
  echo "--- mssql loglarının son 200 satırı ---"
  sudo docker logs --tail 200 mssql || true
  echo "--- Container içinde sqlcmd denemesi (stderr) ---"
  sudo docker exec mssql bash -lc "/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'gulcanB1.' -Q 'SELECT 1'" || true
  echo "--- Hata bilgisi sonu ---"
  exit 1
fi

# todo_api başlat
if [ -f ./todo_api/mvnw ]; then
  echo "todo_api başlatılıyor"
  (cd todo_api && chmod +x ./mvnw && ./mvnw -DskipTests spring-boot:run > ../todo_api.log 2>&1 &) || true
else
  echo "todo_api için mvnw bulunamadı, atlanıyor"
fi

# todo-bff başlat
if [ -f ./todo-bff/mvnw ]; then
  echo "todo-bff başlatılıyor"
  (cd todo-bff && chmod +x ./mvnw && ./mvnw -DskipTests spring-boot:run > ../todo-bff.log 2>&1 &) || true
else
  echo "todo-bff için mvnw bulunamadı, atlanıyor"
fi

# user_api başlat
if [ -f ./user_api/mvnw ]; then
  echo "user_api başlatılıyor"
  (cd user_api && chmod +x ./mvnw && \
    SPRING_DATASOURCE_URL='jdbc:sqlserver://localhost:1433;databaseName=GoToDB;trustServerCertificate=true' \
    SPRING_DATASOURCE_USERNAME='sa' \
    SPRING_DATASOURCE_PASSWORD='gulcanB1.' \
    JWT_SECRET='ChangeThisVerySecretKey' \
    SERVER_PORT='8081' \
    ./mvnw -DskipTests spring-boot:run > ../user_api.log 2>&1 &) || true
else
  echo "user_api için mvnw bulunamadı, atlanıyor"
fi

# frontend’leri başlat (vite)
if [ -d ./todo-frontend ]; then
  echo "todo-frontend başlatılıyor"
  (cd todo-frontend && npm ci && npm run dev > ../todo-frontend.log 2>&1 &) || true
fi

if [ -d ./user-frontend ]; then
  echo "user-frontend başlatılıyor"
  (cd user-frontend && npm ci && npm run dev > ../user-frontend.log 2>&1 &) || true
fi

echo "Tüm başlatma komutları gönderildi. Log dosyaları:"
ls -1 *.log || true

echo "Loglar takip ediliyor (çıkmak için Ctrl-C’ye basın)."
# logları takip et
tail -n +1 -f *.log
