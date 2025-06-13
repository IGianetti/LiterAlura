package com.challenge.literalura.service;

import com.challenge.literalura.modelos.Autor;
import com.challenge.literalura.modelos.Libro; // Necesario para los títulos de libros
import com.challenge.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    // listar todos los autores registrados
    public List<Autor> listarTodosLosAutores() {
        return autorRepository.findAll();
    }

    //  listar autores vivos en un año específico
    public List<Autor> listarAutoresVivosEnAno(Integer year) {
        return autorRepository.findAuthorsAliveInYear(year);
    }

    // para buscar autores por criterio flexible (nombre, año de nacimiento, año de fallecimiento)
    // Esto encapsula la lógica que estaba directamente en Principal.buscarAutoresPorCriterio()
    public List<Autor> buscarAutoresPorCriterio(int opcionBusqueda, String busquedaInput) {
        List<Autor> autoresEncontrados = null;

        switch (opcionBusqueda) {
            case 1: // Búsqueda flexible por nombre
                String originalSearch = busquedaInput;
                String invertedSearch = busquedaInput;
                String[] palabras = busquedaInput.split(" ");
                if (palabras.length >= 2) {
                    invertedSearch = palabras[palabras.length - 1] + " " + String.join(" ", java.util.Arrays.copyOfRange(palabras, 0, palabras.length - 1));
                }
                autoresEncontrados = autorRepository.findFlexibleByName(originalSearch, invertedSearch);
                break;
            case 2: // Por año de nacimiento
                Integer anoNacimiento = Integer.valueOf(busquedaInput);
                autoresEncontrados = autorRepository.findByBirthYear(anoNacimiento);
                break;
            case 3: // Por año de fallecimiento
                Integer anoFallecimiento = Integer.valueOf(busquedaInput);
                autoresEncontrados = autorRepository.findByDeathYear(anoFallecimiento);
                break;
            // No se necesita el '0' aquí, ya que el menú lo manejará.
            default:
                // Deberías manejar esto en el menú principal antes de llamar al servicio
                autoresEncontrados = List.of(); // Devuelve una lista vacía para opción inválida
                break;
        }
        return autoresEncontrados;
    }

    // ara mostrar detalles de autores (esto es más de presentación, pero lo incluimos para la refactorización)
    public void mostrarDetallesAutores(List<Autor> autores) {
        if (autores.isEmpty()) {
            System.out.println("\nNo se encontraron autores con ese criterio.");
        } else {
            System.out.println("\n--- Autores Encontrados ---");
            autores.forEach(a -> {
                System.out.println("--------------------------------------");
                System.out.println("Nombre: " + a.getName());
                System.out.println("Año de Nacimiento: " + (a.getBirthYear() != null ? a.getBirthYear() : "N/A"));
                System.out.println("Año de Fallecimiento: " + (a.getDeathYear() != null ? a.getDeathYear() : "N/A"));
                int cantidadLibros = (a.getLibros() != null) ? a.getLibros().size() : 0;
                System.out.println("Cantidad de Libros: " + cantidadLibros);
                if (cantidadLibros > 0) {
                    System.out.println("Títulos: " + a.getLibros().stream()
                            .map(Libro::getTitle)
                            .collect(Collectors.joining("; ")));
                }
            });
            System.out.println("--------------------------------------");
        }
    }
}