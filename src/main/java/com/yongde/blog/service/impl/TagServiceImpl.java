package com.yongde.blog.service.impl;

import com.yongde.blog.dto.request.CreateTagRequestDto;
import com.yongde.blog.dto.response.TagResponseDto;
import com.yongde.blog.entity.Tag;
import com.yongde.blog.exception.TagNameExistsException;
import com.yongde.blog.mapper.TagMapper;
import com.yongde.blog.repository.TagRepository;
import com.yongde.blog.service.TagService;
import org.springframework.stereotype.Service;


@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public TagResponseDto createTag(CreateTagRequestDto createTagRequestDto) {

        //normalizing the user input tag name for standardization.
        String tagName = createTagRequestDto.name().toLowerCase();

        //check if the tag name already exists.
        if (tagRepository.findTagByName(tagName).isPresent()) {
            throw new TagNameExistsException(tagName);
        }

        Tag tag = tagMapper.toEntity(createTagRequestDto);

        tag.setName(tagName);

        Tag savedTag = tagRepository.save(tag);
        return tagMapper.toDto(savedTag);
    }
}
