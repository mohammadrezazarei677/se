package com.library.service;

import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import com.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepository();
        userRepository = new UserRepository();
        bookService = new BookService(bookRepository, userRepository);

        // Register an employee for book registration
        userRepository.addEmployee(new com.library.model.Employee("emp1", "password"));
    }

    @Test
    void testRegisterBook_Success() {
        boolean result = bookService.registerBook("B001", "Java Programming", "John Doe", 2023, "emp1");
        assertTrue(result);

        var book = bookService.getBookById("B001");
        assertTrue(book.isPresent());
        assertEquals("Java Programming", book.get().getTitle());
    }

    @Test
    void testRegisterBook_DuplicateId() {
        bookService.registerBook("B001", "Java Programming", "John Doe", 2023, "emp1");
        boolean result = bookService.registerBook("B001", "Another Book", "Jane Doe", 2024, "emp1");
        assertFalse(result);
    }

    @Test
    void testSearchBooks() {
        bookService.registerBook("B001", "Java Programming", "John Doe", 2023, "emp1");
        bookService.registerBook("B002", "Advanced Java", "John Doe", 2024, "emp1");

        var results = bookService.searchBooks("Java", "John Doe", null);
        assertEquals(2, results.size());

        results = bookService.searchBooks("Advanced", null, 2024);
        assertEquals(1, results.size());
        assertEquals("Advanced Java", results.get(0).getTitle());
    }

    @Test
    void testUpdateBook() {
        bookService.registerBook("B001", "Java Programming", "John Doe", 2023, "emp1");

        var book = bookService.getBookById("B001");
        assertTrue(book.isPresent());

        book.get().setTitle("Updated Java Programming");
        boolean result = bookService.updateBook(book.get());
        assertTrue(result);

        var updatedBook = bookService.getBookById("B001");
        assertTrue(updatedBook.isPresent());
        assertEquals("Updated Java Programming", updatedBook.get().getTitle());
    }
}