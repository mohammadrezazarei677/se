package com.library.service;

import com.library.repository.*;
import com.library.model.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportService {
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private BorrowRepository borrowRepository;

    public ReportService(UserRepository userRepository, BookRepository bookRepository, BorrowRepository borrowRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.borrowRepository = borrowRepository;
    }

    // 2-3 اطلاعات آماری ساده برای مهمان
    public GuestStatistics getGuestStatistics() {
        int totalStudents = userRepository.getStudentCount();
        int totalBooks = bookRepository.getTotalBooksCount();
        int totalBorrows = borrowRepository.getTotalBorrowCount();
        int activeBorrows = borrowRepository.getActiveBorrows().size();

        return new GuestStatistics(totalStudents, totalBooks, totalBorrows, activeBorrows);
    }

    // 4-2 مشاهده عملکرد کارمند
    public EmployeePerformance getEmployeePerformance(String employeeUsername) {
        return userRepository.findEmployeeByUsername(employeeUsername)
                .map(employee -> new EmployeePerformance(
                        employee.getUsername(),
                        employee.getBooksRegistered(),
                        employee.getBooksLent(),
                        employee.getBooksReceived()
                ))
                .orElse(null);
    }

    // 4-3 اطلاعات آماری امانات کتاب
    public BorrowStatistics getBorrowStatistics() {
        List<BorrowRecord> allRecords = borrowRepository.getAllBorrowRecords();

        int totalRequests = borrowRepository.getAllBorrowRequests().size();
        int totalBorrows = allRecords.size();

        double averageBorrowDays = allRecords.stream()
                .filter(BorrowRecord::isReturned)
                .mapToLong(record -> ChronoUnit.DAYS.between(record.getBorrowDate(), record.getReturnDate()))
                .average()
                .orElse(0.0);

        return new BorrowStatistics(totalRequests, totalBorrows, averageBorrowDays);
    }

    // 3-6 و 4-4 اطلاعات آماری دانشجویان
    public StudentStatistics getStudentStatistics() {
        List<Student> allStudents = userRepository.getAllStudents();

        int totalStudents = allStudents.size();
        int totalBorrows = allStudents.stream().mapToInt(Student::getTotalBorrows).sum();
        int totalNotReturned = allStudents.stream().mapToInt(Student::getNotReturnedBooks).sum();
        int totalDelayed = allStudents.stream().mapToInt(Student::getDelayedReturns).sum();

        // 10 دانشجوی با بیشترین تاخیر
        List<Student> topDelayedStudents = allStudents.stream()
                .sorted(Comparator.comparingInt(Student::getDelayedReturns).reversed())
                .limit(10)
                .collect(Collectors.toList());

        return new StudentStatistics(totalStudents, totalBorrows, totalNotReturned, totalDelayed, topDelayedStudents);
    }

    // 3-6 گزارش تاریخچه امانات یک دانشجو
    public StudentBorrowHistory getStudentBorrowHistory(String studentUsername) {
        List<BorrowRecord> history = borrowRepository.getBorrowHistoryByStudent(studentUsername);
        Optional<Student> student = userRepository.findStudentByUsername(studentUsername);

        if (student.isEmpty()) {
            return null;
        }

        return new StudentBorrowHistory(
                student.get(),
                history,
                history.size(),
                student.get().getNotReturnedBooks(),
                student.get().getDelayedReturns()
        );
    }

    // کلاس‌های کمکی برای گزارش‌ها
    public static class GuestStatistics {
        public final int totalStudents;
        public final int totalBooks;
        public final int totalBorrows;
        public final int activeBorrows;

        public GuestStatistics(int totalStudents, int totalBooks, int totalBorrows, int activeBorrows) {
            this.totalStudents = totalStudents;
            this.totalBooks = totalBooks;
            this.totalBorrows = totalBorrows;
            this.activeBorrows = activeBorrows;
        }
    }

    public static class EmployeePerformance {
        public final String username;
        public final int booksRegistered;
        public final int booksLent;
        public final int booksReceived;

        public EmployeePerformance(String username, int booksRegistered, int booksLent, int booksReceived) {
            this.username = username;
            this.booksRegistered = booksRegistered;
            this.booksLent = booksLent;
            this.booksReceived = booksReceived;
        }
    }

    public static class BorrowStatistics {
        public final int totalRequests;
        public final int totalBorrows;
        public final double averageBorrowDays;

        public BorrowStatistics(int totalRequests, int totalBorrows, double averageBorrowDays) {
            this.totalRequests = totalRequests;
            this.totalBorrows = totalBorrows;
            this.averageBorrowDays = averageBorrowDays;
        }
    }

    public static class StudentStatistics {
        public final int totalStudents;
        public final int totalBorrows;
        public final int totalNotReturned;
        public final int totalDelayed;
        public final List<Student> topDelayedStudents;

        public StudentStatistics(int totalStudents, int totalBorrows, int totalNotReturned,
                                 int totalDelayed, List<Student> topDelayedStudents) {
            this.totalStudents = totalStudents;
            this.totalBorrows = totalBorrows;
            this.totalNotReturned = totalNotReturned;
            this.totalDelayed = totalDelayed;
            this.topDelayedStudents = topDelayedStudents;
        }
    }

    public static class StudentBorrowHistory {
        public final Student student;
        public final List<BorrowRecord> history;
        public final int totalBorrows;
        public final int notReturnedBooks;
        public final int delayedReturns;

        public StudentBorrowHistory(Student student, List<BorrowRecord> history, int totalBorrows,
                                    int notReturnedBooks, int delayedReturns) {
            this.student = student;
            this.history = history;
            this.totalBorrows = totalBorrows;
            this.notReturnedBooks = notReturnedBooks;
            this.delayedReturns = delayedReturns;
        }
    }
}