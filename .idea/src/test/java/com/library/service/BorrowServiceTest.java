package com.library.service;

import com.library.repository.BorrowRepository;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import com.library.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BorrowServiceTest {
    private BorrowRepository borrowRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private BorrowService borrowService;

    @BeforeEach
    void setUp() {
        borrowRepository = new BorrowRepository();
        bookRepository = new BookRepository();
        userRepository = new UserRepository();
        borrowService = new BorrowService(borrowRepository, bookRepository, userRepository);

        // Setup test data
        userRepository.addStudent(new Student("student1", "password"));
        userRepository.addEmployee(new Employee("emp1", "password"));
        bookRepository.addBook(new Book("B001", "Java Programming", "John Doe", 2023, "emp1"));
    }

    @Test
    void testRequestBorrow_Success() {
        boolean result = borrowService.requestBorrow(
                "REQ001", "student1", "B001",
                LocalDate.now(), LocalDate.now().plusDays(7)
        );
        assertTrue(result);
    }

    @Test
    void testRequestBorrow_StudentNotActive() {
        userRepository.findStudentByUsername("student1").ifPresent(student ->
                student.setActive(false)
        );

        boolean result = borrowService.requestBorrow(
                "REQ001", "student1", "B001",
                LocalDate.now(), LocalDate.now().plusDays(7)
        );
        assertFalse(result);
    }

    @Test
    void testApproveBorrowRequest_Success() {
        // First request a borrow
        borrowService.requestBorrow("REQ001", "student1", "B001",
                LocalDate.now(), LocalDate.now().plusDays(7));

        boolean result = borrowService.approveBorrowRequest("REQ001", "emp1");
        assertTrue(result);

        // Check if book is no longer available
        var book = bookRepository.findById("B001");
        assertTrue(book.isPresent());
        assertFalse(book.get().isAvailable());
    }

    @Test
    void testReturnBook_Success() {
        // Setup: request and approve a borrow
        borrowService.requestBorrow("REQ001", "student1", "B001",
                LocalDate.now(), LocalDate.now().plusDays(7));
        borrowService.approveBorrowRequest("REQ001", "emp1");

        // Find the created record
        var records = borrowRepository.getAllBorrowRecords();
        assertEquals(1, records.size());
        String recordId = records.get(0).getId();

        boolean result = borrowService.returnBook(recordId, "emp1");
        assertTrue(result);

        // Check if book is available again
        var book = bookRepository.findById("B001");
        assertTrue(book.isPresent());
        assertTrue(book.get().isAvailable());
    }
}