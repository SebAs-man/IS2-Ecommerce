# Microservicio de Catálogo (`catalog-service`)

**Contexto:** Este servicio es parte del [Monorepo E-commerce IS2](../../../README.md). Su código reside en `backend/catalog-service/`.
Consulta el [README del Backend](../README.md) para patrones arquitectónicos, stack tecnológico y convenciones comunes a todos los microservicios backend.

## 1. Descripción Específica

Este microservicio es la **fuente de verdad** para toda la información relacionada con los productos que se venden en la plataforma. Sus responsabilidades incluyen:

* Gestión de la información base de los **Productos**.
* Definición del **esquema de atributos** que determinan las variantes de cada producto (`attributeDefinitions`).
* Gestión de **Variantes** individuales y vendibles (SKU, precio, stock, imágenes, atributos específicos).
* Gestión de **Categorías** y su estructura jerárquica multi-nivel.
* Gestión de **Marcas**.
* Almacenamiento de contenido descriptivo adicional (atributos estáticos, HTML enriquecido).
* Exposición de una **API REST** para consultar y manipular todos estos datos de forma paginada y filtrada.

## 2. Arquitectura y Diseño Específico

Este servicio implementa los patrones generales del backend con las siguientes particularidades tecnológicas y de modelado:

* **Base de Datos:** **MongoDB** (elegida por su flexibilidad para el manejo de atributos y esquemas variables).
* **Modelo de Variantes:** Se utiliza el enfoque de **Colecciones Separadas**:
    * `products`: Almacena la información base del producto y el esquema de sus variantes (`attributeDefinitions`).
    * `variants`: Almacena cada unidad vendible individualmente, con su `sku`, `price`, `stock`, `images` y el mapa `definingAttributes` con los valores específicos seleccionados/sobrescritos. Cada variante referencia a su `productId`.
* **Modelo de Categorías:** Se implementa el patrón **"Array of Ancestors"** para permitir consultas eficientes de subárboles y ramas de categorías. Cada categoría almacena su `parentId` y una lista `ancestors` con su propio ID y el de todos sus ancestros.
* **Modelo de Atributos:**
    * `Product` define los atributos relevantes y sus reglas (tipo, requerido, default, etc.) en `List<ProductAttributeDefinition>`.
    * `Variant` almacena los valores concretos para los atributos que la definen o sobrescriben un default en `Map<String, Object> definingAttributes`.
    * Se usa el enum `AttributeType` para la validación de tipos de valor en el servicio.
* **IDs de Entidad:** Se generan **ULIDs** (Universally Unique Lexicographically Sortable Identifier) como `String` en la capa de servicio (`IdGenerator`) antes de crear nuevas instancias de `Product`, `Variant`, `Category`, `Brand`. Se usan como `@Id`.
* **Value Objects (VOs):** Se utilizan VOs como `NonBlankString`, `Money` (con `BigDecimal` y `Currency`), `NonNegativeInteger` para encapsular validaciones y semántica en el dominio.
* **Concurrencia:** La entidad `Variant` utiliza `@Version` para implementar **Bloqueo Optimista** y prevenir actualizaciones perdidas (ej: en `stock`).
* **Auditoría:** Se utiliza una `BaseEntity` interna (en `sharedkernel`) con campos `createdAt` y `updatedAt` de tipo `LocalDateTime`, gestionados automáticamente por Spring Data Auditing (`@EnableMongoAuditing`).

## 3. Stack Tecnológico Específico

* Java 17+
* Spring Boot 3+
* Spring Web MVC
* Spring Data MongoDB
* MapStruct (+ `mapstruct-processor`)
* Jakarta Bean Validation
* Maven
* `com.github.f4b6a3:ulid-creator` (Librería para generar ULIDs)
* SLF4J + Logback
* JUnit 5, Mockito, Testcontainers (para testing)

## 4. Configuración Específica

Revisa y configura las siguientes propiedades en `src/main/resources/application.properties` (o `.yml`):

* **`server.port`**: Puerto único para este servicio (ej: `8081`).
* **`spring.application.name=catalog-service`**
* **`spring.data.mongodb.uri`**: URI de conexión a la base de datos MongoDB (ej: `mongodb://localhost:27017/catalogdb`). Asegúrate de que la base de datos (`catalogdb` en el ejemplo) exista o que MongoDB esté configurado para crearla.
* **(Opcional) `logging.level.com.tuempresa.ecommerce.catalog=DEBUG`**: Para ver más detalles en los logs durante el desarrollo.

**Importante:** Asegúrate de que la clase principal `CatalogApplication.java` tenga la anotación `@EnableMongoAuditing` para que los campos `createdAt` y `updatedAt` funcionen.

## 5. Ejecución Local

1.  **Prerrequisitos:**
    * JDK 17+, Maven instalados.
    * **Instancia de MongoDB corriendo** y accesible en la URI configurada (se recomienda usar Docker/Docker Compose con un replica set para habilitar transacciones si fueran necesarias en el futuro, aunque para CRUD básico no son estrictamente obligatorias).
2.  **Comandos (desde la raíz del monorepo):**
    ```bash
    # Navegar al directorio del servicio
    cd backend/catalog-service
    # Limpiar y construir (genera código de MapStruct)
    mvn clean package
    # Ejecutar el JAR creado
    java -jar target/*.jar
    ```
3.  **Alternativa (Ejecución directa con Maven):**
    ```bash
    cd backend/catalog-service
    mvn spring-boot:run
    ```
4.  **Desde el IDE:** Ejecuta la clase `com.tuempresa.ecommerce.catalog.CatalogApplication`.

El servicio estará disponible en `http://localhost:{server.port}` (ej: `http://localhost:8081`).

## 6. API Endpoints Principales

La API REST se expone bajo la ruta base `/api/v1`. Consulta la documentación OpenAPI/Swagger (si está configurada, usualmente en `/swagger-ui.html`) para detalles completos.

* **Marcas:** `GET, POST /brands` | `GET, PUT, DELETE /brands/{id}` | `GET /brands/search?name=...`
* **Categorías:** `GET, POST /categories` | `GET, PUT, DELETE /categories/{id}` | `GET /categories/roots` | `GET /categories/{parentId}/subcategories` | `GET /categories/{ancestorId}/tree`
* **Productos:** `POST /products` | `GET, PUT, DELETE /products/{id}` | `GET /products?categoryId=...&brandId=...&name=...` (Endpoint de búsqueda general)
* **Variantes:** `GET /products/{productId}/variants` | `POST /products/{productId}/variants` | `GET /variants/sku/{sku}` | `PUT /variants/sku/{sku}` | `DELETE /variants/sku/{sku}` | `PATCH /variants/sku/{sku}/stock`

## 7. Estrategia de Testing

* **Pruebas Unitarias:** Ubicadas en `src/test/java`. Se usan JUnit 5 y Mockito para probar la lógica de Servicios, VOs y componentes aislados.
* **Pruebas de Integración:** Ubicadas en `src/test/java`. Se usan `@SpringBootTest`, `MockMvc`/`WebTestClient` para probar los endpoints de la API y Testcontainers (recomendado) para probar la interacción real con una base de datos MongoDB.