Grafana

Purpose: visualization/dashboarding of metrics and logs.

Grafana does not collect metrics itself — it reads data from Prometheus (or Loki for logs).

Lets you create dashboards, charts, and alerts for metrics collected by Prometheus.


###### Command to install Grafana with custom values:
helm install grafana prometheus-community/grafana -f k8s/grafana/values.yaml
###### Accessing Grafana UI:
kubectl port-forward svc/grafana 3000:80