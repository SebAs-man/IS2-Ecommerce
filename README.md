# Proyecto E-commerce Monorepo - Ingeniería del Software II

## 1. Introducción

Bienvenido al Monorepo del proyecto de E-commerce desarrollado para la asignatura Ingeniería del Software II. Este repositorio contiene el código fuente completo de la plataforma, incluyendo múltiples microservicios backend y microfrontends.

El objetivo principal es construir una aplicación de e-commerce funcional aplicando principios de arquitectura moderna, metodologías ágiles (Scrum) y fomentando la colaboración en equipo.

## 2. Arquitectura General

La plataforma sigue una arquitectura distribuida:

* **Backend:** Implementado como un conjunto de **Microservicios** desarrollados con Java y Spring Boot. Cada servicio se enfoca en una capacidad de negocio específica (Catálogo, Carrito, Pedidos, Usuarios, etc.). Se comunican entre sí mediante APIs REST y/o mensajería asíncrona (RabbitMQ). Un API Gateway (Kong) gestiona el acceso externo.
* **Frontend:** Implementado usando **Microfrontends** desarrollados con Angular y potencialmente orquestados mediante Webpack Module Federation a través de una aplicación Shell principal.

Para más detalles sobre cada parte, consulta:

* [README del Backend](./backend/README.md)
* [README del Frontend](./frontend/README.md) (Pendiente)

## 3. Estructura del Monorepo

Este repositorio organiza los diferentes componentes de la siguiente manera:

```text
/
├── backend/                # Microservicios Backend (Java/Spring Boot)
│   ├── catalog-service/
│   ├── cart-service/
│   └── ...
├── frontend/               # Microfrontends (Angular)
│   ├── navbar-mf/
│   ├── catalog-mf/
│   └── ...
├── libs/                   # (Opcional) Librerías compartidas entre servicios/frontends
│   └── shared-dtos/        # Ejemplo: DTOs comunes para comunicación
├── .gitignore              # Reglas globales para ignorar archivos
└── README.md               # Este archivo (Visión General)
```

Para detalles específicos sobre cómo configurar, ejecutar o probar cada microservicio o microfrontend, consulta el archivo README.md dentro de su respectivo directorio.

## Tecnologías principales
-   **Backend:** Java 17+, Spring Boot 3+, Spring Data (MongoDB, JPA), RabbitMQ, gRPC, Kong API Gateway
-   **Frontend:** Angular 16+, TypeScript
-   **Base de Datos:** MongoDB, Redis, Bases de Datos Relacionales
-   **Build:** Maven (Backend), Angular CLI (Frontend)
-   **Control de Versiones:** Git, GitHub
-   **Metodología:** Scrum

## Prerrequisitos Globales

-   Git
-   JDK 17+
-   Maven
-   Node.js y NPM
-   Docker (Recomendado, para bases de datos y otros servicios)
-   Una instancia de MongoDB, Redis, y BD Relacional (pueden correr en Docker)
-   RabbitMQ, Kong

## Cómo Empezar

 1. **Clonar el Repositorio:**
	```
	git clone <URL_DEL_REPOSITORIO_GITHUB> 
	cd tu-proyecto-ecommerce
	```
 2. **Configurar Ramas:** La rama principal de desarrollo es `develop`. Asegúrate de estar en ella:
	```
	git checkout develop
	git pull origin develop # Para obtener lo último
	```
3.  **Dependencias:**
    -   Backend: Revisa los `pom.xml` o `build.gradle` de cada servicio. Un `mvn install` dentro de cada servicio podría ser necesario.
    -   Frontend: Ve a cada directorio de microfrontend (`cd frontend/navbar-mf`) y ejecuta `npm install`.
4.  **Variables de Entorno / Configuración:** Revisa los archivos `application.properties` de cada servicio backend para configurar las conexiones a bases de datos, RabbitMQ, etc. Pueden requerir variables de entorno o perfiles de Spring.
5.  **Ejecutar Servicios/Frontends:** Consulta el `README.md` específico de cada componente para instrucciones detalladas de ejecución.

## Estrategia de Ramificación

Este repositorio utiliza un flujo **Gitflow Simplificado**:

-   `main`: Contiene el código estable y probado, potencialmente desplegable. No se hace push directo.
-   `develop`: Rama principal de integración. Todo el nuevo desarrollo se fusiona aquí mediante Pull Requests. Es la rama de trabajo por defecto.
-   `feature/<area>/<descripcion>`: Ramas para desarrollar nuevas funcionalidades o arreglos. Se crean desde `develop` y vuelven a `develop` vía Pull Request (ej: `feature/catalog/agregar-validacion-atributo`).


