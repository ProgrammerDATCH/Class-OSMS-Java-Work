<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Details - OSMS Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block bg-dark sidebar collapse">
                <div class="position-sticky pt-3">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/dashboard">
                                <i class="bi bi-speedometer2"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active text-white" href="${pageContext.request.contextPath}/admin/sellers">
                                <i class="bi bi-shop"></i> Manage Sellers
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/suppliers">
                                <i class="bi bi-truck"></i> Manage Suppliers
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/admin/reports">
                                <i class="bi bi-file-earmark-text"></i> System Reports
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/logout">
                                <i class="bi bi-box-arrow-right"></i> Logout
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1>Seller Details</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/admin/sellers" class="btn btn-secondary">
                            <i class="bi bi-arrow-left"></i> Back to Sellers
                        </a>
                    </div>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>

                <c:if test="${not empty success}">
                    <div class="alert alert-success" role="alert">
                        ${success}
                    </div>
                </c:if>

                <div class="row">
                    <div class="col-md-6">
                        <div class="card mb-4">
                            <div class="card-header">
                                <h5 class="card-title mb-0">Seller Information</h5>
                            </div>
                            <div class="card-body">
                                <div class="mb-3">
                                    <label class="fw-bold">ID:</label>
                                    <p>${seller.sellerId}</p>
                                </div>
                                <div class="mb-3">
                                    <label class="fw-bold">Name:</label>
                                    <p>${seller.firstName} ${seller.lastName}</p>
                                </div>
                                <div class="mb-3">
                                    <label class="fw-bold">Email:</label>
                                    <p>${seller.email}</p>
                                </div>
                                <div class="mb-3">
                                    <label class="fw-bold">Phone:</label>
                                    <p>${seller.phone}</p>
                                </div>
                                <div class="mb-3">
                                    <label class="fw-bold">Address:</label>
                                    <p>${seller.address}</p>
                                </div>
                                <div class="mb-3">
                                    <label class="fw-bold">Status:</label>
                                    <p>
                                        <span class="badge bg-${seller.status eq 'Active' ? 'success' : 'warning'}">
                                            ${seller.status}
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <div class="card mb-4">
                            <div class="card-header">
                                <h5 class="card-title mb-0">Store Information</h5>
                            </div>
                            <div class="card-body">
                                <div class="mb-3">
                                    <label class="fw-bold">Store Name:</label>
                                    <p>${store.name}</p>
                                </div>
                                <div class="mb-3">
                                    <label class="fw-bold">Store Status:</label>
                                    <p>
                                        <span class="badge bg-${store.status eq 'Open' ? 'success' : 'danger'}">
                                            ${store.status}
                                        </span>
                                    </p>
                                </div>
                                <div class="mb-3">
                                    <label class="fw-bold">Total Products:</label>
                                    <p>${totalProducts}</p>
                                </div>
                                <div class="mb-3">
                                    <label class="fw-bold">Total Orders:</label>
                                    <p>${totalOrders}</p>
                                </div>
                            </div>
                        </div>

                        <div class="card">
                            <div class="card-header">
                                <h5 class="card-title mb-0">Actions</h5>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${seller.status eq 'Active'}">
                                        <button onclick="updateSellerStatus(${seller.sellerId}, 'suspend')" 
                                                class="btn btn-warning">
                                            <i class="bi bi-pause-circle"></i> Suspend Seller
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button onclick="updateSellerStatus(${seller.sellerId}, 'approve')" 
                                                class="btn btn-success">
                                            <i class="bi bi-check-circle"></i> Approve Seller
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function updateSellerStatus(sellerId, action) {
            if (!confirm('Are you sure you want to ' + action + ' this seller?')) {
                return;
            }

            fetch('${pageContext.request.contextPath}/admin/sellers/', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'sellerId=' + sellerId + '&action=' + action
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    location.reload();
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred while updating the seller status');
            });
        }
    </script>
</body>
</html> 