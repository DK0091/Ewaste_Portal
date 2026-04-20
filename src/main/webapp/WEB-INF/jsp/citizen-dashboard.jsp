<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Citizen Dashboard - E-Waste Portal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/global.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body {
            background-color: #f8fafc;
        }
        .navbar-custom {
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(0,0,0,0.05);
        }
        .progress-glow {
            box-shadow: 0 0 10px rgba(16, 185, 129, 0.5);
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light navbar-custom sticky-top">
    <div class="container py-2">
        <a class="navbar-brand fw-bold brand-font fs-4" href="#">
            <i class="fa-solid fa-leaf text-success me-2 hover-float"></i> E-Waste Citizen
        </a>
        <div class="d-flex align-items-center">
            <a class="btn btn-outline-danger btn-sm rounded-pill px-4 fw-bold shadow-sm" href="/logout">
                <i class="fa-solid fa-right-from-bracket me-1"></i> Logout
            </a>
        </div>
    </div>
</nav>

<div class="container mt-5 mb-5">
    <!-- Success Msg Capture -->
    <c:if test="${not empty msg}">
        <div id="flash-msg" data-msg="${msg}" class="d-none"></div>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-5 border-bottom pb-3">
        <h2 class="brand-font fw-bold m-0"><i class="fa-solid fa-recycle text-gradient me-2"></i> My E-Waste Pickups</h2>
        <a href="/citizen/schedule" class="btn bg-gradient-success text-white shadow-lg rounded-pill px-4 py-2 hover-float fw-bold">
            <i class="fa-solid fa-plus me-1"></i> Schedule Pickup
        </a>
    </div>

    <!-- Empty State -->
    <c:if test="${empty pickups}">
        <div class="text-center py-5">
            <i class="fa-solid fa-box-open fa-4x text-muted mb-3 opacity-50"></i>
            <h4 class="fw-bold text-secondary">No Pickups Scheduled</h4>
            <p class="text-muted">You haven't requested any e-waste collections yet. Help the environment by scheduling one!</p>
            <a href="/citizen/schedule" class="btn btn-outline-success mt-2 rounded-pill px-4">Start Recycling</a>
        </div>
    </c:if>

    <c:if test="${not empty pickups}">
        <div class="table-responsive">
            <table class="table table-modern w-100">
                <thead class="text-secondary small text-uppercase tracking-wider">
                    <tr>
                        <th class="ps-4">Request ID</th>
                        <th>Items & Weights</th>
                        <th>Pickup Address</th>
                        <th style="width: 35%;">Live Progress</th>
                        <th class="text-end pe-4">Documentation</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pickup" items="${pickups}">
                        <tr>
                            <td class="ps-4 fw-bold text-dark fs-5">#${pickup.id}</td>
                            <td>
                                <c:forEach var="item" items="${pickup.items}">
                                    <div class="mb-1">
                                        <i class="fa-solid fa-plug text-primary opacity-75"></i> 
                                        <span class="fw-medium">${item.itemType}</span> 
                                        <span class="badge bg-light text-dark border ms-1">${item.weight} kg</span>
                                    </div>
                                </c:forEach>
                            </td>
                            <td class="text-muted">
                                <div class="d-flex align-items-baseline">
                                    <i class="fa-solid fa-location-dot text-danger me-2"></i>
                                    <span>${pickup.address}</span>
                                </div>
                            </td>
                            <td>
                                <c:set var="prog" value="10" />
                                <c:set var="color" value="bg-secondary" />
                                <c:set var="status" value="${pickup.status}" />
                                
                                <c:if test="${status == 'ASSIGNED'}"><c:set var="prog" value="25"/><c:set var="color" value="bg-info"/></c:if>
                                <c:if test="${status == 'COLLECTED'}"><c:set var="prog" value="50"/><c:set var="color" value="bg-primary"/></c:if>
                                <c:if test="${status == 'SORTING'}"><c:set var="prog" value="70"/><c:set var="color" value="bg-warning text-dark"/></c:if>
                                <c:if test="${status == 'SHREDDING'}"><c:set var="prog" value="90"/><c:set var="color" value="bg-danger"/></c:if>
                                <c:if test="${status == 'CERTIFIED'}"><c:set var="prog" value="100"/><c:set var="color" value="bg-success progress-glow"/></c:if>
                                
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="badge ${color} rounded-pill px-3 shadow-sm">${status}</span>
                                    <span class="fw-bold fs-6 text-dark">${prog}%</span>
                                </div>
                                <div class="progress shadow-sm bg-light" style="height: 10px; border-radius: 20px;">
                                    <div class="progress-bar ${color} ${status != 'CERTIFIED' ? 'progress-bar-striped progress-bar-animated' : ''}" style="width: ${prog}%; border-radius: 20px;"></div>
                                </div>
                            </td>
                            <td class="text-end pe-4">
                                <c:choose>
                                    <c:when test="${status == 'CERTIFIED'}">
                                        <a href="/citizen/certificate/download?pickupId=${pickup.id}" class="btn btn-success rounded-pill px-3 py-2 shadow-sm hover-float">
                                            <i class="fa-solid fa-file-pdf me-1"></i> Get Certificate
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-muted d-inline-flex align-items-center bg-light px-3 py-2 rounded-pill border">
                                            <i class="fa-solid fa-circle-notch fa-spin me-2 text-primary"></i> <span class="small fw-bold">Processing</span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const msgEl = document.getElementById('flash-msg');
        if(msgEl) {
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: msgEl.getAttribute('data-msg'),
                timer: 3000,
                showConfirmButton: false,
                toast: true,
                position: 'top-end'
            });
        }
    });
</script>
</body>
</html>
