package me.whiteship.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	@GetMapping("/")
	public String hello() {
		return "Hello Spring";
	}

	@PostMapping("/post")
    public String post() {
	    return "post";
    }

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
