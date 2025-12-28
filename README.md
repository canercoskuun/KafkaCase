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
---
### Kafka Topics

**kafka_case.public.package** :Debezium publishes all raw `INSERT / UPDATE / DELETE` changes captured from PostgreSQL. This topic acts as the main CDC source topic.

**mapped_package_cdc**:Contains records transformed by the Java application.The source of this topic is `kafka_case.public.package`.

**bootstrap_mapped_package** :Used for the bulk bootstrap process.All existing package records are transformed and published to this topic.  
Triggered via REST endpoint:  
`GET http://localhost:8090/kafka/bootstrap`

**single_mapped_packages**:Used to publish a single transformed package record.  
Triggered via REST endpoint:  
`GET http://localhost:8090/kafka/send/{id}`


## Continuous Data Ingestion Strategy

To continuously populate data into Kafka, several approaches can be considered.  
Below is a brief overview of common methods and the approach used in this project.

### Incremental Load (Polling-Based)
One approach is to perform incremental loads using a timestamp column such as `last_updated_at`.
On each run, the application fetches only records updated after the last processed timestamp.

**Limitations:**
- Captures only INSERT and UPDATE operations
- Hard deletes are not detected since deleted rows are physically removed from the table

---

### Trigger-Based Change Tracking
Another approach is to create database triggers on the `package` table for INSERT, UPDATE, and DELETE operations.
These triggers write change events into a separate `change_log` table, including the operation type and affected record.
The application periodically reads this table and processes new changes.

**Advantages:**
- Captures all data changes, including deletes

**Drawbacks:**
- Additional write overhead on the database
- Potential performance impact under high traffic
- Requires periodic cleanup of the change log table

---

### Log-Based CDC (Chosen Approach)
Modern CDC tools read database transaction logs directly (e.g., WAL in PostgreSQL) to capture changes in real time.
This approach avoids polling and triggers, and does not add extra load to application queries.

In this project, **Debezium** is used to implement a log-based CDC architecture:

- All INSERT / UPDATE / DELETE operations in PostgreSQL are captured from the transaction log
- Debezium streams these changes in real time to the Kafka topic:
  - `kafka_case.public.package`
- Debezium supports:
  - Initial snapshot (bootstrap)
  - Column-level filtering
  - Event filtering and transformation

The Java Spring Boot application consumes CDC events from this topic, transforms the records into
`MappedPackage` format, and publishes the results to:

- `mapped_package_cdc`

This design enables real-time, scalable, and reliable data streaming from PostgreSQL to Kafka
without using polling or database triggers.
