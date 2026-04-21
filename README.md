# ♻️ E-Waste Collection & Recycling Portal

A full-stack **Spring Boot Enterprise Application** that enables citizens to schedule e-waste pickups, recyclers to process waste through defined stages, and administrators to monitor system activity and environmental impact.

----

## 🚀 Project Overview

This system digitizes the e-waste recycling workflow:

* Citizens request pickup of electronic waste
* Recyclers process waste through multiple stages
* Admin monitors operations and SDG impact

The application follows a **real-world enterprise architecture** using Spring Boot, Hibernate, and MySQL.

---

## 🎯 Features

### 👤 Citizen

* Schedule e-waste pickup
* Track pickup status (stage-wise)
* View recycling history
* Download recycling certificates
* View personal environmental impact

---

### ♻️ Recycler

* View available and assigned pickups
* Update processing stages:

  * Collected → Sorting → Shredding → Completed
* Issue recycling certificate
* View processing statistics

---

### 🛠 Admin

* Dashboard with system analytics
* Monitor all pickups
* View total e-waste recycled (kg)
* Filter and search pickups
* Visual charts (status & trends)

---

## 🔄 Pickup Workflow

```text
Scheduled → Assigned → Collected → Sorting → Shredding → Completed → Certificate Issued
```

---

## 🏗️ Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring Security
* Hibernate (JPA)
* JDBC (PreparedStatement for batch operations)

### Frontend

* Thymeleaf (HTML templates) / JSP
* Bootstrap 5
* Chart.js

### Database

* MySQL

---

## 📚 Rubric Mapping

### ✅ CO1 — OOP, JDBC, Multithreading

* Entity classes implementing OOP concepts
* JDBC `PreparedStatement` for batch insert
* `@Async` used for background notifications

---

### ✅ CO2 — Web Layer & Security

* Spring MVC Controllers
* Thymeleaf/JSP for views
* Session-based authentication (Spring Security)
* Role-based access control
* Security filter chain

---

### ✅ CO3 — Spring + Hibernate

* Layered architecture:
  Controller → Service → Repository
* Hibernate ORM with JPA annotations
* Transaction management using `@Transactional`

---

### ⚠️ CO4 — Socket Programming

* Not applicable for this problem statement

---

## 🌱 SDG Integration

This project aligns with:

### 🌍 SDG 12 — Responsible Consumption & Production

### 📊 Impact Metric:

* Total e-waste recycled (in kilograms)
* Displayed on admin dashboard

---

## ⚙️ Setup Instructions

1. Clone the repository:

```bash
git clone https://github.com/DK0091/Ewaste_Portal.git
```

2. Open in Eclipse / IntelliJ

3. Configure MySQL database:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ewaste_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Run the application:

```bash
mvn spring-boot:run
```

5. Open browser:

```text
http://localhost:8080
```

---

## 🔐 Default Roles

* CITIZEN
* RECYCLER
* ADMIN

---

## ⚡ Special Features

* Asynchronous notifications using `@Async`
* Batch database operations using JDBC
* Role-based dashboards
* Dark & Light theme support
* Structured status workflow

---

## 📸 Screenshots



---

## 🧠 Learning Outcomes

* Built a full-stack enterprise web application
* Applied Spring Boot, Hibernate, and MVC architecture
* Implemented security and session handling
* Integrated database with both JPA and JDBC
* Designed a real-world workflow system

---

## 📌 Future Scope

* Real-time notifications (WebSocket)
* Email/SMS alerts
* Mobile app integration
* AI-based waste classification

---

## 👨‍💻 Author

**Darshan Kale**


---

## 📄 License

This project is for academic purposes.
