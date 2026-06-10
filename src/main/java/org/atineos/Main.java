package org.atineos;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.atineos.dto.GutendexResponse;
import org.atineos.service.AuthorService;
import org.atineos.service.FileWriterService;
import org.atineos.service.GutendexClient;
import org.atineos.service.WriterService;

@Slf4j
public class Main {

    private static final String INITIAL_URL = "https://gutendex.com/books/";
    private static final int MAX_PAGES = 5;
    private static final String OUTPUT_FILE = "authors.txt";

    static void main() {
        var httpClient = HttpClient.newHttpClient();
        var objectMapper = new ObjectMapper();
        var client = new GutendexClient(httpClient, objectMapper);
        var authorService = new AuthorService();
        var fileWriterService = new FileWriterService();

        log.info("Starting Gutendex API consumption to fetch authors...");
        
        var responses = fetchAllPages(client);

        if (!responses.isEmpty()) {
            processAndWriteAuthors(responses, authorService, fileWriterService);
        } else {
            log.warn("No data fetched from the API. Process halted.");
        }
    }

    private static List<GutendexResponse> fetchAllPages(GutendexClient client) {
        var responses = new ArrayList<GutendexResponse>();
        var currentUrl = new AtomicReference<>(INITIAL_URL);
        var pageCount = new AtomicInteger(0);

        Stream.generate(currentUrl::get)
                .takeWhile(url -> url != null && pageCount.get() < MAX_PAGES)
                .map(url -> {
                    try {
                        var response = client.getBooks(url);
                        currentUrl.set(response.next());
                        pageCount.incrementAndGet();
                        log.info("Successfully fetched page {}", pageCount.get());
                        return response;
                    } catch (IOException | InterruptedException e) {
                        log.error("Error occurred while fetching data from API: ", e);
                        if (e instanceof InterruptedException) {
                            Thread.currentThread().interrupt();
                        }
                        return null;
                    }
                })
                .takeWhile(java.util.Objects::nonNull)
                .forEach(responses::add);

        return responses;
    }

    private static void processAndWriteAuthors(List<GutendexResponse> responses, AuthorService authorService, WriterService fileWriterService) {
        log.info("Extraction of authors from {} pages...", responses.size());
        var uniqueAuthors = authorService.extractAuthors(responses);

        var outputPath = Paths.get(OUTPUT_FILE);
        try {
            fileWriterService.writeAuthorsToFile(uniqueAuthors, outputPath);
            log.info("Process completed successfully.");
        } catch (IOException e) {
            log.error("Error occurred while writing authors to file: ", e);
        }
    }
}