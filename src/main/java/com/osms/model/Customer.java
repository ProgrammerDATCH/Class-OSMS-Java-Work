package com.osms.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Customer extends BaseModel {
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private int userId;
    private String username;

    public Customer() {}

    public Customer(int customerId, String firstName, String lastName, String email, 
                   String phone, String address, int userId, String username) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.userId = userId;
        this.username = username;
    }

    // Getters and setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
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
        String query = "INSERT INTO Customers (FirstName, LastName, Email, Phone, Address, UserId, Username) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(query, firstName, lastName, email, phone, address, userId, username) > 0;
    }

    public boolean updateCustomer() throws SQLException {
        String query = "UPDATE Customers SET FirstName = ?, LastName = ?, Email = ?, " +
                      "Phone = ?, Address = ?, Username = ? WHERE CustomerId = ?";
        return executeUpdate(query, firstName, lastName, email, phone, address, username, customerId) > 0;
    }

    public boolean deleteCustomer() throws SQLException {
        String query = "DELETE FROM Customers WHERE CustomerId = ?";
        return executeUpdate(query, customerId) > 0;
    }

    public Customer getCustomerById(int customerId) throws SQLException {
        String query = "SELECT * FROM Customers WHERE CustomerId = ?";
        List<Object> results = executeQuery(query, customerId);
        return results.isEmpty() ? null : (Customer) results.get(0);
    }

    public Customer getCustomerByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM Customers WHERE UserId = ?";
        List<Object> results = executeQuery(query, userId);
        return results.isEmpty() ? null : (Customer) results.get(0);
    }

    public List<Customer> getAllCustomers() throws SQLException {
        String query = "SELECT * FROM Customers";
        List<Object> results = executeQuery(query);
        List<Customer> customers = new ArrayList<>();
        for (Object result : results) {
            customers.add((Customer) result);
        }
        return customers;
    }

    @Override
    protected Object mapResultSet(ResultSet resultSet) throws SQLException {
        return new Customer(
            resultSet.getInt("CustomerId"),
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