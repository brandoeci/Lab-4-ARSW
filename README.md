## Laboratorio #4 – REST API Blueprints (Java 21 / Spring Boot 3.3.x)
# Escuela Colombiana de Ingeniería – Arquitecturas de Software  

---

## 📋 Requisitos
- Java 21
- Maven 3.9+

## ▶️ Ejecución del proyecto
```bash
mvn clean install
mvn spring-boot:run
```
Probar con `curl`:
```bash
curl -s http://localhost:8080/blueprints | jq
curl -s http://localhost:8080/blueprints/john | jq
curl -s http://localhost:8080/blueprints/john/house | jq
curl -i -X POST http://localhost:8080/blueprints -H 'Content-Type: application/json' -d '{ "author":"john","name":"kitchen","points":[{"x":1,"y":1},{"x":2,"y":2}] }'
curl -i -X PUT  http://localhost:8080/blueprints/john/kitchen/points -H 'Content-Type: application/json' -d '{ "x":3,"y":3 }'
```

> Si deseas activar filtros de puntos (reducción de redundancia, *undersampling*, etc.), implementa nuevas clases que implementen `BlueprintsFilter` y cámbialas por `IdentityFilter` con `@Primary` o usando configuración de Spring.
---

Abrir en navegador:  
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)  

---

## 🗂️ Estructura de carpetas (arquitectura)

```
src/main/java/edu/eci/arsw/blueprints
  ├── model/         # Entidades de dominio: Blueprint, Point
  ├── persistence/   # Interfaz + repositorios (InMemory, Postgres)
  │    └── impl/     # Implementaciones concretas
  ├── services/      # Lógica de negocio y orquestación
  ├── filters/       # Filtros de procesamiento (Identity, Redundancy, Undersampling)
  ├── controllers/   # REST Controllers (BlueprintsAPIController)
  └── config/        # Configuración (Swagger/OpenAPI, etc.)
```

> Esta separación sigue el patrón **capas lógicas** (modelo, persistencia, servicios, controladores), facilitando la extensión hacia nuevas tecnologías o fuentes de datos.

---

## 📖 Actividades del laboratorio

### 1. Familiarización con el código base
- Revisa el paquete `model` con las clases `Blueprint` y `Point`.  
- Entiende la capa `persistence` con `InMemoryBlueprintPersistence`.  
- Analiza la capa `services` (`BlueprintsServices`) y el controlador `BlueprintsAPIController`.

### 2. Migración a persistencia en PostgreSQL
- Configura una base de datos PostgreSQL (puedes usar Docker).  
- Implementa un nuevo repositorio `PostgresBlueprintPersistence` que reemplace la versión en memoria.  
- Mantén el contrato de la interfaz `BlueprintPersistence`.  

### 3. Buenas prácticas de API REST
- Cambia el path base de los controladores a `/api/v1/blueprints`.  
- Usa **códigos HTTP** correctos:  
  - `200 OK` (consultas exitosas).  
  - `201 Created` (creación).  
  - `202 Accepted` (actualizaciones).  
  - `400 Bad Request` (datos inválidos).  
  - `404 Not Found` (recurso inexistente).  
- Implementa una clase genérica de respuesta uniforme:
  ```java
  public record ApiResponse<T>(int code, String message, T data) {}
  ```
  Ejemplo JSON:
  ```json
  {
    "code": 200,
    "message": "execute ok",
    "data": { "author": "john", "name": "house", "points": [...] }
  }
  ```

### 4. OpenAPI / Swagger
- Configura `springdoc-openapi` en el proyecto.  
- Expón documentación automática en `/swagger-ui.html`.  
- Anota endpoints con `@Operation` y `@ApiResponse`.

### 5. Filtros de *Blueprints*
- Implementa filtros:
  - **RedundancyFilter**: elimina puntos duplicados consecutivos.  
  - **UndersamplingFilter**: conserva 1 de cada 2 puntos.  
- Activa los filtros mediante perfiles de Spring (`redundancy`, `undersampling`).  

---

## ✅ Entregables

