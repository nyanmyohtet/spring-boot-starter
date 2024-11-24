# Spring Boot Start

Template for Spring Boot Projects

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
- Jasypt: ENV Variables en/decryption

## Run the Service

Change the encryption password in application's configuration file (`application-<profile>.properties`
or `application-<profile>.yml`):

```shell
jasypt.encryptor.password=encryptionPassword
```

```shell
mvn clean spring-boot:run
```

## Encryption - Jasypt

Encrypt credentials that are wrapped with `DEC(value)` in application.properties file.

!!! Use GitBash if you are on Windows.

```sh
mvn jasypt:encrypt -Djasypt.plugin.path="file:src/main/resources/application-dev.properties"
```

Decrypt credentials that are wrapped with `ENC(value)` in application.properties file.

```shell
mvn jasypt:decrypt -Djasypt.plugin.path="file:src/main/resources/application-dev.properties"
```

Run the Spring Boot application by passing the private key password as VM arguments in the command prompt like this:

```shell
java -Djasypt.plugin.path="file:src/main/resources/application-dev.properties" -Djasypt.encryptor.password=encryptionPassword -jar target/<service-name>-0.0.1-SNAPSHOT.jar
```

## Swagger

- http://localhost:8080/v2/api-docs
- http://localhost:8080/swagger-ui/
