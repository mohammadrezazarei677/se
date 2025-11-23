package com.library.model;

public class Student extends User {
    private int totalBorrows;
    private int delayedReturns;
    private int notReturnedBooks;

    public Student(String username, String password) {
        super(username, password);
        this.totalBorrows = 0;
        this.delayedReturns = 0;
        this.notReturnedBooks = 0;
    }

    // Getters and Setters
    public int getTotalBorrows() { return totalBorrows; }
    public void setTotalBorrows(int totalBorrows) { this.totalBorrows = totalBorrows; }

    public int getDelayedReturns() { return delayedReturns; }
    public void setDelayedReturns(int delayedReturns) { this.delayedReturns = delayedReturns; }

    public int getNotReturnedBooks() { return notReturnedBooks; }
    public void setNotReturnedBooks(int notReturnedBooks) { this.notReturnedBooks = notReturnedBooks; }

    public void incrementTotalBorrows() { this.totalBorrows++; }
    public void incrementDelayedReturns() { this.delayedReturns++; }
    public void incrementNotReturnedBooks() { this.notReturnedBooks++; }
    public void decrementNotReturnedBooks() {
        if (this.notReturnedBooks > 0) this.notReturnedBooks--;
    }
}