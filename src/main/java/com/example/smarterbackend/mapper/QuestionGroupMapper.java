package com.example.smarterbackend.mapper;

import com.example.smarterbackend.framework.dto.questiongroup.QuestionGroupResponse;
import com.example.smarterbackend.model.QuestionGroup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuestionGroupMapper {
  QuestionGroupMapper INSTANCE = Mappers.getMapper(QuestionGroupMapper.class);

  QuestionGroupResponse questionGroupToQuestionGroupDTO(QuestionGroup questionGroup);
  List<QuestionGroupResponse> listQuestionGroupToListQuestionGroupDTO(List<QuestionGroup> questionGroups);
}
