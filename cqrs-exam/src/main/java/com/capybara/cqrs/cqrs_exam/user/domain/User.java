package com.capybara.cqrs.cqrs_exam.user.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.ToString; // 추가
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Getter
@Setter // CQRS 패턴에서는 Setter를 지양하거나 Command를 통해 상태 변경을 명시적으로 할 수 있습니다. 우선 기본적으로 추가합니다.
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString // 추가
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
