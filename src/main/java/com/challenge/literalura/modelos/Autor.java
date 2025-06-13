package com.challenge.literalura.modelos;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Nombre del autor

    private Integer birthYear; // Año de nacimiento
    private Integer deathYear; // Año de fallecimiento

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER) // Mapeado por el campo "autores" en la entidad Libro
    private Set<Libro> libros = new HashSet<>(); // Libros asociados a este autor

    // Constructor vacío requerido por JPA
    public Autor() {}

    // ¡NUEVO CONSTRUCTOR PARA DATOS DE LA API!
    public Autor(DatosAutor datosAutor) {
        this.name = datosAutor.nombre();
        this.birthYear = datosAutor.anoNacimiento();
        this.deathYear = datosAutor.anoFallecimiento();
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return """
                Autor: %s (Nacimiento: %s, Fallecimiento: %s)
                """.formatted(name,
                birthYear != null ? String.valueOf(birthYear) : "N/A",
                deathYear != null ? String.valueOf(deathYear) : "N/A");
    }
}