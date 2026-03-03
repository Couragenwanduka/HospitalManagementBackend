# 🏥 Hospital Management System

A secure, production-ready REST API built with Java Spring Boot
for managing hospital operations including doctors, patients,
and administrators.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)
![JWT](https://img.shields.io/badge/JWT-Authentication-red)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📋 Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Security](#security)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Environment Variables](#environment-variables)
- [Project Structure](#project-structure)

---

## ✨ Features

- 🔐 JWT Authentication with role-based access control (Admin, Doctor, Patient)
- 🔒 AES-256 encryption for sensitive data (HIPAA compliant)
- 🔑 SHA-256 hashing for encrypted field lookups
- 👨‍⚕️ Doctor management with full CRUD operations
- 🧑‍🤝‍🧑 Patient management with full CRUD operations
- 👤 Admin management with full CRUD operations
- 📧 Automated email notifications on account creation
- 🖼️ Image upload with Cloudinary
- 📄 API documentation with Swagger UI
- 🗄️ Paginated API responses

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 3.2 | Backend framework |
| Spring Security | Authentication & Authorization |
| JWT | Stateless authentication |
| PostgreSQL | Database |
| JPA / Hibernate | ORM |
| AES-256 | Data encryption |
| BCrypt | Password hashing |
| Cloudinary | Image storage |
| Docker | Containerization |
| Swagger / OpenAPI | API documentation |
| Lombok | Boilerplate reduction |

---

## 🏗️ Architecture
```
src/
├── auth/                  # Login & JWT token generation
├── security/              # JWT filter, Security config, UserDetailsService
├── doctors/               # Doctor entity, service, controller, DTO
├── patients/              # Patient entity, service, controller, DTO
├── admin/                 # Admin entity, service, controller, DTO
├── utils/                 # Encryption service, Email service
└── config/                # Swagger config
```

---

## 🔒 Security

This project takes security seriously, particularly given the sensitive
nature of healthcare data.

- **JWT Authentication** — Stateless authentication using signed tokens
- **Role-Based Access Control** — Three roles: ADMIN, DOCTOR, PATIENT
- **AES-256 Encryption** — All sensitive fields (email, phone, address)
  are encrypted at rest
- **SHA-256 Hashing** — Used for encrypted field lookups without
  decrypting the entire dataset
- **BCrypt Password Hashing** — Industry standard password hashing
- **HIPAA Compliance** — Encryption and access control designed to
  meet HIPAA data protection requirements

### Role Permissions

| Endpoint | ADMIN | DOCTOR | PATIENT |
|---|---|---|---|
| /api/admin/** | ✅ | ❌ | ❌ |
| /api/doctors/** | ✅ | ✅ | ❌ |
| /api/patients/** | ✅ | ✅ | ✅ |
| /api/auth/login | ✅ | ✅ | ✅ |

---

## 🚀 Getting Started

### Prerequisites
- Docker & Docker Compose
- Java 17 (if running without Docker)
- PostgreSQL (if running without Docker)

### Run with Docker (Recommended)
```bash
# Clone the repository
git clone https://github.com/Couragenwanduka/HospitalManagementBackend.git

# Navigate to project directory
cd HospitalManagementBackend

# Start the application
docker-compose up
```

### Run without Docker
```bash
# Clone the repository
git clone https://github.com/Couragenwanduka/HospitalManagementBackend.git

# Navigate to project directory
cd HospitalManagementBackend

# Configure environment variables (see below)
# Then run
mvn spring-boot:run
```

---

## 📄 API Documentation

Once the project is running, visit:
```
http://localhost:8080/swagger-ui.html
```

You can test all endpoints directly from the browser.
Use the **Authorize** button to add your JWT token.

### Key Endpoints
```
POST   /api/auth/login          Login and get JWT token

POST   /api/doctors             Create doctor
GET    /api/doctors             Get all doctors (paginated)
GET    /api/doctors/{id}        Get doctor by ID
PUT    /api/doctors/{id}        Update doctor
DELETE /api/doctors/{id}        Delete doctor

POST   /api/patients            Create patient
GET    /api/patients            Get all patients (paginated)
GET    /api/patients/{id}       Get patient by ID
PUT    /api/patients/{id}       Update patient
DELETE /api/patients/{id}       Delete patient

POST   /api/admin               Create admin
GET    /api/admin/{id}          Get admin by ID
PUT    /api/admin/{id}          Update admin
DELETE /api/admin/{id}          Delete admin
```

---

## ⚙️ Environment Variables

Create an `application.properties` file with the following:
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_db
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# Encryption
encryption.secret=your_aes_256_secret_key

# Cloudinary
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret

# Email
mail.username=your_email
mail.password=your_email_password
```

> ⚠️ Never commit your actual credentials to GitHub.
> Use environment variables or a `.env` file.

---

## 📁 Project Structure
```
src/main/java/com/hospitalManagement/demo/
├── auth/
│   ├── AuthController.java
│   ├── LoginRequest.java
│   └── LoginResponse.java
├── security/
│   ├── JwtUtil.java
│   ├── JwtFilter.java
│   ├── SecurityConfig.java
│   └── CustomUserDetailsService.java
├── doctors/
│   ├── Doctor.java
│   ├── DoctorDTO.java
│   ├── DoctorMapper.java
│   ├── DoctorRepository.java
│   ├── DoctorService.java
│   └── DoctorController.java
├── patients/
│   ├── Patient.java
│   ├── PatientDTO.java
│   ├── PatientMapper.java
│   ├── PatientRepository.java
│   ├── PatientService.java
│   └── PatientController.java
├── admin/
│   ├── Admin.java
│   ├── AdminDTO.java
│   ├── AdminMapper.java
│   ├── AdminRepository.java
│   ├── AdminService.java
│   └── AdminController.java
└── utils/
    ├── EncryptService.java
    └── EmailService.java
```

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open
an issue first to discuss what you would like to change.

---

## 📝 License

[MIT](LICENSE)

---

⭐ If you found this project helpful, please give it a star!
```

---

**Two important things to do now:**

**1. Make sure your `.gitignore` has this** so you never commit passwords:
```
application.properties
.env