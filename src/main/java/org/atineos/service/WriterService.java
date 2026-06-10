package org.atineos.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

@FunctionalInterface
public interface WriterService {
    void writeAuthorsToFile(Set<String> authors, Path path) throws IOException;
}