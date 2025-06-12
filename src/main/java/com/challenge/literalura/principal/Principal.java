package com.challenge.literalura.principal;

import com.challenge.literalura.modelos.Autor;
import com.challenge.literalura.modelos.DatosLibro;
import com.challenge.literalura.modelos.Libro;
import com.challenge.literalura.modelos.RespuestaApiGutendex;

import com.challenge.literalura.repository.AutorRepository;
import com.challenge.literalura.repository.LibroRepository;

import com.challenge.literalura.service.ConsumoAPI;
import com.challenge.literalura.service.ConvierteDatos;
import com.challenge.literalura.service.ConversorDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.DoubleSummaryStatistics;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


@Component
public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    // ¡Ahora Spring inyectará estas dependencias!
    @Autowired
    private ConsumoAPI consumoApi;
    @Autowired
    private ConvierteDatos conversor; // Inyecta la interfaz, Spring encontrará la implementación @Service

    @Value("${api.gutendex.url}") // ¡Inyecta la URL_BASE directamente aquí!
    private String URL_BASE;

    @Autowired //  Los repositorios también se inyectan automáticamente.
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;



    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    -------------------------------------
                    Elija la opción a través de su número:
                    1- Buscar libro por título
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    6- Generar estadísticas de descargas
                    0- Salir
                    -------------------------------------
                    """;
            System.out.println(menu);
            try {
                opcion = Integer.valueOf(teclado.nextLine());

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAno();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        generarEstadisticasDescargas();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación. ¡Gracias!");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                teclado.nextLine();
            } catch (Exception e) {
                System.out.println("Error al comunicarse con la API o al procesar datos: " + e.getMessage());
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingresa el título del libro que deseas buscar:");
        var tituloLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));

        var datosApi = conversor.obtenerDatos(json, RespuestaApiGutendex.class);

        if (datosApi != null && !datosApi.getLibros().isEmpty()) {
            DatosLibro primerDatosLibro = datosApi.getLibros().get(0);

            Libro primerLibro = new Libro(primerDatosLibro);

            List<Autor> autoresAGuardar = primerDatosLibro.autores().stream()
                    .map(datosAutor -> {
                        Autor autorExistente = autorRepository.findByName(datosAutor.nombre());
                        if (autorExistente != null) {
                            return autorExistente;
                        } else {
                            Autor nuevoAutor = new Autor(datosAutor.nombre(), datosAutor.anoNacimiento(), datosAutor.anoFallecimiento());
                            return autorRepository.save(nuevoAutor);
                        }
                    })
                    .collect(Collectors.toList());

            primerLibro.setAutores(autoresAGuardar);

            System.out.println("\n--- Libro Encontrado (y será guardado) ---");
            System.out.println(primerLibro);

            Libro libroExistente = libroRepository.findByGutendexId(primerLibro.getGutendexId());

            if (libroExistente == null) {
                libroRepository.save(primerLibro);
                System.out.println("Libro y sus autores guardados exitosamente en la base de datos.");
            } else {
                System.out.println("Este libro ya existe en la base de datos (ID Gutendex: " + primerLibro.getGutendexId() + "). No se guardó nuevamente.");
            }

        } else {
            System.out.println("No se encontró ningún libro con ese título.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n--- Libros Registrados ---");
            libros.forEach(System.out::println);
            System.out.println("--------------------------");
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n--- Autores Registrados ---");
            autores.forEach(System.out::println);
            System.out.println("---------------------------");
        }
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("Ingrese el año para buscar autores vivos:");
        try {
            Integer ano = Integer.valueOf(teclado.nextLine());
            List<Autor> autoresVivos = autorRepository.findAuthorsAliveInYear(ano);
            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + ano + ".");
            } else {
                System.out.println("\n--- Autores Vivos en el Año " + ano + " ---");
                autoresVivos.forEach(System.out::println);
                System.out.println("-------------------------------------");
            }
        } catch (NumberFormatException e) {
            System.out.println("Año inválido. Por favor, ingrese un número entero.");
        }
    }

    // --- Metodo modificado para mostrar solo la cantidad ---
    private void listarLibrosPorIdioma() {
        System.out.println("""
            Ingrese el idioma para buscar libros:
            es - español
            en - inglés
            fr - francés
            pt - portugués
            """);
        var idiomaInput = teclado.nextLine().trim().toLowerCase();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idiomaInput);

        // Llamamos a la nueva función auxiliar para obtener el nombre del idioma
        String nombreIdioma = obtenerNombreIdioma(idiomaInput);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en " + nombreIdioma + ".");
        } else {
            // ¡Aquí solo imprimimos la cantidad y el nombre del idioma!
            System.out.println("\n--- Libros en " + nombreIdioma + ": " + librosPorIdioma.size() + " ---");
        }
    }

    // --- Nueva función auxiliar para traducir códigos de idioma ---
    private String obtenerNombreIdioma(String codigoIdioma) {
        return switch (codigoIdioma.toLowerCase()) {
            case "es" -> "español";
            case "en" -> "inglés";
            case "fr" -> "francés";
            case "pt" -> "portugués";
            default -> codigoIdioma; // Devuelve el código si no es uno de los conocidos
        };
    }

    private void generarEstadisticasDescargas() {

        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados para generar estadísticas.");
            return;
        }

        DoubleSummaryStatistics stats = libros.stream()
                .mapToDouble(Libro::getNumeroDeDescargas)
                .summaryStatistics();

        System.out.println("\n--- Estadísticas de Descargas de Libros ---");
        System.out.println("Total de libros: " + stats.getCount());
        System.out.println("Promedio de descargas: " + String.format("%.2f", stats.getAverage()));
        System.out.println("Máximo de descargas: " + stats.getMax());
        System.out.println("Mínimo de descargas: " + stats.getMin());
        System.out.println("------------------------------------------");
    }
}