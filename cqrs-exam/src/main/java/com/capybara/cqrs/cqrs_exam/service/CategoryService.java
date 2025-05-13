package com.capybara.cqrs.cqrs_exam.service;

import com.capybara.cqrs.cqrs_exam.controller.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {


    List<CategoryDTO.Category> getAllCategories(Integer lever);
}
