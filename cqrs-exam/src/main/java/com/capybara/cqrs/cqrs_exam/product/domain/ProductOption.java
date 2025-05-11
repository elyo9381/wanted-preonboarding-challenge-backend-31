package com.capybara.cqrs.cqrs_exam.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_options")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id", nullable = false)
    private ProductOptionGroup optionGroup;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "additional_price", precision = 19, scale = 2)
    private BigDecimal additionalPrice;

    @Column(length = 100,nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>();

    // 양방향 연관관계 편의 메서드
    public void setOptionGroup(ProductOptionGroup optionGroup) {
        this.optionGroup = optionGroup;
    }

    public void addProductImage(ProductImage productImage) {
        productImages.add(productImage);
        productImage.setOption(this);
    }

    public void removeProductImage(ProductImage productImage) {
        productImages.remove(productImage);
        productImage.setOption(null);
    }
}
