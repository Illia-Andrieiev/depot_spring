package com.Illia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    private static final Logger logger = LogManager.getLogger(Application.class);
    public static void main(String[] args) {
        logger.info("run spring");
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // Указываем, что это Spring Boot приложение для Tomcat
        logger.info("point app for tomcat");
        return builder.sources(Application.class);
    }
}
