# Calculation-tool
This project was developed to assist the project planning process by creating a user-friendly overview of the 
project, as well as calculating the timeframe and estimated workdays required to finish the project.

## Project Description

The program can create projects with attached subprojects as well as tasks and assign these to specific employees.
The estimated hours based on the task details are then displayed in a project detail view along with the total workdays required.
To function, it requires valid authentication via email and password, and only project managers can create new tasks, subprojects, and projects.

### Technologies Used

- **Programming Languages**:
  - Java (Azul JDK 21)
  - HTML5
  - CSS3
    
- **Frameworks & Libraries**:
  - Spring Data JDBC 2024.1
  - Spring Web 3.4.0
  - Thymeleaf 3.1.2
  - Protocol Buffers 4.28.2 (for efficient data serialization)
    
- **Databases**:
  - H2 SQL (org.hibernate.dialect.H2Dialect for local development)
  - MySQL 8.0.29 (for production)
    
- **Build Tools**:
  - Maven 4.0.0 (for dependency management and build automation)
    
- **Other Tools**:
  - H2 2.3.232 (in-memory database for local testing/development)
  - Mockito 5.14.2 (for local testing)
  - Maven Surefire plugin 3.0.0-M5 (for debugging logs)
  - JUnit Jupiter 5.11 (for local unit testing)

## Installation

### Prerequisites

Before you can run this project, ensure that you have the following tools installed on your local machine:

- **Java 21 (Azul JDK)**: Ensure you have **JDK 21** installed. You can download it from [Azul JDK Downloads](https://www.azul.com/downloads/zulu/).
- **Maven**: Make sure you have **Maven 4.0.0** installed for managing dependencies and building the project. You can download it from [Maven's official site](https://maven.apache.org/download.cgi).
- **MySQL 8.0+** (if using the production setup): Download and install MySQL from [MySQL's official site](https://dev.mysql.com/downloads/).
- **H2 Database (optional for local testing)**: You may use **H2** for in-memory database during local development. [H2 download link](http://www.h2database.com/html/main.html).

### Steps to Install

**Clone the Repository**

Clone this project to your local machine using git:

git clone https://github.com/ajoeili/Eksamensprojekt

**Navigate to Project Directory**

cd main

**Configure Your Database**

- For H2 (Development): The application is pre-configured to use H2 for development by default. You don't need to change any configurations if you want to use H2.
- For MySQL (Production): Set up a MySQL database and update the application.properties file with your database credentials (e.g., database name, username, and password).

**Build the Project**

Use Maven to build the project and download dependencies:

mvn clean install

**Run the Application**

You can run the application using Maven:

mvn spring-boot:run

**Access the Application**

After starting the application, you can access the application via the browser at:

http://localhost:8080/calculation-tool/login

**Authentication (for Project Managers)**

To use the full functionality of the application, you need to log in with a valid email and password. Only project managers can create tasks, subprojects, and projects.
Test login: teje49@email.com // password2

**Authentication (for Employee)**

Test login: jajo40@email.com // password3

### Contribute

You are welcome contribute to the project! 
If you would like to help improve the project, please follow the guidelines in the CONTRIBUTING.md file.
