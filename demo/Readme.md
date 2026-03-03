# 🏥 Hospital Management System

A secure, production-ready REST API built with Java Spring Boot
for managing hospital operations including doctors, patients, 
and administrators.

## Features
- 🔐 JWT Authentication with role-based access control
- 🔒 AES-256 encryption for sensitive data (HIPAA compliant)
- 👨‍⚕️ Doctor management with full CRUD
- 🧑‍🤝‍🧑 Patient management
- 👤 Admin management
- 📧 Email notifications on account creation
- 🖼️ Image upload with Cloudinary
- 📄 API documentation with Swagger

## Tech Stack
- Java 17
- Spring Boot 3.2
- Spring Security + JWT
- PostgreSQL
- Docker
- Cloudinary

## API Documentation
Run the project and visit:
http://localhost:8080/swagger-ui.html

## Getting Started
\`\`\`bash
docker-compose up
\`\`\`
```

---

### Realistic job timeline:
```
Now → 2 weeks:
  Finish all 3 modules (Doctor, Patient, Admin)
  JWT working end to end
  Test everything in Postman

Week 3:
  Add Swagger documentation
  Write a good README
  Push clean code to GitHub
  Add Docker

Week 4:
  Add appointment scheduling
  Start applying for jobs
  Post progress on LinkedIn daily

Month 2:
  Start getting interviews
```

---

### What to say in interviews:

When they ask "tell me about your project":
```
"I built a Hospital Management System REST API in Java Spring Boot.
The system manages doctors, patients, and administrators with 
role-based JWT authentication. I implemented AES-256 encryption 
on all sensitive personal data to comply with HIPAA regulations, 
which meant I had to solve an interesting problem — encrypting 
fields breaks database queries, so I implemented SHA-256 hashing 
for lookups alongside the encryption. The system also handles 
image uploads via Cloudinary and sends automated email 
notifications."