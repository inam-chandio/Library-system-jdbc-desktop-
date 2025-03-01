
// 4.4 User.java
package library.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private List<LibraryItem> borrowedItems;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowedItems = new ArrayList<>();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<LibraryItem> getBorrowedItems() { return borrowedItems; }

    @Override
    public String toString() {
        return String.format("User ID: %s, Name: %s, Email: %s", id, name, email);
    }
}
