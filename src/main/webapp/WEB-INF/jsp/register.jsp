<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - E-Waste Portal</title>
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
            padding: 20px 0;
        }
        .register-wrapper {
            width: 100%;
            max-width: 600px;
            padding: 15px;
        }
        .form-control, .form-select {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.1);
            color: #fff;
        }
        .form-select option {
            background: var(--bg-dark);
            color: white;
        }
        .form-control:focus, .form-select:focus {
            background: rgba(255, 255, 255, 0.1);
            color: #fff;
            border-color: #10b981;
            box-shadow: 0 0 0 0.25rem rgba(16, 185, 129, 0.25);
        }
        .form-control::placeholder {
            color: rgba(255, 255, 255, 0.5);
        }
    </style>
</head>
<body>

<div class="register-wrapper">
    <div class="card glass-panel p-5 rounded-4">
        <div class="text-center mb-4">
            <i class="fa-solid fa-leaf fa-3x mb-3 text-gradient hover-float"></i>
            <h3 class="brand-font fw-bold mb-1">Join E-Waste Portal</h3>
            <p class="text-white-50">Create an account to track and recycle</p>
        </div>
        
        <form action="/register" method="post" class="needs-validation" novalidate id="regForm">
            <div class="row">
                <div class="col-md-6 mb-4">
                    <label class="form-label text-white-50 small fw-bold text-uppercase">Full Name</label>
                    <input type="text" name="name" class="form-control py-2" placeholder="John Doe" required/>
                    <div class="invalid-feedback text-warning">Please enter your name.</div>
                </div>
                <div class="col-md-6 mb-4">
                    <label class="form-label text-white-50 small fw-bold text-uppercase">Email</label>
                    <input type="email" name="email" class="form-control py-2" placeholder="john@example.com" required/>
                    <div class="invalid-feedback text-warning">Please provide a valid email.</div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-6 mb-4">
                    <label class="form-label text-white-50 small fw-bold text-uppercase">Username</label>
                    <input type="text" name="username" class="form-control py-2" placeholder="Choose a username" required/>
                    <div class="invalid-feedback text-warning">Username is required.</div>
                </div>
                <div class="col-md-6 mb-4">
                    <label class="form-label text-white-50 small fw-bold text-uppercase">Password</label>
                    <input type="password" name="password" class="form-control py-2" placeholder="Create a strong password" required/>
                    <div class="invalid-feedback text-warning">Password is required.</div>
                </div>
            </div>

            <div class="mb-5">
                <label class="form-label text-white-50 small fw-bold text-uppercase">Account Type</label>
                <select name="role" class="form-select py-2" required>
                    <option value="" disabled selected>Select your role</option>
                    <option value="ROLE_CITIZEN">Citizen</option>
                    <option value="ROLE_RECYCLER">Recycler Partner</option>
                    <option value="ROLE_ADMIN">System Admin</option>
                </select>
                <div class="invalid-feedback text-warning">Please select a role.</div>
            </div>

            <button type="submit" class="btn bg-gradient-success text-white w-100 py-3 fw-bold rounded-3 hover-float shadow-lg border-0 mb-4">
                <i class="fa-solid fa-user-plus me-2"></i> CREATE ACCOUNT
            </button>
        </form>

        <div class="text-center">
            <span class="text-white-50">Already have an account? <a href="/login" class="text-white fw-bold text-decoration-none">Login</a></span>
        </div>
    </div>
</div>

<script>
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    } else {
                        // Show brief loading swal before letting standard post go through
                        Swal.fire({
                            title: 'Registering...',
                            background: '#1e293b',
                            color: '#fff',
                            allowOutsideClick: false,
                            didOpen: () => {
                                Swal.showLoading()
                            }
                        });
                    }
                    form.classList.add('was-validated')
                }, false)
            })
    })();
</script>
</body>
</html>
