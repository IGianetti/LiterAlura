package com.challenge.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespuestaApiGutendex { // Puedes mantenerla como clase si prefieres, aunque un record es más conciso para DTOs
    // Puedes mantener 'count' si lo necesitas para algo, si no, puedes quitarlo.
    private Long count;

    @JsonAlias("results") // Mapea el campo 'results' del JSON a 'libros'
    private List<DatosLibro> libros; // <--- Ahora mapea a DatosLibro

    // Getters y Setters
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }


    public List<DatosLibro> getLibros() {
        return libros;
    }


    public void setLibros(List<DatosLibro> libros) {
        this.libros = libros;
    }
}