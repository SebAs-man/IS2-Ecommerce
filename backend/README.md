# Proyecto E-commerce Monorepo (Ingeniería de Software 2)

## Descripción General

Este repositorio contiene el código fuente completo para un sistema de E-commerce desarrollado como proyecto para la asignatura Ingeniería del Software 2. La arquitectura está basada en **Microservicios** para el backend y **Microfrontends** para la interfaz de usuario, todo gestionado dentro de este Monorepo.

El objetivo es simular un entorno de desarrollo colaborativo aplicando metodologías ágiles (Scrum) y tecnologías modernas.

## Estructura del Repositorio

Este Monorepo organiza el código de la siguiente manera:

```text
/
├── backend/                # Contiene todos los microservicios del backend (Spring Boot)
│   ├── catalog-service/    # Microservicio para el catálogo (MongoDB)
│   │   └── README.md       # <-- README específico del microservicio
│   ├── cart-service/       # Microservicio para el Carrito de Compras (Redis)
│   │   └── README.md      
│   ├── security-service/   # Microservicio para Seguridad
│   │   └── README.md     
│   ├── discount-service/   # Microservicio para Descuentos (Relacional)
│   │   └── README.md       
│   ├── order-service/      # Microservicio para Pedidos (Relacional)
│   │   └── README.md      
│   └── ...                 # Otros posibles servicios
├── frontend/               # Contiene todos los microfrontends (Angular)
│   ├── navbar-mf/          # Microfrontend para la Barra de Navegación
│   │   └── README.md       
│   ├── catalog-mf/         # Microfrontend para el Catálogo
│   │   └── README.md       
│   ├── cart-mf/            # Microfrontend para el Carrito
│   │   └── README.md       
│   └── shell-app/          # Aplicación "Shell" principal que carga los microfrontends
│       └── README.md       
├── libs/                   # (Opcional) Librerías compartidas entre proyectos
│   └── shared-dtos/
├── .gitignore              # Ignora archivos de build, logs, IDEs, etc. (Principal)
└── README.md               # Este archivo (README Principal)
```

Para detalles específicos sobre cómo configurar, ejecutar o probar cada microservicio o microfrontend, consulta el archivo README.md dentro de su respectivo directorio.

## Tecnologías principales
-   **Backend:** Java 17+, Spring Boot 3+, Spring Data (MongoDB, JPA), RabbitMQ, gRPC, Kong API Gateway
-   **Frontend:** Angular 16+, TypeScript, Module Federation (probablemente)
-   **Base de Datos:** MongoDB, Redis, Bases de Datos Relacionales (ej: PostgreSQL)
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
-   (Opcional) RabbitMQ, Kong

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


