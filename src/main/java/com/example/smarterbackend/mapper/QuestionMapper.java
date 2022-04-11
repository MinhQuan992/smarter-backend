package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.question.QuestionResponse;
import com.example.smarterbackend.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuestionMapper {
  QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

  @Mapping(target = "groupId", source = "group.id")
  QuestionResponse questionToQuestionDTO(Question question);
  List<QuestionResponse> listQuestionToListQuestionDTO(List<Question> questions);
}
