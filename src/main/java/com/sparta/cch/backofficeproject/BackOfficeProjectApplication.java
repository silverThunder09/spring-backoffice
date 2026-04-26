package com.sparta.cch.backofficeproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackOfficeProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackOfficeProjectApplication.class, args);
    }

}
