Proyecto de una biblioteca baseada en java con jdbc para conexión a base de datos y java swing como interfaz gráfica de usuario. Se pueden realizar multiple opciones como gestionar prestamos, libros, usuarios...

## Requisitos
Se requiere tener java 21 o superior instalado en el sistema, así como un servidor de base de datos mysql instalado o alternativamente se puede usar un contenedor de docker con mysql.

## Configuración 
Existen 2 ficheros de configuración, uno para el dockerfile .env.example debe ser renombrado como .env y modificado con los valores necesarios y otro para la conexión a la base de datos (application.properties en src/main/resources). En este último fichero existen 3 lineas que gestionas la dirección de la base de datos, el usuario y la contraseña. En este último caso solo debe ser modificado el usuario y la contraseña si se ha modificado en el fichero .env.

## Compilación
Para compilar el proyecto se puede usar maven, para ello se debe ejecutar el siguiente comando en la raíz del proyecto:

```bash
# Para compilar el proyecto y generar el jar ejecutable
mvn clean install

# Para ejecutar el proyecto
java -jar target/biblioteca-1.0-SNAPSHOT.jar
```

El docker-compose.yml se puede usar para levantar un contenedor con mysql y así no tener que instalarlo en el sistema. Para ello se debe ejecutar el siguiente comando en la raíz del proyecto:

```bash
docker-compose up -d
```

## Los test se pueden ejecutar con el siguiente comando:

```bash
mvn test
```

Alternativamente se puede hacer desde un IDE como vscode.

## Estructura del proyecto

Actualmente en refactorización

