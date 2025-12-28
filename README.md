# Kafka Case – Debezium CDC Pipeline

This project demonstrates a **Change Data Capture (CDC)** pipeline using **PostgreSQL, Debezium, Kafka, and a Java Spring Boot application**, fully containerized with **Docker Compose**.

The system captures database changes from PostgreSQL, publishes them to Kafka via Debezium, processes the data in a Java application, and writes the transformed output back to Kafka.

---

##  Architecture Overview

- **PostgreSQL** – Source database  
- **Debezium PostgreSQL Connector** – Captures INSERT / UPDATE / DELETE events  
- **Apache Kafka** – Message broker  
- **Java Spring Boot Application** – Consumes CDC events, transforms data, and publishes results  
- **Docker Compose** – Orchestrates all services  

> ⚠️ The application is designed **only for PostgreSQL** and relies on Debezium CDC.

---

##  Getting Started

### Prerequisites

- Docker  
- Docker Compose  
- Java 17+ (only required to run the Spring Boot application)

---

### Start Docker Services

Make sure Docker Desktop is running.  
Navigate to the project root directory and run:

```bash
docker-compose up -d
```
---

###  Database Access

PostgreSQL is running inside Docker Compose and is already initialized.

To connect to the database container:

```bash
docker exec -it <postgres_container_id> psql -U postgres kafka-case
```
