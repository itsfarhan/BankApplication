package com.farhan.secure_banking_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(description = "Secure Banking Application", title = "Banking Application API", version = "1.0.0",

		contact = @Contact(name = "Farhan Ahmed", email = "farockzzzz@gmail.com", url = "https://github.com/itsfarhan"),

		license = @License(name = "Farhan Ahmed - Linkedin", url = "https://linkedin.com/in/itsfarhan")))
public class SecureBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureBankingApplication.class, args);
	}

}
