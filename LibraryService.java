
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {
    public Map<String, Student> students = new HashMap<>();
    public Map<String, Staff> staffs = new HashMap<>();
    public Map<String, Manager> managers = new HashMap<>();
    public Map<String, Book> books = new HashMap<>();
    public Map<String, Loan> loans = new HashMap<>();

    // ثبت‌نام دانشجو
    public Student registerStudent(String username, String password, String studentNumber) {
        String id = UUID.randomUUID().toString();
        Student s = new Student(id, username, password, studentNumber);
        students.put(id, s);
        return s;
    }

    // ورود
    public User login(String username, String password) {
        for (User u : allUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    // اضافه‌کردن کتاب توسط کارمند
    public Book addBook(String title, String author, int year, int totalCopies, Staff staff) {
        String id = UUID.randomUUID().toString();
        Book b = new Book(id, title, author, year, totalCopies, staff.getId());
        books.put(id, b);
        return b;
    }

    // جستجوی کتاب (دانشجو)
    public List<Book> searchBooks(String title, String author, Integer year) {
        return books.values().stream().filter(b ->
                (title == null || b.getTitle().toLowerCase().contains(title.toLowerCase())) &&
                        (author == null || b.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                        (year == null || b.getYear() == year)
        ).collect(Collectors.toList());
    }

    // درخواست امانت
    public Loan requestLoan(Student s, String bookId, LocalDate start, LocalDate end) {
        Book b = books.get(bookId);
        if (b == null || !b.isAvailable()) throw new RuntimeException("کتاب موجود نیست");
        if (!s.isActive()) throw new RuntimeException("دانشجو غیرفعال است");
        String id = UUID.randomUUID().toString();
        Loan l = new Loan(id, s.getId(), bookId, start, end);
        loans.put(id, l);
        return l;
    }

    // تایید امانت توسط کارمند
    public void approveLoan(String loanId, Staff staff) {
        Loan l = loans.get(loanId);
        if (l == null) return;
        LocalDate today = LocalDate.now();
        if (!(l.getStartDate().isEqual(today) || l.getStartDate().isEqual(today.minusDays(1))))
            throw new RuntimeException("فقط امانت‌های امروز یا دیروز قابل تایید هستند");
        Book b = books.get(l.getBookId());
        if (!b.isAvailable()) { l.reject(staff.getId()); return; }
        b.borrowCopy();
        l.approve(staff.getId());
    }

    // دریافت کتاب برگشتی
    public void returnBook(String loanId, Staff staff) {
        Loan l = loans.get(loanId);
        if (l == null) return;
        Book b = books.get(l.getBookId());
        b.returnCopy();
        l.returned();
    }

    // آمار ساده (برای مهمان)
    public void showGuestStats() {
        long totalStudents = students.size();
        long totalBooks = books.size();
        long totalLoans = loans.size();
        long currentlyBorrowed = loans.values().stream().filter(x -> x.getStatus() == LoanStatus.APPROVED).count();

        System.out.println("تعداد دانشجویان ثبت شده: " + totalStudents);
        System.out.println("تعداد کل کتاب‌ها: " + totalBooks);
        System.out.println("تعداد کل امانت‌ها: " + totalLoans);
        System.out.println("کتاب‌های در امانت فعلی: " + currentlyBorrowed);
    }

    // جمع همه کاربران
    private List<User> allUsers() {
        List<User> all = new ArrayList<>();
        all.addAll(students.values());
        all.addAll(staffs.values());
        all.addAll(managers.values());
        return all;
    }
}
