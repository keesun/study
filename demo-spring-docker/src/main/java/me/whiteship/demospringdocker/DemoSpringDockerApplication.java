package me.whiteship.demospringdocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoSpringDockerApplication {

	@GetMapping("/")
	public String hello() {
		return "Hello Spring Boot";
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringDockerApplication.class, args);
	}
}
