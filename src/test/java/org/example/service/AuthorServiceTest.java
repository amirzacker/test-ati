package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.example.dto.Author;
import org.example.dto.Book;
import org.example.dto.GutendexResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorServiceTest {

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorService();
    }

    @Test
    void authors_with_duplicates_returns_only_unique() {
        var tolkien = new Author("Tolkien, J. R. R.");
        var rowling = new Author("Rowling, J. K.");
        var asimov = new Author("Asimov, Isaac");

        var book1 = new Book(List.of(tolkien, rowling));
        var book2 = new Book(List.of(asimov, tolkien));

        var response1 = new GutendexResponse("nextUrl", List.of(book1));
        var response2 = new GutendexResponse(null, List.of(book2));

        var responses = List.of(response1, response2);

        var uniqueAuthors = authorService.extractAuthors(responses);

        assertEquals(3, uniqueAuthors.size());
        
        var sortedAuthors = List.copyOf(uniqueAuthors);
        assertEquals("Asimov, Isaac", sortedAuthors.get(0));
        assertEquals("Rowling, J. K.", sortedAuthors.get(1));
        assertEquals("Tolkien, J. R. R.", sortedAuthors.get(2));
    }

    @Test
    void authors_with_empty_and_null_values_handles_gracefully() {
        var validAuthor = new Author("Valid Author");
        var emptyAuthor = new Author("");
        var nullAuthor = new Author(null);
        
        var bookWithInvalidAuthors = new Book(List.of(validAuthor, emptyAuthor, nullAuthor));
        var bookWithNullAuthorsList = new Book(null);
        
        var response = new GutendexResponse(null, List.of(bookWithInvalidAuthors, bookWithNullAuthorsList));

        var uniqueAuthors = authorService.extractAuthors(List.of(response));

        assertEquals(1, uniqueAuthors.size());
        assertTrue(uniqueAuthors.contains("Valid Author"));
    }

    @Test
    void authors_with_null_responses_returns_empty() {
        var uniqueAuthors = authorService.extractAuthors(null);
        assertTrue(uniqueAuthors.isEmpty());
    }
}
