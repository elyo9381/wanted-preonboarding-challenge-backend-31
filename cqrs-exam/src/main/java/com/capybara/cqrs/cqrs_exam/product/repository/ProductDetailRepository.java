package com.capybara.cqrs.cqrs_exam.product.repository;


import com.capybara.cqrs.cqrs_exam.product.domain.Product;
import com.capybara.cqrs.cqrs_exam.product.domain.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
