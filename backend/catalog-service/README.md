# Microservicio de Catálogo (Proyecto e-commerce IS2)

## Descripción
Es un microservicio cuya responsabilidad principal es gestionar toda la información relacionada con los productos del catálogo, incluyendo sus categorías y atributos dinámicos. Proporciona una API REST para operaciones CRUD y consultas sobre productos y categorías.


## Arquitectura y diseño
Este microservicio se ha diseñado siguiendo varios principios y patrones clave para buscar organización, mantenibilidad y escalabilidad:
* **Arquitectura de Microservicios:** Diseñado para funcionar como una unidad independiente dentro de un sistema de e-commerce más grande.
* **Estructura por Funcionalidad (Feature-based):** El código se organiza en paquetes por capacidad de negocio (ej.: `product`, `category`) para mejorar la cohesión y reducir el acoplamiento entre funcionalidades.
* **Capas Arquitectónicas (Dentro de cada Feature):** Cada funcionalidad sigue una separación por capas:
    * `api`: Controladores REST y exposición de la API.
    * `application`: Orquestación de casos de uso, DTO, Mappers, Servicios de Aplicación.
    * `domain`: El núcleo del negocio. Entidades, Value Objects, Interfaces de Repositorio, lógica y reglas de negocio fundamentales.
    * `infrastructure`: Implementaciones técnicas (ej.: acceso a BD, clientes externos).
* **Principios DDD (Domain-Driven Design) Ligeros:**
    * Uso de **Value Objects** (ej.: `Attribute`) para representar conceptos inmutables con sus propias validaciones (usando Métodos Factoría Estáticos).
    * Uso de **Entidades** (ej.: `Product`, `Category`, `BaseEntity`) con identidad y ciclo de vida.
    * Encapsulación de reglas de negocio dentro del dominio (ej: validación en `Attribute.createValidatedAttribute`).
* **Persistencia NoSQL:** Se eligió **MongoDB** como base de datos principal debido a la necesidad de flexibilidad para manejar los atributos dinámicos y variados de los productos.
* **API REST:** Exposición de funcionalidades a través de una API REST usando Spring Web MVC.
* **Manejo de Excepciones Global:** Se utiliza `@RestControllerAdvice` para centralizar el manejo de excepciones y devolver respuestas de error consistentes en formato JSON.
* **Estrategia de ID:** Se utiliza el `ObjectId` de MongoDB como identificador interno (`_id`), mapeado a `String` en Java. *(Nota: Se consideró usar slugs para las URL de API, pero se pospuso por simplicidad inicial).*
* **Inmutabilidad:** Se favorece la inmutabilidad donde tiene sentido (Value Objects, Records para DTO) para mejorar la predictibilidad y seguridad.
* **Encapsulamiento:** Se protegen las colecciones internas de las entidades devolviendo vistas inmodificables en los getters.

## Tecnologías Utilizadas

* **Lenguaje:** Java 23
* **Framework Principal:** Spring Boot 3.4.4
* **Persistencia:** Spring Data MongoDB
* **Base de Datos:** MongoDB (v8.0.6)
* **Mapeo:** MapStruct (para conversión DTO <-> Dominio)
* **Validación:** Jakarta Bean Validation (anotaciones en DTO)
* **Logging:** SLF4J (fachada) + Logback (implementación por defecto en Spring Boot)
* **Build:** Maven / Gradle (elige el que uses)
* **Testing (Planeado):** JUnit 5, Mockito, Testcontainers (para pruebas de integración con MongoDB)
* **Control de Versiones:** Git

## Estructura del Proyecto

