Prometheus

Purpose: metrics collection (numbers about your system).

Collects quantitative data from your apps and cluster, like:

CPU / memory usage of pods

Number of requests per second

Response time of your microservices

Custom application metrics (e.g., orders processed, payments completed)

Prometheus scrapes metrics from endpoints exposed by your services, usually at /actuator/prometheus in Spring Boot.

Metrics are time-series data, meaning they are tracked over time.

###### Command to install Prometheus with custom values:
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm install prometheus prometheus-community/kube-prometheus-stack -f k8s/prometheus/values.yaml


###### Accessing Prometheus UI:
kubectl port-forward svc/prometheus-kube-prometheus-prometheus 9090:9090