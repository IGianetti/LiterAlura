
package com.challenge.literalura.service;

public interface ConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}