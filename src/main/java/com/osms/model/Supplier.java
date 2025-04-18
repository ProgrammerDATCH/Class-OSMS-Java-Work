package com.osms.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Supplier extends BaseModel {
    private int supplierId;
    private String companyName;
    private String contactName;
    private String email;
    private String phone;
    private String address;
    private int userId;
    private String username;

    public Supplier() {}

    public Supplier(int supplierId, String companyName, String contactName, String email, 
                   String phone, String address, int userId, String username) {
        this.supplierId = supplierId;
        this.companyName = companyName;
        this.contactName = contactName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.userId = userId;
        this.username = username;
    }

    // Getters and setters
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
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
        String query = "INSERT INTO Suppliers (CompanyName, ContactName, Email, Phone, Address, UserId, Username) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(query, companyName, contactName, email, phone, address, userId, username) > 0;
    }

    public boolean updateSupplier() throws SQLException {
        String query = "UPDATE Suppliers SET CompanyName = ?, ContactName = ?, Email = ?, " +
                      "Phone = ?, Address = ?, Username = ? WHERE SupplierId = ?";
        return executeUpdate(query, companyName, contactName, email, phone, address, username, supplierId) > 0;
    }

    public boolean deleteSupplier() throws SQLException {
        String query = "DELETE FROM Suppliers WHERE SupplierId = ?";
        return executeUpdate(query, supplierId) > 0;
    }

    public Supplier getSupplierById(int supplierId) throws SQLException {
        String query = "SELECT * FROM Suppliers WHERE SupplierId = ?";
        List<Object> results = executeQuery(query, supplierId);
        return results.isEmpty() ? null : (Supplier) results.get(0);
    }

    public Supplier getSupplierByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM Suppliers WHERE UserId = ?";
        List<Object> results = executeQuery(query, userId);
        return results.isEmpty() ? null : (Supplier) results.get(0);
    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        String query = "SELECT * FROM Suppliers";
        List<Object> results = executeQuery(query);
        List<Supplier> suppliers = new ArrayList<>();
        for (Object result : results) {
            suppliers.add((Supplier) result);
        }
        return suppliers;
    }

    public List<Product> getSuppliedProducts() throws SQLException {
        String query = "SELECT * FROM Product WHERE SupplierId = ?";
        List<Object> results = executeQuery(query, supplierId);
        List<Product> products = new ArrayList<>();
        for (Object result : results) {
            products.add((Product) result);
        }
        return products;
    }

    @Override
    protected Object mapResultSet(ResultSet resultSet) throws SQLException {
        return new Supplier(
            resultSet.getInt("SupplierId"),
            resultSet.getString("CompanyName"),
            resultSet.getString("ContactName"),
            resultSet.getString("Email"),
            resultSet.getString("Phone"),
            resultSet.getString("Address"),
            resultSet.getInt("UserId"),
            resultSet.getString("Username")
        );
    }
} 