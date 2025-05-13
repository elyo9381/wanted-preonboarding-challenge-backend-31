package com.capybara.cqrs.cqrs_exam.controller.mapper;


import com.capybara.cqrs.cqrs_exam.category.domain.Category;
import com.capybara.cqrs.cqrs_exam.controller.dto.CategoryDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDTO.Category toCategoryResponse(Category category){
        return CategoryDTO.Category.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .level(category.getLevel())
                .imageUrl(category.getImageUrl())
                .children(category.getChildren().stream().map(this::toCategoryResponse).collect(Collectors.toList()))
                .build();
    }
}
