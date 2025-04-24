package com.osms.servlet;

import com.osms.dao.CustomerDAO;
import com.osms.dao.UserDAO;
import com.osms.model.User;
import com.osms.model.Customer;
import com.osms.model.Seller;
import com.osms.model.Supplier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private UserDAO userDAO;
    private CustomerDAO customerDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userType = request.getParameter("userType");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        try {
            // First create the user
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setUserType(userType);
            
            if (!user.register()) {
                request.setAttribute("error", "Username already exists");
                request.getRequestDispatcher("signup.jsp").forward(request, response);
                return;
            }

            // Get the newly created user's ID
            User newUser = user.login(username, password);
            if (newUser == null) {
                request.setAttribute("error", "Registration failed");
                request.getRequestDispatcher("signup.jsp").forward(request, response);
                return;
            }

            // Create the specific user type
            switch (userType) {
                case "Customer":
                    Customer customer = new Customer();
                    customer.setFirstName(firstName);
                    customer.setLastName(lastName);
                    customer.setEmail(email);
                    customer.setPhone(phone);
                    customer.setAddress(address);
                    customer.setUserId(newUser.getUserId());
                    customer.setUsername(username);
                    if (!customer.register()) {
                        request.setAttribute("error", "Failed to create customer profile");
                        request.getRequestDispatcher("signup.jsp").forward(request, response);
                        return;
                    }
                    break;

                case "Seller":
                    Seller seller = new Seller();
                    seller.setFirstName(firstName);
                    seller.setLastName(lastName);
                    seller.setEmail(email);
                    seller.setPhone(phone);
                    seller.setUserId(newUser.getUserId());
                    if (!seller.register()) {
                        request.setAttribute("error", "Failed to create seller profile");
                        request.getRequestDispatcher("signup.jsp").forward(request, response);
                        return;
                    }
                    break;

                case "Supplier":
                    Supplier supplier = new Supplier();
                    supplier.setCompanyName(firstName + " " + lastName);
                    supplier.setContactName(firstName + " " + lastName);
                    supplier.setEmail(email);
                    supplier.setPhone(phone);
                    supplier.setAddress(address);
                    supplier.setUserId(newUser.getUserId());
                    supplier.setUsername(username);
                    if (!supplier.register()) {
                        request.setAttribute("error", "Failed to create supplier profile");
                        request.getRequestDispatcher("signup.jsp").forward(request, response);
                        return;
                    }
                    break;
            }

            // Redirect to login page on success
            response.sendRedirect("login.jsp?registered=true");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error occurred");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }
} 