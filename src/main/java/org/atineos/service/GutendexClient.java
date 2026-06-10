package org.atineos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.atineos.dto.GutendexResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class GutendexClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GutendexClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public GutendexResponse getBooks(String url) throws IOException, InterruptedException {
        log.info("Fetching books from URL: {}", url);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            log.error("Failed to fetch books from Gutendex API. Status code: {}", response.statusCode());
            throw new IOException("Failed to fetch books from Gutendex API. Status code: " + response.statusCode());
        }

        return objectMapper.readValue(response.body(), GutendexResponse.class);
    }
}
