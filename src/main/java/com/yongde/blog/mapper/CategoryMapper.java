package com.yongde.blog.mapper;

import com.yongde.blog.dto.request.CreateCategoryRequestDto;
import com.yongde.blog.dto.response.CategoryResponseDto;
import com.yongde.blog.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CreateCategoryRequestDto createCategoryRequestDto) {
        return new Category(createCategoryRequestDto.name(), createCategoryRequestDto.description());
    }

    public CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getPosts(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

}
