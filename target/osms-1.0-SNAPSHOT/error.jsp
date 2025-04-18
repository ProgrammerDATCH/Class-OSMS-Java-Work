<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - OSMS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .error-container {
            max-width: 600px;
            margin: 100px auto;
            padding: 20px;
            text-align: center;
        }
        .error-icon {
            font-size: 5rem;
            color: #dc3545;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    <i class="bi bi-exclamation-triangle"></i>
                </c:when>
                <c:otherwise>
                    <i class="bi bi-exclamation-circle"></i>
                </c:otherwise>
            </c:choose>
        </div>
        <h1 class="mb-4">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    Page Not Found
                </c:when>
                <c:otherwise>
                    Internal Server Error
                </c:otherwise>
            </c:choose>
        </h1>
        <p class="lead mb-4">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    The page you are looking for might have been removed, had its name changed, or is temporarily unavailable.
                </c:when>
                <c:otherwise>
                    An unexpected error occurred. Please try again later.
                </c:otherwise>
            </c:choose>
        </p>
        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary">Return to Login</a>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 