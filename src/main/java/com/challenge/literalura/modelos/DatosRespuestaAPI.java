package com.challenge.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosRespuestaAPI(
        @JsonAlias("count") Integer count,
        @JsonAlias("next") String next,
        @JsonAlias("previous") String previous,
        @JsonAlias("results") List<DatosLibro> results // <-- Aquí mapeamos a tu actual record DatosLibro
) {}