# RideShare Backend (Spring Boot + MongoDB + JWT)

This is a simplified Ride Sharing backend application built using **Spring Boot**, **MongoDB (Atlas)**, and **JWT-based authentication**.  
It supports **user & driver registration, authentication, ride requesting, accepting, and completing rides**.

---

## 🚀 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- MongoDB Atlas (cloud)
- JWT (JSON Web Tokens)
- Maven

---

## 📁 Project Structure

```text
src/main/java/
 └── com.example.project
      ├── config        → Security & JWT filter
      ├── controller    → REST APIs
      ├── dto           → Request/Response DTOs
      ├── exception     → Custom exceptions & global handler
      ├── model         → MongoDB entities
      ├── repository   → MongoDB repositories
      ├── service      → Business logic
      └── util         → JWT utility

src/main/resources/
 └── application.yaml   → App configuration (uses placeholders, no secrets)
```
---

## ⚙️ Configuration (Mongo URI & JWT Secret)
This project is configured so that no real credentials are stored in the repository.
You must set your own MongoDB URI and JWT secret locally using environment variables or simply replace the placeholders with your credentials.

```test
spring:
  data:
    mongodb:
      uri: "${MONGODB_URI}"

  application:
    name: rideshare

server:
  port: 8082

jwt:
  secret: "${JWT_SECRET}"
  expiration: 86400000
```

MONGODB_URI → your real MongoDB Atlas connection string <br>
JWT_SECRET → your own long random secret key for signing JWTs

---

## 🔐 Authentication Flow

1. User or Driver registers using the register API.
2. User or Driver logs in using the login API.
3. A JWT token is returned on successful login.
4. This JWT token must be sent in the `Authorization` header for all protected APIs.

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## ✅ API Endpoints

### 🔹 Authentication APIs

#### 1. Register  
`POST /api/auth/register`

```json
{
  "username": "user1",
  "password": "1234",
  "role": "ROLE_USER"
}
```
#### 2. Login  
`POST /api/auth/login`

```json
{
  "username": "user1",
  "password": "1234"
}
```
## 🚕 Ride APIs  
*(All ride APIs require JWT authentication)*

#### 1. Request a Ride (USER)  
`POST /api/v1/rides`

```json
{
  "pickupLocation": "Point A",
  "dropLocation": "Point B"
}
```
#### 2. Get User Rides  
`GET /api/v1/user/rides`

#### 3. View Pending Ride Requests (DRIVER)  
`GET /api/v1/driver/rides/requests`

#### 4. Accept Ride (DRIVER)  
`POST /api/v1/driver/rides/{rideId}/accept`

#### 5. Complete Ride (USER or DRIVER)  
`POST /api/v1/rides/{rideId}/complete`

---
## ▶️ How to Run the Project

1. Clone the repository  
2. Add MongoDB connection string and JWT secret  
3. Run the application.
4. Application will start on: `http://localhost:8082`
---

## ✅ Features Implemented

- User & Driver Registration  
- JWT-based Authentication  
- Role-based Authorization  
- Ride Request by User  
- Viewing Pending Ride Requests by Driver  
- Ride Acceptance by Driver  
- Ride Completion by User or Driver  
- User Ride History  
- Input Validation  
- Centralized Exception Handling  
- MongoDB Atlas Integration  
