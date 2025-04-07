# Backend - Microservicios del Proyecto E-commerce (IS2)

Este directorio alberga el código fuente de todos los microservicios que componen el backend de la plataforma E-commerce.

Para una visión general del proyecto completo (incluyendo frontend), prerrequisitos globales, estructura del monorepo y estrategia de ramificación, por favor consulta el [**README Principal**](../README.md) en la raíz del repositorio.

## 1. Arquitectura Backend

El backend sigue una **arquitectura de microservicios**, donde cada servicio se enfoca en una capacidad de negocio delimitada (Bounded Context de DDD). Los servicios están diseñados para ser desplegados y escalados independientemente.

El stack tecnológico principal es **Java 17+** y **Spring Boot 3+**, utilizando **Maven** como gestor de dependencias y build.

## 2. Patrones Arquitectónicos y de Diseño Comunes

Con el objetivo de lograr consistencia, mantenibilidad, testabilidad y escalabilidad, los microservicios dentro de este directorio siguen (o deberían seguir) los siguientes patrones y convenciones:

### 2.1. Estructura Interna del Microservicio

Cada microservicio adopta una combinación de **organización por funcionalidad (feature-based)** y **arquitectura por capas**, alineada con los principios de **Arquitectura Hexagonal (Puertos y Adaptadores)** y **Domain-Driven Design (DDD)**:

```text
backend/micro-service
├── pom.xml                     # Build (Maven)
├── README.md                   # README específico del servicio
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/ecommerce/micro-service/  # Paquete base del servicio
    │   │       ├── {feature1}/                
    │   │       │   ├── api/                      # ADAPTADOR: Driving (Web/REST) -> Entrada
    │   │       │   │   └── ProductController.java
    │   │       │   ├── application/              # CORE: Lógica de Aplicación/Casos de Uso
    │   │       │   │   ├── dto/                  # DTOs (Request/Response) para este feature
    │   │       │   │   ├── mapper/               # Mappers (MapStruct) para este feature
    │	│	│   │	├── exception/		  # Excepciones de aplicación específicas		
    │   │       │   │   └── ProductService.java   # Servicio de aplicación (orquesta)
    │   │       │   ├── domain/                   # CORE: Lógica y Modelo de Dominio
    │   │       │   │   ├── constant/             # Enums/class específicos
    │   │       │   │   ├── exception/            # Excepciones de dominio específicas
    │   │       │   │   ├── model/                # Entidades, VO's, etc.
    │   │       │   │   └── repository/           # INTERFACES de Repositorio <-- PUERTOS
    │   │       │   └── infrastructure/           # ADAPTADORES: Driven (Persistencia, etc) -> Salida
    │   │       │       ├── persistence/          # Implementación Repos (si es custom), Config específica BD
    │   │       │       └── client/               # Clientes para otros servicios si este feature los necesita
    │   │       ├── {feature2}/                 
    │   │       │   └── ...
    │   │       ├── {feature3}/                 
    │   │       │   └── ...
    │   │       ├── sharedkernel/                 # Componentes compartidos DENTRO de este servicio
    │   │       │   ├── application/              # DTOs comunes, Utils aplicados
    │   │       │   ├── domain/                   # Modelos/VOs comunes, Exceptions comunes, Utils dominio
    │   │       │   └── infrastructure/           # Configuración/Helpers infra compartidos (raro)
    │   │       ├── config/                       # Configuración global del servicio
    │   │       │   ├── GlobalExceptionHandler.java
    │   │       │   ├── ApplicationConfig.java    # (Otros beans globales)
    │   │       │   ├── AuditingConfig.java       # (@EnableMongoAuditing)
    │   │       │   └── ...
    │   │       └── CatalogServiceApplication.java  # Main class (@SpringBootApplication, @Enable...Repositories)
    │   └── resources/
    │       └── application.properties		 # Configuración externa
    └── test/
        └── java/                              # Pruebas (con estructura paralela)                              # Pruebas (Unitarias, Integración)
```

