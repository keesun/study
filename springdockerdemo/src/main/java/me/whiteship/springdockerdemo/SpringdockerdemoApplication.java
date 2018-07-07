package me.whiteship.springdockerdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringdockerdemoApplication {

    @GetMapping("/")
    public String hello() {
        return "Hello Spring Boot";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringdockerdemoApplication.class, args);
    }
}
