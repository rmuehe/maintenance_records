# Maintenance Records Application

## Overview
This Spring Boot application helps manage maintenance records and item usages, 
allowing users to keep track of various items, their usage, and maintenance over time.

## Prerequisites
- JDK 17 or later
- Maven 3.2+
- MySQL Server(for production)
- H2 Database Engine (for testing)

## Setup
**Clone the repository:**

```
git clone https://github.com/rmuehe/maintenance_records

cd maintenancerecords
```


**Configure MySQL (for production):**
   Update `src/main/resources/application.properties` with your MySQL user and password:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/maintenance_records
   spring.datasource.username=<your_username>
   spring.datasource.password=<your_password>
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```
Ensure the empty database `maintenance_records` exists in your MySQL server.

```mysql> CREATE DATABASE maintenance_records;```

**Configure H2 (for testing):**
Create `src/test/resources/application.properties` 
   ```properties
spring.application.name=maintenancerecords-test
# Use a random port for testing
server.port=1000
# H2 DataSource configuration
spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
# JPA and Hibernate properties for H2
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
# Drop and re-create the schema on each test run
spring.jpa.hibernate.ddl-auto=create-drop
# optional SQL statement console logging
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
   ```


**Run the application:**
   ```
   mvn spring-boot:run
   ```
   The application will start running at [http://localhost:8080](http://localhost:8080).

## Testing
The application uses H2, an in-memory database for testing. No additional setup is required for testing purposes. Run tests using Maven:
```
mvn test
```

## Contributing
Feel free to fork the project and submit pull requests. 

## License
[MIT](https://choosealicense.com/licenses/mit/)

