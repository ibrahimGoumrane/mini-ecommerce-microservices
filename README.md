"# mini-ecommerce-microservices"

| Concept                          | Analogy                                                                                                |
| -------------------------------- | ------------------------------------------------------------------------------------------------------ |
| **Deployment YAML**              | Recipe for your microservice: what image to run, how many copies (pods), ports, environment variables. |
| **Service YAML**                 | Way for other apps to reach your microservice, like a phone number.                                    |
| **Ingress / Gateway YAML**       | The receptionist who routes incoming traffic to the right microservice.                                |
| **Helm chart**                   | Pre-made bundle of instructions that you can customize with values — like a cooking kit.               |
| **Prometheus/Grafana/Loki YAML** | Instructions for installing monitoring & logging systems on your cluster.                              |


/k8s
    rabbitmq/        <-- Install RabbitMQ via Helm chart (simple)
    prometheus/      <-- Install Prometheus via Helm chart (optional for metrics)
    grafana/         <-- Install Grafana via Helm chart (optional)
    gateway/         <-- Simple ingress YAML to route traffic to your services
    services/        <-- One Deployment + Service YAML per microservice (auth, product, order...)