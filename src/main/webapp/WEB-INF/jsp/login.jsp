<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - E-Waste Portal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/global.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body { 
            background: url('/images/ewaste_login_bg.png') no-repeat center center fixed; 
            background-size: cover;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0;
        }
        .login-wrapper {
            width: 100%;
            max-width: 450px;
            padding: 15px;
        }
        .form-control {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
            color: #fff;
        }
        .form-control:focus {
            background: rgba(255, 255, 255, 0.1);
            color: #fff;
            border-color: #10b981;
            box-shadow: 0 0 0 0.25rem rgba(16, 185, 129, 0.25);
        }
        .form-control::placeholder {
            color: rgba(255, 255, 255, 0.5);
        }
        .input-group-text {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
            color: #10b981;
        }
    </style>
</head>
<body>

<div class="login-wrapper">
    <div class="card glass-panel p-5 rounded-4">
        <div class="text-center mb-4">
            <i class="fa-solid fa-microchip fa-3x mb-3 text-gradient hover-float"></i>
            <h3 class="brand-font fw-bold mb-1">E-Waste Portal</h3>
            <p class="text-white-50">Enter the eco-system</p>
        </div>
        
        <form action="/login" method="post" class="needs-validation" novalidate>
            <div class="mb-4">
                <label class="form-label text-white-50 small fw-bold text-uppercase tracking-wider">Username</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                    <input type="text" name="username" class="form-control py-2" placeholder="Your username" required />
                    <div class="invalid-feedback text-warning">Username is required.</div>
                </div>
            </div>
            <div class="mb-5">
                <label class="form-label text-white-50 small fw-bold text-uppercase tracking-wider">Password</label>
                <div class="input-group">
                    <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                    <input type="password" name="password" class="form-control py-2" placeholder="Your password" required />
                    <div class="invalid-feedback text-warning">Password is required.</div>
                </div>
            </div>
            <button type="submit" class="btn bg-gradient-success text-white w-100 py-3 fw-bold rounded-3 hover-float shadow-lg border-0 mb-4">
                <i class="fa-solid fa-right-to-bracket me-2"></i> SECURE LOGIN
            </button>
        </form>

        <div class="text-center">
            <span class="text-white-50">New to the portal? <a href="/register" class="text-white fw-bold text-decoration-none">Register</a></span>
        </div>
    </div>
</div>

<script>
    // Validation Script
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
    })();

    // SweetAlert Interceptors for URL parameters
    document.addEventListener("DOMContentLoaded", function() {
        const urlParams = new URLSearchParams(window.location.search);
        
        if(urlParams.has('error')) {
            Swal.fire({
                icon: 'error',
                title: 'Access Denied',
                text: 'Invalid username or password!',
                background: '#1e293b',
                color: '#fff',
                confirmButtonColor: '#ef4444'
            });
            // Clean URL after alert
            window.history.replaceState({}, document.title, "/login");
        }
        
        if(urlParams.has('logout')) {
            Swal.fire({
                icon: 'success',
                title: 'Logged Out',
                text: 'You have been securely logged out.',
                background: '#1e293b',
                color: '#fff',
                confirmButtonColor: '#10b981',
                timer: 2500,
                showConfirmButton: false
            });
            window.history.replaceState({}, document.title, "/login");
        }
        
        if(urlParams.has('registered')) {
            Swal.fire({
                icon: 'success',
                title: 'Registration Complete',
                text: 'Welcome! You can now log in securely.',
                background: '#1e293b',
                color: '#fff',
                confirmButtonColor: '#10b981'
            });
            window.history.replaceState({}, document.title, "/login");
        }
    });
</script>
</body>
</html>
