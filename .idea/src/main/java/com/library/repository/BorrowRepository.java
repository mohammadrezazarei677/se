package com.library.repository;

import com.library.model.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BorrowRepository {
    private Map<String, BorrowRequest> borrowRequests;
    private Map<String, BorrowRecord> borrowRecords;

    public BorrowRepository() {
        this.borrowRequests = new HashMap<>();
        this.borrowRecords = new HashMap<>();
    }

    public boolean addBorrowRequest(BorrowRequest request) {
        if (borrowRequests.containsKey(request.getId())) {
            return false;
        }
        borrowRequests.put(request.getId(), request);
        return true;
    }

    public boolean addBorrowRecord(BorrowRecord record) {
        if (borrowRecords.containsKey(record.getId())) {
            return false;
        }
        borrowRecords.put(record.getId(), record);
        return true;
    }

    public Optional<BorrowRequest> findBorrowRequestById(String id) {
        return Optional.ofNullable(borrowRequests.get(id));
    }

    public Optional<BorrowRecord> findBorrowRecordById(String id) {
        return Optional.ofNullable(borrowRecords.get(id));
    }

    public List<BorrowRequest> getPendingRequestsForToday() {
        LocalDate today = LocalDate.now();
        return borrowRequests.values().stream()
                .filter(request -> request.getStatus() == BorrowRequest.RequestStatus.PENDING)
                .filter(request -> request.getStartDate().isEqual(today) || request.getStartDate().isBefore(today))
                .collect(Collectors.toList());
    }

    public List<BorrowRecord> getBorrowHistoryByStudent(String studentUsername) {
        return borrowRecords.values().stream()
                .filter(record -> record.getStudentUsername().equals(studentUsername))
                .collect(Collectors.toList());
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<>(borrowRecords.values());
    }

    public List<BorrowRequest> getAllBorrowRequests() {
        return new ArrayList<>(borrowRequests.values());
    }

    public boolean updateBorrowRequest(BorrowRequest request) {
        if (borrowRequests.containsKey(request.getId())) {
            borrowRequests.put(request.getId(), request);
            return true;
        }
        return false;
    }

    public boolean updateBorrowRecord(BorrowRecord record) {
        if (borrowRecords.containsKey(record.getId())) {
            borrowRecords.put(record.getId(), record);
            return true;
        }
        return false;
    }

    public List<BorrowRecord> getActiveBorrows() {
        return borrowRecords.values().stream()
                .filter(record -> !record.isReturned())
                .collect(Collectors.toList());
    }

    public int getTotalBorrowCount() {
        return borrowRecords.size();
    }
}