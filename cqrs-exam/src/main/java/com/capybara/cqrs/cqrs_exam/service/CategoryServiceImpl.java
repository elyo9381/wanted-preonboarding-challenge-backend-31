package com.capybara.cqrs.cqrs_exam.service;

import com.capybara.cqrs.cqrs_exam.category.domain.Category;
import com.capybara.cqrs.cqrs_exam.category.domain.CategoryRepository;
import com.capybara.cqrs.cqrs_exam.controller.dto.CategoryDTO;
import com.capybara.cqrs.cqrs_exam.controller.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    void getProduct(Long categoryId) {}
    void getCategoryById(Long categoryId) {}

    @Override
    public List<CategoryDTO.Category> getAllCategories(Integer level) {

        if( level != null ) {


            List<Category> categories = categoryRepository.findAll();


        } else {
            return buildCategoryHierarcy();
        }


        return List.of();
    }

    private List<CategoryDTO.Category> buildCategoryHierarcy() {

        List<Category> allCategories = categoryRepository.findAll();

        Map<Long, Category> categoryMap = allCategories.stream()
                .collect(Collectors.toMap(Category::getId, category -> category));

        HashMap<Long, List<Category>> childrenMap = new HashMap<>();

        for (Category category : allCategories) {
            if(category.getParent() != null) {
                Long parentId = category.getParent().getId();
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
            }
        }

        List<Category> rootCategories = allCategories.stream()
                .filter(category -> category.getLevel() == 1)
                .toList();


        return rootCategories.stream().map(root -> buildCategoty(root,childrenMap)).toList();
    }

    private CategoryDTO.Category buildCategoty(Category category, HashMap<Long, List<Category>> childrenMap) {
        CategoryDTO.Category responseDTO = categoryMapper.toCategoryResponse(category);

        List<Category> children = childrenMap.getOrDefault(category.getId(), new ArrayList<>());
        if(!children.isEmpty()) {
            List<CategoryDTO.Category> childs = children.stream()
                    .map(child -> buildCategoty(child, childrenMap))
                    .toList();
            responseDTO.setChildren(childs);
        }

        return responseDTO;
    }
}
