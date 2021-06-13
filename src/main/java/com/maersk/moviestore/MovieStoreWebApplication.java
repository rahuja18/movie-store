package com.maersk.moviestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class MovieStoreWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieStoreWebApplication.class, args);
	}

}
