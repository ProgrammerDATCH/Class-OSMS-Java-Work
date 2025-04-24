package com.osms.servlet;

import com.osms.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = new User();
            User loggedInUser = user.login(username, password);

            if (loggedInUser != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", loggedInUser);

                switch (loggedInUser.getUserType().toLowerCase()) {
                    case "admin":
                        response.sendRedirect("admin/dashboard");
                        break;
                    case "seller":
                        response.sendRedirect("seller/dashboard");
                        break;
                    case "customer":
                        response.sendRedirect("customer/dashboard");
                        break;
                    default:
                        response.sendRedirect("login.jsp?error=Invalid user role");
                }
            } else {
                response.sendRedirect("login.jsp?error=Invalid credentials");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Database error");
        }
    }
} 