package com.capybara.cqrs.cqrs_exam.user.application;

import com.capybara.cqrs.cqrs_exam.user.domain.User;
import com.capybara.cqrs.cqrs_exam.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 생성합니다.
@Transactional(readOnly = true) // 기본적으로 읽기 전용 트랜잭션, CUD 메소드에 별도 설정
public class UserService {

    private final UserRepository userRepository;

    @Transactional // 쓰기 작업이므로 readOnly = false (기본값)
    public User createUser(User user) {
        // 이메일 중복 검사 등 비즈니스 로직 추가 가능
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id)); // 간단한 예외 처리

        // 변경할 필드 업데이트
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail()); // 이메일 변경 시 중복 검사 필요
        user.setAvatarUrl(userDetails.getAvatarUrl());
        // createdAt은 변경하지 않음

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id); // 간단한 예외 처리
        }
        userRepository.deleteById(id);
    }
}