* ```domain/```: Contiene el modelo y la lógica de negocio fundamental, independiente de la tecnología externa. Define los contratos para la persistencia (interfaces de Repositorio). Es el corazón.
* ```application/```: Orquesta los casos de uso, usa los repositorios (a través de interfaces), maneja DTOs y mappers, gestiona transacciones. Es el director de orquesta.
* ```api/```: Expone los casos de uso como una API REST. Recibe peticiones HTTP, valida DTOs de entrada, llama a los servicios de aplicación y devuelve respuestas HTTP (ResponseEntity). Es la puerta de entrada web.
* ```infrastructure/```: Contiene la implementación técnica que interactúa con el exterior (bases de datos, otros servicios, colas de mensajes). Implementa las interfaces definidas en el dominio. Son los enchufes al mundo real.
* ```config/```: Configuración global de Spring para el servicio.
* ```sharedkernel/```: Para reutilizar código (VOs, DTOs base, excepciones comunes) entre las diferentes features de un mismo microservicio.

### 2.2. Principios Adicionales

* Value Objects (VOs): Se utilizan VOs (preferiblemente records Java) para encapsular tipos de datos primitivos con validaciones o lógica asociada (ej: NonBlankString, Money, NonNegativeInteger).
* DTOs con Records: Se usan records Java para los DTOs (Request/Response) por su concisión e inmutabilidad.
* Mapeo con MapStruct: Se utiliza MapStruct para generar código de mapeo eficiente entre entidades de dominio y DTOs. Se favorece un mapper común (CommonValueObjectMapper) para conversiones reutilizables (VO <-> Tipo Primitivo/DTO).
* Manejo de Excepciones: Cada servicio incluye un GlobalExceptionHandler (@RestControllerAdvice) para centralizar el mapeo de excepciones a respuestas HTTP estandarizadas (ErrorResponse DTO).
* Auditoría: Se utiliza Spring Data Auditing (@CreatedDate, @LastModifiedDate con LocalDateTime) en una BaseEntity (definida dentro de cada servicio, no compartida entre servicios).
* IDs: Se generan IDs únicos (String, usando ULID) en la capa de servicio antes de crear las entidades.
* Inyección de Dependencias: Se prefiere la inyección por constructor.
* Logging: Se utiliza SLF4J + Logback para el registro de eventos.

## 3. Stack Tecnológico Común
* Lenguaje: Java 17+
* Framework: Spring Boot 3+
* Persistencia: Spring Data MongoDB, Spring Data Redis (según necesidad del servicio)
* Build: Maven
* API: Spring Web MVC (REST Controllers)
* Mensajería: Spring AMQP (RabbitMQ)
* API Gateway: Kong (Gestiona el tráfico externo hacia los servicios)
* Mapeo: MapStruct
* Validación: Jakarta Bean Validation
* Testing: JUnit 5, Mockito, AssertJ, Testcontainers

## 4. Comunicación Inter-Servicios

* Asíncrona (Preferida para Eventos): RabbitMQ (usando Spring AMQP). Para notificar cambios de estado o eventos de dominio.
* Síncrona (Para Consultas): Llamadas REST (usando RestTemplate o WebClient de Spring) entre servicios cuando se requiere una respuesta inmediata.

## 5. Configuración y Ejecución

* Cada servicio define su puerto (server.port) y nombre (spring.application.name) en su application.properties.
* Las URIs de conexión a bases de datos (MongoDB, Redis) y brokers (RabbitMQ) también se configuran allí.
* Se recomienda encarecidamente usar Docker Compose para levantar las dependencias de infraestructura (Mongo, Redis, RabbitMQ, Kong) localmente durante el desarrollo. (Sería ideal tener un docker-compose.yml en la raíz o en backend/).
* Para ejecutar un servicio específico, navega a su directorio y utiliza los comandos de Maven o ejecútalo desde tu IDE (consulta el README de cada servicio).

## 6. Lista de Microservicios Backend

* ```catalog-service/```: Gestiona Productos, Variantes, Categorías, Marcas.
* ```cart-service/```: Gestiona Carritos de Compra (Usa Redis).
* ```security-service/```: Gestiona Autenticación y Autorización. (Implementación Pendiente)
* ```discount-service/```: Gestiona Descuentos. (Implementación Pendiente)
* ```order-service/```: Gestiona Pedidos. (Implementación Pendiente)
(Añadir/quitar según los servicios reales del proyecto)


