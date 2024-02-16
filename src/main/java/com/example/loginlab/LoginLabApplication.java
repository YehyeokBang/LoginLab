package com.example.loginlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LoginLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginLabApplication.class, args);
    }

}
