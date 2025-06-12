package com.challenge.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro( // Este record mapea un solo objeto libro dentro de la respuesta JSON
                          @JsonAlias("id") Long idGutendex, // Este es el ID original de Gutendex
                          @JsonAlias("title") String titulo,
                          @JsonAlias("authors") List<DatosAutor> autores, // La API devuelve una lista de autores para cada libro
                          @JsonAlias("languages") List<String> idiomas,
                          @JsonAlias("download_count") Integer numeroDeDescargas
) {}