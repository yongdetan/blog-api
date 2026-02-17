package com.yongde.blog.controller;

import com.yongde.blog.dto.request.CreateCategoryRequestDto;
import com.yongde.blog.dto.response.CategoryResponseDto;
import com.yongde.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// admin only controller
@RestController
@RequestMapping(path = "api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {

        List<CategoryResponseDto> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }

    @GetMapping(path = "/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategory(
            @PathVariable Long categoryId
    ) {

        CategoryResponseDto categoryResponseDto = categoryService.getCategory(categoryId);

        return ResponseEntity.ok(categoryResponseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto
    ) {
        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(categoryId, createCategoryRequestDto);

        return ResponseEntity.ok(categoryResponseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long categoryId
    ) {
        categoryService.deleteCategory(categoryId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
