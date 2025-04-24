<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to OSMS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .hero {
            background: linear-gradient(135deg, #6B73FF 0%, #000DFF 100%);
            color: white;
            padding: 100px 0;
            margin-bottom: 50px;
        }
        .feature-card {
            border: none;
            border-radius: 15px;
            transition: transform 0.3s;
            margin-bottom: 20px;
        }
        .feature-card:hover {
            transform: translateY(-5px);
        }
        .feature-icon {
            font-size: 2.5rem;
            margin-bottom: 20px;
            color: #000DFF;
        }
    </style>
</head>
<body>
    <%
        // Check if user is logged in
        Object user = session.getAttribute("user");
        if (user != null) {
            String role = ((com.osms.model.User)user).getRole().toLowerCase();
            String redirectUrl = "/" + role + "/dashboard";
            response.sendRedirect(request.getContextPath() + redirectUrl);
            return;
        }
    %>
    
    <div class="hero">
        <div class="container text-center">
            <h1 class="display-4 mb-4">Online Shop Management System</h1>
            <p class="lead mb-4">Streamline your business operations with our comprehensive shop management solution</p>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-light btn-lg me-3">Login</a>
            <a href="${pageContext.request.contextPath}/signup.jsp" class="btn btn-outline-light btn-lg">Sign Up</a>
        </div>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <div class="card feature-card shadow-sm">
                    <div class="card-body text-center">
                        <div class="feature-icon">🛍️</div>
                        <h3>For Sellers</h3>
                        <p>Manage your inventory, track sales, and grow your business with powerful seller tools.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card feature-card shadow-sm">
                    <div class="card-body text-center">
                        <div class="feature-icon">👥</div>
                        <h3>For Customers</h3>
                        <p>Browse products, track orders, and enjoy a seamless shopping experience.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card feature-card shadow-sm">
                    <div class="card-body text-center">
                        <div class="feature-icon">📊</div>
                        <h3>For Suppliers</h3>
                        <p>Manage your product catalog, fulfill orders, and connect with sellers efficiently.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 