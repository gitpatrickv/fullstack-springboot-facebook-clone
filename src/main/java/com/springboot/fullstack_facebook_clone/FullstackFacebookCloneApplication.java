package com.springboot.fullstack_facebook_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class FullstackFacebookCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(FullstackFacebookCloneApplication.class, args);
	}

}
