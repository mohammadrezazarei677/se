package com.library.model;

import java.time.LocalDate;

public class BorrowRecord {
    private String id;
    private String studentUsername;
    private String bookId;
    private String employeeUsername;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;
    private boolean isDelayed;

    public BorrowRecord(String id, String studentUsername, String bookId, String employeeUsername,
                        LocalDate borrowDate, LocalDate dueDate) {
        this.id = id;
        this.studentUsername = studentUsername;
        this.bookId = bookId;
        this.employeeUsername = employeeUsername;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.isReturned = false;
        this.isDelayed = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String studentUsername) { this.studentUsername = studentUsername; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getEmployeeUsername() { return employeeUsername; }
    public void setEmployeeUsername(String employeeUsername) { this.employeeUsername = employeeUsername; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() { return isReturned; }
    public void setReturned(boolean returned) { isReturned = returned; }

    public boolean isDelayed() { return isDelayed; }
    public void setDelayed(boolean delayed) { isDelayed = delayed; }
}