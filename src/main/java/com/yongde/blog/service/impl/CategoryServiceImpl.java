package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.CreateCategoryRequestDto;
import com.yongde.blog.dto.response.CategoryResponseDto;
import com.yongde.blog.entity.Category;
import com.yongde.blog.exception.CategoryNameExistsException;
import com.yongde.blog.exception.CategoryNotFoundException;
import com.yongde.blog.mapper.CategoryMapper;
import com.yongde.blog.repository.CategoryRepository;
import com.yongde.blog.service.CategoryService;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto getCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto updateCategory(Long categoryId, CreateCategoryRequestDto createCategoryRequestDto) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        category.setName(createCategoryRequestDto.name());
        category.setDescription(createCategoryRequestDto.description());
        category.setUpdatedAt(Instant.now());

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        categoryRepository.delete(category);
    }
}
