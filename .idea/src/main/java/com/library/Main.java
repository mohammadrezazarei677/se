package com.library;

import com.library.repository.*;
import com.library.service.*;
import com.library.model.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static BookService bookService;
    private static BorrowService borrowService;
    private static ReportService reportService;
    private static User currentUser;

    public static void main(String[] args) {
        initializeServices();
        showWelcomeMessage();

        while (true) {
            showUserTypeMenu();
        }
    }

    private static void initializeServices() {
        UserRepository userRepository = new UserRepository();
        BookRepository bookRepository = new BookRepository();
        BorrowRepository borrowRepository = new BorrowRepository();

        userService = new UserService(userRepository);
        bookService = new BookService(bookRepository, userRepository);
        borrowService = new BorrowService(borrowRepository, bookRepository, userRepository);
        reportService = new ReportService(userRepository, bookRepository, borrowRepository);

        // ایجاد داده‌های نمونه
        initializeSampleData();
    }

    private static void initializeSampleData() {
        // ثبت کارمندهای نمونه
        userService.registerEmployee("emp1", "emp123");
        userService.registerEmployee("emp2", "emp456");

        // ثبت کتاب‌های نمونه
        bookService.registerBook("B001", "Java Programming", "John Doe", 2023, "emp1");
        bookService.registerBook("B002", "Database Systems", "Alice Smith", 2022, "emp1");
        bookService.registerBook("B003", "Data Structures", "Bob Wilson", 2023, "emp2");

        // ثبت دانشجوی نمونه
        userService.registerStudent("student1", "stu123");
    }

    private static void showWelcomeMessage() {
        System.out.println("🎓 سیستم مدیریت کتابخانه دانشگاه");
        System.out.println("=================================");
    }

    private static void showUserTypeMenu() {
        System.out.println("\n🔘 انتخاب نوع کاربر:");
        System.out.println("1. مهمان");
        System.out.println("2. دانشجو");
        System.out.println("3. کارمند");
        System.out.println("4. مدیر");
        System.out.println("0. خروج از برنامه");

        int choice = getIntInput("لطفاً انتخاب کنید: ");

        switch (choice) {
            case 1 -> showGuestMenu();
            case 2 -> loginAsStudent();
            case 3 -> loginAsEmployee();
            case 4 -> loginAsManager();
            case 0 -> {
                System.out.println("👋 خروج از برنامه...");
                System.exit(0);
            }
            default -> System.out.println("❌ گزینه نامعتبر!");
        }
    }

    private static void showGuestMenu() {
        System.out.println("\n👤 منوی مهمان:");
        System.out.println("1. جستجوی کتاب");
        System.out.println("2. مشاهده آمار کتابخانه");
        System.out.println("0. بازگشت به منوی اصلی");

        int choice = getIntInput("انتخاب: ");

        switch (choice) {
            case 1 -> searchBooksAsGuest();
            case 2 -> showGuestStatistics();
            case 0 -> { return; }
            default -> System.out.println("❌ گزینه نامعتبر!");
        }
    }

    private static void loginAsStudent() {
        System.out.println("\n🎓 ورود به عنوان دانشجو:");
        String username = getStringInput("نام کاربری: ");
        String password = getStringInput("رمز عبور: ");

        var user = userService.login(username, password);
        if (user.isPresent() && user.get() instanceof Student) {
            currentUser = user.get();
            showStudentMenu();
        } else {
            System.out.println("❌ نام کاربری یا رمز عبور اشتباه یا حساب دانشجو نیست");
        }
    }

    private static void loginAsEmployee() {
        System.out.println("\n👨‍💼 ورود به عنوان کارمند:");
        String username = getStringInput("نام کاربری: ");
        String password = getStringInput("رمز عبور: ");

        var user = userService.login(username, password);
        if (user.isPresent() && user.get() instanceof Employee) {
            currentUser = user.get();
            showEmployeeMenu();
        } else {
            System.out.println("❌ نام کاربری یا رمز عبور اشتباه یا حساب کارمند نیست");
        }
    }

    private static void loginAsManager() {
        System.out.println("\n👑 ورود به عنوان مدیر:");
        String username = getStringInput("نام کاربری: ");
        String password = getStringInput("رمز عبور: ");

        var user = userService.login(username, password);
        if (user.isPresent() && user.get() instanceof Manager) {
            currentUser = user.get();
            showManagerMenu();
        } else {
            System.out.println("❌ نام کاربری یا رمز عبور اشتباه یا حساب مدیر نیست");
        }
    }

    private static void showStudentMenu() {
        while (currentUser != null && currentUser instanceof Student) {
            System.out.println("\n🎓 منوی دانشجو (" + currentUser.getUsername() + "):");
            System.out.println("1. جستجوی کتاب");
            System.out.println("2. درخواست امانت کتاب");
            System.out.println("3. مشاهده تاریخچه امانت");
            System.out.println("4. تغییر رمز عبور");
            System.out.println("5. خروج از سیستم");

            int choice = getIntInput("انتخاب: ");

            switch (choice) {
                case 1 -> searchBooks();
                case 2 -> requestBorrow();
                case 3 -> showBorrowHistory();
                case 4 -> changePassword();
                case 5 -> {
                    logout();
                    return;
                }
                default -> System.out.println("❌ گزینه نامعتبر!");
            }
        }
    }

    private static void showEmployeeMenu() {
        while (currentUser != null && currentUser instanceof Employee) {
            System.out.println("\n👨‍💼 منوی کارمند (" + currentUser.getUsername() + "):");
            System.out.println("1. ثبت کتاب جدید");
            System.out.println("2. جستجو و ویرایش کتاب");
            System.out.println("3. تایید درخواست امانت");
            System.out.println("4. بازگرداندن کتاب");
            System.out.println("5. مشاهده تاریخچه دانشجو");
            System.out.println("6. فعال/غیرفعال کردن دانشجو");
            System.out.println("7. تغییر رمز عبور");
            System.out.println("8. خروج از سیستم");

            int choice = getIntInput("انتخاب: ");

            switch (choice) {
                case 1 -> registerBook();
                case 2 -> editBook();
                case 3 -> approveBorrowRequest();
                case 4 -> returnBook();
                case 5 -> showStudentBorrowHistory();
                case 6 -> toggleStudentStatus();
                case 7 -> changePassword();
                case 8 -> {
                    logout();
                    return;
                }
                default -> System.out.println("❌ گزینه نامعتبر!");
            }
        }
    }

    private static void showManagerMenu() {
        while (currentUser != null && currentUser instanceof Manager) {
            System.out.println("\n👑 منوی مدیر (" + currentUser.getUsername() + "):");
            System.out.println("1. ثبت کارمند جدید");
            System.out.println("2. مشاهده عملکرد کارمند");
            System.out.println("3. مشاهده آمار امانات");
            System.out.println("4. مشاهده آمار دانشجویان");
            System.out.println("5. فعال/غیرفعال کردن دانشجو");
            System.out.println("6. تغییر رمز عبور");
            System.out.println("7. خروج از سیستم");

            int choice = getIntInput("انتخاب: ");

            switch (choice) {
                case 1 -> registerEmployee();
                case 2 -> showEmployeePerformance();
                case 3 -> showBorrowStatistics();
                case 4 -> showStudentStatistics();
                case 5 -> toggleStudentStatus();
                case 6 -> changePassword();
                case 7 -> {
                    logout();
                    return;
                }
                default -> System.out.println("❌ گزینه نامعتبر!");
            }
        }
    }

    // بقیه متدها دقیقاً مثل قبلی می‌مانند...
    private static void registerStudent() {
        System.out.println("\n🎓 ثبت نام دانشجو:");
        String username = getStringInput("نام کاربری: ");
        String password = getStringInput("رمز عبور: ");

        if (userService.registerStudent(username, password)) {
            System.out.println("✅ ثبت نام موفقیت‌آمیز");
        } else {
            System.out.println("❌ نام کاربری تکراری است");
        }
    }

    private static void searchBooksAsGuest() {
        System.out.println("\n🔍 جستجوی کتاب (مهمان):");
        String title = getStringInput("عنوان کتاب: ");

        var results = bookService.searchBooksByTitle(title);
        System.out.println("📚 نتایج جستجو (" + results.size() + " کتاب):");

        for (Book book : results) {
            System.out.println("- " + book.getTitle() + " | " + book.getAuthor() +
                    " | " + book.getPublicationYear());
        }
    }

    private static void showGuestStatistics() {
        var stats = reportService.getGuestStatistics();
        System.out.println("\n📊 آمار کتابخانه:");
        System.out.println("👥 تعداد دانشجویان: " + stats.totalStudents);
        System.out.println("📚 تعداد کتاب‌ها: " + stats.totalBooks);
        System.out.println("🔄 تعداد کل امانت‌ها: " + stats.totalBorrows);
        System.out.println("📖 کتاب‌های در امانت: " + stats.activeBorrows);
    }

    private static void searchBooks() {
        System.out.println("\n🔍 جستجوی کتاب:");
        String title = getStringInput("عنوان (Enter برای نادیده گرفتن): ");
        if (title.isEmpty()) title = null;

        String author = getStringInput("نویسنده (Enter برای نادیده گرفتن): ");
        if (author.isEmpty()) author = null;

        String yearStr = getStringInput("سال انتشار (Enter برای نادیده گرفتن): ");
        Integer year = null;
        if (!yearStr.isEmpty()) {
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                System.out.println("❌ سال نامعتبر");
                return;
            }
        }

        var results = bookService.searchBooks(title, author, year);
        System.out.println("📚 نتایج (" + results.size() + " کتاب):");

        for (Book book : results) {
            String status = book.isAvailable() ? "✅ موجود" : "❌ امانت داده شده";
            System.out.println("- " + book.getTitle() + " | " + book.getAuthor() +
                    " | " + book.getPublicationYear() + " | " + status);
        }
    }

    private static void requestBorrow() {
        System.out.println("\n📨 درخواست امانت کتاب:");
        String requestId = getStringInput("کد درخواست: ");
        String bookId = getStringInput("کد کتاب: ");
        int days = getIntInput("تعداد روز امانت: ");

        if (borrowService.requestBorrow(requestId, currentUser.getUsername(), bookId,
                LocalDate.now(), LocalDate.now().plusDays(days))) {
            System.out.println("✅ درخواست امانت ثبت شد");
        } else {
            System.out.println("❌ خطا در ثبت درخواست (کتاب ناموجود یا حساب غیرفعال)");
        }
    }

    private static void showBorrowHistory() {
        var history = borrowService.getBorrowHistoryByStudent(currentUser.getUsername());
        System.out.println("\n📊 تاریخچه امانت شما (" + history.size() + " رکورد):");

        for (var record : history) {
            String status = record.isReturned() ? "✅ بازگردانده شده" : "📖 در امانت";
            String delay = record.isDelayed() ? " | ⚠️ تاخیر داشته" : "";
            System.out.println("- " + record.getBookId() + " | " + record.getBorrowDate() +
                    " تا " + record.getDueDate() + " | " + status + delay);
        }
    }

    private static void changePassword() {
        System.out.println("\n🔐 تغییر رمز عبور:");
        String oldPassword = getStringInput("رمز عبور فعلی: ");
        String newPassword = getStringInput("رمز عبور جدید: ");

        if (userService.changePassword(currentUser.getUsername(), oldPassword, newPassword)) {
            System.out.println("✅ رمز عبور تغییر کرد");
        } else {
            System.out.println("❌ رمز عبور فعلی اشتباه است");
        }
    }

    private static void logout() {
        currentUser = null;
        System.out.println("✅ خروج از سیستم موفقیت‌آمیز");
    }

    private static void registerBook() {
        System.out.println("\n📖 ثبت کتاب جدید:");
        String id = getStringInput("کد کتاب: ");
        String title = getStringInput("عنوان کتاب: ");
        String author = getStringInput("نویسنده: ");
        int year = getIntInput("سال انتشار: ");

        if (bookService.registerBook(id, title, author, year, currentUser.getUsername())) {
            System.out.println("✅ کتاب ثبت شد");
        } else {
            System.out.println("❌ خطا در ثبت کتاب (کد تکراری)");
        }
    }

    private static void editBook() {
        System.out.println("\n✏️ ویرایش کتاب:");
        String id = getStringInput("کد کتاب: ");

        var bookOpt = bookService.getBookById(id);
        if (bookOpt.isEmpty()) {
            System.out.println("❌ کتاب یافت نشد");
            return;
        }

        Book book = bookOpt.get();
        System.out.println("کتاب فعلی: " + book.getTitle() + " - " + book.getAuthor());

        String newTitle = getStringInput("عنوان جدید (Enter برای تغییر ندادن): ");
        String newAuthor = getStringInput("نویسنده جدید (Enter برای تغییر ندادن): ");
        String newYearStr = getStringInput("سال جدید (Enter برای تغییر ندادن): ");

        if (!newTitle.isEmpty()) book.setTitle(newTitle);
        if (!newAuthor.isEmpty()) book.setAuthor(newAuthor);
        if (!newYearStr.isEmpty()) book.setPublicationYear(Integer.parseInt(newYearStr));

        if (bookService.updateBook(book)) {
            System.out.println("✅ کتاب ویرایش شد");
        } else {
            System.out.println("❌ خطا در ویرایش کتاب");
        }
    }

    private static void approveBorrowRequest() {
        System.out.println("\n✅ تایید درخواست امانت:");
        var pendingRequests = borrowService.getPendingRequestsForToday();

        if (pendingRequests.isEmpty()) {
            System.out.println("⚠️ هیچ درخواست در انتظاری وجود ندارد");
            return;
        }

        System.out.println("📋 درخواست‌های در انتظار:");
        for (int i = 0; i < pendingRequests.size(); i++) {
            var request = pendingRequests.get(i);
            System.out.println((i + 1) + ". " + request.getId() + " | دانشجو: " +
                    request.getStudentUsername() + " | کتاب: " + request.getBookId());
        }

        String requestId = getStringInput("کد درخواست برای تایید: ");

        if (borrowService.approveBorrowRequest(requestId, currentUser.getUsername())) {
            System.out.println("✅ درخواست تایید شد");
        } else {
            System.out.println("❌ خطا در تایید درخواست");
        }
    }

    private static void returnBook() {
        System.out.println("\n📚 بازگرداندن کتاب:");
        String recordId = getStringInput("کد رکورد امانت: ");

        if (borrowService.returnBook(recordId, currentUser.getUsername())) {
            System.out.println("✅ کتاب بازگردانده شد");
        } else {
            System.out.println("❌ خطا در بازگرداندن کتاب");
        }
    }

    private static void showStudentBorrowHistory() {
        System.out.println("\n📊 مشاهده تاریخچه دانشجو:");
        String username = getStringInput("نام کاربری دانشجو: ");

        var history = reportService.getStudentBorrowHistory(username);
        if (history != null) {
            System.out.println("📈 آمار " + username + ":");
            System.out.println("📚 تعداد کل امانات: " + history.totalBorrows);
            System.out.println("❌ کتاب‌های تحویل داده نشده: " + history.notReturnedBooks);
            System.out.println("⚠️ امانت‌های با تاخیر: " + history.delayedReturns);

            System.out.println("\n📖 تاریخچه امانت:");
            for (var record : history.history) {
                String status = record.isReturned() ? "✅ بازگردانده شده" : "📖 در امانت";
                String delay = record.isDelayed() ? " | ⚠️ تاخیر داشته" : "";
                System.out.println("- " + record.getBookId() + " | " + record.getBorrowDate() +
                        " تا " + record.getDueDate() + " | " + status + delay);
            }
        } else {
            System.out.println("❌ دانشجو یافت نشد");
        }
    }

    private static void toggleStudentStatus() {
        System.out.println("\n⚙️ فعال/غیرفعال کردن دانشجو:");
        String username = getStringInput("نام کاربری دانشجو: ");
        boolean active = getYesNoInput("آیا دانشجو فعال باشد؟ (y/n): ");

        if (userService.activateStudent(username, active)) {
            String status = active ? "فعال" : "غیرفعال";
            System.out.println("✅ دانشجو " + username + " " + status + " شد");
        } else {
            System.out.println("❌ دانشجو یافت نشد");
        }
    }

    private static void registerEmployee() {
        System.out.println("\n👨‍💼 ثبت کارمند جدید:");
        String username = getStringInput("نام کاربری: ");
        String password = getStringInput("رمز عبور: ");

        if (userService.registerEmployee(username, password)) {
            System.out.println("✅ کارمند با موفقیت ثبت شد");
        } else {
            System.out.println("❌ نام کاربری تکراری است");
        }
    }

    private static void showEmployeePerformance() {
        System.out.println("\n📊 مشاهده عملکرد کارمند:");
        String username = getStringInput("نام کاربری کارمند: ");

        var performance = reportService.getEmployeePerformance(username);
        if (performance != null) {
            System.out.println("📈 عملکرد " + username + ":");
            System.out.println("📝 کتاب‌های ثبت شده: " + performance.booksRegistered);
            System.out.println("📤 کتاب‌های امانت داده: " + performance.booksLent);
            System.out.println("📥 کتاب‌های تحویل گرفته: " + performance.booksReceived);
        } else {
            System.out.println("❌ کارمند یافت نشد");
        }
    }

    private static void showBorrowStatistics() {
        System.out.println("\n📈 آمار امانات کتاب:");
        var stats = reportService.getBorrowStatistics();

        System.out.println("📨 تعداد درخواست‌های امانت: " + stats.totalRequests);
        System.out.println("✅ تعداد امانت‌های داده شده: " + stats.totalBorrows);
        System.out.println("📅 میانگین روزهای امانت: " + String.format("%.2f", stats.averageBorrowDays));
    }

    private static void showStudentStatistics() {
        System.out.println("\n🎓 آمار دانشجویان:");
        var stats = reportService.getStudentStatistics();

        System.out.println("👥 تعداد کل دانشجویان: " + stats.totalStudents);
        System.out.println("📚 تعداد کل امانت‌ها: " + stats.totalBorrows);
        System.out.println("❌ کتاب‌های تحویل داده نشده: " + stats.totalNotReturned);
        System.out.println("⚠️ امانت‌های با تاخیر: " + stats.totalDelayed);

        if (!stats.topDelayedStudents.isEmpty()) {
            System.out.println("\n🚨 10 دانشجوی با بیشترین تاخیر:");
            for (int i = 0; i < stats.topDelayedStudents.size(); i++) {
                Student student = stats.topDelayedStudents.get(i);
                System.out.println((i + 1) + ". " + student.getUsername() +
                        " - " + student.getDelayedReturns() + " تاخیر");
            }
        }
    }

    // متدهای کمکی برای ورودی
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            int value = scanner.nextInt();
            scanner.nextLine(); // مصرف newline
            return value;
        } catch (Exception e) {
            scanner.nextLine(); // مصرف خطای ورودی
            return -1;
        }
    }

    private static boolean getYesNoInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().toLowerCase();
        return input.equals("y") || input.equals("بله") || input.equals("y");
    }
}