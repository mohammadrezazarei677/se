
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public class Main {
    public static void main(String[] args) {
        LibraryService lib = new LibraryService();

        // مدیر، کارمند و دانشجو
        Manager m = new Manager(UUID.randomUUID().toString(), "admin", "1234");
        lib.managers.put(m.getId(), m);

        Staff staff = new Staff(UUID.randomUUID().toString(), "staff1", "1111");
        lib.staffs.put(staff.getId(), staff);

        Student s = lib.registerStudent("student1", "2222", "981234");

        // ثبت کتاب توسط کارمند
        lib.addBook("الگوریتم‌ها", "کورمن", 2015, 2, staff);
        lib.addBook("هوش مصنوعی", "راسل", 2021, 1, staff);

        // جستجو توسط دانشجو
        List<Book> results = lib.searchBooks("هوش", null, null);
        System.out.println("🔎 نتایج جستجو:");
        for (Book b : results) System.out.println(" - " + b);

        // درخواست امانت
        Book selected = results.get(0);
        Loan loan = lib.requestLoan(s, selected.getId(), LocalDate.now(), LocalDate.now().plusDays(10));
        System.out.println("\n📚 درخواست امانت ثبت شد: " + loan);

        // تایید توسط کارمند
        lib.approveLoan(loan.getId(), staff);
        System.out.println("✅ وضعیت بعد از تایید: " + loan.getStatus());

        // بازگرداندن کتاب
        lib.returnBook(loan.getId(), staff);
        System.out.println("🔄 وضعیت بعد از بازگرداندن: " + loan.getStatus());

        // نمایش آمار برای کاربر مهمان
        System.out.println("\n📊 اطلاعات آماری:");
        lib.showGuestStats();
    }
}//.
