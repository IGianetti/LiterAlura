package com.challenge.literalura.repository;

import com.challenge.literalura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Buscar autor por nombre exacto (podría devolver null o un Optional si no existe)
    Autor findByName(String name);

    // Consulta para encontrar autores vivos en un determinado año
    // Un autor está vivo en un año si su año de nacimiento es menor o igual al año
    // Y su año de fallecimiento es mayor o igual al año O su año de fallecimiento es NULO
    @Query("SELECT a FROM Autor a WHERE a.birthYear <= :year AND (a.deathYear >= :year OR a.deathYear IS NULL)")
    List<Autor> findAuthorsAliveInYear(@Param("year") Integer year);

    // Buscar autores por nombre que contenga la cadena (ignorando mayúsculas/minúsculas)
    List<Autor> findByNameContainingIgnoreCase(String nombre);

    // Buscar autores nacidos después de un año
    List<Autor> findByBirthYearGreaterThan(Integer year);

    // Buscar autores fallecidos antes de un año
    List<Autor> findByDeathYearLessThan(Integer year);

    // Buscar por un año de nacimiento exacto
    List<Autor> findByBirthYear(Integer year);

    // Buscar por un año de fallecimiento exacto
    List<Autor> findByDeathYear(Integer year);

    // --- METODO ROBUSTO PARA BÚSQUEDA FLEXIBLE DE AUTORES ---
    @Query("SELECT a FROM Autor a WHERE " +
            // Normaliza el nombre del autor (quita ', ', '.', ' ') para comparar
            "LOWER(REPLACE(REPLACE(REPLACE(a.name, ', ', ''), '.', ''), ' ', '')) " +
            "LIKE " +
            // Compara con la búsqueda original normalizada
            "LOWER(CONCAT('%', REPLACE(REPLACE(:originalNormalized, ' ', ''), '.', ''), '%')) OR " +
            // Compara con la búsqueda invertida normalizada
            "LOWER(REPLACE(REPLACE(REPLACE(a.name, ', ', ''), '.', ''), ' ', '')) " +
            "LIKE " +
            "LOWER(CONCAT('%', REPLACE(REPLACE(:invertedNormalized, ' ', ''), '.', ''), '%'))")
    List<Autor> findFlexibleByName(@Param("originalNormalized") String originalNormalized,
                                   @Param("invertedNormalized") String invertedNormalized);
}