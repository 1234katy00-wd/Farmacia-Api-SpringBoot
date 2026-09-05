# Farmacia API

API REST para gestionar medicamentos, compras y tickets en una farmacia. El proyecto está desarrollado con Spring Boot y el módulo ejecutable se encuentra dentro del directorio `demo/`.


## Tecnologías

- Java 17
- Spring Boot 3.3.2
- Maven Wrapper
- Spring Web
- Spring Validation
- Spring Data JPA
- PostgreSQL 16
- Springdoc OpenAPI / Swagger UI
- JUnit 5 + Spring Boot Test
- JaCoCo

## Requisitos

- JDK 17
- Docker Desktop o Docker Engine
- Git
- Node.js y npm para ejecutar el frontend
- Windows: usar `demo/mvnw.cmd`
- Linux/macOS: usar `demo/mvnw`

## Obtener el proyecto con Git

Clona los repositorios en una misma carpeta de trabajo. Sustituye las URLs por las URLs reales de tus repositorios:

```bash
git clone <https://github.com/1234katy00-wd/Farmacia-Api-SpringBoot.git> farmacia-Api-SpringBoot
git clone <https://github.com/1234katy00-wd/Farmacia.git> farmacia-frontend
```

La estructura esperada es:

```text
ProyectoLatam/
├── farmacia-Api-SpringBoot/
└── farmacia-frontend/
```

## Arranque rápido

1. Levantar la base de datos PostgreSQL con Docker:

```powershell
cd demo
docker compose up -d
```

2. Ejecutar la aplicación:

```powershell
cd demo
.\mvnw.cmd spring-boot:run
```

o en Linux/macOS:

```bash
cd demo
./mvnw spring-boot:run
```

La API queda disponible en:

- http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

Swagger y OpenAPI se habilitan mediante el perfil `dev`, que está activo por defecto en `application.yaml`.

## Levantar el frontend

El frontend debe ejecutarse en una terminal separada de la API.

```powershell
cd farmacia-frontend
npm install
npm run dev
```

En Linux/macOS:

```bash
cd farmacia-frontend
npm install
npm run dev
```

La aplicación frontend queda disponible normalmente en:

- http://localhost:5173

Para ejecutar el proyecto completo, deja la API ejecutándose en una terminal y levanta el frontend en otra:

| Terminal | Comando | URL |
| --- | --- | --- |
| API | `cd farmacia-Api-SpringBoot/demo` y `./mvnw spring-boot:run` | http://localhost:8080 |
| Frontend | `cd farmacia-frontend` y `npm run dev` | http://localhost:5173 |

## Configuración de entorno

El proyecto usa el perfil `dev` por defecto. La config principal está en:

- `demo/src/main/resources/application.yaml`
- `demo/src/main/resources/application-dev.yaml`

Parámetros por defecto:

| Variable | Valor por defecto |
| --- | --- |
| `DB_HOST` | `localhost` |
| `DB_PORT` | `5432` |
| `DB_NAME` | `farmacia_db` |
| `DB_USER` | `user_db` |
| `DB_PASSWORD` | `pass_db` |
| `SERVER_PORT` | `8080` |

También existe un ejemplo de variables en:

- `demo/.env.example`

## Docker PostgreSQL

El contenedor se define en:

- `demo/compose.yml`

Service:

```yaml
services:
  db:
    image: postgres:16-alpine
    ports:
      - "5432:5432"
```

Credenciales por defecto:

- base: `farmacia_db`
- usuario: `user_db`
- password: `pass_db`

Para detener la base de datos:

```powershell
cd demo
docker compose down
```

## Ejecutar pruebas

Para correr la suite completa:

```powershell
cd demo
.\mvnw.cmd test
```

Para ejecutar únicamente la prueba de integración de medicamentos:

```powershell
cd demo
.\mvnw.cmd test -Dtest=MedicationIntegrationTest
```

o:

```bash
cd demo
./mvnw test
```

El proyecto incluye un perfil de tests en:

- `demo/src/test/resources/application-test.yaml`

Este perfil está conectado al mismo PostgreSQL local de Docker para que los tests de contexto y JPA se ejecuten con la configuración real del proyecto.

## Endpoints principales

Todos los endpoints de medicamentos usan el prefijo `/api/v1/medications`.

### Health check

```http
GET /healthcheck
```

Respuesta:

```json
{"status":"UP"}
```

### Obtener todos los medicamentos

```http
GET /api/v1/medications
```

### Obtener un medicamento por id

```http
GET /api/v1/medications/{id}
```

### Crear medicamento

```http
POST /api/v1/medications
Content-Type: application/json
```

Ejemplo de body:

```json
{
  "code": "MED-001",
  "medicationName": "Paracetamol",
  "totalPrice": 950,
  "status": "OPEN",
  "availableMedication": 24,
  "laboratory": "Laboratorio Chile"
}
```

### Actualizar medicamento

```http
PUT /api/v1/medications/{id}
Content-Type: application/json
```

El campo `code` es obligatorio. Los demás campos se aplican cuando son enviados.

### Eliminar medicamento

```http
DELETE /api/v1/medications/{id}
```

### Comprar tickets

```http
POST /api/v1/medications/{id}/purchase
Content-Type: application/json
```

Ejemplo:

```bash
curl -X POST http://localhost:8080/api/v1/medications/1/purchase \
  -H "Content-Type: application/json" \
  -d '{"customerEmail":"cliente@example.com","quantity":2,"medicationName":"Paracetamol"}'
```

La compra valida que la cantidad sea mayor que cero y que exista stock disponible.

### Obtener tickets de un medicamento

```http
GET /api/v1/medications/{id}/tickets
```

## Estructura del proyecto

```text
demo/
├── src/
│   ├── main/
│   │   ├── java/com/katerin/farmacia/
│   │   │   ├── application/
│   │   │   ├── domain/
│   │   │   └── infrastructure/
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── application-dev.yaml
│   │       └── static/
│   └── test/
│       ├── java/
│       └── resources/
├── compose.yml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

La organización principal sigue una separación por capas:

- `application`: servicios y casos de uso.
- `domain`: modelos y excepciones del dominio.
- `infrastructure/persistence`: entidades JPA y repositorios.
- `infrastructure/web`: controladores, DTOs y manejo HTTP.

## Swagger

La documentación Swagger está habilitada en la configuración del perfil `dev` y es accesible mientras la aplicación esté ejecutándose:

- http://localhost:8080/swagger-ui.html
- http://localhost:8080/api-docs

## Licencia

Este proyecto no tiene una licencia definida actualmente.
