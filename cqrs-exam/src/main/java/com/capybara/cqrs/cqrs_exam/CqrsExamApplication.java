package com.capybara.cqrs.cqrs_exam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CqrsExamApplication {

    private static final Logger log = LoggerFactory.getLogger(CqrsExamApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CqrsExamApplication.class, args);
    }
}
