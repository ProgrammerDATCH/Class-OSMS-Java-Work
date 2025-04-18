package com.osms.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class User extends BaseModel {
    private int userId;
    private String username;
    private String password;
    private String role;

    public User() {}

    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User login(String username, String password) throws SQLException {
        String query = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        List<Object> results = executeQuery(query, username, password);
        return results.isEmpty() ? null : (User) results.get(0);
    }

    public boolean register() throws SQLException {
        String query = "INSERT INTO Users (Username, Password, Role) VALUES (?, ?, ?)";
        return executeUpdate(query, username, password, role) > 0;
    }

    public boolean updateUser() throws SQLException {
        String query = "UPDATE Users SET Username = ?, Password = ?, Role = ? WHERE UserId = ?";
        return executeUpdate(query, username, password, role, userId) > 0;
    }

    public boolean deleteUser() throws SQLException {
        String query = "DELETE FROM Users WHERE UserId = ?";
        return executeUpdate(query, userId) > 0;
    }

    @Override
    protected Object mapResultSet(ResultSet resultSet) throws SQLException {
        return new User(
            resultSet.getInt("UserId"),
            resultSet.getString("Username"),
            resultSet.getString("Password"),
            resultSet.getString("Role")
        );
    }
} 