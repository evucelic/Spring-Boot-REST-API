package com.example.springbootrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // ne treba nam baza podataka za ovaj zadatak
public class SpringBootRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestApplication.class, args);
	}

}
