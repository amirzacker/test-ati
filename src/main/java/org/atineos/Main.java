package org.atineos;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.atineos.dto.GutendexResponse;
import org.atineos.service.AuthorService;
import org.atineos.service.FileWriterService;
import org.atineos.service.GutendexClient;

@Slf4j
public class Main {

    private static final String INITIAL_URL = "https://gutendex.com/books/";
    private static final int MAX_PAGES = 5;
    private static final String OUTPUT_FILE = "authors.txt";

    static void main() {
        var client = new GutendexClient();
        var authorService = new AuthorService();
        var fileWriterService = new FileWriterService();

        log.info("Starting Gutendex API consumption to fetch authors...");
        
        List<GutendexResponse> responses = fetchAllPages(client);

        if (!responses.isEmpty()) {
            processAndWriteAuthors(responses, authorService, fileWriterService);
        } else {
            log.warn("No data fetched from the API. Process halted.");
        }
    }

    private static List<GutendexResponse> fetchAllPages(GutendexClient client) {
        List<GutendexResponse> responses = new ArrayList<>();
        var currentUrl = INITIAL_URL;
        int pageCount = 0;

        while (currentUrl != null && pageCount < MAX_PAGES) {
            try {
                var response = client.getBooks(currentUrl);
                responses.add(response);
                currentUrl = response.next();
                pageCount++;
                log.info("Successfully fetched page {}", pageCount);
            } catch (IOException | InterruptedException e) {
                log.error("Error occurred while fetching data from API: ", e);
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                break;
            }
        }
        return responses;
    }

    private static void processAndWriteAuthors(List<GutendexResponse> responses, AuthorService authorService, FileWriterService fileWriterService) {
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
