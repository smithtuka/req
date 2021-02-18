package com.galbern.req;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Calendar;


@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = "com.galbern.req.*")
@EnableRetry
@EnableScheduling // use for notifications when delayed approval & accountability submission
public class ReqApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReqApplication.class, args);
	}

	@Bean
	public Hibernate5Module hibernate5Module()
	{
		return new Hibernate5Module();
	}

	@Scheduled(cron = "1/5 1/1 * * * ?")
	public void logMessage(){
		System.out.println("[GCW-RMS LIVE] "  + Calendar.getInstance().getTime());
	}

}
