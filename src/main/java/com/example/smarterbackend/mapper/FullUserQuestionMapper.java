package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.question.FullUserQuestionResponse;
import com.example.smarterbackend.model.UserQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FullUserQuestionMapper {
  FullUserQuestionMapper INSTANCE = Mappers.getMapper(FullUserQuestionMapper.class);

  @Mapping(target = "authorId", source = "author.id")
  FullUserQuestionResponse userQuestionToFullUserQuestionDTO(UserQuestion userQuestion);
}
