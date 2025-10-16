import java.time.LocalDate;
public class Loan {
    private String id;
    private String studentId;
    private String bookId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LoanStatus status = LoanStatus.PENDING;
    private String handledByStaffId;
    private LocalDate returnedDate;

    public Loan(String id, String studentId, String bookId, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.studentId = studentId;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getBookId() { return bookId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LoanStatus getStatus() { return status; }

    public void approve(String staffId) { this.status = LoanStatus.APPROVED; this.handledByStaffId = staffId; }
    public void reject(String staffId) { this.status = LoanStatus.REJECTED; this.handledByStaffId = staffId; }
    public void returned() { this.status = LoanStatus.RETURNED; this.returnedDate = LocalDate.now(); }

    @Override
    public String toString() {
        return "Loan{" + status + ", book=" + bookId + ", student=" + studentId + "}";
    }
}
