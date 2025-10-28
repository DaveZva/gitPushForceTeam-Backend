# Cat Show Backend API

## Spuštění přes IDEA
# 1. Spustit \src\main\java\com\gpfteam\catshow\catshow_backend\CatShowBackendApplication.java

## Lokální spuštění
```bash
# 1. Nainstalovat dependencies
mvn clean install

# 2. Spustit aplikaci
mvn spring-boot:run
```

## API Endpoints

- `GET /api/v1/auth'

## Environment Variables

| Variable | Default | Popis |
|----------|---------|-------|
| DB_HOST | localhost | PostgreSQL host |
| DB_PORT | 5432 | PostgreSQL port |
| DB_NAME | catshow | Database name |
| DB_USER | postgres | Database user |
| DB_PASSWORD | postgres | Database password |
