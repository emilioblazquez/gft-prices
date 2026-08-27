# Price Service

Resolución de la prueba técnica propuesta para el proceso de selección de GFT/Inditex.

La aplicación expone un servicio REST que obtiene el precio aplicable de un producto a partir de la fecha de aplicación, el identificador del producto y el identificador de la cadena.

Cuando existen varias tarifas aplicables para un mismo instante, se devuelve aquella con mayor prioridad.

---

# Arquitectura

La aplicación sigue una **Arquitectura Hexagonal**, manteniendo desacoplada la lógica de negocio de los detalles de infraestructura.

```text
src/main/java
├── application
│   └── usecase
├── domain
│   ├── exception
│   ├── model
│   └── repository
├── infrastructure
│   ├── entrypoint
│   │   ├── advice
│   │   ├── controller
│   │   └── mapper
│   └── persistence
│       ├── adapter
│       ├── entity
│       ├── mapper
│       └── repository
└── generated
```

## Capas

| Capa | Responsabilidad |
|------|-----------------|
| **domain** | Modelo de dominio, excepciones y puertos de salida |
| **application** | Casos de uso de la aplicación |
| **infrastructure** | Adaptadores de entrada, persistencia y mapeos |
| **generated** | Código generado automáticamente a partir de OpenAPI |

La capa de dominio contiene las reglas y conceptos propios del negocio y no depende de detalles de infraestructura.

La capa de aplicación contiene los casos de uso y coordina la ejecución de la lógica de negocio mediante los puertos definidos en el dominio.

La infraestructura contiene los adaptadores necesarios para exponer la aplicación mediante HTTP y para acceder a la persistencia.

---

# Tecnologías utilizadas

- Java 21
- Spring Boot 3
- Spring Data JPA
- H2 Database
- OpenAPI Generator (API First)
- Swagger UI
- Lombok
- MapStruct
- JUnit 5
- Mockito
- Karate
- Docker

---

# Ejecución

## Requisitos

- Java 21
- Maven 3.9+
- Docker, únicamente si se desea ejecutar la aplicación mediante contenedor

## Con Maven

Para arrancar la aplicación directamente:

```bash
mvn spring-boot:run
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

---

## Con Docker

Construcción de la imagen:

```bash
docker build -t prices .
```

Ejecución del contenedor:

```bash
docker run -p 8080:8080 prices
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

---

# Documentación de la API

La API se define mediante una especificación OpenAPI siguiendo un enfoque **API First**.

A partir de dicha especificación se genera automáticamente la interfaz de la API utilizada por el controlador.

Swagger UI está disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

La especificación OpenAPI se encuentra en:

```text
src/main/resources/openapi/prices-api.yaml
```

---


# Ejecución de los tests

El proyecto diferencia entre tests unitarios, tests de integración y tests end-to-end.

## Tests unitarios e integración

Para ejecutar los tests unitarios y de integración:

```bash
mvn clean verify
```

Los tests unitarios validan de forma aislada la lógica de negocio y diferentes componentes de infraestructura.

Los tests de integración levantan el contexto completo de Spring y permiten validar el comportamiento de la aplicación y sus componentes de infraestructura de forma integrada.

---

## Tests E2E

Los tests end-to-end están implementados con **Karate** y validan el comportamiento de la API mediante peticiones HTTP reales.

Los escenarios cubren las cinco casuísticas de precio indicadas en el enunciado, además de casos de error relacionados con parámetros incorrectos y ausencia de un precio aplicable.

### Ejecutar los E2E localmente

Primero es necesario arrancar la aplicación:

```bash
mvn spring-boot:run
```

Con la aplicación disponible en:

```text
http://localhost:8080
```

ejecutar los tests E2E:

```bash
mvn test -Dtest=PricesE2ETest
```

Los escenarios Karate se encuentran en:

```text
src/test/resources/com/gft/prices/e2e/prices.feature
```

---

## Ejecutar los E2E contra Docker

También es posible ejecutar los tests E2E contra una instancia de la aplicación ejecutándose dentro de Docker.

Construir la imagen:

```bash
docker build -t prices .
```

Arrancar el contenedor:

```bash
docker run -p 8080:8080 prices
```

Con el contenedor en ejecución, ejecutar los tests:

```bash
mvn test -Dtest=PricesE2ETest
```

Los tests realizarán las peticiones HTTP contra la aplicación disponible en `http://localhost:8080`.

---

# Ejemplos de uso

## Consulta de precio

Petición:

```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-14T16:00:00Z&productId=35455&brandId=1"
```

Respuesta:

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00Z",
  "endDate": "2020-06-14T18:30:00Z",
  "price": 25.45,
  "currency": "EUR"
}
```

---

# Gestión de errores

La aplicación dispone de un manejador global de excepciones que transforma los errores de la API en respuestas HTTP utilizando `ProblemDetail`.

Entre los casos gestionados se encuentran:

- Precio no encontrado → `404 Not Found`
- Parámetros obligatorios ausentes → `400 Bad Request`
- Parámetros con un tipo incorrecto → `400 Bad Request`

Ejemplo de respuesta cuando no existe un precio aplicable:

```json
{
  "type": "...",
  "title": "Price not found",
  "status": 404,
  "detail": "No applicable price found for brandId=1, productId=99999 and applicationDate=2020-06-14T10:00",
  "instance": "/prices"
}
```

---

# Decisiones de diseño

Durante el desarrollo se han tomado las siguientes decisiones:

- Arquitectura Hexagonal para desacoplar la lógica de negocio de la infraestructura.
- Interfaces de los casos de uso para evitar que los controladores dependan directamente de sus implementaciones.
- Puertos de salida definidos en la capa de dominio para mantener el dominio independiente de la infraestructura.
- Desarrollo siguiendo un enfoque **API First**, generando la interfaz REST a partir de la especificación OpenAPI.
- Base de datos H2 en memoria inicializada automáticamente mediante scripts SQL.
- Selección del precio aplicable mediante una consulta optimizada que filtra por producto, cadena y rango temporal y ordena por prioridad descendente.
- Cuando existen varias tarifas aplicables para un mismo instante, se selecciona la de mayor prioridad.
- Gestión centralizada de excepciones mediante `ProblemDetail`.
- MapStruct para el mapeo entre modelos de dominio y DTOs, evitando código de mapeo manual.
- Tests unitarios para la lógica de negocio y adaptadores.
- Tests de integración para validar el comportamiento de la aplicación dentro del contexto Spring.
- Tests end-to-end implementados con Karate y ejecutados mediante peticiones HTTP.
- Imagen Docker construida mediante **multi-stage build**, separando la fase de compilación de la imagen final de ejecución.

---

# Hipótesis adoptadas

- Cuando existen varias tarifas aplicables para un mismo instante, prevalece la de mayor prioridad.
- Las fechas se exponen en la API REST utilizando UTC (`OffsetDateTime`).
- Los datos de ejemplo proporcionados en el enunciado se cargan automáticamente durante el arranque de la aplicación.