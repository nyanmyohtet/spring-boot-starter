# Spring Boot Start

Template for Spring Boot Projects

## Features

- [x] En/decrypt ENV variables

## Tech Stack

- JDK: 17
- Maven: 2.x
- Spring Boot: 2.x
- MySQL: 8.4

### Dependencies

- Spring Boot
- Spring Web
- Spring Validation
- Spring Security with JWT
- Spring Data JPA with MySQL
- Lombok
- Log4j2: for application logging
- SpringFox: for API Specification | Swagger UI
- bucket4j: for IP-based Rate Limiting
- [Jasypt](https://github.com/ulisesbocchio/jasypt-spring-boot): ENV Variables en/decryption

## Run the Service

> Need to pass `jasypt.encryptor.password` as Spring Boot Property.

```shell
mvn clean spring-boot:run -Dspring-boot.run.arguments="--jasypt.encryptor.password=encryption-password"
```

## Encryption with Jasypt

### Workflow

1. Add properties to be encrypted in `application.properties` with `DEC(value-here)`
2. Run the encrypt command to replace all `DEC(...)` values with their encrypted form

### Commands

#### Encrypt Properties
!!! Use GitBash if you are on Windows.

```sh
mvn jasypt:encrypt -Djasypt.plugin.path="file:src/main/resources/application-dev.properties -Djasypt.encryptor.password=encryption-password"
```

#### Decrypt Properties

Decrypt credentials that are wrapped with `ENC(value)` in application.properties file.

```shell
mvn jasypt:decrypt -Djasypt.plugin.path="file:src/main/resources/application-dev.properties -Djasypt.encryptor.password=encryption-password"
```

Which would output the decrypted contents to the screen, rather than editing the file in place.

#### Run packaged Jar with encrypted properties

Run the Spring Boot application by passing the private key password as VM arguments in the command prompt like this:

```shell
java -jar target/your-application.jar -Djasypt.encryptor.password=encryption-password
```

## Swagger

- http://localhost:8080/v2/api-docs
- http://localhost:8080/swagger-ui/
