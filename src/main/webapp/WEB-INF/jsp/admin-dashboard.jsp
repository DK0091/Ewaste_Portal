<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - E-Waste Portal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/global.css">
    <style>
        body { background-color: #f8fafc; }
        .admin-nav {
            background: rgba(15, 23, 42, 0.95) !important;
            backdrop-filter: blur(10px);
            border-bottom: 2px solid #10b981;
        }
        .stat-card {
            border-radius: 1rem;
            border: none;
            overflow: hidden;
            position: relative;
        }
        .stat-card::before {
            content: '';
            position: absolute;
            top: 0; left: 0; right: 0;
            height: 4px;
            background: rgba(255,255,255,0.3);
        }
        .stat-icon-bg {
            position: absolute;
            right: -20px;
            bottom: -20px;
            font-size: 8rem;
            opacity: 0.1;
            transform: rotate(-15deg);
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark admin-nav sticky-top py-3">
    <div class="container">
        <a class="navbar-brand fw-bold brand-font fs-4 text-white" href="#">
            <i class="fa-solid fa-shield-halved text-success me-2"></i> System Administration
        </a>
        <div class="d-flex">
            <a class="btn btn-danger btn-sm rounded-pill px-4 fw-bold hover-float shadow" href="/logout">
                <i class="fa-solid fa-power-off me-1"></i> Terminate Session
            </a>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <h3 class="brand-font fw-bold mb-4 text-dark"><i class="fa-solid fa-chart-line text-primary me-2"></i> Key Metrics</h3>
    
    <div class="row row-cols-1 row-cols-md-2 row-cols-xl-4 g-4 mb-5">
        <div class="col">
            <div class="card stat-card shadow-lg hover-float" style="background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%); color: white;">
                <div class="card-body p-4 position-relative">
                    <i class="fa-solid fa-leaf stat-icon-bg"></i>
                    <h6 class="text-uppercase mb-2 opacity-75 fw-bold tracking-wider fs-7">SDG Impact Metric</h6>
                    <h2 class="fw-bold mb-0 display-5">${totalOverallWeight} <span class="fs-4">kg</span></h2>
                    <div class="mt-2 small opacity-75"><i class="fa-solid fa-arrow-trend-up"></i> Total E-waste Recycled</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card stat-card shadow-lg hover-float" style="background: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%); color: white;">
                <div class="card-body p-4 position-relative">
                    <i class="fa-solid fa-clock-rotate-left stat-icon-bg"></i>
                    <h6 class="text-uppercase mb-2 opacity-75 fw-bold tracking-wider fs-7">Pending Attention</h6>
                    <h2 class="fw-bold mb-0 display-5">${countRequested}</h2>
                    <div class="mt-2 small opacity-75">Awaiting Recycler Assignment</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card stat-card shadow-lg hover-float" style="background: linear-gradient(135deg, #f7b733 0%, #fc4a1a 100%); color: white;">
                <div class="card-body p-4 position-relative">
                    <i class="fa-solid fa-gears stat-icon-bg"></i>
                    <h6 class="text-uppercase mb-2 opacity-75 fw-bold tracking-wider fs-7">Active Processing</h6>
                    <h2 class="fw-bold mb-0 display-5">${countProcessing}</h2>
                    <div class="mt-2 small opacity-75">Currently in logistical pipeline</div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card stat-card shadow-lg hover-float" style="background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); color: white;">
                <div class="card-body p-4 position-relative">
                    <i class="fa-solid fa-certificate stat-icon-bg"></i>
                    <h6 class="text-uppercase mb-2 opacity-75 fw-bold tracking-wider fs-7">Fully Certified</h6>
                    <h2 class="fw-bold mb-0 display-5">${countCertified}</h2>
                    <div class="mt-2 small opacity-75">Successfully recycled & documented</div>
                </div>
            </div>
        </div>
    </div>

    <div class="card shadow-sm border-0 rounded-4 overflow-hidden mb-5">
        <div class="card-header bg-white border-bottom pt-4 px-4 pb-3 d-flex justify-content-between align-items-center">
            <h5 class="fw-bold brand-font m-0"><i class="fa-solid fa-network-wired text-primary me-2"></i> Global Logistics Log</h5>
            <span class="badge bg-light text-dark border"><i class="fa-solid fa-list text-muted me-1"></i> ${pickups.size()} Records</span>
        </div>
        <div class="card-body p-4 bg-light">
            <c:if test="${empty pickups}">
                 <div class="text-center py-5">
                    <i class="fa-solid fa-folder-open fa-3x text-muted opacity-25"></i>
                    <p class="mt-3 text-muted">No pickup requests logged in the system.</p>
                </div>
            </c:if>
            <c:if test="${not empty pickups}">
                <div class="table-responsive">
                    <table class="table table-modern w-100 m-0">
                        <thead class="text-secondary small text-uppercase tracking-wider">
                            <tr>
                                <th class="ps-4">Req #</th>
                                <th>Citizen</th>
                                <th>Assigned Recycler</th>
                                <th>Date Initiated</th>
                                <th class="text-end pe-4">Current Stage</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${pickups}">
                                <tr>
                                    <td class="ps-4 fw-bold text-dark">#${p.id}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="bg-primary bg-opacity-10 text-primary rounded-circle d-flex align-items-center justify-content-center me-2" style="width: 32px; height: 32px;">
                                                <i class="fa-solid fa-user fs-6"></i>
                                            </div>
                                            <span class="fw-medium">${p.citizen.name}</span>
                                        </div>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty p.recycler}">
                                                <div class="d-flex align-items-center">
                                                    <i class="fa-solid fa-truck text-success me-2"></i>
                                                    <span class="text-dark">${p.recycler.name}</span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning text-dark px-3 py-2 rounded-pill"><i class="fa-solid fa-circle-exclamation me-1"></i> Unassigned</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-muted"><i class="fa-regular fa-calendar me-1"></i> ${p.pickupDate}</td>
                                    <td class="text-end pe-4">
                                        <c:set var="statusColor" value="secondary"/>
                                        <c:if test="${p.status == 'REQUESTED'}"><c:set var="statusColor" value="danger"/></c:if>
                                        <c:if test="${p.status == 'ASSIGNED'}"><c:set var="statusColor" value="info"/></c:if>
                                        <c:if test="${p.status == 'COLLECTED' || p.status == 'SORTING' || p.status == 'SHREDDING'}"><c:set var="statusColor" value="primary"/></c:if>
                                        <c:if test="${p.status == 'CERTIFIED'}"><c:set var="statusColor" value="success"/></c:if>
                                        
                                        <span class="badge bg-${statusColor} bg-opacity-10 text-${statusColor} px-3 py-2 border border-${statusColor} rounded-pill shadow-sm">
                                            <span class="status-dot"></span> ${p.status}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>
    </div>
</div>

</body>
</html>
