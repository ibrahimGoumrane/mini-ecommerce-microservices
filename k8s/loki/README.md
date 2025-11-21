Loki

Purpose: centralized logging.

Collects logs from all microservices (stdout / log files) and stores them.

Grafana can query Loki to visualize logs in dashboards.

Very useful for debugging and monitoring the system, especially in microservices where logs are scattered across many pods.

###### Command to install Loki with custom values:
helm install loki grafana/loki -f k8s/loki/values.yaml 