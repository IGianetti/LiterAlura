package com.challenge.literalura.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ConsumoAPI {

    // Si api.gutendex.url no se usa en este metodo, puedes quitar esta línea para este ConsumoAPI
    // Pero si tienes otros métodos en ConsumoAPI que sí necesiten una URL base para construir peticiones, está bien dejarla.
    @Value("${api.gutendex.url}")
    private String URL_BASE;

    // Este metodo recibe la URL completa y la consume.
    public String obtenerDatos(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)) // Aquí usa la 'url' que recibe directamente
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al obtener datos de la API: " + e.getMessage());
            throw new RuntimeException("Error de conexión o interrupción en ConsumoAPI: " + e.getMessage(), e);
        }
        return response.body();
    }
}