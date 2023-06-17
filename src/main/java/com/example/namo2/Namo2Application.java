package com.example.namo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Namo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Namo2Application.class, args);
    }

}
