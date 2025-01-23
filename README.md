<h1 align="center">Spring Boot Starter</h1>

## :dart: About

Simple and scalable starter-kit to build powerful and organized REST projects with Spring Boot.

## :sparkles: Features

- [x] Logging
- [x] IP-based Rate Limiting using TokenBucket
- [x] En/decrypt ENV variables
- [x] Secure APIs using HTTPS
- [x] Exception Handling
- [ ] Organize custom error codes for consistency, clarity, and easy debugging

## :rocket: Tech Stack

- JDK: 17
- Maven: 2.x
- Spring: 3.x
  - Spring Boot
  - Spring Web
  - Spring Validation
  - Spring Security with JWT
  - Spring Data JPA
- MySQL: 8.4
- Lombok: 1.18

### Dependencies

- Spring Boot Modules:
  - Spring Web (for REST APIs)
  - Spring Validation (for input validation)
  - Spring Security (with JWT for authentication)
  - Spring Data JPA (with MySQL for datasource)
- Other Libs:
  - Lombok (simplify development with annotations)
  - Log4j2 (for high-performance logging)
  - [SpringFox](https://github.com/thingsboard/springfox) (generate Swagger API documentation and UI)
  - [bucket4j](https://github.com/bucket4j/bucket4j) (for IP-based Rate Limiting)
  - [Jasypt](https://github.com/ulisesbocchio/jasypt-spring-boot) (en/decrypt sensitive environment variables)

## :gear: Running the Service

### Development Setup

Run the application locally, pass `jasypt.encryptor.password` as Spring Boot Property.

```shell
mvn clean spring-boot:run -Dspring-boot.run.arguments="--jasypt.encryptor.password=encryption-password"
```

> **Note:** When accessing these URLs in development, you might see certificate warnings in your browser due to
> self-signed certificates. This is expected, and you can proceed safely for development purposes.

## :lock: Encryption with Jasypt

Jasypt ensures sensitive configuration values like database credentials and API keys are securely encrypted.

### Workflow

1. Add properties to be encrypted in `application.properties` with `DEC(value-here)`
2. Run the provided encrypt command to replace all `DEC(...)` values with their encrypted form

### Commands

#### Encrypt Properties
!!! Use GitBash if you are on Windows.

```sh
mvn jasypt:encrypt -Djasypt.plugin.path=file:src/main/resources/application-dev.properties -Djasypt.encryptor.password=encryption-password
```

#### Decrypt Properties

Decrypt credentials that are wrapped with `ENC(value)` in application.properties file.

```shell
mvn jasypt:decrypt -Djasypt.plugin.path=file:src/main/resources/application-dev.properties -Djasypt.encryptor.password=encryption-password
```

Which would output the decrypted contents to the screen, rather than editing the file in place.

#### Run packaged Jar with encrypted properties

Run the Spring Boot application by passing the private key password as VM arguments in the command prompt like this:

```shell
java -jar target/your-application.jar -Djasypt.encryptor.password=encryption-password
```

## :clipboard: Swagger

- [Swagger API Documentation - JSON](http://localhost:8080/v3/api-docs)
- [Swagger API Documentation - YAML](http://localhost:8080/v3/api-docs.yaml)
- [Swagger UI](http://localhost:8080/swagger-ui/index.html)

Swagger provides a user-friendly interface for interacting with your REST endpoints during development.

## Structure

### Package Organization

Structured the app with layered architecture to be modular, testable and scalable.
Can adjust base on specific project needs.

```shell
com.nyanmyohtet.springbootstarter
│
├── api/rest            // REST controllers
├── service             // Business logic
├── repository          // Data access logic (Spring Data JPA, etc.)
├── model               // Domain models/entities
├── dto                 // Data Transfer Objects
├── config              // Configuration classes (security, caching, etc.)
├── exception           // Custom exceptions and handlers
├── util                // Utility or helper classes
└── SpringBootApplication.java    // Main entry point
```

### Layer Responsibilities

- **Controller Layer**: Handles incoming HTTP request and map them to services.
- **Service Layer**: Contains business logic, avoid placing logic in controller.
- **Repository Layer**: Manages database interactions, usually using Spring Data JPA.
- **Model Layer**: Defines the structure of the data.
- **DTOs (Data Transfer Object)**: Defines data structure for communication between parties(e.g., client and server).

### Configuration

The `config` package hold:

- `application.properties`, `application-<profile>.properties`: define externalized configurations.
- **Security**: Spring Security setup.
- **Scheduler**: Scheduler config.
- **Custom beans**: defines beans for service wiring, third-party libs, etc.

### Exception Handling

- Place custom exceptions in `exception` package.
- use `@RestControllerAdvise` for centralized exception handling for REST endpoints.

### Testing

- Place tests in `src/test/java` with the same package structure as main code.
- Use JUnit 5 for testing, MockMvc for controller tests and Mockito for mocking dependencies.
