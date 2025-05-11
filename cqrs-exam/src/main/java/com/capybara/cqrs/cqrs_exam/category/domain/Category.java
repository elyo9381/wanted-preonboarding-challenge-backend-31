package com.capybara.cqrs.cqrs_exam.category.domain; // 패키지 경로 오타 가능성 인지

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<Category> children = new ArrayList<>();

    @Column(nullable = false)
    private Integer level;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    // 순환 참조를 막기 위해 Product와의 직접적인 연관관계는 ProductCategory를 통해 매핑

    public void addChild(Category child){
        child.setParent(this);
        child.setLevel(this.level+1);
        child.addChild(child);
    }
}
