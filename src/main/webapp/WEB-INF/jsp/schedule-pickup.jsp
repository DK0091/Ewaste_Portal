<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Schedule Pickup - E-Waste Portal</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/global.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body {
            background-color: #f8fafc;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        .form-floating > label {
            color: #64748b;
        }
        .form-control:focus, .form-select:focus {
            border-color: #10b981;
            box-shadow: 0 0 0 0.25rem rgba(16, 185, 129, 0.25);
        }
    </style>
</head>
<body>

<nav class="navbar navbar-light bg-white shadow-sm mb-5">
    <div class="container">
        <a class="navbar-brand fw-bold text-secondary" href="/citizen/dashboard">
            <i class="fa-solid fa-arrow-left me-2"></i> Back to Dashboard
        </a>
    </div>
</nav>

<div class="container flex-grow-1">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
            <div class="text-center mb-4">
                 <div class="d-inline-flex bg-success bg-opacity-10 text-success p-3 rounded-circle mb-3 shadow-sm">
                     <i class="fa-solid fa-truck-pickup fa-2x"></i>
                 </div>
                 <h3 class="brand-font fw-bold">Schedule an E-Waste Pickup</h3>
                 <p class="text-muted">Fill out the form below and our partners will collect it from your location.</p>
            </div>

            <div class="card shadow-lg border-0 rounded-4 p-4 p-md-5 bg-white">
                <form action="/citizen/schedule" method="post" class="needs-validation" novalidate>
                    
                    <div class="form-floating mb-4">
                        <textarea name="address" class="form-control" placeholder="Address" id="floatingAddress" style="height: 100px" required></textarea>
                        <label for="floatingAddress"><i class="fa-solid fa-location-dot me-1"></i> Exact Pickup Address</label>
                        <div class="invalid-feedback">Please provide an address for pickup.</div>
                    </div>

                    <div class="form-floating mb-4">
                        <input type="date" name="date" class="form-control" id="floatingDate" placeholder="Date" required/>
                        <label for="floatingDate"><i class="fa-regular fa-calendar me-1"></i> Preferred Date</label>
                        <div class="invalid-feedback">Please select a valid date.</div>
                    </div>

                    <div class="form-floating mb-4">
                        <select name="itemType" class="form-select" id="floatingType" required>
                            <option value="" disabled selected>Select Primary Item Type</option>
                            <option value="Mobile/Tablet">Mobile / Tablet Device</option>
                            <option value="Laptop/PC">Laptop / Personal Computer</option>
                            <option value="Large Appliance">Large Household Appliance (TV, Fridge)</option>
                            <option value="Miscellaneous">Miscellaneous Components</option>
                        </select>
                        <label for="floatingType"><i class="fa-solid fa-plug me-1"></i> Waste Category</label>
                         <div class="invalid-feedback">Please categorize the e-waste.</div>
                    </div>

                    <div class="form-floating mb-5">
                        <input type="number" step="0.1" name="weight" class="form-control" id="floatingWeight" placeholder="Weight" required/>
                        <label for="floatingWeight"><i class="fa-solid fa-weight-scale me-1"></i> Estimated Total Weight (kg)</label>
                        <div class="invalid-feedback">Please enter an approximate weight.</div>
                    </div>

                    <button type="submit" class="btn bg-gradient-success text-white w-100 fw-bold py-3 pt-3 rounded-pill hover-float shadow-lg border-0 fs-5">
                        <i class="fa-solid fa-check-circle me-1"></i> Confirm Schedule
                    </button>
                    
                </form>
            </div>
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
                        Swal.fire({
                            title: 'Scheduling...',
                            text: 'Processing your pickup request',
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
