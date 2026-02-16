package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.CreateCategoryRequestDto;
import com.yongde.blog.dto.response.CategoryResponseDto;
import com.yongde.blog.entity.Category;
import com.yongde.blog.exception.CategoryNameExistsException;
import com.yongde.blog.mapper.CategoryMapper;
import com.yongde.blog.repository.CategoryRepository;
import com.yongde.blog.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponseDto createCategory(CreateCategoryRequestDto createCategoryRequestDto) {

        //Normalizing category name. all category name should be in lower case for standardization.
        String categoryName = createCategoryRequestDto.name().toLowerCase();

        if (categoryRepository.findByName(categoryName).isPresent()) {
            throw new CategoryNameExistsException(categoryName);
        }

        Category category = categoryMapper.toEntity(createCategoryRequestDto);

        //normalizing category name
        category.setName(categoryName);

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return List.of();
    }

    @Override
    public CategoryResponseDto getCategory(Long categoryId) {
        return null;
    }

    @Override
    public CategoryResponseDto updateCategory(Long categoryId) {
        return null;
    }

    @Override
    public void deleteCategory(Long categoryId) {

    }
}
