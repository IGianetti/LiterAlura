package com.challenge.literalura.modelos;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity // Indica que esta clase es una entidad JPA
@Table(name = "libros") // Mapea esta entidad a la tabla 'libros' en tu DB

public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID primario generado por la base de datos

    // ELIMINA @JsonAlias("id")
    @Column(unique = true) // Asegura que el ID de Gutendex sea único en tu DB
    private Long gutendexId; // Almacena el ID original del libro de la API Gutendex

    @Column(unique = true) // Asegura que no haya títulos de libros duplicados (si ese es tu deseo)
    private String title;

    // ELIMINA @JsonAlias("authors")
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER) // CascadeType.MERGE es clave para autores
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>(); // Inicializado para evitar NullPointerExceptions

    private String idioma;


    private Integer numeroDeDescargas; // Cambiado a Integer para consistencia

    // --- Constructor vacío, necesario para JPA ---
    public Libro() {}


    // Constructor para crear un objeto Libro (entidad DB) a partir de un DatosLibro (record de la API)
    public Libro(DatosLibro datosLibro) {
        this.gutendexId = datosLibro.idGutendex();
        this.title = datosLibro.titulo();

        // Tomamos solo el primer idioma, como se acordó para simplificar
        this.idioma = datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty() ? datosLibro.idiomas().get(0) : "N/A";
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
        this.autores = new ArrayList<>(); // Se inicializa, la lógica de asignación de autores se hará en Principal.java
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGutendexId() { return gutendexId; }
    public void setGutendexId(Long gutendexId) { this.gutendexId = gutendexId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Autor> getAutores() { return autores; }
    public void setAutores(List<Autor> autores) { this.autores = autores; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public Integer getNumeroDeDescargas() { return numeroDeDescargas; } // Getter con tipo Integer
    public void setNumeroDeDescargas(Integer numeroDeDescargas) { this.numeroDeDescargas = numeroDeDescargas; }


    @Override
    public String toString() {
        return "--- Libro ---\n" +
                "Título: " + title + "\n" +
                "Autores: " + (autores != null && !autores.isEmpty() ? autores.stream().map(Autor::getName).collect(Collectors.joining(", ")) : "N/A") + "\n" +
                "Idioma: " + (idioma != null ? idioma : "N/A") + "\n" +
                "Número de Descargas: " + (numeroDeDescargas != null ? numeroDeDescargas : "N/A") + "\n" +
                "-------------\n";
    }
}