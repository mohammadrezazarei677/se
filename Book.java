public class Book {
    private String id;
    private String title;
    private String author;
    private int year;
    private int totalCopies;
    private int availableCopies;
    private String createdByStaffId;

    public Book(String id, String title, String author, int year, int totalCopies, String createdByStaffId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.createdByStaffId = createdByStaffId;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return availableCopies > 0; }
    public int getAvailableCopies() { return availableCopies; }

    public void borrowCopy() { if (availableCopies > 0) availableCopies--; }
    public void returnCopy() { if (availableCopies < totalCopies) availableCopies++; }

    @Override
    public String toString() {
        return title + " (" + author + ", " + year + ") - موجود: " + availableCopies;
    }
}