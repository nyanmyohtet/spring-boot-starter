package com.nyanmyohtet.springbootstarter;

import org.springframework.boot.SpringApplication;

// @EnableJpaAuditing // TODO: enable JPA Auditing
@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }

}
