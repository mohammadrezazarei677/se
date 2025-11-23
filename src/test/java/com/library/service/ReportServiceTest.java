package com.library.service;

import com.library.repository.UserRepository;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReportServiceTest {
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private BorrowRepository borrowRepository;
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        bookRepository = new BookRepository();
        borrowRepository = new BorrowRepository();
        reportService = new ReportService(userRepository, bookRepository, borrowRepository);
    }

    @Test
    void testGetGuestStatistics_EmptyData() {
        var stats = reportService.getGuestStatistics();

        // فقط مدیر پیش‌فرض وجود دارد
        assertEquals(0, stats.totalStudents);
        assertEquals(0, stats.totalBooks);
        assertEquals(0, stats.totalBorrows);
        assertEquals(0, stats.activeBorrows);
    }

    @Test
    void testGetGuestStatistics_WithData() {
        // اضافه کردن داده‌های تست
        userRepository.addStudent(new Student("student1", "password"));
        userRepository.addStudent(new Student("student2", "password"));
        bookRepository.addBook(new Book("B001", "Java", "Author", 2023, "admin"));
        bookRepository.addBook(new Book("B002", "Python", "Author", 2023, "admin"));

        var stats = reportService.getGuestStatistics();

        assertEquals(2, stats.totalStudents);
        assertEquals(2, stats.totalBooks);
        assertEquals(0, stats.totalBorrows);
        assertEquals(0, stats.activeBorrows);
    }

    @Test
    void testGetEmployeePerformance_EmployeeNotFound() {
        var performance = reportService.getEmployeePerformance("nonexistent");
        assertNull(performance);
    }

    @Test
    void testGetBorrowStatistics_EmptyData() {
        var stats = reportService.getBorrowStatistics();

        assertEquals(0, stats.totalRequests);
        assertEquals(0, stats.totalBorrows);
        assertEquals(0.0, stats.averageBorrowDays);
    }

    @Test
    void testGetStudentStatistics_EmptyData() {
        var stats = reportService.getStudentStatistics();

        assertEquals(0, stats.totalStudents);
        assertEquals(0, stats.totalBorrows);
        assertEquals(0, stats.totalNotReturned);
        assertEquals(0, stats.totalDelayed);
        assertNotNull(stats.topDelayedStudents);
        assertTrue(stats.topDelayedStudents.isEmpty());
    }

    @Test
    void testGetStudentStatistics_WithStudents() {
        // اضافه کردن دانشجویان با داده‌های مختلف
        Student student1 = new Student("s1", "pass");
        student1.setTotalBorrows(5);
        student1.setDelayedReturns(2);
        student1.setNotReturnedBooks(1);
        userRepository.addStudent(student1);

        Student student2 = new Student("s2", "pass");
        student2.setTotalBorrows(3);
        student2.setDelayedReturns(1);
        student2.setNotReturnedBooks(0);
        userRepository.addStudent(student2);

        var stats = reportService.getStudentStatistics();

        assertEquals(2, stats.totalStudents);
        assertEquals(8, stats.totalBorrows); // 5 + 3
        assertEquals(1, stats.totalNotReturned); // فقط student1 کتاب تحویل نداده
        assertEquals(3, stats.totalDelayed); // 2 + 1

        // لیست topDelayedStudents باید شامل دانشجویان باشد
        assertNotNull(stats.topDelayedStudents);
        assertEquals(2, stats.topDelayedStudents.size());
    }

    @Test
    void testGetStudentBorrowHistory_StudentNotFound() {
        var history = reportService.getStudentBorrowHistory("nonexistent");
        assertNull(history);
    }

}