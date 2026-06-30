package com.pms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.pms")
@EnableAsync
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		
		logger.info("Main Entry Point Class");
		
		SpringApplication.run(Application.class, args);
	}
	
	 // ← ADD THIS DIRECTLY HERE FOR TESTING
//    @Scheduled(fixedRate = 5000)
//    public void testScheduler() {
//        System.out.println("✅ TEST SCHEDULER RUNNING - " + java.time.LocalDateTime.now());
//    }
	
	
}
