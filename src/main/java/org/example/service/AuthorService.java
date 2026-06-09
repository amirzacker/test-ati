package org.example.service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.example.dto.GutendexResponse;

public class AuthorService {

    public Set<String> extractAuthors(List<GutendexResponse> responses) {
        var uniqueAuthors = new TreeSet<String>();

        if (responses != null) {
            responses.stream()
                .filter(response -> response.results() != null)
                .flatMap(response -> response.results().stream())
                .filter(book -> book.authors() != null)
                .flatMap(book -> book.authors().stream())
                .filter(author -> author.name() != null && !author.name().isBlank())
                .map(author -> author.name().trim())
                .forEach(uniqueAuthors::add);
        }

        return uniqueAuthors;
    }
}
