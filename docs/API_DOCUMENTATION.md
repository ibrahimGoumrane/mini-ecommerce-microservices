# Microservices API Documentation

## Overview

This document provides comprehensive documentation for all API endpoints accessible through the API Gateway. The gateway runs on port `8080` and routes requests to the appropriate microservices.

**Gateway URL:** `http://localhost:8080`

---

## Table of Contents

- [Authentication](#authentication)
- [API Gateway Routing](#api-gateway-routing)
- [Auth Service Endpoints](#auth-service-endpoints)
- [Product Service Endpoints](#product-service-endpoints)
- [Order Service Endpoints](#order-service-endpoints)

---

## Authentication

### JWT Authentication

Most endpoints require JWT authentication. To access protected endpoints:

1. **Register** or **Login** to obtain a JWT token
2. Include the token in the `Authorization` header for protected requests:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

### Token Configuration
- **Secret:** Set via `JWT_SECRET` environment variable
- **Expiration:** 3600000ms (1 hour)

---

## API Gateway Routing

The API Gateway uses the following routing configuration:

| Service | Gateway Path | Internal URI | Authentication | StripPrefix |
|---------|-------------|--------------|----------------|-------------|
| **Auth Service** | `/api/v1/auth/**` | `http://auth-service:8081` | ❌ Public | Yes (removes `/api/v1`) |
| **Product Service** | `/api/v1/products/**` | `http://product-service:8082` | ✅ JWT Required | Yes (removes `/api/v1`) |
| **Order Service** | `/api/v1/orders/**` | `http://order-service:8083` | ✅ JWT Required | Yes (removes `/api/v1`) |

> **Note:** `StripPrefix=2` removes `/api/v1` from the path before forwarding to the backend service.

---

## Auth Service Endpoints

**Base URL (via Gateway):** `http://localhost:8080/api/v1/auth`

### 1. User Registration (Signup)

**Endpoint:** `POST /api/v1/auth/signup`  
**Authentication:** ❌ Public  
**Description:** Register a new user account

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "roles": "USER"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "roles": "USER"
    }
  }
}
```

---

### 2. User Login

**Endpoint:** `POST /api/v1/auth/login`  
**Authentication:** ❌ Public  
**Description:** Authenticate user and receive JWT token

**Request Body:**
```json
{
  "username": "john.doe@example.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@example.com",
      "roles": "USER"
    }
  }
}
```

---

### 3. Get Current User Info

**Endpoint:** `GET /api/v1/auth/me`  
**Authentication:** ✅ JWT Required  
**Description:** Retrieve authenticated user's information

**Headers:**
```
Authorization: Bearer <your-jwt-token>
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User information retrieved successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "roles": "USER"
  }
}
```

---

## Product Service Endpoints

**Base URL (via Gateway):** `http://localhost:8080/api/v1/products`  
**Authentication:** ✅ All endpoints require JWT

### 1. Get All Products

**Endpoint:** `GET /api/v1/products`  
**Description:** Retrieve all products

**Headers:**
```
Authorization: Bearer <your-jwt-token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "category": "Electronics",
    "stockQuantity": 50,
    "reservedQuantity": 5
  },
  {
    "id": 2,
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "price": 29.99,
    "category": "Accessories",
    "stockQuantity": 200,
    "reservedQuantity": 10
  }
]
```

---

### 2. Get Product by ID

**Endpoint:** `GET /api/v1/products/{id}`  
**Description:** Retrieve a specific product by ID

**Path Parameters:**
- `id` (Long): Product ID

**Example:** `GET /api/v1/products/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1299.99,
  "category": "Electronics",
  "stockQuantity": 50,
  "reservedQuantity": 5
}
```

---

### 3. Search Products

**Endpoint:** `GET /api/v1/products/search?name={searchTerm}`  
**Description:** Search products by name

**Query Parameters:**
- `name` (String): Search term

**Example:** `GET /api/v1/products/search?name=laptop`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "category": "Electronics",
    "stockQuantity": 50,
    "reservedQuantity": 5
  }
]
```

---

### 4. Get Products by Category

**Endpoint:** `GET /api/v1/products/category/{category}`  
**Description:** Retrieve all products in a specific category

**Path Parameters:**
- `category` (String): Category name

**Example:** `GET /api/v1/products/category/Electronics`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1299.99,
    "category": "Electronics",
    "stockQuantity": 50,
    "reservedQuantity": 5
  }
]
```

---

### 5. Create Product

**Endpoint:** `POST /api/v1/products`  
**Description:** Create a new product

**Request Body:**
```json
{
  "name": "Gaming Keyboard",
  "description": "RGB mechanical keyboard",
  "price": 149.99,
  "category": "Accessories",
  "stockQuantity": 100,
  "reservedQuantity": 0
}
```