1. Repositorio en GitHub con:  
   - Código fuente actualizado.  
   - Configuración PostgreSQL (`application.yml` o script SQL).  
   - Swagger/OpenAPI habilitado.  
   - Clase `ApiResponse<T>` implementada.  

2. Documentación:  
   - Informe de laboratorio con instrucciones claras.  
   - Evidencia de consultas en Swagger UI y evidencia de mensajes en la base de datos.  
   - Breve explicación de buenas prácticas aplicadas.  

---

## 📊 Criterios de evaluación

| Criterio | Peso |
|----------|------|
| Diseño de API (versionamiento, DTOs, ApiResponse) | 25% |
| Migración a PostgreSQL (repositorio y persistencia correcta) | 25% |
| Uso correcto de códigos HTTP y control de errores | 20% |
| Documentación con OpenAPI/Swagger + README | 15% |
| Pruebas básicas (unitarias o de integración) | 15% |

**Bonus**:  

- Imagen de contenedor (`spring-boot:build-image`).  
- Métricas con Actuator.
- <img width="1010" height="809" alt="image" src="https://github.com/user-attachments/assets/12ec28d4-9fdd-4aab-9144-98b48f1b13a3" />


  <img width="1515" height="923" alt="image" src="https://github.com/user-attachments/assets/f2b5c6b4-0771-4a31-9aab-c2adb3e80143" />
  <img width="1653" height="807" alt="image" src="https://github.com/user-attachments/assets/c7e5b0cb-88bc-471d-bf35-69d54072abd3" />
  <img width="1566" height="850" alt="image" src="https://github.com/user-attachments/assets/a4c22d50-94b5-4629-8211-7b6dac1a2a1d" />
  <img width="936" height="769" alt="image" src="https://github.com/user-attachments/assets/1bbbeab5-ecd9-4be5-9132-c6afa3fab254" />




  <img width="1549" height="410" alt="image" src="https://github.com/user-attachments/assets/ced3d353-06d9-4fff-84fa-54e552c48b84" />
  <img width="474" height="694" alt="image" src="https://github.com/user-attachments/assets/dd923be4-432d-4386-ae9e-7dd3c84f659f" />


  <img width="1804" height="827" alt="image" src="https://github.com/user-attachments/assets/e51860dd-19c8-45bc-a051-afba858b8ab9" />
  <img width="1799" height="656" alt="image" src="https://github.com/user-attachments/assets/46b8d465-bd15-4fc8-95ea-040dca686509" />



  <img width="1774" height="102" alt="image" src="https://github.com/user-attachments/assets/44bd2080-7fe6-47a2-965d-2b8c7a9de840" />
  <img width="1804" height="462" alt="image" src="https://github.com/user-attachments/assets/a073da04-a509-4501-8b03-a3aa05d4b43a" />

<img width="1325" height="80" alt="image" src="https://github.com/user-attachments/assets/04799f70-64d4-4269-a2f5-dab0248490cf" />
<img width="1803" height="367" alt="image" src="https://github.com/user-attachments/assets/bda08869-aecf-4088-b927-da8ccd89efbf" />

<img width="1809" height="794" alt="image" src="https://github.com/user-attachments/assets/c3f39f42-8f45-4832-bc21-c484aae2832a" />


# Laboratorio #4 – REST API Blueprints (Java 21 / Spring Boot 3.3.x)

**Escuela Colombiana de Ingeniería – Arquitecturas de Software**

API REST para gestión de planos (*blueprints*) y sus puntos, migrada a persistencia en PostgreSQL, con versionamiento de API, respuestas uniformes, manejo centralizado de errores, documentación OpenAPI/Swagger, filtros de procesamiento de puntos y pruebas de integración con Testcontainers.

---

## 📋 Tabla de contenido

