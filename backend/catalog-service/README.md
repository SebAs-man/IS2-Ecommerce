# Microservicio de Catálogo (`catalog-service`)

**Contexto:** Este servicio es parte del [Monorepo E-commerce](../../../README.md) y su código reside en `backend/catalog-service/`. Consulta el [README del Backend](../README.md) para patrones y tecnologías comunes.

## Descripción

Este microservicio es el responsable de gestionar toda la información relacionada con el catálogo de productos del e-commerce. Sus responsabilidades principales incluyen:

* Gestión de Productos base.
* Gestión de Variantes de producto (SKUs, precio, stock, imágenes, atributos específicos).
* Definición del esquema de atributos que generan variantes por producto.
* Gestión de atributos estáticos/descriptivos de productos.
* Gestión de Categorías y su jerarquía (multi-nivel).
* Gestión de Marcas.
* Proveer APIs REST para consultar y manipular esta información.

## Arquitectura Específica

Además de los patrones generales definidos en el [README del Backend](../README.md), este servicio implementa:

* **Base de Datos:** MongoDB.
* **Modelado de Variantes:** Utiliza colecciones separadas para `products` (información base y esquema de variantes) y `variants` (unidades vendibles específicas con SKU, precio, stock, imágenes, atributos definitorios).
* **Modelado de Categorías:** Implementa el patrón "Array of Ancestors" para consultas eficientes de subárboles jerárquicos.
* **Modelado de Atributos:**
    * `Product` define atributos estáticos (`Map<String, String> staticAttributes`) y el esquema de opciones de variante (`List<VariantOptionDefinition> variantOptions`).
    * `VariantOptionDefinition` (incrustada en Product) especifica la clave (String), etiqueta (String), tipo de valor (`AttributeType` enum), si es requerida, si afecta imágenes y la lista opcional de valores permitidos (`availableValues`).
    * `Variant` almacena los atributos que la definen en un mapa (`Map<String, Object> definingAttributes`).
* **Concurrencia:** Utiliza bloqueo optimista (`@Version`) en la entidad `Variant` para manejar actualizaciones concurrentes de stock/precio.
* **Auditoría:** Utiliza `@CreatedDate` y `@LastModifiedDate` con `LocalDateTime` en `BaseEntity`.

## Stack Tecnológico Específico

* Spring Boot 3+
* Spring Web MVC
* Spring Data MongoDB
* MapStruct
* Jakarta Bean Validation
* Java 17+ (para `records`)
* Maven

## Configuración Específica

Revisa y configura las siguientes propiedades en `src/main/resources/application.properties`:

* `server.port`: Puerto para este servicio (ej: `8081`).
* `spring.application.name=catalog-service`
* `spring.data.mongodb.uri`: URI de conexión a tu base de datos MongoDB (ej: `mongodb://localhost:27017/catalogdb`).
* `logging.level.*`: Ajusta los niveles de log según sea necesario.

## Ejecución Local

1.  **Prerrequisitos:** Asegúrate de tener MongoDB corriendo y accesible en la URI configurada.
2.  **Desde la Raíz del Monorepo:**
    ```bash
    # Construir solo este módulo (si tu build lo permite)
    mvn clean package -pl backend/catalog-service -am
    # Ejecutar
    java -jar backend/catalog-service/target/*.jar
    ```
3.  **Desde el Directorio del Servicio:**
    ```bash
    cd backend/catalog-service
    mvn spring-boot:run
    ```
    *(O usa los comandos `package` y `java -jar` como arriba)*
4.  **Desde el IDE:** Ejecuta la clase `com.tuempresa.ecommerce.catalog.CatalogApplication`.

El servicio estará disponible en `http://localhost:{server.port}` (ej: `http://localhost:8081`).

## API Endpoints Principales

*(Idealmente, integra Springdoc-OpenAPI y pon el enlace aquí: "La documentación completa de la API está disponible en /swagger-ui.html")*

* `/api/v1/brands` (GET, POST)
* `/api/v1/brands/{idOrSlug}` (GET, PUT, DELETE)
* `/api/v1/brands/search?name=...` (GET Paginado)
* `/api/v1/categories` (GET, POST)
* `/api/v1/categories/{idOrSlug}` (GET, PUT, DELETE)
* `/api/v1/categories/tree?root=...` (GET - Ejemplo para obtener rama)
* `/api/v1/products` (POST) - Crea producto y variante inicial
* `/api/v1/products/{idOrSlug}` (GET, PUT) - GET obtiene producto base, PUT actualiza producto base
* `/api/v1/products/{productId}/variants` (GET, POST) - GET obtiene variantes, POST crea nueva variante
* `/api/v1/variants/{variantIdOrSku}` (GET, DELETE)
* `/api/v1/variants/{variantIdOrSku}/stock` (PUT/PATCH - Ejemplo para actualizar stock)
* *(Define los endpoints finales según tu diseño API)*

## Estrategia de Testing

* **Pruebas Unitarias:** Se deben crear para la lógica de dominio (entidades, VOs), servicios de aplicación (con repositorios mockeados) y mappers. Ubicadas en `src/test/java`.
* **Pruebas de Integración:** Se deben crear para probar la interacción con MongoDB (usando Testcontainers o una BD embebida) y para probar los endpoints de la API (usando `MockMvc` o `WebTestClient`). Ubicadas en `src/test/java`.

*(Opcional: Añadir detalles sobre cómo correr tests específicos)*
