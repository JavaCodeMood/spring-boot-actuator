package com.lhf.springboot.springbootactuator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//http://localhost:8081/actuator
@SpringBootApplication
public class SpringBootActuatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootActuatorApplication.class, args);
	}

}
