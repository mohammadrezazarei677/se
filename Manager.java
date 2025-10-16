public class Manager extends User {
    public Manager(String id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String toString() {
        return "Manager{" + username + "}";
    }
}
