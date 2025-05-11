package com.capybara.cqrs.cqrs_exam.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_prices")
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(name = "base_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "sale_price", precision = 19, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "cost_price", precision = 19, scale = 2)
    private BigDecimal costPrice;

    @Column(length = 3, nullable = false)
    @Builder.Default
    private String currency = "KRW";

    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate;

    // 양방향 연관관계 편의 메서드
    public void setProduct(Product product) {
        this.product = product;
        // Product 엔티티의 productPrices 리스트에도 추가/제거 로직이 필요 (Product 엔티티에 이미 구현됨)
    }
}
