package com.url_shortner.url_shortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class UrlShortnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortnerApplication.class, args);
	}

}
