//package com.capybara.cqrs.cqrs_exam.user.service;
//
//import com.capybara.cqrs.cqrs_exam.user.domain.User;
//import com.capybara.cqrs.cqrs_exam.user.domain.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class) // Mockito 확장 사용
//class UserServiceTest  {
//
//    @Mock // Mock 객체 생성
//    private UserRepository userRepository;
//
//    @InjectMocks // @Mock으로 생성된 객체를 주입받는 객체 생성
//    private UserService userService;
//
//    private User user1;
//    private User user2;
//
//    @BeforeEach
//    void setUp() {
//        user1 = User.builder()
//                .id(1L)
//                .name("Test User 1")
//                .email("test1@example.com")
//                .avatarUrl("url1")
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        user2 = User.builder()
//                .id(2L)
//                .name("Test User 2")
//                .email("test2@example.com")
//                .avatarUrl("url2")
//                .createdAt(LocalDateTime.now())
//                .build();
//    }
//
//    @DisplayName("사용자 생성 테스트")
//    @Test
//    void createUser() {
//        // given
//        given(userRepository.save(any(User.class))).willReturn(user1);
//
//        // when
//        User createdUser = userService.createUser(user1);
//
//        // then
//        assertThat(createdUser).isNotNull();
//        assertThat(createdUser.getName()).isEqualTo(user1.getName());
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @DisplayName("ID로 사용자 조회 테스트")
//    @Test
//    void getUserById() {
//        // given
//        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
//
//        // when
//        Optional<User> foundUser = userService.getUserById(1L);
//
//        // then
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getEmail()).isEqualTo(user1.getEmail());
//    }
//
//    @DisplayName("ID로 사용자 조회 실패 테스트 (사용자 없음)")
//    @Test
//    void getUserById_notFound() {
//        // given
//        given(userRepository.findById(3L)).willReturn(Optional.empty());
//
//        // when
//        Optional<User> foundUser = userService.getUserById(3L);
//
//        // then
//        assertThat(foundUser).isNotPresent();
//    }
//
//    @DisplayName("이메일로 사용자 조회 테스트")
//    @Test
//    void getUserByEmail() {
//        // given
//        given(userRepository.findByEmail("test1@example.com")).willReturn(Optional.of(user1));
//
//        // when
//        Optional<User> foundUser = userService.getUserByEmail("test1@example.com");
//
//        // then
//        assertThat(foundUser).isPresent();
//        assertThat(foundUser.get().getName()).isEqualTo(user1.getName());
//    }
//
//    @DisplayName("모든 사용자 조회 테스트")
//    @Test
//    void getAllUsers() {
//        // given
//        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2));
//
//        // when
//        List<User> users = userService.getAllUsers();
//
//        // then
//        assertThat(users).hasSize(2);
//        assertThat(users).contains(user1, user2);
//    }
//
//    @DisplayName("사용자 정보 수정 테스트")
//    @Test
//    void updateUser() {
//        // given
//        User updatedDetails = User.builder()
//                .name("Updated User")
//                .email("updated@example.com")
//                .avatarUrl("updated_url")
//                .build();
//
//        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
//        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
//
//
//        // when
//        User updatedUser = userService.updateUser(1L, updatedDetails);
//
//        // then
//        assertThat(updatedUser).isNotNull();
//        assertThat(updatedUser.getName()).isEqualTo("Updated User");
//        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
//        assertThat(updatedUser.getAvatarUrl()).isEqualTo("updated_url");
//        // createdAt은 변경되지 않아야 함
//        assertThat(updatedUser.getCreatedAt()).isEqualTo(user1.getCreatedAt());
//        verify(userRepository, times(1)).save(any(User.class));
//    }
//
//    @DisplayName("사용자 정보 수정 실패 테스트 (사용자 없음)")
//    @Test
//    void updateUser_notFound() {
//        // given
//        User updatedDetails = User.builder().name("Updated").build();
//        given(userRepository.findById(3L)).willReturn(Optional.empty());
//
//        // when & then
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            userService.updateUser(3L, updatedDetails);
//        });
//        assertThat(exception.getMessage()).isEqualTo("User not found with id: 3");
//        verify(userRepository, never()).save(any(User.class));
//    }
//
//    @DisplayName("사용자 삭제 테스트")
//    @Test
//    void deleteUser() {
//        // given
//        given(userRepository.existsById(1L)).willReturn(true);
//        doNothing().when(userRepository).deleteById(1L); // void 메소드 mock
//
//        // when
//        userService.deleteUser(1L);
//
//        // then
//        verify(userRepository, times(1)).deleteById(1L);
//    }
//
//    @DisplayName("사용자 삭제 실패 테스트 (사용자 없음)")
//    @Test
//    void deleteUser_notFound() {
//        // given
//        given(userRepository.existsById(3L)).willReturn(false);
//
//        // when & then
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            userService.deleteUser(3L);
//        });
//        assertThat(exception.getMessage()).isEqualTo("User not found with id: 3");
//        verify(userRepository, never()).deleteById(anyLong());
//    }
//}
