package com.example.EmployeePayrollApp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j  // Lombok annotation to initialize Logger
@SpringBootApplication
public class EmployeePayrollAppApplication {
	public static void main(String[] args) {
		log.info("Application is starting... Logger is initialized.");
		log.debug("Debugging enabled - Logger is working.");

		SpringApplication.run(EmployeePayrollAppApplication.class, args);

		log.info("Application has started successfully!");
	}
}
