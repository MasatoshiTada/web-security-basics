package com.example.todobackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoBackendApplication {

    private static final Logger logger = LoggerFactory.getLogger(TodoBackendApplication.class);

    public static void main(String[] args) {
        logger.info("起動しました。");
        SpringApplication.run(TodoBackendApplication.class, args);
    }
}
