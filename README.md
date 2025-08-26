# Inventory API

## Resumen de la funcionalidad

Esta aplicación es una API para la gestión de inventarios y tiendas. Permite crear, actualizar, eliminar y consultar productos, inventarios y tiendas, así como gestionar el stock de productos en diferentes tiendas. Incluye integración con una cola de mensajes para operaciones asincrónicas.

## Estructura de carpetas


```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/inventory/
│   │   │       ├── application/           # Lógica de aplicación (DTOs, servicios, mapeadores, puertos)
│   │   │       │   ├── dto/               # Objetos de transferencia de datos
│   │   │       │   ├── exception/         # Excepciones de negocio
│   │   │       │   ├── mapper/            # Conversores entre entidades y DTOs
│   │   │       │   ├── port/              # Puertos de entrada/salida (hexagonal)
│   │   │       │   └── service/           # Servicios de aplicación
│   │   │       │       └── impl/          # Implementaciones de servicios
│   │   │       ├── config/                # Configuración general y propiedades
│   │   │       ├── domain/                # Lógica de dominio (entidades, eventos, repositorios)
│   │   │       │   ├── event/             # Eventos de dominio
│   │   │       │   ├── model/             # Entidades de dominio
│   │   │       │   └── repository/        # Interfaces de repositorios de dominio
│   │   │       ├── infrastructure/        # Infraestructura (implementaciones técnicas)
│   │   │       │   ├── config/            # Configuración técnica (Swagger, RabbitMQ)
│   │   │       │   ├── messaging/         # Mensajería (RabbitMQ, DTOs de eventos)
│   │   │       │   ├── persistence/       # Persistencia (entidades JPA, repositorios)
│   │   │       │   └── web/               # Manejo de errores web
│   │   │       ├── interface_/            # Interfaces de usuario (controladores REST)
│   │   │       │   ├── controller/        # Controladores generales
│   │   │       │   └── v1/                # Controladores versión 1 (Inventory, Product, Store)
│   │   │       └── InventoryApiApplication.java # Clase principal Spring Boot
│   │   └── resources/
│   │       ├── application.properties     # Configuración de la aplicación
│   │       ├── db/
│   │       │   └── migration/             # Migraciones de base de datos (Flyway)
│   │       │       └── V1__create_products.sql
│   │       ├── static/                    # Recursos estáticos (vacío)
│   │       └── templates/                 # Plantillas (vacío)
│   └── test/
│       └── java/
│           └── com/example/inventory/
│               ├── application/service/impl/ # Pruebas de servicios
│               ├── interface_/v1/           # Pruebas de controladores
│               └── InventoryApiApplicationTests.java # Prueba de integración principal
├── data/
│   └── central_inventory.db               # Base de datos local de ejemplo
├── target/                                # Archivos generados por la compilación y pruebas
├── pom.xml                                # Configuración de Maven
├── mvnw, mvnw.cmd                         # Wrappers de Maven
├── HELP.md                                # Ayuda adicional
```

