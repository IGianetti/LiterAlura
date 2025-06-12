package com.challenge.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor( // Este record mapea un solo objeto autor dentro de la respuesta JSON
                          @JsonAlias("name") String nombre,
                          @JsonAlias("birth_year") Integer anoNacimiento,
                          @JsonAlias("death_year") Integer anoFallecimiento
) {}