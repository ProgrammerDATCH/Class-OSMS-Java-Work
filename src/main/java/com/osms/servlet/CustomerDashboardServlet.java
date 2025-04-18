package com.osms.servlet;

import com.osms.dao.CustomerDAO;
import com.osms.dao.OrderDAO;
import com.osms.dao.ProductDAO;
import com.osms.model.Customer;
import com.osms.model.Order;
import com.osms.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/customer/dashboard")
public class CustomerDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        customerDAO = new CustomerDAO();
        orderDAO = new OrderDAO();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            Customer customer = customerDAO.getCustomerByUserId(userId);
            if (customer == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            List<Order> recentOrders = orderDAO.getOrdersByCustomer(customer.getCustomerId());
            List<Product> products = productDAO.getAllProducts();

            request.setAttribute("customer", customer);
            request.setAttribute("recentOrders", recentOrders);
            request.setAttribute("products", products);

            request.getRequestDispatcher("/customer/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error occurred", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("addToCart".equals(action)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                Product product = productDAO.getProductById(productId);
                if (product == null || product.getStock() < quantity) {
                    request.setAttribute("error", "Product not available in requested quantity");
                } else {
                    // Add to cart logic here
                    request.setAttribute("success", "Product added to cart successfully");
                }
            } catch (SQLException e) {
                throw new ServletException("Database error occurred", e);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid product or quantity");
            }
        }

        doGet(request, response);
    }
} 