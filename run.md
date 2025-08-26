# Guía rápida para ejecutar el proyecto Inventory API

## 1. Requisitos previos

- Docker instalado (para RabbitMQ)
- Java 17+ instalado
- Maven instalado (opcional, puedes usar el wrapper)

## 2. Iniciar RabbitMQ con Docker

Ejecuta el siguiente comando para levantar RabbitMQ y su consola de administración:

```
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Accede a la consola de administración en: [http://localhost:15672](http://localhost:15672)
- Usuario: `guest`
- Contraseña: `guest`

## 3. Configuración de la aplicación

Asegúrate de que el archivo `src/main/resources/application.properties` tenga la configuración correcta para RabbitMQ:

```
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

## 4. Ejecutar la aplicación

### Opción 1: Usando Maven Wrapper

```
./mvnw spring-boot:run
```

### Opción 2: Usando Maven instalado globalmente

```
mvn spring-boot:run
```

### Opción 3: Ejecutar el JAR generado

Primero, compila el proyecto:
```
./mvnw clean package
```
Luego ejecuta:
```
java -jar target/inventory-api-1.jar
```

## 5. Acceso a la documentación Swagger

Una vez la aplicación esté corriendo, accede a:
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- o [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)

## 6. Apagar RabbitMQ

Cuando termines, puedes detener y eliminar el contenedor:
```
docker stop rabbitmq && docker rm rabbitmq
```

---

Para más detalles, revisa el archivo `README.md`.
