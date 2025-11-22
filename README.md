# 🛒 Mini E-Commerce Microservices

A cloud-native e-commerce platform built with microservices architecture, Spring Boot, and Kubernetes.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Microservices](#microservices)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Deployment](#deployment)
- [Monitoring & Observability](#monitoring--observability)
- [Development](#development)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)

## 🎯 Overview

This project demonstrates a production-ready microservices architecture for an e-commerce platform. It includes user authentication, product catalog, inventory management, order processing, payment handling, and notifications.

## 🏗️ Architecture

The system follows microservices architecture principles with:

- **API Gateway**: Single entry point for all client requests
- **Service Discovery**: Dynamic service registration and discovery
- **Message Queue**: Asynchronous communication via RabbitMQ
- **Database per Service**: Each microservice has its own database
- **Monitoring**: Prometheus + Grafana for metrics, Loki for logs
- **Containerization**: Docker containers orchestrated by Kubernetes

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
┌──────▼──────────┐
│  API Gateway    │
└────────┬────────┘
         │
    ┌────┴────┬─────────┬─────────┬──────────┬──────────┐
    │         │         │         │          │          │
┌───▼───┐ ┌──▼──┐ ┌────▼────┐ ┌──▼───┐ ┌───▼────┐ ┌───▼────┐
│ Auth  │ │Prod │ │Inventory│ │Order │ │Payment │ │Notify  │
└───┬───┘ └──┬──┘ └────┬────┘ └──┬───┘ └───┬────┘ └───┬────┘
    │        │         │         │         │          │
    └────────┴─────────┴─────────┴─────────┴──────────┘
                         │
                    ┌────▼─────┐
                    │ RabbitMQ │
                    └──────────┘
```

## 🔧 Microservices

| Service                  | Description                                | Port | Database         |
| ------------------------ | ------------------------------------------ | ---- | ---------------- |
| **Auth Service**         | User authentication & JWT token management | 8081 | MySQL            |
| **Product Service**      | Product catalog & management               | 8082 | MySQL/PostgreSQL |
| **Inventory Service**    | Stock management & availability            | 8083 | MySQL/PostgreSQL |
| **Order Service**        | Order processing & management              | 8084 | MySQL/PostgreSQL |
| **Payment Service**      | Payment processing & transactions          | 8085 | MySQL/PostgreSQL |
| **Notification Service** | Email/SMS notifications                    | 8086 | -                |
| **Gateway Service**      | API Gateway & routing                      | 8080 | -                |

## 🛠️ Technology Stack

### Backend

- **Framework**: Spring Boot 4.0.0
- **Language**: Java 21
- **Build Tool**: Maven
- **Security**: Spring Security + JWT
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Message Queue**: RabbitMQ

### DevOps & Infrastructure

- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **Service Mesh**: (Optional) Istio
- **Monitoring**: Prometheus + Grafana
- **Logging**: Loki
- **CI/CD**: GitHub Actions / Jenkins

## 📦 Prerequisites

- **Java 21+**
- **Maven 3.8+**
- **Docker & Docker Compose**
- **Kubernetes** (Minikube/Docker Desktop/Cloud Provider)
- **kubectl CLI**
- **Helm 3+** (for installing RabbitMQ, Prometheus, etc.)
- **MySQL 8+**

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/ibrahimGoumrane/mini-ecommerce-microservices.git
cd mini-ecommerce-microservices
```

### 2. Build All Microservices

```bash
# Build each microservice
cd microservices/auth-service && mvn clean package && cd ../..
cd microservices/gateway-service && mvn clean package && cd ../..
cd microservices/product-service && mvn clean package && cd ../..
cd microservices/inventory-service && mvn clean package && cd ../..
cd microservices/order-service && mvn clean package && cd ../..
cd microservices/payment-service && mvn clean package && cd ../..
cd microservices/notification-service && mvn clean package && cd ../..
```

Or use the Makefile in each service:

```bash
cd microservices/auth-service && make build
```

### 3. Build Docker Images

```bash
# Build all images
cd microservices/auth-service && docker build -t auth-service:latest .
cd microservices/gateway-service && docker build -t gateway-service:latest .
# ... repeat for other services
```

### 4. Deploy to Kubernetes

#### Install Dependencies (Helm Charts)

```bash
# Add Helm repositories
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update

# Install RabbitMQ
kubectl create namespace messaging
helm install rabbitmq bitnami/rabbitmq -f k8s/rabbitmq/values.yaml -n messaging

# Install Prometheus (optional)
kubectl create namespace monitoring
helm install prometheus prometheus-community/prometheus -f k8s/prometheus/values.yaml -n monitoring

# Install Grafana (optional)
helm install grafana grafana/grafana -f k8s/grafana/values.yaml -n monitoring

# Install Loki (optional)
helm install loki grafana/loki-stack -f k8s/loki/values.yaml -n monitoring
```

#### Deploy Microservices

```bash
# Create namespace
kubectl create namespace ecommerce

# Deploy all services
kubectl apply -f k8s/services/ -n ecommerce

# Deploy ingress/gateway
kubectl apply -f k8s/gateway/ -n ecommerce
```

### 5. Verify Deployment

```bash
# Check pods
kubectl get pods -n ecommerce

# Check services
kubectl get svc -n ecommerce

# Check ingress
kubectl get ingress -n ecommerce
```

## 📁 Project Structure

```
mini-ecommerce-microservices/
├── microservices/              # All microservice applications
│   ├── auth-service/          # Authentication & authorization
│   ├── gateway-service/       # API Gateway
│   ├── product-service/       # Product management
│   ├── inventory-service/     # Inventory management
│   ├── order-service/         # Order processing
│   ├── payment-service/       # Payment processing
│   └── notification-service/  # Notifications
├── k8s/                       # Kubernetes manifests
│   ├── services/              # Service deployments & services
│   ├── gateway/               # Ingress configuration
│   ├── rabbitmq/              # RabbitMQ Helm values
│   ├── prometheus/            # Prometheus Helm values
│   ├── grafana/               # Grafana Helm values
│   └── loki/                  # Loki Helm values
├── docs/                      # Documentation
└── README.md
```

## 🎯 Kubernetes Concepts Guide

| Concept             | Description                                                    | Analogy                         |
| ------------------- | -------------------------------------------------------------- | ------------------------------- |
| **Deployment YAML** | Defines what container image to run, replicas, ports, env vars | Recipe for your microservice    |
| **Service YAML**    | Provides stable endpoint to access pods                        | Phone number for your app       |
| **Ingress/Gateway** | Routes external traffic to appropriate services                | Receptionist directing visitors |
| **Helm Chart**      | Package manager for Kubernetes configs                         | Pre-configured installation kit |
| **ConfigMap**       | Non-sensitive configuration data                               | Settings file                   |
| **Secret**          | Sensitive data (passwords, tokens)                             | Locked safe for credentials     |

## 📊 Monitoring & Observability

### Prometheus

Access metrics at: `http://<minikube-ip>:30090`

### Grafana

Access dashboards at: `http://<minikube-ip>:30030`

- Default credentials in `k8s/grafana/values.yaml`

### Loki

Centralized logging accessible through Grafana

## 💻 Development

### Running Locally (Without Kubernetes)

Each microservice can be run independently:

```bash
cd microservices/auth-service
./mvnw spring-boot:run
```

Make sure to configure database and RabbitMQ connections in `application.properties`.

### Adding a New Microservice

1. Create service in `microservices/` directory
2. Add Dockerfile
3. Create Kubernetes manifests in `k8s/services/`
4. Update this README

## 📚 API Documentation

Once deployed, access Swagger UI at:

- Auth Service: `http://<gateway-url>/auth/swagger-ui.html`
- Product Service: `http://<gateway-url>/products/swagger-ui.html`
- ... (similar for other services)

### Sample Endpoints

```
POST   /auth/register          - Register new user
POST   /auth/login             - Login and get JWT token
GET    /products               - List all products
POST   /products               - Create new product
GET    /orders                 - List orders
POST   /orders                 - Create new order
POST   /payments               - Process payment
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👤 Author

**Ibrahim Goumrane**

- GitHub: [@ibrahimGoumrane](https://github.com/ibrahimGoumrane)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Kubernetes community
- All open-source contributors

---

⭐ Star this repo if you find it helpful!
