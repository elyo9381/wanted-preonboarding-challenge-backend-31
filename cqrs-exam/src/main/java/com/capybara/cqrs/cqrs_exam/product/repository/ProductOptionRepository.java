package com.capybara.cqrs.cqrs_exam.product.repository;


import com.capybara.cqrs.cqrs_exam.product.domain.Product;
import com.capybara.cqrs.cqrs_exam.product.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
