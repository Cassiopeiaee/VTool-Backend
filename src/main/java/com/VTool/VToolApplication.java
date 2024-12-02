package com.VTool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example"})
public class VToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(VToolApplication.class, args);
	}

}
