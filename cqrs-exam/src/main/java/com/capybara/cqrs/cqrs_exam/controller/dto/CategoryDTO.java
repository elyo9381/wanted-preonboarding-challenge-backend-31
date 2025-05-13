package com.capybara.cqrs.cqrs_exam.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Category {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private Integer level;
        private String imageUrl;
        @Builder.Default
        List<Category> children = new ArrayList<>();
    }

}
