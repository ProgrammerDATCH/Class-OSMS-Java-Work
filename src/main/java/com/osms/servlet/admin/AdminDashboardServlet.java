package com.osms.servlet.admin;

import com.osms.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !user.getUserType().equals("Admin")) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // Get dashboard statistics
            Map<String, Object> stats = getDashboardStats();
            request.setAttribute("stats", stats);
            
            // Forward to dashboard page
            request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load dashboard data");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        
        // Get total number of sellers
        String sellerQuery = "SELECT COUNT(*) as total FROM Seller";
        stats.put("totalSellers", executeCountQuery(sellerQuery));
        
        // Get total number of suppliers
        String supplierQuery = "SELECT COUNT(*) as total FROM Suppliers";
        stats.put("totalSuppliers", executeCountQuery(supplierQuery));
        
        // Get total number of customers
        String customerQuery = "SELECT COUNT(*) as total FROM Customers";
        stats.put("totalCustomers", executeCountQuery(customerQuery));
        
        // Get total number of products
        String productQuery = "SELECT COUNT(*) as total FROM Product";
        stats.put("totalProducts", executeCountQuery(productQuery));
        
        // Get total number of orders
        String orderQuery = "SELECT COUNT(*) as total FROM Orders";
        stats.put("totalOrders", executeCountQuery(orderQuery));
        
        // Get total revenue
        String revenueQuery = "SELECT SUM(TotalByProduct) as total FROM Orders";
        stats.put("totalRevenue", executeCountQuery(revenueQuery));
        
        return stats;
    }

    private int executeCountQuery(String query) throws SQLException {
        try {
            return new BaseModel() {
                @Override
                protected Object mapResultSet(java.sql.ResultSet rs) throws SQLException {
                    return rs.getInt("total");
                }
            }.executeQuery(query).stream()
                .findFirst()
                .map(obj -> (Integer) obj)
                .orElse(0);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
} 