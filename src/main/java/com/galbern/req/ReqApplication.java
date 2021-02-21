package com.galbern.req;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.websocket.OnError;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Calendar;


@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = "com.galbern.req.*")
@EnableRetry
@EnableScheduling // use for notifications when delayed approval & accountability submission
public class ReqApplication {
	Logger LOGGER = LoggerFactory.getLogger(ReqApplication.class);

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ReqApplication.class, args);
//		new Configuration().setInterceptor( new HibernateConf() );
//		ReqApplication.getSessionWithInterceptor(new HibernateConf());

	}

	@Bean
	public Hibernate5Module hibernate5Module()
	{
		return new Hibernate5Module();
	}

	@Scheduled(cron = "1/15 1/1 * * * ?")
	public void logMessage(){
		LOGGER.info("[GCW-RMS LIVE] "  + Calendar.getInstance().getTime());
	}

//	@OnError
//	public void onError(Session sess, Throwable e) {
//		Throwable cause = e.getCause();
//		/* normal handling... */
//		if (cause != null)
//			System.out.println("Error-info: cause->" + cause);
//		try {
//			// Likely EOF (i.e. user killed session)
//			// so just Close the input stream as instructed
//			sess.close();
//		} catch (IOException ex) {
//			System.out.println("Handling eof, A cascading IOException was caught: " + ex.getMessage());
//			ex.printStackTrace();
//		} finally {
//			System.out.println("Session error handled. (likely unexpected EOF) resulting in closing User Session.");
//
//		}
//	}
}
