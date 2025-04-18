package com.osms.dao;

import com.osms.model.Order;
import com.osms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private static final String SELECT_RECENT_ORDERS = 
        "SELECT o.*, c.FirstName, c.LastName, COUNT(oi.OrderItemId) as ItemCount " +
        "FROM Orders o " +
        "JOIN Customers c ON o.CustomerId = c.CustomerId " +
        "LEFT JOIN OrderItems oi ON o.OrderId = oi.OrderId " +
        "WHERE o.CustomerId = ? " +
        "GROUP BY o.OrderId " +
        "ORDER BY o.OrderDate DESC " +
        "LIMIT 5";

    private static final String SELECT_ORDER_BY_ID = 
        "SELECT o.*, c.FirstName, c.LastName " +
        "FROM Orders o " +
        "JOIN Customers c ON o.CustomerId = c.CustomerId " +
        "WHERE o.OrderId = ?";

    private static final String INSERT_ORDER = 
        "INSERT INTO Orders (CustomerId, OrderDate, TotalAmount, Status) " +
        "VALUES (?, NOW(), ?, 'Pending')";

    private Connection connection;

    public OrderDAO() {
        try {
            connection = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByCustomer(int customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE CustomerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("OrderId"));
                order.setProductId(rs.getInt("ProductId"));
                order.setCustomerId(rs.getInt("CustomerId"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setUnitPrice(rs.getDouble("UnitPrice"));
                order.setTotalByProduct(rs.getDouble("TotalByProduct"));
                order.setOrderDate(rs.getTimestamp("OrderDate"));
                order.setTotalAmount(rs.getDouble("TotalAmount"));
                order.setStatus(rs.getString("Status"));
                order.setCustomerName(rs.getString("CustomerName"));
                order.setItemCount(rs.getInt("ItemCount"));
                orders.add(order);
            }
        }
        return orders;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("OrderId"));
                order.setProductId(rs.getInt("ProductId"));
                order.setCustomerId(rs.getInt("CustomerId"));
                order.setQuantity(rs.getInt("Quantity"));
                order.setUnitPrice(rs.getDouble("UnitPrice"));
                order.setTotalByProduct(rs.getDouble("TotalByProduct"));
                order.setOrderDate(rs.getTimestamp("OrderDate"));
                order.setTotalAmount(rs.getDouble("TotalAmount"));
                order.setStatus(rs.getString("Status"));
                order.setCustomerName(rs.getString("CustomerName"));
                order.setItemCount(rs.getInt("ItemCount"));
                orders.add(order);
            }
        }
        return orders;
    }

    public List<Order> getRecentOrdersByCustomer(int customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_RECENT_ORDERS)) {
            
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
        }
        return orders;
    }

    public Order getOrderById(int orderId) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ORDER_BY_ID)) {
            
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrder(rs);
                }
            }
        }
        return null;
    }

    public int createOrder(int customerId, double totalAmount) throws SQLException {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, customerId);
            pstmt.setDouble(2, totalAmount);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("OrderId"));
        order.setCustomerId(rs.getInt("CustomerId"));
        order.setOrderDate(rs.getTimestamp("OrderDate"));
        order.setTotalAmount(rs.getDouble("TotalAmount"));
        order.setStatus(rs.getString("Status"));
        order.setCustomerName(rs.getString("FirstName") + " " + rs.getString("LastName"));
        order.setItemCount(rs.getInt("ItemCount"));
        return order;
    }
} 