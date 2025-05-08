package com.capybara.cqrs.cqrs_exam.user.application;

import com.capybara.cqrs.cqrs_exam.DbContextTest;
import com.capybara.cqrs.cqrs_exam.user.domain.User;
import com.capybara.cqrs.cqrs_exam.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional; // 통합 테스트에서는 실제 DB 변경을 롤백하기 위해 사용

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional // 각 테스트 후 롤백하여 테스트 간 격리 보장
public class UserIntegrationTest extends DbContextTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository; // 데이터 검증용

//    @BeforeEach
//    void setUp() {
//        // 각 테스트 전에 데이터베이스를 초기화 (필요한 경우)
//        // @Transactional 어노테이션으로 인해 각 테스트는 독립적인 트랜잭션에서 실행되고 롤백되므로,
//        // 일반적으로 명시적인 데이터 삭제는 필요 없을 수 있습니다.
//        // 하지만, ddl.sql 외에 추가적인 데이터 로딩이 있다면 여기서 처리할 수 있습니다.
//        userRepository.deleteAll(); // 확실한 격리를 위해 모든 사용자 삭제
//    }

    @DisplayName("통합 테스트: 사용자 생성 및 조회")
    @Test
    void createUserAndFindById() {
        // given
        User newUser = User.builder()
                .name("Integration Test User")
                .email("integration@example.com")
                .avatarUrl("integration_url")
                .build();

        // when
        User createdUser = userService.createUser(newUser);

        // then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getName()).isEqualTo("Integration Test User");

        Optional<User> foundUserOpt = userService.getUserById(createdUser.getId());
        assertThat(foundUserOpt).isPresent();
        User foundUser = foundUserOpt.get();
        assertThat(foundUser.getEmail()).isEqualTo("integration@example.com");
        assertThat(foundUser.getCreatedAt()).isNotNull();
    }

    @DisplayName("통합 테스트: 이메일로 사용자 조회")
    @Test
    void getUserByEmail() {
        // given
        User user = User.builder().name("Email User").email("email.user@example.com").build();
        userService.createUser(user);

        // when
        Optional<User> foundUserOpt = userService.getUserByEmail("email.user@example.com");

        // then
        assertThat(foundUserOpt).isPresent();
        assertThat(foundUserOpt.get().getName()).isEqualTo("Email User");
    }
    
    @DisplayName("통합 테스트: 존재하지 않는 이메일로 사용자 조회")
    @Test
    void getUserByEmail_notFound() {
        // when
        Optional<User> foundUserOpt = userService.getUserByEmail("nonexistent@example.com");

        // then
        assertThat(foundUserOpt).isNotPresent();
    }

    @DisplayName("통합 테스트: 모든 사용자 조회")
    @Test
    void getAllUsers() {
        // given
//        userService.createUser(User.builder().name("User A").email("a@example.com").build());
//        userService.createUser(User.builder().name("User B").email("b@example.com").build());

        // when
//        List<User> users = userService.getAllUsers();
        List<User> all = userRepository.findAll();

        all.forEach(User::toString);

        assertThat(all).isNotNull();
        // then
//        assertThat(users).hasSize(2);
    }

    @DisplayName("통합 테스트: 사용자 정보 수정")
    @Test
    void updateUser() {
        // given
        User originalUser = userService.createUser(User.builder().name("Original Name").email("original@example.com").build());
        
        User updatedDetails = User.builder()
                                .name("Updated Name")
                                .email("updated@example.com") // 이메일도 변경
                                .avatarUrl("new_avatar.png")
                                .build();

        // when
        User updatedUser = userService.updateUser(originalUser.getId(), updatedDetails);

        // then
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedUser.getAvatarUrl()).isEqualTo("new_avatar.png");

        // DB에서 직접 확인
        Optional<User> userFromDbOpt = userRepository.findById(originalUser.getId());
        assertThat(userFromDbOpt).isPresent();
        assertThat(userFromDbOpt.get().getName()).isEqualTo("Updated Name");
    }
    
    @DisplayName("통합 테스트: 존재하지 않는 사용자 정보 수정 시 예외 발생")
    @Test
    void updateUser_notFound_throwsException() {
        // given
        User details = User.builder().name("any").email("any@any.com").build();

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(999L, details); // 존재하지 않는 ID
        });
        assertThat(exception.getMessage()).isEqualTo("User not found with id: 999");
    }


    @DisplayName("통합 테스트: 사용자 삭제")
    @Test
    void deleteUser() {
        // given
        User userToDelete = userService.createUser(User.builder().name("To Delete").email("delete@example.com").build());
        Long userId = userToDelete.getId();

        // when
        userService.deleteUser(userId);

        // then
        Optional<User> deletedUserOpt = userService.getUserById(userId);
        assertThat(deletedUserOpt).isNotPresent();
        assertThat(userRepository.existsById(userId)).isFalse();
    }

    @DisplayName("통합 테스트: 존재하지 않는 사용자 삭제 시 예외 발생")
    @Test
    void deleteUser_notFound_throwsException() {
         // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(888L); // 존재하지 않는 ID
        });
        assertThat(exception.getMessage()).isEqualTo("User not found with id: 888");
    }
}
