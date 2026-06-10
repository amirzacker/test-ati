package org.atineos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.atineos.dto.GutendexResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GutendexClientTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private GutendexClient gutendexClient;

    @Test
    void get_books_when_api_call_is_successful_and_returns_response() throws IOException, InterruptedException {
        var url = "https://api.com/books";
        var jsonResponse = "{\"next\": \"some-url\", \"results\": []}";
        var expectedResponse = new GutendexResponse("some-url", null);

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(jsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(objectMapper.readValue(jsonResponse, GutendexResponse.class)).thenReturn(expectedResponse);

        var actualResponse = gutendexClient.getBooks(url);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }
}