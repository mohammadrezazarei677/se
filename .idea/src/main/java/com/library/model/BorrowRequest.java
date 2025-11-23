package com.library.model;

import java.time.LocalDate;

public class BorrowRequest {
    private String id;
    private String studentUsername;
    private String bookId;
    private LocalDate startDate;
    private LocalDate endDate;
    private RequestStatus status;

    public BorrowRequest(String id, String studentUsername, String bookId, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.studentUsername = studentUsername;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = RequestStatus.PENDING;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String studentUsername) { this.studentUsername = studentUsername; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED, COMPLETED
    }
}