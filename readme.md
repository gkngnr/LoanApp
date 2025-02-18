# Loan API

## 📌 Overview
This is a **Spring Boot Loan API** for a banking system that allows **Admins** to create customers, issue loans, and process payments, while **Customers** can only list their own loans and installments.

## 🚀 Features
- **User Authentication & Authorization** (Basic Authentication with roles: `ADMIN` and `CUSTOMER`)
- **Loan Management** (Admins can create and pay loans)
- **Installment Tracking** (Customers can view their loan installments)
- **Penalty & Reward Logic** (Automatic adjustments for early/late payments)

---

## 🛠️ Tech Stack
- **Java 21**
- **Spring Boot** (Spring Security, Spring Data JPA, Spring Web)
- **H2 Database** (For testing, can be replaced with PostgreSQL/MySQL)

---

## ⚙️ Setup & Installation

### 1️⃣ Clone the Repository
```sh
 git clone https://github.com/gkngnr/LoanApp.git
 cd LoanApp
```

### 2️⃣ Build the Project
```sh
mvn clean install
```

### 3️⃣ Run the Application
```sh
mvn spring-boot:run
```

The application will start on **`http://localhost:8080`**.


## 🔐 Authentication & Roles
The API uses **Basic Authentication** with two roles:

| Role    | Permissions |
|---------|------------|
| **ADMIN** | Create customers, create loans, process payments |
| **CUSTOMER** | View their own loans and installments only |

#### Default Credentials (for testing):
```
Admin:    admin / adminx
Customer1: john.doe / john.doex
Customer2: jane.doe / jane.doex
```

---

## 📌 API Endpoints

### 🔹 Loan Management
| Method | Endpoint            | Role | Description |
|--------|---------------------|------|-------------|
| `POST` | `/api/v1/customers` | ADMIN | Create a new customer |
| `POST` | `/api/v1/loans`       | ADMIN | Create a new loan |
| `GET` | `/api/v1/loans`       | ADMIN/CUSTOMER | List loans (Admins get all loans, Customers get only their loans) |
| `POST` | `/api/v1/loan/pay`    | ADMIN | Pay a loan installment |

### 🔹 Installments
| Method | Endpoint                  | Role | Description |
|--------|---------------------------|------|-------------|
| `GET` | `/api/v1/loans/{loanId}/installments` | ADMIN/CUSTOMER | List installments for a loan |

---

## 🧪 Running Tests

### 1️⃣ Unit Tests
Run unit tests using:
```sh
mvn test
```

### 2️⃣ Integration Tests (Testcontainers + H2)
Run integration tests using:
```sh
mvn verify
```

## 📜 Exception Handling
The API uses `@RestControllerAdvice` to return structured error responses. Example:
```json
{
  "timestamp": "2025-02-14T12:34:56",
  "status": 404,
  "error": "Not Found",
  "path": "/api/v1/customers/123"
}
```

## 👨‍💻 Author
**Gokhan Guner**  
---

