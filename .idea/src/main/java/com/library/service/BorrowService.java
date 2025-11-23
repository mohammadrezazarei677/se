package com.library.service;

import com.library.repository.*;
import com.library.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BorrowService {
    private BorrowRepository borrowRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public BorrowService(BorrowRepository borrowRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // 1-4 ثبت درخواست امانت کتاب
    public boolean requestBorrow(String requestId, String studentUsername, String bookId,
                                 LocalDate startDate, LocalDate endDate) {
        // بررسی وجود دانشجو و فعال بودن آن
        Optional<Student> student = userRepository.findStudentByUsername(studentUsername);
        if (!student.isPresent() || !student.get().isActive()) {
            return false;
        }

        // بررسی وجود کتاب و موجود بودن آن
        Optional<Book> book = bookRepository.findById(bookId);
        if (!book.isPresent() || !book.get().isAvailable()) {
            return false;
        }

        BorrowRequest request = new BorrowRequest(requestId, studentUsername, bookId, startDate, endDate);
        return borrowRepository.addBorrowRequest(request);
    }

    // 3-5 تایید درخواست امانت کتاب
    public boolean approveBorrowRequest(String requestId, String employeeUsername) {
        Optional<BorrowRequest> request = borrowRepository.findBorrowRequestById(requestId);
        if (!request.isPresent() || request.get().getStatus() != BorrowRequest.RequestStatus.PENDING) {
            return false;
        }

        // بررسی وجود کارمند
        if (!userRepository.findEmployeeByUsername(employeeUsername).isPresent()) {
            return false;
        }

        BorrowRequest borrowRequest = request.get();
        borrowRequest.setStatus(BorrowRequest.RequestStatus.APPROVED);

        // ایجاد رکورد امانت
        BorrowRecord record = new BorrowRecord(
                "REC_" + requestId,
                borrowRequest.getStudentUsername(),
                borrowRequest.getBookId(),
                employeeUsername,
                borrowRequest.getStartDate(),
                borrowRequest.getEndDate()
        );

        // به‌روزرسانی وضعیت کتاب
        bookRepository.findById(borrowRequest.getBookId()).ifPresent(book -> {
            book.setAvailable(false);
            bookRepository.updateBook(book);
        });

        // به‌روزرسانی آمار دانشجو
        userRepository.findStudentByUsername(borrowRequest.getStudentUsername()).ifPresent(student -> {
            student.incrementTotalBorrows();
            student.incrementNotReturnedBooks();
            userRepository.updateStudent(student);
        });

        // به‌روزرسانی آمار کارمند
        userRepository.findEmployeeByUsername(employeeUsername).ifPresent(employee -> {
            employee.incrementBooksLent();
            userRepository.updateEmployee(employee);
        });

        return borrowRepository.addBorrowRecord(record) && borrowRepository.updateBorrowRequest(borrowRequest);
    }

    // 3-8 دریافت کتاب بازگردانده شده
    public boolean returnBook(String recordId, String employeeUsername) {
        Optional<BorrowRecord> record = borrowRepository.findBorrowRecordById(recordId);
        if (!record.isPresent() || record.get().isReturned()) {
            return false;
        }

        // بررسی وجود کارمند
        if (!userRepository.findEmployeeByUsername(employeeUsername).isPresent()) {
            return false;
        }

        BorrowRecord borrowRecord = record.get();
        borrowRecord.setReturned(true);
        borrowRecord.setReturnDate(LocalDate.now());

        // بررسی تاخیر
        if (borrowRecord.getReturnDate().isAfter(borrowRecord.getDueDate())) {
            borrowRecord.setDelayed(true);

            // به‌روزرسانی آمار تاخیر دانشجو
            userRepository.findStudentByUsername(borrowRecord.getStudentUsername()).ifPresent(student -> {
                student.incrementDelayedReturns();
                userRepository.updateStudent(student);
            });
        }

        // به‌روزرسانی وضعیت کتاب
        bookRepository.findById(borrowRecord.getBookId()).ifPresent(book -> {
            book.setAvailable(true);
            bookRepository.updateBook(book);
        });

        // به‌روزرسانی آمار دانشجو
        userRepository.findStudentByUsername(borrowRecord.getStudentUsername()).ifPresent(student -> {
            student.decrementNotReturnedBooks();
            userRepository.updateStudent(student);
        });

        // به‌روزرسانی آمار کارمند
        userRepository.findEmployeeByUsername(employeeUsername).ifPresent(employee -> {
            employee.incrementBooksReceived();
            userRepository.updateEmployee(employee);
        });

        return borrowRepository.updateBorrowRecord(borrowRecord);
    }

    // 3-5 مشاهده درخواست‌های در انتظار
    public List<BorrowRequest> getPendingRequestsForToday() {
        return borrowRepository.getPendingRequestsForToday();
    }

    // 3-6 مشاهده تاریخچه امانات دانشجو
    public List<BorrowRecord> getBorrowHistoryByStudent(String studentUsername) {
        return borrowRepository.getBorrowHistoryByStudent(studentUsername);
    }

    public int getTotalBorrowCount() {
        return borrowRepository.getTotalBorrowCount();
    }

    public List<BorrowRecord> getActiveBorrows() {
        return borrowRepository.getActiveBorrows();
    }
}