**Response (201 Created):**
```json
{
  "id": 3,
  "name": "Gaming Keyboard",
  "description": "RGB mechanical keyboard",
  "price": 149.99,
  "category": "Accessories",
  "stockQuantity": 100,
  "reservedQuantity": 0
}
```

---

### 6. Update Product

**Endpoint:** `PUT /api/v1/products/{id}`  
**Description:** Update an existing product

**Path Parameters:**
- `id` (Long): Product ID

**Request Body:**
```json
{
  "name": "Gaming Keyboard Pro",
  "description": "RGB mechanical keyboard with custom switches",
  "price": 179.99,
  "category": "Accessories",
  "stockQuantity": 120,
  "reservedQuantity": 0
}
```

**Response (200 OK):**
```json
{
  "id": 3,
  "name": "Gaming Keyboard Pro",
  "description": "RGB mechanical keyboard with custom switches",
  "price": 179.99,
  "category": "Accessories",
  "stockQuantity": 120,
  "reservedQuantity": 0
}
```

---

### 7. Delete Product

**Endpoint:** `DELETE /api/v1/products/{id}`  
**Description:** Delete a product

**Path Parameters:**
- `id` (Long): Product ID

**Example:** `DELETE /api/v1/products/3`

**Response (204 No Content)**

---

### 8. Reserve Product Stock

**Endpoint:** `POST /api/v1/products/{id}/reserve?quantity={qty}`  
**Description:** Reserve stock for a product (used during order creation)

**Path Parameters:**
- `id` (Long): Product ID

**Query Parameters:**
- `quantity` (Integer): Quantity to reserve

**Example:** `POST /api/v1/products/1/reserve?quantity=2`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Stock reserved successfully"
}
```

**Insufficient Stock Response:**
```json
{
  "success": false,
  "message": "Insufficient stock"
}
```

---

### 9. Release Product Stock

**Endpoint:** `POST /api/v1/products/{id}/release?quantity={qty}`  
**Description:** Release reserved stock (used when order is cancelled)

**Path Parameters:**
- `id` (Long): Product ID

**Query Parameters:**
- `quantity` (Integer): Quantity to release

**Example:** `POST /api/v1/products/1/release?quantity=2`

**Response (200 OK)**

---

## Order Service Endpoints

**Base URL (via Gateway):** `http://localhost:8080/api/v1/orders`  
**Authentication:** ✅ All endpoints require JWT

### 1. Get All Orders

**Endpoint:** `GET /api/v1/orders`  
**Description:** Retrieve all orders

**Headers:**
```
Authorization: Bearer <your-jwt-token>
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "orderNumber": "ORD-1234567890",
    "customerId": 1,
    "totalAmount": 1329.98,
    "status": "PENDING",
    "createdAt": "2025-11-23T10:00:00",
    "updatedAt": "2025-11-23T10:00:00",
    "orderItems": [
      {
        "id": 1,
        "productId": 1,
        "quantity": 1,
        "price": 1299.99
      },
      {
        "id": 2,
        "productId": 2,
        "quantity": 1,
        "price": 29.99
      }
    ]
  }
]
```

---

### 2. Get Order by ID

**Endpoint:** `GET /api/v1/orders/{id}`  
**Description:** Retrieve a specific order by ID

**Path Parameters:**
- `id` (Long): Order ID

**Example:** `GET /api/v1/orders/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "orderNumber": "ORD-1234567890",
  "customerId": 1,
  "totalAmount": 1329.98,
  "status": "PENDING",
  "createdAt": "2025-11-23T10:00:00",
  "updatedAt": "2025-11-23T10:00:00",
  "orderItems": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 1,
      "price": 1299.99
    },
    {
      "id": 2,
      "productId": 2,
      "quantity": 1,
      "price": 29.99
    }
  ]
}
```

---

### 3. Get Order by Order Number

**Endpoint:** `GET /api/v1/orders/number/{orderNumber}`  
**Description:** Retrieve an order by its unique order number

**Path Parameters:**
- `orderNumber` (String): Order number

**Example:** `GET /api/v1/orders/number/ORD-1234567890`

**Response (200 OK):**
```json
{
  "id": 1,
  "orderNumber": "ORD-1234567890",
  "customerId": 1,
  "totalAmount": 1329.98,
  "status": "PENDING",
  "createdAt": "2025-11-23T10:00:00",
  "updatedAt": "2025-11-23T10:00:00",
  "orderItems": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 1,
      "price": 1299.99
    }
  ]
}
```

---

### 4. Get Orders by Customer ID

**Endpoint:** `GET /api/v1/orders/customer/{customerId}`  
**Description:** Retrieve all orders for a specific customer

**Path Parameters:**
- `customerId` (Long): Customer ID

