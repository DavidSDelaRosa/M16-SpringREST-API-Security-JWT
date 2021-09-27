# M16-SpringREST-API-Security-JWT
Modulo 16 ITAcademy Backend Java con Spring

Se trata de un juego con dos dados. Cada vez que se realiza el request adecuado (POST) a la API (se necesita autorización en la última fase), la respuesta será el resultado del 
lanzamiento aleatorio de dos dados. Si ambos suman 7, es victoria; si no, pérdida. 

Primera fase:  Persistencia de datos en MySql

Segunda fase: Persistencia de datos en MongoDb

Tercera fase: MongoDb y seguridad JWT a todos los endpoints de la API

3 maneras de conseguir los JWT:
  -como admin ("admin"/"admin")
  -como usuario ("user"/"user")
  -como anonimo ("anonymous"/"anonymous")
  
  
![getting-jwt](https://user-images.githubusercontent.com/77668181/134876127-e62a8ac6-cbcc-4d3d-8966-c345fb5e60ea.PNG)

Tuve problemas con este error: java.lang.NoClassDefFoundError: Could not initialize class javax.xml.bind.DatatypeConverterImpl

Era debido a usar Java11, tuve que añadir esta dependencia al pom:

    <groupId>javax.xml.bind</groupId> 
    <artifactId>jaxb-api</artifactId>
    <version>2.1</version>


Con CommandLineRunner cargo algunos datos sobre la base de datos en Mongo:


![mongo_db](https://user-images.githubusercontent.com/77668181/134877407-196a1bd5-c078-42cd-8050-7d88cbb3ab77.PNG)

Una vez se consigue el JWT, al añadirlo en el Header, se permite el acceso a la API:

-Crear usuario (introduciendo un body con username y password)

-Modificar el nombre del usuario

-JUGAR (se realiza la tirada y se le añaden a dicho jugador)

-Eliminar tiradas del jugador

-Listar usuarios

-Encontrar el ranking medio de tiradas

-Encontrar el mejor jugador

-Encontrar el peor jugador



Ejemplo: 

![authorization](https://user-images.githubusercontent.com/77668181/134878256-94d46db0-85f6-457e-abec-da990f243c33.PNG)
