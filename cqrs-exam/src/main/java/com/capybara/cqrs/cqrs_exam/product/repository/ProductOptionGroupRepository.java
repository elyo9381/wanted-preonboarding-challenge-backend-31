package com.capybara.cqrs.cqrs_exam.product.repository;


import com.capybara.cqrs.cqrs_exam.product.domain.Product;
import com.capybara.cqrs.cqrs_exam.product.domain.ProductOptionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionGroupRepository extends JpaRepository<ProductOptionGroup, Long> {
}
