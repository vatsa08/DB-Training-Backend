package com.deutsche.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeutscheSpringbootDemoApplication {
	private static final Logger LOG = LoggerFactory.getLogger(DeutscheSpringbootDemoApplication.class);

	public static void main(String[] args) {
		LOG.info("Starting...");
		SpringApplication.run(DeutscheSpringbootDemoApplication.class, args);
		LOG.info("Started");

	}
}