package com.library.repository;

import com.library.model.Book;
import java.util.*;
import java.util.stream.Collectors;

public class BookRepository {
    private Map<String, Book> books;

    public BookRepository() {
        this.books = new HashMap<>();
    }

    public boolean addBook(Book book) {
        if (books.containsKey(book.getId())) {
            return false;
        }
        books.put(book.getId(), book);
        return true;
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> findByTitle(String title) {
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooks(String title, String author, Integer publicationYear) {
        return books.values().stream()
                .filter(book -> title == null || book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(book -> author == null || book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .filter(book -> publicationYear == null || book.getPublicationYear() == publicationYear)
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public int getTotalBooksCount() {
        return books.size();
    }

    public boolean updateBook(Book book) {
        if (books.containsKey(book.getId())) {
            books.put(book.getId(), book);
            return true;
        }
        return false;
    }

    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Book> getBorrowedBooks() {
        return books.values().stream()
                .filter(book -> !book.isAvailable())
                .collect(Collectors.toList());
    }
}