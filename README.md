 <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" integrity="sha512-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" crossorigin="anonymous" referrerpolicy="no-referrer" />

# <i class="fas fa-book"></i> Literalura: Tu Catálogo Personal de Libros <i class="fas fa-laptop-code"></i>

<br>

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Apache_Maven-C6003F?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)](https://www.jetbrains.com/idea/)

<br>

Este proyecto, **Literalura**, es una aplicación de catálogo de libros construida con **Java** y el poderoso framework **Spring Boot**. Permite a los usuarios buscar información sobre libros utilizando la API pública de [Gutendex](https://gutendex.com/) y guardar sus libros favoritos en una base de datos **PostgreSQL**. Además, ofrece funcionalidades para listar los libros y autores registrados, así como estadísticas básicas.

---

## <i class="fas fa-lightbulb"></i> Funcionalidades Principales:

* **Búsqueda de Libros:** <i class="fas fa-search"></i> Busca libros por título utilizando la API de Gutendex.
* **Registro de Libros y Autores:** <i class="fas fa-database"></i> Guarda la información de los libros encontrados y sus autores en una base de datos PostgreSQL, evitando duplicados.
* **Listado de Registros:** <i class="fas fa-list-ul"></i>
    * Muestra todos los libros guardados en la base de datos.
    * Lista todos los autores registrados.
* **Estadísticas:** <i class="fas fa-chart-bar"></i>
    * Indica la **cantidad** de libros almacenados en un idioma específico.
    * Lista los autores que estaban vivos en un año determinado.

---

## <i class="fas fa-cogs"></i> Tecnologías Utilizadas:

* **Java:** <i class="fab fa-java"></i> El lenguaje de programación principal.
* **Spring Boot:** <i class="fas fa-leaf"></i> Framework para el desarrollo rápido de aplicaciones Java.
* **Spring Data JPA:** <i class="fas fa-code-branch"></i> Para la interacción sencilla con la base de datos.
* **PostgreSQL:** <i class="fas fa-database"></i> La base de datos relacional utilizada para la persistencia de datos.
* **Maven:** <i class="fas fa-box-open"></i> Herramienta de gestión de dependencias y construcción del proyecto.
* **Java HttpClient:** <i class="fas fa-network-wired"></i> Para consumir la API de Gutendex.
* **Jackson:** <i class="fas fa-file-code"></i> Librería para el manejo de JSON.

---

## <i class="fas fa-play-circle"></i> Cómo Ejecutar el Proyecto:

1.  **Requisitos:** <i class="fas fa-clipboard-check"></i>
    * Java Development Kit (JDK) instalado.
    * Maven instalado.
    * Una instancia de PostgreSQL en ejecución (puedes configurarla en `src/main/resources/application.properties`).

2.  **Clonar el Repositorio:**
    ```bash
    git clone https://github.com/IGianetti/LiterAlura.git
    cd literalura
    ```

3.  **Configuración de la Base de Datos:**
    * Asegúrate de que la configuración de la conexión a PostgreSQL en `src/main/resources/application.properties` sea correcta (URL, usuario, contraseña, nombre de la base de datos). Spring Boot intentará crear las tablas automáticamente si no existen gracias a Spring Data JPA.

4.  **Construir el Proyecto con Maven:**
    ```bash
    mvn clean install
    ```

5.  **Ejecutar la Aplicación:**
    Puedes ejecutar la aplicación de varias maneras:
    * **Desde Maven:**
        ```bash
        mvn spring-boot:run
        ```
    * **Como un JAR ejecutable** (generado en el paso 4 en la carpeta `target`):
        ```bash
        java -jar target/literalura-*.jar
        ```

6.  **Interactuar con la Aplicación:**
    Una vez que la aplicación esté en ejecución, se mostrará un menú en la consola donde podrás elegir las diferentes opciones.

---

## <i class="fas fa-terminal"></i> Uso:

1.  Elige la opción del menú ingresando su número.
2.  Sigue las instrucciones en pantalla (por ejemplo, ingresar el título del libro o el idioma).
3.  La aplicación mostrará la información solicitada o realizará la acción correspondiente.

---

## <div align="center">[Espacio para una Imagen o Video Demostrativo Aquí]</div>

Puedes reemplazar este texto con una imagen (por ejemplo, una captura de pantalla del menú o de la aplicación en funcionamiento) utilizando Markdown:


---


## <i class="fas fa-rocket"></i> Próximos Pasos y Posibles Mejoras:

* Implementar más opciones de búsqueda (por autor, ISBN, etc.).
* Permitir a los usuarios crear listas de lectura personalizadas.
* Añadir una interfaz de usuario más amigable (por ejemplo, una aplicación web).
* Incluir más estadísticas (por autor, por género, etc.).
* Implementar pruebas unitarias e de integración.
* Manejo de errores más robusto.

---

## <i class="fas fa-hands-helping"></i> Contribución:

Las contribuciones son bienvenidas. Si encuentras algún error o tienes alguna sugerencia de mejora, no dudes en abrir un "issue" o enviar un "pull request" en el repositorio.


---


## <i class="fas fa-scroll"></i> Licencia:

Este proyecto está bajo la **Licencia MIT**. Puedes consultar el archivo `LICENSE` en el repositorio para más detalles.

---


## <i class="fas fa-envelope"></i> Contacto:
Para cualquier consulta o colaboración, no dudes en contactarme: `ivangianetti@gmail.com`

---