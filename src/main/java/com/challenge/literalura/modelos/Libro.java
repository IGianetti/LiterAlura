package com.challenge.literalura.modelos;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long gutendexId;

    @Column(unique = true)
    private String title;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>(); //

    private String idioma;

    private Integer numeroDeDescargas;

    public Libro() {}

    public Libro(DatosLibro datosLibro) {
        this.gutendexId = datosLibro.idGutendex();
        this.title = datosLibro.titulo();
        this.idioma = datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty() ? datosLibro.idiomas().get(0) : "N/A";
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
        this.autores = new HashSet<>(); //
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGutendexId() { return gutendexId; }
    public void setGutendexId(Long gutendexId) { this.gutendexId = gutendexId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Set<Autor> getAutores() { return autores; }
    public void setAutores(Set<Autor> autores) { this.autores = autores; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public Integer getNumeroDeDescargas() { return numeroDeDescargas; }
    public void setNumeroDeDescargas(Integer numeroDeDescargas) { this.numeroDeDescargas = numeroDeDescargas; }

    // Metodo para añadir autor (ahora consistente)
    public void addAutor(Autor autor) {
        // No necesitamos el if (this.autores == null) si ya lo inicializamos arriba.
        this.autores.add(autor); // Añade al Set<Autor>

        // Asegura la relación bidireccional en el autor
        if (autor.getLibros() == null) {
            autor.setLibros(new HashSet<>()); // Inicializa el Set<Libro> del autor si es nulo
        }
        autor.getLibros().add(this); // Añade 'este libro' al Set<Libro> del autor
    }

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