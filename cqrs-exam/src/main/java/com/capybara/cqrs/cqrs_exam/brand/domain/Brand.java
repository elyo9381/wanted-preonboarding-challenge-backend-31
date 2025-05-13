package com.capybara.cqrs.cqrs_exam.brand.domain;

import com.capybara.cqrs.cqrs_exam.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(length = 1000)
    private String description;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(length = 255)
    private String website;

    @OneToMany(mappedBy = "brand" , cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    @Builder.Default
    private List<Product> products = new ArrayList<>();
}
