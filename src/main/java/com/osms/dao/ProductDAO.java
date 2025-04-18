package com.osms.dao;

import com.osms.model.Product;
import com.osms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        try {
            connection = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addProduct(Product product) throws SQLException {
        String query = "INSERT INTO Products (ProductName, Description, Price, Stock, Category, SupplierId, ImageUrl) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getUnitPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getCategory());
            stmt.setInt(6, product.getSupplierId());
            stmt.setString(7, product.getImageUrl());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateProduct(Product product) throws SQLException {
        String query = "UPDATE Products SET ProductName = ?, Description = ?, Price = ?, Stock = ?, Category = ?, SupplierId = ?, ImageUrl = ? WHERE ProductId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getDescription());
            stmt.setDouble(3, product.getUnitPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getCategory());
            stmt.setInt(6, product.getSupplierId());
            stmt.setString(7, product.getImageUrl());
            stmt.setInt(8, product.getProductId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteProduct(int productId) throws SQLException {
        String query = "DELETE FROM Products WHERE ProductId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Product getProductById(int productId) throws SQLException {
        String query = "SELECT * FROM Products WHERE ProductId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("ProductId"));
                product.setProductName(rs.getString("ProductName"));
                product.setDescription(rs.getString("Description"));
                product.setUnitPrice(rs.getDouble("Price"));
                product.setStock(rs.getInt("Stock"));
                product.setCategory(rs.getString("Category"));
                product.setSupplierId(rs.getInt("SupplierId"));
                product.setImageUrl(rs.getString("ImageUrl"));
                return product;
            }
        }
        return null;
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("ProductId"));
                product.setProductName(rs.getString("ProductName"));
                product.setDescription(rs.getString("Description"));
                product.setUnitPrice(rs.getDouble("Price"));
                product.setStock(rs.getInt("Stock"));
                product.setCategory(rs.getString("Category"));
                product.setSupplierId(rs.getInt("SupplierId"));
                product.setImageUrl(rs.getString("ImageUrl"));
                products.add(product);
            }
        }
        return products;
    }

    public List<Product> getProductsByCategory(String category) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE Category = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("ProductId"));
                product.setProductName(rs.getString("ProductName"));
                product.setDescription(rs.getString("Description"));
                product.setUnitPrice(rs.getDouble("Price"));
                product.setStock(rs.getInt("Stock"));
                product.setCategory(rs.getString("Category"));
                product.setSupplierId(rs.getInt("SupplierId"));
                product.setImageUrl(rs.getString("ImageUrl"));
                products.add(product);
            }
        }
        return products;
    }

    public List<Product> getProductsBySupplier(int supplierId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE SupplierId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("ProductId"));
                product.setProductName(rs.getString("ProductName"));
                product.setDescription(rs.getString("Description"));
                product.setUnitPrice(rs.getDouble("Price"));
                product.setStock(rs.getInt("Stock"));
                product.setCategory(rs.getString("Category"));
                product.setSupplierId(rs.getInt("SupplierId"));
                product.setImageUrl(rs.getString("ImageUrl"));
                products.add(product);
            }
        }
        return products;
    }
} 