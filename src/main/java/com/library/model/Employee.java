package com.library.model;

public class Employee extends User {
    private int booksRegistered;
    private int booksLent;
    private int booksReceived;

    public Employee(String username, String password) {
        super(username, password);
        this.booksRegistered = 0;
        this.booksLent = 0;
        this.booksReceived = 0;
    }

    // Getters and Setters
    public int getBooksRegistered() { return booksRegistered; }
    public void setBooksRegistered(int booksRegistered) { this.booksRegistered = booksRegistered; }

    public int getBooksLent() { return booksLent; }
    public void setBooksLent(int booksLent) { this.booksLent = booksLent; }

    public int getBooksReceived() { return booksReceived; }
    public void setBooksReceived(int booksReceived) { this.booksReceived = booksReceived; }

    public void incrementBooksRegistered() { this.booksRegistered++; }
    public void incrementBooksLent() { this.booksLent++; }
    public void incrementBooksReceived() { this.booksReceived++; }
}