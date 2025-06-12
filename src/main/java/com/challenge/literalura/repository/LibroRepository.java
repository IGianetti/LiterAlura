package com.challenge.literalura.repository;

import com.challenge.literalura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {


    List<Libro> findByIdioma(String idioma); // Derived query para listar por idioma

    // Metodo para buscar un libro por su gutendexId (si ya lo tienes, verifica que sea así)
    Libro findByGutendexId(Long gutendexId);
}