package com.yongde.blog.controller;

import com.yongde.blog.dto.request.CreateTagRequestDto;
import com.yongde.blog.dto.response.TagResponseDto;
import com.yongde.blog.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "api/v1/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<TagResponseDto> createTag(
            @Valid @RequestBody CreateTagRequestDto createTagRequestDto
    ) {

        TagResponseDto tagResponseDto = tagService.createTag(createTagRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{tagId}")
                .buildAndExpand(tagResponseDto.id())
                .toUri();

        return ResponseEntity.created(location).body(tagResponseDto);
    }
}
