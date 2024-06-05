# Olympics Microservices Project

This repository contains four independent microservices developed using Maven and Spring Boot. Each microservice is executed independently and connected to a database that matches the models used within the microservice. The microservices may interact with each other to recover model data. For example, the Schedules microservice interacts with the Sports and Sites microservices.

## Microservices Overview

1. **Sports Service**
   - Port: 8082
   - Manages sports-related data.
   - CRUD operations for sports.

2. **Sites Service**
   - Port: 8081
   - Manages site-related data.
   - CRUD operations for sites.
   - Advanced functionalities like defining relationships between sites and calculating total travel times.

3. **Schedules Service**
   - Port: 8083
   - Manages event schedules.
   - CRUD operations for schedules.
   - Advanced search functionalities to find sports and sites by date and/or time.

4. **Planning Service**
   - Port: 8084
   - Manages user planning data.
   - CRUD operations for planning.

## Project Setup and Execution

### Prerequisites

- IntelliJ IDEA (recommended for running the project)
- Maven
- Java 8 or higher
- Databases (configured as per each microservice's requirements in `resources/application.properties`)

### Running the Project

Each microservice can be run independently. Alternatively, you can configure IntelliJ to run all four services simultaneously. This is possible because in the root project, we defined the different modules inside the `pom.xml` file, which helps IntelliJ recognize the four microservices as part of the same root project.
