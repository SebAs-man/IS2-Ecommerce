# Backend Microservices (E-commerce IS2)

Este directorio contiene todos los microservicios que componen el backend de la plataforma de e-commerce.

Consulta el [README Principal](../../README.md) para una visión general del proyecto completo, prerrequisitos globales y estrategia de ramificación.

## Arquitectura y Patrones Comunes

El backend sigue una arquitectura de microservicios implementada con Spring Boot. Cada microservicio dentro de este directorio (`catalog-service`, `cart-service`, etc.) sigue, en general, los siguientes patrones:

* **Estructura por Funcionalidad (Feature-based):** Dentro de cada microservicio, el código se organiza en paquetes por capacidad de negocio (ej: `product`, `category` dentro de `catalog-service`).
* **Capas Arquitectónicas (Dentro de cada Feature/Servicio):**
    * `api`: Controladores REST (`@RestController`) y exposición de la API. Define el contrato HTTP.
    * `application`: Orquestación de casos de uso (`@Service`), DTOs (se usan `records` Java), Mappers (MapStruct). Lógica de aplicación, validación de entrada DTO, transacciones.
    * `domain`: El núcleo del negocio. Entidades (`@Document`), Value Objects (`record` o clases inmutables), constantes/enums, *interfaces* de Repositorio, excepciones de dominio, lógica de negocio fundamental.
    * `infrastructure`: Implementaciones técnicas. Configuración de persistencia específica, clientes para otros servicios, etc. (Con Spring Data, la implementación del repositorio es a menudo implícita).
    * `config`: Configuración global del microservicio (Beans, Seguridad, Excepciones Globales, Auditoría).
    * `sharedkernel`: Componentes (clases base, VOs, DTOs comunes como `ErrorResponse`, excepciones) compartidos *dentro* del mismo microservicio entre sus features. *(NO confundir con librerías compartidas entre microservicios, que irían en `/libs`)*.
* **Principios DDD Ligeros:** Uso de Entidades, Value Objects, separación clara entre capas.
* **API RESTful:** Exposición de funcionalidades mediante JSON sobre HTTP.
* **Manejo de Errores:** Uso de `GlobalExceptionHandler` (`@RestControllerAdvice`) para respuestas de error consistentes.
* **Validación:** Anotaciones de Jakarta Bean Validation en DTOs de Request, validaciones de dominio en entidades/VOs/servicios.
* **Persistencia:** Spring Data (MongoDB, Redis según el servicio).

## Stack Tecnológico Común (Backend)

* Java 17+
* Spring Boot 3+
* Spring Web MVC / WebFlux (según el servicio)
* Spring Data MongoDB / Redis
* Maven (Gestor de dependencias y build)
* MapStruct (Mapeo Objeto-DTO)
* SLF4J + Logback (Logging)
* JUnit 5, Mockito, Testcontainers (Testing)

## Comunicación Inter-Servicios

La comunicación entre microservicios se realiza principalmente mediante:

* **Llamadas REST Síncronas:** (Usando `RestTemplate` o `WebClient`) para consultas directas.
* **Mensajería Asíncrona (RabbitMQ):** Para publicar/consumir eventos de dominio entre servicios (ej: `ProductoCreado`, `StockActualizado`).

Un **API Gateway (Kong)** actúa como punto de entrada único para las peticiones externas (frontends, apps móviles).

## Configuración y Ejecución General

* Cada microservicio tiene su propio archivo `application.properties` (o `.yml`) dentro de su directorio `src/main/resources`.
* Es **crucial** configurar correctamente las **conexiones a bases de datos** (MongoDB URI, Redis Host/Port) y el **puerto del servidor (`server.port`)** para cada servicio para evitar conflictos al ejecutar localmente.
* Se recomienda usar **Docker y Docker Compose** para levantar las bases de datos (Mongo, Redis), RabbitMQ y Kong localmente de forma sencilla. (Un archivo `docker-compose.yml` en la raíz del monorepo o en `backend/` sería ideal).

Consulta el `README.md` específico de cada microservicio para instrucciones detalladas de configuración y ejecución.

## Microservicios Backend

* [**catalog-service/**](./catalog-service/README.md): Gestiona productos, variantes, categorías, marcas.
* [**cart-service/**](./cart-service/README.md): Gestiona los carritos de compra de los usuarios. (Pendiente)
* [**security-service/**](./security-service/README.md): Gestiona autenticación y autorización. (Pendiente)
* [**discount-service/**](./discount-service/README.md): Gestiona descuentos y promociones. (Pendiente)
* [**order-service/**](./order-service/README.md): Gestiona el proceso de pedidos. (Pendiente)
* *(Añadir otros si existen)*
