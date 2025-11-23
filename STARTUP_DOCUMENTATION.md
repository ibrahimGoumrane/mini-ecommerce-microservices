# Microservices Kubernetes Deployment

This repository contains a microservices application deployed on Kubernetes using Minikube. It includes services for authentication, products, orders, gateway routing, MySQL, and RabbitMQ.

---

## Prerequisites

- [Docker](https://www.docker.com/get-started) installed
- [Minikube](https://minikube.sigs.k8s.io/docs/start/) installed
- [kubectl](https://kubernetes.io/docs/tasks/tools/) installed
- Optional: [Helm](https://helm.sh/docs/intro/install/) (if you want to manage charts)
- Internet access to pull Docker images

---

## Steps to Start the Microservices

### 1. Start Minikube
Start Minikube with enough resources:

```bash
minikube start --cpus=4 --memory=6000
```

Check the cluster:

```bash
kubectl get nodes
```

---

### 2. Enable Minikube Addons

Enable the Ingress controller:

```bash
minikube addons enable ingress
```

---

### 3. Create Configs

Create namespace:

```bash
kubectl apply -f .\k8s\config\namespace.yaml
```
Create Kubernetes secrets for sensitive data:

```bash
kubectl apply -f .\k8s\config\secrets.yaml -n microservices
```

Create Kubernetes configmap for sensitive data:

```bash
kubectl apply -f .\k8s\config\configmap.yaml -n microservices
```
---

### 4. Deploy Services and Deployments

Apply all deployment YAMLs:

```bash
kubectl apply -f .\k8s\services\mysql-deploy.yaml -n microservices
kubectl apply -f .\k8s\rabbitmq\values.yaml -n microservices
kubectl apply -f .\k8s\services\auth-service-deploy.yaml -n microservices
kubectl apply -f .\k8s\services\order-service-deploy.yaml -n microservices
kubectl apply -f .\k8s\services\product-service-deploy.yaml -n microservices
kubectl apply -f .\k8s\services\gateway-service-deploy.yaml -n microservices
```

Check pods:

```bash
kubectl get pods -n microservices -w
```

---

### 5. Deploy Ingress

Apply the ingress configuration:

```bash
kubectl apply -f .\k8s\gateway\ingress.yaml -n microservices
```

---

### 6. Expose Services Locally (Minikube Service)

Run the minikube service to access services from localhost:

```bash
minikube service api-gateway -n microservices
```

* Keep this terminal open while testing.
* You can access the API via `http://localhost:<minikube-assigned-port>`.

Check services and their endpoints:

```bash
kubectl get svc -n microservices
kubectl get endpoints -n microservices
kubectl get ingress -n microservices
kubectl get pods -n microservices
kubectl logs <pod-name> -n microservices
```

---

### 7. Access Services

* Use the gateway service URL provided by `minikube tunnel` or ingress.
* Example:

```bash
curl http://localhost:58693/api/v1/auth/signup
```

> Note: Ports may vary depending on the minikube tunnel assignment.

---

### 8. Restarting Services

To restart a service after updating its image or config:

```bash
kubectl rollout restart deployment <deployment-name> -n microservices
```

Check rollout status:

```bash
kubectl get pods -n microservices -w
```

---

### 9. Logs & Debugging

Get logs of a specific pod:

```bash
kubectl logs <pod-name> -n microservices
```

Follow logs of all pods in real-time:

```bash
kubectl logs -n microservices -l app=<service-label> --follow
```

---

### Notes

* Make sure all services point to the correct ports (gateway to microservices on port 8080).
* Ensure secrets and environment variables are properly configured.
* JWT secret must be 256-bit for HMAC-SHA algorithms.
* During rolling updates, old pods terminate automatically while new pods come up.

---
