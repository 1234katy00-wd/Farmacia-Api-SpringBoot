# Farmacia API

API REST para consultar medicamentos y simular compras en una farmacia. El proyecto está construido con Spring Boot y se encuentra dentro del directorio `demo/`.

## Tecnologías

- Java 25
- Spring Boot 3.3.2
- Maven
- Spring Web
- Spring Validation
- JUnit y Spring Boot Test
- JaCoCo para el reporte de cobertura

## Requisitos

- JDK 25 configurado en `JAVA_HOME`
- Windows: se puede usar `demo/mvnw.cmd` sin instalar Maven
- Linux/macOS: se puede usar `demo/mvnw`

## Ejecutar el proyecto

Desde la raíz del repositorio:

### Windows

```powershell
cd demo
\.\mvnw.cmd spring-boot:run
```

### Linux/macOS

```bash
cd demo
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

## Compilar y probar

Windows:

```powershell
cd demo
\.\mvnw.cmd clean verify
```

Linux/macOS:

```bash
cd demo
./mvnw clean verify
```

El reporte de cobertura se genera en `demo/target/site/jacoco/index.html`.

## Endpoints

### Health check

```http
GET /healthcheck
```

Respuesta:

```json
{"status":"UP"}
```

### Listar medicamentos

```http
GET /api/v1/medications
```

Ejemplo:

```bash
curl http://localhost:8080/api/v1/medications
```

La respuesta contiene los medicamentos disponibles en memoria:

```json
[
    {"id":1,"name":"Paracetamol","preice":950},
    {"id":2,"name":"Ibuprofeno","preice":1200},
    {"id":2,"name":"Amoxicilina","preice":7490}
]
```

### Consultar un medicamento

```http
GET /api/v1/medications/{id}
```

Devuelve `200 OK` si encuentra el medicamento y `404 Not Found` si no existe.

```bash
curl http://localhost:8080/api/v1/medications/1
```

### Comprar medicamentos

```http
POST /api/v1/medications/purchase
Content-Type: application/json
```

El stock actual de la simulación es de 3 unidades. Una compra que supera ese valor devuelve `422 Unprocessable Entity`.

```bash
curl -X POST http://localhost:8080/api/v1/medications/purchase \
    -H "Content-Type: application/json" \
    -d "{\"quantity\":2}"
```

Compra exitosa:

```json
{"status":200,"message":"Operación exitosa.","name":"","timestamp":"2026-01-01T12:00:00"}
```

## Estructura

```text
demo/
├── src/main/java/com/katerin/farmacia/
│   ├── application/service/       # Casos de uso y lógica de medicamentos
│   ├── domain/model/              # Modelo Medication
│   ├── domain/exception/          # Excepciones del dominio
│   └── infrastructure/web/        # Controladores, DTOs y manejo de errores
├── src/main/resources/
│   ├── application.yaml
│   └── static/index.html
└── src/test/                      # Pruebas unitarias y de integración
```

## Configuración

La configuración principal está en `demo/src/main/resources/application.yaml`. Actualmente define el nombre de la aplicación como `farmacia_api`; no requiere base de datos ni variables de entorno adicionales.

## Licencia

No se ha definido una licencia para este proyecto.
