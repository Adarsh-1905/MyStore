# MyStore Backend

Spring Boot 3 backend application for MyStore e-commerce platform.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 (for production)

## Technologies Used

- **Framework**: Spring Boot 3.1.5
- **Build Tool**: Maven
- **Database**: H2 (development), MySQL (production)
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA/Hibernate
- **Utilities**: Lombok

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/mystore/
│   │   │   ├── MyStoreApplication.java
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/
│   │   │   ├── config/
│   │   │   └── exception/
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
├── pom.xml
└── README.md
```

## Getting Started

### 1. Install Dependencies

```bash
cd backend
mvn clean install
```

### 2. Run Application

```bash
mvn spring-boot:run
```

The application will start at `http://localhost:8080`

### 3. Check Health

```bash
curl http://localhost:8080/api/health
```

## Database Configuration

### Development (H2 In-Memory)

H2 console is available at: `http://localhost:8080/h2-console`

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (leave blank)

### Production (MySQL)

Update `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mystore
    username: root
    password: your_password
    driverClassName: com.mysql.cj.jdbc.Driver
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: validate
```

## API Endpoints

### Health Check

```bash
GET /api/health
```

## Build for Production

```bash
mvn clean package -DskipTests
java -jar target/backend-1.0.0.jar
```

## CORS Configuration

By default, CORS is enabled for:
- `http://localhost:4200` (Angular)
- `http://localhost:3000` (React)

Modify `SecurityConfig.java` to add more origins.

## License

MIT
