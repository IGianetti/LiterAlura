// src/main/java/com/challenge/literalura/MenuPrincipal.java
package com.challenge.literalura.principal; // Asegúrate de que el paquete sea el correcto

import com.challenge.literalura.modelos.Autor;
import com.challenge.literalura.modelos.Libro; // Necesario para el display en listarLibrosPorIdiomaPaginado

import com.challenge.literalura.service.AutorService;
import com.challenge.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component; // Cambiamos de @SpringBootApplication para ser Component
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component // Esta clase ahora es un componente de Spring
public class MenuPrincipal {

    // Colores ANSI para la consola
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    private Scanner teclado = new Scanner(System.in);

    // Inyectamos los servicios en lugar de los repositorios directamente
    @Autowired
    private LibroService libroService;
    @Autowired
    private AutorService autorService;


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
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
                    BOLD + CYAN + "║ " + BLUE + "8. Buscar autores por criterio   " + "👤" + "  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "║ " + BLUE + "9. Libros por idioma (Paginado)  " + "📄" + "  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "║                                     ║" + RESET + "\n" +
                    BOLD + CYAN + "║ " + RED + "0. Salir                         ❌  " + CYAN + "║" + RESET + "\n" +
                    BOLD + CYAN + "╚═════════════════════════════════════╝" + RESET + "\n" +
                    BOLD + YELLOW + "Elija una opción: " + RESET;

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
                        // La opción 5 es la que no está paginada, podrías eliminarla si quieres
                        // o implementar una versión no paginada en LibroService
                        System.out.print(BLUE + "Ingrese el código de idioma (ej. es, en, fr): " + RESET);
                        String idioma = teclado.nextLine().trim().toLowerCase();
                        List<Libro> librosPorIdioma = libroService.listarTodosLosLibros().stream()
                                .filter(l -> l.getIdioma().contains(idioma)) // Filter clientside for now
                                .collect(Collectors.toList());
                        libroService.mostrarDetallesLibros(librosPorIdioma); // Reutiliza el metodo de servicio
                        break;
                    case 6:
                        libroService.mostrarEstadisticasDeDescargas();
                        break;
                    case 7:
                        listarTop10LibrosMasDescargados();
                        break;
                    case 8:
                        buscarAutoresPorCriterio();
                        break;
                    case 9:
                        listarLibrosPorIdiomaPaginado();
                        break;
                    case 0:
                        System.out.println(GREEN + "Cerrando la aplicación. ¡Gracias!" + RESET);
                        break;
                    default:
                        System.out.println(RED + "Opción inválida. Por favor, ingrese un número entre 0 y 9." + RESET);
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println(RED + "Entrada inválida. Por favor, ingrese un número." + RESET);
                teclado.nextLine(); // Consumir la entrada inválida
            } catch (Exception e) {
                System.out.println(RED + "Error inesperado: " + e.getMessage() + RESET);
            }
        }
    }

    // Métodos que ahora delegan la lógica a los servicios
    private void buscarLibroPorTitulo() {
        System.out.print(BLUE + "Ingrese el título del libro que desea buscar: " + RESET);
        String tituloLibro = teclado.nextLine();
        libroService.buscarYGuardarLibro(tituloLibro);
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroService.listarTodosLosLibros();
        libroService.mostrarDetallesLibros(libros);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorService.listarTodosLosAutores();
        autorService.mostrarDetallesAutores(autores);
    }

    private void listarAutoresVivosPorAno() {
        System.out.print(BLUE + "Ingrese el año para buscar autores vivos: " + RESET);
        try {
            Integer ano = Integer.valueOf(teclado.nextLine());
            List<Autor> autores = autorService.listarAutoresVivosEnAno(ano);
            autorService.mostrarDetallesAutores(autores);
        } catch (NumberFormatException e) {
            System.out.println(RED + "Entrada inválida. Por favor, ingrese un año numérico." + RESET);
        }
    }

    private void listarTop10LibrosMasDescargados() {
        List<Libro> top10Libros = libroService.obtenerTop10LibrosMasDescargados();
        libroService.mostrarDetallesLibros(top10Libros);
    }

    // Metodo refactorizado para la búsqueda de autores por criterio
    private void buscarAutoresPorCriterio() {
        System.out.println(YELLOW + "\n--- Buscar Autor ---" + RESET);
        System.out.println(CYAN + "1. Por nombre (búsqueda flexible)" + RESET);
        System.out.println(CYAN + "2. Por año de nacimiento" + RESET);
        System.out.println(CYAN + "3. Por año de fallecimiento" + RESET);
        System.out.println(CYAN + "0. Volver al menú principal" + RESET);
        System.out.print(BOLD + YELLOW + "Elija una opción: " + RESET);

        try {
            int opcionBusqueda = Integer.valueOf(teclado.nextLine());
            String busquedaInput = ""; // Variable para la entrada del usuario

            if (opcionBusqueda == 0) {
                return; // Volver al menú principal
            } else if (opcionBusqueda >= 1 && opcionBusqueda <= 3) {
                // Solicitar la entrada al usuario según la opción seleccionada
                switch (opcionBusqueda) {
                    case 1:
                        System.out.print(BLUE + "Ingrese el nombre del autor (ej. Jane Austen o Austen): " + RESET);
                        break;
                    case 2:
                        System.out.print(BLUE + "Ingrese el año de nacimiento (ej. 1800): " + RESET);
                        break;
                    case 3:
                        System.out.print(BLUE + "Ingrese el año de fallecimiento (ej. 1950): " + RESET);
                        break;
                }
                busquedaInput = teclado.nextLine();
            } else {
                System.out.println(RED + "Opción de búsqueda inválida." + RESET);
                return;
            }

            // Delegar la lógica de búsqueda al servicio
            List<Autor> autoresEncontrados = autorService.buscarAutoresPorCriterio(opcionBusqueda, busquedaInput);
            autorService.mostrarDetallesAutores(autoresEncontrados); // Mostrar los resultados
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println(RED + "Entrada inválida. Por favor, ingrese un número o un texto válido." + RESET);
            teclado.nextLine(); // Consumir la entrada inválida
        } catch (Exception e) {
            System.out.println(RED + "Error al buscar autores: " + e.getMessage() + RESET);
        }
        System.out.println(YELLOW + "---------------------------------------" + RESET);
    }


    // Metodo refactorizado para listar libros por idioma con paginación
    private void listarLibrosPorIdiomaPaginado() {
        System.out.println(YELLOW + "\n--- Listar Libros por Idioma (Paginado) ---" + RESET);
        System.out.print(BLUE + "Ingrese el código de idioma (ej. es, en, fr): " + RESET);
        String idioma = teclado.nextLine().trim().toLowerCase();

        int pageNum = 0;
        int pageSize = 5; // Definimos el tamaño de la página

        while (true) {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            Page<Libro> librosPage = libroService.listarLibrosPorIdiomaPaginado(idioma, pageable);

            // Mensaje para 0 libros encontrados
            if (librosPage.isEmpty() && pageNum == 0) {
                System.out.println(RED + "0 libros encontrados en '" + idioma + "'." + RESET);
                break;
            } else if (librosPage.isEmpty() && pageNum > 0) {
                System.out.println(YELLOW + "No hay más libros para mostrar." + RESET);
                break;
            }

            System.out.println(GREEN + "\n--- Página " + (pageNum + 1) + " de " + librosPage.getTotalPages() + " (Total de libros: " + librosPage.getTotalElements() + ") ---" + RESET);
            librosPage.getContent().forEach(l -> {
                System.out.println(CYAN + "--------------------------------------" + RESET);
                System.out.println(BOLD + "Título: " + RESET + l.getTitle());
                if (l.getAutores() != null && !l.getAutores().isEmpty()) {
                    System.out.println(BOLD + "Autor(es): " + RESET + l.getAutores().stream()
                            .map(Autor::getName)
                            .collect(Collectors.joining(", ")));
                } else {
                    System.out.println(BOLD + "Autor(es): " + RESET + "Desconocido");
                }
                System.out.println(BOLD + "Idioma(s): " + RESET + l.getIdioma());
                System.out.println(BOLD + "Número de Descargas: " + RESET + l.getNumeroDeDescargas());
            });
            System.out.println(CYAN + "--------------------------------------" + RESET);

            if (!librosPage.hasNext()) {
                System.out.println(YELLOW + "\nFin de la lista de libros para este idioma." + RESET);
                break;
            }

            System.out.print(BOLD + YELLOW + "\nPresione Enter para ver la siguiente página o 's' para salir: " + RESET);
            String respuesta = teclado.nextLine().trim().toLowerCase();
            if (respuesta.equals("s")) {
                break;
            }
            pageNum++;
        }
        System.out.println(YELLOW + "---------------------------------------" + RESET);
    }
}