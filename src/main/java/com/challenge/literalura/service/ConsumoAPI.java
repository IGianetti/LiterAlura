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

    @Value("${api.gutendex.url}") // ¡Inyecta la URL desde application.properties!
    private String URL_BASE; // Ahora se inyectará, no será hardcodeada

    public String obtenerDatos(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
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