package com.osms.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class Order extends BaseModel {
    private int orderId;
    private int productId;
    private int customerId;
    private int quantity;
    private double unitPrice;
    private double totalByProduct;
    private Timestamp orderDate;
    private double totalAmount;
    private String status;
    private String customerName;
    private int itemCount;

    public Order() {}

    public Order(int orderId, int productId, int customerId, int quantity, double unitPrice, 
                double totalByProduct, Timestamp orderDate, double totalAmount, String status, 
                String customerName, int itemCount) {
        this.orderId = orderId;
        this.productId = productId;
        this.customerId = customerId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalByProduct = totalByProduct;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.customerName = customerName;
        this.itemCount = itemCount;
    }

    // Getters and setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public double getTotalByProduct() { return totalByProduct; }
    public void setTotalByProduct(double totalByProduct) { this.totalByProduct = totalByProduct; }
    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }

    public boolean placeOrder() throws SQLException {
        String query = "INSERT INTO Orders (ProductId, CustomerId, Quantity, UnitPrice, TotalByProduct, OrderDate, TotalAmount, Status, CustomerName, ItemCount) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(query, productId, customerId, quantity, unitPrice, totalByProduct, orderDate, totalAmount, status, customerName, itemCount) > 0;
    }

    public boolean updateOrder() throws SQLException {
        String query = "UPDATE Orders SET ProductId = ?, CustomerId = ?, Quantity = ?, " +
                      "UnitPrice = ?, TotalByProduct = ?, OrderDate = ?, TotalAmount = ?, Status = ?, CustomerName = ?, ItemCount = ? WHERE OrderId = ?";
        return executeUpdate(query, productId, customerId, quantity, unitPrice, totalByProduct, orderDate, totalAmount, status, customerName, itemCount, orderId) > 0;
    }

    public boolean deleteOrder() throws SQLException {
        String query = "DELETE FROM Orders WHERE OrderId = ?";
        return executeUpdate(query, orderId) > 0;
    }

    public Order getOrderById(int orderId) throws SQLException {
        String query = "SELECT * FROM Orders WHERE OrderId = ?";
        List<Object> results = executeQuery(query, orderId);
        return results.isEmpty() ? null : (Order) results.get(0);
    }

    public List<Order> getOrdersByCustomer(int customerId) throws SQLException {
        String query = "SELECT * FROM Orders WHERE CustomerId = ?";
        List<Object> results = executeQuery(query, customerId);
        List<Order> orders = new ArrayList<>();
        for (Object result : results) {
            orders.add((Order) result);
        }
        return orders;
    }

    public List<Order> getAllOrders() throws SQLException {
        String query = "SELECT * FROM Orders";
        List<Object> results = executeQuery(query);
        List<Order> orders = new ArrayList<>();
        for (Object result : results) {
            orders.add((Order) result);
        }
        return orders;
    }

    @Override
    protected Object mapResultSet(ResultSet resultSet) throws SQLException {
        return new Order(
            resultSet.getInt("OrderId"),
            resultSet.getInt("ProductId"),
            resultSet.getInt("CustomerId"),
            resultSet.getInt("Quantity"),
            resultSet.getDouble("UnitPrice"),
            resultSet.getDouble("TotalByProduct"),
            resultSet.getTimestamp("OrderDate"),
            resultSet.getDouble("TotalAmount"),
            resultSet.getString("Status"),
            resultSet.getString("CustomerName"),
            resultSet.getInt("ItemCount")
        );
    }
} 