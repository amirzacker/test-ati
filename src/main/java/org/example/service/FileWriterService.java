package org.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileWriterService {

    public void writeAuthorsToFile(Set<String> authors, Path filePath) throws IOException {
        log.info("Writing {} authors to file: {}", authors.size(), filePath);
        Files.write(filePath, authors);
        log.info("Successfully wrote authors to file.");
    }
}
