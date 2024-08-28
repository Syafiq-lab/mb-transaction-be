# Transaction BackEnd API

## Overview

This project is a Spring Boot application that provides RESTful APIs for managing transactions. It includes authentication using JWT tokens, role-based access control, and comprehensive error handling with custom exceptions.

## Prerequisites

- Java 17
- Maven
- MySQL Database

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-repo/transaction-backend.git
cd transaction-backend
```

### 2. Configure the Application
   Create an application.properties file in the src/main/resources directory with the following content:
   ```properties
   # Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/maybank_test
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JWT Configuration
jwt.secret=your_jwt_secret_key

# Spring Boot Configuration
spring.main.allow-circular-references=true
spring.mvc.pathmatch.matching-strategy=ant_path_matcher
spring.jpa.show-sql=true
```

### 3. Build and Run the Application
Use Maven to build and run the application:

```mvn
mvn clean install
mvn spring-boot:run
```

## Available APIs
### Authentication
1. Register a New User
   - Endpoint: /auth/register
   - Method: POST 
- Request Body
```json
{
    "username": "newuser",
    "password": "newpassword"
}
```
- Response:
```json
{
    "success": true,
    "message": "User registered successfully",
    "data": {
        "id": 1,
        "username": "newuser",
        "password": "$2a$10$...",
        "role": null
    }
}
```
2.  Login and Get JWT Token
    - Endpoint: /auth/login
    - Method: POST
- Request Body
```json
{
  "username": "newuser",
  "password": "newpassword"
}
```
- Response:
```json
{
  "token": "your_jwt_token"
}
```
## Transaction Management
1. Register a New User
    - Endpoint: /transactions
    - Method: GET
    - Headers:
      - Authorization: Bearer your_jwt_token
- Response:
```json
{
  "success": true,
  "message": "Transactions retrieved successfully",
  "data": {
    "transactions": [
      {
        "id": 1,
        "accountNumber": "123456789",
        "trxAmount": 1000.0,
        "description": "Deposit",
        "trxDate": "2023-08-01",
        "trxTime": "10:00:00",
        "customerId": "C001",
        "version": 1
      }
    ],
    "currentPage": 0,
    "totalItems": 1,
    "totalPages": 1
  }
}
```

2.  Get a Transaction by ID
    - Endpoint: /transactions/{id}
    - Method: GET
    - Headers:
        - Authorization: Bearer your_jwt_token
- Response:
```json
{
  "success": true,
  "message": "Transaction retrieved successfully",
  "data": {
    "id": 1,
    "accountNumber": "123456789",
    "trxAmount": 1000.0,
    "description": "Deposit",
    "trxDate": "2023-08-01",
    "trxTime": "10:00:00",
    "customerId": "C001",
    "version": 1
  }
}

```

3. Create a New Transaction
    - Endpoint: /transactions
    - Method: POST
    - Headers:
        - Authorization: Bearer your_jwt_token

- Request Body
```json
{
  "accountNumber": "123456789",
  "trxAmount": 1000.0,
  "description": "Deposit",
  "trxDate": "2023-08-01",
  "trxTime": "10:00:00",
  "customerId": "C001"
}

```
- Response:
```json
{
    "success": true,
    "message": "Transaction created successfully",
    "data": {
        "id": 2,
        "accountNumber": "123456789",
        "trxAmount": 1000.0,
        "description": "Deposit",
        "trxDate": "2023-08-01",
        "trxTime": "10:00:00",
        "customerId": "C001",
        "version": 1
    }
}

```

4. Update a Transaction
    - Endpoint: /transactions/{id}
    - Method: PUT
    - Headers:
        - Authorization: Bearer your_jwt_token

- Request Body
```json
{
  "description": "Updated description",
  "version": 1
}

```
- Response:
```json
{
  "success": true,
  "message": "Transaction updated successfully",
  "data": {
    "id": 1,
    "accountNumber": "123456789",
    "trxAmount": 1000.0,
    "description": "Updated description",
    "trxDate": "2023-08-01",
    "trxTime": "10:00:00",
    "customerId": "C001",
    "version": 2
  }
}
```

5. Delete a Transaction
    - Endpoint: /transactions/{id}
    - Method: DELETE
    - Headers:
        - Authorization: Bearer your_jwt_token
- Response:
```json
{
  "success": true,
  "message": "Transaction deleted successfully",
  "data": null
}
```

## Error Handling
The API uses a consistent response structure for both successful and error responses. Here's an example of the response structure:

- Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```
- Error Response:
```json
{
  "success": false,
  "message": "Error message",
  "data": null
}

```

## Logging and Error Handling
All exceptions are handled by the GlobalExceptionHandler class.
Detailed logging is provided using SLF4J, including logging for both successful operations and exceptions.

```mermaid
sequenceDiagram
    participant User as User
    participant Controller as Controller
    participant JwtRequestFilter as JwtRequestFilter
    participant AuthManager as AuthenticationManager
    participant JwtTokenUtil as JwtTokenUtil
    participant Service as Service
    participant ExceptionHandler as ExceptionHandler
    participant LoggingAspect as LoggingAspect

    User->>JwtRequestFilter: Send Request (login/transaction)
    JwtRequestFilter->>Controller: Pass validated request
    Controller->>AuthManager: Authenticate (if login)
    AuthManager->>JwtTokenUtil: Generate JWT Token
    JwtTokenUtil-->>Controller: Return JWT Token
    Controller->>Service: Process business logic (transaction)
    Service-->>Controller: Return response
    Controller-->>User: Send response (JWT/Transaction)
    Controller->>ExceptionHandler: Handle exceptions (if any)
    ExceptionHandler-->>User: Return error response
    LoggingAspect->>Controller: Log before and after method execution
    LoggingAspect-->>LoggingAspect: Log results

```