/*
 * Copyright (c) Devin B. Royal. All Rights Reserved.
 */

import org.mindrot.jbcrypt.BCrypt;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecureDatabaseManager {

    private static final Logger logger = Logger.getLogger(SecureDatabaseManager.class.getName());

    // Constants
    private static final String DB_NAME = "private_database.db";
    private static final String ENCRYPTION_KEY = "your_encryption_key"; // Secure this key appropriately
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_NAME;
    private static final int PASSWORD_LENGTH = 12;

    public static void main(String[] args) {
        try {
            // Ensure the JDBC driver is available
            ensureJDBCDriver();

            // Connect to the encrypted database
            Connection conn = connectToDatabase(JDBC_URL, ENCRYPTION_KEY);

            // Create table if not exists
            createTable(conn);

            // Retrieve a user from the database
            String username = getFirstUsername(conn);
            if (username != null) {
                logger.info("Located username: " + username);

                // Delete password
                deletePassword(conn, username);

                // Generate new password
                String newPassword = generateRandomPassword(PASSWORD_LENGTH);
                updatePassword(conn, username, hashPassword(newPassword));
                logger.info("New password generated for user '" + username + "': " + newPassword);
            } else {
                logger.warning("No user found in the database.");
            }

            // Close the database connection
            conn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred: ", e);
        }
    }

    // Ensure the SQLite JDBC driver is available
    private static void ensureJDBCDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found. Please ensure it is included in the classpath.", e);
        }
    }

    // Connect to the SQLite database
    private static Connection connectToDatabase(String url, String encryptionKey) throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        Connection conn = dataSource.getConnection();
        // Apply encryption key using custom SQL (if supported by the driver)
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA key = '" + encryptionKey + "'");
        } catch (SQLException e) {
            throw new SQLException("Error applying encryption key: " + e.getMessage(), e);
        }
        return conn;
    }

    // Create table if not exists
    private static void createTable(Connection conn) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";
        try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error creating table: " + e.getMessage(), e);
        }
    }

    // Insert data into the table
    private static void insertUser(Connection conn, String username, String password) throws SQLException {
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username format.");
        }
        if (usernameExists(conn, username)) {
            throw new IllegalArgumentException("Username already exists.");
        }
        String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(password));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error inserting user: " + e.getMessage(), e);
        }
    }

    // Delete password
    private static void deletePassword(Connection conn, String username) throws SQLException {
        String updateSQL = "UPDATE users SET password = NULL WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            logger.info("Password for user '" + username + "' has been removed.");
        } catch (SQLException e) {
            throw new SQLException("Error deleting password: " + e.getMessage(), e);
        }
    }

    // Generate random password
    private static String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:',.<>?/";
        Random random = new Random();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    // Hash password using BCrypt
    private static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Retrieve the first username from the database
    private static String getFirstUsername(Connection conn) throws SQLException {
        String selectSQL = "SELECT username FROM users LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            throw new SQLException("Error retrieving username: " + e.getMessage(), e);
        }
        return null;
    }

    // Update password
    private static void updatePassword(Connection conn, String username, String hashedPassword) throws SQLException {
        String updateSQL = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating password: " + e.getMessage(), e);
        }
    }

    // Validate username format
    private static boolean isValidUsername(String username) {
        return username != null && !username.trim().isEmpty() && username.length() >= 3;
    }

    // Check if a username already exists in the database
    private static boolean usernameExists(Connection conn, String username) throws SQLException {
        String querySQL = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new SQLException("Error checking username existence: " + e.getMessage(), e);
        }
    }

    // Delete user from the database
    private static void deleteUser(Connection conn, String username) throws SQLException {
        if (!usernameExists(conn, username)) {
            throw new IllegalArgumentException("Username does not exist.");
        }
        String deleteSQL = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            logger.info("User '" + username + "' has been deleted.");
        } catch (SQLException e) {
            throw new SQLException("Error deleting user: " + e.getMessage(), e);
        }
    }

    // Retrieve all usernames from the database
    private static void listAllUsers(Connection conn) throws SQLException {
        String selectSQL = "SELECT username FROM users";
        try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                logger.info("User found: " + rs.getString("username"));
            }
        } catch (SQLException e) {
            throw new SQLException("Error listing users: " + e.getMessage(), e);
        }
    }
}