**Example:** `GET /api/v1/orders/customer/1`

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "orderNumber": "ORD-1234567890",
    "customerId": 1,
    "totalAmount": 1329.98,
    "status": "PENDING",
    "createdAt": "2025-11-23T10:00:00",
    "updatedAt": "2025-11-23T10:00:00",
    "orderItems": [
      {
        "id": 1,
        "productId": 1,
        "quantity": 1,
        "price": 1299.99
      }
    ]
  }
]
```

---

### 5. Create Order

**Endpoint:** `POST /api/v1/orders`  
**Description:** Create a new order

**Request Body:**
```json
{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 1
    },
    {
      "productId": 2,
      "quantity": 2
    }
  ]
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "orderNumber": "ORD-0987654321",
  "customerId": 1,
  "totalAmount": 1359.97,
  "status": "PENDING",
  "createdAt": "2025-11-23T14:30:00",
  "updatedAt": "2025-11-23T14:30:00",
  "orderItems": [
    {
      "id": 3,
      "productId": 1,
      "quantity": 1,
      "price": 1299.99
    },
    {
      "id": 4,
      "productId": 2,
      "quantity": 2,
      "price": 29.99
    }
  ]
}
```

---

### 6. Update Order Status

**Endpoint:** `PATCH /api/v1/orders/{id}/status`  
**Description:** Update the status of an order

**Path Parameters:**
- `id` (Long): Order ID

**Request Body:**
```json
{
  "status": "CONFIRMED"
}
```

**Valid Status Values:**
- `PENDING`
- `CONFIRMED`
- `PROCESSING`
- `SHIPPED`
- `DELIVERED`
- `CANCELLED`

**Response (200 OK):**
```json
{
  "id": 1,
  "orderNumber": "ORD-1234567890",
  "customerId": 1,
  "totalAmount": 1329.98,
  "status": "CONFIRMED",
  "createdAt": "2025-11-23T10:00:00",
  "updatedAt": "2025-11-23T15:00:00",
  "orderItems": [
    {
      "id": 1,
      "productId": 1,
      "quantity": 1,
      "price": 1299.99
    }
  ]
}
```

---

### 7. Cancel Order

**Endpoint:** `DELETE /api/v1/orders/{id}`  
**Description:** Cancel an order

**Path Parameters:**
- `id` (Long): Order ID

**Example:** `DELETE /api/v1/orders/1`

**Response (204 No Content)**

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "success": false,
  "message": "Invalid request data",
  "error": "Validation failed"
}
```

### 401 Unauthorized
```json
{
  "success": false,
  "message": "Authentication required",
  "error": "Missing or invalid JWT token"
}
```

### 403 Forbidden
```json
{
  "success": false,
  "message": "Access denied",
  "error": "Insufficient permissions"
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Resource not found",
  "error": "The requested resource does not exist"
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "message": "Internal server error",
  "error": "An unexpected error occurred"
}
```

---

## Testing with Postman

### Step 1: Import Collection

You can create a Postman collection using this documentation. Here's a recommended workflow:

1. **Register a user** via `POST /api/v1/auth/signup`
2. **Copy the JWT token** from the response
3. **Set the token** as an environment variable in Postman
4. **Use the token** in the Authorization header for protected endpoints

### Step 2: Set Environment Variables

Create the following environment variables in Postman:

| Variable | Value |
|----------|-------|
| `gateway_url` | `http://localhost:8080` |
| `jwt_token` | `<your-token-here>` |

### Step 3: Configure Authorization

For protected endpoints:
1. Go to the **Authorization** tab
2. Select **Bearer Token**
3. Enter `{{jwt_token}}` as the token value

---

## Service Architecture

```
┌─────────────────┐
│   API Gateway   │  (Port 8080)
│   (WebFlux)     │
└────────┬────────┘
         │
         ├──────────────────┬──────────────────┬────────────────────┐
         │                  │                  │                    │
         v                  v                  v                    v
┌────────────────┐  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐
│ Auth Service   │  │Product Service │  │ Order Service  │  │  Database(s)   │
│  (Port 8081)   │  │  (Port 8082)   │  │  (Port 8083)   │  │                │
└────────────────┘  └────────────────┘  └────────────────┘  └────────────────┘
```

---

## Notes

- All timestamps are in ISO 8601 format
- Prices are in decimal format (e.g., 1299.99)
- The gateway strips `/api/v1` prefix before forwarding to backend services
- JWT tokens expire after 1 hour (3600000ms)
- Stock reservation is atomic and transaction-safe

---

**Last Updated:** 2025-11-23  
**Gateway Version:** 0.0.1-SNAPSHOT  
**Spring Boot Version:** 3.4.0  
**Spring Cloud Version:** 2024.0.0
