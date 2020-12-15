package com.galbern.req;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class ReqApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReqApplication.class, args);
//		System.setProperty("server.servlet.context-path", ""); //
	}

	@Bean
	public Hibernate5Module hibernate5Module()
	{
		return new Hibernate5Module();
	}

}
