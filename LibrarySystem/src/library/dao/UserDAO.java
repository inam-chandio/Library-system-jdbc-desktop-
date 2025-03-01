
// 4. UserDAO.java
package library.dao;

import library.db.DatabaseConnection;
import library.model.User;
import java.sql.*;

public class UserDAO {

    // Add a new user
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
        }
    }

    // Find user by ID
    public User findById(String userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        }
        return null;
    }
}
