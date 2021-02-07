package com.boot.demo;

import com.boot.demo.manipulator.crawlers.LocalFilesCrawler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.boot.demo.manipulator.processors")
@Slf4j
public class DemoApplication {
private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public LocalFilesCrawler localFilesCrawler(){
		return new LocalFilesCrawler();
	}


	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx)
	{

		return args ->
		{
			LocalFilesCrawler crawler = ctx.getBean(LocalFilesCrawler.class);
			logger.info("starting demo app");
			crawler.crawl(args[0]);
			logger.info("end");
			System.exit(0);
		};
	}
}
