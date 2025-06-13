package com.challenge.literalura.service;

import com.challenge.literalura.modelos.Autor;
import com.challenge.literalura.modelos.DatosRespuestaAPI; // Usamos el record DatosRespuestaAPI
import com.challenge.literalura.modelos.DatosLibro;       // Usamos tu record DatosLibro
import com.challenge.literalura.modelos.Libro;
import com.challenge.literalura.repository.AutorRepository;
import com.challenge.literalura.repository.LibroRepository;
import com.challenge.literalura.service.ConsumoAPI;
import com.challenge.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Importamos @Value para inyectar la URL
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.DoubleSummaryStatistics; // Para las estadísticas
import java.util.stream.Collectors;

@Service
public class LibroService {

    // Colores ANSI para la consola
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    // Inyectamos la URL base desde application.properties
    @Value("${api.gutendex.url}")
    private String URL_BASE;

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private ConsumoAPI consumoAPI;
    @Autowired
    private ConvierteDatos conversor;

    /**
     * Busca un libro por título en la API de Gutendex, lo guarda en la base de datos
     * si no existe y maneja sus autores.
     * @param tituloLibro El título del libro a buscar.
     * @return El objeto Libro guardado o encontrado, o null si no se encuentra.
     */
    public Libro buscarYGuardarLibro(String tituloLibro) {
        // Construimos la URL completa y se la pasamos a ConsumoAPI
        String urlBusqueda = URL_BASE + "?search=" + tituloLibro.replace(" ", "+");
        String json = consumoAPI.obtenerDatos(urlBusqueda);

        DatosRespuestaAPI datosRespuesta = conversor.obtenerDatos(json, DatosRespuestaAPI.class);

        if (datosRespuesta != null && datosRespuesta.results() != null && !datosRespuesta.results().isEmpty()) {
            DatosLibro datosLibroGutendex = datosRespuesta.results().get(0);

            // Verificar si el libro ya existe en la base de datos
            Optional<Libro> libroExistente = libroRepository.findByTitle(datosLibroGutendex.titulo());
            if (libroExistente.isPresent()) {
                System.out.println(YELLOW + "El libro '" + libroExistente.get().getTitle() + "' ya está registrado." + RESET);
                return libroExistente.get();
            }

            // Crear un nuevo objeto Libro
            Libro libro = new Libro(datosLibroGutendex);

            // Procesar autores: buscar existentes o crear nuevos
            if (datosLibroGutendex.autores() != null && !datosLibroGutendex.autores().isEmpty()) {
                for (com.challenge.literalura.modelos.DatosAutor datosAutorApi : datosLibroGutendex.autores()) {
                    Autor autor = autorRepository.findByName(datosAutorApi.nombre());
                    if (autor == null) {
                        autor = new Autor(datosAutorApi);
                        autorRepository.save(autor); // Guardar nuevo autor
                    }
                    libro.addAutor(autor); // Asociar autor al libro (maneja la relación bidireccional)
                }
            }

            libroRepository.save(libro); // Guardar el libro (incluyendo sus autores asociados)
            System.out.println(GREEN + "Libro guardado: " + libro.getTitle() + RESET);
            return libro;

        } else {
            System.out.println(RED + "No se encontró ningún libro con ese título en Gutendex." + RESET);
            return null;
        }
    }

    /**
     * Lista todos los libros registrados en la base de datos.
     * @return Una lista de objetos Libro.
     */
    public List<Libro> listarTodosLosLibros() {
        return libroRepository.findAll();
    }

    /**
     * Lista libros por idioma de forma paginada.
     * @param idioma El código de idioma (ej. "es", "en").
     * @param pageable Objeto Pageable para control de paginación.
     * @return Una página de objetos Libro.
     */
    public Page<Libro> listarLibrosPorIdiomaPaginado(String idioma, Pageable pageable) {
        return libroRepository.findByIdioma(idioma, pageable);
    }

    /**
     * Muestra estadísticas de descargas de los libros registrados.
     */
    public void mostrarEstadisticasDeDescargas() {
        List<Libro> libros = libroRepository.findAll();
        DoubleSummaryStatistics est = libros.stream()
                .filter(l -> l.getNumeroDeDescargas() != null)
                .mapToDouble(Libro::getNumeroDeDescargas)
                .summaryStatistics();

        System.out.println("\n--- Estadísticas de Descargas ---");
        System.out.println("Media de descargas: " + String.format("%.2f", est.getAverage()));
        System.out.println("Máximo de descargas: " + est.getMax());
        System.out.println("Mínimo de descargas: " + est.getMin());
        System.out.println("Total de libros con descargas: " + est.getCount());
        System.out.println("---------------------------------\n");
    }

    /**
     * Obtiene y muestra el top 10 de libros más descargados.
     * @return Una lista del top 10 de libros.
     */
    public List<Libro> obtenerTop10LibrosMasDescargados() {
        return libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();
    }

    /**
     * Metodo de utilidad para mostrar los detalles de una lista de libros.
     * (Idealmente, la impresión podría estar en la capa de UI/MenuPrincipal, pero se mantiene aquí para la refactorización actual).
     * @param libros La lista de libros a mostrar.
     */
    public void mostrarDetallesLibros(List<Libro> libros) {
        if (libros.isEmpty()) {
            System.out.println(RED + "\nNo se encontraron libros con ese criterio." + RESET);
        } else {
            System.out.println(YELLOW + "\n--- Libros Encontrados ---" + RESET);
            libros.forEach(l -> {
                System.out.println(CYAN + "--------------------------------------" + RESET);
                System.out.println("Título: " + l.getTitle());
                if (l.getAutores() != null && !l.getAutores().isEmpty()) {
                    System.out.println("Autor(es): " + l.getAutores().stream()
                            .map(Autor::getName)
                            .collect(Collectors.joining(", ")));
                } else {
                    System.out.println("Autor(es): Desconocido");
                }
                System.out.println("Idioma(s): " + l.getIdioma());
                System.out.println("Número de Descargas: " + l.getNumeroDeDescargas());
            });
            System.out.println(CYAN + "--------------------------------------" + RESET);
        }
    }
}