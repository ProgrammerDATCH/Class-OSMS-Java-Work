package com.osms.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserProfile extends BaseModel {
    private int profileId;
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String profileImage;
    private String lastLoginDate;
    private boolean isActive;

    public UserProfile() {}

    public UserProfile(int profileId, int userId, String firstName, String lastName, 
                      String email, String phone, String address, String profileImage, 
                      String lastLoginDate, boolean isActive) {
        this.profileId = profileId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.profileImage = profileImage;
        this.lastLoginDate = lastLoginDate;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    public String getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(String lastLoginDate) { this.lastLoginDate = lastLoginDate; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    // Database operations
    public boolean createProfile() throws SQLException {
        String query = "INSERT INTO UserProfiles (UserId, FirstName, LastName, Email, Phone, Address, " +
                      "ProfileImage, LastLoginDate, IsActive) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?)";
        return executeUpdate(query, userId, firstName, lastName, email, phone, address, 
                           profileImage, isActive) > 0;
    }

    public boolean updateProfile() throws SQLException {
        String query = "UPDATE UserProfiles SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, " +
                      "Address = ?, ProfileImage = ?, IsActive = ? WHERE UserId = ?";
        return executeUpdate(query, firstName, lastName, email, phone, address, profileImage, 
                           isActive, userId) > 0;
    }

    public UserProfile getProfileByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM UserProfiles WHERE UserId = ?";
        List<Object> results = executeQuery(query, userId);
        return results.isEmpty() ? null : (UserProfile) results.get(0);
    }

    public boolean updateLastLogin() throws SQLException {
        String query = "UPDATE UserProfiles SET LastLoginDate = NOW() WHERE UserId = ?";
        return executeUpdate(query, userId) > 0;
    }

    @Override
    protected Object mapResultSet(ResultSet rs) throws SQLException {
        return new UserProfile(
            rs.getInt("ProfileId"),
            rs.getInt("UserId"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Email"),
            rs.getString("Phone"),
            rs.getString("Address"),
            rs.getString("ProfileImage"),
            rs.getString("LastLoginDate"),
            rs.getBoolean("IsActive")
        );
    }
} 