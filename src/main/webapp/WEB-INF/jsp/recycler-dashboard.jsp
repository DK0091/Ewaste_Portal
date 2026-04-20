<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recycler Dashboard - E-Waste Portal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/global.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body { background-color: #f8fafc; }
        .recycler-nav {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-bottom: 2px solid #0d6efd;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light recycler-nav sticky-top py-3 shadow-sm">
    <div class="container">
        <a class="navbar-brand fw-bold brand-font fs-4 text-dark" href="#">
            <i class="fa-solid fa-recycle text-primary me-2 hover-float"></i> Recycler Operations
        </a>
        <div class="d-flex">
            <a class="btn btn-outline-danger btn-sm rounded-pill px-4 fw-bold shadow-sm" href="/logout">
                <i class="fa-solid fa-right-from-bracket me-1"></i> Logout
            </a>
        </div>
    </div>
</nav>

<div class="container mt-5">
    <!-- Success Msg Capture -->
    <c:if test="${not empty msg}">
        <div id="flash-msg" data-msg="${msg}" class="d-none"></div>
    </c:if>

    <div class="row g-5">
        <!-- Available Request Pool -->
        <div class="col-xl-12">
            <div class="d-flex align-items-center mb-3">
                <h4 class="brand-font fw-bold m-0"><i class="fa-solid fa-inbox text-primary me-2"></i> Available Request Pool</h4>
            </div>
            
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                <div class="card-body p-0 bg-light">
                    <c:if test="${empty requestedPickups}">
                        <div class="text-center py-5">
                            <i class="fa-solid fa-check-double fa-3x text-muted opacity-25 mb-3"></i>
                            <h6 class="fw-bold text-secondary">No Pending Requests</h6>
                            <p class="text-muted small">All citizens requests have been assigned. Check back later.</p>
                        </div>
                    </c:if>
                    <c:if test="${not empty requestedPickups}">
                        <div class="table-responsive">
                            <table class="table table-modern w-100 m-0">
                                <thead class="text-secondary small text-uppercase tracking-wider">
                                    <tr>
                                        <th class="ps-4">Citizen Details</th>
                                        <th>Location</th>
                                        <th>Waste Profile</th>
                                        <th class="text-end pe-4">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="req" items="${requestedPickups}">
                                        <tr>
                                            <td class="ps-4">
                                                <div class="fw-bold text-dark fs-6">${req.citizen.name}</div>
                                                <div class="small text-muted">ID: #${req.id}</div>
                                            </td>
                                            <td class="text-muted"><i class="fa-solid fa-location-dot text-danger me-1"></i> ${req.address}</td>
                                            <td>
                                                <div class="d-flex flex-wrap gap-2">
                                                    <c:forEach var="item" items="${req.items}">
                                                        <span class="badge bg-secondary bg-opacity-10 text-dark border border-secondary rounded-pill px-3 py-2">
                                                            <i class="fa-solid fa-plug text-primary me-1"></i> ${item.itemType} <span class="badge bg-white text-dark ms-1 shadow-sm">${item.weight}kg</span>
                                                        </span>
                                                    </c:forEach>
                                                </div>
                                            </td>
                                            <td class="text-end pe-4 align-middle">
                                                <form action="/recycler/assign" method="post">
                                                    <input type="hidden" name="pickupId" value="${req.id}" />
                                                    <button type="submit" class="btn btn-primary rounded-pill shadow-sm hover-float px-4 fw-bold">
                                                        <i class="fa-solid fa-thumbtack me-1"></i> Accept Job
                                                    </button>
                                                </form>
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

        <!-- My Work Queue -->
        <div class="col-xl-12 mb-5">
            <div class="d-flex align-items-center mb-3">
                <h4 class="brand-font fw-bold m-0"><i class="fa-solid fa-list-check text-success me-2"></i> My Active Work Queue</h4>
            </div>
            
            <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                <div class="card-body p-0 bg-light">
                     <c:if test="${empty myPickups}">
                        <div class="text-center py-5">
                            <i class="fa-solid fa-bed fa-3x text-muted opacity-25 mb-3"></i>
                            <h6 class="fw-bold text-secondary">Queue is Empty</h6>
                            <p class="text-muted small">Accept jobs from the Request Pool above to start working.</p>
                        </div>
                    </c:if>
                    <c:if test="${not empty myPickups}">
                        <div class="table-responsive">
                            <table class="table table-modern w-100 m-0">
                                <thead class="text-secondary small text-uppercase tracking-wider">
                                    <tr>
                                        <th class="ps-4">ID</th>
                                        <th>Current Stage</th>
                                        <th style="width: 40%">Logistics Update (Notes)</th>
                                        <th class="text-end pe-4">Finalize</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="job" items="${myPickups}">
                                        <tr>
                                            <td class="ps-4 fw-bold fs-5 text-dark">#${job.id}</td>
                                            <td>
                                                <c:set var="statusColor" value="info"/>
                                                <c:if test="${job.status == 'COLLECTED'}"><c:set var="statusColor" value="primary"/></c:if>
                                                <c:if test="${job.status == 'SORTING'}"><c:set var="statusColor" value="warning"/></c:if>
                                                <c:if test="${job.status == 'SHREDDING'}"><c:set var="statusColor" value="danger"/></c:if>
                                                <c:if test="${job.status == 'CERTIFIED'}"><c:set var="statusColor" value="success"/></c:if>
                                                
                                                <span class="badge bg-${statusColor} bg-opacity-10 text-${statusColor == 'warning' ? 'dark' : statusColor} px-3 py-2 border border-${statusColor} rounded-pill shadow-sm">
                                                    <span class="status-dot"></span> ${job.status}
                                                </span>
                                            </td>
                                            <td>
                                                <c:if test="${job.status != 'CERTIFIED'}">
                                                    <form action="/recycler/update-stage" method="post" class="d-flex align-items-center bg-white p-2 rounded-3 border shadow-sm">
                                                        <input type="hidden" name="pickupId" value="${job.id}" />
                                                        <select name="stage" class="form-select border-0 bg-transparent fw-bold text-secondary me-2" style="width: auto; box-shadow: none;">
                                                            <option value="COLLECTED">COLLECTED</option>
                                                            <option value="SORTING">SORTING</option>
                                                            <option value="SHREDDING">SHREDDING</option>
                                                        </select>
                                                        <div class="vr mx-2"></div>
                                                        <input type="text" name="notes" class="form-control border-0 bg-transparent" placeholder="Add optional log notes..." style="box-shadow: none;"/>
                                                        <button type="submit" class="btn btn-dark text-white rounded-circle ms-2" style="width: 38px; height: 38px; flex-shrink: 0;" title="Update Status">
                                                            <i class="fa-solid fa-arrow-right"></i>
                                                        </button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${job.status == 'CERTIFIED'}">
                                                    <span class="text-success fw-bold"><i class="fa-solid fa-check-double me-1"></i> Processing Completed & Audited</span>
                                                </c:if>
                                            </td>
                                            <td class="text-end pe-4 align-middle">
                                                <c:if test="${job.status != 'CERTIFIED'}">
                                                    <form action="/recycler/issue-certificate" method="post">
                                                        <input type="hidden" name="pickupId" value="${job.id}" />
                                                        <button type="submit" class="btn btn-success rounded-pill shadow-sm hover-float px-3 fw-bold">
                                                            <i class="fa-solid fa-file-signature me-1"></i> Generate Certificate
                                                        </button>
                                                    </form>
                                                </c:if>
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
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const msgEl = document.getElementById('flash-msg');
        if(msgEl) {
            Swal.fire({
                icon: 'success',
                title: 'Request Updated',
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
