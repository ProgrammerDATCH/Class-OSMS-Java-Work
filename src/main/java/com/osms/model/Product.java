package com.osms.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Product extends BaseModel {
    private int productId;
    private String productName;
    private int supplierId;
    private String expirationDate;
    private String category;
    private int quantity;
    private double unitPrice;
    private String description;
    private int stock;
    private String imageUrl;

    public Product() {}

    public Product(int productId, String productName, int supplierId, String expirationDate, 
                  String category, int quantity, double unitPrice, String description, 
                  int stock, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.supplierId = supplierId;
        this.expirationDate = expirationDate;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.description = description;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean addProduct() throws SQLException {
        String query = "INSERT INTO Product (ProductName, SupplierId, ExpirationDate, Category, Quantity, UnitPrice, Description, Stock, ImageUrl) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(query, productName, supplierId, expirationDate, category, quantity, unitPrice, description, stock, imageUrl) > 0;
    }

    public boolean updateProduct() throws SQLException {
        String query = "UPDATE Product SET ProductName = ?, SupplierId = ?, ExpirationDate = ?, Category = ?, Quantity = ?, UnitPrice = ?, Description = ?, Stock = ?, ImageUrl = ? WHERE ProductId = ?";
        return executeUpdate(query, productName, supplierId, expirationDate, category, quantity, unitPrice, description, stock, imageUrl, productId) > 0;
    }

    public boolean deleteProduct() throws SQLException {
        String query = "DELETE FROM Product WHERE ProductId = ?";
        return executeUpdate(query, productId) > 0;
    }

    public Product getProductById(int productId) throws SQLException {
        String query = "SELECT * FROM Product WHERE ProductId = ?";
        List<Object> results = executeQuery(query, productId);
        return results.isEmpty() ? null : (Product) results.get(0);
    }

    public List<Product> getAllProducts() throws SQLException {
        String query = "SELECT * FROM Product";
        List<Object> results = executeQuery(query);
        List<Product> products = new ArrayList<>();
        for (Object result : results) {
            products.add((Product) result);
        }
        return products;
    }

    public boolean addToStore(int storeId, int quantity) throws SQLException {
        String query = "INSERT INTO Store_Product (StoreId, ProductId, Quantity) VALUES (?, ?, ?)";
        return executeUpdate(query, storeId, productId, quantity) > 0;
    }

    public boolean updateStoreQuantity(int storeId, int quantity) throws SQLException {
        String query = "UPDATE Store_Product SET Quantity = ? WHERE StoreId = ? AND ProductId = ?";
        return executeUpdate(query, quantity, storeId, productId) > 0;
    }

    @Override
    protected Object mapResultSet(ResultSet resultSet) throws SQLException {
        return new Product(
            resultSet.getInt("ProductId"),
            resultSet.getString("ProductName"),
            resultSet.getInt("SupplierId"),
            resultSet.getString("ExpirationDate"),
            resultSet.getString("Category"),
            resultSet.getInt("Quantity"),
            resultSet.getDouble("UnitPrice"),
            resultSet.getString("Description"),
            resultSet.getInt("Stock"),
            resultSet.getString("ImageUrl")
        );
    }
} 