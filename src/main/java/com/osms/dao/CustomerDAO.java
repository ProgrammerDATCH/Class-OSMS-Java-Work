package com.osms.dao;

import com.osms.model.Customer;
import com.osms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String SELECT_CUSTOMER_BY_ID = 
        "SELECT c.*, u.Username, u.Email " +
        "FROM Customers c " +
        "JOIN Users u ON c.UserId = u.UserId " +
        "WHERE c.CustomerId = ?";

    private static final String SELECT_CUSTOMER_BY_USERNAME = 
        "SELECT c.*, u.Username, u.Email " +
        "FROM Customers c " +
        "JOIN Users u ON c.UserId = u.UserId " +
        "WHERE u.Username = ?";

    private static final String UPDATE_CUSTOMER = 
        "UPDATE Customers SET FirstName = ?, LastName = ?, Phone = ?, Address = ? " +
        "WHERE CustomerId = ?";

    private Connection connection;

    public CustomerDAO() {
        try {
            connection = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomerById(int customerId) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMER_BY_ID)) {
            
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null;
    }

    public Customer getCustomerByUsername(String username) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMER_BY_USERNAME)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null;
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_CUSTOMER)) {
            
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getAddress());
            pstmt.setInt(5, customer.getCustomerId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    public Customer getCustomerByUserId(Integer userId) throws SQLException {
        String query = "SELECT * FROM Customers WHERE UserId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("CustomerId"));
                customer.setFirstName(rs.getString("FirstName"));
                customer.setLastName(rs.getString("LastName"));
                customer.setEmail(rs.getString("Email"));
                customer.setPhone(rs.getString("Phone"));
                customer.setAddress(rs.getString("Address"));
                customer.setUserId(rs.getInt("UserId"));
                customer.setUsername(rs.getString("Username"));
                return customer;
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM Customers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("CustomerId"));
                customer.setFirstName(rs.getString("FirstName"));
                customer.setLastName(rs.getString("LastName"));
                customer.setEmail(rs.getString("Email"));
                customer.setPhone(rs.getString("Phone"));
                customer.setAddress(rs.getString("Address"));
                customer.setUserId(rs.getInt("UserId"));
                customer.setUsername(rs.getString("Username"));
                customers.add(customer);
            }
        }
        return customers;
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("CustomerId"));
        customer.setUserId(rs.getInt("UserId"));
        customer.setFirstName(rs.getString("FirstName"));
        customer.setLastName(rs.getString("LastName"));
        customer.setPhone(rs.getString("Phone"));
        customer.setAddress(rs.getString("Address"));
        customer.setUsername(rs.getString("Username"));
        customer.setEmail(rs.getString("Email"));
        return customer;
    }
} 