package com.yongde.blog.service;

import com.yongde.blog.dto.request.CreateTagRequestDto;
import com.yongde.blog.dto.response.TagResponseDto;

public interface TagService {

    TagResponseDto createTag(CreateTagRequestDto createTagRequestDto);
}
