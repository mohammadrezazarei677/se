package com.library.repository;

import com.library.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

class BookRepositoryTest {
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = new BookRepository();
    }

    @Test
    void testAddBook_Success() {
        // Given
        Book book = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");

        // When
        boolean result = bookRepository.addBook(book);

        // Then
        assertTrue(result);
        Optional<Book> foundBook = bookRepository.findById("B001");
        assertTrue(foundBook.isPresent());
        assertEquals("Java Programming", foundBook.get().getTitle());
        assertTrue(foundBook.get().isAvailable());
    }

    @Test
    void testAddBook_DuplicateId() {
        // Given
        Book book1 = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        Book book2 = new Book("B001", "Advanced Java", "Jane Doe", 2024, "emp1");

        // When
        boolean firstResult = bookRepository.addBook(book1);
        boolean secondResult = bookRepository.addBook(book2);

        // Then
        assertTrue(firstResult);
        assertFalse(secondResult);
    }

    @Test
    void testFindById_Found() {
        // Given
        Book book = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        bookRepository.addBook(book);

        // When
        Optional<Book> foundBook = bookRepository.findById("B001");

        // Then
        assertTrue(foundBook.isPresent());
        assertEquals("Java Programming", foundBook.get().getTitle());
        assertEquals("John Doe", foundBook.get().getAuthor());
        assertEquals(2023, foundBook.get().getPublicationYear());
    }

    @Test
    void testFindById_NotFound() {
        // When
        Optional<Book> foundBook = bookRepository.findById("NONEXISTENT");

        // Then
        assertFalse(foundBook.isPresent());
    }

    @Test
    void testFindByTitle() {
        // Given
        Book book1 = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        Book book2 = new Book("B002", "Advanced Java", "Jane Doe", 2024, "emp1");
        Book book3 = new Book("B003", "Python Basics", "John Smith", 2023, "emp1");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);
        bookRepository.addBook(book3);

        // When
        List<Book> javaBooks = bookRepository.findByTitle("Java");

        // Then
        assertEquals(2, javaBooks.size());
        assertTrue(javaBooks.stream().anyMatch(b -> b.getId().equals("B001")));
        assertTrue(javaBooks.stream().anyMatch(b -> b.getId().equals("B002")));
    }

    @Test
    void testSearchBooks_ByTitleAndAuthor() {
        // Given
        Book book1 = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        Book book2 = new Book("B002", "Advanced Java", "Jane Doe", 2024, "emp1");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        // When
        List<Book> results = bookRepository.searchBooks("Java", "John", null);

        // Then
        assertEquals(1, results.size());
        assertEquals("B001", results.get(0).getId());
    }

    @Test
    void testSearchBooks_ByPublicationYear() {
        // Given
        Book book1 = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        Book book2 = new Book("B002", "Advanced Java", "Jane Doe", 2024, "emp1");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        // When
        List<Book> results = bookRepository.searchBooks(null, null, 2023);

        // Then
        assertEquals(1, results.size());
        assertEquals("B001", results.get(0).getId());
    }

    @Test
    void testSearchBooks_AllNullParameters() {
        // Given
        Book book1 = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        Book book2 = new Book("B002", "Advanced Java", "Jane Doe", 2024, "emp1");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        // When
        List<Book> results = bookRepository.searchBooks(null, null, null);

        // Then
        assertEquals(2, results.size());
    }

    @Test
    void testGetAllBooks() {
        // Given
        Book book1 = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        Book book2 = new Book("B002", "Advanced Java", "Jane Doe", 2024, "emp1");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        // When
        List<Book> allBooks = bookRepository.getAllBooks();

        // Then
        assertEquals(2, allBooks.size());
    }

    @Test
    void testGetTotalBooksCount() {
        // Given
        Book book1 = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        Book book2 = new Book("B002", "Advanced Java", "Jane Doe", 2024, "emp1");
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        // When
        int count = bookRepository.getTotalBooksCount();

        // Then
        assertEquals(2, count);
    }

    @Test
    void testUpdateBook() {
        // Given
        Book book = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        bookRepository.addBook(book);

        book.setTitle("Updated Java Programming");
        book.setAuthor("John Smith");
        book.setPublicationYear(2024);
        book.setAvailable(false);

        // When
        boolean result = bookRepository.updateBook(book);

        // Then
        assertTrue(result);
        Optional<Book> updatedBook = bookRepository.findById("B001");
        assertTrue(updatedBook.isPresent());
        assertEquals("Updated Java Programming", updatedBook.get().getTitle());
        assertEquals("John Smith", updatedBook.get().getAuthor());
        assertEquals(2024, updatedBook.get().getPublicationYear());
        assertFalse(updatedBook.get().isAvailable());
    }

    @Test
    void testGetAvailableBooks() {
        // Given
        Book book1 = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        Book book2 = new Book("B002", "Advanced Java", "Jane Doe", 2024, "emp1");
        book2.setAvailable(false);
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        // When
        List<Book> availableBooks = bookRepository.getAvailableBooks();

        // Then
        assertEquals(1, availableBooks.size());
        assertEquals("B001", availableBooks.get(0).getId());
    }

    @Test
    void testGetBorrowedBooks() {
        // Given
        Book book1 = new Book("B001", "Java Programming", "John Doe", 2023, "emp1");
        Book book2 = new Book("B002", "Advanced Java", "Jane Doe", 2024, "emp1");
        book2.setAvailable(false);
        bookRepository.addBook(book1);
        bookRepository.addBook(book2);

        // When
        List<Book> borrowedBooks = bookRepository.getBorrowedBooks();

        // Then
        assertEquals(1, borrowedBooks.size());
        assertEquals("B002", borrowedBooks.get(0).getId());
    }
}