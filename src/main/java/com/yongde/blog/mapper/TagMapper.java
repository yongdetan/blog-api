package com.yongde.blog.mapper;

import com.yongde.blog.dto.request.CreateTagRequestDto;
import com.yongde.blog.dto.response.TagResponseDto;
import com.yongde.blog.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public Tag toEntity(CreateTagRequestDto createTagRequestDto) {
        return new Tag(createTagRequestDto.name());
    }

    public TagResponseDto toDto(Tag tag) {

        return new TagResponseDto(
                tag.getId(),
                tag.getName(),
                tag.getCreatedAt(),
                tag.getUpdatedAt());
    }
}
