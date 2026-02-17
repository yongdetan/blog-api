package com.yongde.blog.service;

import com.yongde.blog.dto.request.CreateCategoryRequestDto;
import com.yongde.blog.dto.response.CategoryResponseDto;

import java.util.List;

public interface CategoryService {


    CategoryResponseDto createCategory(CreateCategoryRequestDto createCategoryRequestDto);

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategory(Long categoryId);

    CategoryResponseDto updateCategory(Long categoryId, CreateCategoryRequestDto createCategoryRequestDto);

    void deleteCategory(Long categoryId);

}
