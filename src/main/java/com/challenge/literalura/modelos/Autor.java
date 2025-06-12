package com.challenge.literalura.modelos;

import jakarta.persistence.*; // Importa las anotaciones JPA

import java.util.ArrayList;
import java.util.List;

@Entity // Marca esta clase como una entidad JPA
@Table(name = "autores") // Nombre de la tabla en la base de datos

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID de nuestra base de datos

    @Column(unique = true) // El nombre del autor debe ser único
    private String name;


    private Integer birthYear;


    private Integer deathYear;

    // La relación con Libro. Un autor puede tener muchos libros.
    // MappedBy indica que la relación es bidireccional y la maneja el campo 'autores' en la clase Libro
    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER) // EAGER para cargar libros inmediatamente
    private List<Libro> libros = new ArrayList<>(); // ¡Inicializa la lista aquí para evitar NPEs!

    // Constructor vacío necesario para JPA
    public Autor() {}

    // Constructor para cuando creamos un Autor (entidad DB) a partir de DatosAutor (record de la API)
    public Autor(String name, Integer birthYear, Integer deathYear) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        // La lista de libros no se inicializa aquí porque se gestiona por la relación JPA.
        // Si necesitas que esté lista para añadir libros de inmediato en otro contexto, puedes hacer 'this.libros = new ArrayList<>();'
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }
    public Integer getDeathYear() { return deathYear; }
    public void setDeathYear(Integer deathYear) { this.deathYear = deathYear; }
    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }

    @Override
    public String toString() {
        return "--- Autor ---\n" +
                "Nombre: " + name + "\n" +
                "Año de Nacimiento: " + (birthYear != null ? birthYear : "N/A") + "\n" +
                "Año de Fallecimiento: " + (deathYear != null ? deathYear : "N/A") + "\n" +
                "-------------";
    }
}