package com.challenge.literalura.repository;

import com.challenge.literalura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    /** Podemos añadir un metodo para buscar un autor por nombre para evitar duplicados**/
    Autor findByName(String name);

    // Consulta para encontrar autores vivos en un determinado año
    // Un autor está vivo en un año si su año de nacimiento es menor o igual al año
    // Y su año de fallecimiento es mayor o igual al año O su año de fallecimiento es NULO

    @Query("SELECT a FROM Autor a WHERE a.birthYear <= :year AND (a.deathYear >= :year OR a.deathYear IS NULL)")
    List<Autor> findAuthorsAliveInYear(@Param("year") Integer year);
}