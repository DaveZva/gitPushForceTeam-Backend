# Cat Show Backend API

## 🚀 Lokální spuštění (bez Dockeru)
```bash
# 1. Nainstalovat dependencies
mvn clean install

# 2. Spustit aplikaci
mvn spring-boot:run
```

## 🐳 Docker spuštění
```bash
# Z root složky projektu
docker-compose up backend
```

## 📝 API Endpoints

- `GET /api/auth'

## 🔧 Environment Variables

| Variable | Default | Popis |
|----------|---------|-------|
| DB_HOST | localhost | PostgreSQL host |
| DB_PORT | 5432 | PostgreSQL port |
| DB_NAME | catshow | Database name |
| DB_USER | postgres | Database user |
| DB_PASSWORD | postgres | Database password |
