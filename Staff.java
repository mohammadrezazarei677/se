public class Staff extends User {
    public Staff(String id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String toString() {
        return "Staff{" + username + "}";
    }
}