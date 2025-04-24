package com.osms.servlet.admin;

import com.osms.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/admin/sellers/*")
public class AdminSellerManagementServlet extends HttpServlet {
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !user.getUserType().equals("Admin")) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // List all sellers
                List<Seller> sellers = new Seller().getAllSellers();
                request.setAttribute("sellers", sellers);
                request.getRequestDispatcher("/admin/sellers.jsp").forward(request, response);
            } else if (pathInfo.matches("/\\d+")) {
                // Get specific seller
                int sellerId = Integer.parseInt(pathInfo.substring(1));
                Seller seller = new Seller().getSellerById(sellerId);
                if (seller != null) {
                    request.setAttribute("seller", seller);
                    request.getRequestDispatcher("/admin/seller-details.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load seller data");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !user.getUserType().equals("Admin")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("approve".equals(action)) {
                int sellerId = Integer.parseInt(request.getParameter("sellerId"));
                approveSeller(sellerId);
                response.getWriter().write("{\"status\":\"success\",\"message\":\"Seller approved successfully\"}");
            } else if ("suspend".equals(action)) {
                int sellerId = Integer.parseInt(request.getParameter("sellerId"));
                suspendSeller(sellerId);
                response.getWriter().write("{\"status\":\"success\",\"message\":\"Seller suspended successfully\"}");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    private void approveSeller(int sellerId) throws SQLException {
        String query = "UPDATE Seller SET Status = 'Active' WHERE SellerId = ?";
        new BaseModel() {
            @Override
            protected Object mapResultSet(java.sql.ResultSet rs) throws SQLException {
                return null; // No result mapping needed for UPDATE
            }
        }.executeUpdate(query, sellerId);
    }

    private void suspendSeller(int sellerId) throws SQLException {
        String query = "UPDATE Seller SET Status = 'Suspended' WHERE SellerId = ?";
        new BaseModel() {
            @Override
            protected Object mapResultSet(java.sql.ResultSet rs) throws SQLException {
                return null; // No result mapping needed for UPDATE
            }
        }.executeUpdate(query, sellerId);
    }
} 