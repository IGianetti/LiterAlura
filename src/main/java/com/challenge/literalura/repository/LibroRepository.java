package com.challenge.literalura.repository;

import com.challenge.literalura.modelos.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Metodo para buscar un libro por su título, ignorando mayúsculas/minúsculas
    // Spring Data JPA lo implementa automáticamente si el nombre del metodo sigue la convención
    Optional<Libro> findByTitle(String title);

    List<Libro> findByIdioma(String idioma); // Derived query para listar por idioma

    // Metodo para buscar un libro por su gutendexId (si ya lo tienes, verifica que sea así)
    Libro findByGutendexId(Long gutendexId);

    // Metodo para encontrar el top 10 de libros por número de descargas de forma descendente
    List<Libro> findTop10ByOrderByNumeroDeDescargasDesc();

    // Nuevo metodo para encontrar libros por idioma con paginación
    Page<Libro> findByIdioma(String idioma, Pageable pageable);
}