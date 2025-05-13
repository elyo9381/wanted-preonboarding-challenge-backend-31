package com.capybara.cqrs.cqrs_exam.product.repository;


import com.capybara.cqrs.cqrs_exam.product.domain.Product;
import com.capybara.cqrs.cqrs_exam.product.domain.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
}
