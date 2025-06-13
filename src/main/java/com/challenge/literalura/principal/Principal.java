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

    // Constantes ANSI para colores
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    // Para texto en negrita
    public static final String BOLD = "\u001B[1m";



    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            // --- Menú con símbolos Unicode más compatibles y ajuste de espacios ---
            var menu = BOLD + CYAN + "╔═════════════════════════════════════╗" + RESET + "\n" +
                    BOLD + CYAN + "║" + YELLOW + "        LITERALURA - MENÚ PRINCIPAL" + CYAN + "        ║" + RESET + "\n" +
                    BOLD + CYAN + "╠═════════════════════════════════════╣" + RESET + "\n" +
                    BOLD + CYAN + "║ " + BLUE + "1. Buscar libro por título       🔎  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "║ " + BLUE + "2. Listar libros registrados     📚  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "║ " + BLUE + "3. Listar autores registrados    ✍️  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "║ " + BLUE + "4. Listar autores vivos por año  🌳  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "║ " + BLUE + "5. Listar libros por idioma      🌐  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "║ " + BLUE + "6. Estadísticas de descargas     📊  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "║ " + BLUE + "7. Top 10 libros más descargados " + "⭐" + "  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "║                                     ║" + RESET + "\n" +
                    BOLD + CYAN + "║ " + RED + "0. Salir                         ❌  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "╚═════════════════════════════════════╝" + RESET + "\n" +
                    BOLD + YELLOW + "Elija una opción: " + RESET;
            // -------------------------------------------------------------------

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
                    case 7:
                        mostrarTop10LibrosMasDescargados();
                        break;
                    case 0:
                        System.out.println(GREEN + "Cerrando la aplicación. ¡Gracias!" + RESET);
                        break;
                    default:
                        System.out.println(RED + "Opción inválida. Por favor, ingrese un número entre 0 y 6." + RESET);
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(RED + "Entrada inválida. Por favor, ingrese un número." + RESET);
                teclado.nextLine(); // Consumir la entrada inválida
            } catch (Exception e) {
                System.out.println(RED + "Error inesperado: " + e.getMessage() + RESET);
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

    private void mostrarTop10LibrosMasDescargados() {
        System.out.println(YELLOW + "\n--- Top 10 Libros Más Descargados ---" + RESET);
        List<Libro> top10Libros = libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();

        if (top10Libros.isEmpty()) {
            System.out.println("No hay libros registrados para mostrar el top 10.");
        } else {
            top10Libros.forEach(l -> {
                System.out.println(CYAN + "--------------------------------------" + RESET);
                System.out.println(BOLD + "Título: " + RESET + l.getTitle());
                // Asegúrate de que tu entidad Libro tiene un método getAutorPrincipal() o similar
                // para mostrar el primer autor o todos los autores asociados.
                // Si tienes una relación ManyToMany con Autor, podría ser algo como:
                // l.getAutores().stream().map(Autor::getName).collect(Collectors.joining(", "))
                if (l.getAutores() != null && !l.getAutores().isEmpty()) {
                    System.out.println(BOLD + "Autor(es): " + RESET + l.getAutores().stream()
                            .map(a -> a.getName())
                            .collect(Collectors.joining(", ")));
                } else {
                    System.out.println(BOLD + "Autor(es): " + RESET + "Desconocido");
                }
                System.out.println(BOLD + "Idioma(s): " + RESET + l.getIdioma());
                System.out.println(BOLD + "Número de Descargas: " + RESET + l.getNumeroDeDescargas());
            });
            System.out.println(CYAN + "--------------------------------------" + RESET);
        }
        System.out.println(YELLOW + "---------------------------------------" + RESET);
    }
}