public class Contact {
    String name;
    String phone;
    String email;
    Contact left, right;
    int height;

    public Contact(String name, String phone, String email) {
        this.name = name.toLowerCase();
        this.phone = phone;
        this.email = email;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}
