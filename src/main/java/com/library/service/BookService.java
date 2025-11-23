package com.library.service;

import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import com.library.model.Book;
import java.util.List;
import java.util.Optional;

public class BookService {
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // 3-3 ثبت اطلاعات کتاب
    public boolean registerBook(String id, String title, String author, int publicationYear, String employeeUsername) {
        if (!userRepository.findEmployeeByUsername(employeeUsername).isPresent()) {
            return false;
        }

        Book book = new Book(id, title, author, publicationYear, employeeUsername);
        boolean success = bookRepository.addBook(book);

        if (success) {
            userRepository.findEmployeeByUsername(employeeUsername).ifPresent(employee ->
                    employee.incrementBooksRegistered());
        }

        return success;
    }

    // 1-3 جستجوی کتاب (دانشجو)
    public List<Book> searchBooks(String title, String author, Integer publicationYear) {
        return bookRepository.searchBooks(title, author, publicationYear);
    }

    // 2-2 جستجوی کتاب (مهمان) - فقط بر اساس عنوان
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    // 3-4 جستجو و ویرایش اطلاعات کتاب
    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public boolean updateBook(Book book) {
        return bookRepository.updateBook(book);
    }

    // 2-3 اطلاعات آماری
    public int getTotalBooksCount() {
        return bookRepository.getTotalBooksCount();
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.getAvailableBooks();
    }

    public List<Book> getBorrowedBooks() {
        return bookRepository.getBorrowedBooks();
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }
}