# CRUD backend

> Este proyecto cuenta con un CRUD con spring boot utilizando Webflux asi como su respectiva documentacion mediante swagger.
> Con respecto a la base de datos se esta usando MongoDB y debido a ello no es necesario un script con la creacion de tablas, al correr el proyecto creara la bd (spring_boot) y una coleccion llamada (tareas)

## Requerimietos

- Java 11
- MongoDB 4.4.0
- Maven

## Instalacion

```bash
git clone https://github.com/jpsa16/CRUD-backend-webflux.git
```

## Uso de comandos

```bash
mvn install

mvn spring-boot:run
```

Para ver el swagger se tiene que ingresar a la siguiente ruta una vez se ha corrido el proyecto `http://localhost:8080/swagger-ui.html`


