package com.yongde.blog.controller;

import com.yongde.blog.dto.request.CreateCategoryRequestDto;
import com.yongde.blog.dto.response.CategoryResponseDto;
import com.yongde.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// admin only controller
@RestController
@RequestMapping(path = "api/v1/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto
    ) {
        CategoryResponseDto categoryResponseDto = categoryService.createCategory(createCategoryRequestDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{categoryId}")
                .buildAndExpand(categoryResponseDto.id())
                .toUri();

        return ResponseEntity.created(uri).body(categoryResponseDto);
    }
}