1. [Arquitectura](#-arquitectura)
2. [Requisitos](#-requisitos)
3. [Cómo ejecutar](#-cómo-ejecutar)
4. [Endpoints de la API](#-endpoints-de-la-api)
5. [Swagger / OpenAPI](#-swagger--openapi)
6. [Evidencia en base de datos](#-evidencia-en-base-de-datos)
7. [Filtros de blueprints](#-filtros-de-blueprints)
8. [Pruebas](#-pruebas)
9. [Buenas prácticas aplicadas](#-buenas-prácticas-aplicadas)
10. [Problemas resueltos durante el desarrollo](#-problemas-resueltos-durante-el-desarrollo)

---

## 🏗️ Arquitectura

El proyecto sigue una separación en capas lógicas:

```
src/main/java/edu/eci/arsw/blueprints
  ├── model/         # Entidades de dominio: Blueprint, Point
  ├── persistence/   # Interfaz BlueprintPersistence + implementaciones
  │    ├── InMemoryBlueprintPersistence   (perfil: !postgres, por defecto)
  │    └── PostgresBlueprintPersistence   (perfil: postgres)
  ├── services/      # BlueprintsServices: lógica de negocio y aplicación de filtros
  ├── filters/       # IdentityFilter, RedundancyFilter, UndersamplingFilter
  ├── controllers/   # BlueprintsAPIController + ApiExceptionHandler
  ├── dto/           # ApiResponse<T>: envoltorio uniforme de respuestas
  └── config/        # OpenApiConfig (Swagger)
```

**Por qué esta separación:** cada capa depende solo de la interfaz de la capa inferior, no de su implementación concreta. Esto permite cambiar de memoria a PostgreSQL (o agregar un tercer tipo de persistencia, como MongoDB) sin tocar `services` ni `controllers` — solo se agrega una nueva clase y se activa con `@Profile`.

**Cómo conviven dos implementaciones del mismo contrato sin conflicto:**
`InMemoryBlueprintPersistence` tiene `@Profile("!postgres")` y `PostgresBlueprintPersistence` tiene `@Profile("postgres")`. Spring nunca activa ambos beans al mismo tiempo, así que `BlueprintsServices` siempre recibe exactamente uno por inyección de dependencias. El mismo patrón se usa con los filtros (`IdentityFilter` se desactiva cuando `redundancy` o `undersampling` están activos).

---

## ⚙️ Requisitos

- Java 21
- Maven 3.9+
- Docker Desktop (para el perfil `postgres` y para las pruebas de integración con Testcontainers)

---

## ▶️ Cómo ejecutar

### Modo por defecto (persistencia en memoria, sin Docker)

```powershell
mvn clean install
mvn spring-boot:run
```

### Modo con PostgreSQL

**1. Levantar la base de datos:**

```powershell
docker compose up -d
```

> **Nota:** el `docker-compose.yml` mapea PostgreSQL al puerto **5433** del host (no 5432), para evitar conflicto con otros contenedores de Postgres que puedan estar corriendo en la máquina.

**2. Activar el perfil y ejecutar:**

```powershell
$env:SPRING_PROFILES_ACTIVE="postgres"
mvn spring-boot:run
```

Al arrancar, Spring Boot ejecuta automáticamente `schema.sql` y `data.sql` contra la base de datos (creación de tablas + datos semilla), gracias a `spring.sql.init.mode=always`.

### Combinar con filtros

```powershell
$env:SPRING_PROFILES_ACTIVE="postgres,redundancy"
# o
$env:SPRING_PROFILES_ACTIVE="postgres,undersampling"
mvn spring-boot:run
```

---

## 🔌 Endpoints de la API

Base path: **`/api/v1/blueprints`**

Todas las respuestas se envuelven en el DTO uniforme:

```json
{ "code": 200, "message": "execute ok", "data": { ... } }
```

| Método | Endpoint | Código éxito | Código error | Descripción |
|---|---|---|---|---|
| GET | `/api/v1/blueprints` | 200 | — | Lista todos los blueprints |
| GET | `/api/v1/blueprints/{author}` | 200 | 404 | Blueprints de un autor |
| GET | `/api/v1/blueprints/{author}/{bpname}` | 200 | 404 | Un blueprint específico |
| POST | `/api/v1/blueprints` | 201 | 400 | Crea un blueprint nuevo |
| PUT | `/api/v1/blueprints/{author}/{bpname}/points` | 202 | 404 | Agrega un punto a un blueprint existente |

### Ejemplos `curl`

```powershell
curl.exe -i http://localhost:8080/api/v1/blueprints
curl.exe -i http://localhost:8080/api/v1/blueprints/john/house
curl.exe -i http://localhost:8080/api/v1/blueprints/nadie

curl.exe -i -X POST http://localhost:8080/api/v1/blueprints -H "Content-Type: application/json" -d '{ \"author\":\"pedro\",\"name\":\"taller\",\"points\":[{\"x\":5,\"y\":5}] }'

curl.exe -i -X PUT http://localhost:8080/api/v1/blueprints/pedro/taller/points -H "Content-Type: application/json" -d '{ \"x\":10,\"y\":10 }'
```


  <img width="1774" height="102" alt="image" src="https://github.com/user-attachments/assets/44bd2080-7fe6-47a2-965d-2b8c7a9de840" />
  <img width="1804" height="462" alt="image" src="https://github.com/user-attachments/assets/a073da04-a509-4501-8b03-a3aa05d4b43a" />


---

## 📖 Swagger / OpenAPI

URL: `http://localhost:8080/swagger-ui.html`

Cada endpoint está documentado con `@Operation` (resumen) y `@ApiResponses` (códigos de respuesta posibles con su descripción).

**Vista general de los endpoints:**

<img width="1359" height="916" alt="image" src="https://github.com/user-attachments/assets/5044556a-2d2b-488c-8cfe-45d576940737" />

**Detalle de un endpoint expandido (mostrando los códigos de respuesta documentados):**

<img width="940" height="769" alt="image" src="https://github.com/user-attachments/assets/cb4cc5d2-9674-4e45-b2ec-e8fbbc5d222c" />

---

## 🗄️ Evidencia en base de datos

Con la app corriendo en perfil `postgres`, y tras hacer algunos POST/PUT vía `curl`:

```powershell
docker exec -it lab_p1_blueprints_java21_api-postgres-1 psql -U arsw -d blueprints -c "SELECT * FROM blueprints;"
docker exec -it lab_p1_blueprints_java21_api-postgres-1 psql -U arsw -d blueprints -c "SELECT * FROM points;"
```

  <img width="1549" height="410" alt="image" src="https://github.com/user-attachments/assets/ced3d353-06d9-4fff-84fa-54e552c48b84" />

    <img width="474" height="694" alt="image" src="https://github.com/user-attachments/assets/dd923be4-432d-4386-ae9e-7dd3c84f659f" />


>
---

## 🧪 Filtros de blueprints

Implementados en el paquete `filters/`, todos implementan la interfaz `BlueprintsFilter`. Se aplican únicamente al consultar un blueprint individual (`GET /{author}/{bpname}`), no al listarlos todos ni al guardarlos — el filtro es una transformación de **lectura**, los datos guardados nunca se modifican.

| Filtro | Perfil | Comportamiento |
|---|---|---|
| `IdentityFilter` | (ninguno activo) | Devuelve el blueprint sin cambios |
| `RedundancyFilter` | `redundancy` | Elimina puntos consecutivos duplicados |
| `UndersamplingFilter` | `undersampling` | Conserva 1 de cada 2 puntos (índices pares) |

### Evidencia `RedundancyFilter`

```powershell
curl.exe -i -X POST http://localhost:8080/api/v1/blueprints -H "Content-Type: application/json" -d '{ \"author\":\"captura\",\"name\":\"redundancy\",\"points\":[{\"x\":1,\"y\":1},{\"x\":1,\"y\":1},{\"x\":2,\"y\":2}] }'
curl.exe -i http://localhost:8080/api/v1/blueprints/captura/redundancy
```

> **[PEGAR CAPTURA DE PANTALLA AQUÍ]**

Se guardan 3 puntos, pero la consulta devuelve solo 2 (el duplicado `(1,1)` se filtra al leer).

### Evidencia `UndersamplingFilter`

```powershell
curl.exe -i -X POST http://localhost:8080/api/v1/blueprints -H "Content-Type: application/json" -d '{ \"author\":\"captura\",\"name\":\"undersample\",\"points\":[{\"x\":0,\"y\":0},{\"x\":1,\"y\":1},{\"x\":2,\"y\":2},{\"x\":3,\"y\":3},{\"x\":4,\"y\":4}] }'
curl.exe -i http://localhost:8080/api/v1/blueprints/captura/undersample
```

> **[PEGAR CAPTURA DE PANTALLA AQUÍ]**

Se guardan 5 puntos, la consulta devuelve solo 3 (índices pares: 0, 2, 4).


---

## ✅ Pruebas

```powershell
mvn clean test
```

> **[PEGAR CAPTURA DE PANTALLA AQUÍ]**

**Cobertura de pruebas:**

- `BlueprintsSmokeTest`: verifica que el contexto de Spring carga correctamente en modo por defecto (sin tocar Postgres).
- `PostgresBlueprintPersistenceIntegrationTest` (8 casos): usa **Testcontainers** para levantar un PostgreSQL real y efímero, y valida contra él:
  - Lectura de los blueprints semilla.
  - Orden correcto de los puntos.
  - Excepciones al buscar un blueprint o autor inexistente.
  - Inserción y lectura de un blueprint nuevo.
  - Rechazo de blueprints duplicados (constraint `UNIQUE(author, name)`).
  - Que `addPoint` preserva el orden correcto de los puntos.
  - Excepción al agregar un punto a un blueprint inexistente.


---

## 🛠️ Buenas prácticas aplicadas

- **Versionamiento de API** (`/api/v1/...`): permite introducir cambios incompatibles en una v2 futura sin romper a los clientes que sigan usando v1.
- **DTO de respuesta uniforme (`ApiResponse<T>`):** todas las respuestas (éxito o error) tienen la misma forma (`code`, `message`, `data`), lo que simplifica el consumo desde el cliente.
- **Manejo centralizado de errores (`@RestControllerAdvice`):** los controllers quedan limpios, sin `try/catch`; la traducción de excepciones a códigos HTTP vive en un solo lugar.
- **Separación por perfiles de Spring (`@Profile`):** permite alternar entre implementaciones (memoria/Postgres, filtros) sin condicionales en el código, solo cambiando configuración externa.
- **Validación declarativa (`@Valid`, `@NotBlank`):** las reglas de validación viven en el DTO de entrada, no en el cuerpo del controller.
- **Pruebas de integración contra una base real (Testcontainers):** en vez de mockear el `JdbcTemplate`, las pruebas validan SQL real contra un PostgreSQL real, detectando problemas que un mock no vería (por ejemplo, errores de sintaxis SQL o de constraints).

---

## 🐛 Problemas resueltos durante el desarrollo

Documentados aquí porque son parte del aprendizaje real del laboratorio, no solo el código final:

1. **Conflicto de puerto 5432:** ya había otro contenedor de PostgreSQL corriendo en la máquina. Solución: remapear el `docker-compose.yml` a `5433:5432`.

2. **`curl` en PowerShell:** PowerShell tiene un alias de `curl` que apunta a `Invoke-WebRequest`, incompatible con la sintaxis estándar de curl. Solución: usar `curl.exe` explícitamente.

3. **Ambigüedad de beans (`InMemoryBlueprintPersistence` vs `PostgresBlueprintPersistence`, e `IdentityFilter` vs los demás filtros):** Spring no permite dos beans del mismo tipo sin desambiguar. Solución: anotaciones `@Profile` mutuamente excluyentes (`!postgres`, `!redundancy & !undersampling`).

4. **Incompatibilidad Testcontainers / Docker Engine 29:** Docker Engine 29 exige API mínima `1.44`, pero la versión de Testcontainers heredada del BOM de Spring Boot 3.3.9 negocia con una versión más antigua, causando `BadRequestException (Status 400)`. Solución: forzar `<testcontainers.version>1.21.3</testcontainers.version>` en el `pom.xml` y crear `src/test/resources/docker-java.properties` con `api.version=1.44`.

---









