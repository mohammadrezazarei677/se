public class Student extends User {
    private String studentNumber;

    public Student(String id, String username, String password, String studentNumber) {
        super(id, username, password);
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() { return studentNumber; }

    @Override
    public String toString() {
        return "Student{" + username + ", id=" + id + ", number=" + studentNumber + "}";
    }
}