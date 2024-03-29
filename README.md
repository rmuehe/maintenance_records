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
  spring.application.name=maintenancerecords
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/maintenance_records
spring.datasource.username=username
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.format_sql=true


# loggers endpoint of Spring Actuator exposed
#management.endpoints.web.exposure.include=loggers
#management.endpoints.web.exposure.include=health,info,loggers
management.endpoints.web.exposure.include=*

# requires
#			<groupId>org.springframework.boot</groupId>
#			<artifactId>spring-boot-starter-actuator</artifactId>
#

# LOGGING
# Change logging level here to TRACE, DEBUG, INFO, WARN, ERROR
#Console logging
#logging.level.root=INFO

#logs print to resources/log/app.log
logging.file.name=log/appl.log
logging.file.path=logs
# FFile styles
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
#logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{1.} - %msg%n

# Log Console Styles
spring.output.ansi.enabled=always
logging.pattern.console=%clr(%d{yyyy-MM-dd HH:mm:ss}){faint} %clr([%5p]){magenta} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx

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

