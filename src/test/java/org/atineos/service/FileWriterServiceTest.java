package org.atineos.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileWriterServiceTest {

    private final FileWriterService fileWriterService = new FileWriterService();

    @Test
    void write_authors_to_file_writes_all_authors_successfully(@TempDir Path tempDir) throws IOException {
        var outputFile = tempDir.resolve("authors.txt");
        var authors = Set.of("Author 1", "Author 2", "Author 3");

        fileWriterService.writeAuthorsToFile(authors, outputFile);

        var content = Files.readString(outputFile);
        assertTrue(content.contains("Author 1"));
        assertTrue(content.contains("Author 2"));
        assertTrue(content.contains("Author 3"));
    }
}