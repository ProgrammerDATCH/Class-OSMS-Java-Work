package com.osms.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Seller extends BaseModel {
    private int sellerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private int userId;
    private String username;

    public Seller() {}

    public Seller(int sellerId, String firstName, String lastName, String email, 
                 String phone, String address, int userId, String username) {
        this.sellerId = sellerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.userId = userId;
        this.username = username;
    }

    // Getters and setters
    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
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
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public boolean register() throws SQLException {
        String query = "INSERT INTO Sellers (FirstName, LastName, Email, Phone, Address, UserId, Username) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(query, firstName, lastName, email, phone, address, userId, username) > 0;
    }

    public boolean updateSeller() throws SQLException {
        String query = "UPDATE Sellers SET FirstName = ?, LastName = ?, Email = ?, " +
                      "Phone = ?, Address = ?, Username = ? WHERE SellerId = ?";
        return executeUpdate(query, firstName, lastName, email, phone, address, username, sellerId) > 0;
    }

    public boolean deleteSeller() throws SQLException {
        String query = "DELETE FROM Sellers WHERE SellerId = ?";
        return executeUpdate(query, sellerId) > 0;
    }

    public Seller getSellerById(int sellerId) throws SQLException {
        String query = "SELECT * FROM Sellers WHERE SellerId = ?";
        List<Object> results = executeQuery(query, sellerId);
        return results.isEmpty() ? null : (Seller) results.get(0);
    }

    public Seller getSellerByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM Sellers WHERE UserId = ?";
        List<Object> results = executeQuery(query, userId);
        return results.isEmpty() ? null : (Seller) results.get(0);
    }

    public List<Seller> getAllSellers() throws SQLException {
        String query = "SELECT * FROM Sellers";
        List<Object> results = executeQuery(query);
        List<Seller> sellers = new ArrayList<>();
        for (Object result : results) {
            sellers.add((Seller) result);
        }
        return sellers;
    }

    public List<Product> getStoreProducts() throws SQLException {
        String query = "SELECT p.* FROM Product p " +
                      "JOIN Store_Product sp ON p.ProductId = sp.ProductId " +
                      "WHERE sp.StoreId IN (SELECT StoreId FROM Stores WHERE SellerId = ?)";
        List<Object> results = executeQuery(query, sellerId);
        List<Product> products = new ArrayList<>();
        for (Object result : results) {
            products.add((Product) result);
        }
        return products;
    }

    @Override
    protected Object mapResultSet(ResultSet resultSet) throws SQLException {
        return new Seller(
            resultSet.getInt("SellerId"),
            resultSet.getString("FirstName"),
            resultSet.getString("LastName"),
            resultSet.getString("Email"),
            resultSet.getString("Phone"),
            resultSet.getString("Address"),
            resultSet.getInt("UserId"),
            resultSet.getString("Username")
        );
    }
} 