package com.osms.servlet;

import com.osms.model.BaseModel;
import com.osms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect("../login.jsp");
            return;
        }

        DashboardData dashboardData = new DashboardData();
        try {
            // Get total counts
            dashboardData.setTotalProducts(getTotalProducts());
            dashboardData.setTotalOrders(getTotalOrders());
            dashboardData.setTotalCustomers(getTotalCustomers());
            dashboardData.setTotalRevenue(getTotalRevenue());

            // Get recent orders
            dashboardData.setRecentOrders(getRecentOrders());

            // Get low stock products
            dashboardData.setLowStockProducts(getLowStockProducts());

            request.setAttribute("totalProducts", dashboardData.getTotalProducts());
            request.setAttribute("totalOrders", dashboardData.getTotalOrders());
            request.setAttribute("totalCustomers", dashboardData.getTotalCustomers());
            request.setAttribute("totalRevenue", dashboardData.getTotalRevenue());
            request.setAttribute("recentOrders", dashboardData.getRecentOrders());
            request.setAttribute("lowStockProducts", dashboardData.getLowStockProducts());

            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("../login.jsp?error=Database error");
        }
    }

    private int getTotalProducts() throws SQLException {
        String query = "SELECT COUNT(*) FROM Product";
        return executeCountQuery(query);
    }

    private int getTotalOrders() throws SQLException {
        String query = "SELECT COUNT(*) FROM Orders";
        return executeCountQuery(query);
    }

    private int getTotalCustomers() throws SQLException {
        String query = "SELECT COUNT(*) FROM Customers";
        return executeCountQuery(query);
    }

    private double getTotalRevenue() throws SQLException {
        String query = "SELECT SUM(TotalByProduct) FROM Orders";
        return executeSumQuery(query);
    }

    private List<Map<String, Object>> getRecentOrders() throws SQLException {
        String query = "SELECT o.OrderId, CONCAT(c.FirstName, ' ', c.LastName) as customerName, " +
                      "o.TotalByProduct as totalAmount, o.Date as orderDate " +
                      "FROM Orders o " +
                      "JOIN Customers c ON o.CustomerId = c.CustomerId " +
                      "ORDER BY o.Date DESC LIMIT 5";
        return executeQuery(query);
    }

    private List<Map<String, Object>> getLowStockProducts() throws SQLException {
        String query = "SELECT p.ProductName, sp.Quantity as currentStock " +
                      "FROM Product p " +
                      "JOIN Store_Product sp ON p.ProductId = sp.ProductId " +
                      "WHERE sp.Quantity < (SELECT MAX(Quantity) * 0.2 FROM Store_Product)";
        return executeQuery(query);
    }

    private int executeCountQuery(String query) throws SQLException {
        BaseModel baseModel = new BaseModel() {
            @Override
            protected Object mapResultSet(ResultSet resultSet) throws SQLException {
                return resultSet.getInt(1);
            }
        };
        List<Object> results = baseModel.executeQuery(query);
        return results.isEmpty() ? 0 : (int) results.get(0);
    }

    private double executeSumQuery(String query) throws SQLException {
        BaseModel baseModel = new BaseModel() {
            @Override
            protected Object mapResultSet(ResultSet resultSet) throws SQLException {
                return resultSet.getDouble(1);
            }
        };
        List<Object> results = baseModel.executeQuery(query);
        return results.isEmpty() ? 0.0 : (double) results.get(0);
    }

    private List<Map<String, Object>> executeQuery(String query) throws SQLException {
        BaseModel baseModel = new BaseModel() {
            @Override
            protected Object mapResultSet(ResultSet resultSet) throws SQLException {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                }
                return row;
            }
        };
        List<Object> results = baseModel.executeQuery(query);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Object result : results) {
            rows.add((Map<String, Object>) result);
        }
        return rows;
    }

    private static class DashboardData {
        private int totalProducts;
        private int totalOrders;
        private int totalCustomers;
        private double totalRevenue;
        private List<Map<String, Object>> recentOrders;
        private List<Map<String, Object>> lowStockProducts;

        // Getters and setters
        public int getTotalProducts() { return totalProducts; }
        public void setTotalProducts(int totalProducts) { this.totalProducts = totalProducts; }
        public int getTotalOrders() { return totalOrders; }
        public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
        public int getTotalCustomers() { return totalCustomers; }
        public void setTotalCustomers(int totalCustomers) { this.totalCustomers = totalCustomers; }
        public double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
        public List<Map<String, Object>> getRecentOrders() { return recentOrders; }
        public void setRecentOrders(List<Map<String, Object>> recentOrders) { this.recentOrders = recentOrders; }
        public List<Map<String, Object>> getLowStockProducts() { return lowStockProducts; }
        public void setLowStockProducts(List<Map<String, Object>> lowStockProducts) { this.lowStockProducts = lowStockProducts; }
    }
} 