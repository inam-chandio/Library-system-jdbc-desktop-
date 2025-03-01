package library.dao;

import library.model.*;
import library.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowingDAO {
    private final LibraryItemDAO itemDAO = new LibraryItemDAO();

    public List<LibraryItem> getBorrowedItems(String userId) throws SQLException {
        String sql = "SELECT i.* FROM library_items i " +
                "JOIN borrowed_items b ON i.id = b.item_id " +
                "WHERE b.user_id = ? AND b.return_date IS NULL";

        List<LibraryItem> items = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
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

    public void borrowItem(String userId, String itemId) throws SQLException {
        String sql = "INSERT INTO borrowed_items (user_id, item_id, borrow_date) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, itemId);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();

            // Update item availability
            updateItemAvailability(itemId, false);
        }
    }

    public void returnItem(String userId, String itemId) throws SQLException {
        String sql = "UPDATE borrowed_items SET return_date = ? " +
                "WHERE user_id = ? AND item_id = ? AND return_date IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setString(2, userId);
            stmt.setString(3, itemId);
            stmt.executeUpdate();

            // Update item availability
            updateItemAvailability(itemId, true);
        }
    }

    private void updateItemAvailability(String itemId, boolean available) throws SQLException {
        String sql = "UPDATE library_items SET available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, available);
            stmt.setString(2, itemId);
            stmt.executeUpdate();
        }
    }
}