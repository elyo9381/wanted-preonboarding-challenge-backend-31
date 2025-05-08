package com.capybara.cqrs.cqrs_exam;

import com.capybara.cqrs.cqrs_exam.user.application.UserService;
import com.capybara.cqrs.cqrs_exam.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CqrsExamApplication {

    private static final Logger log = LoggerFactory.getLogger(CqrsExamApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CqrsExamApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner demoUserFindAll(UserService userService) {
//        return args -> {
//            log.info("--- 모든 사용자 조회 시작 ---");
//            List<User> users = userService.getAllUsers();
//            if (users.isEmpty()) {
//                log.info("사용자가 없습니다.");
//            } else {
//                users.forEach(user -> log.info("사용자: {}", user.toString()));
//            }
//            log.info("--- 모든 사용자 조회 완료 ---");
//
//            // 애플리케이션 시작 시 테스트 사용자 1명 생성 (데이터가 없을 경우 확인용)
//            if (users.isEmpty()) {
//                log.info("--- 테스트 사용자 생성 ---");
//                User newUser = User.builder().name("App Runner User").email("apprunner@example.com").avatarUrl("avatar.png").build();
//                User createdUser = userService.createUser(newUser);
//                log.info("생성된 사용자: {}", createdUser);
//
//                log.info("--- 다시 모든 사용자 조회 ---");
//                List<User> currentUsers = userService.getAllUsers();
//                currentUsers.forEach(user -> log.info("사용자: {}", user.toString()));
//                log.info("--- 모든 사용자 조회 완료 ---");
//            }
//        };
//    }
}
