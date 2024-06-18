# fia-weather

Small project that allow to view weather data for any Formula 1 Grand Prix. It includes a Spring Boot application, H2 database, Airflow, InfluxDB, and a Streamlit frontend. 

The project setup and configuration are managed using Maven and Docker Compose.

## Prerequisites

Ensure you have the following installed:

- Java 17 or higher
- Maven 3.6.3 or higher
- Docker
- Docker Compose

## Building the Spring Boot Application

To build the Spring Boot application, navigate to the `backend/rest-server` directory and run the following command:

```bash
mvn clean install
```

This will compile the project and package it into a JAR file located in the `target/` directory.


## Running the stack with Docker Compose

Navigate to the project root directory and run:
```bash
docker-compose up --build
```

This command will start the following services:

 - H2 Database (storing F1 schedule data): Accessible at http://localhost:8090/h2-console (JDBC URL: jdbc:h2:mem:dcbapp, username: sa, no password)
 - Airflow (ETL DAG to fetch data from OpenMeteo): Accessible at http://localhost:8080/home (username: airflow, password: airflow)
 - InfluxDB (Storing the interpolated time series weather data): Accessible at http://localhost:8086/ (username: admin, password: adminpassword)
 - Streamlit Frontend: Accessible at http://localhost:8501/