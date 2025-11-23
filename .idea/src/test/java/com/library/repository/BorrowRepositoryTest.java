package com.library.repository;

import com.library.model.BorrowRequest;
import com.library.model.BorrowRecord;
import com.library.model.BorrowRequest.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class BorrowRepositoryTest {
    private BorrowRepository borrowRepository;

    @BeforeEach
    void setUp() {
        borrowRepository = new BorrowRepository();
    }

    @Test
    void testAddBorrowRequest_Success() {
        // Given
        BorrowRequest request = new BorrowRequest("REQ001", "student1", "B001",
                LocalDate.now(), LocalDate.now().plusDays(7));

        // When
        boolean result = borrowRepository.addBorrowRequest(request);

        // Then
        assertTrue(result);
        Optional<BorrowRequest> foundRequest = borrowRepository.findBorrowRequestById("REQ001");
        assertTrue(foundRequest.isPresent());
        assertEquals("student1", foundRequest.get().getStudentUsername());
        assertEquals(RequestStatus.PENDING, foundRequest.get().getStatus());
    }

    @Test
    void testAddBorrowRequest_DuplicateId() {
        // Given
        BorrowRequest request1 = new BorrowRequest("REQ001", "student1", "B001",
                LocalDate.now(), LocalDate.now().plusDays(7));
        BorrowRequest request2 = new BorrowRequest("REQ001", "student2", "B002",
                LocalDate.now(), LocalDate.now().plusDays(5));

        // When
        boolean firstResult = borrowRepository.addBorrowRequest(request1);
        boolean secondResult = borrowRepository.addBorrowRequest(request2);

        // Then
        assertTrue(firstResult);
        assertFalse(secondResult);
    }

    @Test
    void testAddBorrowRecord_Success() {
        // Given
        BorrowRecord record = new BorrowRecord("REC001", "student1", "B001", "emp1",
                LocalDate.now(), LocalDate.now().plusDays(7));

        // When
        boolean result = borrowRepository.addBorrowRecord(record);

        // Then
        assertTrue(result);
        Optional<BorrowRecord> foundRecord = borrowRepository.findBorrowRecordById("REC001");
        assertTrue(foundRecord.isPresent());
        assertEquals("student1", foundRecord.get().getStudentUsername());
        assertFalse(foundRecord.get().isReturned());
    }

    @Test
    void testGetPendingRequestsForToday() {
        // Given
        BorrowRequest request1 = new BorrowRequest("REQ001", "student1", "B001",
                LocalDate.now(), LocalDate.now().plusDays(7));
        BorrowRequest request2 = new BorrowRequest("REQ002", "student2", "B002",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(7));
        BorrowRequest request3 = new BorrowRequest("REQ003", "student3", "B003",
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(7));

        request3.setStatus(RequestStatus.APPROVED);

        borrowRepository.addBorrowRequest(request1);
        borrowRepository.addBorrowRequest(request2);
        borrowRepository.addBorrowRequest(request3);

        // When
        List<BorrowRequest> pendingRequests = borrowRepository.getPendingRequestsForToday();

        // Then
        assertEquals(2, pendingRequests.size());
        assertTrue(pendingRequests.stream().anyMatch(r -> r.getId().equals("REQ001")));
        assertTrue(pendingRequests.stream().anyMatch(r -> r.getId().equals("REQ002")));
        assertTrue(pendingRequests.stream().allMatch(r -> r.getStatus() == RequestStatus.PENDING));
    }

    @Test
    void testGetBorrowHistoryByStudent() {
        // Given
        BorrowRecord record1 = new BorrowRecord("REC001", "student1", "B001", "emp1",
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(3));
        BorrowRecord record2 = new BorrowRecord("REC002", "student1", "B002", "emp1",
                LocalDate.now().minusDays(5), LocalDate.now().plusDays(2));
        BorrowRecord record3 = new BorrowRecord("REC003", "student2", "B003", "emp1",
                LocalDate.now(), LocalDate.now().plusDays(7));

        borrowRepository.addBorrowRecord(record1);
        borrowRepository.addBorrowRecord(record2);
        borrowRepository.addBorrowRecord(record3);

        // When
        List<BorrowRecord> studentHistory = borrowRepository.getBorrowHistoryByStudent("student1");

        // Then
        assertEquals(2, studentHistory.size());
        assertTrue(studentHistory.stream().allMatch(r -> r.getStudentUsername().equals("student1")));
    }

    @Test
    void testUpdateBorrowRequest() {
        // Given
        BorrowRequest request = new BorrowRequest("REQ001", "student1", "B001",
                LocalDate.now(), LocalDate.now().plusDays(7));
        borrowRepository.addBorrowRequest(request);

        request.setStatus(RequestStatus.APPROVED);

        // When
        boolean result = borrowRepository.updateBorrowRequest(request);

        // Then
        assertTrue(result);
        Optional<BorrowRequest> updatedRequest = borrowRepository.findBorrowRequestById("REQ001");
        assertTrue(updatedRequest.isPresent());
        assertEquals(RequestStatus.APPROVED, updatedRequest.get().getStatus());
    }

    @Test
    void testUpdateBorrowRecord() {
        // Given
        BorrowRecord record = new BorrowRecord("REC001", "student1", "B001", "emp1",
                LocalDate.now().minusDays(7), LocalDate.now());
        borrowRepository.addBorrowRecord(record);

        record.setReturned(true);
        record.setDelayed(true);
        record.setReturnDate(LocalDate.now());

        // When
        boolean result = borrowRepository.updateBorrowRecord(record);

        // Then
        assertTrue(result);
        Optional<BorrowRecord> updatedRecord = borrowRepository.findBorrowRecordById("REC001");
        assertTrue(updatedRecord.isPresent());
        assertTrue(updatedRecord.get().isReturned());
        assertTrue(updatedRecord.get().isDelayed());
        assertEquals(LocalDate.now(), updatedRecord.get().getReturnDate());
    }

    @Test
    void testGetActiveBorrows() {
        // Given
        BorrowRecord record1 = new BorrowRecord("REC001", "student1", "B001", "emp1",
                LocalDate.now().minusDays(7), LocalDate.now().plusDays(7));
        BorrowRecord record2 = new BorrowRecord("REC002", "student2", "B002", "emp1",
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(1));
        record2.setReturned(true);

        borrowRepository.addBorrowRecord(record1);
        borrowRepository.addBorrowRecord(record2);

        // When
        List<BorrowRecord> activeBorrows = borrowRepository.getActiveBorrows();

        // Then
        assertEquals(1, activeBorrows.size());
        assertEquals("REC001", activeBorrows.get(0).getId());
        assertFalse(activeBorrows.get(0).isReturned());
    }

    @Test
    void testGetTotalBorrowCount() {
        // Given
        BorrowRecord record1 = new BorrowRecord("REC001", "student1", "B001", "emp1",
                LocalDate.now(), LocalDate.now().plusDays(7));
        BorrowRecord record2 = new BorrowRecord("REC002", "student2", "B002", "emp1",
                LocalDate.now(), LocalDate.now().plusDays(5));

        borrowRepository.addBorrowRecord(record1);
        borrowRepository.addBorrowRecord(record2);

        // When
        int count = borrowRepository.getTotalBorrowCount();

        // Then
        assertEquals(2, count);
    }

    @Test
    void testGetAllBorrowRecords() {
        // Given
        BorrowRecord record1 = new BorrowRecord("REC001", "student1", "B001", "emp1",
                LocalDate.now(), LocalDate.now().plusDays(7));
        BorrowRecord record2 = new BorrowRecord("REC002", "student2", "B002", "emp1",
                LocalDate.now(), LocalDate.now().plusDays(5));

        borrowRepository.addBorrowRecord(record1);
        borrowRepository.addBorrowRecord(record2);

        // When
        List<BorrowRecord> allRecords = borrowRepository.getAllBorrowRecords();

        // Then
        assertEquals(2, allRecords.size());
    }
}