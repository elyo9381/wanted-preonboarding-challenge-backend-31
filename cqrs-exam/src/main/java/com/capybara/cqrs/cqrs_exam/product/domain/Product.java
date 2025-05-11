package com.capybara.cqrs.cqrs_exam.product.domain;

import com.capybara.cqrs.cqrs_exam.brand.domain.Brand;
import com.capybara.cqrs.cqrs_exam.seller.domain.Seller;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    @Column(length = 500)
    private String shortDescription;

    @Lob
    private String fullDescription;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(nullable = false, length = 20)
    private String status;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private ProductDetail productDetail;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPrice> productPrices = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategory> productCategories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOptionGroup> productOptionGroups = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductTag> productTags = new ArrayList<>();

    // 편의 메서드 (양방향 연관관계 설정)
    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
        if (productDetail != null && productDetail.getProduct() != this) {
            productDetail.setProduct(this);
        }
    }

    public void addProductPrice(ProductPrice productPrice) {
        productPrices.add(productPrice);
        productPrice.setProduct(this);
    }

    public void removeProductPrice(ProductPrice productPrice) {
        productPrices.remove(productPrice);
        productPrice.setProduct(null);
    }

    public void addProductCategory(ProductCategory productCategory) {
        productCategories.add(productCategory);
        productCategory.setProduct(this);
    }

    public void removeProductCategory(ProductCategory productCategory) {
        productCategories.remove(productCategory);
        productCategory.setProduct(null);
    }

    public void addProductOptionGroup(ProductOptionGroup productOptionGroup) {
        productOptionGroups.add(productOptionGroup);
        productOptionGroup.setProduct(this);
    }

    public void removeProductOptionGroup(ProductOptionGroup productOptionGroup) {
        productOptionGroups.remove(productOptionGroup);
        productOptionGroup.setProduct(null);
    }

    public void addProductImage(ProductImage productImage) {
        productImages.add(productImage);
        productImage.setProduct(this);
    }

    public void removeProductImage(ProductImage productImage) {
        productImages.remove(productImage);
        productImage.setProduct(null);
    }

    public void addProductTag(ProductTag productTag) {
        productTags.add(productTag);
        productTag.setProduct(this);
    }

    public void removeProductTag(ProductTag productTag) {
        productTags.remove(productTag);
        productTag.setProduct(null);
    }
}
