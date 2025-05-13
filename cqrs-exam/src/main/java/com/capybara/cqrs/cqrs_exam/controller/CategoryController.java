package com.capybara.cqrs.cqrs_exam.controller;


import com.capybara.cqrs.cqrs_exam.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;

    public ResponseEntity<?> getAllCategories() {
        return null;
    }

    public ResponseEntity<?> getCategoryById(Long categoryId) {
        return null;
    }

    public ResponseEntity<?> getProduct() {
        return null;
    }

}
