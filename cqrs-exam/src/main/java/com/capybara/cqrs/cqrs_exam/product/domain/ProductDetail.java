package com.capybara.cqrs.cqrs_exam.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_details")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(columnDefinition = "jsonb")
    private String dimensions; // JSONB

    @Lob
    private String materials;

    @Column(name = "country_of_origin", length = 100)
    private String countryOfOrigin;

    @Lob
    private String warrantyInfo;

    @Lob
    private String careInstructions;

    @Column(name = "additional_info", columnDefinition = "jsonb")
    private String additionalInfo; // JSONB

    // 양방향 연관관계 편의 메서드
    public void setProduct(Product product) {
        this.product = product;
        // Product 엔티티의 productDetail 필드도 설정 (Product 엔티티에 해당 메서드가 있다면)
        // if (product != null && product.getProductDetail() != this) {
        //     product.setProductDetail(this);
        // }
    }
}