El proyecto sigue una estructura de paquetes orientada a funcionalidades (features):
```
.
├── .gitignore
├── pom.xml    # Archivo de construcción
├── README.md                  # Este archivo
└── src
    ├── main
    │   ├── java
    │   │   └── com/ecommerce/catalog/  # Raíz del código del microservicio
    │   │       ├── product/                      # Feature: Producto
    │   │       │   ├── api/                      # Controladores REST (ProductController)
    │   │       │   ├── application/              # Lógica de Aplicación
    │   │       │   │   ├── dto/                  # DTOs (Records: AttributeDTO, ProductDTOs)
    │   │       │   │   ├── mapper/               # Mappers (ProductMapper)
    │   │       │   │   └── ProductApplicationService.java # Servicio de Aplicación
    │   │       │   ├── domain/                   # Lógica y Modelo de Dominio
    │   │       │   │   ├── constant/             # Enums, Constantes (AttributeKey, AttributeType)
    │   │       │   │   ├── exception/            # Excepciones específicas del dominio (si las hay)
    │   │       │   │   ├── model/                # Entidades (Product), VOs (Attribute)
    │   │       │   │   └── repository/           # Interfaces de Repositorio (ProductRepository)
    │   │       │   └── infrastructure/           # Implementaciones Técnicas
    │   │       │       └── persistence/          # (Implementación de repo si es custom)
    │   │       ├── category/                     # Feature: Categoría (estructura similar)
    │   │       │   └── ...
    │   │       ├── sharedkernel/                 # Código compartido entre features del catálogo
    │   │       │   ├── domain/
    │   │       │   │   └── model/                # (BaseEntity)
    │   │       │   ├── exceptions/               # (ResourceNotFoundException)
    │   │       │   └── application/dto/          # (ErrorResponse)
    │   │       ├── config/                       # Configuración global del microservicio
    │   │       │   ├── GlobalExceptionHandler.java
    │   │       │   ├── MongoConfig.java          # (Configuración específica de Mongo si es necesaria)
    │   │       │   └── ...                       # (Config Audit, Security si aplica aquí)
    │   │       └── CatalogApplication.java       # Punto de entrada Spring Boot
    │   └── resources
    │       ├── application.properties            # Configuración principal (BD, puerto, etc.)
    │       └── logback-spring.xml                # (Opcional: Configuración avanzada de logging)
    └── test
```

* **`api/`**: Contiene los `@RestController` que definen los endpoints HTTP y manejan la comunicación request/response. Delega en la capa de aplicación.
* **`application/`**: Orquesta los casos de uso. Contiene los Servicios de Aplicación (`@Service`), los DTO (Data Transfer Objects - usados para la API), y los Mappers (para convertir entre Dominio y DTO). No contiene lógica de negocio fundamental.
* **`domain/`**: El corazón. Contiene las Entidades, Value Objects, constantes/enums del negocio, interfaces de Repositorio y la lógica de negocio fundamental (reglas, invariantes, validaciones de dominio). Debe ser independiente de la tecnología externa.
* **`infrastructure/`**: Implementaciones concretas de interfaces del dominio o la aplicación que dependen de tecnología externa (ej.: implementación de repositorio si no basta Spring Data, clientes HTTP, etc.).
* **`sharedkernel/`**: Código (clases base, excepciones comunes, DTO comunes) que es compartido por *múltiples features* dentro de *este mismo microservicio*.
* **`config/`**: Clases de configuración globales para el microservicio (Beans de Spring, seguridad, manejo de excepciones global, configuración de auditoría).

## Configuración

Las configuraciones principales se encuentran en `src/main/resources/application.properties`. Las claves más importantes son:

* `server.port`: Puerto donde corre el servicio (ej.: `9000`).
* `spring.data.mongodb.uri`: URI de conexión a la base de datos MongoDB.
* `logging.level.*`: Niveles de log para diferentes paquetes.

## Ejecución Local

1. **Prerrequisitos:**
  * JDK 17+ instalado.
  * Maven instalado.
  * Una instancia de MongoDB corriendo y accesible (local o remota).
2. **Configuración:** Ajusta la URI de MongoDB en `application.properties`.
3. **Construcción:**
  * Maven: `mvn clean package`
4. **Ejecución:**
  * Desde la línea de comandos: `java -jar target/catalog-service-0.0.1-SNAPSHOT.jar` (ajusta el nombre del JAR)
  * Desde el IDE: Ejecuta la clase `CatalogApplication`.

El servicio estará disponible en `http://localhost:{server.port}`.

## API Endpoints
La API REST principal se encuentra bajo la ruta base `/api/v1/products`. Los endpoints principales son:

* `POST /api/v1/products`: Crea un nuevo producto.
* `GET /api/v1/products/{id}`: Obtiene un producto por su ID (ObjectId).
* `GET /api/v1/products`: Obtiene una lista de todos los productos (añadir paginación/filtros).
* `PUT /api/v1/products/{id}`: Actualiza un producto existente por su ID.
* `DELETE /api/v1/products/{id}`: Elimina un producto por su ID.