### Apuntamientos de arquitectura
- **application/**: Lógica de negocio y orquestación de casos de uso.
- **domain/**: Entidades, lógica de dominio y contratos de repositorios/eventos.
- **infrastructure/**: Implementaciones técnicas (mensajería, persistencia, configuración).
- **interface_/**: Controladores REST y endpoints expuestos.
- **resources/**: Configuración, migraciones y recursos estáticos.
- **test/**: Pruebas unitarias e integración.
- **data/**: Base de datos local para desarrollo/pruebas.
- **target/**: Archivos generados por Maven (no modificar manualmente).

## Tecnología utilizada

- Java (Spring Boot)
- Maven
- Base de datos SQLite (central_inventory.db)
- Integración con cola de mensajes (ej. RabbitMQ, ActiveMQ)

## Configuración de `application.properties` para la cola de mensajes

Ajusta los siguientes parámetros en `src/main/resources/application.properties`:

```
# Ejemplo para RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=usuario
spring.rabbitmq.password=contraseña
```

## Configuración de parámetros para definir las tiendas

Puedes definir parámetros personalizados para las tiendas en el archivo `application.properties`:

```
# Ejemplo de configuración de tiendas
# --- Configuración para una tienda central ---
store.name=TIENDA_CENTRAL
store.role=central

# --- Configuración para una tienda local ---
store.name=TIENDA_A
store.role=local
```

Estos parámetros pueden ser leídos desde la aplicación para inicializar o configurar las tiendas disponibles.

---


## Cómo iniciar la aplicación

### Paso previo: Iniciar RabbitMQ con Docker

Antes de iniciar la aplicación, asegúrate de tener RabbitMQ corriendo. Puedes levantar una instancia rápidamente usando Docker:

```
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Esto iniciará RabbitMQ y habilitará la consola de administración en [http://localhost:15672](http://localhost:15672) (usuario/clave por defecto: guest/guest).

Puedes iniciar la aplicación utilizando Maven desde la terminal:

```
mvnw spring-boot:run
```
o, si tienes Maven instalado globalmente:
```
mvn spring-boot:run
```

También puedes ejecutar el archivo JAR generado:
```
java -jar target/inventory-api-1.jar
```

## Acceso a Swagger (Documentación de la API)

Una vez iniciada la aplicación, accede a la documentación interactiva de la API en:

```
http://localhost:8080/swagger-ui.html
```
o
```
http://localhost:8080/swagger-ui/
```

## Apuntamientos

- El proyecto sigue una arquitectura por capas (controller, service, repository).
- La configuración principal se encuentra en `src/main/resources/application.properties`.
- La base de datos de ejemplo está en `data/central_inventory.db`.
- Las pruebas unitarias están en `src/test/java/`.
- El endpoint Swagger puede variar según la configuración de SpringFox o Springdoc.

Para más detalles sobre la configuración o la estructura, consulta los archivos fuente y la documentación interna del proyecto.

## Ejecución de pruebas unitarias

Para ejecutar las pruebas unitarias del proyecto, utiliza uno de los siguientes comandos desde la raíz del proyecto:

### Usando Maven Wrapper
```
./mvnw test
```

### Usando Maven instalado globalmente
```
mvn test
```

Los reportes de pruebas se generan en la carpeta `target/surefire-reports/`.
## Endpoints principales

### StoreController (`/api/v1/stores`)
- `GET    /api/v1/stores` — Buscar tienda por nombre (param: `name`)
- `GET    /api/v1/stores/all` — Listar todas las tiendas
- `POST   /api/v1/stores` — Crear una nueva tienda
- `PUT    /api/v1/stores` — Actualizar una tienda (param: `name`)
- `PUT    /api/v1/stores/deactivate` — Desactivar una tienda (param: `name`)
- `PUT    /api/v1/stores/activate` — Activar una tienda (param: `name`)
- `DELETE /api/v1/stores` — Eliminar una tienda (param: `name`)

### ProductController (`/api/v1/products`)
- `GET    /api/v1/products/{code}` — Buscar producto por código
- `POST   /api/v1/products` — Crear un nuevo producto
- `PUT    /api/v1/products/{code}` — Actualizar un producto
- `DELETE /api/v1/products/{code}` — Eliminar un producto
- `GET    /api/v1/products` — Listar todos los productos

### InventoryController (`/api/v1/inventory`)
- `GET    /api/v1/inventory/stock/{storeName}/{productCode}` — Consultar stock de un producto en una tienda
- `GET    /api/v1/inventory/products/{storeName}` — Consultar productos en una tienda
- `GET    /api/v1/inventory/stores/{productCode}` — Consultar tiendas con un producto
- `POST   /api/v1/inventory/add` — Añadir stock a un producto en una tienda
- `POST   /api/v1/inventory/remove` — Quitar stock de un producto en una tienda
- `POST   /api/v1/inventory` — Crear un nuevo inventario

### HealthController
- `GET    /health` — Verificar salud del servicio
