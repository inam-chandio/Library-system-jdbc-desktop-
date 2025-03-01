
// 2. Replace your existing LibraryItemDAO.java with this complete version
package library.dao;

import library.model.*;
import library.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryItemDAO {

    // Add a new item
    public void addItem(LibraryItem item) throws SQLException {
        String sql = "INSERT INTO library_items (id, title, year, item_type, author, genre, issue_number) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getId());
            stmt.setString(2, item.getTitle());
            stmt.setInt(3, item.getYear());
            stmt.setString(4, item instanceof Book ? "BOOK" : "MAGAZINE");

            if (item instanceof Book) {
                Book book = (Book) item;
                stmt.setString(5, book.getAuthor());
                stmt.setString(6, book.getGenre());
                stmt.setNull(7, Types.INTEGER);
            } else {
                Magazine magazine = (Magazine) item;
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setInt(7, magazine.getIssueNumber());
            }

            stmt.executeUpdate();
        }
    }

    // Find item by ID
    public Optional<LibraryItem> findById(String id) throws SQLException {
        String sql = "SELECT * FROM library_items WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LibraryItem item;
                String type = rs.getString("item_type");

                if ("BOOK".equals(type)) {
                    item = new Book(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getInt("year"),
                            rs.getString("author"),
                            rs.getString("genre")
                    );
                } else {
                    item = new Magazine(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getInt("year"),
                            rs.getInt("issue_number")
                    );
                }
                item.setAvailable(rs.getBoolean("available"));
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    // Get all items
    public List<LibraryItem> getAllItems() throws SQLException {
        List<LibraryItem> items = new ArrayList<>();
        String sql = "SELECT * FROM library_items";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LibraryItem item;
                String type = rs.getString("item_type");

                if ("BOOK".equals(type)) {
                    item = new Book(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getInt("year"),
                            rs.getString("author"),
                            rs.getString("genre")
                    );
                } else {
                    item = new Magazine(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getInt("year"),
                            rs.getInt("issue_number")
                    );
                }
                item.setAvailable(rs.getBoolean("available"));
                items.add(item);
            }
        }
        return items;
    }

    // Update item
    public void updateItem(LibraryItem item) throws SQLException {
        String sql = "UPDATE library_items SET title = ?, year = ?, item_type = ?, " +
                "available = ?, author = ?, genre = ?, issue_number = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getTitle());
            stmt.setInt(2, item.getYear());
            stmt.setString(3, item instanceof Book ? "BOOK" : "MAGAZINE");
            stmt.setBoolean(4, item.isAvailable());

            if (item instanceof Book) {
                Book book = (Book) item;
                stmt.setString(5, book.getAuthor());
                stmt.setString(6, book.getGenre());
                stmt.setNull(7, Types.INTEGER);
            } else {
                Magazine magazine = (Magazine) item;
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.VARCHAR);
                stmt.setInt(7, magazine.getIssueNumber());
            }

            stmt.setString(8, item.getId());
            stmt.executeUpdate();
        }
    }

    // Update availability
    public void updateAvailability(String itemId, boolean available) throws SQLException {
        String sql = "UPDATE library_items SET available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, available);
            stmt.setString(2, itemId);
            stmt.executeUpdate();
        }
    }

    // Search items
    public List<LibraryItem> searchItems(String query) throws SQLException {
        String sql = "SELECT * FROM library_items WHERE title LIKE ? OR author LIKE ?";
        List<LibraryItem> items = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LibraryItem item;
                String type = rs.getString("item_type");

                if ("BOOK".equals(type)) {
                    item = new Book(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getInt("year"),
                            rs.getString("author"),
                            rs.getString("genre")
                    );
                } else {
                    item = new Magazine(
                            rs.getString("id"),
                            rs.getString("title"),
                            rs.getInt("year"),
                            rs.getInt("issue_number")
                    );
                }
                item.setAvailable(rs.getBoolean("available"));
                items.add(item);
            }
        }
        return items;
    }
}